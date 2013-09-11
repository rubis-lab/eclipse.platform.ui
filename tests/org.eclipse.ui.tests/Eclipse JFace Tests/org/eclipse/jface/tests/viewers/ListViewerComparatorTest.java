/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.jface.tests.viewers;

import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.Composite;

/**
 * @since 3.2
 *
 */
public class ListViewerComparatorTest extends ViewerComparatorTest {

	/**
	 * @param name
	 */
	public ListViewerComparatorTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.tests.viewers.ViewerTestCase#createViewer(org.eclipse.swt.widgets.Composite)
	 */
	protected StructuredViewer createViewer(Composite parent) {
        ListViewer<Object,Object> viewer = new ListViewer<Object,Object>(parent);
        viewer.setContentProvider(new TeamModelContentProvider());
        viewer.setLabelProvider(new TeamModelLabelProvider());
        comparatorTestViewer = viewer;
        return viewer;
	}

	public void testViewerSorter(){
		comparatorTestViewer.setSorter(new ViewerSorter());
		assertSortedResult(TEAM1_SORTED);
	}

	public void testViewerSorterInsertElement(){
		comparatorTestViewer.setSorter(new ViewerSorter());
		team1.addMember("Duong");
		assertSortedResult(TEAM1_SORTED_WITH_INSERT);
	}

	public void testViewerComparator(){
		comparatorTestViewer.setComparator(new ViewerComparator<Object,Object>());
		assertSortedResult(TEAM1_SORTED);
	}

	public void testViewerComparatorInsertElement(){
		comparatorTestViewer.setComparator(new ViewerComparator<Object,Object>());
		team1.addMember("Duong");
		assertSortedResult(TEAM1_SORTED_WITH_INSERT);
	}

	private void assertSortedResult(String[] expected){
		String[] items = getListViewer().getList().getItems();
		for (int i = 0; i < items.length; i++){
			String item = items[i];
			assertEquals("Item not expected.  actual=" + item + " expected=", expected[i], item);
		}
	}

	protected void setInput() {
		comparatorTestViewer.setInput(team1);
	}

	protected ListViewer<Object,Object> getListViewer(){
		return (ListViewer<Object,Object>)comparatorTestViewer;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(ListViewerComparatorTest.class);
	}

}
