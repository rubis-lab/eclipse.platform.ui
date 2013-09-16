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
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * A simple TableViewer to demonstrate the usage of a standard content provider
 * with a virtual table
 *
 * @author Tom Schindl <tom.schindl@bestsolution.at>
 *
 */
public class Snippet029VirtualTableViewer {
	private class MyContentProvider implements IStructuredContentProvider<MyModel,List<MyModel>> {
		private List<MyModel> elements;

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		public MyModel[] getElements(List<MyModel> inputElement) {
			MyModel[] myModels = new MyModel[inputElement.size()];
			return elements.toArray(myModels);
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
			this.elements = newInput;
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

	public Snippet029VirtualTableViewer(Shell shell) {
		final TableViewer<MyModel,List<MyModel>> v = new TableViewer<MyModel,List<MyModel>>(shell, SWT.VIRTUAL);
		v.setLabelProvider(new LabelProvider<MyModel>());
		v.setContentProvider(new MyContentProvider());
		v.setUseHashlookup(true);
		List<MyModel> model = createModel();
		v.setInput(model);

		v.getTable().setLinesVisible(true);
	}

	private List<MyModel> createModel() {
		List<MyModel> elements = new ArrayList<MyModel>(10000);
		for( int i = 0; i < 10000; i++ ) {
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
		new Snippet029VirtualTableViewer(shell);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();

	}

}
