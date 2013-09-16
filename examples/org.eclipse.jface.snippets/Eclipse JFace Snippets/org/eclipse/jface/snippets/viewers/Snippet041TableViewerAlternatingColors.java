/*******************************************************************************
 * Copyright (c) 2007, 2013 Tom Schindl and others.
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
import java.util.List;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

/**
 * Demonstrate alternating row colors using new Jace 3.3 API
 *
 * @author Tom Schindl <tom.schindl@bestsolution.at>
 *
 */
public class Snippet041TableViewerAlternatingColors {

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

	private class OptimizedIndexSearcher {
		private int lastIndex = 0;

		public boolean isEven(TableItem item) {
			TableItem[] items = item.getParent().getItems();

			// 1. Search the next ten items
			for( int i = lastIndex; i < items.length && lastIndex + 10 > i; i++ ) {
				if( items[i] == item ) {
					lastIndex = i;
					return lastIndex % 2 == 0;
				}
			}

			// 2. Search the previous ten items
			for( int i = lastIndex; i < items.length && lastIndex - 10 > i; i-- ) {
				if( items[i] == item ) {
					lastIndex = i;
					return lastIndex % 2 == 0;
				}
			}

			// 3. Start from the beginning
			for( int i = 0; i < items.length; i++ ) {
				if( items[i] == item ) {
					lastIndex = i;
					return lastIndex % 2 == 0;
				}
			}

			return false;
		}
	}

	public Snippet041TableViewerAlternatingColors(Shell shell) {
		final TableViewer<MyModel,List<MyModel>> v = new TableViewer<MyModel,List<MyModel>>(shell, SWT.BORDER
				| SWT.FULL_SELECTION|SWT.VIRTUAL);
		v.setContentProvider(new MyContentProvider());

		final OptimizedIndexSearcher searcher = new OptimizedIndexSearcher();

		TableViewerColumn<MyModel,List<MyModel>> column = new TableViewerColumn<MyModel,List<MyModel>>(v, SWT.NONE);
		column.getColumn().setWidth(200);
		column.getColumn().setText("Column 1");
		column.setLabelProvider(new ColumnLabelProvider<MyModel,List<MyModel>>() {
			boolean even = true;

			public Color getBackground(MyModel element) {
				if( even ) {
					return null;
				} else {
					return v.getTable().getDisplay().getSystemColor(SWT.COLOR_GRAY);
				}
			}

			public void update(ViewerCell<MyModel> cell) {
				even = searcher.isEven((TableItem)cell.getItem());
				super.update(cell);
			}
		});

		column = new TableViewerColumn<MyModel,List<MyModel>>(v, SWT.NONE);
		column.getColumn().setWidth(200);
		column.getColumn().setText("Column 2");
		column.setLabelProvider(new ColumnLabelProvider<MyModel,List<MyModel>>() {
			boolean even = true;

			public Color getBackground(MyModel element) {
				if( even ) {
					return null;
				} else {
					return v.getTable().getDisplay().getSystemColor(SWT.COLOR_GRAY);
				}
			}

			public void update(ViewerCell<MyModel> cell) {
				even = searcher.isEven((TableItem)cell.getItem());
				super.update(cell);
			}

		});

		List<MyModel> model = createModel();
		v.setInput(model);
		v.getTable().setLinesVisible(true);
		v.getTable().setHeaderVisible(true);

		final ViewerFilter<MyModel,List<MyModel>> filter = new ViewerFilter<MyModel,List<MyModel>>() {

			public boolean select(Viewer<List<MyModel>> viewer, Object parentElement,
					MyModel element) {
				return element.counter % 2 == 0;
			}

		};

		Button b = new Button(shell,SWT.PUSH);
		b.addSelectionListener(new SelectionAdapter() {
			boolean b = true;

			public void widgetSelected(SelectionEvent e) {
				if( b ) {
					v.addFilter(filter);
					b = false;
				} else {
					v.setFilters(new ViewerFilter[0]);
					b = true;
				}
			}

		});
	}

	private List<MyModel> createModel() {
		List<MyModel> elements = new ArrayList<MyModel>(100000);

		for (int i = 0; i < 100000; i++) {
			elements.add(i,new MyModel(i));
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
		new Snippet041TableViewerAlternatingColors(shell);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();

	}

}
