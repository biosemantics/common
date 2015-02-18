package edu.arizona.biosemantics.common.ontology.search;

import java.util.List;

import edu.arizona.biosemantics.common.ontology.search.model.OntologyEntry;
import edu.arizona.biosemantics.common.ontology.search.model.OntologyEntry.Type;

public interface Searcher {

	public List<OntologyEntry> getEntries(String term);
	
	public List<OntologyEntry> getEntries(String term, Type type);
	
}
