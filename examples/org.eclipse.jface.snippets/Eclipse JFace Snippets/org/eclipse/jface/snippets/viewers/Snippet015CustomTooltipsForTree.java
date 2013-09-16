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

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Explore New API: JFace custom tooltips drawing.
 *
 * @author Tom Schindl <tom.schindl@bestsolution.at>
 * @since 3.3M2
 */
public class Snippet015CustomTooltipsForTree {
	private static class MyContentProvider implements ITreeContentProvider<String,Object> {

		private static final String ROOT = "Root";

		public String[] getElements(Object inputElement) {
			return new String[]{ROOT};
		}

		public void dispose() {

		}

		public void inputChanged(Viewer<? extends Object> viewer, Object oldInput, Object newInput) {


		}

		public String[] getChildren(String parentElement) {
			if(parentElement.equals(ROOT))
				return new String[] { "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten" };
			return new String[0];
		}

		public String getParent(String element) {
			return null;
		}

		public boolean hasChildren(String element) {
			return element.equals(ROOT);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final Display display = new Display ();
		Shell shell = new Shell (display);
	    shell.setLayout(new FillLayout());

	    TreeViewer<String,Object> v = new TreeViewer<String,Object>(shell,SWT.FULL_SELECTION);
	    v.getTree().setLinesVisible(true);
	    v.getTree().setHeaderVisible(true);
	    ColumnViewerToolTipSupport.enableFor(v);

	    v.setContentProvider(new MyContentProvider());

	    CellLabelProvider<String,Object> labelProvider = new CellLabelProvider<String,Object>() {

			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.ViewerLabelProvider#getTooltipText(java.lang.Object)
			 */
			public String getToolTipText(String element) {
				return "Tooltip (" + element + ")";
			}

			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.ViewerLabelProvider#getTooltipShift(java.lang.Object)
			 */
			public Point getToolTipShift(String object) {
				return new Point(5,5);
			}

			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.ViewerLabelProvider#getTooltipDisplayDelayTime(java.lang.Object)
			 */
			public int getToolTipDisplayDelayTime(String object) {
				return 2000;
			}

			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.ViewerLabelProvider#getTooltipTimeDisplayed(java.lang.Object)
			 */
			public int getToolTipTimeDisplayed(String object) {
				return 5000;
			}

			public void update(ViewerCell<String> cell) {
				cell.setText(cell.getElement().toString());

			}
	    };


	    v.setLabelProvider(labelProvider);
	    v.setInput("");

	    shell.setSize(200,200);
	    shell.open ();

	    while (!shell.isDisposed()) {
	        if (!display.readAndDispatch ()) {
	        	display.sleep ();
	        }
	    }

	    display.dispose ();
	}

}
