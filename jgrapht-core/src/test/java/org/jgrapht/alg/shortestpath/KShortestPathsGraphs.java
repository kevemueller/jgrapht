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
import org.jgrapht.graph.DirectedWeightedPseudograph;

/**
 * Sample graphs for use in k shortest path tests.
 *
 */
public final class KShortestPathsGraphs
{
    /**
     * Graph used in Eppstein's paper.
     */
    public static final DefaultDirectedGraph<Integer, DefaultWeightedEdge> EPPSTEIN;
    /**
     * Graph used in Graehl's C++ implementation.
     */
    public static final DefaultDirectedGraph<Integer, DefaultWeightedEdge> GRAEHL;
    /**
     * graph with multiple edges but no loop
     * 
     * <p>
     * 0 -- 1.0|2.0|3.0 --> 1
     * 
     * <p>
     * 1 -- 1.0 --> 2
     */
    public static final DirectedWeightedPseudograph<Integer, DefaultWeightedEdge> NOLOOPME;
    /**
     * pseudograph used in http://www.isi.edu/natural-language/people/epp-cs562.pdf page44
     * 
     * <p>
     * 0 -- 1.0 --> 1 0
     * 
     * <p>
     * 0 -- 2.0|3.0 --> 0
     */
    public static final DirectedWeightedPseudograph<Integer, DefaultWeightedEdge> LOOPME;
    /**
     * pseudograph used in http://www.isi.edu/natural-language/people/epp-cs562.pdf page45
     * 
     * <p>
     * 0 -- 1000.0 --> 1 0
     * 
     * <p>
     * 0 -- 1.0|2.0|...|100.0 --> 0
     */
    public static final DirectedWeightedPseudograph<Integer, DefaultWeightedEdge> LOOPME1000;

    /**
     * Another graph.
     */
    public static final DirectedWeightedPseudograph<Integer, DefaultWeightedEdge> WIRE;

    static {
        EPPSTEIN = new DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge>(
            DefaultWeightedEdge.class);
        addEdge(EPPSTEIN, 0, 1, 2.0);
        addEdge(EPPSTEIN, 1, 2, 20.0);
        addEdge(EPPSTEIN, 2, 3, 14.0);
        addEdge(EPPSTEIN, 0, 4, 13.0);
        addEdge(EPPSTEIN, 1, 5, 27.0);
        addEdge(EPPSTEIN, 2, 6, 14.0);
        addEdge(EPPSTEIN, 3, 7, 15.0);
        addEdge(EPPSTEIN, 4, 5, 9.0);
        addEdge(EPPSTEIN, 5, 6, 10.0);
        addEdge(EPPSTEIN, 6, 7, 25.0);
        addEdge(EPPSTEIN, 4, 8, 15.0);
        addEdge(EPPSTEIN, 5, 9, 20.0);
        addEdge(EPPSTEIN, 6, 10, 12.0);
        addEdge(EPPSTEIN, 7, 11, 7.0);
        addEdge(EPPSTEIN, 8, 9, 18.0);
        addEdge(EPPSTEIN, 9, 10, 8.0);
        addEdge(EPPSTEIN, 10, 11, 11.0);

        GRAEHL = new DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge>(
            DefaultWeightedEdge.class);
        addEdge(GRAEHL, 0, 5, .1);
        addEdge(GRAEHL, 5, 1, .5);
        addEdge(GRAEHL, 5, 2, 1.0);
        addEdge(GRAEHL, 1, 2, .9);
        addEdge(GRAEHL, 3, 4, .2);
        addEdge(GRAEHL, 4, 2, .3);
        addEdge(GRAEHL, 5, 3, .3);
        addEdge(GRAEHL, 5, 4, .8);
        addEdge(GRAEHL, 2, 4, 0.1);
        addEdge(GRAEHL, 3, 2, .6);
        addEdge(GRAEHL, 0, 0, .05);

        NOLOOPME = new DirectedWeightedPseudograph<Integer, DefaultWeightedEdge>(
            DefaultWeightedEdge.class);
        addEdge(NOLOOPME, 0, 1, 1.0);
        addEdge(NOLOOPME, 0, 1, 2.0);
        addEdge(NOLOOPME, 0, 1, 3.0);
        addEdge(NOLOOPME, 1, 2, 1.0);

        LOOPME = new DirectedWeightedPseudograph<Integer, DefaultWeightedEdge>(
            DefaultWeightedEdge.class);
        addEdge(LOOPME, 0, 1, 1.0);
        addEdge(LOOPME, 0, 0, 2.0);
        addEdge(LOOPME, 0, 0, 3.0);

        LOOPME1000 = new DirectedWeightedPseudograph<Integer, DefaultWeightedEdge>(
            DefaultWeightedEdge.class);
        addEdge(LOOPME1000, 0, 0, 1.0);
        addEdge(LOOPME1000, 0, 0, 2.0);
        addEdge(LOOPME1000, 0, 0, 3.0);
        addEdge(LOOPME1000, 0, 0, 4.0);
        addEdge(LOOPME1000, 0, 0, 5.0);
        addEdge(LOOPME1000, 0, 0, 6.0);
        addEdge(LOOPME1000, 0, 0, 7.0);
        addEdge(LOOPME1000, 0, 0, 8.0);
        addEdge(LOOPME1000, 0, 0, 9.0);
        addEdge(LOOPME1000, 0, 0, 10.0);
        addEdge(LOOPME1000, 0, 0, 11.0);
        addEdge(LOOPME1000, 0, 0, 12.0);
        addEdge(LOOPME1000, 0, 0, 13.0);
        addEdge(LOOPME1000, 0, 0, 14.0);
        addEdge(LOOPME1000, 0, 0, 15.0);
        addEdge(LOOPME1000, 0, 0, 16.0);
        addEdge(LOOPME1000, 0, 0, 17.0);
        addEdge(LOOPME1000, 0, 0, 18.0);
        addEdge(LOOPME1000, 0, 0, 19.0);
        addEdge(LOOPME1000, 0, 0, 20.0);
        addEdge(LOOPME1000, 0, 0, 21.0);
        addEdge(LOOPME1000, 0, 0, 22.0);
        addEdge(LOOPME1000, 0, 0, 23.0);
        addEdge(LOOPME1000, 0, 0, 24.0);
        addEdge(LOOPME1000, 0, 0, 25.0);
        addEdge(LOOPME1000, 0, 0, 26.0);
        addEdge(LOOPME1000, 0, 0, 27.0);
        addEdge(LOOPME1000, 0, 0, 28.0);
        addEdge(LOOPME1000, 0, 0, 29.0);
        addEdge(LOOPME1000, 0, 0, 30.0);
        addEdge(LOOPME1000, 0, 0, 31.0);
        addEdge(LOOPME1000, 0, 0, 32.0);
        addEdge(LOOPME1000, 0, 0, 33.0);
        addEdge(LOOPME1000, 0, 0, 34.0);
        addEdge(LOOPME1000, 0, 0, 35.0);
        addEdge(LOOPME1000, 0, 0, 36.0);
        addEdge(LOOPME1000, 0, 0, 37.0);
        addEdge(LOOPME1000, 0, 0, 38.0);
        addEdge(LOOPME1000, 0, 0, 39.0);
        addEdge(LOOPME1000, 0, 0, 40.0);
        addEdge(LOOPME1000, 0, 0, 41.0);
        addEdge(LOOPME1000, 0, 0, 42.0);
        addEdge(LOOPME1000, 0, 0, 43.0);
        addEdge(LOOPME1000, 0, 0, 44.0);
        addEdge(LOOPME1000, 0, 0, 45.0);
        addEdge(LOOPME1000, 0, 0, 46.0);
        addEdge(LOOPME1000, 0, 0, 47.0);
        addEdge(LOOPME1000, 0, 0, 48.0);
        addEdge(LOOPME1000, 0, 0, 49.0);
        addEdge(LOOPME1000, 0, 0, 50.0);
        addEdge(LOOPME1000, 0, 0, 51.0);
        addEdge(LOOPME1000, 0, 0, 52.0);
        addEdge(LOOPME1000, 0, 0, 53.0);
        addEdge(LOOPME1000, 0, 0, 54.0);
        addEdge(LOOPME1000, 0, 0, 55.0);
        addEdge(LOOPME1000, 0, 0, 56.0);
        addEdge(LOOPME1000, 0, 0, 57.0);
        addEdge(LOOPME1000, 0, 0, 58.0);
        addEdge(LOOPME1000, 0, 0, 59.0);
        addEdge(LOOPME1000, 0, 0, 60.0);
        addEdge(LOOPME1000, 0, 0, 61.0);
        addEdge(LOOPME1000, 0, 0, 62.0);
        addEdge(LOOPME1000, 0, 0, 63.0);
        addEdge(LOOPME1000, 0, 0, 64.0);
        addEdge(LOOPME1000, 0, 0, 65.0);
        addEdge(LOOPME1000, 0, 0, 66.0);
        addEdge(LOOPME1000, 0, 0, 67.0);
        addEdge(LOOPME1000, 0, 0, 68.0);
        addEdge(LOOPME1000, 0, 0, 69.0);
        addEdge(LOOPME1000, 0, 0, 70.0);
        addEdge(LOOPME1000, 0, 0, 71.0);
        addEdge(LOOPME1000, 0, 0, 72.0);
        addEdge(LOOPME1000, 0, 0, 73.0);
        addEdge(LOOPME1000, 0, 0, 74.0);
        addEdge(LOOPME1000, 0, 0, 75.0);
        addEdge(LOOPME1000, 0, 0, 76.0);
        addEdge(LOOPME1000, 0, 0, 77.0);
        addEdge(LOOPME1000, 0, 0, 78.0);
        addEdge(LOOPME1000, 0, 0, 79.0);
        addEdge(LOOPME1000, 0, 0, 80.0);
        addEdge(LOOPME1000, 0, 0, 81.0);
        addEdge(LOOPME1000, 0, 0, 82.0);
        addEdge(LOOPME1000, 0, 0, 83.0);
        addEdge(LOOPME1000, 0, 0, 84.0);
        addEdge(LOOPME1000, 0, 0, 85.0);
        addEdge(LOOPME1000, 0, 0, 86.0);
        addEdge(LOOPME1000, 0, 0, 87.0);
        addEdge(LOOPME1000, 0, 0, 88.0);
        addEdge(LOOPME1000, 0, 0, 89.0);
        addEdge(LOOPME1000, 0, 0, 90.0);
        addEdge(LOOPME1000, 0, 0, 91.0);
        addEdge(LOOPME1000, 0, 0, 92.0);
        addEdge(LOOPME1000, 0, 0, 93.0);
        addEdge(LOOPME1000, 0, 0, 94.0);
        addEdge(LOOPME1000, 0, 0, 95.0);
        addEdge(LOOPME1000, 0, 0, 96.0);
        addEdge(LOOPME1000, 0, 0, 97.0);
        addEdge(LOOPME1000, 0, 0, 98.0);
        addEdge(LOOPME1000, 0, 0, 99.0);
        addEdge(LOOPME1000, 0, 0, 100.0);
        addEdge(LOOPME1000, 0, 1, 1000.0);

        WIRE = new DirectedWeightedPseudograph<Integer, DefaultWeightedEdge>(
            DefaultWeightedEdge.class);
        addEdge(WIRE, 0, 1, 1.0);
        addEdge(WIRE, 2, 3, 40.0);
        addEdge(WIRE, 3, 1, 1.0);
        addEdge(WIRE, 1, 4, 40.0);
        addEdge(WIRE, 1, 1, 40.0);
        addEdge(WIRE, 4, 5, 40.0);
        addEdge(WIRE, 4, 0, 41.0);
        addEdge(WIRE, 4, 6, 42.0);
        addEdge(WIRE, 4, 4, 40.0);
        addEdge(WIRE, 4, 1, 40.0);
        addEdge(WIRE, 5, 4, 1.0);
        addEdge(WIRE, 5, 1, 2.0);
        addEdge(WIRE, 4, 7, 40.0);
        addEdge(WIRE, 4, 8, 41.0);
        addEdge(WIRE, 7, 4, 1.0);
        addEdge(WIRE, 7, 1, 2.0);
        addEdge(WIRE, 4, 9, 40.0);
        addEdge(WIRE, 9, 10, 40.0);
        addEdge(WIRE, 9, 0, 41.0);
        addEdge(WIRE, 9, 6, 42.0);
        addEdge(WIRE, 9, 9, 40.0);
        addEdge(WIRE, 9, 4, 40.0);
        addEdge(WIRE, 9, 1, 40.0);
        addEdge(WIRE, 10, 9, 1.0);
        addEdge(WIRE, 10, 4, 2.0);
        addEdge(WIRE, 10, 1, 2.0);
        addEdge(WIRE, 4, 11, 40.0);
        addEdge(WIRE, 4, 12, 41.0);
        addEdge(WIRE, 11, 13, 10.0);
        addEdge(WIRE, 13, 11, 10.0);
        addEdge(WIRE, 13, 12, 11.0);
        addEdge(WIRE, 13, 4, 10.0);
        addEdge(WIRE, 13, 1, 10.0);
        addEdge(WIRE, 4, 14, 40.0);
        addEdge(WIRE, 14, 15, 40.0);
        addEdge(WIRE, 14, 12, 41.0);
        addEdge(WIRE, 14, 14, 40.0);
        addEdge(WIRE, 14, 4, 40.0);
        addEdge(WIRE, 14, 1, 40.0);
        addEdge(WIRE, 15, 13, 10.0);
        addEdge(WIRE, 13, 15, 10.0);
        addEdge(WIRE, 13, 14, 10.0);
        addEdge(WIRE, 4, 16, 40.0);
        addEdge(WIRE, 16, 13, 10.0);
        addEdge(WIRE, 13, 16, 10.0);
        addEdge(WIRE, 9, 17, 40.0);
        addEdge(WIRE, 9, 12, 41.0);
        addEdge(WIRE, 17, 13, 10.0);
        addEdge(WIRE, 13, 17, 10.0);
        addEdge(WIRE, 13, 9, 10.0);
        addEdge(WIRE, 9, 18, 40.0);
        addEdge(WIRE, 18, 13, 10.0);
        addEdge(WIRE, 13, 18, 10.0);
        addEdge(WIRE, 9, 19, 40.0);
        addEdge(WIRE, 19, 13, 10.0);
        addEdge(WIRE, 13, 19, 10.0);
        addEdge(WIRE, 11, 20, 1.0);
        addEdge(WIRE, 11, 21, 2.0);
        addEdge(WIRE, 17, 22, 1.0);
        addEdge(WIRE, 17, 21, 2.0);
        addEdge(WIRE, 17, 23, 2.0);
        addEdge(WIRE, 17, 24, 3.0);
        addEdge(WIRE, 17, 25, 4.0);
        addEdge(WIRE, 17, 26, 2.0);
        addEdge(WIRE, 18, 27, 1.0);
        addEdge(WIRE, 18, 21, 2.0);
        addEdge(WIRE, 18, 23, 2.0);
        addEdge(WIRE, 18, 24, 3.0);
        addEdge(WIRE, 18, 25, 4.0);
        addEdge(WIRE, 18, 26, 2.0);
        addEdge(WIRE, 19, 27, 1.0);
        addEdge(WIRE, 19, 21, 2.0);
        addEdge(WIRE, 19, 23, 2.0);
        addEdge(WIRE, 19, 24, 3.0);
        addEdge(WIRE, 19, 25, 4.0);
        addEdge(WIRE, 19, 26, 2.0);
        addEdge(WIRE, 9, 28, 40.0);
        addEdge(WIRE, 28, 29, 1.0);
        addEdge(WIRE, 28, 30, 2.0);
        addEdge(WIRE, 28, 24, 3.0);
        addEdge(WIRE, 28, 25, 4.0);
        addEdge(WIRE, 28, 21, 2.0);
        addEdge(WIRE, 28, 23, 2.0);
        addEdge(WIRE, 28, 26, 2.0);
        addEdge(WIRE, 23, 31, 40.0);
        addEdge(WIRE, 23, 21, 41.0);
        addEdge(WIRE, 23, 23, 40.0);
        addEdge(WIRE, 23, 24, 41.0);
        addEdge(WIRE, 23, 25, 42.0);
        addEdge(WIRE, 23, 26, 40.0);
        addEdge(WIRE, 31, 17, 1.0);
        addEdge(WIRE, 31, 12, 2.0);
        addEdge(WIRE, 31, 9, 2.0);
        addEdge(WIRE, 31, 4, 2.0);
        addEdge(WIRE, 31, 1, 2.0);
        addEdge(WIRE, 27, 17, 1.0);
        addEdge(WIRE, 27, 12, 2.0);
        addEdge(WIRE, 27, 9, 2.0);
        addEdge(WIRE, 27, 4, 2.0);
        addEdge(WIRE, 27, 1, 2.0);
        addEdge(WIRE, 29, 17, 1.0);
        addEdge(WIRE, 29, 12, 2.0);
        addEdge(WIRE, 29, 9, 2.0);
        addEdge(WIRE, 29, 4, 2.0);
        addEdge(WIRE, 29, 1, 2.0);
        addEdge(WIRE, 4, 32, 40.0);
        addEdge(WIRE, 32, 4, 1.0);
        addEdge(WIRE, 32, 1, 2.0);
        addEdge(WIRE, 2, 33, 40.0);
        addEdge(WIRE, 2, 34, 41.0);
        addEdge(WIRE, 33, 35, 40.0);
        addEdge(WIRE, 33, 33, 40.0);
        addEdge(WIRE, 33, 34, 41.0);
        addEdge(WIRE, 35, 4, 1.0);
        addEdge(WIRE, 35, 1, 2.0);
        addEdge(WIRE, 9, 36, 40.0);
        addEdge(WIRE, 36, 24, 1.0);
        addEdge(WIRE, 36, 25, 2.0);
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
