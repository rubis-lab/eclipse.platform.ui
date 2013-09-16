/*******************************************************************************
 * Copyright (c) 2006, 2013 Tom Schindl and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl - initial API and implementation
 *     Hendrik Still <hendrik.still@gammas.de> - bug 417676
 *******************************************************************************/

package org.eclipse.jface.snippets.viewers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Example usage of ViewerComparator in tables to allow sorting
 *
 * @author Tom Schindl <tom.schindl@bestsolution.at>
 *
 */
public class Snippet040TableViewerSorting {

	private class MyContentProvider implements IStructuredContentProvider<Person,List<Person>> {

		public Person[] getElements(List<Person> inputElement) {
			Person[] persons = new Person[inputElement.size()];
			return inputElement.toArray(persons);
		}

		public void dispose() {
		}

		public void inputChanged(Viewer<? extends List<Person>> viewer, List<Person> oldInput, List<Person> newInput) {
		}

	}

	public class Person {
		public String givenname;
		public String surname;
		public String email;

		public Person(String givenname, String surname, String email) {
			this.givenname = givenname;
			this.surname = surname;
			this.email = email;
		}

	}

	protected abstract class AbstractEditingSupport extends EditingSupport {
		private TextCellEditor editor;

		public AbstractEditingSupport(TableViewer<Person,List<Person>> viewer) {
			super(viewer);
			this.editor = new TextCellEditor(viewer.getTable());
		}

		protected boolean canEdit(Object element) {
			return true;
		}

		protected CellEditor getCellEditor(Object element) {
			return editor;
		}

		protected void setValue(Object element, Object value) {
			doSetValue(element, value);
			getViewer().update(element, null);
		}

		protected abstract void doSetValue(Object element, Object value);
	}

	public Snippet040TableViewerSorting(Shell shell) {
		TableViewer<Person,List<Person>> v = new TableViewer<Person,List<Person>>(shell, SWT.BORDER | SWT.FULL_SELECTION);
		v.setContentProvider(new MyContentProvider());

		TableViewerColumn<Person,List<Person>> column = new TableViewerColumn<Person,List<Person>>(v, SWT.NONE);
		column.getColumn().setWidth(200);
		column.getColumn().setText("Givenname");
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider<Person,List<Person>>() {

			public String getText(Person element) {
				return element.givenname;
			}
		});

		column.setEditingSupport(new AbstractEditingSupport(v) {

			protected Object getValue(Object element) {
				return ((Person) element).givenname;
			}

			protected void doSetValue(Object element, Object value) {
				((Person) element).givenname = value.toString();
			}

		});

		ColumnViewerSorter cSorter = new ColumnViewerSorter(v,column) {

			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Person p1 = (Person) e1;
				Person p2 = (Person) e2;
				return p1.givenname.compareToIgnoreCase(p2.givenname);
			}

		};

		column = new TableViewerColumn<Person,List<Person>>(v, SWT.NONE);
		column.getColumn().setWidth(200);
		column.getColumn().setText("Surname");
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider<Person,List<Person>>() {

			public String getText(Person element) {
				return element.surname;
			}

		});

		column.setEditingSupport(new AbstractEditingSupport(v) {

			protected Object getValue(Object element) {
				return ((Person) element).surname;
			}

			protected void doSetValue(Object element, Object value) {
				((Person) element).surname = value.toString();
			}

		});

		new ColumnViewerSorter(v,column) {

			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Person p1 = (Person) e1;
				Person p2 = (Person) e2;
				return p1.surname.compareToIgnoreCase(p2.surname);
			}

		};

		column = new TableViewerColumn<Person,List<Person>>(v, SWT.NONE);
		column.getColumn().setWidth(200);
		column.getColumn().setText("E-Mail");
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider<Person,List<Person>>() {

			public String getText(Person element) {
				return element.email;
			}

		});

		column.setEditingSupport(new AbstractEditingSupport(v) {

			protected Object getValue(Object element) {
				return ((Person) element).email;
			}

			protected void doSetValue(Object element, Object value) {
				((Person) element).email = value.toString();
			}

		});

		new ColumnViewerSorter(v,column) {

			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Person p1 = (Person) e1;
				Person p2 = (Person) e2;
				return p1.email.compareToIgnoreCase(p2.email);
			}

		};

		List<Person> model = createModel();
		v.setInput(model);
		v.getTable().setLinesVisible(true);
		v.getTable().setHeaderVisible(true);
		cSorter.setSorter(cSorter, ColumnViewerSorter.ASC);
	}

	private List<Person> createModel() {
		List<Person> elements = new ArrayList<Person>(4);
		elements.add(new Person("Tom", "Schindl",
				"tom.schindl@bestsolution.at"));
		elements.add(new Person("Boris", "Bokowski",
				"Boris_Bokowski@ca.ibm.com"));
		elements.add(new Person("Tod", "Creasey", "Tod_Creasey@ca.ibm.com"));
		elements.add(new Person("Wayne", "Beaton", "wayne@eclipse.org"));

		return elements;
	}

	private static abstract class ColumnViewerSorter extends ViewerComparator {
		public static final int ASC = 1;

		public static final int NONE = 0;

		public static final int DESC = -1;

		private int direction = 0;

		private TableViewerColumn<Person,List<Person>> column;

		private ColumnViewer<Person,List<Person>> viewer;

		public ColumnViewerSorter(ColumnViewer<Person,List<Person>> viewer, TableViewerColumn<Person,List<Person>> column) {
			this.column = column;
			this.viewer = viewer;
			this.column.getColumn().addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					if( ColumnViewerSorter.this.viewer.getComparator() != null ) {
						if( ColumnViewerSorter.this.viewer.getComparator() == ColumnViewerSorter.this ) {
							int tdirection = ColumnViewerSorter.this.direction;

							if( tdirection == ASC ) {
								setSorter(ColumnViewerSorter.this, DESC);
							} else if( tdirection == DESC ) {
								setSorter(ColumnViewerSorter.this, NONE);
							}
						} else {
							setSorter(ColumnViewerSorter.this, ASC);
						}
					} else {
						setSorter(ColumnViewerSorter.this, ASC);
					}
				}
			});
		}

		public void setSorter(ColumnViewerSorter sorter, int direction) {
			if( direction == NONE ) {
				column.getColumn().getParent().setSortColumn(null);
				column.getColumn().getParent().setSortDirection(SWT.NONE);
				viewer.setComparator(null);
			} else {
				column.getColumn().getParent().setSortColumn(column.getColumn());
				sorter.direction = direction;

				if( direction == ASC ) {
					column.getColumn().getParent().setSortDirection(SWT.DOWN);
				} else {
					column.getColumn().getParent().setSortDirection(SWT.UP);
				}

				if( viewer.getComparator() == sorter ) {
					viewer.refresh();
				} else {
					viewer.setComparator(sorter);
				}

			}
		}

		public int compare(Viewer viewer, Object e1, Object e2) {
			return direction * doCompare(viewer, e1, e2);
		}

		protected abstract int doCompare(Viewer viewer, Object e1, Object e2);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = new Display();

		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		new Snippet040TableViewerSorting(shell);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();

	}

}
