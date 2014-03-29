package com.gunnarro.android.bandy.view.playerdetailflow;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

public class NewPlayerActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_details_container_layout);
		setTitle("Player Details");
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
			String teamName = getIntent().getStringExtra(DashboardActivity.ARG_TEAM_NAME);
			PlayerEditFragment fragment = new PlayerEditFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.player_details_container_id, fragment).commit();
		}
	}

}
