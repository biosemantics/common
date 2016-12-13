package edu.arizona.biosemantics.common.ontology.graph;

public enum Ontology {
	
	BSPO("bspo", "http://purl.obolibrary.org/obo/bspo.owl"), 
	CARO("caro", "http://purl.obolibrary.org/obo/caro/src/caro.obo.owl"),
	CHEBI("chebi", "http://purl.obolibrary.org/obo/chebi.owl"),
	CL("cl", "http://purl.obolibrary.org/obo/cl.owl"), 
	RO("ro", "http://purl.obolibrary.org/obo/ro.owl"),
	PATO("pato", "http://purl.obolibrary.org/obo/pato.owl"),
	PO("po", "http://purl.obolibrary.org/obo/po.owl"),
	HAO("hao", "http://purl.obolibrary.org/obo/hao.owl"),
	ENVO("envo", "http://purl.obolibrary.org/obo/envo.owl"),
	GO("go", "http://purl.obolibrary.org/obo/go.owl"),
	UBERON("uberon", "http://purl.obolibrary.org/obo/uberon.owl"),
	PORO("poro", "http://purl.obolibrary.org/obo/poro.owl"),
	ModifierOntology("ModifierOntology", "http://biosemantics.arizona.edu/ontologies/ModifierOntology.owl");
	
	private String name;
	private String iri;
	
	private Ontology(String name, String iri) {
		this.name = name;
		this.iri = iri;
	}

	public String getName() {
		return name;
	}

	public String getIri() {
		return iri;
	}
	
	
}