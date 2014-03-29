package com.gunnarro.android.bandy.view.teamdetailflow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.statistic.Statistic;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link TeamListActivity} in two-pane mode (on tablets) or a
 * {@link TeamDetailActivity} on handsets.
 */
public class TeamDetailFragment extends Fragment {

	private BandyService bandyService;
	private int teamId;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TeamDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setHasOptionsMenu(true);
		if (getArguments().containsKey(DashboardActivity.ARG_TEAM_ID)) {
			teamId = getArguments().getInt(DashboardActivity.ARG_TEAM_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.team_details_layout, container, false);
		if (this.bandyService == null) {
			this.bandyService = new BandyServiceImpl(rootView.getContext());
		}
		Team team = this.bandyService.getTeam(teamId);
		getActivity().setTitle(team.getName());
		updateTeamDetails(rootView, team);
		// Statistic teamStatistic =
		// this.bandyService.getTeamStatistic(player.getTeam().getId(),
		// player.getId(), 1);
		setupEventHandlers(rootView);
		// updateTeamStatistic(rootView, playerStatistic);
		return rootView;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.actionbar_menu_edit, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	/**
	 * Note that the parent Activity’s onOptionsItemSelected() method is called
	 * first. Your fragment’s method is called only, when the Activity didn’t
	 * consume the event! {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		CustomLog.e(this.getClass(), item.toString());
		// handle item selection
		switch (item.getItemId()) {
		// This is consumed in the parent activity
		// case R.id.action_edit:
		// return true;
		case R.id.action_delete:
			delete(teamId);
			super.getActivity().onBackPressed();
		default:
			return false;
		}
	}

	private void delete(Integer teamId) {
		// this.bandyService.deleteTeam(teamId);
	}

	private void setupEventHandlers(View view) {
	}

	private void updateTeamDetails(View rootView, Team team) {
		if (team != null) {
			((TextView) rootView.findViewById(R.id.teamNameTxt)).setText(team.getName());
			((TextView) rootView.findViewById(R.id.teamYearOfBirthTxt)).setText("");
			((TextView) rootView.findViewById(R.id.teamGenderTxt)).setText("");

			if (team.getTeamLead() != null) {
				((TextView) rootView.findViewById(R.id.teamLeadTxt)).setText(team.getTeamLead().getFullName());
				((TextView) rootView.findViewById(R.id.teamleadMobileTxt)).setText(team.getTeamLead().getMobileNumber());
				((TextView) rootView.findViewById(R.id.teamleadEmailTxt)).setText(team.getTeamLead().getEmailAddress());
			}

			if (team.getCoach() != null) {
				((TextView) rootView.findViewById(R.id.coachTxt)).setText(team.getCoach().getFullName());
				((TextView) rootView.findViewById(R.id.coachMobileTxt)).setText(team.getCoach().getMobileNumber());
				((TextView) rootView.findViewById(R.id.coachEmailTxt)).setText(team.getCoach().getEmailAddress());
			}
		}
	}

	private void updateTeamStatistic(View rootView, Statistic statistic) {
		if (statistic != null) {
			((TextView) rootView.findViewById(R.id.teamTxt)).setText(statistic.getClubName() + ", " + statistic.getTeamName());
			((TextView) rootView.findViewById(R.id.matchesTxt)).setText("played=" + statistic.getNumberOfPlayerMatches() + ", total="
					+ statistic.getNumberOfTeamMatches());
			((TextView) rootView.findViewById(R.id.cupsTxt)).setText("played=" + statistic.getNumberOfPlayerCups() + ", total="
					+ statistic.getNumberOfTeamCups());
			((TextView) rootView.findViewById(R.id.trainingsTxt)).setText("participated=" + statistic.getNumberOfPlayerTrainings() + ",  total="
					+ statistic.getNumberOfTeamTrainings());
		}
	}

}
