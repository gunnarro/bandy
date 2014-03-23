package com.gunnarro.bandy.repository.view;

import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.repository.view.MatchResultView;

import junit.framework.TestCase;

public class MatchResultViewTest extends TestCase {

	// @Test
	public void testQuery() {
		assertNotNull(MatchResultView.createMatchResultViewQuery());
		CustomLog.d(this.getClass(), MatchResultView.createMatchResultViewQuery());
	}
}
