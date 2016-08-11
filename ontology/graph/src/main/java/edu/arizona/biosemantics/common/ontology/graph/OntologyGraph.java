package edu.arizona.biosemantics.common.ontology.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

public class OntologyGraph implements Serializable {

	private static final long serialVersionUID = 1L;

	public static class Vertex implements Serializable {

		private static final long serialVersionUID = 1L;
		private String name;
		private String iri;

		public Vertex() {
		}

		public Vertex(String name, String iri) {
			this.name = name;
			this.iri = iri;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getIri() {
			return iri;
		}

		public void setIri(String iri) {
			this.iri = iri;
		}
		
		public boolean hasIri() {
			return iri != null && !iri.isEmpty();
		}

		@Override
		public String toString() {
			return name + " (" + iri + ")";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((iri == null) ? 0 : iri.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Vertex other = (Vertex) obj;
			if (iri == null) {
				if (other.iri != null)
					return false;
			} else if (!iri.equals(other.iri))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
	}

	public static class Edge implements Serializable {

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
	
	public static class Relation implements Serializable {
		
		private static final long serialVersionUID = 1L;
		private Vertex source;
		private Vertex destination;
		private Edge edge;
		
		public Relation() { }
		
		public Relation(Vertex source, Vertex destination, Edge edge) {
			this.source = source;
			this.destination = destination;
			this.edge = edge;
		}
		
		public Vertex getSource() {
			return source;
		}
		public void setSource(Vertex source) {
			this.source = source;
		}
		public Vertex getDestination() {
			return destination;
		}
		public void setDestination(Vertex destination) {
			this.destination = destination;
		}
		public Edge getEdge() {
			return edge;
		}
		public void setEdge(Edge edge) {
			this.edge = edge;
		}
		
		@Override
		public String toString() {
			return source.toString() + " --- " +  edge.toString() + " --> " + destination.toString();
		}


	}

	private DirectedSparseMultigraph<Vertex, Edge> graph;
	private Map<String, Set<Vertex>> nameIndex;
	private Map<String, Vertex> iriIndex;
	
	public OntologyGraph() {
		graph = new DirectedSparseMultigraph<Vertex, Edge>();
		nameIndex = new HashMap<String, Set<Vertex>>();
		iriIndex = new HashMap<String, Vertex>();
	}
	
	public boolean addVertex(Vertex vertex) {
		boolean result = graph.addVertex(vertex);
		if (result) {
			if(!nameIndex.containsKey(vertex.getName())) 
				nameIndex.put(vertex.getName(), new HashSet<Vertex>());
			nameIndex.get(vertex.getName()).add(vertex);
			iriIndex.put(vertex.getIri(), vertex);
		}
		return result;
	}

	private boolean removeVertex(Vertex vertex) {
		boolean result = graph.removeVertex(vertex);
		if (result) {
			if(nameIndex.containsKey(vertex.getName())) {
				nameIndex.get(vertex.getName()).remove(vertex);
				if(nameIndex.get(vertex.getName()).isEmpty())
					nameIndex.remove(vertex.getName());
			}
			iriIndex.remove(vertex.getIri());
		}
		return result;
	}

	public boolean addRelation(Relation relation) {
		if(!graph.containsVertex(relation.getSource()))
			this.addVertex(relation.getSource());
		if(!graph.containsVertex(relation.getDestination()))
			this.addVertex(relation.getDestination());
		
		return graph.addEdge(relation.getEdge(), relation.getSource(), relation.getDestination(), EdgeType.DIRECTED);
	}

	public Set<Vertex> getVerticesByName(String name) {
		if(!nameIndex.containsKey(name))
			return new HashSet<Vertex>();
		return nameIndex.get(name);
	}
	
	public Vertex getVertexByIri(String iri) {
		return iriIndex.get(iri);
	}
	
	public Relation getRelation(Edge edge) {
		return new Relation(graph.getSource(edge), graph.getDest(edge), edge);
	}
	
	public List<Relation> getOutRelations(Vertex vertex, Edge.Type type) {
		List<Relation> result = new LinkedList<Relation>();
		if(graph.containsVertex(vertex)) {
			java.util.Collection<Edge> edges = graph.getOutEdges(vertex);
			for(Edge edge : edges) {
				if(edge.getType().equals(type))
					result.add(new Relation(vertex, graph.getDest(edge), edge));
			}
		}
		return result;
	}
	
	public List<Relation> getInRelations(Vertex vertex, Edge.Type type) {
		List<Relation> result = new LinkedList<Relation>();
		if(graph.containsVertex(vertex)) {
			java.util.Collection<Edge> edges = graph.getInEdges(vertex);
			for(Edge edge : edges) {
				if(edge.getType().equals(type))
					result.add(new Relation(graph.getSource(edge), vertex, edge));
			}
		}
		return result;
	}

	public void removeRelation(Relation relation) {
		graph.removeEdge(relation.getEdge());
		graph.removeVertex(relation.getDestination());
	}

	public Collection<Vertex> getVertices() {
		return graph.getVertices();
	}
}
