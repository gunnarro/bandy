package com.gunnarro.android.bandy.view.matchdetailflow;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.activity.Season;
import com.gunnarro.android.bandy.domain.party.Referee;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.utility.Utility;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

public class NewMatchActivity extends FragmentActivity {

	private String selectedSeasonPeriod = "2013/2014";
	private String selectedTeamName = "Kn%tt 2003";
	private String selectedVenue = "Bergbanen";
	private String selectedHomeTeamName = "";
	private String selectedAwayTeamName = "";
	private String selectedRefereeName = "";
	private String selectedMatchTypeName = "";
	private BandyService bandyService;
	private Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.match_details_container_layout);
		setTitle("New Match");
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
			String teamName = getIntent().getStringExtra(DashboardActivity.ARG_TEAM_NAME);
			MatchEditFragment fragment = new MatchEditFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.match_details_container_id, fragment).commit();
		}
	}

	
//	/** Called when the activity is first created. */
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.match_new_layout);
//		this.setTitle(selectedTeamName);
//		// Show the Up button in the action bar.
//		getActionBar().setDisplayHomeAsUpEnabled(true);
//		
//		
//		this.getActionBar().setSubtitle("New match");
//		this.bandyService = new BandyServiceImpl(getApplicationContext());
//		setupEventHandlers();
//		bundle = getIntent().getExtras();
//		init();
//	}

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
				Toast.makeText(getApplicationContext(), "Created new match!", Toast.LENGTH_SHORT).show();
				returnToParentView();
			}
		});

	}

	private void returnToParentView() {
		Intent matchIntent = new Intent(getApplicationContext(), MatchListActivity.class);
		matchIntent.putExtra(DashboardActivity.ARG_TEAM_NAME, DashboardActivity.DEFAULT_TEAM_NAME);
		startActivity(matchIntent);
	}

	private void init() {
		Calendar startTime = Calendar.getInstance();
		startTime.setTimeInMillis(System.currentTimeMillis());
		startTime.set(Calendar.HOUR_OF_DAY, 16);
		startTime.set(Calendar.MILLISECOND, 0);
		startTime.set(Calendar.SECOND, 0);
		startTime.set(Calendar.MINUTE, 0);
		TextView dateTxtView = (TextView) findViewById(R.id.matchStartDateId);
		dateTxtView.setText(Utility.formatTime(System.currentTimeMillis(), "dd.MM.yyy"));
		TextView startTimeTxtView = (TextView) findViewById(R.id.matchStartTimeId);
		startTimeTxtView.setText(Utility.formatTime(startTime.getTimeInMillis(), "HH:mm"));
	}

	private void createMatch() {
		TextView startDateTxtView = (TextView) findViewById(R.id.matchStartDateId);
		String startDate = startDateTxtView.getText().toString();
		TextView startTimeTxtView = (TextView) findViewById(R.id.matchStartTimeId);
		Team homeTeam = bandyService.getTeam(selectedHomeTeamName, false);
		Team awayTeam = bandyService.getTeam(selectedAwayTeamName, false);
		Season season = bandyService.getSeason(selectedSeasonPeriod);
		Match match = new Match(null, season, Utility.timeToDate(startDate + " " + startTimeTxtView.getText().toString(), "dd.MM.yyyy hh:mm").getTime(),
				homeTeam, homeTeam, awayTeam, selectedVenue, new Referee(this.selectedRefereeName, this.selectedRefereeName), 1);

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
			venueeView.setText(team.getClub().getStadiumName());
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
