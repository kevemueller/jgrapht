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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.function.Function;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.graph.DirectedMaskSubgraph;
import org.jgrapht.graph.GraphWalk;
import org.jgrapht.graph.MaskSubgraph;

/**
 * Yen's algorithm computes single-source K-shortest loopless paths for a graph with non-negative
 * edge cost. The algorithm was published by Jin Y. Yen in 1971 and employs any shortest path
 * algorithm to find the best path, then proceeds to find K-1 deviations of the best path.
 * 
 * <p>
 * Based on pseudocode published on
 * <a href="https://en.wikipedia.org/wiki/Yen%27s_algorithm">Wikipedia</a>
 * 
 * <p>
 * This implementation will hold a strong reference to all shortest paths returned so far. The
 * algorithm pre-calculates and stores the minimum number of next shortest paths that is needed to
 * determine the actual next shortest path.
 * 
 * <p>
 * The shortest path algorithm to determine the paths may be passed as a parameter. Any constraint
 * of that algorithm must be obeyed, e.g. the default {@link DijkstraShortestPath} will not allow
 * negative edge weights.
 * 
 * <p>
 * The time complexity of the algorithm depends on the chosen shortest path algorithm. With the
 * default @link {@link DijkstraShortestPath} which uses Fibonacci heaps the complexity is $O(K N (M
 * + N log N))$, where N is the number of edges and M is the number of vertices.
 *
 * @author Keve Müller
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 *
 * @since June 1, 2017
 */
public final class YenKShortestPathsIterator<V, E>
    implements Iterator<GraphPath<V, E>>
{
    /**
     * The list of paths found so far sorted by their weight.
     */
    private final ArrayList<GraphPath<V, E>> a;
    /**
     * Candidate next shortest paths.
     */
    private final PriorityQueue<GraphPath<V, E>> b;
    /**
     * The underlying graph on which the shortest paths are searched.
     */
    private final Graph<V, E> graph;
    /**
     * The sink vertex.
     */
    private final V sink;
    /**
     * Factory function to create new instances of the shortest path algorithm to be used.
     */
    private final Function<Graph<V, E>, ShortestPathAlgorithm<V, E>> spFactory;
    /**
     * The index in {@link #a} of the path that will be returned by the next call to
     * {@link #next()}.
     */
    private int nextIndex;

    /**
     * Construct the object for searching k shortest paths in provided graph using the provided
     * shortest path algorithm.
     * <p>
     * It will immediately look for the overall shortest path using an instance of the algorithm
     * provided by spFactory.
     * 
     * @param graph the underlying graph on which the paths are searched
     * @param source the source/from/start vertex
     * @param sink the sink/to/end vertex
     * @param spFactory the factory function to create new instances of the shortest path algorithm.
     */
    public YenKShortestPathsIterator(
        Graph<V, E> graph, V source, V sink,
        Function<Graph<V, E>, ShortestPathAlgorithm<V, E>> spFactory)
    {
        this.spFactory = spFactory;
        this.graph = graph;
        this.sink = sink;
        a = new ArrayList<GraphPath<V, E>>();
        // Determine the shortest path from the source to the sink.
        ShortestPathAlgorithm<V, E> shortestPathAlgorithm = spFactory.apply(graph);
        GraphPath<V, E> p = shortestPathAlgorithm.getPath(source, sink);
        if (null != p) {
            a.add(p);
        }
        nextIndex = 0;
        // // Initialize the heap to store the potential kth shortest paths.
        b = new PriorityQueue<GraphPath<V, E>>(new Comparator<GraphPath<V, E>>()
        {
            @Override
            public int compare(GraphPath<V, E> arg0, GraphPath<V, E> arg1)
            {
                return Double.compare(arg0.getWeight(), arg1.getWeight());
            }
        });
    }

    /**
     * Construct the object for searching k shortest paths in provided graph using
     * {@link DijkstraShortestPath} as the base algorithm.
     * 
     * @param graph the underlying graph on which the paths are searched
     * @param source the source/from/start vertex
     * @param sink the sink/to/end vertex
     */
    public YenKShortestPathsIterator(Graph<V, E> graph, V source, V sink)
    {
        this(graph, source, sink, DijkstraShortestPath<V, E>::new);
    }

    @Override
    public boolean hasNext()
    {
        if (nextIndex < a.size()) {
            return true;
        }
        tryAddNext();
        return nextIndex < a.size();
    }

    @Override
    public GraphPath<V, E> next()
    {
        if (nextIndex < a.size()) {
            return a.get(nextIndex++);
        }
        tryAddNext();
        if (nextIndex >= a.size()) {
            throw new NoSuchElementException();
        }
        return a.get(nextIndex++);
    }

    /**
     * Determine and store in {@link #b} the next shortest paths and determine the actual shortest
     * path and add it to {@link #a}. Instead of altering the underlying graph, this implementation
     * masks edges and nodes that shall not be considered using {@link MaskSubgraph}.
     */
    private void tryAddNext()
    {
        GraphPath<V, E> lastShortestPath = a.get(a.size() - 1);
        // The spur node ranges from the first node to the next to last
        // node in the previous k-shortest path.
        for (int i = 0; i < lastShortestPath.getLength() - 1; i++) {
            // Spur node is retrieved from the previous k-shortest path
            V spurNode = lastShortestPath.getVertexList().get(i);
            // The sequence of nodes from the source to the spur node of
            // the previous k-shortest path.
            List<V> rootPath = lastShortestPath.getVertexList().subList(0, i);
            List<V> maskedVertices = new ArrayList<V>();
            List<E> maskedEdges = new ArrayList<E>();
            for (GraphPath<V, E> path : a) {
                if (rootPath.equals(path.getVertexList().subList(0, i))) {
                    // Remove the links that are part of the previous
                    // shortest paths which share the same root path.
                    maskedEdges.add(
                        graph
                            .getEdge(path.getVertexList().get(i), path.getVertexList().get(i + 1)));
                }
            }
            for (V rootPathNode : rootPath) {
                if (!rootPathNode.equals(spurNode)) {
                    maskedVertices.add(rootPathNode);
                }
            }

            MaskSubgraph<V, E> maskedSubgraph;
            if (graph instanceof DirectedGraph) {
                maskedSubgraph = new DirectedMaskSubgraph<V, E>(
                    (DirectedGraph<V, E>) graph, maskedVertices::contains, maskedEdges::contains);
            } else {
                maskedSubgraph =
                    new MaskSubgraph<V, E>(graph, maskedVertices::contains, maskedEdges::contains);
            }
            // Calculate the spur path from the spur node to the sink.
            ShortestPathAlgorithm<V, E> maskedShortestSpurPathAlgorithm =
                spFactory.apply(maskedSubgraph);
            GraphPath<V, E> spurPath = maskedShortestSpurPathAlgorithm.getPath(spurNode, sink);

            if (null != spurPath && 0 != spurPath.getLength()) { // BellmannFord
                                                                 // returns
                                                                 // empty
                                                                 // path
                                                                 // instead
                                                                 // of null
                // Entire path is made up of the root path and spur path.
                ArrayList<E> totalPath = new ArrayList<E>();
                Iterator<V> iter;
                V first, current;
                double weight = 0.0;
                if (!rootPath.isEmpty()) {
                    iter = rootPath.iterator();
                    first = iter.next();
                    current = first;
                    while (iter.hasNext()) {
                        V v = iter.next();
                        E e = graph.getEdge(current, v);
                        weight += graph.getEdgeWeight(e);
                        totalPath.add(e);
                        current = v;
                    }
                    iter = spurPath.getVertexList().iterator();
                } else {
                    iter = spurPath.getVertexList().iterator();
                    first = iter.next();
                    current = first;
                }
                while (iter.hasNext()) {
                    V v = iter.next();
                    E e = graph.getEdge(current, v);
                    weight += graph.getEdgeWeight(e);
                    totalPath.add(e);
                    current = v;
                }
                // Add the potential k-shortest path to the heap.
                GraphWalk<V, E> gw = new GraphWalk<V, E>(graph, first, current, totalPath, weight);
                b.add(gw);
            }
            // Add back the edges and nodes that were removed from the
            // graph.
            // restore edges to Graph;
            // restore nodes in rootPath to Graph;
            // not needed, we simply throw away the mask.
        }
        if (b.isEmpty()) {
            // This handles the case of there being no spur paths, or no
            // spur paths left.
            // This could happen if the spur paths have already been
            // exhausted (added to A),
            // or there are no spur paths at all - such as when both the
            // source and sink vertices
            // lie along a "dead end".
            return;
        }
        // Sort the potential k-shortest paths by cost.
        // This is done already at insertion to the PriorityQueue.
        // Add the lowest cost path becomes the k-shortest path.
        GraphPath<V, E> csp;
        do {
            csp = b.poll();
            if (null == csp) {
                return;
            }
        } while (lastShortestPath.getEdgeList().equals(csp.getEdgeList()));
        a.add(csp);
    }
}
