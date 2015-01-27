package edu.arizona.biosemantics.common.ontology.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.common.ontology.search.model.Ontology;
import edu.arizona.biosemantics.common.ontology.search.model.OntologyEntry;
import edu.arizona.biosemantics.common.ontology.search.model.OntologyEntry.Type;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.OntologyLookupClient;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.data.Entity;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.data.EntityProposals;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.data.FormalConcept;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.search.TermSearcher;

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
		/*try {
			List<EntityProposals> entityProposals = this.ontologyLookupClient.searchStrucutre(term);
			if(entityProposals != null && !entityProposals.isEmpty()) {
				for(Entity entity : entityProposals.get(0).getProposals()) {
					result.add(new OntologyEntry(ontology, entity.getClassIRI(), entity.get(double)entity.getConfidenceScore()));
				}
			}
		} catch(Throwable t) {
			log(LogLevel.ERROR, "Problem invoking oto-common code", t);
		}*/
		
		Collections.sort(result);
		return result;
	}

	public List<OntologyEntry> getEntries(String term, Type type) {
		List<OntologyEntry> result = new ArrayList<OntologyEntry>();
		
		TermSearcher termSearcher = new TermSearcher(ontologyLookupClient);
		ArrayList<FormalConcept> concepts = termSearcher.searchTerm(term, type.toString().toLowerCase());
		if(concepts != null)
			for(FormalConcept concept : concepts) 
				result.add(new OntologyEntry(ontology, concept.getClassIRI(), type, concept.getConfidenceScore()));
		
		Collections.sort(result);
		return result;
	}
	
	
	public static void main(String[] args) {
		FileSearcher fileSearcher = new FileSearcher(Ontology.PO, "C:/Users/rodenhausen/etcsite/ontologies", "C:/gitEtc/charaparser/wordnet/wn31/dict");
		List<OntologyEntry> entries = fileSearcher.getEntries("axil", Type.ENTITY);
		System.out.println(entries.size());
		for(OntologyEntry entry : entries) {
			System.out.println(entry.getClassIRI());
		}
	}
}
