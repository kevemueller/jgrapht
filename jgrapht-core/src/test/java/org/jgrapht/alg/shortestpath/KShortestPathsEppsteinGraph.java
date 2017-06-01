package org.jgrapht.alg.shortestpath;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

public class KShortestPathsEppsteinGraph extends DirectedWeightedPseudograph<String, DefaultWeightedEdge> {
	private static final long serialVersionUID = 4784241694367922869L;

	public KShortestPathsEppsteinGraph() {
		super(DefaultWeightedEdge.class);
		addEdge("0", "1", 2.0);
		addEdge("1", "2", 20.0);
		addEdge("2", "3", 14.0);
		addEdge("0", "4", 13.0);
		addEdge("1", "5", 27.0);
		addEdge("2", "6", 14.0);
		addEdge("3", "7", 15.0);
		addEdge("4", "5", 9.0);
		addEdge("5", "6", 10.0);
		addEdge("6", "7", 25.0);
		addEdge("4", "8", 15.0);
		addEdge("5", "9", 20.0);
		addEdge("6", "10", 12.0);
		addEdge("7", "11", 7.0);
		addEdge("8", "9", 18.0);
		addEdge("9", "10", 8.0);
		addEdge("10", "11", 11.0);
	}

	private void addEdge(String sourceVertex, String targetVertex, double weight) {
		addVertex(sourceVertex);
		addVertex(targetVertex);
		DefaultWeightedEdge e = addEdge(sourceVertex, targetVertex);
		setEdgeWeight(e, weight);
	}
}
