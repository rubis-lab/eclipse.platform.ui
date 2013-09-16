/*******************************************************************************
 * Copyright (c) 2013 Hendrik Still and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Hendrik Still<hendrik.still@gammas.de> - initial API and implementation
 ******************************************************************************/

package org.eclipse.jface.tests.viewers;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;

/**
 * @since 3.5
 *
 */
public class ArrayContentProviderTest extends TestCase {

	private Shell shell;

	protected void setUp() throws Exception {
		super.setUp();

		shell = new Shell();
		shell.setLayout(new GridLayout());
		shell.setSize(new Point(500, 200));

		shell.open();
	}

	private List<String> getInput(){
		List<String> input = new ArrayList<String>();
		input.add("Test 1");
		input.add("Test 2");

		return input;
	}

	/**
	 * Tests of ArrayContentProvider works as typed ContentProvider
	 * @throws Exception
	 */
	public void testGenericArrayProvider() throws Exception {
		ListViewer<String, List<String>> listViewer = new ListViewer<String, List<String>>(shell);
		listViewer.setContentProvider(new ArrayContentProvider<String>(String.class));
		listViewer.setInput(getInput());


		String[] items = listViewer.getList().getItems();
		assertEquals(items[0], "Test 1");
		assertEquals(items[1], "Test 2");
	}

	/**
	 * Tests of ArrayContentProvider works as typed ContentProvider with array as input type
	 * @throws Exception
	 */
	public void testGenericArrayProviderWithArray() throws Exception {
		ListViewer<String, String[]> listViewer = new ListViewer<String, String[]>(shell);
		listViewer.setContentProvider(new ArrayContentProvider<String>(String.class));

		String[] input = new String[2];
		input[0] = "Test 1";
		input[1] = "Test 2";


		listViewer.setInput(input);


		String[] items = listViewer.getList().getItems();
		assertEquals(items[0], "Test 1");
		assertEquals(items[1], "Test 2");
	}

	/**
	 * Tests of ArrayContentProvider works as typed ContentProvider for a super type
	 * @throws Exception
	 */
	public void testGenericArrayProviderWithSuperType() throws Exception {
		ListViewer<String, List<String>> listViewer = new ListViewer<String, List<String>>(shell);
		listViewer.setContentProvider(new ArrayContentProvider<Object>(Object.class));
		listViewer.setInput(getInput());


		String[] items = listViewer.getList().getItems();
		assertEquals(items[0], "Test 1");
		assertEquals(items[1], "Test 2");
	}


	/**
	 * Tests if ArrayContentProvider throws Exception if wrong typed input is set.
	 * @throws Exception
	 */
	public void testWrongInputType() throws Exception {
		ListViewer<String, TestElement[]> listViewer = new ListViewer<String, TestElement[]>(
				shell);
		listViewer.setContentProvider(new ArrayContentProvider<String>(
				String.class));
		TestElement[] input = new TestElement[1];
		try {
			listViewer.setInput(input);
			fail("No AssertionFailedException");
		} catch (AssertionFailedException e) {
		}
	}

	/**
	 * Tests if ArrayContentProvider works as RawType without type information
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void testNonGenericArrayProvider() throws Exception {
		ListViewer listViewer = new ListViewer(shell);
		listViewer.setContentProvider(new ArrayContentProvider());
		listViewer.setInput(getInput());


		String[] items = listViewer.getList().getItems();
		assertEquals(items[0], "Test 1");
		assertEquals(items[1], "Test 2");
	}

	/**
	 * Tests if ArrayContentProvider.getInstance() returns the correct instances.
	 * @throws Exception
	 */
	public void testGenericArrayProviderSingelton() throws Exception {
		ArrayContentProvider<String> firstArrayContentProviderOfString = ArrayContentProvider.getInstance(String.class);
		ArrayContentProvider<String> secondArrayContentProviderOfString = ArrayContentProvider.getInstance(String.class);
		ArrayContentProvider<Integer> firstArrayContentProviderOfInteger = ArrayContentProvider.getInstance(Integer.class);
		assertSame(firstArrayContentProviderOfString, secondArrayContentProviderOfString);
		assertNotSame(firstArrayContentProviderOfString, firstArrayContentProviderOfInteger);
	}

	/**
	 * Tests if ArrayContentProvider.getInstance() returns the correct instances in case of rawtypes.
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void testNonGenericArrayProviderSingelton() throws Exception {
		ArrayContentProvider firstArrayContentProvider = ArrayContentProvider.getInstance();
		ArrayContentProvider secondArrayContentProvider = ArrayContentProvider.getInstance();
		assertSame(firstArrayContentProvider,secondArrayContentProvider);
	}

	protected void tearDown() throws Exception {
		shell.close();
		super.tearDown();
	}

}
