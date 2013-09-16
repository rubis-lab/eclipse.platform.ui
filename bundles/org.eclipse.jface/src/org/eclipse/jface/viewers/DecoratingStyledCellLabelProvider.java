/*******************************************************************************
 * Copyright (c) 2008, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jface.viewers;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

/**
 * A {@link DecoratingStyledCellLabelProvider} is a
 * {@link DelegatingStyledCellLabelProvider} that uses a nested
 * {@link DelegatingStyledCellLabelProvider.IStyledLabelProvider} to compute
 * styled text label and image and takes a {@link ILabelDecorator} to decorate
 * the label.
 * 
 * <p>
 * Use this label provider as a replacement for the
 * {@link DecoratingLabelProvider} when decorating styled text labels.
 * </p>
 * 
 * <p>
 * The {@link DecoratingStyledCellLabelProvider} will try to evaluate the text
 * decoration added by the {@link ILabelDecorator} and will apply the style
 * returned by {@link #getDecorationStyle(Object)}
 * </p>
 * <p>
 * The {@link ILabelDecorator} can optionally implement {@link IColorDecorator}
 * and {@link IFontDecorator} to provide foreground and background color and
 * font decoration.
 * </p>
 * @param <E> Type of an element of the model
 * @param <I> Type of the input
 * 
 * @since 3.4
 */
public class DecoratingStyledCellLabelProvider<E,I> extends
		DelegatingStyledCellLabelProvider<E,I> {

	private ILabelDecorator<E> decorator;
	private IDecorationContext decorationContext= DecorationContext.DEFAULT_CONTEXT;
	private ILabelProviderListener<E> labelProviderListener;

	/**
	 * Creates a {@link DecoratingStyledCellLabelProvider} that delegates the
	 * requests for styled labels and for images to a
	 * {@link DelegatingStyledCellLabelProvider.IStyledLabelProvider}.
	 * 
	 * @param labelProvider
	 *            the styled label provider
	 * @param decorator
	 *            a label decorator or <code>null</code> to not decorate the
	 *            label
	 * @param decorationContext
	 *            a decoration context or <code>null</code> if the no
	 *            decorator is configured or the default decorator should be
	 *            used
	 */
	public DecoratingStyledCellLabelProvider(
			IStyledLabelProvider<E> labelProvider, ILabelDecorator<E> decorator,
			IDecorationContext decorationContext) {
		super(labelProvider);

		this.decorator = decorator;
		this.decorationContext = decorationContext != null ? decorationContext
				: DecorationContext.DEFAULT_CONTEXT;
		
		this.labelProviderListener = new ILabelProviderListener<E>() {
			public void labelProviderChanged(LabelProviderChangedEvent<E> event) {
				fireLabelProviderChanged(event);
			}
		};
		labelProvider.addListener(this.labelProviderListener);
		if (decorator != null)
			decorator.addListener(this.labelProviderListener);
	}

	/**
	 * Returns the decoration context associated with this label provider. It
	 * will be passed to the decorator if the decorator is an instance of
	 * {@link LabelDecorator}.
	 * 
	 * @return the decoration context associated with this label provider
	 */
	public IDecorationContext getDecorationContext() {
		return this.decorationContext;
	}

	/**
	 * Set the decoration context that will be based to the decorator for this
	 * label provider if that decorator implements {@link LabelDecorator}.
	 * 
	 * @param decorationContext
	 *            the decoration context.
	 */
	public void setDecorationContext(IDecorationContext decorationContext) {
		Assert.isNotNull(decorationContext);
		this.decorationContext = decorationContext;
	}

	private boolean waitForPendingDecoration(ViewerCell<E> cell) {
		if (this.decorator == null)
			return false;

		E element = cell.getElement();
		String oldText = cell.getText();

		boolean isDecorationPending = false;
		if (this.decorator instanceof LabelDecorator) {
			isDecorationPending = !((LabelDecorator<E>) this.decorator)
					.prepareDecoration(element, oldText, getDecorationContext());
		} else if (this.decorator instanceof IDelayedLabelDecorator) {
			isDecorationPending = !((IDelayedLabelDecorator<E>) this.decorator)
					.prepareDecoration(element, oldText);
		}
		if (isDecorationPending && oldText.length() == 0) {
			// item is empty: is shown for the first time: don't wait
			return false;
		}
		return isDecorationPending;
	}

	@Override
	public void update(ViewerCell<E> cell) {
		if (waitForPendingDecoration(cell)) {
			return; // wait until the decoration is ready
		}
		super.update(cell);
	}

	@Override
	public Color getForeground(E element) {
		if (this.decorator instanceof IColorDecorator) {
			@SuppressWarnings("unchecked")
			IColorDecorator<E> colorDecorator = (IColorDecorator<E>) this.decorator;
			Color foreground = colorDecorator.decorateForeground(element);
			if (foreground != null)
				return foreground;
		}
		return super.getForeground(element);
	}

	@Override
	public Color getBackground(E element) {
		if (this.decorator instanceof IColorDecorator) {
			@SuppressWarnings("unchecked")
			IColorDecorator<E> colorDecorator = (IColorDecorator<E>) this.decorator;
			Color color = colorDecorator.decorateBackground(element);
			if (color != null)
				return color;
		}
		return super.getBackground(element);
	}

	@Override
	public Font getFont(E element) {
		if (this.decorator instanceof IFontDecorator) {
			@SuppressWarnings("unchecked")
			IFontDecorator<E> fontDecorator = (IFontDecorator<E>) this.decorator;
			Font font = fontDecorator.decorateFont(element);
			if (font != null)
				return font;
		}
		return super.getFont(element);
	}

	@Override
	public Image getImage(E element) {
		Image image = super.getImage(element);
		if (this.decorator == null) {
			return image;
		}
		Image decorated = null;
		if (this.decorator instanceof LabelDecorator) {
			decorated = ((LabelDecorator<E>) this.decorator).decorateImage(image,
					element, getDecorationContext());
		} else {
			decorated = this.decorator.decorateImage(image, element);
		}
		if (decorated != null)
			return decorated;

		return image;
	}

	/**
	 * Returns the styled text for the label of the given element.
	 * 
	 * @param element
	 *            the element for which to provide the styled label text
	 * @return the styled text string used to label the element
	 */
	@Override
	protected StyledString getStyledText(E element) {
		StyledString styledString = super.getStyledText(element);
		if (this.decorator == null) {
			return styledString;
		}

		String label = styledString.getString();
		String decorated;
		if (this.decorator instanceof LabelDecorator) {
			decorated = ((LabelDecorator<E>) this.decorator).decorateText(label,
					element, getDecorationContext());
		} else {
			decorated = this.decorator.decorateText(label, element);
		}
		if (decorated == null)
			return styledString;

		Styler style = getDecorationStyle(element);
		return StyledCellLabelProvider.styleDecoratedString(decorated, style, styledString);
	}

	/**
	 * Sets the {@link StyledString.Styler} to be used for string
	 * decorations. By default the
	 * {@link StyledString#DECORATIONS_STYLER decoration style}. Clients
	 * can override.
	 * 
	 * Note that it is the client's responsibility to react on color changes of
	 * the decoration color by refreshing the view
	 * 
	 * @param element
	 *            the element that has been decorated
	 * 
	 * @return return the decoration style
	 */
	protected Styler getDecorationStyle(Object element) {
		return StyledString.DECORATIONS_STYLER;
	}

	/**
	 * Returns the decorator or <code>null</code> if no decorator is installed
	 * 
	 * @return the decorator or <code>null</code> if no decorator is installed
	 */
	public ILabelDecorator<E> getLabelDecorator() {
		return this.decorator;
	}

	/**
	 * Sets the label decorator. Removes all known listeners from the old
	 * decorator, and adds all known listeners to the new decorator. The old
	 * decorator is not disposed. Fires a label provider changed event
	 * indicating that all labels should be updated. Has no effect if the given
	 * decorator is identical to the current one.
	 * 
	 * @param newDecorator
	 *            the label decorator, or <code>null</code> if no decorations
	 *            are to be applied
	 */
	public void setLabelDecorator(ILabelDecorator<E> newDecorator) {
		ILabelDecorator<E> oldDecorator = this.decorator;
		if (oldDecorator != newDecorator) {
			if (oldDecorator != null)
				oldDecorator.removeListener(this.labelProviderListener);
			this.decorator = newDecorator;
			if (newDecorator != null) {
				newDecorator.addListener(this.labelProviderListener);
			}
		}
		fireLabelProviderChanged(new LabelProviderChangedEvent<E>(this));
	}

	@Override
	public void addListener(ILabelProviderListener<E> listener) {
		super.addListener(listener);
		if (this.decorator != null) {
			this.decorator.addListener(this.labelProviderListener);
		}
	}

	@Override
	public void removeListener(ILabelProviderListener<E> listener) {
		super.removeListener(listener);
		if (this.decorator != null && !isListenerAttached()) {
			this.decorator.removeListener(this.labelProviderListener);
		}
	}

	@Override
	public boolean isLabelProperty(E element, String property) {
		if (super.isLabelProperty(element, property)) {
			return true;
		}
		return this.decorator != null
				&& this.decorator.isLabelProperty(element, property);
	}

	@Override
	public void dispose() {
		super.dispose();
		if (this.decorator != null) {
			this.decorator.removeListener(this.labelProviderListener);
			this.decorator.dispose();
			this.decorator = null;
		}
	}

}
