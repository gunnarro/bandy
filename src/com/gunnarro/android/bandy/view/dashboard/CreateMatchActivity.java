package com.gunnarro.android.bandy.view.dashboard;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Activity.ActivityTypesEnum;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.activity.Season;
import com.gunnarro.android.bandy.domain.party.Referee;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.utility.Utility;
import com.gunnarro.android.bandy.view.matchdetailflow.MatchListActivity;

public class CreateMatchActivity extends Activity {

	private String selectedSeasonPeriod = "2013/2014";
	private String selectedTeamName = "Kn%tt 2003";
	private String selectedVenue = "Bergbanen";
	private String selectedHomeTeamName = "";
	private String selectedAwayTeamName = "";
	private String selectedRefereeName = "";
	private String selectedMatchTypeName = "";
	private BandyService bandyService;
	private Bundle bundle;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.match_create_layout);
		// selectedTeamName =
		// getIntent().getStringExtra(DashboardActivity.ARG_TEAM_NAME);
		this.setTitle(ActivityTypesEnum.Training.name() + " " + selectedTeamName);
		this.getActionBar().setSubtitle("New match");
		this.bandyService = new BandyServiceImpl(getApplicationContext());
		setupEventHandlers();
		bundle = getIntent().getExtras();
		init();
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

	private void setupEventHandlers() {
		// Match type spinner
		String[] matchTypeNames = bandyService.getMatchTypes();
		Spinner matchTypeSpinner = (Spinner) findViewById(R.id.matchTypeSpinnerId);
		ArrayAdapter<CharSequence> matchTypeAdapter = new ArrayAdapter<CharSequence>(getApplicationContext(), android.R.layout.simple_spinner_item,
				matchTypeNames);
		matchTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		matchTypeSpinner.setAdapter(matchTypeAdapter);
		matchTypeSpinner.setOnItemSelectedListener(new MatchTypeOnItemSelectedListener());

		// Home Team spinner
		String[] homeTeamNames = this.bandyService.getTeamNames("%");
		Spinner homeTeamsSpinner = (Spinner) findViewById(R.id.matchHomeTeamSpinnerId);
		ArrayAdapter<CharSequence> homeTeamsAdapter = new ArrayAdapter<CharSequence>(getApplicationContext(), android.R.layout.simple_spinner_item,
				homeTeamNames);
		homeTeamsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		homeTeamsSpinner.setAdapter(homeTeamsAdapter);
		homeTeamsSpinner.setOnItemSelectedListener(new HomeTeamOnItemSelectedListener());

		// Away Team spinner
		String[] awayTeamNames = this.bandyService.getTeamNames("%");
		Spinner awayTeamsSpinner = (Spinner) findViewById(R.id.matchAwayTeamSpinnerId);
		ArrayAdapter<CharSequence> awayTeamsAdapter = new ArrayAdapter<CharSequence>(getApplicationContext(), android.R.layout.simple_spinner_item,
				awayTeamNames);
		awayTeamsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		awayTeamsSpinner.setAdapter(awayTeamsAdapter);
		awayTeamsSpinner.setOnItemSelectedListener(new AwayTeamOnItemSelectedListener());

		// Referee spinner
		String[] refereeNames = this.bandyService.getRefereeNames();
		Spinner refereeSpinner = (Spinner) findViewById(R.id.matchRefereeSpinnerId);
		ArrayAdapter<CharSequence> refereeAdapter = new ArrayAdapter<CharSequence>(getApplicationContext(), android.R.layout.simple_spinner_item, refereeNames);
		refereeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		refereeSpinner.setAdapter(refereeAdapter);
		refereeSpinner.setOnItemSelectedListener(new RefereeOnItemSelectedListener());

		findViewById(R.id.cancel_create_activity_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				returnToParentView();
			}
		});

		findViewById(R.id.create_activity_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createMatch();

			}
		});

	}

	private void returnToParentView() {
		Intent trainingsIntent = new Intent(getApplicationContext(), MatchListActivity.class);
		trainingsIntent.putExtra(DashboardActivity.ARG_TEAM_NAME, DashboardActivity.DEFAULT_TEAM_NAME);
		startActivity(trainingsIntent);
	}

	private void init() {
		Calendar startTime = Calendar.getInstance();
		startTime.setTimeInMillis(System.currentTimeMillis());
		startTime.set(Calendar.HOUR_OF_DAY, 16);
		startTime.set(Calendar.MILLISECOND, 0);
		startTime.set(Calendar.SECOND, 0);
		startTime.set(Calendar.MINUTE, 0);
		TextView dateTxtView = (TextView) findViewById(R.id.matchDateId);
		dateTxtView.setText(Utility.formatTime(System.currentTimeMillis(), "dd.MM.yyy"));
		TextView startTimeTxtView = (TextView) findViewById(R.id.matchStartTimeId);
		startTimeTxtView.setText(Utility.formatTime(startTime.getTimeInMillis(), "HH:mm"));
	}

	private void createMatch() {
		TextView dateTxtView = (TextView) findViewById(R.id.matchDateId);
		String date = dateTxtView.getText().toString();
		TextView matchDateTxtView = (TextView) findViewById(R.id.trainingFromTimeId);
		Team homeTeam = bandyService.getTeam(selectedHomeTeamName, false);
		Team awayTeam = bandyService.getTeam(selectedAwayTeamName, false);
		Season season = bandyService.getSeason(selectedSeasonPeriod);
		Match match = new Match(null, season, Utility.timeToDate(date + " " + matchDateTxtView.getText().toString(), "dd.MM.yyyy hh:mm").getTime(), homeTeam,
				homeTeam, awayTeam, selectedVenue, new Referee(this.selectedRefereeName, this.selectedRefereeName), 1);

		int matchId = bandyService.createMatch(match);
	}

	/**
	 * Season spinner listener
	 * 
	 * @author gunnarro
	 * 
	 */
	public class SeasonOnItemSelectedListener implements OnItemSelectedListener {

		public SeasonOnItemSelectedListener() {
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			selectedSeasonPeriod = parent.getItemAtPosition(pos).toString();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.
		}
	}

	/**
	 * Team spinner listener
	 * 
	 * @author gunnarro
	 * 
	 */
	public class MatchTypeOnItemSelectedListener implements OnItemSelectedListener {

		public MatchTypeOnItemSelectedListener() {
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			selectedMatchTypeName = parent.getItemAtPosition(pos).toString();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.
		}
	}

	/**
	 * Team spinner listener
	 * 
	 * @author gunnarro
	 * 
	 */
	public class RefereeOnItemSelectedListener implements OnItemSelectedListener {

		public RefereeOnItemSelectedListener() {
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			selectedRefereeName = parent.getItemAtPosition(pos).toString();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.
		}
	}

	/**
	 * Team spinner listener
	 * 
	 * @author gunnarro
	 * 
	 */
	public class HomeTeamOnItemSelectedListener implements OnItemSelectedListener {

		public HomeTeamOnItemSelectedListener() {
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			selectedHomeTeamName = parent.getItemAtPosition(pos).toString();
			Team team = bandyService.getTeam(selectedHomeTeamName, false);
			TextView venueeView = (TextView) findViewById(R.id.venueTxtId);
			venueeView.setText(team.getClub().getStadium());
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.
		}
	}

	/**
	 * Team spinner listener
	 * 
	 * @author gunnarro
	 * 
	 */
	public class AwayTeamOnItemSelectedListener implements OnItemSelectedListener {

		public AwayTeamOnItemSelectedListener() {
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			selectedAwayTeamName = parent.getItemAtPosition(pos).toString();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.
		}
	}
}
