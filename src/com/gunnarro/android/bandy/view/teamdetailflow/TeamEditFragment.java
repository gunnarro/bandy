package com.gunnarro.android.bandy.view.teamdetailflow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

public class TeamEditFragment extends Fragment {

	private BandyService bandyService;
	private String teamName;
	private Integer teamId;

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
		String teamGender = getInputValue(R.id.teamGenderTxt);
		Club club = bandyService.getClub(1);
		Team team = new Team(teamName, club, Integer.parseInt(teamYearOfBirth), teamGender);
		if (teamId > 0) {
			team = new Team(teamId, teamName, club, Integer.parseInt(teamYearOfBirth), teamGender);
		}
		int id = bandyService.saveTeam(team);
		CustomLog.e(this.getClass(), team.toString());
	}

	private String getInputValue(int id) {
		EditText inputView = (EditText) getView().findViewById(id);
		if (inputView != null) {
			return inputView.getText().toString();
		} else {
			CustomLog.e(this.getClass(), "No input field found for id: " + id);
		}
		return null;
	}

	private void setInputValue(View rootView, int id, String value) {
		EditText inputView = (EditText) rootView.findViewById(id);
		if (inputView != null) {
			inputView.setText(value);
		} else {
			CustomLog.e(this.getClass(), "No input field found for id: " + id + ", value: " + value);
		}
	}

}
