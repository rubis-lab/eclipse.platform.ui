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
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Example usage of ITableLabelProvider using images and labels
 *
 * @author Tom Schindl <tom.schindl@bestsolution.at>
 *
 */
public class Snippet003TableLabelProvider {
	private static Image[] images;

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

	public class MyLabelProvider extends LabelProvider<MyModel> implements ITableLabelProvider<MyModel> {

		public Image getColumnImage(MyModel element, int columnIndex) {
			if( columnIndex == 1 ) {
				return images[element.counter%4];
			}

			return null;
		}

		public String getColumnText(MyModel element, int columnIndex) {
			return "Column " + columnIndex + " => " + element.toString();
		}

	}

	private static Image createImage(Display display, int red, int green, int blue) {
		Color color = new Color(display,red,green,blue);
		Image image = new Image(display,10,10);
		GC gc = new GC(image);
		gc.setBackground(color);
		gc.fillRectangle(0, 0, 10, 10);
		gc.dispose();

		return image;
	}

	public Snippet003TableLabelProvider(Shell shell) {
		final TableViewer<MyModel,List<MyModel>> v = new TableViewer<MyModel,List<MyModel>>(shell,SWT.BORDER|SWT.FULL_SELECTION);
		v.setLabelProvider(new MyLabelProvider());
		v.setContentProvider(new MyContentProvider());

		TableColumn column = new TableColumn(v.getTable(),SWT.NONE);
		column.setWidth(200);
		column.setText("Column 1");

		column = new TableColumn(v.getTable(),SWT.NONE);
		column.setWidth(200);
		column.setText("Column 2");

		List<MyModel> model = createModel();
		v.setInput(model);
		v.getTable().setLinesVisible(true);
		v.getTable().setHeaderVisible(true);
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

		images = new Image[4];
		images[0] = createImage(display,0,0,255);
		images[1] = createImage(display,0,255,255);
		images[2] = createImage(display,0,255,0);
		images[3] = createImage(display,255,0,255);

		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		new Snippet003TableLabelProvider(shell);
		shell.open ();

		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}

		for( int i = 0; i < images.length; i++ ) {
			images[i].dispose();
		}

		display.dispose ();

	}

}
