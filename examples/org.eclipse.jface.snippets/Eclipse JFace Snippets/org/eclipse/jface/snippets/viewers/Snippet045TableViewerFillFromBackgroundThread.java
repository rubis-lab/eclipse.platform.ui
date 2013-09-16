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
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Example how to fill a {@link TableViewer} from the background without
 * blocking the UI
 *
 * @author Tom Schindl <tom.schindl@bestsolution.at>
 *
 */
public class Snippet045TableViewerFillFromBackgroundThread {
	private static int COUNTER = 0;

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

	public class MyLabelProvider extends LabelProvider<MyModel> implements
			ITableLabelProvider<MyModel>, ITableFontProvider<MyModel>, ITableColorProvider<MyModel> {
		FontRegistry registry = new FontRegistry();

		public Image getColumnImage(MyModel element, int columnIndex) {
			return null;
		}

		public String getColumnText(MyModel element, int columnIndex) {
			return "Column " + columnIndex + " => " + element.toString();
		}

		public Font getFont(MyModel element, int columnIndex) {
			if (element.counter % 2 == 0) {
				return registry.getBold(Display.getCurrent().getSystemFont()
						.getFontData()[0].getName());
			}
			return null;
		}

		public Color getBackground(MyModel element, int columnIndex) {
			if (element.counter % 2 == 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
			}
			return null;
		}

		public Color getForeground(MyModel element, int columnIndex) {
			if (element.counter % 2 == 1) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
			}
			return null;
		}

	}

	public Snippet045TableViewerFillFromBackgroundThread(final Shell shell) {
		final TableViewer<MyModel,List<MyModel>> v = new TableViewer<MyModel,List<MyModel>>(shell, SWT.BORDER
				| SWT.FULL_SELECTION);
		v.setLabelProvider(new MyLabelProvider());
		v.setContentProvider(new MyContentProvider());

		TableColumn column = new TableColumn(v.getTable(), SWT.NONE);
		column.setWidth(200);
		column.setText("Column 1");

		column = new TableColumn(v.getTable(), SWT.NONE);
		column.setWidth(200);
		column.setText("Column 2");

		final ArrayList<MyModel> model = new ArrayList<MyModel>();
		v.setInput(model);
		v.setComparator(new ViewerComparator<MyModel,List<MyModel>>() {
			public int compare(Viewer<List<MyModel>> viewer, MyModel e1, MyModel e2) {
				return e2.counter - e1.counter;
			}

		});
		v.getTable().setLinesVisible(true);
		v.getTable().setHeaderVisible(true);

		TimerTask task = new TimerTask() {

			public void run() {
				shell.getDisplay().syncExec(new Runnable() {

					public void run() {
						MyModel el = new MyModel(++COUNTER);
						v.add(el);
						model.add(el);
					}
				});
			}
		};

		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(task, 1000, 1000);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = new Display();

		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		new Snippet045TableViewerFillFromBackgroundThread(shell);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();

	}

}
