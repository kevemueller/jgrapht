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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.KShortestPathAlgorithm;
import org.jgrapht.graph.GraphWalk;
import org.junit.Test;

/**
 * Abstract base class for JUnit tests of k-shortest path algorithms. Tests are performed against
 * these algorithms:
 * <ul>
 * <li>{@link KShortestPaths}</li>
 * <li>{@link YenKShortestPaths} with {@link DijkstraShortestPath}</li>
 * <li>{@link YenKShortestPaths} with {@link BellmanFordShortestPath}</li>
 * </ul>
 * 
 * @author Keve Müller
 * 
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 *
 * @since June 1, 2017
 * 
 */
public abstract class AbstractMultiShortestPathsTest<V, E>
{
    private static final double EPSILON = 0.00000005;
    private final AlgorithmInfo<V, E> algInfo;
    private final Graph<V, E> graph;
    private final int k;
    private final V source;
    private final V sink;
    private final List<GraphPath<V, E>> expectedPaths;
    private final EnumSet<GraphProperties> graphProperties;

    /**
     * Helper enumeration to hold information about a graph's properties.
     */
    protected static enum GraphProperties
    {
        LOOP,
        MULTIEDGE,
        NEGATIVE_WEIGHT;
    }

    protected static EnumSet<GraphProperties> GRAPH_SIMPLE = EnumSet.noneOf(GraphProperties.class);
    protected static EnumSet<GraphProperties> GRAPH_NONE = EnumSet.noneOf(GraphProperties.class);
    protected static EnumSet<GraphProperties> GRAPH_WITH_LOOP = EnumSet.of(GraphProperties.LOOP);
    protected static EnumSet<GraphProperties> GRAPH_WITH_MULTIEDGE =
        EnumSet.of(GraphProperties.MULTIEDGE);
    protected static EnumSet<GraphProperties> GRAPH_WITH_NEGATIVE_WEIGHT =
        EnumSet.of(GraphProperties.NEGATIVE_WEIGHT);
    protected static EnumSet<GraphProperties> GRAPH_WITH_LOOP_AND_MULTIEDGE =
        EnumSet.of(GraphProperties.LOOP, GraphProperties.MULTIEDGE);

    /**
     * Helper class to hold information about an algorithm.
     */
    protected final static class AlgorithmInfo<V, E>
    {
        private final String name;
        private final EnumSet<GraphProperties> algIncompatibility;
        private final BiFunction<Graph<V, E>, Integer, KShortestPathAlgorithm<V, E>> algFactory;

        /**
         * Create an instance of this class.
         * 
         * @param name The algorithm's name.
         * @param algIncompatibility A set of GraphProperties this algorithm is incompatible with.
         * @param algFactory A factory function that creates new instances of the algorithm.
         */
        public AlgorithmInfo(
            String name, EnumSet<GraphProperties> algIncompatibility,
            BiFunction<Graph<V, E>, Integer, KShortestPathAlgorithm<V, E>> algFactory)
        {
            this.name = name;
            this.algIncompatibility = algIncompatibility;
            this.algFactory = algFactory;
        }
    }

    /**
     * Create the test object using the provided parameters.
     * 
     * @param name the friendly name
     * @param alg the algorithm information (name, factory, incompatibilities)
     * @param fVertexValue the function converting a vertex from string to V
     * @param graph the graph
     * @param graphProperties the properties of the graph
     * @param k the number of shortest paths to retrieve
     * @param source the source/from/start vertex
     * @param sink the sink/to/end vertex
     * @param expectedKShortestPaths the expected/reference list of k shortest paths
     * @param expectedKShortestWeights the expected/reference list of k shortest path's weights
     */
    public AbstractMultiShortestPathsTest(
        String name, AlgorithmInfo<V, E> alg, Function<String, V> fVertexValue, Graph<V, E> graph,
        EnumSet<GraphProperties> graphProperties, int k, V source, V sink,
        List<String> expectedKShortestPaths, List<Number> expectedKShortestWeights)
    {
        this.algInfo = alg;
        this.graph = graph;
        this.graphProperties = graphProperties;
        this.k = k;
        this.source = source;
        this.sink = sink;
        assert (expectedKShortestPaths.size() == expectedKShortestWeights.size());
        expectedPaths = new ArrayList<GraphPath<V, E>>(expectedKShortestPaths.size());
        for (int i = 0; i < expectedKShortestPaths.size(); i++) {
            String[] edgeOrNodeList = expectedKShortestPaths.get(i).split(",");
            double weight = expectedKShortestWeights.get(i).doubleValue();
            expectedPaths.add(edgeOrNodeListToPath(graph, edgeOrNodeList, weight, fVertexValue));
        }
    }

    /**
     * Test that the paths returned are
     * <ul>
     * <li>starting from the source vertex.</li>
     * <li>ending at the sink vertex.</li>
     * <li>the total cost of the path equals the sum of its edge costs</li>
     * <li>are returned in increasing order of cost.</li>
     * </ul>
     */
    @Test
    public void testBasics()
    {
        assumeTrue(Collections.disjoint(algInfo.algIncompatibility, graphProperties));
        System.out.println("Expecting: ");
        System.out.println(expectedPaths);

        List<GraphPath<V, E>> paths = getPaths();

        System.out.println("Returned: ");
        System.out.println(paths);

        double lastWeight = Double.MIN_VALUE;
        for (GraphPath<V, E> p : paths) {
            assertEquals(source, p.getStartVertex());
            assertEquals(sink, p.getEndVertex());
            double weight = p.getWeight();
            double edgesWeight =
                p.getEdgeList().stream().map(graph::getEdgeWeight).reduce(0.0, Double::sum);
            assertEquals(weight, edgesWeight, EPSILON);
            assertTrue(weight >= lastWeight);
            lastWeight = weight;
        }
    }

    /**
     * Test that the paths returned are matching the expected set of paths.
     */
    @Test
    public void testPaths()
    {
        assumeTrue(Collections.disjoint(algInfo.algIncompatibility, graphProperties));
        System.out.println("Expecting: ");
        System.out.println(expectedPaths);
        List<GraphPath<V, E>> paths = getPaths();
        System.out.println("Returned: ");
        System.out.println(paths);
        assertKPathsEquals(expectedPaths, paths);
    }

    /**
     * Helper to create the cartesian product of graphs cross algs by mixing in the algs at the
     * right place.
     * 
     * @param graphs the graph information.
     * @return the collection of graphs times algs.
     * 
     * @param <V> the graph vertex type
     * @param <E> the graph edge type
     * 
     */
    protected static <V, E> Collection<Object[]> blendGraphsAlgs(List<Object[]> graphs)
    {
        ArrayList<AlgorithmInfo<V, E>> algs =
            new ArrayList<AbstractMultiShortestPathsTest.AlgorithmInfo<V, E>>();
        algs.add(new AlgorithmInfo<V, E>("KShortestPaths", GRAPH_NONE, KShortestPaths<V, E>::new));
        algs.add(new AlgorithmInfo<V, E>("YenKShortestPaths+Dijkstra", EnumSet.of(
            GraphProperties.LOOP, GraphProperties.NEGATIVE_WEIGHT, GraphProperties.MULTIEDGE),
            YenKShortestPaths<V, E>::new));
        algs.add(new AlgorithmInfo<V, E>(
            "YenKShortestPaths+BellmanFord",
            EnumSet.of(GraphProperties.LOOP, GraphProperties.MULTIEDGE),
            (g, k) -> new YenKShortestPaths<V, E>(g, k, BellmanFordShortestPath<V, E>::new)));
        algs.add(
            new AlgorithmInfo<V, E>("Eppstein", GRAPH_NONE, EppsteinKShortestPaths<V, E>::new));

        ArrayList<Object[]> params = new ArrayList<Object[]>();
        for (Object[] graph : graphs) {
            for (AlgorithmInfo<V, E> alg : algs) {
                Object[] graphalg = new Object[graph.length + 1];
                graphalg[0] = alg.name + "/" + graph[0];
                graphalg[1] = alg;
                System.arraycopy(graph, 1, graphalg, 2, graph.length - 1);
                params.add(graphalg);
            }
        }
        return params;
    }

    /**
     * Helper to
     * 
     * @return the k shortest paths.
     */
    private List<GraphPath<V, E>> getPaths()
    {
        KShortestPathAlgorithm<V, E> pathFinder = algInfo.algFactory.apply(graph, k);
        return pathFinder.getPaths(source, sink);
    }

    /**
     * Helper to compare k-shortest paths reference with actual returned values. Asserts that
     * <ol>
     * <li>The number of paths equal</li>
     * <li>The weights equal</li>
     * <li>The edgelists of the returned paths equal</li>
     * </ol>
     * The comparison allows paths of the same length to be returned in arbitrary order.
     * 
     * @param expectedPaths reference paths
     * @param paths actual paths.
     */
    private static <V,
        E> void assertKPathsEquals(List<GraphPath<V, E>> expectedPaths, List<GraphPath<V, E>> paths)
    {
        // graphwalk does not override equals
        // the paths with the same weight might be returned in any order

        // ensure we got the same number of paths and the path total weights are
        // matching
        assertEquals("number of paths differ", expectedPaths.size(), paths.size());
        for (int i = 0; i < paths.size(); i++) {
            assertEquals(
                "path weight mismatch @" + i, expectedPaths.get(i).getWeight(),
                paths.get(i).getWeight(), 0.000005);
        }

        {
            // ensure we got the right paths by looking at the vertexlists
            List<List<V>> expectedVertexLists =
                expectedPaths.stream().map(GraphPath::getVertexList).collect(Collectors.toList());
            List<List<V>> pathsVertexLists =
                paths.stream().map(GraphPath::getVertexList).collect(Collectors.toList());

            // assert that the bag of expected equals the bag of paths
            ArrayList<List<V>> add1 = new ArrayList<>(expectedVertexLists);
            add1.removeAll(pathsVertexLists);
            ArrayList<List<V>> add2 = new ArrayList<>(pathsVertexLists);
            add2.removeAll(expectedVertexLists);

            assertTrue(
                "additional paths were found: " + add2,
                expectedVertexLists.containsAll(pathsVertexLists));
            assertTrue(
                "not all expected paths were found: " + add1,
                pathsVertexLists.containsAll(expectedVertexLists));
        }

        {
            // ensure we got the right paths by looking at the edgelists
            List<List<E>> expectedEdgeLists =
                expectedPaths.stream().map(GraphPath::getEdgeList).collect(Collectors.toList());
            List<List<E>> pathsEdgeLists =
                paths.stream().map(GraphPath::getEdgeList).collect(Collectors.toList());

            // assert that the bag of expected equals the bag of paths
            ArrayList<List<E>> add1 = new ArrayList<>(expectedEdgeLists);
            add1.removeAll(pathsEdgeLists);
            ArrayList<List<E>> add2 = new ArrayList<>(pathsEdgeLists);
            add2.removeAll(expectedEdgeLists);

            assertTrue(
                "additional paths were found: " + add2,
                expectedEdgeLists.containsAll(pathsEdgeLists));
            assertTrue(
                "not all expected paths were found: " + add1,
                pathsEdgeLists.containsAll(expectedEdgeLists));
        }
    }

    /**
     * Helper to transform a string representation of a GraphPath into a GraphPath. An element of
     * edgesOrNodes is either matching a from:to/weight in parentheses or is a node.
     * 
     * @param graph the underlying graph
     * @param edgesOrNodes the array of edges or node strings
     * @param weight the weight
     * @return a GraphPath
     */
    private static <V, E> GraphPath<V, E> edgeOrNodeListToPath(
        Graph<V, E> graph, String[] edgesOrNodes, double weight, Function<String, V> fValueOf)
    {
        ArrayList<E> edgeList = new ArrayList<E>();
        V current = null;
        V from = null;
        for (int i = 0; i < edgesOrNodes.length; i++) {
            if ('(' == edgesOrNodes[i].charAt(0)) {
                String[] edgeS = edgesOrNodes[i].split("[\\(\\):/]");
                V v0 = fValueOf.apply(edgeS[1]);
                if (null != current && !current.equals(v0)) {
                    throw new IllegalArgumentException(
                        "edge not from previous node" + edgesOrNodes[i]);
                }
                current = v0;
                if (null == from) {
                    from = current;
                }
                V vertex = fValueOf.apply(edgeS[2]);
                Double ew = Double.valueOf(edgeS[3]);
                Set<E> allEdges = graph.getAllEdges(current, vertex);
                E foundEdge = null;
                for (E e : allEdges) {
                    if (ew.equals(graph.getEdgeWeight(e))) {
                        foundEdge = e;
                        break;
                    }
                }
                if (null == foundEdge) {
                    throw new IllegalArgumentException(
                        "no edge with such weight: " + current + " to " + vertex + "/" + ew);
                } else {
                    edgeList.add(foundEdge);
                    current = vertex;
                }
            } else {
                V vertex = fValueOf.apply(edgesOrNodes[i]);
                if (null != current) {
                    edgeList.add(graph.getEdge(current, vertex));
                }
                current = vertex;
                if (null == from) {
                    from = current;
                }
            }
        }
        return new GraphWalk<V, E>(graph, from, current, edgeList, weight);
    }
}
