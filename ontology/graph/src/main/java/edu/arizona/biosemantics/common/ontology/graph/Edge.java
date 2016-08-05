package edu.arizona.biosemantics.common.ontology.graph;

import java.io.Serializable;

public class Edge implements Serializable {

	public static enum Type { 
		SUBCLASS_OF, PART_OF, SYNONYM_OF;	
	}
	
	private static final long serialVersionUID = 1L;
	private Type type;
	
	public Edge() {
	}

	public Edge(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
