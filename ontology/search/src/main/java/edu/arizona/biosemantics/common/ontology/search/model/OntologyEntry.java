package edu.arizona.biosemantics.common.ontology.search.model;

public class OntologyEntry implements Comparable<OntologyEntry> {

	public static enum Type {
		ENTITY
	}
	
	private double score;
	private Ontology ontology;
	private String iri;
	private Type type;
	
	public OntologyEntry(Ontology ontology, String iri, Type type, double score) {
		super();
		this.ontology = ontology;
		this.iri = iri;
		this.type = type;
		this.score = score;
	}

	public Ontology getOntology() {
		return ontology;
	}

	public String getClassIRI() {
		return iri;
	}
	
	public double getScore() {
		return score;
	}

	public Type getType() {
		return type;
	}

	public int compareTo(OntologyEntry o) {
		return new Double(this.score).compareTo(new Double(o.score));
	}
	
	
}
