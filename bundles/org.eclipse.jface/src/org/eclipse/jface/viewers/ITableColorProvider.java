/*******************************************************************************
 * Copyright (c) 2004, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Initial implementation - Gunnar Ahlberg (IBS AB, www.ibs.net)
 *     IBM Corporation - further revisions
 *     Hendrik Still <hendrik.still@gammas.de> - bug 415561
 *******************************************************************************/

package org.eclipse.jface.viewers;

import org.eclipse.swt.graphics.Color;

/**
 * Interface to provide color representation for a given cell within
 * the row for an element in a table.
 * @param <E> Type of an single element of the model
 * @since 3.1
 */
public interface ITableColorProvider<E> {

    /**
     * Provides a foreground color for the given element.
     *
     * @param element the element
     * @param columnIndex the zero-based index of the column in which
     * 	the color appears
     * @return the foreground color for the element, or <code>null</code> to
     *         use the default foreground color
     */
    Color getForeground(E element, int columnIndex);

    /**
     * Provides a background color for the given element at the specified index
     *
     * @param element the element
     * @param columnIndex the zero-based index of the column in which the color appears
     * @return the background color for the element, or <code>null</code> to
     *         use the default background color
     *
     */
    Color getBackground(E element, int columnIndex);
}
