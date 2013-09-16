/*******************************************************************************
 * Copyright (c) 2006, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 *     Hendrik Still <hendrik.still@gammas.de> - bug 413973
 *******************************************************************************/

package org.eclipse.jface.viewers;

/**
 * TreeViewerLabelProvider is the ViewerLabelProvider that handles TreePaths.
 * @param <E> Type of an element of the model
 * @param <I> Type of the input
 *
 * @since 3.3
 *
 */
public class TreeColumnViewerLabelProvider<E,I> extends
		TableColumnViewerLabelProvider<E,I> {
	private ITreePathLabelProvider<E> treePathProvider = new ITreePathLabelProvider<E>() {
		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.ITreePathLabelProvider#updateLabel(org.eclipse.jface.viewers.ViewerLabel,
		 *      org.eclipse.jface.viewers.TreePath)
		 */
		public void updateLabel(ViewerLabel label, TreePath<E> elementPath) {
			// Do nothing by default

		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
		 */
		public void dispose() {
			// Do nothing by default

		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
		 */
		public void addListener(ILabelProviderListener<E> listener) {
			// Do nothing by default

		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
		 */
		public void removeListener(ILabelProviderListener<E> listener) {
			// Do nothing by default

		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
		 */
		public boolean isLabelProperty(E element, String property) {
			return false;
		}

	};

	/**
	 * Create a new instance of the receiver with the supplied labelProvider.
	 *
	 * @param labelProvider
	 */
	public TreeColumnViewerLabelProvider(IBaseLabelProvider<E> labelProvider) {
		super(labelProvider);
	}

	/**
	 * Update the label for the element with TreePath.
	 *
	 * @param label
	 * @param elementPath
	 */
	public void updateLabel(ViewerLabel label, TreePath<E> elementPath) {
		treePathProvider.updateLabel(label, elementPath);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ViewerLabelProvider#setProviders(java.lang.Object)
	 */
	@Override
	public void setProviders(Object provider) {
		super.setProviders(provider);
		if (provider instanceof ITreePathLabelProvider)
			treePathProvider = (ITreePathLabelProvider<E>) provider;
	}

	/**
	 * Return the ITreePathLabelProvider for the receiver.
	 *
	 * @return Returns the treePathProvider.
	 */
	public ITreePathLabelProvider<E> getTreePathProvider() {
		return treePathProvider;
	}

}
