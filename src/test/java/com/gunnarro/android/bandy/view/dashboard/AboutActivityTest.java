package com.gunnarro.android.bandy.view.dashboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.os.Bundle;

import com.gunnarro.android.bandy.R;

@RunWith(RobolectricTestRunner.class)
public class AboutActivityTest {

	AboutActivity activity;

	@Before
	public void setup() {
		activity = Robolectric.buildActivity(AboutActivity.class).create().get();
		Bundle bundle = new Bundle();
	}

	// @Ignore
	@Test
	public void checkActivityNotNull() throws Exception {
		assertNotNull(activity);
		// assertNotNull(
		// homeActivity.getSupportFragmentManager().findFragmentById( R.id. ) );
	}

	@Test
	public void checkView() throws Exception {
		String title = new AboutActivity().getResources().getString(R.string.about);
		assertEquals(title, activity.getTitle());
	}

}
