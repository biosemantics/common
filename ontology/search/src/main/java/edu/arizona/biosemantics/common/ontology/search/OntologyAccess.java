package edu.arizona.biosemantics.common.ontology.search;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import edu.arizona.biosemantics.common.log.LogLevel;

public class OntologyAccess {

	private Set<OWLOntology> ontologies;
	private OWLDataFactory owlDataFactory;
	private HashMap<OWLOntology, OWLReasoner> ontologyReasonerMap;
	private OWLObjectProperty partof;
	private OWLObjectProperty haspart;
	
	public OntologyAccess(Set<OWLOntology> ontologies) {
		this.ontologies = ontologies;
		
		OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager();
		owlDataFactory = owlOntologyManager.getOWLDataFactory();
		partof = owlDataFactory.getOWLObjectProperty(IRI.create("http://purl.obolibrary.org/obo/BFO_0000050"));
		haspart = owlDataFactory.getOWLObjectProperty(IRI.create("http://purl.obolibrary.org/obo/BFO_0000051"));
		
		OWLReasonerFactory reasonerFactory = new ElkReasonerFactory();
		ontologyReasonerMap = new HashMap<OWLOntology, OWLReasoner>();
		for(OWLOntology ontology : ontologies)
			ontologyReasonerMap.put(ontology, reasonerFactory.createReasoner(ontology));
	}
	
	public OWLEntity getOWLEntityForIRI(String iri) {
		for(OWLOntology ontology : ontologies) {
			Set<OWLEntity> result = ontology.entitiesInSignature(IRI.create(iri)).collect(Collectors.toSet());
			if(!result.isEmpty())
				return result.iterator().next();
		}
		return null;
	}
	
	public String getLabel(OWLClass owlClass) {
		Set<OWLAnnotation> labels = this.getLabels(owlClass);
		if(!labels.isEmpty()) {
			OWLAnnotation label = (OWLAnnotation) labels.iterator().next();
			return this.getRefinedOutput(label.getValue().toString());
		}
		return null;
	}
	
	public Set<OWLAnnotation> getLabels(OWLClass c) {
		return this.getAnnotationByIRI(c, OWLRDFVocabulary.RDFS_LABEL.getIRI());
	}
	
	public Set<OWLAnnotation> getAnnotationByIRI(OWLClass owlClass, IRI iri) {
		Set<OWLAnnotation> result = new HashSet<OWLAnnotation>();
		for(OWLOntology ontology : ontologies){
			//result.addAll(owlClass.getAnnotations(ontology, owlDataFactory.getOWLAnnotationProperty(iri)));
			result.addAll(EntitySearcher.getAnnotationObjects(owlClass, ontology, owlDataFactory.getOWLAnnotationProperty(iri)).collect(Collectors.toSet()));
		}
		return result;
	}
	
	public Set<OWLClass> getAncestors(OWLClass owlClass) {
		Set<OWLClass> result = new HashSet<OWLClass>();
		for(OWLReasoner reasoner : ontologyReasonerMap.values()) {
			NodeSet<OWLClass> ancestorNodes = reasoner.getSuperClasses(owlClass, false);
			
			for(Node<OWLClass> ancestorNode : ancestorNodes) {
				OWLClass ancestor = ancestorNode.getRepresentativeElement();
				if(!ancestor.isBottomEntity() && !ancestor.isTopEntity())
					result.add(ancestor);
			}
		}
		return result;
	}
	
	public Set<OWLClass> getDescendants(OWLClass owlClass) {
		Set<OWLClass> result = new HashSet<OWLClass>();
		for(OWLReasoner reasoner : ontologyReasonerMap.values()) {
			NodeSet<OWLClass> descendantNodes = reasoner.getSubClasses(owlClass, false);
			
			for(Node<OWLClass> descendantNode : descendantNodes) {
				OWLClass descendant = descendantNode.getRepresentativeElement();
				if(!descendant.isBottomEntity() && !descendant.isTopEntity())
					result.add(descendant);
			}
		}
		return result;
	}
	
	/**
	 * getParts and getBearers may not find all part relationships (even when using the reasoner)
	 * when ontologies don't supply reciprical relations consistently while using part_of and has_part relations are seperate relations (not inverse of each other --they are inverse relations strictly speaking)
	 * @param owlClass
	 * @return owlClass that has_part argument owlclass
	 */
	public Set<OWLClass> getBearers(OWLClass owlClass) {
		Set<OWLClass> result = new HashSet<OWLClass>();
		for(OWLReasoner reasoner : ontologyReasonerMap.values()) {
			OWLClassExpression bearersClass = owlDataFactory.getOWLObjectSomeValuesFrom(haspart, owlClass);// ? has_part some owlclass
			NodeSet<OWLClass> bearerNodes = reasoner.getSubClasses(bearersClass, false);
			
			for(Node<OWLClass> bearerNode : bearerNodes) {
				OWLClass bearer = bearerNode.getRepresentativeElement();
				if(!bearer.isBottomEntity() && !bearer.isTopEntity())
					result.add(bearer);
			}
				
		}
		return result;
	}
	
	/**
	 * 
	 * @param owlClass
	 * @return owlClass that are part_of argument owlclass
	 */
	public Set<OWLClass> getParts(OWLClass owlClass) {
		Set<OWLClass> result = new HashSet<OWLClass>();
		for(OWLReasoner reasoner : ontologyReasonerMap.values()) {
			OWLClassExpression partClass = owlDataFactory.getOWLObjectSomeValuesFrom(partof, owlClass);//// ? part_of some owlclass
			NodeSet<OWLClass> partNodes = reasoner.getSubClasses(partClass, false);
			
			for(Node<OWLClass> partNode : partNodes) {
				OWLClass part = partNode.getRepresentativeElement();
				if(!part.isBottomEntity() && !part.isTopEntity())
					result.add(part);
			}
				
		}
		return result;
	}
	
	/**
	 * Remove the non-readable or non-meaningful characters in the retrieval
	 * from OWL API, and return the refined output.
	 *
	 * @param origin the origin??? what does it look like?
	 * @return the refined output ??? what does it look like??
	 */
	public String getRefinedOutput(String origin) {
		// Annotation(<http://www.geneontology.org/formats/oboInOwl#hasExactSynonym>
		// W)
		if (origin.startsWith("Annotation")) {
			origin = origin.replaceFirst("^Annotation.*>\\s+", "")
					.replaceFirst("\\)\\s*$", "").trim();
		}

		/*
		 * Remove the ^^xsd:string tail from the returned annotation value
		 */
		return origin.replaceAll("\\^\\^xsd:string", "").replaceAll("\"", "")
				.replaceAll("\\.", "").replaceFirst("@.*", ""); //remove lang info @en
	}
	
	//possibly want to cache the read in results once, similar to old ontologysearcher / termsearcher
	public IRI getIRIForLabel(String label) {
		for(OWLOntology ontology : ontologies) {
			for (OWLClass owlClass : ontology.classesInSignature().collect(Collectors.toSet())) {
				Stream<OWLAnnotation> annotationStream = EntitySearcher.getAnnotationObjects(owlClass, ontology, owlDataFactory.getRDFSLabel());
				Iterator<OWLAnnotation> it = annotationStream.iterator();
				while(it.hasNext()){
					OWLAnnotation annotation = it.next();
					if (annotation.getValue() instanceof OWLLiteral) {
						OWLLiteral val = (OWLLiteral) annotation.getValue();
						if(val.getLiteral().equals(label)) {
							return owlClass.getIRI();
						}
					}
				}
			}
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		Set<OWLOntology> ontologies = new HashSet<OWLOntology>();
		OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager();
		//OWLDataFactory df = owlOntologyManager.getOWLDataFactory();
		
		File ontologyDirectory = new File("C:/Users/hongcui/Documents/etcsite/resources/shared/ontologies");
		for(File ontologyFile : ontologyDirectory.listFiles()) {
			try {
				OWLOntology ontology = owlOntologyManager.loadOntologyFromOntologyDocument(ontologyFile);
				ontologies.add(ontology);
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			}
		}
		OntologyAccess ontologyAccess = new OntologyAccess(ontologies);
		//test case 1
		System.out.println("red IRI = "+ontologyAccess.getIRIForLabel("red"));
		
		//test case 2
		System.out.println();
		System.out.println("OWL Entity of red = " +ontologyAccess.getOWLEntityForIRI("http://purl.obolibrary.org/obo/PATO_0000322"));
					
		OWLEntity anther = ontologyAccess.getOWLEntityForIRI("http://purl.obolibrary.org/obo/PO_0009066");
		//test case 3	
		Set<OWLClass> ancestors = ontologyAccess.getAncestors((OWLClass)anther);
		int i = 0;
		System.out.println();
		System.out.println("anther's superclasses include: ");
		for(OWLClass ancestor: ancestors){
			System.out.println((i++)+". "+ontologyAccess.getLabel(ancestor));
		}
		
		//test case 4
		Set<OWLClass> descendants = ontologyAccess.getDescendants((OWLClass)anther);
		i = 0;
		System.out.println();
		System.out.println("anther's subclasses include: ");
		for(OWLClass descendant: descendants){
			System.out.println((i++)+". "+ontologyAccess.getLabel(descendant));
		}

		//test case 5
		//anther (0009066) part_of stamen (0009029) part_of  androecium (0009061)
		Set<OWLClass> parents = ontologyAccess.getBearers((OWLClass)anther);
		i = 0;
		System.out.println();
		System.out.println("anther is part of : ");
		for(OWLClass parent: parents){
			System.out.println((i++)+". "+ontologyAccess.getLabel(parent));
		}

		OWLEntity fruit = ontologyAccess.getOWLEntityForIRI("http://purl.obolibrary.org/obo/PO_0009001");
		parents = ontologyAccess.getBearers((OWLClass)fruit);
		i = 0;
		System.out.println();
		System.out.println("fruit is part of : ");
		for(OWLClass parent: parents){
			System.out.println((i++)+". "+ontologyAccess.getLabel(parent));
		}
		
		//test case 6
		Set<OWLClass> parts = ontologyAccess.getParts((OWLClass)anther);
		i = 0;
		System.out.println();
		System.out.println("anther has following parts: ");
		for(OWLClass part: parts){
			System.out.println((i++)+". "+ontologyAccess.getLabel(part));
		}
	}
}
