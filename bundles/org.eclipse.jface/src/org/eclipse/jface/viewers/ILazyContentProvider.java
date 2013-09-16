/*******************************************************************************
 * Copyright (c) 2005, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
<<<<<<< HEAD
 *     Hendrik Still <hendrik.still@gammas.de> - bug 415573
=======
 *     Hendrik Still <hendrik.still@gammas.de> - bug 413973
>>>>>>> 6d6b20c... Bug 413973 - [Viewers] Add generics to the TreeViewer
 *******************************************************************************/
package org.eclipse.jface.viewers;

/**
 * The ILazyContentProvider is the content provider
 * for table viewers created using the SWT.VIRTUAL flag that
 * only wish to return their contents as they are queried.
 *
 * <strong>NOTE:</strong> As the ILazyContentProvider does
 * not have API for determining the total item count any
 * changes to the number of items for this object while
 * require a call to <code>#setItemCount</code> on the
 * viewer that uses it.
 * @param <I> Type of the input for the view
 *
 */
public interface ILazyContentProvider<I> extends IContentProvider<I> {
	/**
	 * Called when a previously-blank item becomes visible in the
	 * TableViewer. If the content provider knows the element
	 * at this row, it should respond by calling
	 * TableViewer#replace(Object, int).
	 *
	 * <strong>NOTE</strong> #updateElement(int index) can be used to determine selection
	 * values. TableViewer#replace(Object, int) is not called before
	 * returning from this method selections may have missing or stale elements.
	 * In this situation it is suggested that the selection is asked
	 * for again after he update.
	 *
	 * @param index The index that is being updated in the
	 * table.
	 */
	public void updateElement(int index);

}
