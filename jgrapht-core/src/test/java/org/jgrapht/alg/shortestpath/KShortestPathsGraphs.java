/*
 * (C) Copyright 2017-2017, by Keve Müller and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * This program and the accompanying materials are dual-licensed under
 * either
 *
 * (a) the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation, or (at your option) any
 * later version.
 *
 * or (per the licensee's choosing)
 *
 * (b) the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation.
 */
package org.jgrapht.alg.shortestpath;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * Sample graphs for use in k shortest path tests.
 */
public class KShortestPathsGraphs
{

    /**
     * Graph used in Eppstein's paper.
     */
    public static final DefaultDirectedGraph<String, DefaultWeightedEdge> EPPSTEIN;

    static {
        EPPSTEIN = new DefaultDirectedWeightedGraph<String, DefaultWeightedEdge>(
            DefaultWeightedEdge.class);
        addEdge(EPPSTEIN, "0", "1", 2.0);
        addEdge(EPPSTEIN, "1", "2", 20.0);
        addEdge(EPPSTEIN, "2", "3", 14.0);
        addEdge(EPPSTEIN, "0", "4", 13.0);
        addEdge(EPPSTEIN, "1", "5", 27.0);
        addEdge(EPPSTEIN, "2", "6", 14.0);
        addEdge(EPPSTEIN, "3", "7", 15.0);
        addEdge(EPPSTEIN, "4", "5", 9.0);
        addEdge(EPPSTEIN, "5", "6", 10.0);
        addEdge(EPPSTEIN, "6", "7", 25.0);
        addEdge(EPPSTEIN, "4", "8", 15.0);
        addEdge(EPPSTEIN, "5", "9", 20.0);
        addEdge(EPPSTEIN, "6", "10", 12.0);
        addEdge(EPPSTEIN, "7", "11", 7.0);
        addEdge(EPPSTEIN, "8", "9", 18.0);
        addEdge(EPPSTEIN, "9", "10", 8.0);
        addEdge(EPPSTEIN, "10", "11", 11.0);
    }

    private static <V,
        E> void addEdge(Graph<V, E> graph, V sourceVertex, V targetVertex, double weight)
    {
        graph.addVertex(sourceVertex);
        graph.addVertex(targetVertex);
        E e = graph.addEdge(sourceVertex, targetVertex);
        graph.setEdgeWeight(e, weight);
    }

    private KShortestPathsGraphs()
    {
    }
}
