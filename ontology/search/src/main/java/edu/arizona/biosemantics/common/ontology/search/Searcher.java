package edu.arizona.biosemantics.common.ontology.search;

import java.util.List;

import edu.arizona.biosemantics.common.ontology.search.model.OntologyEntry;

public interface Searcher {

	public List<OntologyEntry> getEntries(String term);
	
}
