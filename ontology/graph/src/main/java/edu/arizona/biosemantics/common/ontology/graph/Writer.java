package edu.arizona.biosemantics.common.ontology.graph;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import edu.arizona.biosemantics.common.ontology.AnnotationProperty;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

public class Writer {

	private OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager();
	private OWLAnnotationProperty labelProperty = 
			owlOntologyManager.getOWLDataFactory()
			.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
	private OWLAnnotationProperty subclassOfProperty = 
			owlOntologyManager.getOWLDataFactory()
			.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_SUBCLASS_OF.getIRI());
	private OWLObjectProperty partOfProperty = owlOntologyManager.getOWLDataFactory().getOWLObjectProperty(IRI.create(AnnotationProperty.PART_OF.getIRI()));
	private OWLAnnotationProperty synonymProperty = owlOntologyManager.getOWLDataFactory().getOWLAnnotationProperty(IRI.create(AnnotationProperty.SYNONYM.getIRI()));
	private OWLAnnotationProperty definitionProperty = owlOntologyManager.getOWLDataFactory().getOWLAnnotationProperty(IRI.create(AnnotationProperty.DEFINITION.getIRI()));
	private OWLAnnotationProperty creationDateProperty = owlOntologyManager.getOWLDataFactory().getOWLAnnotationProperty(IRI.create(AnnotationProperty.CREATION_DATE.getIRI()));
	private OWLAnnotationProperty createdByProperty = owlOntologyManager.getOWLDataFactory().getOWLAnnotationProperty(IRI.create(AnnotationProperty.CREATED_BY.getIRI()));
	private OWLAnnotationProperty relatedSynonymProperty = owlOntologyManager.getOWLDataFactory().getOWLAnnotationProperty(IRI.create(AnnotationProperty.RELATED_SYNONYM.getIRI()));
	private OWLAnnotationProperty narrowSynonymProperty = owlOntologyManager.getOWLDataFactory().getOWLAnnotationProperty(IRI.create(AnnotationProperty.NARROW_SYNONYM.getIRI()));
	private OWLAnnotationProperty exactSynonymProperty = owlOntologyManager.getOWLDataFactory().getOWLAnnotationProperty(IRI.create(AnnotationProperty.EXACT_SYNONYM.getIRI()));
	private OWLAnnotationProperty broadSynonymProperty = owlOntologyManager.getOWLDataFactory().getOWLAnnotationProperty(IRI.create(AnnotationProperty.BROAD_SYNONYM.getIRI()));
	private String inputOntologyFile;
	private String outputGraphFile;
	
	public Writer(String inputOntologyFile, String outputGraphFile) {
		this.inputOntologyFile = inputOntologyFile;
		this.outputGraphFile = outputGraphFile;
	}
	
	public void write() throws Exception {
		DirectedSparseMultigraph<String, Edge> graph = createGraph();
		serializeGraph(graph);
	}
	
	private void serializeGraph(DirectedSparseMultigraph<String, Edge> graph) throws IOException {
		try(FileOutputStream fileOutputStream = new FileOutputStream(new File(outputGraphFile))) {
			try(ObjectOutputStream out = new ObjectOutputStream(fileOutputStream)) {
				out.writeObject(graph);
				out.flush();
			}
		}
	}

	private DirectedSparseMultigraph<String, Edge> createGraph() throws OWLOntologyCreationException {
		File ontologyFile = new File(inputOntologyFile);
		OWLOntology owlOntology = owlOntologyManager.loadOntologyFromOntologyDocument(ontologyFile);
		DirectedSparseMultigraph<String, Edge> graph = new DirectedSparseMultigraph<String, Edge>();
		
		Set<OWLClass> owlClasses = owlOntology.getClassesInSignature(Imports.EXCLUDED);

		//assumption: no duplicate labels exist in the ontology. 
		//If they did, the relations would be merged
		for(OWLClass owlClass : owlClasses) {
			String label = this.getLabel(owlOntology, owlClass);
			
			if(label != null && !label.isEmpty()) {
				graph.addVertex(label);
				
				Set<String> superclasses = getSuperclasses(owlOntology, owlClass);
				for(String superclass : superclasses) {
					graph.addVertex(superclass);
					graph.addEdge(new Edge(Edge.Type.SUBCLASS_OF), superclass, label);
					System.out.println(label + " ---subclass-of---> " +  superclass);
				}
				
				Set<String> parents = getParents(owlOntology, owlClass);
				for(String parent : parents) {
					graph.addVertex(parent);
					graph.addEdge(new Edge(Edge.Type.PART_OF), label, parent);
					System.out.println(label + " ---part-of---> " +  parent);
				}
				
				Set<String> synonyms = getSynonyms(owlOntology, owlClass);
				for(String synonym : synonyms) {
					System.out.println(synonym + " ---synonym-of---> " +  label);
					graph.addVertex(synonym);
					graph.addEdge(new Edge(Edge.Type.SYNONYM_OF), synonym, label);
					System.out.println(synonym + " ---synonym-of---> " +  label);
				}
				
				/*Set<OWLClassAxiom> classAxioms = owlOntology.getAxioms(owlClass, Imports.EXCLUDED);
				for(OWLClassAxiom axiom : classAxioms) {
					if(axiom instanceof OWLSubClassOfAxiom) {
						OWLSubClassOfAxiom owlSubClassOfAxiom = (OWLSubClassOfAxiom)axiom;
						OWLClassExpression owlClassExpression = owlSubClassOfAxiom.getSuperClass();
						
						if(owlSubClassOfAxiom.getSubClass().equals(owlClass) && owlClassExpression.getObjectPropertiesInSignature().contains(synonymProperty)) {
							if(owlClassExpression instanceof OWLObjectSomeValuesFrom) {
								OWLObjectSomeValuesFrom owlObjectSomeValuesFrom = (OWLObjectSomeValuesFrom)owlClassExpression;
								OWLClassExpression filler = owlObjectSomeValuesFrom.getFiller();
								if(filler instanceof OWLClass) {
									OWLClass synonym = (OWLClass)filler;
									String synonymLabel = iriLabelMap.get(synonym.getIRI().toString());
									if(synonymLabel != null && !synonymLabel.isEmpty()) {
										g.addVertex(synonymLabel);
										g.addEdge(label, synonymLabel, Relationship.SYNONYM);
										
										System.out.println(label + " ---synonym---> " +  synonymLabel);
									}
								}
							}
						}
						
						if(owlSubClassOfAxiom.getSubClass().equals(owlClass) && owlClassExpression.getObjectPropertiesInSignature().contains(partOfProperty)) {
							if(owlClassExpression instanceof OWLObjectSomeValuesFrom) {
								OWLObjectSomeValuesFrom owlObjectSomeValuesFrom = (OWLObjectSomeValuesFrom)owlClassExpression;
								OWLClassExpression filler = owlObjectSomeValuesFrom.getFiller();
								if(filler instanceof OWLClass) {
									OWLClass parent = (OWLClass)filler;
									String parentLabel = iriLabelMap.get(parent.getIRI().toString());
									if(parentLabel != null && !parentLabel.isEmpty()) {
										g.addVertex(parentLabel);
										g.addEdge(label, parentLabel, Relationship.PART_OF);
										
										System.out.println(label + " ---part-of---> " +  parentLabel);
									}
								}
							}
						}
					}
				}	*/
			}
		}
		return graph;
	}

	private Set<String> getParents(OWLOntology owlOntology, OWLClass owlClass) {
		Set<String> result = new HashSet<String>();
		Collection<OWLClassExpression> superclasses = EntitySearcher.getSuperClasses(owlClass, owlOntology);
		for(OWLClassExpression superclass : superclasses) {
			if(superclass instanceof OWLObjectSomeValuesFrom) {
				OWLObjectSomeValuesFrom owlObjectSomeValuesFrom = (OWLObjectSomeValuesFrom)superclass;
				if(owlObjectSomeValuesFrom.getObjectPropertiesInSignature().contains(partOfProperty)) {
					OWLClassExpression filler = owlObjectSomeValuesFrom.getFiller();
					if(filler instanceof OWLClass) {
						OWLClass parent = (OWLClass)filler;
						String parentLabel = getLabel(owlOntology, parent);
						if(parentLabel != null && !parentLabel.isEmpty()) 
							result.add(parentLabel);
					}
				}
			}
		}
		return result;
	}
	
	private Set<String> getSuperclasses(OWLOntology owlOntology, OWLClass owlClass) {
		Collection<OWLClassExpression> superclasses = EntitySearcher.getSuperClasses(owlClass, owlOntology);
		Set<String> result = new HashSet<String>();
		for(OWLClassExpression superclass : superclasses) {
			if(superclass instanceof OWLClass) {
				String superclassLabel = getLabel(owlOntology, ((OWLClass)superclass));
				if(superclassLabel != null && !superclassLabel.isEmpty()) 
					result.add(superclassLabel);
			}
		}
		return result;
	}
	
	private String getLabel(OWLOntology owlOntology, OWLClass owlClass) {
		for (OWLAnnotation annotation : EntitySearcher.getAnnotations(owlClass, owlOntology, labelProperty)) {
			if (annotation.getValue() instanceof OWLLiteral) {
				OWLLiteral val = (OWLLiteral) annotation.getValue();
				//if (val.hasLang("en")) {
				return val.getLiteral();
				//}
			}
		}
		return null;
	}
	
	private Set<String> getSynonyms(OWLOntology owlOntology, OWLClass owlClass) {
		Set<String> result = new HashSet<String>();
		for(OWLAnnotationAssertionAxiom axiom : EntitySearcher.getAnnotationAssertionAxioms(owlClass, owlOntology)) {
			if(axiom.getProperty().equals(exactSynonymProperty) || axiom.getProperty().equals(broadSynonymProperty) || axiom.getProperty().equals(narrowSynonymProperty) || 
					axiom.getProperty().equals(relatedSynonymProperty) || axiom.getProperty().equals(synonymProperty)) {
				OWLAnnotationValue annotationValue = axiom.getValue();
				if(annotationValue instanceof OWLLiteral) {
					String value = ((OWLLiteral) annotationValue).getLiteral();
					result.add(value);
				}
			}
		}
		return result;
	}
	
	private OWLClass getOWLClass(String classIRI) {
		return owlOntologyManager.getOWLDataFactory().getOWLClass(IRI.create(classIRI));
	}
	
}
