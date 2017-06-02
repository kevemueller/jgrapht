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
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.alg.NotBiconnectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Parameterized JUnit test of {@link AbstractMultiShortestPathsTest} for Graphs of
 * <String,DefaultEdge>.
 * 
 * @author Keve Müller
 * 
 * @since June 1, 2017
 * 
 */
@RunWith(Parameterized.class)
public final class MultiShortestPathsStringDETest
    extends AbstractMultiShortestPathsTest<String, DefaultEdge>
{

    /**
     * Return the parameters to instantiate this test.
     * 
     * @return the collection of parameters.
     */
    @Parameters(name = "{0}")
    public static Collection<Object[]> data()
    {
        List<Object[]> graphs = Collections.singletonList(
            new Object[] { "NotBiconnectedGraph", new NotBiconnectedGraph(), EnumSet.of(GraphProperties.CYCLES), 5, "0",
                "5", Arrays.asList("0,3,5", "0,3,1,4,5"), Arrays.asList(2, 4) });

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
    public MultiShortestPathsStringDETest(
        String name, AlgorithmInfo<String, DefaultEdge> algInfo, Graph<String, DefaultEdge> graph,
        EnumSet<GraphProperties> graphProperties, int k, String source, String sink,
        List<String> expectedKShortestPaths, List<Number> expectedKShortestWeights)
    {
        super(
            name, algInfo, String::valueOf, graph, graphProperties, k, source, sink,
            expectedKShortestPaths, expectedKShortestWeights);
    }
}
