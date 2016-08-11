package edu.arizona.biosemantics.common.ontology.graph;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

public class Main {

	public static void main(String[] args) throws Exception {
		
		for(Ontology ontology : Ontology.values()) {
			Writer writer = new Writer("C:/Users/rodenhausen.CATNET/Desktop/etcsite/resources/ontologize/ontologies/" + ontology.getName() + ".owl", 
					"graphs/" + ontology.getName() + ".graph");
			writer.write();
			
			Reader reader = new Reader("graphs/" + ontology.getName() + ".graph");
			OntologyGraph graph = reader.read();
		}
	}

}
