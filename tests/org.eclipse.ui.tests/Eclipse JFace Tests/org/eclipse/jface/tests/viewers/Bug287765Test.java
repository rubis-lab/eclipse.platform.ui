/*******************************************************************************
 * Copyright (c) 2009 Chris Horneck and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Chris Horneck - initial API and implementation (bug 287765)
 *******************************************************************************/
package org.eclipse.jface.tests.viewers;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;

/**
 * @since 3.4
 *
 */
public class Bug287765Test extends TestCase {
	private TreeViewer<Node,Node> treeViewer;
	private Node root;

	/**
	 * An element in the Tree. Knows about its children and parent.
	 */
	private static class Node {
		private final Node parent;
		private final List<Node> children = new ArrayList<Node>();
		private final int level;

		private Node(Node parentNode, int nodeLevel) {
			this.parent = parentNode;
			this.level = nodeLevel;

			if (parent != null) {
				parent.children.add(this);
			}
		}
	}

	private final class SimpleTreeContentProvider implements
			ITreeContentProvider<Node,Node>, ILabelProvider<Node> {

		public Image getImage(Node element) {
			// TODO Auto-generated method stub
			return null;
		}

		public String getText(Node element) {
			Node node = element;
			return Integer.toString(node.level);
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public boolean isLabelProperty(Node element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}

		public Node[] getChildren(Node parentElement) {
			Node node = parentElement;
			Node[] children = new Node[node.children.size()];
			return node.children.toArray(children);
		}

		public boolean hasChildren(Node element) {
			Node node = element;
			return node.children.size() > 0;
		}

		public Node[] getElements(Node inputElement) {
			int depth = 4;

			Node node = new Node(root, 1);

			Node parentNode = node;
			for (int i = 2; i <= depth; i++) {
				Node newNode = new Node(parentNode, i);
				parentNode = newNode;
			}

			return new Node[] { node };
		}

		public Node getParent(Node element) {
			return element.parent;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer<? extends Node> viewer, Node oldInput, Node newInput) {
		}
	}

	protected void setUp() throws Exception {
		super.setUp();

		final Shell shell = new Shell();
		shell.setLayout(new GridLayout());
		shell.setSize(new Point(500, 200));

		treeViewer = new TreeViewer<Node,Node>(shell);
		treeViewer.getControl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));

		/*
		 * This is a key element to reproducing this bug. It causes
		 * AbstractTreeViewer#disassociate to recurse when it is disassociating
		 * TreeItems. This causes the data field in all of the TreeItems to get
		 * cleared out.
		 */
		treeViewer.setUseHashlookup(true);

		SimpleTreeContentProvider provider = new SimpleTreeContentProvider();

		treeViewer.setContentProvider(provider);
		treeViewer.setLabelProvider(provider);

		root = new Node(null, 0);

		treeViewer.setInput(root);
		shell.open();
	}

	protected void tearDown() throws Exception {
		treeViewer.getControl().getShell().dispose();
		treeViewer = null;
		root = null;
		super.tearDown();
	}

	/**
	 * Test to make the bug occur
	 */
	public void testException() {
		// Expand all the nodes
		treeViewer.expandAll();

		// Refresh the tree to generate new nodes
		treeViewer.refresh();

		// Retrieve the expanded paths
		treeViewer.getExpandedTreePaths();
	}
}
