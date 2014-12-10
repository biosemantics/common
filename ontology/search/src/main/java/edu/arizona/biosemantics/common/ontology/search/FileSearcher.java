package edu.arizona.biosemantics.common.ontology.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.common.ontology.search.model.Ontology;
import edu.arizona.biosemantics.common.ontology.search.model.OntologyEntry;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.OntologyLookupClient;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.data.Entity;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.data.EntityProposals;

public class FileSearcher implements Searcher {

	private OntologyLookupClient ontologyLookupClient;
	private Ontology ontology;

	public FileSearcher(Ontology ontology, String ontologyDir, String dictDir) {
		this.ontology = ontology;
		try {
			this.ontologyLookupClient = new OntologyLookupClient(ontology.toString().toLowerCase(), ontologyDir, dictDir);
		} catch(Throwable t) {
			log(LogLevel.ERROR, "Problem invoking oto-common code", t);
		}
	}
	
	public List<OntologyEntry> getEntries(String term) {
		List<OntologyEntry> result = new ArrayList<OntologyEntry>();
		
		//Only search structures for now leveraging ontologylookup client
		//This is all construction zone to find out use cases of a Searcher of ontologies we have
		try {
			List<EntityProposals> entityProposals = this.ontologyLookupClient.searchStrucutre(term);
			if(entityProposals != null && !entityProposals.isEmpty()) {
				for(Entity entity : entityProposals.get(0).getProposals()) {
					result.add(new OntologyEntry(ontology, entity.getClassIRI(), (double)entity.getConfidenceScore()));
				}
			}
		} catch(Throwable t) {
			log(LogLevel.ERROR, "Problem invoking oto-common code", t);
		}
		
		Collections.sort(result);
		return result;
	}

}
