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
	
	public List<OntologyEntry> getEntityEntries(String term, String locator, String rel) {
		List<OntologyEntry> result = new ArrayList<OntologyEntry>();
		
		//Only search structures for now leveraging ontologylookup client
		//This is all construction zone to find out use cases of a Searcher of ontologies we have
		try {
			List<EntityProposals> entityProposals = this.ontologyLookupClient.searchStructure(term, locator, rel);
			if(entityProposals != null && !entityProposals.isEmpty()) {
				for(Entity entity : entityProposals.get(0).getProposals()) {
					result.add(new OntologyEntry(ontology, entity.getClassIRI(), Type.ENTITY, entity.getConfidenceScore(),entity.getLabel(), entity.getPLabel()));
				}
			}
		} catch(Throwable t) {
			log(LogLevel.ERROR, "Problem invoking oto-common code", t);
		}
		
		Collections.sort(result);
		return result;
	}

	public List<OntologyEntry> getQualityEntries(String term) {
		List<OntologyEntry> result = new ArrayList<OntologyEntry>();
		
		TermSearcher termSearcher = new TermSearcher(ontologyLookupClient);
		ArrayList<FormalConcept> concepts = termSearcher.searchTerm(term, Type.QUALITY.toString().toLowerCase(), 1.0f);
		if(concepts != null)
			for(FormalConcept concept : concepts) 
				result.add(new OntologyEntry(ontology, concept.getClassIRI(), Type.QUALITY, concept.getConfidenceScore(), concept.getLabel(), concept.getPLabel()));
		
		Collections.sort(result);
		return result;
	}
	
	
	public static void main(String[] args) {
		FileSearcher fileSearcher = new FileSearcher(Ontology.PO, "C:/Users/hongcui/Documents/etcsite/resources/shared/ontologies",
				"C:/Users/hongcui/Documents/etcsite/resources/shared/wordnet/wn31/dict");
		String term = "embryo proper";//"plant proper";//"leaf blade";[has two matches]////"lamina";//"blade";
		String parent = ""; //leaf
		List<OntologyEntry> entries = fileSearcher.getEntityEntries(term, "", "");
		//List<OntologyEntry> entries = fileSearcher.getEntityEntries(term, parent, "of");
		System.out.println("Find matches: "+entries.size());
		for(OntologyEntry entry : entries) {
			System.out.println(term +"/"+parent+" has an ID: "+entry.getClassIRI());
			System.out.println(term +"/"+parent+" has label: "+entry.getLabel());
			System.out.println(term +"/"+parent+" has match score: "+entry.getScore());
		}
	}
}
