package edu.arizona.biosemantics.common.ontology.search;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import edu.arizona.biosemantics.common.biology.TaxonGroup;
import edu.arizona.biosemantics.common.ontology.search.model.Ontology;

public class TaxonGroupOntology {

	public static Set<Ontology> getOntologies(TaxonGroup taxonGroup) {
		switch(taxonGroup) {
		case Algae:
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] { 
					Ontology.BSPO, Ontology.RO, Ontology.PATO}));
		case Cnidaria:
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] { 
					Ontology.BSPO, Ontology.RO, Ontology.PATO}));
		case Fossil:
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] {  
					Ontology.BSPO, Ontology.RO, Ontology.PATO}));
		case Gastropods:
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] { 
					Ontology.BSPO, Ontology.RO, Ontology.PATO}));
		case Hymenoptera:
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] { Ontology.HAO, 
					Ontology.BSPO, Ontology.RO, Ontology.PATO}));
		case Plant:
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] { Ontology.PO, 
					Ontology.BSPO, Ontology.RO, Ontology.PATO}));
		case Porifera:
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] { Ontology.PORO, 
					Ontology.BSPO, Ontology.RO, Ontology.PATO}));
		}
		return new HashSet<Ontology>();
	}	
}
