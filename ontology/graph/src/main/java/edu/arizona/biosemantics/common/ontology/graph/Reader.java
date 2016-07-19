package edu.arizona.biosemantics.common.ontology.graph;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.jgrapht.DirectedGraph;

public class Reader {

	private String inputGraphFile;

	public Reader(String inputGraphFile) {
		this.inputGraphFile = inputGraphFile;
	}
	
	public DirectedGraph<String, Relationship> read() throws Exception {
		try(FileInputStream fileInputStream = new FileInputStream(new File(inputGraphFile))) {
			try(ObjectInputStream in = new ObjectInputStream(fileInputStream)) {
				Object object = in.readObject();
				return (DirectedGraph<String, Relationship>)object;
			}
		}
	}
	
}
