/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.internal.activities;

import java.util.Set;

import org.eclipse.ui.activities.ActivityManagerEvent;
import org.eclipse.ui.activities.IActivity;
import org.eclipse.ui.activities.IActivityManager;
import org.eclipse.ui.activities.IActivityManagerListener;
import org.eclipse.ui.activities.ICategory;

public final class ProxyActivityManager extends AbstractActivityManager {

	private IActivityManager activityManager;

	public ProxyActivityManager(IActivityManager activityManager) {
		if (activityManager == null)
			throw new NullPointerException();

		this.activityManager = activityManager;

		this
			.activityManager
			.addActivityManagerListener(new IActivityManagerListener() {
			public void activityManagerChanged(ActivityManagerEvent activityManagerEvent) {
				ActivityManagerEvent proxyActivityManagerEvent =
					new ActivityManagerEvent(
						ProxyActivityManager.this,
						activityManagerEvent.haveDefinedActivityIdsChanged(),
						false, activityManagerEvent.haveEnabledActivityIdsChanged(), false);
				fireActivityManagerChanged(proxyActivityManagerEvent);
			}
		});
	}

	public IActivity getActivity(String activityId) {
		return activityManager.getActivity(activityId);
	}

	public Set getDefinedActivityIds() {
		return activityManager.getDefinedActivityIds();
	}

	public Set getEnabledActivityIds() {
		return activityManager.getEnabledActivityIds();
	}

	public ICategory getCategory(String categoryId) {
		return activityManager.getCategory(categoryId);
	}

	public Set getDefinedCategoryIds() {
		return activityManager.getDefinedCategoryIds();
	}

	public Set getEnabledCategoryIds() {
		return activityManager.getEnabledCategoryIds();
	}	
	
	public boolean isMatch(String string, Set activityIds) {
		return activityManager.isMatch(string, activityIds);
	}

	public boolean match(String string, Set activityIds) {
		return isMatch(string, activityIds);
	}	
	
	public Set getMatches(String string, Set activityIds) {
		return activityManager.getMatches(string, activityIds);
	}
}
