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

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.EdgeReversedGraph;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Parameterized JUnit test of {@link AbstractMultiShortestPathsTest} for Graphs of
 * <String,DefaultWeightedEdge>.
 * 
 * @author Keve Müller
 * 
 * @since June 1, 2017
 * 
 */
@RunWith(Parameterized.class)
public final class MultiShortestPathsIntegerDWETest
    extends AbstractMultiShortestPathsTest<Integer, DefaultWeightedEdge>
{

    /**
     * Return the parameters to instantiate this test.
     * 
     * @return the collection of parameters.
     */
    @Parameters(name = "{0}")
    public static Collection<Object[]> data()
    {
        EdgeReversedGraph<Integer, DefaultWeightedEdge> loopMEInv =
            new EdgeReversedGraph<>(KShortestPathsGraphs.LOOPME);
        List<Object[]> graphs = Arrays.asList(
            new Object[] { "Wiring", KShortestPathsGraphs.WIRE, EnumSet.of(GraphProperties.CYCLES),
                15, 5, 15,
                Arrays.asList(
                    "5,4,16,13,15", "5,4,11,13,15", "5,4,16,13,11,13,15", "5,4,11,13,15,13,15",
                    "5,4,16,13,15,13,15", "5,4,16,13,16,13,15", "5,4,11,13,11,13,15",
                    "5,4,16,13,18,13,15", "5,4,11,13,16,13,15", "5,4,14,15", "5,4,16,13,19,13,15",
                    "5,4,11,13,19,13,15", "5,4,16,13,17,13,15", "5,4,11,13,17,13,15",
                    "5,4,11,13,18,13,15"),
                Arrays.asList(61, 61, 81, 81, 81, 81, 81, 81, 81, 81, 81, 81, 81, 81, 81) },
            new Object[] { "Eppstein", KShortestPathsGraphs.EPPSTEIN, GRAPH_SIMPLE, 20, 0, 11,
                Arrays.asList(
                    "0,4,5,6,10,11", "0,1,2,3,7,11", "0,1,2,6,10,11", "0,4,5,9,10,11",
                    "0,1,5,6,10,11", "0,4,5,6,7,11", "0,4,8,9,10,11", "0,1,2,6,7,11",
                    "0,1,5,9,10,11", "0,1,5,6,7,11"),
                Arrays.asList(55, 58, 59, 61, 62, 64, 65, 68, 68, 71) },
            new Object[] { "Graehl", KShortestPathsGraphs.GRAEHL, GRAPH_WITH_SELFLOOP, 7, 0, 1,
                Arrays.asList(
                    "0,5,1", "0,0,5,1", "0,0,0,5,1", "0,0,0,0,5,1", "0,0,0,0,0,5,1",
                    "0,0,0,0,0,0,5,1", "0,0,0,0,0,0,0,5,1"),
                Arrays.asList(.6, .65, .7, .75, .8, .85, .9) },
            new Object[] { "NoLoopME", KShortestPathsGraphs.NOLOOPME, GRAPH_WITH_MULTIEDGE, 20, 0,
                2, Arrays.asList("(0:1/1.0),2", "(0:1/2.0),2", "(0:1/3.0),2"),
                Arrays.asList(2, 3, 4) },
            new Object[] { "LoopME", KShortestPathsGraphs.LOOPME, GRAPH_WITH_SELFLOOP_AND_MULTIEDGE,
                11, 0, 1,
                Arrays.asList(
                    "0,1", "(0:0/2.0),1", "(0:0/3.0),1", "(0:0/2.0),(0:0/2.0),1",
                    "(0:0/3.0),(0:0/2.0),1", "(0:0/2.0),(0:0/3.0),1",
                    "(0:0/2.0),(0:0/2.0),(0:0/2.0),1", "(0:0/3.0),(0:0/3.0),1",
                    "(0:0/3.0),(0:0/2.0),(0:0/2.0),1", "(0:0/2.0),(0:0/2.0),(0:0/3.0),1",
                    "(0:0/2.0),(0:0/3.0),(0:0/2.0),1"),
                Arrays.asList(1, 3, 4, 5, 6, 6, 7, 7, 8, 8, 8) },
            new Object[] { "LoopMEInv", loopMEInv, GRAPH_WITH_SELFLOOP_AND_MULTIEDGE, 11, 1, 0,
                Arrays.asList(
                    "(1:0/1.0)", "(1:0/1.0),(0:0/2.0)", "(1:0/1.0),(0:0/3.0)",
                    "(1:0/1.0),(0:0/2.0),(0:0/2.0)", "(1:0/1.0),(0:0/3.0),(0:0/2.0)",
                    "(1:0/1.0),(0:0/2.0),(0:0/3.0)", "(1:0/1.0),(0:0/3.0),(0:0/3.0)",
                    "(1:0/1.0),(0:0/2.0),(0:0/2.0),(0:0/2.0)",
                    "(1:0/1.0),(0:0/3.0),(0:0/2.0),(0:0/2.0)",
                    "(1:0/1.0),(0:0/2.0),(0:0/3.0),(0:0/2.0)",
                    "(1:0/1.0),(0:0/2.0),(0:0/2.0),(0:0/3.0)"),
                Arrays.asList(1, 3, 4, 5, 6, 6, 7, 7, 8, 8, 8) },
            new Object[] { "LoopME1000", KShortestPathsGraphs.LOOPME1000,
                GRAPH_WITH_SELFLOOP_AND_MULTIEDGE, 16, 0, 1,
                Arrays.asList(
                    "0,1", "(0:0/1.0),1", "(0:0/2.0),1", "(0:0/1.0),(0:0/1.0),1", "(0:0/3.0),1",
                    "(0:0/2.0),(0:0/1.0),1", "(0:0/1.0),(0:0/1.0),(0:0/1.0),1",
                    "(0:0/1.0),(0:0/2.0),1", "(0:0/3.0),(0:0/1.0),1",
                    "(0:0/1.0),(0:0/2.0),(0:0/1.0),1", "(0:0/1.0),(0:0/1.0),(0:0/1.0),(0:0/1.0),1",
                    "(0:0/2.0),(0:0/1.0),(0:0/1.0),1", "(0:0/1.0),(0:0/3.0),1",
                    "(0:0/2.0),(0:0/2.0),1", "(0:0/4.0),1", "(0:0/1.0),(0:0/1.0),(0:0/2.0),1"),
                Arrays.asList(
                    1000, 1001, 1002, 1002, 1003, 1003, 1003, 1003, 1004, 1004, 1004, 1004, 1004,
                    1004, 1004, 1004) });

        return blendGraphsAlgs(graphs);
    }

    /**
     * Create an instance of this test.
     * 
     * @param name the friendly name
     * @param algInfo the algorithm information (name, factory, incompatibilities)
     * @param graph the graph
     * @param graphProperties the properties of the graph
     * @param k the number of shortest paths to retrieve
     * @param source the source/from/start vertex
     * @param sink the sink/to/end vertex
     * @param expectedKShortestPaths the expected/reference list of k shortest paths
     * @param expectedKShortestWeights the expected/reference list of k shortest path's weights
     */
    public MultiShortestPathsIntegerDWETest(
        String name, AlgorithmInfo<Integer, DefaultWeightedEdge> algInfo,
        Graph<Integer, DefaultWeightedEdge> graph, EnumSet<GraphProperties> graphProperties, int k,
        Integer source, Integer sink, List<String> expectedKShortestPaths,
        List<Number> expectedKShortestWeights)
    {
        super(
            name, algInfo, Integer::valueOf, graph, graphProperties, k, source, sink,
            expectedKShortestPaths, expectedKShortestWeights);
    }
}
