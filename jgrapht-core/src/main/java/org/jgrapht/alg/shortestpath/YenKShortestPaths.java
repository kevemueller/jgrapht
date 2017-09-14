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
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.GraphTests;
import org.jgrapht.alg.interfaces.KShortestPathAlgorithm;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;

/**
 * Yen's algorithm computes single-source K-shortest loopless paths for a graph using an underlying
 * shortest path algorithm passed as a parameter. The algorithm was published by Jin Y. Yen in 1971
 * and employs any shortest path algorithm to find the best path, then proceeds to find K-1
 * deviations of the best path.
 * <p>
 * Based on pseudocode published on
 * <a href="https://en.wikipedia.org/wiki/Yen%27s_algorithm">Wikipedia</a>
 * 
 * <p>
 * This implementation will hold a strong reference to all shortest paths returned so far. The
 * algorithm pre-calculates and stores a small number of next shortest paths that is needed to
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
 * <p>
 * This class uses {@link YenShortestPathsIterator} to perform the actual work.
 *
 * @author Keve Müller
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 *
 * @since June 1, 2017
 */
public final class YenKShortestPaths<V, E>
    implements KShortestPathAlgorithm<V, E>
{
    /**
     * Underlying graph on which the shortest path are searched.
     */
    private final Graph<V, E> graph;
    /**
     * The maximum number of paths that shall be returned in a call to
     * {@link #getPaths(Object, Object)}.
     */
    private final int k;
    /**
     * Factory function to create new instances of the shortest path algorithm to be used.
     */
    private final Function<Graph<V, E>, ShortestPathAlgorithm<V, E>> spFactory;

    /**
     * Construct the object for searching k shortest paths in provided graph. The shortest path
     * algorithm defaults to {@link DijkstraShortestPath}.
     * 
     * @param graph the underlying graph on which the paths are searched
     * @param k the maximum number of paths that shall be returned by
     *        {@link #getPaths(Object, Object)}.
     */
    public YenKShortestPaths(Graph<V, E> graph, int k)
    {
        this(graph, k, DijkstraShortestPath<V, E>::new);
    }

    /**
     * Construct the object for searching k shortest paths in provided simple graph using the
     * provided shortest path algorithm.
     * 
     * @param graph the underlying graph on which the paths are searched.
     * @param k the maximum number of paths that shall be returned by
     *        {@link #getPaths(Object, Object)}.
     * @param spFactory the factory function to create new instances of the shortest path algorithm.
     */
    public YenKShortestPaths(
        Graph<V, E> graph, int k, Function<Graph<V, E>, ShortestPathAlgorithm<V, E>> spFactory)
    {
        if (!GraphTests.isSimple(graph))
            throw new IllegalArgumentException("This implementation only supports simple graphs");
        this.graph = graph;
        this.k = k;
        this.spFactory = Objects.requireNonNull(spFactory);
    }

    /**
     * Get an iterator over all shortest paths between a source vertex and a sink vertex. The paths
     * are returned in increasing order of their cost. The first path returned is guaranteed to be
     * the cheapest path between the source and the sink vertex.
     * 
     * @param source the source/from/start vertex.
     * @param sink the sink/to/end vertex.
     * @return an iterator for the k-shortest paths.
     */
    public Iterator<GraphPath<V, E>> getPathsIterator(V source, V sink)
    {
        return new YenShortestPathsIterator<V, E>(graph, source, sink, spFactory);
    }

    /**
     * Get a list of shortest paths from a source vertex to a sink vertex. If no such paths exist
     * this method returns an empty list. The returned List contains at most k paths, where k is the number
     * passed in the constructor. If the graph contains less than k shortest paths, only thos paths
     * will be returned in the List.
     * 
     * @param source the source vertex
     * @param sink the target vertex
     * @return a list of shortest paths
     */
    public List<GraphPath<V, E>> getPaths(V source, V sink)
    {
        ArrayList<GraphPath<V, E>> a = new ArrayList<GraphPath<V, E>>();
        YenShortestPathsIterator<V, E> iterator =
            new YenShortestPathsIterator<V, E>(graph, source, sink, spFactory);
        for (int i = 0; i < k && iterator.hasNext(); i++) {
            a.add(iterator.next());
        }
        return a;
    }
}
