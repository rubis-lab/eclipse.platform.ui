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

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

/**
 * Example showing how to center an image using an owner draw label provider
 *
 * @author Tom Schindl <tom.schindl@bestsolution.at>
 *
 */
public class Snippet051TableCenteredImage {
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

	private abstract class CenterImageLabelProvider extends
			OwnerDrawLabelProvider<MyModel,List<MyModel>> {

		protected void measure(Event event, MyModel element) {
		}

		protected void paint(Event event, MyModel element) {

			Image img = getImage(element);

			if (img != null) {
				Rectangle bounds = ((TableItem) event.item)
						.getBounds(event.index);
				Rectangle imgBounds = img.getBounds();
				bounds.width /= 2;
				bounds.width -= imgBounds.width / 2;
				bounds.height /= 2;
				bounds.height -= imgBounds.height / 2;

				int x = bounds.width > 0 ? bounds.x + bounds.width : bounds.x;
				int y = bounds.height > 0 ? bounds.y + bounds.height : bounds.y;

				event.gc.drawImage(img, x, y);
			}
		}

		protected abstract Image getImage(Object element);
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

	private static Image createImage(Display display, int red, int green,
			int blue) {
		Color color = new Color(display, red, green, blue);
		Image image = new Image(display, 10, 10);
		GC gc = new GC(image);
		gc.setBackground(color);
		gc.fillRectangle(0, 0, 10, 10);
		gc.dispose();

		return image;
	}

	public Snippet051TableCenteredImage(Shell shell) {
		final TableViewer<MyModel,List<MyModel>> v = new TableViewer<MyModel,List<MyModel>>(shell, SWT.BORDER
				| SWT.FULL_SELECTION);
		v.setContentProvider(new MyContentProvider());

		TableViewerColumn<MyModel,List<MyModel>> column = new TableViewerColumn<MyModel,List<MyModel>>(v, SWT.NONE);
		column.getColumn().setWidth(200);
		column.getColumn().setText("Column 1");
		column.setLabelProvider(new ColumnLabelProvider<MyModel,List<MyModel>>() {

			public String getText(MyModel element) {
				return "Column 1 => " + element.toString();
			}
		});

		column = new TableViewerColumn<MyModel,List<MyModel>>(v, SWT.NONE);
		column.getColumn().setWidth(200);
		column.getColumn().setText("Column 2");
		column.setLabelProvider(new CenterImageLabelProvider() {

			protected Image getImage(Object element) {
				return images[((MyModel) element).counter % 4];
			}

		});

		// OwnerDrawLabelProvider.setUpOwnerDraw(v);

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
		Display display = new Display();

		images = new Image[4];
		images[0] = createImage(display, 0, 0, 255);
		images[1] = createImage(display, 0, 255, 255);
		images[2] = createImage(display, 0, 255, 0);
		images[3] = createImage(display, 255, 0, 255);

		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		new Snippet051TableCenteredImage(shell);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		for (int i = 0; i < images.length; i++) {
			images[i].dispose();
		}

		display.dispose();

	}

}
