package com.gunnarro.android.bandy.view.playerdetailflow;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

/**
 * An activity representing a single Item detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link PlayerListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link PlayerDetailFragment}.
 */
public class PlayerDetailActivity extends FragmentActivity {

	private String teamName;
	private Integer playerId;

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
			String clubName = getIntent().getStringExtra(DashboardActivity.ARG_CLUB_NAME);
			teamName = getIntent().getStringExtra(DashboardActivity.ARG_TEAM_NAME);
			setTitle(clubName);
			getActionBar().setTitle(teamName);

			playerId = getIntent().getIntExtra(DashboardActivity.ARG_PLAYER_ID, -1);
			arguments.putInt(DashboardActivity.ARG_PLAYER_ID, playerId);
			PlayerDetailFragment fragment = new PlayerDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.player_details_container_id, fragment).commit();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		CustomLog.e(this.getClass(), item.toString());
		switch (item.getItemId()) {
		case R.id.action_edit:
			teamName = getIntent().getStringExtra(DashboardActivity.ARG_TEAM_NAME);
			playerId = getIntent().getIntExtra(DashboardActivity.ARG_PLAYER_ID, -1);
			Bundle arguments = new Bundle();
			arguments.putInt(DashboardActivity.ARG_PLAYER_ID, playerId);
			PlayerEditFragment fragment = new PlayerEditFragment();
			fragment.setArguments(arguments);
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.player_details_container_id, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
			return true;
		default:
			// startActivity(new Intent(getApplicationContext(),
			// HomeActivity.class));
			break;
		}
		CustomLog.d(this.getClass(), "clicked on: " + item.toString());
		return false;
	}
}
