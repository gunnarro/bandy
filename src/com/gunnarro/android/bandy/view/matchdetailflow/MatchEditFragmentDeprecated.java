package com.gunnarro.android.bandy.view.matchdetailflow;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.Toast;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.activity.MatchEvent;
import com.gunnarro.android.bandy.domain.party.Referee;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.utility.Utility;
import com.gunnarro.android.bandy.view.dashboard.CommonFragment;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;
import com.gunnarro.android.bandy.view.dashboard.ViewUtils;

public class MatchEditFragmentDeprecated extends CommonFragment {

	private BandyService bandyService;
	private Integer matchId;
	private Match match;

	private List<MatchEvent> matchHomeEventList = new ArrayList<MatchEvent>();
	private List<MatchEvent> matchAwayEventList = new ArrayList<MatchEvent>();
	/**
	 * Need this in order to prevent triggering the selection listeners upon gui
	 * initialization.
	 */
	private boolean isInitMode = true;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MatchEditFragmentDeprecated() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setHasOptionsMenu(true);
		if (getArguments().containsKey(DashboardActivity.ARG_TEAM_NAME)) {
			teamName = getArguments().getString(DashboardActivity.ARG_TEAM_NAME);
		}
		if (getArguments().containsKey(DashboardActivity.ARG_MATCH_ID)) {
			matchId = getArguments().getInt(DashboardActivity.ARG_MATCH_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.match_edit_layout_deprecated, container, false);
		if (this.bandyService == null) {
			this.bandyService = new BandyServiceImpl(rootView.getContext());
		}
		if (matchId != null) {
			match = this.bandyService.getMatch(matchId);
			init(rootView);
			getActivity().getActionBar().setSubtitle("Match online");
		}
		setupEventhandlers(rootView);
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
			Toast.makeText(getActivity().getApplicationContext(), "Saved match!", Toast.LENGTH_SHORT).show();
			super.getActivity().onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void init(View rootView) {
		if (match != null) {
			setInputValue(rootView, R.id.matchStartDateTxt, Utility.formatTime(match.getStartTime(), Utility.DATE_PATTERN));
			setInputValue(rootView, R.id.matchStartTimeTxt, Utility.formatTime(match.getStartTime(), Utility.TIME_PATTERN));
			setInputValue(rootView, R.id.matchHomeTeamTxt, match.getHomeTeam().getName());
			setInputValue(rootView, R.id.matchAwayTeamTxt, match.getAwayTeam().getName());
			setInputValue(rootView, R.id.matchGoalsHomeTxt, match.getNumberOfGoalsHome().toString());
			setInputValue(rootView, R.id.matchGoalsAwayTxt, match.getNumberOfGoalsAway().toString());
		}
	}

	private void setupEventhandlers(View rootView) {
	}

	private void updateMatchEventTable() {
//		TableLayout homeTable = (TableLayout) getView().findViewById(tableId);
//		TableLayout awayTable = (TableLayout) getView().findViewById(tableId);
//		List<MatchEvent> matchEventList = bandyService.getMatchEventList(matchId);
//		for (MatchEvent event : matchEventList) {
//			if (event.getTeamName().equals(match.getHomeTeam().getName())) {
//				homeTable.addView(createTableRow(event, homeTable.getChildCount()));
//			} else if (event.getTeamName().equals(match.getAwayTeam().getName())) {
//				awayTable.addView(createTableRow(event, awayTable.getChildCount()));
//			}
//		}
	}

	private TableRow createTableRow(MatchEvent event, int rowNumber) {
		TableRow row = new TableRow(getActivity().getApplicationContext());
		int rowBgColor = getResources().getColor(R.color.black);
		int txtColor = getResources().getColor(R.color.dark_green);
		row.addView(ViewUtils.createTextView(getActivity().getApplicationContext(), event.getInfo(), rowBgColor, txtColor, Gravity.LEFT));
		row.setPadding(1, 1, 1, 1);
		return row;
	}

	private void save() {
		String startDateStr = getInputValue(R.id.matchStartDateTxt, false);
		String startTimeStr = getInputValue(R.id.matchStartTimeTxt, false);
		// String goalsHome = getInputValue(R.id.matchGoalsHomeTxt);
		// String goalsAway = getInputValue(R.id.matchGoalsAwayTxt);
		if (!startDateStr.isEmpty() && !startTimeStr.isEmpty()) {
			long startTime = Utility.timeToDate(startDateStr + " " + startTimeStr, Utility.DATE_TIME_PATTERN).getTime();
			match.setStartTime(startTime);
		}
		// match.setNumberOfGoalsHome(Integer.parseInt(goalsHome));
		// match.setNumberOfGoalsAway(Integer.parseInt(goalsAway));
		Referee referee = new Referee("", "");
		match.setReferee(referee);
		this.bandyService.saveMatch(match);
	}

}
