/*******************************************************************************
 * Copyright (c) 2007, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Michael Krkoska - initial API and implementation (bug 188333)
 *     Lars Vogel (lars.vogel@gmail.com) - Bug 413427
 *     Hendrik Still <hendrik.still@gammas.de> - bug 417676
 *******************************************************************************/
package org.eclipse.jface.snippets.viewers;

import java.io.File;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;

import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


/**
 * Using a {@link DelegatingStyledCellLabelProvider} on tree viewer with multiple columns. Compare the result with a native tree viewer.
 */
public class Snippet050DelegatingStyledCellLabelProvider {


	private static final int SHELL_WIDTH= 640;
	private static final Display DISPLAY= Display.getDefault();


	public static void main(String[] args) {

		JFaceResources.getColorRegistry().put(JFacePreferences.COUNTER_COLOR, new RGB(0,127,174));



		Shell shell= new Shell(DISPLAY, SWT.CLOSE | SWT.RESIZE);
		shell.setSize(SHELL_WIDTH, 300);
		shell.setLayout(new GridLayout(1, false));

		Snippet050DelegatingStyledCellLabelProvider example= new Snippet050DelegatingStyledCellLabelProvider();
		example.createPartControl(shell);

		shell.open();

		while (!shell.isDisposed()) {
			if (!DISPLAY.readAndDispatch()) {
				DISPLAY.sleep();
			}
		}
		DISPLAY.dispose();
	}

	public Snippet050DelegatingStyledCellLabelProvider() {
	}

	public void createPartControl(Composite parent) {
		Composite composite= new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(2, true));

		final DelegatingStyledCellLabelProvider<File,FileSystemRoot> styledCellLP1= new DelegatingStyledCellLabelProvider<File,FileSystemRoot>(new NameAndSizeLabelProvider());
		final DelegatingStyledCellLabelProvider<File,FileSystemRoot> styledCellLP2= new DelegatingStyledCellLabelProvider<File,FileSystemRoot>(new ModifiedDateLabelProvider());
		final ColumnViewer<File,FileSystemRoot> ownerDrawViewer= createViewer("Owner draw viewer:", composite, styledCellLP1, styledCellLP2); //$NON-NLS-1$

		CellLabelProvider<File,FileSystemRoot> normalLP1= new NameAndSizeLabelProvider();
		CellLabelProvider<File,FileSystemRoot> normalLP2= new ModifiedDateLabelProvider();
		final ColumnViewer<File,FileSystemRoot> normalViewer= createViewer("Normal viewer:", composite, normalLP1, normalLP2); //$NON-NLS-1$

		Composite buttons= new Composite(parent, SWT.NONE);
		buttons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		buttons.setLayout(new GridLayout(3, false));


		Button button1= new Button(buttons, SWT.PUSH);
		button1.setText("Refresh Viewers"); //$NON-NLS-1$
		button1.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				ownerDrawViewer.refresh();
				normalViewer.refresh();
			}
		});

		final Button button2= new Button(buttons, SWT.CHECK);
		button2.setText("Owner draw on column 1"); //$NON-NLS-1$
		button2.setSelection(true);
		button2.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				boolean newState= button2.getSelection();
				styledCellLP1.setOwnerDrawEnabled(newState);
				ownerDrawViewer.refresh();
			}
		});

		final Button button3= new Button(buttons, SWT.CHECK);
		button3.setText("Owner draw on column 2"); //$NON-NLS-1$
		button3.setSelection(true);
		button3.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				boolean newState= button3.getSelection();
				styledCellLP2.setOwnerDrawEnabled(newState);
				ownerDrawViewer.refresh();
			}
		});
	}

	private static class FileSystemRoot {
		public File[] getRoots() {
			return File.listRoots();
		}
	}

	private ColumnViewer<File,FileSystemRoot> createViewer(String description, Composite parent, CellLabelProvider<File,FileSystemRoot>labelProvider1, CellLabelProvider<File,FileSystemRoot> labelProvider2) {

		Composite composite= new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new GridLayout(1, true));

		Label label= new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		label.setText(description);

		TreeViewer<File,FileSystemRoot> treeViewer= new TreeViewer<File,FileSystemRoot>(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		treeViewer.getTree().setHeaderVisible(true);
		treeViewer.setContentProvider(new FileSystemContentProvider());

		TreeViewerColumn<File,FileSystemRoot> tvc1 = new TreeViewerColumn<File,FileSystemRoot>(treeViewer, SWT.NONE);
		tvc1.getColumn().setText("Name"); //$NON-NLS-1$
		tvc1.getColumn().setWidth(200);
		tvc1.setLabelProvider(labelProvider1);

		TreeViewerColumn<File,FileSystemRoot>tvc2 = new TreeViewerColumn<File,FileSystemRoot>(treeViewer, SWT.NONE);
		tvc2.getColumn().setText("Date Modified"); //$NON-NLS-1$
		tvc2.getColumn().setWidth(200);
		tvc2.setLabelProvider(labelProvider2);

		GridData data= new GridData(GridData.FILL, GridData.FILL, true, true);
		treeViewer.getControl().setLayoutData(data);

		treeViewer.setInput(new FileSystemRoot());

		return treeViewer;
	}

	/**
	 * A simple label provider
	 */
	private static class NameAndSizeLabelProvider extends ColumnLabelProvider<File,FileSystemRoot> implements IStyledLabelProvider<File> {

		private static int IMAGE_SIZE= 16;
		private static final Image IMAGE1= new Image(DISPLAY, DISPLAY.getSystemImage(SWT.ICON_WARNING).getImageData().scaledTo(IMAGE_SIZE, IMAGE_SIZE));
		private static final Image IMAGE2= new Image(DISPLAY, DISPLAY.getSystemImage(SWT.ICON_ERROR).getImageData().scaledTo(IMAGE_SIZE, IMAGE_SIZE));

		public Image getImage(File element) {
			File file = element;
			if (file.isDirectory()) {
				return IMAGE1;
			} else {
				return IMAGE2;
			}
		}

		public String getText(File element) {
			return getStyledText(element).toString();
		}

		public StyledString getStyledText(File element) {
			StyledString styledString= new StyledString();
			if (element instanceof File) {
				File file= element;
				if (file.getName().length() == 0) {
					styledString.append(file.getAbsolutePath());
				} else {
					styledString.append(file.getName());
				}
				if (file.isFile()) {
					String decoration= MessageFormat.format(" ({0} bytes)", new Object[] { new Long(file.length()) }); //$NON-NLS-1$
					styledString.append(decoration, StyledString.COUNTER_STYLER);
				}
			}
			return styledString;
		}
	}

	private static class ModifiedDateLabelProvider extends ColumnLabelProvider<File,FileSystemRoot> implements IStyledLabelProvider<File> {
		public String getText(File element) {
			return getStyledText(element).toString();
		}

		public StyledString getStyledText(File element) {
			StyledString styledString = new StyledString();
			File file = element;

			String date = DateFormat.getDateInstance().format(
					new Date(file.lastModified()));
			styledString.append(date);

			styledString.append(' ');

			String time = DateFormat.getTimeInstance(3).format(
					new Date(file.lastModified()));
			styledString.append(time, StyledString.COUNTER_STYLER);
			return styledString;
		}
	}

	private static class FileSystemContentProvider implements ITreeContentProvider<File,FileSystemRoot> {

		public File[] getChildren(File element) {
			File file = element;
			if (file.isDirectory()) {
				File[] listFiles = file.listFiles();
				if (listFiles != null) {
					return listFiles;
				}
			}
			return new File[0];
		}

		public File getParent(File element) {
			File file = element;
			return file.getParentFile();
		}

		public boolean hasChildren(File element) {
			return getChildren(element).length > 0;
		}

		public File[] getElements(FileSystemRoot inputElement) {
			return inputElement.getRoots();
		}

		public void dispose() {
		}

		public void inputChanged(Viewer<? extends FileSystemRoot> viewer, FileSystemRoot oldInput, FileSystemRoot newInput) {
		}
	}
}
