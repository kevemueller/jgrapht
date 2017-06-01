/*
 * (C) Copyright 2017, by Keve Müller.
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
public final class MultiShortestPathsStringDWETest
    extends AbstractMultiShortestPathsTest<String, DefaultWeightedEdge>
{

    /**
     * Return the parameters to instantiate this test.
     * 
     * @return the collection of parameters.
     */
    @Parameters(name = "{0}")
    public static Collection<Object[]> data()
    {
        List<
            Object[]> graphs =
                Arrays
                    .asList(
                        new Object[] { "KShortestPathCompleteGraph4",
                            new KShortestPathCompleteGraph4(), GRAPH_WITH_LOOP, 5, "vS", "v3",
                            Arrays.asList(
                                "vS,v1,v3", "vS,v2,v3", "vS,v2,v1,v3", "vS,v1,v2,v3", "vS,v3"),
                            Arrays.asList(2, 2, 3, 3, 1000) },
                        new Object[] { "KShortestPathCompleteGraph5",
                            new KShortestPathCompleteGraph5(), GRAPH_WITH_LOOP, 9, "vS", "v4",
                            Arrays.asList(
                                "vS,v1,v4", "vS,v2,v4", "vS,v3,v4", "vS,v1,v2,v4", "vS,v1,v3,v4",
                                "vS,v2,v1,v4", "vS,v2,v3,v4", "vS,v3,v1,v4", "vS,v3,v2,v4"),
                            Arrays.asList(2, 2, 2, 3, 3, 3, 3, 3, 3) },
                        new Object[] { "KShortestPathCompleteGraph6",
                            new KShortestPathCompleteGraph6(), GRAPH_WITH_LOOP, 6, "vS", "v5",
                            Arrays.asList(
                                "vS,v1,v5", "vS,v4,v5", "vS,v3,v5", "vS,v2,v5", "vS,v2,v1,v5",
                                "vS,v2,v4,v5"),
                            Arrays.asList(2, 2, 2, 2, 3, 3) },
                        new Object[] { "KSPExampleGraph", new KSPExampleGraph(), GRAPH_WITH_LOOP, 5,
                            "S", "T", Arrays.asList("S,T", "S,A,D,E,C,T", "S,A,D,E,C,B,T"),
                            Arrays.asList(1, 104, 105) },
                        new Object[] { "Picture1Graph", new Picture1Graph(),
                            GRAPH_WITH_NEGATIVE_WEIGHT, 10, "vS", "v5",
                            Arrays.asList("vS,v1,v5", "vS,v2,v5"), Arrays.asList(6.0, 8.0) },
                        new Object[] { "Eppstein", KShortestPathsGraphs.EPPSTEIN, GRAPH_SIMPLE, 20,
                            "0", "11",
                            Arrays.asList(
                                "0,4,5,6,10,11", "0,1,2,3,7,11", "0,1,2,6,10,11", "0,4,5,9,10,11",
                                "0,1,5,6,10,11", "0,4,5,6,7,11", "0,4,8,9,10,11", "0,1,2,6,7,11",
                                "0,1,5,9,10,11", "0,1,5,6,7,11"),
                            Arrays.asList(55, 58, 59, 61, 62, 64, 65, 68, 68, 71) },
                        new Object[] { "Graehl", KShortestPathsGraphs.GRAEHL, GRAPH_WITH_LOOP, 7,
                            "0", "1",
                            Arrays.asList(
                                "0,5,1", "0,0,5,1", "0,0,0,5,1", "0,0,0,0,5,1", "0,0,0,0,0,5,1",
                                "0,0,0,0,0,0,5,1", "0,0,0,0,0,0,0,5,1"),
                            Arrays.asList(.6, .65, .7, .75, .8, .85, .9) },
                        new Object[] { "NoLoopME", KShortestPathsGraphs.NOLOOPME,
                            GRAPH_WITH_MULTIEDGE, 20, "0", "2",
                            Arrays.asList("(0:1/1.0),2", "(0:1/2.0),2", "(0:1/3.0),2"),
                            Arrays.asList(2, 3, 4) },
                        new Object[] { "LoopME", KShortestPathsGraphs.LOOPME,
                            GRAPH_WITH_LOOP_AND_MULTIEDGE, 11, "0", "1",
                            Arrays.asList(
                                "0,1", "(0:0/2.0),1", "(0:0/3.0),1", "(0:0/2.0),(0:0/2.0),1",
                                "(0:0/3.0),(0:0/2.0),1", "(0:0/2.0),(0:0/3.0),1",
                                "(0:0/2.0),(0:0/2.0),(0:0/2.0),1", "(0:0/3.0),(0:0/3.0),1",
                                "(0:0/3.0),(0:0/2.0),(0:0/2.0),1",
                                "(0:0/2.0),(0:0/2.0),(0:0/3.0),1",
                                "(0:0/2.0),(0:0/3.0),(0:0/2.0),1"),
                            Arrays.asList(1, 3, 4, 5, 6, 6, 7, 7, 8, 8, 8) },
                        new Object[] { "LoopME1000", KShortestPathsGraphs.LOOPME1000,
                            GRAPH_WITH_MULTIEDGE, 16, "0", "1",
                            Arrays.asList(
                                "0,1", "(0:0/1.0),1", "(0:0/2.0),1", "(0:0/1.0),(0:0/1.0),1",
                                "(0:0/3.0),1", "(0:0/2.0),(0:0/1.0),1",
                                "(0:0/1.0),(0:0/1.0),(0:0/1.0),1", "(0:0/1.0),(0:0/2.0),1",
                                "(0:0/3.0),(0:0/1.0),1", "(0:0/1.0),(0:0/2.0),(0:0/1.0),1",
                                "(0:0/1.0),(0:0/1.0),(0:0/1.0),(0:0/1.0),1",
                                "(0:0/2.0),(0:0/1.0),(0:0/1.0),1", "(0:0/1.0),(0:0/3.0),1",
                                "(0:0/2.0),(0:0/2.0),1", "(0:0/4.0),1",
                                "(0:0/1.0),(0:0/1.0),(0:0/2.0),1"),
                            Arrays.asList(
                                1000, 1001, 1002, 1002, 1003, 1003, 1003, 1003, 1004, 1004, 1004,
                                1004, 1004, 1004, 1004, 1004) });
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
    public MultiShortestPathsStringDWETest(
        String name, AlgorithmInfo<String, DefaultWeightedEdge> algInfo,
        Graph<String, DefaultWeightedEdge> graph, EnumSet<GraphProperties> graphProperties, int k,
        String source, String sink, List<String> expectedKShortestPaths,
        List<Number> expectedKShortestWeights)
    {
        super(
            name, algInfo, String::valueOf, graph, graphProperties, k, source, sink,
            expectedKShortestPaths, expectedKShortestWeights);
    }
}
