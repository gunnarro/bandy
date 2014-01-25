package com.gunnarro.android.bandy.view.teamdetailflow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.domain.Statistic;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.view.list.Item;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link PlayerListActivity} in two-pane mode (on tablets) or a
 * {@link PlayerDetailActivity} on handsets.
 */
public class TeamDetailFragment extends Fragment {

	public static final String ARG_TEAM_ID = "team_id";

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
		if (getArguments().containsKey(ARG_TEAM_ID)) {
			teamId = getArguments().getInt(ARG_TEAM_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.team_details_layout, container, false);
		if (this.bandyService == null) {
			this.bandyService = new BandyServiceImpl(rootView.getContext());
		}
		Team team = this.bandyService.getTeam(teamId);
		updateTeamDetails(rootView, team);
		Statistic teamStatistic = bandyService.getTeamStatistic(team.getId());
		updateTeamStatistic(rootView, teamStatistic);
		return rootView;
	}

	private void updateTeamDetails(View rootView, Team team) {
		if (team != null) {
			((TextView) rootView.findViewById(R.id.nameTxt)).setText(team.getName());
			((TextView) rootView.findViewById(R.id.teamleadTxt)).setText(team.getTeamLead().getFullName());
			((TextView) rootView.findViewById(R.id.coachTxt)).setText(team.getCoach().getFullName());

			ListView parentListView = (ListView) rootView.findViewById(R.id.team_player_list);
			ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(getActivity(), R.layout.custom_simple_list_item, team.getPlayerItemList());
			parentListView.setAdapter(adapter);
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
