package com.gunnarro.android.bandy.view.playerdetailflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.view.dashboard.CreatePlayerActivity;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;
import com.gunnarro.android.bandy.view.dashboard.HomeActivity;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link PlayerDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link PlayerListFragment} and the item details (if present) is a
 * {@link PlayerDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link PlayerListFragment.Callbacks} interface to listen for item selections.
 */
public class PlayerListActivity extends DashboardActivity implements PlayerListFragment.Callbacks {

	private String teamName;

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.player_item_list);
		teamName = getIntent().getStringExtra(ARG_TEAM_NAME);
		if (teamName == null) {
			teamName = DashboardActivity.DEFAULT_TEAM_NAME;
		}
		this.setTitle(teamName);

		if (findViewById(R.id.item_detail_container) != null) {
		}
		CustomLog.d(this.getClass(), "onCreate state: " + savedInstanceState);
		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putString(ARG_TEAM_NAME, teamName);
			PlayerListFragment fragment = new PlayerListFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.player_item_list, fragment).commit();
		}
		// TODO: If exposing deep links into your app, handle intents here.
		CustomLog.d(this.getClass(), "is Two Pane layout : " + mTwoPane);
	}

	/**
	 * Callback method from {@link PlayerListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(int id) {
		if (mTwoPane) {
		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, PlayerDetailActivity.class);
			detailIntent.putExtra(ARG_TEAM_NAME, teamName);
			detailIntent.putExtra(PlayerDetailActivity.ARG_PLAYER_ID, id);
			startActivity(detailIntent);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar_menu_create, menu);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_new_activity:
			startActivity(new Intent(getApplicationContext(), CreatePlayerActivity.class));
			break;
		default:
			startActivity(new Intent(getApplicationContext(), HomeActivity.class));
			break;
		}
		CustomLog.d(this.getClass(), "clicked on: " + item.getItemId());
		return true;
	}
}
