/*******************************************************************************
 * Copyright (c) 2006, 2013 Tom Schindl and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl - initial API and implementation
 *     Hendrik Still <hendrik.still@gammas.de> - bug 413973
 ******************************************************************************/

package org.eclipse.jface.viewers;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * ViewerColumn implementation for TreeViewer to enable column-specific label
 * providers and editing support.
 * @param <E> Type of an element of the model
 * @param <I> Type of the input
 *
 * @since 3.3
 *
 */
public final class TreeViewerColumn<E,I> extends ViewerColumn<E,I> {
	private TreeColumn column;

	/**
	 * Creates a new viewer column for the given {@link TreeViewer} on a new
	 * {@link TreeColumn} with the given style bits. The column is inserted at
	 * the given index into the list of columns.
	 *
	 * @param viewer
	 *            the tree viewer to which this column belongs
	 * @param style
	 *            the style bits used to create the column, for applicable style bits
	 *            see {@link TreeColumn}
	 * @see TreeColumn#TreeColumn(Tree, int)
	 */
	public TreeViewerColumn(TreeViewer<E,I> viewer, int style) {
		this(viewer, style, -1);
	}

	/**
	 * Creates a new viewer column for the given {@link TreeViewer} on a new
	 * {@link TreeColumn} with the given style bits. The column is added at the
	 * end of the list of columns.
	 *
	 * @param viewer
	 *            the tree viewer to which this column belongs
	 * @param style
	 *            the style bits used to create the column, for applicable style bits
	 *            see {@link TreeColumn}
	 * @param index
	 *            the index at which to place the newly created column
	 * @see TreeColumn#TreeColumn(Tree, int, int)
	 */
	public TreeViewerColumn(TreeViewer<E,I> viewer, int style, int index) {
		this(viewer, createColumn(viewer.getTree(), style, index));
	}

	/**
	 * Creates a new viewer column for the given {@link TreeViewer} on the given
	 * {@link TreeColumn}.
	 *
	 * @param viewer
	 *            the tree viewer to which this column belongs
	 * @param column
	 *            the underlying tree column
	 */
	public TreeViewerColumn(TreeViewer<E,I> viewer, TreeColumn column) {
		super(viewer, column);
		this.column = column;
	}

	private static TreeColumn createColumn(Tree table, int style, int index) {
		if (index >= 0) {
			return new TreeColumn(table, style, index);
		}

		return new TreeColumn(table, style);
	}

	/**
	 * @return the underlying SWT column
	 */
	public TreeColumn getColumn() {
		return column;
	}
}
