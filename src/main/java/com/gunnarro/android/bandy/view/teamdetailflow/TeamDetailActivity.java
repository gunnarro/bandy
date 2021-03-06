package com.gunnarro.android.bandy.view.teamdetailflow;

import android.content.Intent;
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
 * side-by-side with a list of items in a {@link TeamListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link TeamDetailFragment}.
 */
public class TeamDetailActivity extends FragmentActivity implements NoticeDialogListener {

	private String clubName;
	private String teamName;
	private Integer teamId;

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
		TeamEditFragment teamEditFragment = (TeamEditFragment) getSupportFragmentManager().findFragmentById(R.id.team_details_container_id);
		//teamEditFragment.updateSelectedField(dialog.getSelectedItem(), dialog.getInputFieldId());
		teamEditFragment.updateSelectedField(dialog.getSelectedItemList(), dialog.getInputFieldId());
	}

	@Override
	public void onDialogNegativeClick(ItemSelection dialog) {
		// User touched the dialog's negative button
	}

	// **********************************************************************

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
			Bundle arguments = new Bundle();
			// clubName =
			// getIntent().getStringExtra(DashboardActivity.ARG_CLUB_NAME);
			// teamName =
			// getIntent().getStringExtra(DashboardActivity.ARG_TEAM_NAME);
			teamId = getIntent().getIntExtra(DashboardActivity.ARG_TEAM_ID, -1);
			// arguments.putString(DashboardActivity.ARG_CLUB_NAME, clubName);
			// arguments.putString(DashboardActivity.ARG_TEAM_NAME, teamName);
			arguments.putInt(DashboardActivity.ARG_TEAM_ID, teamId);
			TeamDetailFragment fragment = new TeamDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.team_details_container_id, fragment).commit();
		}
	}

	// /**
	// * {@inheritDoc}
	// */
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.actionbar_menu_edit, menu);
	// return true;
	// }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit:
			clubName = getIntent().getStringExtra(DashboardActivity.ARG_CLUB_NAME);
			teamName = getIntent().getStringExtra(DashboardActivity.ARG_TEAM_NAME);
			teamId = getIntent().getIntExtra(DashboardActivity.ARG_TEAM_ID, -1);
			Bundle arguments = new Bundle();
			arguments.putString(DashboardActivity.ARG_CLUB_NAME, clubName);
			arguments.putString(DashboardActivity.ARG_TEAM_NAME, teamName);
			arguments.putInt(DashboardActivity.ARG_TEAM_ID, teamId);
			TeamEditFragment fragment = new TeamEditFragment();
			fragment.setArguments(arguments);
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.team_details_container_id, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
			return true;
		case android.R.id.home:
			// Clicked on the navigate back button
			super.finish();
			return true;
		default:
			break;
		}
		CustomLog.d(this.getClass(), "clicked on: " + item);
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case TeamListActivity.RESULT_CODE_TEAM_CHANGED:
			break;
		case TeamListActivity.RESULT_CODE_TEAM_UNCHANGED:
			// do nothing
			break;
		default:
			CustomLog.e(this.getClass(), "Unkown result code: " + resultCode);
			break;
		}
		CustomLog.e(getClass(), "requestCode=" + requestCode + ", resultCode=" + resultCode);
	}
}
