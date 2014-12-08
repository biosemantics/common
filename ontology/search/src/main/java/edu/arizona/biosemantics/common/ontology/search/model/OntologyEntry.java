package edu.arizona.biosemantics.common.ontology.search.model;

public class OntologyEntry implements Comparable<OntologyEntry> {

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

	public int compareTo(OntologyEntry o) {
		return new Double(this.score).compareTo(new Double(o.score));
	}
	
	
}
