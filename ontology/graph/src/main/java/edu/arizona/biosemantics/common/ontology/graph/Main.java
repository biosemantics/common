package edu.arizona.biosemantics.common.ontology.graph;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

public class Main {

	public static void main(String[] args) throws Exception {
		System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("console.out")), true));
		
		for(Ontology ontology : Ontology.values()) {
			System.out.println("Ontology: " + ontology.getName());
			Writer writer = new Writer("C:/Users/rodenhausen.CATNET/Desktop/etcsite/resources/ontologize/ontologies/" + ontology.getName() + ".owl", 
					"graphs/" + ontology.getName() + ".graph");
			writer.write();
			
			Reader reader = new Reader("graphs/" + ontology.getName() + ".graph");
			OntologyGraph graph = reader.read();
		}
	}

}
