/*******************************************************************************
 * Copyright (c) 2007, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 *                                                 fix in bug: 210752
 *******************************************************************************/

package org.eclipse.jface.viewers;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Table;

/**
 * This class is responsible to provide the concept of cells for {@link Table}.
 * This concept is needed to provide features like editor activation with the
 * keyboard
 * @param <E> Type of an single element of the model
 * @param <I> Type of the input
 *
 * @since 3.3
 *
 */
public class TableViewerFocusCellManager<E,I> extends SWTFocusCellManager<E,I> {
	@SuppressWarnings("rawtypes")
	private static final CellNavigationStrategy TABLE_NAVIGATE = new CellNavigationStrategy();

	/**
	 * Create a new manager with a default navigation strategy:
	 * <ul>
	 * <li><code>SWT.ARROW_UP</code>: navigate to cell above</li>
	 * <li><code>SWT.ARROW_DOWN</code>: navigate to cell below</li>
	 * <li><code>SWT.ARROW_RIGHT</code>: navigate to next visible cell on
	 * the right</li>
	 * <li><code>SWT.ARROW_LEFT</code>: navigate to next visible cell on the
	 * left</li>
	 * </ul>
	 *
	 * @param viewer
	 *            the viewer the manager is bound to
	 * @param focusDrawingDelegate
	 *            the delegate responsible to highlight selected cell
	 */
	@SuppressWarnings("unchecked")
	public TableViewerFocusCellManager(TableViewer<E,I> viewer,
			FocusCellHighlighter<E,I> focusDrawingDelegate) {
		this(viewer, focusDrawingDelegate, TABLE_NAVIGATE);
	}

	/**
	 * Create a new manager
	 *
	 * @param viewer
	 *            the viewer the manager is bound to
	 * @param focusDrawingDelegate
	 *            the delegate responsible to highlight selected cell
	 * @param navigationStrategy
	 *            the strategy used to navigate the cells
	 * @since 3.4
	 */
	public TableViewerFocusCellManager(TableViewer<E,I> viewer,
			FocusCellHighlighter<E,I> focusDrawingDelegate,
			CellNavigationStrategy<E,I> navigationStrategy) {
		super(viewer, focusDrawingDelegate, navigationStrategy);
	}

	@Override
	ViewerCell<E> getInitialFocusCell() {
		Table table = (Table) getViewer().getControl();

		if (!table.isDisposed() && table.getItemCount() > 0
				&& !table.getItem(table.getTopIndex()).isDisposed()) {
			final ViewerRow<E> aViewerRow = getViewer().getViewerRowFromItem(
					table.getItem(table.getTopIndex()));
			if (table.getColumnCount() == 0) {
				return aViewerRow.getCell(0);
			}

			Rectangle clientArea = table.getClientArea();
			for (int i = 0; i < table.getColumnCount(); i++) {
				if (aViewerRow.getWidth(i) > 0 && columnInVisibleArea(clientArea,aViewerRow,i))
					return aViewerRow.getCell(i);
				}
			}

		return null;
	}

	private boolean columnInVisibleArea(Rectangle clientArea, ViewerRow<E> row, int colIndex) {
		return row.getBounds(colIndex).x >= clientArea.x;
	}

	@Override
	public ViewerCell<E> getFocusCell() {
		ViewerCell<E> cell = super.getFocusCell();
		Table t = (Table) getViewer().getControl();

		// It is possible that the selection has changed under the hood
		if (cell != null) {
			if (t.getSelection().length == 1
					&& t.getSelection()[0] != cell.getItem()) {
				setFocusCell(getViewer().getViewerRowFromItem(
						t.getSelection()[0]).getCell(cell.getColumnIndex()));
			}
		}

		return super.getFocusCell();
	}

}
