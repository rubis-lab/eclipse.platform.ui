/*******************************************************************************
 * Copyright (c) 2006, 2013 Tom Schindl and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl - initial API and implementation
 *     Hendrik Still <hendrik.still@gammas.de> - bug 417676
 *******************************************************************************/

package org.eclipse.jface.snippets.viewers;

import java.util.ArrayList;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

/**
 * A simple TreeViewer to demonstrate usage of inline editing
 *
 * @author Tom Schindl <tom.schindl@bestsolution.at>
 *
 */
public class Snippet038TreeViewerInlinelEditingOldAPI {
	private class MyContentProvider implements ITreeContentProvider<MyModel,MyModel> {

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		public MyModel[] getElements(MyModel inputElement) {

			MyModel[] myModels = new MyModel[inputElement.child.size()];
			return inputElement.child.toArray(myModels);
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
		public void inputChanged(Viewer<? extends MyModel> viewer, MyModel oldInput, MyModel newInput) {

		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
		 */
		public MyModel[] getChildren(MyModel parentElement) {
			return getElements(parentElement);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
		 */
		public MyModel getParent(MyModel element) {
			if (element == null) {
				return null;
			}

			return element.parent;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
		 */
		public boolean hasChildren(MyModel element) {
			return element.child.size() > 0;
		}

	}

	public class MyModel {
		public MyModel parent;

		public ArrayList<MyModel> child = new ArrayList<MyModel>();

		public int counter;

		public MyModel(int counter, MyModel parent) {
			this.parent = parent;
			this.counter = counter;
		}

		public String toString() {
			String rv = "Item ";
			if (parent != null) {
				rv = parent.toString() + ".";
			}

			rv += counter;

			return rv;
		}
	}

	public class MyLabelProvider extends LabelProvider<MyModel> implements
			ITableLabelProvider<MyModel> {
		FontRegistry registry = new FontRegistry();

		public Image getColumnImage(MyModel element, int columnIndex) {
			return null;
		}

		public String getColumnText(MyModel element, int columnIndex) {
			return "Column " + columnIndex + " => " + element.toString();
		}
	}

	public Snippet038TreeViewerInlinelEditingOldAPI(Shell shell) {
		final TreeViewer<MyModel,MyModel> v = new TreeViewer<MyModel,MyModel>(shell,SWT.FULL_SELECTION);

		TreeColumn column = new TreeColumn(v.getTree(),SWT.NONE);
		column.setWidth(200);
		column.setText("Column 1");

		column = new TreeColumn(v.getTree(),SWT.NONE);
		column.setWidth(200);
		column.setText("Column 2");

		v.setCellEditors(new CellEditor[]{new TextCellEditor(v.getTree()), new TextCellEditor(v.getTree())});
		v.setColumnProperties(new String[]{"col1","col2"});
		v.setCellModifier(new ICellModifier() {

			public boolean canModify(Object element, String property) {
				return true;
			}

			public Object getValue(Object element, String property) {
				return ((MyModel)element).counter+"";
			}

			public void modify(Object element, String property, Object value) {
				((MyModel)((TreeItem)element).getData()).counter = Integer.parseInt(value.toString());
				v.update((MyModel)((TreeItem)element).getData(), null);
			}

		});
		v.setLabelProvider(new MyLabelProvider());
		v.setContentProvider(new MyContentProvider());
		v.setInput(createModel());
	}

	private MyModel createModel() {

		MyModel root = new MyModel(0, null);
		root.counter = 0;

		MyModel tmp;
		for (int i = 1; i < 10; i++) {
			tmp = new MyModel(i, root);
			root.child.add(tmp);
			for (int j = 1; j < i; j++) {
				tmp.child.add(new MyModel(j, tmp));
			}
		}

		return root;
	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		new Snippet038TreeViewerInlinelEditingOldAPI(shell);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}
}
