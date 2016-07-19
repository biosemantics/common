package edu.arizona.biosemantics.common.ontology.graph;

import org.jgrapht.DirectedGraph;

public class Main {

	public static void main(String[] args) throws Exception {
		Writer writer = new Writer("C:/Users/rodenhausen.CATNET/Desktop/etcsite/resources/ontologize/ontologies/go.owl", "graph2");
		writer.write();
		
		Reader reader = new Reader("graph2");
		DirectedGraph<String, Relationship> graph = reader.read();
	}

}
