package edu.arizona.biosemantics.common.ontology.graph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashSet;
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
import edu.arizona.biosemantics.common.ontology.graph.OntologyGraph.Edge;
import edu.arizona.biosemantics.common.ontology.graph.OntologyGraph.Vertex;

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
		OntologyGraph graph = createGraph();
		serializeGraph(graph);
	}
	
	private void serializeGraph(OntologyGraph graph) throws IOException {
		try(FileOutputStream fileOutputStream = new FileOutputStream(new File(outputGraphFile))) {
			try(ObjectOutputStream out = new ObjectOutputStream(fileOutputStream)) {
				out.writeObject(graph);
				out.flush();
			}
		}
	}

	private OntologyGraph createGraph() throws OWLOntologyCreationException {
		File ontologyFile = new File(inputOntologyFile);
		OWLOntology owlOntology = owlOntologyManager.loadOntologyFromOntologyDocument(ontologyFile);
		OntologyGraph graph = new OntologyGraph();
		
		Set<OWLClass> owlClasses = owlOntology.getClassesInSignature(Imports.EXCLUDED);

		//assumption: no duplicate labels exist in the ontology. 
		//If they did, the relations would be merged
		for(OWLClass owlClass : owlClasses) {
			String label = this.getLabel(owlOntology, owlClass);
			
			if(label != null && !label.isEmpty()) {
				Vertex v = new Vertex(label, owlClass.getIRI().toString());
				graph.addVertex(v);
				
				Set<Vertex> superclasses = getSuperclasses(owlOntology, owlClass);
				for(Vertex superclass : superclasses) {
					graph.addVertex(superclass);
					graph.addRelation(new Edge(superclass, v, Edge.Type.SUBCLASS_OF));
					//System.out.println(label + " ---subclass-of---> " +  superclass);
				}
				
				Set<Vertex> parents = getParents(owlOntology, owlClass);
				for(Vertex parent : parents) {
					graph.addVertex(parent);
					graph.addRelation(new Edge(parent, v, Edge.Type.PART_OF));
					//System.out.println(label + " ---part-of---> " +  parent);
				}
				
				Set<String> synonyms = getSynonyms(owlOntology, owlClass);
				for(String syn : synonyms) {
					//System.out.println(synonym + " ---synonym-of---> " +  label);
					Vertex synonym = new Vertex(syn, "");
					graph.addVertex(synonym);
					graph.addRelation(new Edge(v, synonym, Edge.Type.SYNONYM_OF));
					//System.out.println(synonym + " ---synonym-of---> " +  label);
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

	private Set<Vertex> getParents(OWLOntology owlOntology, OWLClass owlClass) {
		Set<Vertex> result = new HashSet<Vertex>();
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
							result.add(new Vertex(parentLabel, parent.getIRI().toString()));
					}
				}
			}
		}
		return result;
	}
	
	private Set<Vertex> getSuperclasses(OWLOntology owlOntology, OWLClass owlClass) {
		Collection<OWLClassExpression> superclasses = EntitySearcher.getSuperClasses(owlClass, owlOntology);
		Set<Vertex> result = new HashSet<Vertex>();
		for(OWLClassExpression superclass : superclasses) {
			if(superclass instanceof OWLClass) {
				String superclassLabel = getLabel(owlOntology, ((OWLClass)superclass));
				if(superclassLabel != null && !superclassLabel.isEmpty()) 
					result.add(new Vertex(superclassLabel, ((OWLClass)superclass).getIRI().toString()));
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
