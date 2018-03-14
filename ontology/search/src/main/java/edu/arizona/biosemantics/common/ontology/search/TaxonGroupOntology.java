package edu.arizona.biosemantics.common.ontology.search;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import edu.arizona.biosemantics.common.biology.TaxonGroup;
import edu.arizona.biosemantics.common.ontology.search.model.Ontology;

public class TaxonGroupOntology {

	public static Set<Ontology> getEntityOntologies(TaxonGroup taxonGroup) {
		switch(taxonGroup) {
		//BSPO, RO, and PATO are auto-loaded in OntologyLookupClient
		case ALGAE:
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] { 
					/*Ontology.BSPO, Ontology.RO, Ontology.PATO*/}));
		case CNIDARIA:
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] { 
					/*Ontology.BSPO, Ontology.RO, Ontology.PATO*/}));
		case FOSSIL:
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] {  
					/*Ontology.BSPO, Ontology.RO, Ontology.PATO*/}));
		case GASTROPODS:
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] { 
					/*Ontology.BSPO, Ontology.RO, Ontology.PATO*/}));
		case HYMENOPTERA:
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] { Ontology.HAO, 
					/*Ontology.BSPO, Ontology.RO, Ontology.PATO*/}));
		case PLANT:
			//TODO add Ontology.CAREX later
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] { Ontology.PO, 
					/*Ontology.BSPO, Ontology.RO, Ontology.PATO*/}));
		case PORIFERA:
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] { Ontology.PORO, 
					/*Ontology.BSPO, Ontology.RO, Ontology.PATO*/}));
		case SPIDER:
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] { Ontology.SPD, 
					/*Ontology.BSPO, Ontology.RO, Ontology.PATO*/}));
		}
		return new HashSet<Ontology>();
	}	
	
	public static Set<Ontology> getExtraQualityOntologies(TaxonGroup taxonGroup) {
		switch(taxonGroup) {
		//BSPO, RO, and PATO are auto-loaded in OntologyLookupClient
		case ALGAE:
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] { 
					/*Ontology.BSPO, Ontology.RO, Ontology.PATO*/}));
		case CNIDARIA:
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] { 
					/*Ontology.BSPO, Ontology.RO, Ontology.PATO*/}));
		case FOSSIL:
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] {  
					/*Ontology.BSPO, Ontology.RO, Ontology.PATO*/}));
		case GASTROPODS:
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] { 
					/*Ontology.BSPO, Ontology.RO, Ontology.PATO*/}));
		case HYMENOPTERA:
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] {  
					/*Ontology.BSPO, Ontology.RO, Ontology.PATO*/}));
		case PLANT:
			//TODO add Ontology.CAREX later
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] { Ontology.CAREX, 
					/*Ontology.BSPO, Ontology.RO, Ontology.PATO*/}));
		case PORIFERA:
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] {  
					/*Ontology.BSPO, Ontology.RO, Ontology.PATO*/}));
		case SPIDER:
			return new HashSet<Ontology>(Arrays.asList(new Ontology[] { 
					/*Ontology.BSPO, Ontology.RO, Ontology.PATO*/}));
		}
		return new HashSet<Ontology>();
	}	
}
