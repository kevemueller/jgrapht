/*
 * (C) Copyright 2003-2018, by Barak Naveh and Contributors.
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
package org.jgrapht.graph;

import java.io.*;

import org.jgrapht.*;

/**
 * An {@link EdgeFactory} for producing edges by using a class as a factory.
 * 
 * <p>
 * Note that when used inside a {@link Graph} the factory must always return a unique object on each
 * call. This implementation calls the no-arguments constructor of the provided class. It is the
 * user's responsibility to make sure that the no-arguments constructor creates unique objects.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 *
 * @author Barak Naveh
 * @since Jul 14, 2003
 */
public class ClassBasedEdgeFactory<V, E>
    implements EdgeFactory<V, E>, Serializable
{
    private static final long serialVersionUID = 3618135658586388792L;

    private final Class<? extends E> edgeClass;

    /**
     * Create a new class based edge factory.
     * 
     * @param edgeClass the edge class
     */
    public ClassBasedEdgeFactory(Class<? extends E> edgeClass)
    {
        this.edgeClass = edgeClass;
    }

    @Override
    public E createEdge(V source, V target)
    {
        try {
            return edgeClass.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Edge factory failed", ex);
        }
    }
}

// End ClassBasedEdgeFactory.java
