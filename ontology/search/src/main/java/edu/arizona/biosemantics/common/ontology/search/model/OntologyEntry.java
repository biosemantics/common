package edu.arizona.biosemantics.common.ontology.search.model;

public class OntologyEntry implements Comparable<OntologyEntry> {

	public static enum Type {
		ENTITY, QUALITY
	}
	
	private double score;
	private Ontology ontology;
	private String iri;
	private Type type;
	private String label;
	private String parentLabel;
	private String matchType;
	private String definition;
	
	public OntologyEntry(Ontology ontology, String iri, Type type, double score, String label, String definition, String parentLabel, String matchType) {
		super();
		this.ontology = ontology;
		this.iri = iri;
		this.type = type;
		this.score = score;
		this.label = label;
		this.parentLabel = parentLabel;
		this.matchType = matchType;
		this.definition = definition;
	}
	
	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getLabel(){
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getParentLabel() {
		return parentLabel;
	}

	public void setParentLabel(String parentLabel) {
		this.parentLabel = parentLabel;
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
	
	public String getMatchType() {
		return matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

	public int compareTo(OntologyEntry o) {
		return new Double(this.score).compareTo(new Double(o.score));
	}
	
	
}
