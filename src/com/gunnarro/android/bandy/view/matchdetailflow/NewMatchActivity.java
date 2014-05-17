package com.gunnarro.android.bandy.view.matchdetailflow;

import java.util.Calendar;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.activity.Match.MatchTypesEnum;
import com.gunnarro.android.bandy.domain.activity.Season;
import com.gunnarro.android.bandy.domain.party.Referee;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.utility.Utility;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;
import com.gunnarro.android.bandy.view.dialog.DialogSelection.NoticeDialogListener;
import com.gunnarro.android.bandy.view.dialog.ItemSelection;
import com.gunnarro.android.bandy.view.dialog.SelectDialogOnClickListener;

public class NewMatchActivity extends FragmentActivity implements NoticeDialogListener {

	private String selectedSeasonPeriod = "2013/2014";
	private String teamName;
	private String selectedVenue = "Bergbanen";
	private String selectedHomeTeamName = "";
	private String selectedAwayTeamName = "";
	private String selectedRefereeName = "";
	private String selectedMatchTypeName = "";
	private BandyService bandyService;

	// The dialog fragment receives a reference to this Activity through the
	// Fragment.onAttach() callback, which it uses to call the following methods
	// defined by the NoticeDialogFragment.NoticeDialogListener interface
	@Override
	public void onDialogPositiveClick(ItemSelection dialog) {
		// User touched the dialog's positive button
		setInputValue(dialog.getInputFieldId(), dialog.getSelectedItem());
	}

	@Override
	public void onDialogNegativeClick(ItemSelection dialog) {
		// User touched the dialog's negative button
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.match_new_layout);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (this.bandyService == null) {
			this.bandyService = new BandyServiceImpl(getApplicationContext());
		}
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
			// Bundle arguments = new Bundle();
			String clubName = getIntent().getStringExtra(DashboardActivity.ARG_CLUB_NAME);
			teamName = getIntent().getStringExtra(DashboardActivity.ARG_TEAM_NAME);
			this.setTitle(clubName);
			this.getActionBar().setSubtitle(teamName);
			// arguments.putString(DashboardActivity.ARG_CLUB_NAME, clubName);
			// arguments.putString(DashboardActivity.ARG_TEAM_NAME, teamName);
			// MatchEditFragment fragment = new MatchEditFragment();
			// fragment.setArguments(arguments);
			// getSupportFragmentManager().beginTransaction().add(R.id.match_details_container_id,
			// fragment).commit();
		}
		init();
		setupEventHandlers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar_menu_save, menu);
		return true;
	}

	/**
	 * Note that the parent Activity’s onOptionsItemSelected() method is called
	 * first. Your fragment’s method is called only, when the Activity didn’t
	 * consume the event! {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		CustomLog.e(this.getClass(), item.toString());
		switch (item.getItemId()) {
		case R.id.action_cancel:
			super.onBackPressed();
			return true;
		case R.id.action_save:
			save();
			Toast.makeText(getApplicationContext(), "Saved Match!", Toast.LENGTH_SHORT).show();
			super.onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPause() {
		super.onPause();
	}

	private void setInputValue(int id, String value) {
		EditText inputView = (EditText) findViewById(id);
		if (inputView != null && value != null) {
			inputView.setText(value.trim());
		} else {
			CustomLog.e(this.getClass(), "No input field found or value is equal to null, for id: " + id + ", value: " + value);
		}
	}

	protected String getInputValue(int id) {
		EditText inputView = (EditText) findViewById(id);
		if (inputView != null) {
			return inputView.getText().toString().trim();
		} else {
			CustomLog.e(this.getClass(), "No input field found for id: " + id);
		}
		return null;
	}

	private void setupEventHandlers() {
		// Match type
		String[] matchTypeNames = bandyService.getMatchTypes();
		ImageButton typeBtn = (ImageButton) findViewById(R.id.matchSelectTypeBtnId);
		typeBtn.setOnClickListener(new SelectDialogOnClickListener(getSupportFragmentManager(), matchTypeNames, R.id.matchTypeTxt, false));

		String[] teamNames = this.bandyService.getTeamNames("%");
		// Home Team
		ImageButton homeBtn = (ImageButton) findViewById(R.id.matchSelectHomeTeamBtnId);
		homeBtn.setOnClickListener(new SelectDialogOnClickListener(getSupportFragmentManager(), teamNames, R.id.matchHomeTeamTxt, false));
		// Away team
		ImageButton awayBtn = (ImageButton) findViewById(R.id.matchSelectAwayTeamBtnId);
		awayBtn.setOnClickListener(new SelectDialogOnClickListener(getSupportFragmentManager(), teamNames, R.id.matchAwayTeamTxt, false));
		// Venue
		String[] venueNames = new String[] { "Bergbanen", "Voldsløkka" };
		ImageButton venueBtn = (ImageButton) findViewById(R.id.matchSelectVenueBtnId);
		venueBtn.setOnClickListener(new SelectDialogOnClickListener(getSupportFragmentManager(), venueNames, R.id.matchVenueTxt, false));
		// Referee spinner
		String[] refereeNames = this.bandyService.getRefereeNames();
		ImageButton refereeBtn = (ImageButton) findViewById(R.id.matchSelectRefereeBtnId);
		refereeBtn.setOnClickListener(new SelectDialogOnClickListener(getSupportFragmentManager(), refereeNames, R.id.matchRefereeNameTxt, false));
	}

	private void init() {
		Calendar startTime = Calendar.getInstance();
		startTime.setTimeInMillis(System.currentTimeMillis());
		startTime.set(Calendar.HOUR_OF_DAY, 16);
		startTime.set(Calendar.MILLISECOND, 0);
		startTime.set(Calendar.SECOND, 0);
		startTime.set(Calendar.MINUTE, 0);
		TextView dateTxtView = (TextView) findViewById(R.id.matchStartDateTxt);
		dateTxtView.setText(Utility.formatTime(System.currentTimeMillis(), "dd.MM.yyy"));
		TextView startTimeTxtView = (TextView) findViewById(R.id.matchStartTimeTxt);
		startTimeTxtView.setText(Utility.formatTime(startTime.getTimeInMillis(), "HH:mm"));
	}

	private void save() {
		String startDate = getInputValue(R.id.matchStartDateTxt);
		String startTime = getInputValue(R.id.matchStartTimeTxt);
		Team team = bandyService.getTeam(teamName, false);
		Team homeTeam = bandyService.getTeam(getInputValue(R.id.matchHomeTeamTxt), false);
		Team awayTeam = bandyService.getTeam(getInputValue(R.id.matchAwayTeamTxt), false);
		Season season = bandyService.getSeason(selectedSeasonPeriod);
		MatchTypesEnum matchType = MatchTypesEnum.valueOf(getInputValue(R.id.matchTypeTxt));
		Referee referee = null;
		if (getInputValue(R.id.matchRefereeNameTxt) != null) {
//			referee = new Referee(getInputValue(R.id.matchRefereeNameTxt), getInputValue(R.id.matchRefereeNameTxt));
		}
		Match match = new Match(null, null, Utility.timeToDate(startDate + " " + startTime, "dd.MM.yyyy hh:mm").getTime(), team, homeTeam, awayTeam,
				getInputValue(R.id.matchVenueTxt), referee, matchType);

		int matchId = bandyService.saveMatch(match);
		CustomLog.d(this.getClass(), "created new match with id : " + matchId);
	}
}
