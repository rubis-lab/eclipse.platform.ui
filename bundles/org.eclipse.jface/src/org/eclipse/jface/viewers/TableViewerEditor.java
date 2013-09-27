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
 *                                                 fixes in bug 198665, 200731
 *******************************************************************************/

package org.eclipse.jface.viewers;

import java.util.List;

import org.eclipse.jface.viewers.CellEditor.LayoutData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * This is an editor-implementation for {@link Table}
 * @param <E> Type of an single element of the model
 * @param <I> Type of the input
 *
 * @since 3.3
 *
 */
public final class TableViewerEditor<E,I> extends ColumnViewerEditor<E,I> {
	/**
	 * This viewer's table editor.
	 */
	private TableEditor tableEditor;

	private SWTFocusCellManager<E,I> focusCellManager;

	/**
	 * @param viewer
	 *            the viewer the editor is attached to
	 * @param focusCellManager
	 *            the cell focus manager if one used or <code>null</code>
	 * @param editorActivationStrategy
	 *            the strategy used to decide about the editor activation
	 * @param feature
	 *            the feature mask
	 */
	TableViewerEditor(TableViewer<E,I> viewer, SWTFocusCellManager<E,I> focusCellManager,
			ColumnViewerEditorActivationStrategy<E,I> editorActivationStrategy,
			int feature) {
		super(viewer, editorActivationStrategy, feature);
		tableEditor = new TableEditor(viewer.getTable());
		this.focusCellManager = focusCellManager;
	}

	/**
	 * Create a customized editor with focusable cells
	 *
	 * @param viewer
	 *            the viewer the editor is created for
	 * @param focusCellManager
	 *            the cell focus manager if one needed else <code>null</code>
	 * @param editorActivationStrategy
	 *            activation strategy to control if an editor activated
	 * @param feature
	 *            bit mask controlling the editor
	 *            <ul>
	 *            <li>{@link ColumnViewerEditor#DEFAULT}</li>
	 *            <li>{@link ColumnViewerEditor#TABBING_CYCLE_IN_ROW}</li>
	 *            <li>{@link ColumnViewerEditor#TABBING_HORIZONTAL}</li>
	 *            <li>{@link ColumnViewerEditor#TABBING_MOVE_TO_ROW_NEIGHBOR}</li>
	 *            <li>{@link ColumnViewerEditor#TABBING_VERTICAL}</li>
	 *            </ul>
	 * @see #create(TableViewer, ColumnViewerEditorActivationStrategy, int)
	 */
	public static <E,I> void create(TableViewer<E,I> viewer,
			SWTFocusCellManager<E,I> focusCellManager,
			ColumnViewerEditorActivationStrategy<E,I> editorActivationStrategy,
			int feature) {
		TableViewerEditor<E,I> editor = new TableViewerEditor<E,I>(viewer,
				focusCellManager, editorActivationStrategy, feature);
		viewer.setColumnViewerEditor(editor);
		if (focusCellManager != null) {
			focusCellManager.init();
		}
	}

	/**
	 * Create a customized editor whose activation process is customized
	 *
	 * @param viewer
	 *            the viewer the editor is created for
	 * @param editorActivationStrategy
	 *            activation strategy to control if an editor activated
	 * @param feature
	 *            bit mask controlling the editor
	 *            <ul>
	 *            <li>{@link ColumnViewerEditor#DEFAULT}</li>
	 *            <li>{@link ColumnViewerEditor#TABBING_CYCLE_IN_ROW}</li>
	 *            <li>{@link ColumnViewerEditor#TABBING_HORIZONTAL}</li>
	 *            <li>{@link ColumnViewerEditor#TABBING_MOVE_TO_ROW_NEIGHBOR}</li>
	 *            <li>{@link ColumnViewerEditor#TABBING_VERTICAL}</li>
	 *            </ul>
	 */
	public static <E,I> void create(TableViewer<E,I> viewer,
			ColumnViewerEditorActivationStrategy<E,I> editorActivationStrategy,
			int feature) {
		create(viewer, null, editorActivationStrategy, feature);
	}

	@Override
	protected void setEditor(Control w, Item item, int columnNumber) {
		tableEditor.setEditor(w, (TableItem) item, columnNumber);
	}

	@Override
	protected void setLayoutData(LayoutData layoutData) {
		tableEditor.grabHorizontal = layoutData.grabHorizontal;
		tableEditor.horizontalAlignment = layoutData.horizontalAlignment;
		tableEditor.minimumWidth = layoutData.minimumWidth;
		tableEditor.verticalAlignment = layoutData.verticalAlignment;

		if (layoutData.minimumHeight != SWT.DEFAULT) {
			tableEditor.minimumHeight = layoutData.minimumHeight;
		}
	}

	@Override
	public ViewerCell<E> getFocusCell() {
		if (focusCellManager != null) {
			return focusCellManager.getFocusCell();
		}

		return super.getFocusCell();
	}

	@Override
	protected void updateFocusCell(ViewerCell<E> focusCell,
			ColumnViewerEditorActivationEvent event) {
		// Update the focus cell when we activated the editor with these 2
		// events
		if (event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC
				|| event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL) {

			List<E> l = getViewer().getSelectionFromWidget();

			if (!l.contains(focusCell.getElement())) {
				getViewer().setSelection(
						new StructuredSelection(focusCell.getElement()), true);
			}

			// Set the focus cell after the selection is updated because else
			// the cell is not scrolled into view
			if (focusCellManager != null) {
				focusCellManager.setFocusCell(focusCell);
			}
		}
	}
}
