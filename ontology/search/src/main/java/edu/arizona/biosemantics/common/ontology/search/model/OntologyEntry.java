package edu.arizona.biosemantics.common.ontology.search.model;

public class OntologyEntry {

	private double score;
	private Ontology ontology;
	private String iri;
	
	public OntologyEntry(Ontology ontology, String iri, double score) {
		super();
		this.ontology = ontology;
		this.iri = iri;
		this.score = score;
	}

	public Ontology getOntology() {
		return ontology;
	}

	public String getClassIRI() {
		return iri;
	}
	
	
}
