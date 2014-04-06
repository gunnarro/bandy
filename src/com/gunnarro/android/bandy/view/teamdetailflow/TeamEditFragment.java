package com.gunnarro.android.bandy.view.teamdetailflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.domain.League;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.view.dashboard.CommonFragment;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

public class TeamEditFragment extends CommonFragment {

	private BandyService bandyService;
	private Integer teamId;
	private String selectedLeagueName;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TeamEditFragment() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setHasOptionsMenu(true);
		if (getArguments().containsKey(DashboardActivity.ARG_TEAM_NAME)) {
			teamName = getArguments().getString(DashboardActivity.ARG_TEAM_NAME);
		}
		if (getArguments().containsKey(DashboardActivity.ARG_TEAM_ID)) {
			teamId = getArguments().getInt(DashboardActivity.ARG_TEAM_ID);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.team_new_layout, container, false);
		if (this.bandyService == null) {
			this.bandyService = new BandyServiceImpl(rootView.getContext());
		}

		if (teamId != null) {
			Team team = this.bandyService.getTeam(teamId);
			init(rootView, team);
			getActivity().getActionBar().setSubtitle(team.getName());
		}
		setupEventHandlers(rootView);
		return rootView;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.actionbar_menu_save, menu);
		super.onCreateOptionsMenu(menu, inflater);
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
			super.getActivity().onBackPressed();
			return true;
		case R.id.action_save:
			save();
			super.getActivity().onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setupEventHandlers(View rootView) {
		// league spinner
		String[] leagueNames = this.bandyService.getLeagueNames();
		ArrayAdapter<CharSequence> leagueAdapter = new ArrayAdapter<CharSequence>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item,
				leagueNames);
		Spinner leagueSpinner = (Spinner) rootView.findViewById(R.id.teamLeagueSpinner);
		leagueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		leagueSpinner.setAdapter(leagueAdapter);
		leagueSpinner.setOnItemSelectedListener(new LeagueOnItemSelectedListener());
	}

	private void init(View rootView, Team team) {
		if (team != null) {
			setInputValue(rootView, R.id.teamNameTxt, team.getName());
			// setInputValue(rootView, R.id.teamGenderTxt, team.getGender());
			// setInputValue(rootView, R.id.teamYearOfBirthTxt,
			// team.getTeamYearOfBirth());
		}
	}

	private void save() {
		String teamName = getInputValue(R.id.teamNameTxt);
		String teamYearOfBirth = getInputValue(R.id.teamYearOfBirthTxt);
		Club club = bandyService.getClub("Ulle%", "Bandy");
		if (club == null) {
			throw new RuntimeException("Club is not found!");
		}
		Team team = new Team(teamName, club, Integer.parseInt(teamYearOfBirth), getSelectedGender());
		if (teamId != null && teamId.intValue() > 0) {
			team = new Team(teamId, teamName, club, Integer.parseInt(teamYearOfBirth), getSelectedGender());
		}
		League league = bandyService.getLeague(selectedLeagueName);
		team.setLeague(league);
		int id = bandyService.saveTeam(team);
		CustomLog.e(this.getClass(), team.toString());
	}

	/**
	 * 
	 * @author gunnarro
	 * 
	 */
	public class LeagueOnItemSelectedListener implements OnItemSelectedListener {

		public LeagueOnItemSelectedListener() {
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			selectedLeagueName = parent.getItemAtPosition(pos).toString();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.
		}
	}

}
