package edu.arizona.biosemantics.common.ontology.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.common.ontology.search.model.Ontology;
import edu.arizona.biosemantics.common.ontology.search.model.OntologyEntry;
import edu.arizona.biosemantics.common.ontology.search.model.OntologyEntry.Type;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.OntologyLookupClient;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.data.Entity;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.data.EntityProposals;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.data.FormalConcept;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.owlaccessor.OWLAccessorImpl;
import edu.arizona.biosemantics.oto.common.ontologylookup.search.search.TermSearcher;

public class FileSearcher implements Searcher {

	private OntologyLookupClient ontologyLookupClient;
	private Ontology ontology;
	private boolean useCache;
	private HashSet<String> entityOntologyNames;
	private HashSet<String> qualityOntologyNames;

	public FileSearcher(HashSet<String> entityOntologyNames, HashSet<String> qualityOntologyNames, String ontologyDir, String dictDir, boolean useCache) {
		//this.ontology = ontology;
		try {
			this.entityOntologyNames = entityOntologyNames;
			this.qualityOntologyNames = qualityOntologyNames;
			//this.ontologyLookupClient = new OntologyLookupClient(ontology.toString().toLowerCase(), ontologyDir, dictDir);
			this.ontologyLookupClient = new OntologyLookupClient(entityOntologyNames, qualityOntologyNames, ontologyDir,
					dictDir); 
		} catch(Throwable t) {
			log(LogLevel.ERROR, "Problem invoking oto-common code", t);
		}
	}
	
	public List<OntologyEntry> getEntityEntries(String term, String locator, String rel) {
		List<OntologyEntry> result = new ArrayList<OntologyEntry>();
		
		//Only search structures for now leveraging ontologylookup client
		//This is all construction zone to find out use cases of a Searcher of ontologies we have
		try {
			List<EntityProposals> entityProposals = this.ontologyLookupClient.searchStructure(term, locator, rel, useCache);
			if(entityProposals != null && !entityProposals.isEmpty()) {
				for(Entity entity : entityProposals.get(0).getProposals()) {
					result.add(new OntologyEntry(ontology, entity.getClassIRI(), Type.ENTITY, entity.getConfidenceScore(),entity.getLabel(), entity.getDef(), entity.getPLabel(), entity.getMatchType()));
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
		
		TermSearcher termSearcher = new TermSearcher(ontologyLookupClient, useCache);
		ArrayList<FormalConcept> concepts = termSearcher.searchTerm(term, Type.QUALITY.toString().toLowerCase(), 1.0f);
		if(concepts != null)
			for(FormalConcept concept : concepts) 
				result.add(new OntologyEntry(ontology, concept.getClassIRI(), Type.QUALITY, concept.getConfidenceScore(), concept.getLabel(), concept.getDef(), concept.getPLabel(), concept.getMatchType()));
		
		Collections.sort(result);
		return result;
	}
	
	public OWLOntologyManager getOwlOntologyManager() {
		if(this.entityOntologyNames.size() > 0)
			return this.ontologyLookupClient.ontoutil.OWLentityOntoAPIs.get(0).getManager();
		return this.ontologyLookupClient.ontoutil.OWLqualityOntoAPIs.get(0).getManager();
	}
	
	/*
	 * update the searcher when ontology itself has been updated
	 */
	public void updateSearcher(IRI oIRI){
		OWLOntologyManager owlOntologyManager = getOwlOntologyManager();
		//get rootOnto, assuming there is not imported ontologies
		OWLOntology owlOntology = owlOntologyManager.getOntology(oIRI);
		//System.out.println("updateSearcher "+oIRI.getName() +"####################owlOntology="+owlOntology);
		//System.out.println("OWLentityOntoAPIs size (expected 1) = "+this.ontologyLookupClient.ontoutil.OWLentityOntoAPIs.size());
		OWLAccessorImpl api = this.ontologyLookupClient.ontoutil.OWLentityOntoAPIs.get(0);
		api.constructorHelper(owlOntology, new ArrayList<String>());
		api.retrieveAllConcept();
		api.resetSearchCache();
		//System.out.println("updateSearcher ####################api = "+api);
		//System.out.println("updateSearcher ####################owlOntology axiom count (updated to)="+owlOntology.getAxiomCount());

		api = this.ontologyLookupClient.ontoutil.OWLqualityOntoAPIs.get(0);
		api.constructorHelper(owlOntology, new ArrayList<String>());
		api.retrieveAllConcept();
		api.resetSearchCache();
	}
	
	public static void main(String[] args) {
		HashSet<String> entity = new HashSet<String>();
		entity.add("carex");
		HashSet<String> quality = new HashSet<String>();
		quality.add("carex");
		//FileSearcher fileSearcher = new FileSearcher(Ontology.po, "C:/Users/hongcui/Documents/etcsite/resources/shared/ontologies",
		//		"C:/Users/hongcui/Documents/etcsite/resources/shared/wordnet/wn31/dict", true);
		FileSearcher fileSearcher = new FileSearcher(entity, quality, "C:/Users/hongcui/git/charaparser-web/ontologies",
				"C:/Users/hongcui/Documents/etcsite/resources/shared/wordnet/wn31/dict", true);
		String term = "awnlike";//"plant proper";//"leaf blade";[has two matches]////"lamina";//"blade";
		//awnlike, full, branching, weak
		String parent = ""; //leaf
		
		String[] list = new String []{"awnlike","full", "branching", "weak"};

		for(String t: list){
			List<OntologyEntry> entries = fileSearcher.getEntityEntries(t, "", "");
			//List<OntologyEntry> entries = fileSearcher.getEntityEntries(term, parent, "of");
			System.out.println("Find matches: "+entries.size());
			for(OntologyEntry entry : entries) {
				System.out.println(t +"/"+parent+" has match type: "+entry.getMatchType());
				System.out.println(t +"/"+parent+" has an ID: "+entry.getClassIRI());
				System.out.println(t +"/"+parent+" has label: "+entry.getLabel());
				System.out.println(t +"/"+parent+" has match score: "+entry.getScore());

			}
		}
	}
}
