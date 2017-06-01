package org.jgrapht.alg.shortestpath;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

/**
 * Sample graphs demonstrating for use in k shortest path tests.
 *
 */
public final class KShortestPathsGraphs
{
    /**
     * Graph used in Eppstein's paper.
     */
    public static final DefaultDirectedGraph<String, DefaultWeightedEdge> EPPSTEIN;
    /**
     * Graph used in Graehl's C++ implementation.
     */
    public static final DefaultDirectedGraph<String, DefaultWeightedEdge> GRAEHL;
    /**
     * graph with multiple edges but no loop
     * 
     * <p>
     * 0 -- 1.0|2.0|3.0 --> 1
     * 
     * <p>
     * 1 -- 1.0 --> 2
     */
    public static final DirectedWeightedPseudograph<String, DefaultWeightedEdge> NOLOOPME;
    /**
     * pseudograph used in http://www.isi.edu/natural-language/people/epp-cs562.pdf page44
     * 
     * <p>
     * 0 -- 1.0 --> 1 0
     * 
     * <p>
     * 0 -- 2.0|3.0 --> 0
     */
    public static final DirectedWeightedPseudograph<String, DefaultWeightedEdge> LOOPME;
    /**
     * pseudograph used in http://www.isi.edu/natural-language/people/epp-cs562.pdf page45
     * 
     * <p>
     * 0 -- 1000.0 --> 1 0
     * 
     * <p>
     * 0 -- 1.0|2.0|...|100.0 --> 0
     */
    public static final DirectedWeightedPseudograph<String, DefaultWeightedEdge> LOOPME1000;

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

        GRAEHL = new DefaultDirectedWeightedGraph<String, DefaultWeightedEdge>(
            DefaultWeightedEdge.class);
        addEdge(GRAEHL, "0", "5", .1);
        addEdge(GRAEHL, "5", "1", .5);
        addEdge(GRAEHL, "5", "2", 1.0);
        addEdge(GRAEHL, "1", "2", .9);
        addEdge(GRAEHL, "3", "4", .2);
        addEdge(GRAEHL, "4", "2", .3);
        addEdge(GRAEHL, "5", "3", .3);
        addEdge(GRAEHL, "5", "4", .8);
        addEdge(GRAEHL, "2", "4", 0.1);
        addEdge(GRAEHL, "3", "2", .6);
        addEdge(GRAEHL, "0", "0", .05);

        NOLOOPME =
            new DirectedWeightedPseudograph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        addEdge(NOLOOPME, "0", "1", 1.0);
        addEdge(NOLOOPME, "0", "1", 2.0);
        addEdge(NOLOOPME, "0", "1", 3.0);
        addEdge(NOLOOPME, "1", "2", 1.0);

        LOOPME =
            new DirectedWeightedPseudograph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        addEdge(LOOPME, "0", "1", 1.0);
        addEdge(LOOPME, "0", "0", 2.0);
        addEdge(LOOPME, "0", "0", 3.0);

        LOOPME1000 =
            new DirectedWeightedPseudograph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        addEdge(LOOPME1000, "0", "0", 1.0);
        addEdge(LOOPME1000, "0", "0", 2.0);
        addEdge(LOOPME1000, "0", "0", 3.0);
        addEdge(LOOPME1000, "0", "0", 4.0);
        addEdge(LOOPME1000, "0", "0", 5.0);
        addEdge(LOOPME1000, "0", "0", 6.0);
        addEdge(LOOPME1000, "0", "0", 7.0);
        addEdge(LOOPME1000, "0", "0", 8.0);
        addEdge(LOOPME1000, "0", "0", 9.0);
        addEdge(LOOPME1000, "0", "0", 10.0);
        addEdge(LOOPME1000, "0", "0", 11.0);
        addEdge(LOOPME1000, "0", "0", 12.0);
        addEdge(LOOPME1000, "0", "0", 13.0);
        addEdge(LOOPME1000, "0", "0", 14.0);
        addEdge(LOOPME1000, "0", "0", 15.0);
        addEdge(LOOPME1000, "0", "0", 16.0);
        addEdge(LOOPME1000, "0", "0", 17.0);
        addEdge(LOOPME1000, "0", "0", 18.0);
        addEdge(LOOPME1000, "0", "0", 19.0);
        addEdge(LOOPME1000, "0", "0", 20.0);
        addEdge(LOOPME1000, "0", "0", 21.0);
        addEdge(LOOPME1000, "0", "0", 22.0);
        addEdge(LOOPME1000, "0", "0", 23.0);
        addEdge(LOOPME1000, "0", "0", 24.0);
        addEdge(LOOPME1000, "0", "0", 25.0);
        addEdge(LOOPME1000, "0", "0", 26.0);
        addEdge(LOOPME1000, "0", "0", 27.0);
        addEdge(LOOPME1000, "0", "0", 28.0);
        addEdge(LOOPME1000, "0", "0", 29.0);
        addEdge(LOOPME1000, "0", "0", 30.0);
        addEdge(LOOPME1000, "0", "0", 31.0);
        addEdge(LOOPME1000, "0", "0", 32.0);
        addEdge(LOOPME1000, "0", "0", 33.0);
        addEdge(LOOPME1000, "0", "0", 34.0);
        addEdge(LOOPME1000, "0", "0", 35.0);
        addEdge(LOOPME1000, "0", "0", 36.0);
        addEdge(LOOPME1000, "0", "0", 37.0);
        addEdge(LOOPME1000, "0", "0", 38.0);
        addEdge(LOOPME1000, "0", "0", 39.0);
        addEdge(LOOPME1000, "0", "0", 40.0);
        addEdge(LOOPME1000, "0", "0", 41.0);
        addEdge(LOOPME1000, "0", "0", 42.0);
        addEdge(LOOPME1000, "0", "0", 43.0);
        addEdge(LOOPME1000, "0", "0", 44.0);
        addEdge(LOOPME1000, "0", "0", 45.0);
        addEdge(LOOPME1000, "0", "0", 46.0);
        addEdge(LOOPME1000, "0", "0", 47.0);
        addEdge(LOOPME1000, "0", "0", 48.0);
        addEdge(LOOPME1000, "0", "0", 49.0);
        addEdge(LOOPME1000, "0", "0", 50.0);
        addEdge(LOOPME1000, "0", "0", 51.0);
        addEdge(LOOPME1000, "0", "0", 52.0);
        addEdge(LOOPME1000, "0", "0", 53.0);
        addEdge(LOOPME1000, "0", "0", 54.0);
        addEdge(LOOPME1000, "0", "0", 55.0);
        addEdge(LOOPME1000, "0", "0", 56.0);
        addEdge(LOOPME1000, "0", "0", 57.0);
        addEdge(LOOPME1000, "0", "0", 58.0);
        addEdge(LOOPME1000, "0", "0", 59.0);
        addEdge(LOOPME1000, "0", "0", 60.0);
        addEdge(LOOPME1000, "0", "0", 61.0);
        addEdge(LOOPME1000, "0", "0", 62.0);
        addEdge(LOOPME1000, "0", "0", 63.0);
        addEdge(LOOPME1000, "0", "0", 64.0);
        addEdge(LOOPME1000, "0", "0", 65.0);
        addEdge(LOOPME1000, "0", "0", 66.0);
        addEdge(LOOPME1000, "0", "0", 67.0);
        addEdge(LOOPME1000, "0", "0", 68.0);
        addEdge(LOOPME1000, "0", "0", 69.0);
        addEdge(LOOPME1000, "0", "0", 70.0);
        addEdge(LOOPME1000, "0", "0", 71.0);
        addEdge(LOOPME1000, "0", "0", 72.0);
        addEdge(LOOPME1000, "0", "0", 73.0);
        addEdge(LOOPME1000, "0", "0", 74.0);
        addEdge(LOOPME1000, "0", "0", 75.0);
        addEdge(LOOPME1000, "0", "0", 76.0);
        addEdge(LOOPME1000, "0", "0", 77.0);
        addEdge(LOOPME1000, "0", "0", 78.0);
        addEdge(LOOPME1000, "0", "0", 79.0);
        addEdge(LOOPME1000, "0", "0", 80.0);
        addEdge(LOOPME1000, "0", "0", 81.0);
        addEdge(LOOPME1000, "0", "0", 82.0);
        addEdge(LOOPME1000, "0", "0", 83.0);
        addEdge(LOOPME1000, "0", "0", 84.0);
        addEdge(LOOPME1000, "0", "0", 85.0);
        addEdge(LOOPME1000, "0", "0", 86.0);
        addEdge(LOOPME1000, "0", "0", 87.0);
        addEdge(LOOPME1000, "0", "0", 88.0);
        addEdge(LOOPME1000, "0", "0", 89.0);
        addEdge(LOOPME1000, "0", "0", 90.0);
        addEdge(LOOPME1000, "0", "0", 91.0);
        addEdge(LOOPME1000, "0", "0", 92.0);
        addEdge(LOOPME1000, "0", "0", 93.0);
        addEdge(LOOPME1000, "0", "0", 94.0);
        addEdge(LOOPME1000, "0", "0", 95.0);
        addEdge(LOOPME1000, "0", "0", 96.0);
        addEdge(LOOPME1000, "0", "0", 97.0);
        addEdge(LOOPME1000, "0", "0", 98.0);
        addEdge(LOOPME1000, "0", "0", 99.0);
        addEdge(LOOPME1000, "0", "0", 100.0);
        addEdge(LOOPME1000, "0", "1", 1000.0);
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
