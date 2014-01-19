package com.gunnarro.android.bandy.view.playerdetailflow;

import android.content.Intent;
import android.os.Bundle;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

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

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_item_list);
		String teamName = getIntent().getStringExtra(ARG_TEAM_NAME);
		this.setTitle(teamName);

		if (findViewById(R.id.item_detail_container) != null) {
		}

		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putString(ARG_TEAM_NAME, teamName);
			PlayerListFragment fragment = new PlayerListFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.item_list, fragment).commit();
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
			detailIntent.putExtra(PlayerDetailActivity.ARG_PLAYER_ID, id);
			startActivity(detailIntent);
		}
	}
}
