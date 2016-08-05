package edu.arizona.biosemantics.common.ontology.graph;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

public class Main {

	public static void main(String[] args) throws Exception {
		Writer writer = new Writer("C:/Users/rodenhausen.CATNET/Desktop/etcsite/resources/ontologize/ontologies/uberon.owl", "graphs/uberon.graph");
		writer.write();
		
		Reader reader = new Reader("graphs/uberon.graph");
		DirectedSparseMultigraph<String, Edge> graph = reader.read();
	}

}
