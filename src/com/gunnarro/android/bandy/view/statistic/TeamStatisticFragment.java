package com.gunnarro.android.bandy.view.statistic;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Season;
import com.gunnarro.android.bandy.domain.statistic.MatchStatistic;
import com.gunnarro.android.bandy.domain.statistic.MatchStatistic.MatchTypesEnum;
import com.gunnarro.android.bandy.domain.statistic.Statistic;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

public class TeamStatisticFragment extends Fragment {

	protected BandyService bandyService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		if (getArguments().containsKey(DashboardActivity.ARG_TEAM_NAME)) {
//			playerId = getArguments().getInt(PlayerDetailActivity.ARG_PLAYER_ID);
//		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.team_statistic_layout, container, false);
		this.bandyService = new BandyServiceImpl(getActivity());
		Team team = bandyService.getTeam(DashboardActivity.DEFAULT_TEAM_NAME, false);
		setupEventHandlers();
		updateData(view, team);
		return view;
	}

	private void setupEventHandlers() {
	}

	private void updateData(View view, Team team) {
		// Remove all rows before updating the table, except for the table
		// header rows.
		Season season = bandyService.getSeason("2013/2014");
		MatchStatistic summaryStatistic = new MatchStatistic(MatchTypesEnum.TOTAL.getCode());
		if (season != null && team != null) {
			Statistic teamStatistic = this.bandyService.getTeamStatistic(team.getId(), season.getId());
			// Date startDate = new Date(teamStatistic.getStartTime());
			// Date endDate = new Date(teamStatistic.getEndTime());
			for (MatchStatistic statistic : teamStatistic.getMatchStatisticList()) {
				updateTableData(view, statistic);
				summaryStatistic.incrementPlayed(statistic.getPlayed());
				summaryStatistic.incrementWon(statistic.getWon());
				summaryStatistic.incrementDraw(statistic.getDraw());
				summaryStatistic.incrementLoss(statistic.getLoss());
				summaryStatistic.incrementGoalsScored(statistic.getGoalsScored());
				summaryStatistic.incrementGoalsAgainst(statistic.getGoalsAgainst());
			}
			updateTableData(view, summaryStatistic);
		}
		// Utility.getDateFormatter().applyPattern("dd.MM.yyyy");
		// String periode = Utility.getDateFormatter().format(startDate) + " - "
		// + Utility.getDateFormatter().format(endDate);
		TextView tableHeaderTxt = (TextView) view.findViewById(R.id.teamStatisticTableHeaderPeriod);
		tableHeaderTxt.setText("Team statistic for season: " + (season != null ? season.getPeriod() : ""));
	}

	private void updateTableData(View view, MatchStatistic statistic) {
		switch (statistic.getMatchType()) {
		case CUP:
			setValue(view, R.id.cupPlayedId, statistic.getPlayed().toString());
			setValue(view, R.id.cupWonId, statistic.getWon().toString());
			setValue(view, R.id.cupDrawId, statistic.getDraw().toString());
			setValue(view, R.id.cupLossId, statistic.getLoss().toString());
			setValue(view, R.id.cupGoalsId, statistic.getGoals().toString());
			break;
		case LEAGUE:
			setValue(view, R.id.leaguePlayedId, statistic.getPlayed().toString());
			setValue(view, R.id.leagueWonId, statistic.getWon().toString());
			setValue(view, R.id.leagueDrawId, statistic.getDraw().toString());
			setValue(view, R.id.leagueLossId, statistic.getLoss().toString());
			setValue(view, R.id.leagueGoalsId, statistic.getGoals().toString());
			break;
		case TRAINING:
			setValue(view, R.id.trainingPlayedId, statistic.getPlayed().toString());
			setValue(view, R.id.trainingWonId, statistic.getWon().toString());
			setValue(view, R.id.trainingDrawId, statistic.getDraw().toString());
			setValue(view, R.id.trainingLossId, statistic.getLoss().toString());
			setValue(view, R.id.trainingGoalsId, statistic.getGoals().toString());
			break;
		case TOTAL:
			setValue(view, R.id.totalPlayedId, statistic.getPlayed().toString());
			setValue(view, R.id.totalWonId, statistic.getWon().toString());
			setValue(view, R.id.totalDrawId, statistic.getDraw().toString());
			setValue(view, R.id.totalLossId, statistic.getLoss().toString());
			setValue(view, R.id.totalGoalsId, statistic.getGoals().toString());
			break;
		default:

		}
	}

	private void setValue(View view, int viewId, String value) {
		TextView textView = (TextView) view.findViewById(viewId);
		textView.setText(value);
	}
}
