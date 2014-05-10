package com.gunnarro.android.bandy.view.teamdetailflow;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;
import com.gunnarro.android.bandy.view.dialog.DialogSelection.NoticeDialogListener;
import com.gunnarro.android.bandy.view.dialog.ItemSelection;

public class NewTeamActivity extends FragmentActivity implements NoticeDialogListener {

	// The dialog fragment receives a reference to this Activity through the
	// Fragment.onAttach() callback, which it uses to call the following methods
	// defined by the NoticeDialogFragment.NoticeDialogListener interface
	@Override
	public void onDialogPositiveClick(ItemSelection dialog) {
		// User touched the dialog's positive button
		// setInputValue(getView(), R.id.teamCoachTxt, dialog.getTag());
		TeamEditFragment teamEditFragment = (TeamEditFragment) getSupportFragmentManager().findFragmentById(R.id.team_details_container_id);
		teamEditFragment.updateSelectedField(dialog.getSelectedItem(), dialog.getInputFieldId());
	}

	@Override
	public void onDialogNegativeClick(ItemSelection dialog) {
		// User touched the dialog's negative button
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_details_container_layout);
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
			Integer clubId = getIntent().getIntExtra(DashboardActivity.ARG_CLUB_ID, -1);
			String clubName = getIntent().getStringExtra(DashboardActivity.ARG_CLUB_NAME);
			Bundle arguments = new Bundle();
			TeamEditFragment fragment = new TeamEditFragment();
			arguments.putInt(DashboardActivity.ARG_CLUB_ID, clubId);
			arguments.putString(DashboardActivity.ARG_CLUB_NAME, clubName);
			arguments.putString(DashboardActivity.ARG_TEAM_NAME, "New Team");
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.team_details_container_id, fragment).commit();
		}
	}

}
