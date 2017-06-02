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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.KShortestPathAlgorithm;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.EdgeReversedGraph;
import org.jgrapht.graph.GraphWalk;
import org.jgrapht.traverse.ClosestFirstIterator;

/**
 * An implementation of the "K Shortest Paths" problem following the algorithm introduced in
 * "Finding the k Shortest Paths" by David Eppstein, March 31, 1997. This implementation tries to
 * adhere to the principles described in the paper as close as Java can get. While implementing the
 * following material was used:
 * <ul>
 * <li><a href="https://www.ics.uci.edu/~eppstein/pubs/Epp-SJC-98.pdf">The paper</a></li>
 * <li><a href="http://www.isi.edu/natural-language/people/epp-cs562.pdf">Course notes</a></li>
 * <li><a href="https://www.ics.uci.edu/~eppstein/pubs/graehl.zip">Graehl's implementation</a></li>
 * </ul>
 * <p>
 * The time complexity of the algorithm is $O(m + n log n + k n)$, where n is the number of edges
 * and m is the number of vertices.
 * 
 * @author Keve Müller
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public final class EppsteinKShortestPaths<V, E>
    implements KShortestPathAlgorithm<V, E>
{
    /**
     * The underlying graph.
     */
    private final Graph<V, E> graph;
    /**
     * A hint on the number of elements to be returned when calling getPaths.
     */
    private final int hintK;

    /**
     * Create an instance of this class.
     * 
     * @param graph the underlying graph.
     * @param hintK the hint on how many paths to return in a call to
     *        {@link #getPaths(Object, Object)}
     */
    public EppsteinKShortestPaths(Graph<V, E> graph, int hintK)
    {
        this.graph = Objects.requireNonNull(graph);
        this.hintK = hintK;
        assert (graph.getType().isDirected());
    }

    @Override
    public List<GraphPath<V, E>> getPaths(V startVertex, V endVertex)
    {
        ArrayList<GraphPath<V, E>> paths = new ArrayList<GraphPath<V, E>>();
        Iterator<GraphPath<V, E>> iter =
            new EppsteinKShortestPathsImpl<V, E>(graph, startVertex, endVertex).iterator();
        int k = 0;
        while (iter.hasNext() && k++ < hintK) {
            paths.add(iter.next());
        }
        return paths;
    }

    /**
     * Return an iterator that on-the-fly produces the k shortest paths and returns them in
     * increasing order. Note that if the underlying graph has a loop, this iterator returns an
     * infinite number of elements.
     * 
     * @param startVertex the start/from/source vertex
     * @param endVertex the end/to/sink vertex
     * @return an iterator that produces the shortest graphs.
     */
    public Iterator<GraphPath<V, E>> getPathsIterator(V startVertex, V endVertex)
    {
        return new EppsteinKShortestPathsImpl<V, E>(graph, startVertex, endVertex).iterator();
    }

    /**
     * The class performing the actual work.
     */
    private static class EppsteinKShortestPathsImpl<V, E>
    {
        /**
         * The underlying graph.
         */
        private final Graph<V, E> graph;
        /**
         * For each vertex v in graph, give first edge on the shortest path from v to the sink.
         */
        private final HashMap<V, E> shortestPathEdge;
        /**
         * For each vertex v in graph, provide the heap of the sidetrack edges.
         */
        private final HashMap<V, HoutHeap<E>> hOutHeaps;
        /**
         * For each vertex v in graph, provide the shared heaps of sidetrack edges.
         */
        private final HashMap<V, EppsteinTreeHeap<E>> hTHeaps;
        /**
         * From/start/source vertex.
         */
        private final V from;
        /**
         * To/end/sink vertex.
         */
        private final V to;

        /**
         * Construct the instance provided the graph and the vertices.
         * 
         * @param graph the graph.
         * @param from the from/start/source vertex.
         * @param to tehe to/end/sink vertex.
         */
        public EppsteinKShortestPathsImpl(Graph<V, E> graph, V from, V to)
        {
            this.graph = Objects.requireNonNull(graph);
            this.from = from;
            this.to = to;
            assert (graph.getType().isDirected());
            final EdgeReversedGraph<V, E> transposedGraph = new EdgeReversedGraph<V, E>(graph);

            HashMap<V, Double> shortestWeights = new HashMap<V, Double>();
            ClosestFirstIterator<V, E> cfi = new ClosestFirstIterator<V, E>(transposedGraph, to);

            // initially all edges are potential sidetrack edges
            final HashSet<E> sideTrackEdges = new HashSet<E>(graph.edgeSet());
            shortestPathEdge = new HashMap<V, E>();
            while (cfi.hasNext()) {
                V v = cfi.next();
                E e = cfi.getSpanningTreeEdge(v);
                V ov;
                double w;
                if (null != e) {
                    sideTrackEdges.remove(e);
                    ov = Graphs.getOppositeVertex(graph, e, v);
                    w = graph.getEdgeWeight(e) + shortestWeights.get(ov);
                    shortestPathEdge.put(v, e);
                } else {
                    ov = null;
                    w = 0.0;
                }
                shortestWeights.put(v, w);
            }

            Double shortestPathWeight = shortestWeights.get(from);
            if (null == shortestPathWeight) {
                // there is no single shortest path
                hTHeaps = null;
                hOutHeaps = null;
            } else {
                HashMap<E, Double> sidetrackWeights = new HashMap<E, Double>();
                for (E e : sideTrackEdges) {
                    Double targetWeight = shortestWeights.get(graph.getEdgeTarget(e));
                    Double sourceWeight = shortestWeights.get(graph.getEdgeSource(e));
                    if (null != targetWeight) {
                        double sidetrackWeight =
                            graph.getEdgeWeight(e) + targetWeight - sourceWeight;
                        sidetrackWeights.put(e, sidetrackWeight);
                    }
                }

                hOutHeaps = new HashMap<V, HoutHeap<E>>();
                for (V v : graph.vertexSet()) {
                    HoutHeap<E> hOutHeap = new HoutHeap<E>();
                    for (Entry<E, Double> ste : sidetrackWeights.entrySet()) {
                        if (graph.getEdgeSource(ste.getKey()).equals(v)) {
                            hOutHeap.add(ste.getKey(), ste.getValue());
                        }
                    }
                    if (null != hOutHeap.root) {
                        hOutHeaps.put(v, hOutHeap);
                    }
                }

                hTHeaps = new HashMap<V, EppsteinTreeHeap<E>>();

                // build hT heaps for all vertices
                // short cuts already built heaps with the use of the map v->hT
                for (V v : graph.vertexSet()) {
                    getEppsteinHeapT(v);
                }
            }
        }

        /**
         * Create and return an iterator that produces on-the-fly the next shortest paths.
         * 
         * @return the iterator over the k shortest paths
         */
        public Iterator<GraphPath<V, E>> iterator()
        {
            final PriorityQueue<EppsteinPath<V, E>> pathQueue =
                new PriorityQueue<EppsteinPath<V, E>>();
            if (null != hTHeaps) {
                // the initial Eppstein path is the special path corresponding
                // to the shortest path
                // if there is one.
                pathQueue.add(new InitialEppsteinPath());
            }
            return new Iterator<GraphPath<V, E>>()
            {
                @Override
                public boolean hasNext()
                {
                    return !pathQueue.isEmpty();
                }

                @Override
                public GraphPath<V, E> next()
                {
                    EppsteinPath<V, E> ep = pathQueue.poll();
                    ep.addNext(pathQueue);
                    return ep.getPath();
                }
            };
        }

        /**
         * The interface for Eppstein paths.
         *
         * @param <V> the graph vertex type
         * @param <E> the graph edge type
         */
        private interface EppsteinPath<V, E>
            extends Comparable<EppsteinPath<V, E>>
        {
            /**
             * Add subsequent paths to the provided queue.
             * 
             * @param pathQueue
             */
            void addNext(Collection<EppsteinPath<V, E>> pathQueue);

            /**
             * Get the GraphPath described by this EppsteinPath.
             * 
             * @return the graph path.
             */
            GraphPath<V, E> getPath();

            /**
             * Get the cost of this path.
             * 
             * @return the cost.
             */
            double getCost();

            /**
             * Get the sidetracks that uniquely define this EppsteinPath.
             * 
             * @return the list of sidetrack edges
             */
            List<E> getSidetracks();

            @Override
            default int compareTo(EppsteinPath<V, E> other)
            {
                return Double.compare(getCost(), other.getCost());
            }
        }

        /**
         * add the shortest path edges starting at vertex f to the sink.
         * 
         * @param path
         * @param f
         * @return the aggregated cost of the edges.
         */
        private double addShortestTo(List<E> path, V f)
        {
            double cost = 0.0;
            while (f != to) {
                E spE = shortestPathEdge.get(f);
                path.add(spE);
                f = Graphs.getOppositeVertex(graph, spE, f);
                cost += graph.getEdgeWeight(spE);
            }
            return cost;
        }

        /**
         * A special EppsteinPath representing the initial edge in the Graph tree pointing to the
         * source vertex. The path itself is the shortest path from source to sink with its cost.
         * The list of sidetracks is empty.
         */
        private class InitialEppsteinPath
            implements EppsteinPath<V, E>
        {
            private final List<E> pathEdges;
            private final double cost;

            public InitialEppsteinPath()
            {
                this.pathEdges = new ArrayList<E>();
                this.cost = addShortestTo(pathEdges, from);
            }

            @Override
            public void addNext(Collection<EppsteinPath<V, E>> pathQueue)
            {
                EppsteinTreeHeap<E> hT = hTHeaps.get(from);
                if (!hT.isEmpty()) {
                    pathQueue.add(new DefaultEppsteinPath(hT, this));
                }
            }

            @Override
            public GraphPath<V, E> getPath()
            {
                return new GraphWalk<V, E>(graph, from, to, pathEdges, cost);
            }

            @Override
            public double getCost()
            {
                return cost;
            }

            @Override
            public List<E> getSidetracks()
            {
                return Collections.emptyList();
            }
        }

        /**
         * The default EppsteinPath.
         */
        private class DefaultEppsteinPath
            implements EppsteinPath<V, E>
        {
            private final EppsteinTreeHeap<E> node;
            private final EppsteinPath<V, E> last;
            private final List<E> edgeList;
            private final double cost;

            public DefaultEppsteinPath(EppsteinTreeHeap<E> node, EppsteinPath<V, E> baseEp)
            {
                this.node = node;
                this.last = baseEp;
                GraphPath<V, E> bp = baseEp.getPath();
                List<E> lastPath = bp.getEdgeList();

                edgeList = new ArrayList<E>();
                ListIterator<E> baseIter = lastPath.listIterator(lastPath.size());
                V sideTrackSource = graph.getEdgeSource(node.sideTrack.getFirst());
                while (baseIter.hasPrevious()) {
                    E e = baseIter.previous();
                    if (graph.getEdgeSource(e).equals(sideTrackSource)) {
                        break;
                    }
                }
                // Collect everything before the sidetrack, always
                // prepending
                while (baseIter.hasPrevious()) {
                    edgeList.add(0, baseIter.previous());
                }
                // Add the sidetrack
                edgeList.add(node.sideTrack.getFirst());
                // Add the shortest path to the sink
                addShortestTo(edgeList, graph.getEdgeTarget(node.sideTrack.getFirst()));

                this.cost = baseEp.getCost() + node.sideTrack.getSecond();
            }

            @Override
            public void addNext(Collection<EppsteinPath<V, E>> pathQueue)
            {
                // the left subheap
                if (null != node.left) {
                    pathQueue.add(new DefaultEppsteinPath(node.left, last));
                }
                // the right subheap
                if (null != node.right) {
                    pathQueue.add(new DefaultEppsteinPath(node.right, last));
                }
                // the heap of the non-best Houts.
                if (null != node.rest) {
                    pathQueue.add(new DefaultEppsteinPath(node.rest, last));
                }
                // the cross-edge heap
                V spawnTo = graph.getEdgeTarget(node.sideTrack.getFirst());
                EppsteinTreeHeap<E> hT = hTHeaps.get(spawnTo);
                if (null != hT && !hT.isEmpty()) {
                    pathQueue.add(new DefaultEppsteinPath(hT, this));
                }
            }

            public List<E> getSidetracks()
            {
                ArrayList<E> sideTracks = new ArrayList<E>();
                sideTracks.add(node.sideTrack.getFirst());
                sideTracks.addAll(last.getSidetracks());
                return sideTracks;
            }

            @Override
            public double getCost()
            {
                return cost;
            }

            /**
             * The path is defined as the basepath until we hit for the last time the node which is
             * the source of the sidetrack edge, then the sidetrack edge and then the shortest path
             * from the target of the sidetrack edge to the sink. Basepath already includes all
             * previously taken sidetracks.
             * 
             * @return
             */
            public GraphPath<V, E> getPath()
            {
                return new GraphWalk<>(graph, from, to, edgeList, cost);
            }
        }

        /**
         * prev is called H_t(next(v)) in the paper as it is the Heap of the node following the
         * current node. As we build starting from the last node, we call if previous, as it is the
         * previous node in the shortest tree graph.
         * 
         * @param v
         * @param prev
         * @return
         */
        private EppsteinTreeHeap<E> getEppsteinHeapT(V v)
        {
            EppsteinTreeHeap<E> hT = hTHeaps.get(v);
            if (null != hT) {
                return hT;
            }

            E spEdge = shortestPathEdge.get(v);
            HoutHeap<E> hOut = hOutHeaps.get(v);
            if (null != spEdge) {
                EppsteinTreeHeap<E> hTNext =
                    getEppsteinHeapT(Graphs.getOppositeVertex(graph, spEdge, v));
                if (null == hOut) {
                    hT = hTNext;
                } else {
                    EppsteinTreeHeap<E> hTself = new EppsteinTreeHeap<E>(hOut);
                    hT = addToHtNext(hTNext, hTself);
                }
            } else {
                if (null == hOut) {
                    hT = EppsteinTreeHeap.empty();
                } else {
                    hT = new EppsteinTreeHeap<E>(hOut);
                }
            }
            hTHeaps.put(v, hT);

            // We do not build hG as we will follow the cross edges on-the fly
            // while traversing the shortest paths

            return hT;
        }

        /**
         * Insert outroot(v) into HtNext. Duplicating heap structure only where needed.
         * 
         * @param hT
         * @param hTNext
         * @return
         */
        private EppsteinTreeHeap<E> addToHtNext(
            EppsteinTreeHeap<E> hTNext, EppsteinTreeHeap<E> outroot)
        {
            if (null == hTNext || hTNext.isEmpty()) {
                return outroot;
            }
            EppsteinTreeHeap<E> hT = hTNext.shallowClone();

            hT.size++;
            boolean goLeft =
                null == hT.left || (null != hT.right && hT.right.size() > hT.left.size());
            if (hT.sideTrack.getSecond() > outroot.sideTrack.getSecond()) {
                outroot.left = hT.left;
                outroot.right = hT.right;
                outroot.size = hT.size;
                if (goLeft) {
                    outroot.left = addToHtNext(outroot.left, hT);
                } else {
                    outroot.right = addToHtNext(outroot.right, hT);
                }
                hT = outroot;
            } else {
                if (goLeft) {
                    hT.left = addToHtNext(hT.left, outroot);
                } else {
                    hT.right = addToHtNext(hT.right, outroot);
                }
            }
            return hT;
        }

        /**
         * Special heap structure distinguishing the smallest element from the rest of the binary
         * heap.
         * 
         * @param <E>
         */
        private static class HoutHeap<E>
        {
            Pair<E, Double> root = null;
            PriorityQueue<Pair<E, Double>> rest =
                new PriorityQueue<Pair<E, Double>>(new Comparator<Pair<E, Double>>()
                {
                    @Override
                    public int compare(Pair<E, Double> arg0, Pair<E, Double> arg1)
                    {
                        return Double.compare(arg0.getSecond(), arg1.getSecond());
                    }
                });

            public void add(E t, double weight)
            {
                Pair<E, Double> candidate = new Pair<E, Double>(t, weight);
                if (null == root) {
                    root = candidate;
                } else {
                    if (weight < root.getSecond()) {
                        rest.add(root);
                        root = candidate;
                    } else {
                        rest.add(candidate);
                    }
                }
            }
        }

        /**
         * The core of Epstein's algorithm is the ternary heap of the sidetracks. This heap is
         * potentially infinite, hence we use a tree structure to store it. It also features shared
         * sub-heaps. Besides the left/right subheaps, it also connects with a heap of the
         * additional sidetracks. Read Epstein's paper (several times) to understand this structure.
         * There are no ordinary add/remove operations, as the construction is custom tailored to
         * the algorithm.
         */
        private static class EppsteinTreeHeap<E>
        {
            /**
             * the sidetrack edge associated with the root of this heap
             */
            private final Pair<E, Double> sideTrack;
            /**
             * The left sub heap.
             */
            private EppsteinTreeHeap<E> left;
            /**
             * The right sub heap.
             */
            private EppsteinTreeHeap<E> right;
            /**
             * The sub heap of the second-best sidetracks.
             */
            private EppsteinTreeHeap<E> rest;
            private int size;

            public static <E> EppsteinTreeHeap<E> empty()
            {
                return new EppsteinTreeHeap<E>(null, null, null, 0, null);
            }

            public int size()
            {
                return size;
            }

            public EppsteinTreeHeap<E> shallowClone()
            {
                return new EppsteinTreeHeap<E>(sideTrack, left, right, size, rest);
            }

            public boolean isEmpty()
            {
                return null == left && null == right && null == sideTrack;
            }

            private EppsteinTreeHeap(
                Pair<E, Double> sideTrack, EppsteinTreeHeap<E> left, EppsteinTreeHeap<E> right,
                int nDescend, EppsteinTreeHeap<E> rest)
            {
                this.sideTrack = sideTrack;
                this.left = left;
                this.right = right;
                this.size = nDescend;
                this.rest = rest;
            }

            public EppsteinTreeHeap(HoutHeap<E> hOut)
            {
                this.sideTrack = Objects.requireNonNull(hOut.root);
                EppsteinTreeHeap<E> restHeap;
                if (null == hOut.rest || hOut.rest.isEmpty()) {
                    restHeap = null;
                } else {
                    PriorityQueue<Pair<E, Double>> wq =
                        new PriorityQueue<Pair<E, Double>>(hOut.rest);
                    Object[] sortedRest = new Object[wq.size()];
                    Pair<E, Double> p;
                    for (int i = 0; null != (p = wq.poll()); i++) {
                        sortedRest[i] = p;
                    }
                    restHeap = toTreeHeap(sortedRest, 0, sortedRest.length - 1);
                }
                this.rest = restHeap;
            }

            private EppsteinTreeHeap(Pair<E, Double> sideTrack)
            {
                this.sideTrack = Objects.requireNonNull(sideTrack);
                this.rest = null;
            }

            @SuppressWarnings("unchecked")
            private EppsteinTreeHeap<E> toTreeHeap(Object[] sortedRest, int fromIndex, int toIndex)
            {
                if (fromIndex > toIndex) {
                    return null;
                }
                int mid = (fromIndex + toIndex) / 2;
                EppsteinTreeHeap<E> root =
                    new EppsteinTreeHeap<E>((Pair<E, Double>) sortedRest[fromIndex]);
                root.left = toTreeHeap(sortedRest, fromIndex + 1, mid);
                root.right = toTreeHeap(sortedRest, mid + 1, toIndex);
                return root;
            }
        }
    }
}
