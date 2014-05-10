package com.gunnarro.android.bandy.view.clubdetailflow;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;
import com.gunnarro.android.bandy.view.dialog.DialogSelection.NoticeDialogListener;
import com.gunnarro.android.bandy.view.dialog.ItemSelection;

public class NewClubActivity extends FragmentActivity implements NoticeDialogListener, ClubListFragment.Callbacks {

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
			String clubName = getIntent().getStringExtra(DashboardActivity.ARG_CLUB_NAME);
			Bundle arguments = new Bundle();
			ClubEditFragment fragment = new ClubEditFragment();
			arguments.putString(DashboardActivity.ARG_CLUB_NAME, clubName);
			arguments.putString(DashboardActivity.ARG_TEAM_NAME, "New Club");
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.club_details_container_id, fragment).commit();
		}
	}

}
