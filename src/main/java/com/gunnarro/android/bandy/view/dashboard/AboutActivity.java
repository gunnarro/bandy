package com.gunnarro.android.bandy.view.dashboard;

import android.os.Bundle;

import com.gunnarro.android.bandy.R;

/**
 * This is the About activity in the dashboard application. It displays some
 * text and provides a way to get back to the home activity.
 * 
 */

public class AboutActivity extends DashboardActivity {

	/**
	 * onCreate
	 * 
	 * Called when the activity is first created. This is where you should do
	 * all of your normal static set up: create views, bind data to lists, etc.
	 * This method also provides you with a Bundle containing the activity's
	 * previously frozen state, if there was one.
	 * 
	 * Always followed by onStart().
	 * 
	 * @param savedInstanceState
	 *            Bundle
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		setTitleFromActivityLabel(R.id.title_text);
	}

} // end class
