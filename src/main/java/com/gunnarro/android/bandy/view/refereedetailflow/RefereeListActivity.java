package com.gunnarro.android.bandy.view.refereedetailflow;

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
 * {@link RefereeDetailActivity} representing item details. On tablets, theL41
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link RefereeListFragment} and the item details (if present) is a
 * {@link RefereeDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link RefereeListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class RefereeListActivity extends DashboardActivity implements RefereeListFragment.Callbacks {

	public final static int REQUEST_CODE_REFEREE_NEW = 400;
	public final static int REQUEST_CODE_REFEREE_DETAIL = 401;
	public final static int REQUEST_CODE_REFEREE_DELET = 402;

	private String clubName;
	private String teamName;

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane = false;

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// getArgs(outState);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.referee_item_list);
		getArgs(savedInstanceState);
		this.setTitle(clubName);
		// if (findViewById(R.id.item_detail_container) != null) {
		// }
		CustomLog.d(this.getClass(), "onCreate state: " + savedInstanceState);
		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putString(ARG_CLUB_NAME, clubName);
			arguments.putString(ARG_TEAM_NAME, teamName);
			RefereeListFragment fragment = new RefereeListFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.referee_item_list_id, fragment).commit();
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
		CustomLog.e(this.getClass(), clubName + "" + teamName);
	}

	/**
	 * Callback method from {@link RefereeListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(int id) {
		if (mTwoPane) {
		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, RefereeDetailActivity.class);
			detailIntent.putExtra(ARG_CLUB_NAME, clubName);
			detailIntent.putExtra(ARG_TEAM_NAME, teamName);
			detailIntent.putExtra(ARG_REFEREE_ID, id);
			startActivity(detailIntent);
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
			Intent newRefereeIntent = new Intent(getApplicationContext(), NewRefereeActivity.class);
			newRefereeIntent.putExtra(ARG_CLUB_NAME, clubName);
			newRefereeIntent.putExtra(ARG_TEAM_NAME, teamName);
			startActivity(newRefereeIntent);
			break;
		default:
			// startActivity(new Intent(getApplicationContext(),
			// HomeActivity.class));
			break;
		}
		CustomLog.d(this.getClass(), "clicked on: " + item.getItemId());
		return true;
	}
}
