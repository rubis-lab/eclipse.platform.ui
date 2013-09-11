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
package org.eclipse.jface.tests.viewers.interactive;

import java.util.ArrayList;

import org.eclipse.jface.tests.viewers.TestElement;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class Filter extends ViewerFilter<TestElement,TestElement> {

    public TestElement[] filter(Viewer<TestElement> viewer, Object parent, TestElement[] elements) {
        ArrayList<TestElement> result = new ArrayList<TestElement>();
        for (int i = 0; i < elements.length; ++i) {
            // toss every second item
            if (i % 2 == 1) {
                result.add(elements[i]);
            }
        }
        TestElement[] resultArray = new TestElement[result.size()];
        return result.toArray(resultArray);
    }

    public boolean isFilterProperty(Object element, Object aspect) {
        return false;
    }

    /* (non-Javadoc)
     * Method declared on ViewerFilter
     */
    public boolean select(Viewer<TestElement> viewer, Object parentElement, TestElement element) {
        // not used
        return false;
    }
}
