package edu.arizona.biosemantics.common.taxonomy;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class TaxonomyBuilder {

	public Set<Taxon> buildTaxonomyForest(Collection<TaxonIdentification> taxonIdentifications) {
		Set<Taxon> rootTaxa = new HashSet<Taxon>();

		Map<RankData, Taxon> rankTaxaMap = new HashMap<RankData, Taxon>();
		for(TaxonIdentification taxonIdentification : taxonIdentifications) {
			 Taxon taxon = createTaxon(taxonIdentification);
			 rankTaxaMap.put(taxonIdentification.getRankData().getLast(), taxon);
		}
		
		for(TaxonIdentification taxonIdentification : taxonIdentifications) {
	    	LinkedList<RankData> rankData = taxonIdentification.getRankData();
	    	Taxon taxon = rankTaxaMap.get(rankData.getLast());
	    	if(rankData.size() == 1) 
	    		rootTaxa.add(taxon);
		    if(rankData.size() > 1) {
		    	int parentRankIndex = rankData.size() - 2;
		    	Taxon parentTaxon = null;
		    	while(parentTaxon == null && parentRankIndex >= 0) {
			    	RankData parentRankData = rankData.get(parentRankIndex);
		    		parentTaxon = rankTaxaMap.get(parentRankData);
		    		parentRankIndex--;
		    	}
		    	if(parentTaxon == null)
		    		rootTaxa.add(taxon);
		    	else
		    		parentTaxon.addChild(taxon);
		    }
	    }
		return rootTaxa;
	}
	
	protected Taxon createTaxon(TaxonIdentification taxonIdentification) {
		Taxon taxon = new Taxon(taxonIdentification);
		return taxon;
	}	
}
