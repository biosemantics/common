package edu.arizona.biosemantics.common.ontology.graph;

import java.io.Serializable;
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
		private Vertex src;
		private Vertex dest;
		
		public Edge() {
		}

		public Edge(Vertex src, Vertex dest, Type type) {
			this.src = src;
			this.dest = dest;
			this.type = type;
		}

		public Type getType() {
			return type;
		}

		public Vertex getSrc() {
			return src;
		}

		public Vertex getDest() {
			return dest;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((dest == null) ? 0 : dest.hashCode());
			result = prime * result + ((src == null) ? 0 : src.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
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
			Edge other = (Edge) obj;
			if (dest == null) {
				if (other.dest != null)
					return false;
			} else if (!dest.equals(other.dest))
				return false;
			if (src == null) {
				if (other.src != null)
					return false;
			} else if (!src.equals(other.src))
				return false;
			if (type != other.type)
				return false;
			return true;
		}
		
		@Override
		public String toString() {
			return src.toString() + " --- " +  type + " --> " + dest.toString();
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

	public boolean addRelation(Edge relation) {
		if(!graph.containsVertex(relation.getSrc()))
			this.addVertex(relation.getSrc());
		if(!graph.containsVertex(relation.getDest()))
			this.addVertex(relation.getDest());
		
		return graph.addEdge(relation, relation.getSrc(), relation.getDest(), EdgeType.DIRECTED);
	}

	public Set<Vertex> getVerticesByName(String name) {
		if(!nameIndex.containsKey(name))
			return new HashSet<Vertex>();
		return nameIndex.get(name);
	}
	
	public Vertex getVertexByIri(String iri) {
		return iriIndex.get(iri);
	}
		
	public List<Edge> getOutRelations(Vertex vertex, Edge.Type type) {
		List<Edge> result = new LinkedList<Edge>();
		if(graph.containsVertex(vertex)) {
			java.util.Collection<Edge> edges = graph.getOutEdges(vertex);
			for(Edge edge : edges) {
				if(edge.getType().equals(type))
					result.add(edge);
			}
		}
		return result;
	}
	
	public List<Edge> getInRelations(Vertex vertex, Edge.Type type) {
		List<Edge> result = new LinkedList<Edge>();
		if(graph.containsVertex(vertex)) {
			java.util.Collection<Edge> edges = graph.getInEdges(vertex);
			for(Edge edge : edges) {
				if(edge.getType().equals(type))
					result.add(edge);
			}
		}
		return result;
	}

	public Collection<Vertex> getVertices() {
		return graph.getVertices();
	}
}
