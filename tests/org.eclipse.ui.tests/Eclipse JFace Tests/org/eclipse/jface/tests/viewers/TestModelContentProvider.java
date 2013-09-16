/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jface.tests.viewers;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IBasicPropertyConstants;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

public class TestModelContentProvider implements ITestModelListener,
        IStructuredContentProvider<TestElement,TestElement>, ITreeContentProvider<TestElement,TestElement> {
    Viewer<? extends TestElement> fViewer;

    public void dispose() {
    }

    protected void doInsert(TestModelChange change) {
        if (fViewer instanceof ListViewer) {
            if (change.getParent() != null
                    && change.getParent().equals(fViewer.getInput())) {
                ((ListViewer<TestElement,TestElement>) fViewer).add(change.getChildren());
            }
        } else if (fViewer instanceof TableViewer) {
            if (change.getParent() != null
                    && change.getParent().equals(fViewer.getInput())) {
                ((TableViewer<TestElement,TestElement>) fViewer).add(change.getChildren());
            }
        } else if (fViewer instanceof AbstractTreeViewer) {
            ((AbstractTreeViewer<TestElement,TestElement>) fViewer).add(change.getParent(), change
                    .getChildren());
        } else if (fViewer instanceof ComboViewer) {
            ((ComboViewer<TestElement,TestElement>) fViewer).add(change.getChildren());
        } else {
            Assert.isTrue(false, "Unknown kind of viewer");
        }
    }

    protected void doNonStructureChange(TestModelChange change) {
        if (fViewer instanceof StructuredViewer) {
            ((StructuredViewer<TestElement,TestElement>) fViewer).update(change.getParent(),
                    new String[] { IBasicPropertyConstants.P_TEXT });
        } else {
            Assert.isTrue(false, "Unknown kind of viewer");
        }
    }

    protected void doRemove(TestModelChange change) {
        if (fViewer instanceof ListViewer) {
            ((ListViewer<TestElement,TestElement>) fViewer).remove(change.getChildren());
        } else if (fViewer instanceof TableViewer) {
            ((TableViewer<TestElement,TestElement>) fViewer).remove(change.getChildren());
        } else if (fViewer instanceof AbstractTreeViewer) {
            ((AbstractTreeViewer<TestElement,TestElement>) fViewer).remove(change.getChildren());
        } else if (fViewer instanceof ComboViewer) {
            ((ComboViewer<TestElement,TestElement>) fViewer).remove(change.getChildren());
        } else {
            Assert.isTrue(false, "Unknown kind of viewer");
        }
    }

    protected void doStructureChange(TestModelChange change) {
        if (fViewer instanceof StructuredViewer) {
            ((StructuredViewer<TestElement,TestElement>) fViewer).refresh(change.getParent());
        } else {
            Assert.isTrue(false, "Unknown kind of viewer");
        }
    }

    public TestElement[] getChildren(TestElement element) {
        TestElement testElement = element;
        int count = testElement.getChildCount();
        TestElement[] children = new TestElement[count];
        for (int i = 0; i < count; ++i)
            children[i] = testElement.getChildAt(i);
        return children;
    }

    public TestElement[] getElements(TestElement element) {
        return getChildren(element);
    }

    public TestElement getParent(TestElement element) {
        return element.getContainer();
    }

    public boolean hasChildren(TestElement element) {
        return element.getChildCount() > 0;
    }

    public void inputChanged(Viewer<? extends TestElement> viewer, TestElement oldInput, TestElement newInput) {
        fViewer = viewer;
        TestElement oldElement = oldInput;
        if (oldElement != null) {
            oldElement.getModel().removeListener(this);
        }
        TestElement newElement = newInput;
        if (newElement != null) {
            newElement.getModel().addListener(this);
        }
    }

    public boolean isDeleted(Object element) {
        return ((TestElement) element).isDeleted();
    }

    public void testModelChanged(TestModelChange change) {
        switch (change.getKind()) {
        case TestModelChange.INSERT:
            doInsert(change);
            break;
        case TestModelChange.REMOVE:
            doRemove(change);
            break;
        case TestModelChange.STRUCTURE_CHANGE:
            doStructureChange(change);
            break;
        case TestModelChange.NON_STRUCTURE_CHANGE:
            doNonStructureChange(change);
            break;
        default:
            throw new IllegalArgumentException("Unknown kind of change");
        }

        StructuredSelection selection = new StructuredSelection(change
                .getChildren());
        if ((change.getModifiers() & TestModelChange.SELECT) != 0) {
            ((StructuredViewer) fViewer).setSelection(selection);
        }
        if ((change.getModifiers() & TestModelChange.REVEAL) != 0) {
            Object element = selection.getFirstElement();
            if (element != null) {
                ((StructuredViewer) fViewer).reveal(element);
            }
        }
    }
}
