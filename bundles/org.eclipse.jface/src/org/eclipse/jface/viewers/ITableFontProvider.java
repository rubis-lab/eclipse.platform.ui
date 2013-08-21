/*******************************************************************************
 * Copyright (c) 2004, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Hendrik Still <hendrik.still@gammas.de> - bug 415561
 *******************************************************************************/
package org.eclipse.jface.viewers;

import org.eclipse.swt.graphics.Font;

/**
 * The ITableFontProvider is a font provider that provides fonts to
 * individual cells within tables.
 * @param <E> Type of an single element of the model
 * @since 3.1
 */
public interface ITableFontProvider<E> {

	/**
	 * Provides a font for the given element at index
	 * columnIndex.
	 * @param element The element being displayed
	 * @param columnIndex The index of the column being displayed
	 * @return Font
	 */
	public Font getFont(E element, int columnIndex);

}
