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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerEditor;
import org.eclipse.jface.viewers.TreeViewerFocusCellManager;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Shell;

/**
 * Demonstrates how to use keyboard-editing support in a TreeViewer with no column
 *
 * @author Tom Schindl <tom.schindl@bestsolution.at>
 *
 */
public class Snippet043NoColumnTreeViewerKeyboardEditing {
	public Snippet043NoColumnTreeViewerKeyboardEditing(final Shell shell) {
		Button b = new Button(shell, SWT.PUSH);
		b.setText("BBB");
		final TreeViewer<MyModel,MyModel> v = new TreeViewer<MyModel,MyModel>(shell, SWT.BORDER
				| SWT.FULL_SELECTION);
		b.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {

			}

			public void widgetSelected(SelectionEvent e) {
				MyModel root = v.getInput();
				TreePath path = new TreePath(new Object[] { root,
						root.child.get(1),
						root.child.get(1).child.get(0) });
				v.editElement(path, 0);
			}

		});

		v.setCellEditors(new CellEditor[] { new TextCellEditor(v.getTree()) });
		v.setColumnProperties(new String[] { "col1" });
		v.setCellModifier(new ICellModifier() {

			public boolean canModify(Object element, String property) {
				return true;
			}

			public Object getValue(Object element, String property) {
				return ((MyModel) element).counter + "";
			}

			public void modify(Object element, String property, Object value) {
				MyModel myModel = (MyModel)((Item) element).getData();
				myModel.counter = Integer.parseInt(value.toString());
				v.update(myModel, null);
			}

		});

		TreeViewerFocusCellManager focusCellManager = new TreeViewerFocusCellManager(
				v, new FocusCellOwnerDrawHighlighter(v));
		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(
				v) {
			protected boolean isEditorActivationEvent(
					ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
						|| event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.CR)
						|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
			}
		};

		TreeViewerEditor.create(v, focusCellManager, actSupport,
				ColumnViewerEditor.TABBING_HORIZONTAL
						| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
						| ColumnViewerEditor.TABBING_VERTICAL
						| ColumnViewerEditor.KEYBOARD_ACTIVATION);

		v.setContentProvider(new MyContentProvider());

		v.setInput(createModel());
	}

	private MyModel createModel() {

		MyModel root = new MyModel(0, null);
		root.counter = 0;

		MyModel tmp;
		MyModel subItem;
		for (int i = 1; i < 10; i++) {
			tmp = new MyModel(i, root);
			root.child.add(tmp);
			for (int j = 1; j < i; j++) {
				subItem = new MyModel(j, tmp);
				subItem.child.add(new MyModel(j * 100, subItem));
				tmp.child.add(subItem);
			}
		}

		return root;
	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		new Snippet043NoColumnTreeViewerKeyboardEditing(shell);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}

	private class MyContentProvider implements ITreeContentProvider<MyModel,MyModel> {

		public MyModel[] getElements(MyModel inputElement) {
			MyModel[] myModels = new MyModel[inputElement.child.size()];
			return inputElement.child.toArray(myModels);
		}

		public void dispose() {
		}

		public void inputChanged(Viewer<? extends MyModel> viewer, MyModel oldInput, MyModel newInput) {
		}

		public MyModel[] getChildren(MyModel parentElement) {
			return getElements(parentElement);
		}

		public MyModel getParent(MyModel element) {
			if (element == null) {
				return null;
			}
			return element.parent;
		}

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

}
