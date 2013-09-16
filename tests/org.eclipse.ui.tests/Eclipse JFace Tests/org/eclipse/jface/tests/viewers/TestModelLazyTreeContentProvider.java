/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jface.tests.viewers;

import junit.framework.Assert;

import org.eclipse.jface.viewers.ILazyTreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

public class TestModelLazyTreeContentProvider extends TestModelContentProvider
		implements ILazyTreeContentProvider<TestElement,TestElement> {

	private final TreeViewer<TestElement,TestElement> treeViewer;

	public TestModelLazyTreeContentProvider(TreeViewer<TestElement,TestElement> treeViewer) {
		this.treeViewer = treeViewer;
	}

	public void updateElement(TestElement parent, int index) {
		TestElement parentElement = parent;
		if(parentElement.getChildCount() > index) {
			TestElement childElement = parentElement.getChildAt(index);
			treeViewer.replace(parent, index, childElement);
			treeViewer.setChildCount(childElement, childElement.getChildCount());
		}
	}

	public TestElement[] getChildren(TestElement element) {
		Assert.fail("should not be called on a LazyTreeContentProvider");
		return null;
	}

	public TestElement[] getElements(TestElement element) {
		Assert.fail("should not be called on a LazyTreeContentProvider");
		return null;
	}

	public boolean hasChildren(TestElement element) {
		Assert.fail("should not be called on a LazyTreeContentProvider");
		return false;
	}

	public void inputChanged(Viewer<? extends TestElement> viewer, TestElement oldInput,
			final TestElement newInput) {
		super.inputChanged(viewer, oldInput, newInput);
	}

	public void updateChildCount(TestElement element, int currentChildCount) {
		treeViewer.setChildCount(element, element.getChildCount());
	}

}
