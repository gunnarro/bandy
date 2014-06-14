package com.gunnarro.android.bandy.view.dashboard;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.view.list.Item;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl.SelectionListType;
import com.gunnarro.android.bandy.view.dialog.DialogSelection.NoticeDialogListener;
import com.gunnarro.android.bandy.view.dialog.ItemSelection;
import com.gunnarro.android.bandy.view.dialog.SelectDialogOnClickListener;

//import com.google.inject.Inject;
/**
 * Application staring point view
 * 
 */
public class HomeActivity extends DashboardActivity implements NoticeDialogListener {

//	@Inject
	private BandyService bandyService;

	// **********************************************************************
	// NoticeDialogListener Methods
	// **********************************************************************
	// The dialog fragment receives a reference to this Activity through the
	// Fragment.onAttach() callback, which it uses to call the following methods
	// defined by the NoticeDialogFragment.NoticeDialogListener interface
	@Override
	public void onDialogPositiveClick(ItemSelection dialog) {
		// User touched the dialog's positive button
		CustomLog.e(this.getClass(), "selected: " + dialog.getSelectedItem());
		setInputValue(dialog.getInputFieldId(), dialog.getSelectedItem());
	}

	@Override
	public void onDialogNegativeClick(ItemSelection dialog) {
		// User touched the dialog's negative button
	}

	// **********************************************************************

	/**
	 * onCreate - called when the activity is first created. Called when the
	 * activity is first created. This is where you should do all of your normal
	 * static set up: create views, bind data to lists, etc. This method also
	 * provides you with a Bundle containing the activity's previously frozen
	 * state, if there was one.
	 * 
	 * Always followed by onStart().
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_layout);
		super.getActionBar().hide();
		// this.bandyService = new BandyServiceImpl(getApplicationContext());
		init();
		setupEventhandlers();
	}

//	public void setBandyService(BandyService bandyService) {
//		this.bandyService = bandyService;
//	}

	/**
	 * onDestroy The final call you receive before your activity is destroyed.
	 * This can happen either because the activity is finishing (someone called
	 * finish() on it, or because the system is temporarily destroying this
	 * instance of the activity to save space. You can distinguish between these
	 * two scenarios with the isFinishing() method.
	 * 
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * onPause Called when the system is about to start resuming a previous
	 * activity. This is typically used to commit unsaved changes to persistent
	 * data, stop animations and other things that may be consuming CPU, etc.
	 * Implementations of this method must be very quick because the next
	 * activity will not be resumed until this method returns. Followed by
	 * either onResume() if the activity returns back to the front, or onStop()
	 * if it becomes invisible to the user.
	 * 
	 */
	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * onRestart Called after your activity has been stopped, prior to it being
	 * started again. Always followed by onStart().
	 * 
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
	}

	/**
	 * onResume Called when the activity will start interacting with the user.
	 * At this point your activity is at the top of the activity stack, with
	 * user input going to it. Always followed by onPause().
	 * 
	 */
	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * onStart Called when the activity is becoming visible to the user.
	 * Followed by onResume() if the activity comes to the foreground, or
	 * onStop() if it becomes hidden.
	 * 
	 */
	@Override
	protected void onStart() {
		super.onStart();
	}

	/**
	 * onStop Called when the activity is no longer visible to the user because
	 * another activity has been resumed and is covering this one. This may
	 * happen either because a new activity is being started, an existing one is
	 * being brought in front of this one, or this one is being destroyed.
	 * 
	 * Followed by either onRestart() if this activity is coming back to
	 * interact with the user, or onDestroy() if this activity is going away.
	 */
	@Override
	protected void onStop() {
		super.onStop();
	}

	private void setupEventhandlers() {
		ImageButton clubBtn = (ImageButton) findViewById(R.id.selectClubBtnId);
		SelectDialogOnClickListener.turnOnInitMode();
		clubBtn.setOnClickListener(new SelectDialogOnClickListener(getSupportFragmentManager(), getBandyService(), SelectionListType.CLUB_NAMES, -1,
				R.id.clubNameTxt, false));

		ImageButton teamBtn = (ImageButton) findViewById(R.id.selectTeamBtnId);
		teamBtn.setOnClickListener(new SelectDialogOnClickListener(getSupportFragmentManager(), getBandyService(), SelectionListType.TEAM_NAMES, -1,
				R.id.teamNameTxt, false));

		ImageButton seasonBtn = (ImageButton) findViewById(R.id.selectSeasonBtnId);
		seasonBtn.setOnClickListener(new SelectDialogOnClickListener(getSupportFragmentManager(), getBandyService(), SelectionListType.SEASONS_TYPES, -1,
				R.id.seasonPeriodTxt, false));

		SelectDialogOnClickListener.turnOffInitMode();
	}

	private void init() {
		// setInputValue(R.id.clubNameTxt, new Item(1, null));
		// setInputValue(R.id.teamNameTxt, new Item(1, null));
		// setInputValue(R.id.seasonPeriodTxt,
		// bandyService.getCurrentSeason().getPeriod());
	}

	private BandyService getBandyService() {
		if (this.bandyService == null) {
			this.bandyService = new BandyServiceImpl(getApplicationContext());
		}
		return bandyService;
	}

	private void setInputValue(int id, Item item) {
		if (item != null) {
			// Update view
			((EditText) findViewById(id)).setText(item.getValue());
			// then update class variables
			if (id == R.id.clubNameTxt) {
				super.setClubName(item.getValue());
				super.setSelectedClubId(item.getId());
			} else if (id == R.id.teamNameTxt) {
				super.setTeamName(item.getValue());
				super.setSelectedTeamId(item.getId());
			} else if (id == R.id.seasonPeriodTxt) {
				super.setSeasonPeriode(item.getValue());
			}
		}
	}

	/**
 */
	// Click Methods

	/**
 */
	// More Methods

} // end class
