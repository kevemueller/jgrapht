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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.KShortestPathAlgorithm;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;

/**
 * Yen's algorithm computes single-source K-shortest loopless paths for a graph with non-negative
 * edge cost. The algorithm was published by Jin Y. Yen in 1971 and employs any shortest path
 * algorithm to find the best path, then proceeds to find K-1 deviations of the best path.
 * <p>
 * Based on pseudocode published at https://en.wikipedia.org/wiki/Yen%27s_algorithm
 * <p>
 * This class uses {@link YenKShortestPathsIterator} to perform the actual work.
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
     * Hint on the number of paths that should be returned in a call to {@link #getPaths(Object, Object)}.
     */
    private final int hintK;
    /**
     * Factory function to create new instances of the shortest path algorithm to be used.
     */
    private final Function<Graph<V, E>, ShortestPathAlgorithm<V, E>> spFactory;

    /**
     * Construct the object for searching k shortest paths in provided graph. The shortest path
     * algorithm defaults to {@link DijkstraShortestPath}.
     * 
     * @param graph the underlying graph on which the paths are searched
     * @param hintK the hint on how many paths shall be returned by {@link #getPaths(Object, Object)}.
     */
    public YenKShortestPaths(Graph<V, E> graph, int hintK)
    {
        this(graph, hintK, DijkstraShortestPath<V, E>::new);
    }

    /**
     * Construct the object for searching k shortest paths in provided graph using the provided
     * shortest path algorithm.
     * 
     * @param graph the underlying graph on which the paths are searched.
     * @param hintK the hint on how many paths shall be returned by {@link #getPaths(Object, Object)}.
     * @param spFactory the factory function to create new instances of the shortest path algorithm.
     */
    public YenKShortestPaths(
        Graph<V, E> graph, int hintK, Function<Graph<V, E>, ShortestPathAlgorithm<V, E>> spFactory)
    {
        this.graph = Objects.requireNonNull(graph);
        this.hintK = hintK;
        this.spFactory = Objects.requireNonNull(spFactory);
    }

    /**
     * Get an iterator that returns the k-shortest paths from source to sink in increasing order.
     * 
     * @param source the source/from/start vertex.
     * @param sink the sink/to/end vertex.
     * @return an iterator for the k-shortest paths.
     */
    public Iterator<GraphPath<V, E>> getPathsIterator(V source, V sink)
    {
        return new YenKShortestPathsIterator<V, E>(graph, source, sink, spFactory);
    }

    @Override
    public List<GraphPath<V, E>> getPaths(V source, V sink)
    {
        ArrayList<GraphPath<V, E>> a = new ArrayList<GraphPath<V, E>>();
        YenKShortestPathsIterator<V, E> iterator =
            new YenKShortestPathsIterator<V, E>(graph, source, sink, spFactory);
        for (int k = 0; k < hintK && iterator.hasNext(); k++) {
            a.add(iterator.next());
        }
        return a;
    }
}
