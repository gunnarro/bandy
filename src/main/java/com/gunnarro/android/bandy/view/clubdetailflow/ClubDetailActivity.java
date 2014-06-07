package com.gunnarro.android.bandy.view.clubdetailflow;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;
import com.gunnarro.android.bandy.view.dialog.DialogSelection.NoticeDialogListener;
import com.gunnarro.android.bandy.view.dialog.ItemSelection;

/**
 * An activity representing a single Item detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link ClubListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link ClubDetailFragment}.
 */
public class ClubDetailActivity extends FragmentActivity implements NoticeDialogListener, ClubListFragment.Callbacks {

	private int clubId;

	// **********************************************************************
	// NoticeDialogListener Methods
	// **********************************************************************
	// The dialog fragment receives a reference to this Activity through the
	// Fragment.onAttach() callback, which it uses to call the following methods
	// defined by the NoticeDialogFragment.NoticeDialogListener interface
	@Override
	public void onDialogPositiveClick(ItemSelection dialog) {
		// User touched the dialog's positive button
		// setInputValue(getView(), R.id.teamCoachTxt, dialog.getTag());
	}

	@Override
	public void onDialogNegativeClick(ItemSelection dialog) {
		// User touched the dialog's negative button
	}

	// **********************************************************************

	/**
	 * Callback method from {@link ClubListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(int id) {
		CustomLog.e(this.getClass(), "tracing only...id:" + id);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.club_details_container_layout);
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
			clubId = getIntent().getIntExtra(DashboardActivity.ARG_CLUB_ID, -1);
			arguments.putInt(DashboardActivity.ARG_CLUB_ID, clubId);
			ClubDetailFragment fragment = new ClubDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.club_details_container_id, fragment).commit();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit:
			Bundle arguments = new Bundle();
			arguments.putInt(DashboardActivity.ARG_CLUB_ID, clubId);
			ClubEditFragment fragment = new ClubEditFragment();
			fragment.setArguments(arguments);
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.club_details_container_id, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
			return true;
		default:
			break;
		}
		CustomLog.d(this.getClass(), "clicked on: " + item.toString());
		return false;
	}
}
