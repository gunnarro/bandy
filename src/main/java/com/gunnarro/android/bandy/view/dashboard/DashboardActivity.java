/*
 * Copyright (C) 2011 Wglxy.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gunnarro.android.bandy.view.dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.view.clubdetailflow.ClubListActivity;
import com.gunnarro.android.bandy.view.contactdetailflow.ContactListActivity;
import com.gunnarro.android.bandy.view.matchdetailflow.MatchListActivity;
import com.gunnarro.android.bandy.view.playerdetailflow.PlayerListActivity;
import com.gunnarro.android.bandy.view.refereedetailflow.RefereeListActivity;
import com.gunnarro.android.bandy.view.teamdetailflow.TeamListActivity;

/**
 * This is the base class for activities in the dashboard application. It
 * implements methods that are useful to all top level activities. That
 * includes: (1) stub methods for all the activity lifecycle methods; (2)
 * onClick methods for clicks on home, search, feature 1, feature 2, etc. (3) a
 * method for displaying a message to the screen via the Toast class.
 * 
 * Make sure the Activity is changed to extend from FragmentActivity which adds
 * support for the fragment manager to all Android versions. Any activity using
 * fragments should make sure to extend from FragmentActivity:
 * 
 */
public abstract class DashboardActivity extends FragmentActivity {

	public final static int RESULT_CODE_CHANGED = 1;
	public final static int RESULT_CODE_UNCHANGED = 0;

	public static final String ARG_CLUB_NAME = "club_name";
	public static final String ARG_TEAM_NAME = "team_name";
	public static final String ARG_CLUB_ID = "club_id";
	public static final String ARG_TEAM_ID = "team_id";
	public static final String ARG_SEASON_ID = "season_id";
	public static final String ARG_PLAYER_ID = "player_id";
	public static final String ARG_CONTACT_ID = "contact_id";
	public static final String ARG_REFEREE_ID = "referee_id";
	public static final String ARG_MATCH_ID = "match_id";
	// public final static String DEFAULT_CLUB_NAME = "Ullevål Idretts Lag";
	// public final static String DEFAULT_TEAM_NAME = "UIL Knøtt 2003";
	// protected final static String DEFAULT_SEASON_PERIOD = "2013/2014";

	private static Integer selectedClubId = null;
	private static Integer selectedTeamId = null;
	private static String selectedClubName = null;
	private static String selectedTeamName = null;
	private static String selectedSeasonPeriod = null;

	public static void setClubName(String name) {
		selectedClubName = name;
	}

	public static void setTeamName(String name) {
		selectedTeamName = name;
	}

	public static void setSeasonPeriode(String seasonPeriod) {
		selectedSeasonPeriod = seasonPeriod;
	}

	public static Integer getSelectedClubId() {
		return selectedClubId;
	}

	public static void setSelectedClubId(Integer selectedClubId) {
		DashboardActivity.selectedClubId = selectedClubId;
	}

	public static Integer getSelectedTeamId() {
		return selectedTeamId;
	}

	public static void setSelectedTeamId(Integer selectedTeamId) {
		DashboardActivity.selectedTeamId = selectedTeamId;
	}

	public static String getSelectedTeamName() {
		return selectedTeamName;
	}

	public static void setSelectedTeamName(String selectedTeamName) {
		DashboardActivity.selectedTeamName = selectedTeamName;
	}

	/**
	 * onCreate - called when the activity is first created.
	 * 
	 * Called when the activity is first created. This is where you should do
	 * all of your normal static set up: create views, bind data to lists, etc.
	 * This method also provides you with a Bundle containing the activity's
	 * previously frozen state, if there was one.
	 * 
	 * Always followed by onStart().
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().setHomeButtonEnabled(true);
		// getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		// getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setDisplayUseLogoEnabled(true);
		getActionBar().show();
		// setContentView(R.layout.activity_default);
	}

	/**
	 * onDestroy The final call you receive before your activity is destroyed.
	 * This can happen either because the activity is finishing (someone called
	 * finish() on it, or because the system is temporarily destroying this
	 * instance of the activity to save space. You can distinguish between these
	 * two scenarios with the isFinishing() method.
	 * 
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * onPause Called when the system is about to start resuming a previous
	 * activity. This is typically used to commit unsaved changes to persistent
	 * data, stop animations and other things that may be consuming CPU, etc.
	 * Implementations of this method must be very quick because the next
	 * activity will not be resumed until this method returns. Followed by
	 * either onResume() if the activity returns back to the front, or onStop()
	 * if it becomes invisible to the user.
	 * 
	 */
	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * onRestart Called after your activity has been stopped, prior to it being
	 * started again. Always followed by onStart().
	 * 
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
	}

	/**
	 * onResume Called when the activity will start interacting with the user.
	 * At this point your activity is at the top of the activity stack, with
	 * user input going to it. Always followed by onPause().
	 * 
	 */
	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * onStart Called when the activity is becoming visible to the user.
	 * Followed by onResume() if the activity comes to the foreground, or
	 * onStop() if it becomes hidden.
	 * 
	 */
	@Override
	protected void onStart() {
		super.onStart();
	}

	/**
	 * onStop Called when the activity is no longer visible to the user because
	 * another activity has been resumed and is covering this one. This may
	 * happen either because a new activity is being started, an existing one is
	 * being brought in front of this one, or this one is being destroyed.
	 * 
	 * Followed by either onRestart() if this activity is coming back to
	 * interact with the user, or onDestroy() if this activity is going away.
	 */
	@Override
	protected void onStop() {
		super.onStop();
	}

	/**
 */
	// Click Methods

	/**
	 * Handle the click on the home button.
	 * 
	 * @param v
	 *            View
	 * @return void
	 */
	public void onClickHome(View v) {
		goHome(this);
	}

	/**
	 * Handle the click on the search button.
	 * 
	 * @param v
	 *            View
	 * @return void
	 */
	public void onClickSearch(View v) {
		startActivity(new Intent(getApplicationContext(), SearchActivity.class));
	}

	/**
	 * 
	 * @param v
	 */
	public void onClickAbout(View v) {
		startActivity(new Intent(getApplicationContext(), AboutActivity.class));
	}

	/**
	 * 
	 * @param v
	 */
	public void onClickFeature(View v) {
		// Bundle argsBundle = new Bundle();
		// argsBundle.putString(ARG_TEAM_NAME, selectedTeamName);
		switch (v.getId()) {
		case R.id.trainings_btn:
			startActivity(createIntent(TrainingsActivity.class));
			break;
		case R.id.matches_btn:
			startActivity(createIntent(MatchListActivity.class));
			break;
		/**
		 * case R.id.cups_btn: Toast.makeText(this, "Cups view not implements",
		 * Toast.LENGTH_SHORT).show(); break;
		 */
		case R.id.tournaments_btn:
			Toast.makeText(this, "Tournamnets view not implements", Toast.LENGTH_SHORT).show();
			break;
		case R.id.clubs_btn:
			startActivity(createIntent(ClubListActivity.class));
			break;
		case R.id.teams_btn:
			startActivity(createIntent(TeamListActivity.class));
			break;
		case R.id.players_btn:
			startActivity(createIntent(PlayerListActivity.class));
			break;
		case R.id.contacts_btn:
			startActivity(createIntent(ContactListActivity.class));
			break;
		case R.id.referee_btn:
			startActivity(createIntent(RefereeListActivity.class));
			break;
		case R.id.settings_btn:
			startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
			break;
		case R.id.search_btn:
			startActivity(new Intent(getApplicationContext(), SearchActivity.class));
			break;
		case R.id.statistic_btn:
			startActivity(createIntent(StatisticActivity.class));
			break;
		case R.id.about_btn:
			startActivity(new Intent(getApplicationContext(), AboutActivity.class));
			break;
		default:
			break;
		}
	}

	private Intent createIntent(Class<?> clazz) {
		Intent intent = new Intent(getApplicationContext(), clazz);
		intent.putExtra(ARG_CLUB_NAME, selectedClubName);
		intent.putExtra(ARG_TEAM_NAME, selectedTeamName);
		intent.putExtra(ARG_CLUB_ID, selectedClubId);
		intent.putExtra(ARG_TEAM_ID, selectedTeamId);
		return intent;
	}

	// private static Bundle createDefaultArguments(Intent intent) {
	// Bundle arguments = new Bundle();
	// String clubName = intent.getStringExtra(DashboardActivity.ARG_CLUB_NAME);
	// String teamName = intent.getStringExtra(DashboardActivity.ARG_TEAM_NAME);
	// if (clubName != null) {
	// arguments.putString(DashboardActivity.ARG_CLUB_NAME, clubName);
	// }
	// if (teamName != null) {
	// arguments.putString(DashboardActivity.ARG_TEAM_NAME, teamName);
	// }
	// return arguments;
	// }

	/**
 */
	// More Methods

	/**
	 * Go back to the home activity.
	 * 
	 * @param context
	 *            Context
	 * @return void
	 */
	public void goHome(Context context) {
		final Intent intent = new Intent(context, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

	/**
	 * Use the activity label to set the text in the activity's title text view.
	 * The argument gives the name of the view.
	 * 
	 * <p>
	 * This method is needed because we have a custom title bar rather than the
	 * default Android title bar. See the theme definitons in styles.xml.
	 * 
	 * @param textViewId
	 *            int
	 * @return void
	 */

	public void setTitleFromActivityLabel(int textViewId) {
		TextView tv = (TextView) findViewById(textViewId);
		if (tv != null) {
			tv.setText(getTitle());
		}
	}

	public void returnToParentView(Class<?> parent, Bundle param) {
		Intent intent = new Intent(getApplicationContext(), parent);
		intent.putExtra("param", param);
		startActivity(intent);
	}

	/**
	 * Show a string on the screen via Toast.
	 * 
	 * @param msg
	 *            String
	 * @return void
	 */

	public void toast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Send a message to the debug log and display it using Toast.
	 */
	public void trace(String msg) {
		Log.d("BandyApp", msg);
		toast(msg);
	}

} // end class
