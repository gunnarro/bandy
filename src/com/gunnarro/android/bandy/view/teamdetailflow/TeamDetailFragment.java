package com.gunnarro.android.bandy.view.teamdetailflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.statistic.Statistic;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.view.dashboard.CommonFragment;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link TeamListActivity} in two-pane mode (on tablets) or a
 * {@link TeamDetailActivity} on handsets.
 */
public class TeamDetailFragment extends CommonFragment {

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
		setupEventHandlers(rootView);
		Statistic teamStatistic = this.bandyService.getTeamStatistic(team.getId(), 1);
		updateTeamStatistic(rootView, teamStatistic);
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
			Toast.makeText(getActivity().getApplicationContext(), "Deleted team!", Toast.LENGTH_SHORT).show();
			super.getActivity().onBackPressed();
		default:
			return false;
		}
	}

	private void delete(Integer teamId) {
		this.bandyService.deleteTeam(teamId);
	}

	private void setupEventHandlers(View view) {
	}

	private void updateTeamDetails(View rootView, Team team) {
		if (team != null) {
			setTextViewValue(rootView, R.id.teamNameTxt, team.getName());
			setTextViewValue(rootView, R.id.teamYearOfBirthTxt, team.getTeamYearOfBirth().toString());
			setTextViewValue(rootView, R.id.teamGenderTxt, team.getGender());
			if (team.getLeague() != null) {
				setTextViewValue(rootView, R.id.teamleagueNameTxt, team.getLeague().getName());
			}
			if (team.getTeamLead() != null) {
				setTextViewValue(rootView, R.id.teamLeadTxt, team.getTeamLead().getFullName());
				setTextViewValue(rootView, R.id.teamleadMobileTxt, team.getTeamLead().getMobileNumber());
				setTextViewValue(rootView, R.id.teamleadEmailTxt, team.getTeamLead().getEmailAddress());
			}

			if (team.getCoach() != null) {
				setTextViewValue(rootView, R.id.coachTxt, team.getCoach().getFullName());
				setTextViewValue(rootView, R.id.coachMobileTxt, team.getCoach().getMobileNumber());
				setTextViewValue(rootView, R.id.coachEmailTxt, team.getCoach().getEmailAddress());
			}
		}
	}

	private void updateTeamStatistic(View rootView, Statistic statistic) {
		if (statistic != null) {
			((TextView) rootView.findViewById(R.id.matchesTxt)).setText("played=" + statistic.getNumberOfPlayerMatches() + ", total="
					+ statistic.getNumberOfTeamMatches());
			((TextView) rootView.findViewById(R.id.cupsTxt)).setText("played=" + statistic.getNumberOfPlayerCups() + ", total="
					+ statistic.getNumberOfTeamCups());
			((TextView) rootView.findViewById(R.id.trainingsTxt)).setText("participated=" + statistic.getNumberOfPlayerTrainings() + ",  total="
					+ statistic.getNumberOfTeamTrainings());
		}
	}

}
