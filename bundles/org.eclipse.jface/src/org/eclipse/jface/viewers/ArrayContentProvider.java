/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jface.viewers;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;

import org.eclipse.core.runtime.Assert;

/**
 * This implementation of <code>IStructuredContentProvider</code> handles
 * the case where the viewer input is an unchanging array or collection of elements.
 * <p>
 * This class is not intended to be subclassed outside the viewer framework.
 * </p>
 * @param <E> Type of an element of the model
 *
 * @since 2.1
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ArrayContentProvider<E> implements IStructuredContentProvider<E,Object> {

	private static HashMap<Class<?>,ArrayContentProvider<?>> instanceMap = new HashMap<Class<?>,ArrayContentProvider<?>>();
	@SuppressWarnings("rawtypes")
	private static ArrayContentProvider instance;
	private Class<E> componentType;



	/**
	 * Constructor for ArrayContentProvider
	 *
	 * <p>
	 * Only use this constructor if you are using rawtyped Viewers
	 * </p>
	 *
	 * @deprecated
	 */
	public ArrayContentProvider(){

	}

	/**
	 * Constructor for ArrayContentProvider
	 *
	 * @param componentType The classtype of the used element e.g. MyElement.class
	 * @since 3.10
	 *
	 */
	public ArrayContentProvider(Class<E> componentType) {
		this.componentType = componentType;
	}

	/**
	 * Returns an instance of ArrayContentProvider. Since instances of this
	 * class do not maintain any state, they can be shared between multiple
	 * clients.
	 *
	 * <p>
	 * Only use this method if you are using rawtyped Viewers
	 * </p>
	 *
	 * @return an instance of ArrayContentProvider
	 *
	 * @since 3.5
	 * @deprecated
	 */
	@SuppressWarnings("rawtypes")
	public static ArrayContentProvider getInstance() {
		synchronized(ArrayContentProvider.class) {
			if (instance == null) {
				instance = new ArrayContentProvider();
			}
			return instance;
		}
	}

	/**
	 * Returns an instance of ArrayContentProvider. Since instances of this
	 * class do not maintain any state, they can be shared between multiple
	 * clients.
	 * @param componentType The classtype of the used element e.g. MyElement.class
	 *
	 * @return an instance of ArrayContentProvider
	 *
	 * @since 3.10
	 */
	public static <E> ArrayContentProvider<E> getInstance(Class<E> componentType) {

		synchronized (instanceMap) {
			@SuppressWarnings("unchecked")
			ArrayContentProvider<E> arrayContentProvider = (ArrayContentProvider<E>) instanceMap
					.get(componentType);
			if (arrayContentProvider == null) {
				arrayContentProvider = new ArrayContentProvider<E>(
						componentType);
				instanceMap.put(componentType, arrayContentProvider);
			}
			return arrayContentProvider;
		}
	}
    /**
     * Returns the elements in the input, which must be either an array or a
     * <code>Collection</code> of the element type.
     */
    @SuppressWarnings("unchecked")
	public E[] getElements(Object inputElement) {
		if (componentType != null) {
			if (inputElement instanceof Object[]) {
				Assert.isTrue(inputElement.getClass().getComponentType()
						.isAssignableFrom(componentType),"The type of the input ("+inputElement.getClass().getComponentType()+") is not compatible to the ArrayContentProvider Type ("+componentType+")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return (E[]) inputElement;
			}
			if (inputElement instanceof Collection) {
				E[] elementArray = (E[]) Array.newInstance(componentType,
						((Collection<E>) inputElement).size());
				return ((Collection<E>) inputElement).toArray(elementArray);
			}
			return (E[]) Array.newInstance(componentType, 0);
		}
		//If the componentType is not given, the ArrayContentProvider should be an instance of a rawtype
		if (inputElement instanceof Object[]) {
			return (E[]) inputElement;
		}
		if (inputElement instanceof Collection) {
			@SuppressWarnings("rawtypes")
			Collection collection = ((Collection) inputElement);
			return (E[]) collection.toArray();
		}
		return (E[]) new Object[0];
    }

    /**
     * This implementation does nothing.
     */
    public void inputChanged(Viewer<? extends Object> viewer, Object oldInput, Object newInput) {
    }

    /**
     * This implementation does nothing.
     */
    public void dispose() {
        // do nothing.
    }
}
