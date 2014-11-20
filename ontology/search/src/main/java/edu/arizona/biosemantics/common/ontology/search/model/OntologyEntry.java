package edu.arizona.biosemantics.common.ontology.search.model;

public class OntologyEntry {

	private String ontology;
	private String iri;
	
	public OntologyEntry(String ontology, String iri) {
		super();
		this.ontology = ontology;
		this.iri = iri;
	}

	public String getOntology() {
		return ontology;
	}

	public String getClassIRI() {
		return iri;
	}
	
	
}
