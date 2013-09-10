/*******************************************************************************
 * Copyright (c) 2006, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Hendrik Still <hendrik.still@gammas.de> - bug 413973
 ******************************************************************************/

package org.eclipse.jface.viewers;

/**
 * An extension to {@link ILabelProvider} that is given the
 * path of the element being decorated, when it is available.
 * @since 3.2
 * @param <E> Type of an element of the model
 */
public interface ITreePathLabelProvider<E> extends IBaseLabelProvider<E> {

    /**
     * Updates the label for the given element.
     *
     * @param label the label to update
     * @param elementPath the path of the element being decorated
     */
    public void updateLabel(ViewerLabel label, TreePath<E> elementPath);
}
