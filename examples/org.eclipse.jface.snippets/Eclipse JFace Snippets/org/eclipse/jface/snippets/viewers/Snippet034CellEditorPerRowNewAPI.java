/*******************************************************************************
 * Copyright (c) 2007, 2013 Tom Schindl and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl<tom.schindl@bestsolution.at> - initial API and implementation
 *     Wayne Beaton - bug 185540
 *     Hendrik Still <hendrik.still@gammas.de> - bug 417676
 *******************************************************************************/

package org.eclipse.jface.snippets.viewers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

/**
 * Snippet to present editor different CellEditors within one column in 3.2
 * for 3.3 and above please use the new EditingSupport class
 *
 * @author Tom Schindl <tom.schindl@bestsolution.at>
 *
 */
public class Snippet034CellEditorPerRowNewAPI {
	private class MyEditingSupport extends EditingSupport {
		private CellEditor textEditor;

		private CellEditor dropDownEditor;

		public MyEditingSupport(TableViewer<MyModel,List<MyModel>> viewer) {
			super(viewer);
			textEditor = new TextCellEditor(viewer.getTable());

			String[] elements = new String[10];

			for (int i = 0; i < 10; i++) {
				elements[i] = i+"";
			}

			dropDownEditor = new ComboBoxCellEditor(viewer.getTable(),elements);
		}

		protected boolean canEdit(Object element) {
			return ((MyModel) element).counter % 2 == 0;
		}

		protected CellEditor getCellEditor(Object element) {
			if( element instanceof MyModel2 ) {
				return dropDownEditor;
			} else {
				return textEditor;
			}
		}

		protected Object getValue(Object element) {
			if( element instanceof MyModel2 ) {
				return new Integer(((MyModel) element).counter);
			} else {
				return ((MyModel) element).counter + "";
			}
		}

		protected void setValue(Object element, Object value) {
			((MyModel)element).counter = Integer.parseInt(value.toString());
			getViewer().update(element, null);
		}

	}

	private class MyContentProvider implements IStructuredContentProvider<MyModel,List<MyModel>> {

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		public MyModel[] getElements(List<MyModel> inputElement) {
			MyModel[] myModels = new MyModel[inputElement.size()];
			return inputElement.toArray(myModels);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {

		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
		 *      java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(Viewer<? extends List<MyModel>> viewer, List<MyModel> oldInput, List<MyModel> newInput) {

		}

	}

	public class MyModel {
		public int counter;

		public MyModel(int counter) {
			this.counter = counter;
		}

		public String toString() {
			return "Item " + this.counter;
		}
	}

	public class MyModel2 extends MyModel {

		public MyModel2(int counter) {
			super(counter);
		}

		public String toString() {
			return "Special Item " + this.counter;
		}
	}

	public Snippet034CellEditorPerRowNewAPI(Shell shell) {
		final Table table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);

		final TableViewer<MyModel,List<MyModel>> v = new TableViewer<MyModel,List<MyModel>>(table);
		v.getTable().setLinesVisible(true);

		TableViewerColumn<MyModel,List<MyModel>> column = new TableViewerColumn<MyModel,List<MyModel>>(v, SWT.NONE);
		column.getColumn().setWidth(200);
		column.setLabelProvider(new ColumnLabelProvider<MyModel,List<MyModel>>() {

			public String getText(MyModel element) {
				return element.toString();
			}

		});

		column.setEditingSupport(new MyEditingSupport(v));

		v.setContentProvider(new MyContentProvider());

		List<MyModel> model = createModel();
		v.setInput(model);
	}

	private List<MyModel> createModel() {
		List<MyModel> elements = new ArrayList<MyModel>(20);

		for (int i = 0; i < 10; i++) {
			elements.add(i,new MyModel(i));
		}

		for (int i = 0; i < 10; i++) {
			elements.add(i+10,new MyModel2(i));
		}

		return elements;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		new Snippet034CellEditorPerRowNewAPI(shell);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();

	}

}
