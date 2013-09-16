/*******************************************************************************
 * Copyright (c) 2007 Lasse Knudsen and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Lasse Knudsen - initial API and implementation, bug 205700
 *     Boris Bokowski, IBM - additional test cases
 *******************************************************************************/
package org.eclipse.jface.tests.viewers;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

public class Bug205700TreeViewerTest extends TestCase {

	private Shell shell;

	private TreeViewer<TreeNode,TreeNode> viewer;

	private TreeNode rootNode;

	private final TreeNode child1 = new TreeNode("Child1");

	private final TreeNode child5 = new TreeNode("Child5");

	private final TreeNode child10 = new TreeNode("Child10");

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		shell = new Shell();

		viewer = new TreeViewer<TreeNode,TreeNode>(shell, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

		viewer.setContentProvider(new InternalContentProvider());
		viewer.setLabelProvider(new InternalLabelProvider());

		viewer.setInput(createInput());

		shell.open();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		shell.close();
	}

	public void testAddWithoutSorter() throws Exception {
		assertItemNames(new String[] { "Child1", "Child5", "Child10" });

		rootNode.add(new TreeNode("Child2"));
		rootNode.add(new TreeNode("Child3"));
		rootNode.add(new TreeNode("Child4"));
		rootNode.add(new TreeNode("Child6"));
		rootNode.add(new TreeNode("Child7"));
		rootNode.add(new TreeNode("Child8"));
		rootNode.add(new TreeNode("Child9"));

		TreeNode[] children = new TreeNode[rootNode.getChildren().size()];
		viewer.add(rootNode, rootNode.getChildren().toArray(children));

		assertItemNames(new String[] { "Child1", "Child5", "Child10", "Child2",
				"Child3", "Child4", "Child6", "Child7", "Child8", "Child9" });
	}

	/**
	 * @param names
	 */
	private void assertItemNames(String[] names) {
		for (int i = 0; i < names.length; i++) {
			assertItemName(i, names[i]);
		}
	}

	/**
	 * @param index
	 * @param name
	 */
	private void assertItemName(int index, String name) {
		assertEquals("at " + index, name, viewer.getTree().getItem(index)
				.getText());
	}

	public void testAddWithSorter() throws Exception {
		assertItemNames(new String[] { "Child1", "Child5", "Child10" });
		viewer.setSorter(new ViewerSorter());
		assertItemNames(new String[] { "Child1", "Child10", "Child5" });

		rootNode.add(new TreeNode("Child2"));
		rootNode.add(new TreeNode("Child3"));
		rootNode.add(new TreeNode("Child4"));
		rootNode.add(new TreeNode("Child6"));
		rootNode.add(new TreeNode("Child7"));
		rootNode.add(new TreeNode("Child8"));
		rootNode.add(new TreeNode("Child9"));

		TreeNode[] children = new TreeNode[rootNode.getChildren().size()];
		viewer.add(rootNode, rootNode.getChildren().toArray(children));

		assertItemNames(new String[] { "Child1", "Child10", "Child2", "Child3",
				"Child4", "Child5", "Child6", "Child7", "Child8", "Child9" });
	}

	public void testAddEquallySortedElements() throws Exception {
		assertItemNames(new String[] { "Child1", "Child5", "Child10" });
		viewer.setSorter(new ViewerSorter());
		assertItemNames(new String[] { "Child1", "Child10", "Child5" });

		// add before the existing "Child1" node
		rootNode.getChildren().add(0, new TreeNode("Child1"));

		TreeNode[] children = new TreeNode[rootNode.getChildren().size()];
		viewer.add(rootNode, rootNode.getChildren().toArray(children));

		assertItemNames(new String[] { "Child1", "Child1", "Child10",
				"Child5" });
	}

	private TreeNode createInput() {
		rootNode = new TreeNode("Root");

		rootNode.add(child1);
		rootNode.add(child5);
		rootNode.add(child10);

		return rootNode;
	}

	private class TreeNode {

		private final String name;

		private TreeNode parent = null;

		private final List<TreeNode> children = new ArrayList<TreeNode>();

		public TreeNode(String name) {
			this.name = name;
		}

		public void add(TreeNode newChild) {
			if (newChild != null) {
				children.add(newChild);
			}
		}

		public List<TreeNode> getChildren() {
			return children;
		}

		public TreeNode getParent() {
			return parent;
		}

		public String getName() {
			return name;
		}

		public String toString() {
			return getName();
		}
	}

	private class InternalLabelProvider extends LabelProvider<TreeNode> {
		public String getText(TreeNode element) {
			if (element != null) {
				return element.getName();
			}
			return null;
		}
	}

	private class InternalContentProvider implements ITreeContentProvider<TreeNode,TreeNode> {
		public TreeNode[] getChildren(TreeNode parentElement) {
			if (parentElement != null) {
				TreeNode[] children = new TreeNode[parentElement.getChildren().size()];
				return parentElement.getChildren().toArray(children);
			}
			return new TreeNode[0];
		}

		public TreeNode getParent(TreeNode element) {
			if (element != null) {
				return element.getParent();
			}
			return null;
		}

		public boolean hasChildren(TreeNode element) {
			if (element != null) {
				return !element.getChildren().isEmpty();
			}
			return false;
		}

		public TreeNode[] getElements(TreeNode inputElement) {
			return getChildren(inputElement);
		}

		public void dispose() {
			// nothing
		}

		public void inputChanged(Viewer<? extends TreeNode> viewer, TreeNode oldInput, TreeNode newInput) {
			// nothing
		}
	}

}
