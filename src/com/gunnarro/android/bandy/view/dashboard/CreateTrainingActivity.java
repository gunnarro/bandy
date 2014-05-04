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
import com.gunnarro.android.bandy.domain.activity.Season;
import com.gunnarro.android.bandy.domain.activity.Training;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.utility.Utility;

public class CreateTrainingActivity extends Activity {

	private String selectedClubName = "Ullev%l";
	private String selectedSeasonPeriod = "2013/2014";
	private String selectedTeamName = "Kn%tt 2003";
	private String selectedVenue = "Bergbanen";
	private BandyService bandyService;
	private Bundle bundle;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.training_create_layout);
		// selectedTeamName =
		// getIntent().getStringExtra(DashboardActivity.ARG_TEAM_NAME);
		this.setTitle(ActivityTypesEnum.Training.name() + " " + selectedTeamName);
		this.getActionBar().setSubtitle("New training");
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
		// Season spinner
		String[] seasonPeriods = bandyService.getSeasonPeriodes();
		Spinner seasonSpinner = (Spinner) findViewById(R.id.activity_season_spinner);
		ArrayAdapter<CharSequence> seasonAdapter = new ArrayAdapter<CharSequence>(getApplicationContext(), android.R.layout.simple_spinner_item, seasonPeriods);
		seasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		seasonSpinner.setAdapter(seasonAdapter);
		seasonSpinner.setOnItemSelectedListener(new SeasonOnItemSelectedListener());

		// Club spinner
		String[] clubNames = this.bandyService.getTeamNames("%");
		Spinner clubsSpinner = (Spinner) findViewById(R.id.club_spinner);
		ArrayAdapter<CharSequence> clubsAdapter = new ArrayAdapter<CharSequence>(getApplicationContext(), android.R.layout.simple_spinner_item, clubNames);
		clubsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		clubsSpinner.setAdapter(clubsAdapter);
		clubsSpinner.setOnItemSelectedListener(new ClubOnItemSelectedListener());

		// Team spinner
		String[] teamNames = this.bandyService.getTeamNames("%");
		Spinner teamsSpinner = (Spinner) findViewById(R.id.team_spinner);
		ArrayAdapter<CharSequence> teamsAdapter = new ArrayAdapter<CharSequence>(getApplicationContext(), android.R.layout.simple_spinner_item, teamNames);
		teamsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		teamsSpinner.setAdapter(teamsAdapter);
		teamsSpinner.setOnItemSelectedListener(new TeamOnItemSelectedListener());

		// Venue spinner
		String[] venueNames = new String[] { "Bergbanen", "NIH" };
		Spinner venueSpinner = (Spinner) findViewById(R.id.venue_spinner);
		ArrayAdapter<CharSequence> venueAdapter = new ArrayAdapter<CharSequence>(getApplicationContext(), android.R.layout.simple_spinner_item, venueNames);
		venueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		venueSpinner.setAdapter(venueAdapter);
		venueSpinner.setOnItemSelectedListener(new VenueOnItemSelectedListener());

		findViewById(R.id.cancel_create_activity_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				returnToParentView();
			}
		});

		findViewById(R.id.create_activity_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createTraining();
				returnToParentView();
			}
		});

	}

	private void returnToParentView() {
		Intent trainingsIntent = new Intent(getApplicationContext(), TrainingsActivity.class);
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
		long endTime = startTime.getTimeInMillis() + 2 * 60 * 60 * 1000;
		TextView dateTxtView = (TextView) findViewById(R.id.trainingDateId);
		dateTxtView.setText(Utility.formatTime(System.currentTimeMillis(), "dd.MM.yyy"));
		TextView fromTimeTxtView = (TextView) findViewById(R.id.trainingFromTimeId);
		fromTimeTxtView.setText(Utility.formatTime(startTime.getTimeInMillis(), "HH:mm"));
		TextView toTimeTxtView = (TextView) findViewById(R.id.trainingToTimeId);
		toTimeTxtView.setText(Utility.formatTime(endTime, "HH:mm"));
	}

	private void createTraining() {
		TextView dateTxtView = (TextView) findViewById(R.id.trainingDateId);
		String date = dateTxtView.getText().toString();
		TextView fromTimeTxtView = (TextView) findViewById(R.id.trainingFromTimeId);
		TextView toTimeTxtView = (TextView) findViewById(R.id.trainingToTimeId);
		Team team = bandyService.getTeam(selectedTeamName, false);
		Season season = bandyService.getSeason(selectedSeasonPeriod);
		Training training = new Training(season, Utility.timeToDate(date + " " + fromTimeTxtView.getText().toString(), "dd.MM.yyyy hh:mm").getTime(), Utility
				.timeToDate(date + " " + toTimeTxtView.getText().toString(), "dd.MM.yyyy hh:mm").getTime(), team, this.selectedVenue);
		int trainingId = bandyService.createTraining(training);
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
	public class ClubOnItemSelectedListener implements OnItemSelectedListener {

		public ClubOnItemSelectedListener() {
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			selectedClubName = parent.getItemAtPosition(pos).toString();
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
	public class TeamOnItemSelectedListener implements OnItemSelectedListener {

		public TeamOnItemSelectedListener() {
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			selectedTeamName = parent.getItemAtPosition(pos).toString();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.
		}
	}

	/**
	 * Venue spinner listener
	 * 
	 * @author gunnarro
	 * 
	 */
	public class VenueOnItemSelectedListener implements OnItemSelectedListener {

		public VenueOnItemSelectedListener() {
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			selectedVenue = parent.getItemAtPosition(pos).toString();
			// update the table upon item selection
			// updateActivitesData();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.
		}
	}
}
