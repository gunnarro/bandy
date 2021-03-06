package com.gunnarro.android.bandy.view.teamdetailflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.service.exception.ApplicationException;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;
import com.gunnarro.android.bandy.view.listener.ReloadListener;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link TeamDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link TeamListFragment} and the item details (if present) is a
 * {@link TeamDetailFragment}.
 * <p>
 * This activity also implements the required {@link TeamListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class TeamListActivity extends DashboardActivity implements TeamListFragment.Callbacks {

	public final static int REQUEST_CODE_TEAM_NEW = 100;
	public final static int REQUEST_CODE_TEAM_DETAIL = 101;
	public final static int REQUEST_CODE_TEAM_DELET = 102;
	public final static int RESULT_CODE_TEAM_CHANGED = 1;
	public final static int RESULT_CODE_TEAM_UNCHANGED = 0;

	private Integer clubId;
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
		this.setContentView(R.layout.team_item_list);
		getArgs(savedInstanceState);
		this.setTitle(clubName);

		if (findViewById(R.id.item_detail_container) != null) {
		}
		CustomLog.d(this.getClass(), "onCreate state: " + savedInstanceState);
		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putInt(ARG_CLUB_ID, clubId);
			arguments.putString(ARG_CLUB_NAME, clubName);
			arguments.putString(ARG_TEAM_NAME, teamName);
			TeamListFragment fragment = new TeamListFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.team_item_list_id, fragment).commit();
		}
		// TODO: If exposing deep links into your app, handle intents here.
		CustomLog.d(this.getClass(), "is Two Pane layout : " + mTwoPane);
	}

	private void getArgs(Bundle bundle) {
		if (bundle != null) {
			clubId = bundle.getInt(ARG_CLUB_ID);
			clubName = bundle.getString(ARG_CLUB_NAME, null);
			teamName = bundle.getString(ARG_TEAM_NAME, null);
		} else {
			clubId = getIntent().getIntExtra(ARG_CLUB_ID, -1);
			clubName = getIntent().getStringExtra(ARG_CLUB_NAME);
			teamName = getIntent().getStringExtra(ARG_TEAM_NAME);
		}
		if (clubId == null) {
			throw new ApplicationException(this.getClass().getSimpleName() + ": Missing club id arg!");
		}
		if (clubName == null) {
			throw new ApplicationException(this.getClass().getSimpleName() + ": Missing club name arg!");
		}
		if (teamName == null) {
			throw new ApplicationException(this.getClass().getSimpleName() + ": Missing team name arg!");
		}
		CustomLog.e(this.getClass(), clubId + ", " + clubName + ", " + teamName);
	}

	/**
	 * Callback method from {@link TeamListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(int id) {
		if (mTwoPane) {
		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, TeamDetailActivity.class);
			detailIntent.putExtra(ARG_CLUB_NAME, clubName);
			detailIntent.putExtra(ARG_TEAM_NAME, teamName);
			detailIntent.putExtra(DashboardActivity.ARG_CLUB_ID, clubId);
			detailIntent.putExtra(DashboardActivity.ARG_TEAM_ID, id);
			startActivityForResult(detailIntent, REQUEST_CODE_TEAM_DETAIL);
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
			Intent newTeamIntent = new Intent(getApplicationContext(), NewTeamActivity.class);
			newTeamIntent.putExtra(ARG_CLUB_ID, clubId);
			newTeamIntent.putExtra(ARG_CLUB_NAME, clubName);
			newTeamIntent.putExtra(ARG_TEAM_NAME, teamName);
			startActivityForResult(newTeamIntent, REQUEST_CODE_TEAM_NEW);
			break;
		case R.id.action_reload:
			CustomLog.e(this.getClass(), "action: " + item.getTitle());
			reloadListData();
		case android.R.id.home:
			super.finish();
			return true;
		default:
			break;
		}
		CustomLog.d(this.getClass(), "clicked on: " + item.getItemId());
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_CODE_TEAM_CHANGED:
			reloadListData();
			break;
		case RESULT_CODE_TEAM_UNCHANGED:
			// do nothing
			break;
		default:
			CustomLog.e(this.getClass(), "Unkown result code: " + resultCode);
			break;
		}
		CustomLog.e(getClass(), "requestCode=" + requestCode + ", resultCode=" + resultCode);
	}

	private void reloadListData() {
		ReloadListener listener = (ReloadListener) getSupportFragmentManager().findFragmentById(R.id.team_item_list_id);
		listener.reloadData();
	}
}
