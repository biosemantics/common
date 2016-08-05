package edu.arizona.biosemantics.common.ontology.graph;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

public class Reader {

	private String inputGraphFile;

	public Reader(String inputGraphFile) {
		this.inputGraphFile = inputGraphFile;
	}
	
	public DirectedSparseMultigraph<String, Edge> read() throws Exception {
		try(FileInputStream fileInputStream = new FileInputStream(new File(inputGraphFile))) {
			try(ObjectInputStream in = new ObjectInputStream(fileInputStream)) {
				Object object = in.readObject();
				return (DirectedSparseMultigraph<String, Edge>)object;
			}
		}
	}
	
}
