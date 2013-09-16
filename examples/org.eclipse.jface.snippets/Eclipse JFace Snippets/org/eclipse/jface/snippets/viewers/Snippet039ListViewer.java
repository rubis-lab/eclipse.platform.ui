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
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Demonstrate a simple ListViewer
 */
public class Snippet039ListViewer {
	private class MyContentProvider implements IStructuredContentProvider<MyModel,List<MyModel>> {

		public MyModel[] getElements(List<MyModel> inputElement) {
			MyModel[] myModels = new MyModel[inputElement.size()];
			return inputElement.toArray(myModels);
		}

		public void dispose() {

		}

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

	public Snippet039ListViewer(Shell shell) {
		final ListViewer<MyModel,List<MyModel>> v = new ListViewer<MyModel,List<MyModel>>(shell,SWT.H_SCROLL|SWT.V_SCROLL);
		v.setLabelProvider(new LabelProvider<MyModel>());
		v.setContentProvider(new MyContentProvider());
		List<MyModel> model = createModel();
		v.setInput(model);
	}

	private List<MyModel> createModel() {
		List<MyModel> elements = new ArrayList<MyModel>(10);
		for( int i = 0; i < 10; i++ ) {
			elements.add(i,new MyModel(i));
		}
		return elements;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = new Display ();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		new Snippet039ListViewer(shell);
		shell.open ();

		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}

		display.dispose ();

	}

}