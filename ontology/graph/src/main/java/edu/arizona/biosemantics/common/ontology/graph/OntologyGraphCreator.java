package edu.arizona.biosemantics.common.ontology.graph;

import java.io.File;

public class OntologyGraphCreator {

	public void create(String ontologiesDir, String graphsDir) throws Exception {
		for(Ontology ontology : Ontology.values()) {
			System.out.println("Ontology: " + ontology.getName());
			Writer writer = new Writer(ontologiesDir + File.separator + ontology.getName() + ".owl", 
					graphsDir + File.separator + ontology.getName() + ".graph");
			writer.write();
		}
	}
	
}
