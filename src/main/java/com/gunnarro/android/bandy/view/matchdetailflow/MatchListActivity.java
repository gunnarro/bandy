package com.gunnarro.android.bandy.view.matchdetailflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.service.exception.ApplicationException;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link MatchDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link MatchListFragment} and the item details (if present) is a
 * {@link MatchDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link MatchListFragment.Callbacks} interface to listen for item selections.
 */
public class MatchListActivity extends DashboardActivity implements MatchListFragment.Callbacks {

	private String clubName;
	private String teamName;

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.match_item_list);
		getArgs(savedInstanceState);
		this.setTitle(clubName);

		CustomLog.d(this.getClass(), "onCreate state: " + savedInstanceState);
		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putString(ARG_CLUB_NAME, clubName);
			arguments.putString(ARG_TEAM_NAME, teamName);
			MatchListFragment fragment = new MatchListFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.match_item_list_id, fragment).commit();
		}
		// TODO: If exposing deep links into your app, handle intents here.
		CustomLog.d(this.getClass(), "is Two Pane layout : " + mTwoPane);
	}

	private void getArgs(Bundle bundle) {
		if (bundle != null) {
			clubName = bundle.getString(ARG_CLUB_NAME, null);
			teamName = bundle.getString(ARG_TEAM_NAME, null);
		} else {
			clubName = getIntent().getStringExtra(ARG_CLUB_NAME);
			teamName = getIntent().getStringExtra(ARG_TEAM_NAME);
		}
		if (clubName == null) {
			throw new ApplicationException(this.getClass().getSimpleName() + ": Missing club name arg!");
		}
		if (teamName == null) {
			throw new ApplicationException(this.getClass().getSimpleName() + ": Missing team name arg!");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar_menu_new, menu);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_new:
			Intent detailIntent = new Intent(this, NewMatchActivity.class);
			detailIntent.putExtra(ARG_CLUB_NAME, clubName);
			detailIntent.putExtra(ARG_TEAM_NAME, teamName);
			startActivity(detailIntent);
			return true;
		default:
			// startActivity(new Intent(getApplicationContext(),
			// HomeActivity.class));
			break;
		}
		CustomLog.d(this.getClass(), "clicked on: " + item.getItemId());
		return false;
	}

	/**
	 * Callback method from {@link MatchListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(int id) {
		if (mTwoPane) {
		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, MatchDetailActivity.class);
			detailIntent.putExtra(ARG_CLUB_NAME, clubName);
			detailIntent.putExtra(ARG_TEAM_NAME, teamName);
			detailIntent.putExtra(DashboardActivity.ARG_MATCH_ID, id);
			startActivity(detailIntent);
		}
	}
}
