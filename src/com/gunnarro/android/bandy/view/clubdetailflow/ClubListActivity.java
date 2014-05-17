package com.gunnarro.android.bandy.view.clubdetailflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;
import com.gunnarro.android.bandy.view.listener.ReloadListener;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ClubDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ClubListFragment} and the item details (if present) is a
 * {@link ClubDetailFragment}.
 * <p>
 * This activity also implements the required {@link ClubListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class ClubListActivity extends DashboardActivity implements ClubListFragment.Callbacks {

	public final static int REQUEST_CODE_CLUB_NEW = 200;
	public final static int REQUEST_CODE_CLUB_DETAIL = 201;
	public final static int REQUEST_CODE_CLUB_DELET = 202;
	public final static int RESULT_CODE_CLUB_CHANGED = 1;
	public final static int RESULT_CODE_CLUB_UNCHANGED = 0;

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.club_item_list);
		this.setTitle("Clubs");

		CustomLog.d(this.getClass(), "onCreate state: " + savedInstanceState);
		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			ClubListFragment fragment = new ClubListFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.club_item_list_id, fragment).commit();
			CustomLog.e(this.getClass(), "tracing call...not saved state");
		}
		// TODO: If exposing deep links into your app, handle intents here.
		CustomLog.d(this.getClass(), "is Two Pane layout : " + mTwoPane);
		CustomLog.e(this.getClass(), "tracing call...");
	}

	/**
	 * Callback method from {@link ClubListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(int id) {
		if (mTwoPane) {
		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, ClubDetailActivity.class);
			detailIntent.putExtra(ARG_CLUB_ID, id);
			startActivity(detailIntent);
		}
		CustomLog.e(this.getClass(), "tracing only...id:" + id);
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
			startActivityForResult(new Intent(getApplicationContext(), NewClubActivity.class), REQUEST_CODE_CLUB_NEW);
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
		case RESULT_CODE_CLUB_CHANGED:
			reloadListData();
			break;
		case RESULT_CODE_CLUB_UNCHANGED:
			// do nothing
			break;
		default:
			CustomLog.e(this.getClass(), "Unkown result code: " + resultCode);
			break;
		}
		CustomLog.e(getClass(), "requestCode=" + requestCode + ", resultCode=" + resultCode);
	}

	private void reloadListData() {
		ReloadListener listener = (ReloadListener) getSupportFragmentManager().findFragmentById(R.id.club_item_list_id);
		listener.reloadData();
	}

}
