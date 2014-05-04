package com.gunnarro.android.bandy.view.playerdetailflow;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.service.exception.ApplicationException;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

public class NewPlayerActivity extends FragmentActivity {

	private String clubName;
	private String teamName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_details_container_layout);
		getArgs(savedInstanceState);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(DashboardActivity.ARG_CLUB_NAME, clubName);
			arguments.putString(DashboardActivity.ARG_TEAM_NAME, teamName);
			PlayerEditFragment fragment = new PlayerEditFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.player_details_container_id, fragment).commit();
		}
	}

	private void getArgs(Bundle bundle) {
		if (bundle != null) {
			clubName = bundle.getString(DashboardActivity.ARG_CLUB_NAME, null);
			teamName = bundle.getString(DashboardActivity.ARG_TEAM_NAME, null);
			CustomLog.e(this.getClass(), clubName + ", " + teamName);
		}

		if (clubName == null || teamName == null) {
			clubName = getIntent().getStringExtra(DashboardActivity.ARG_CLUB_NAME);
			teamName = getIntent().getStringExtra(DashboardActivity.ARG_TEAM_NAME);
			CustomLog.e(this.getClass(), clubName + ", " + teamName);
		}
		CustomLog.e(this.getClass(), clubName + ", " + teamName);
		if (clubName == null) {
			throw new ApplicationException(this.getClass().getSimpleName() + ": Missing club name arg!");
		}
		if (teamName == null) {
			throw new ApplicationException(this.getClass().getSimpleName() + ": Missing team name arg!");
		}
	}
}