package com.gunnarro.android.bandy.view.matchdetailflow;

import java.util.Calendar;
import java.util.List;

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
import android.widget.TableLayout;
import android.widget.Toast;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.activity.Match.MatchStatus;
import com.gunnarro.android.bandy.domain.activity.MatchEvent;
import com.gunnarro.android.bandy.domain.activity.MatchEvent.MatchEventTypesEnum;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.utility.Utility;
import com.gunnarro.android.bandy.view.dashboard.CommonFragment;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;
import com.gunnarro.android.bandy.view.dashboard.ViewUtils;

public class MatchOnlineFragment extends CommonFragment {

	private BandyService bandyService;
	private Integer matchId;
	private Match match;

	/**
	 * Need this in order to prevent triggering the selection listeners upon gui
	 * initialization.
	 */
	private boolean isInitMode = true;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MatchOnlineFragment() {
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
		View rootView = inflater.inflate(R.layout.match_online_layout, container, false);
		if (this.bandyService == null) {
			this.bandyService = new BandyServiceImpl(rootView.getContext());
		}
		match = this.bandyService.getMatch(matchId);
		boolean isEditable = !match.isPlayed();
		init(rootView, isEditable);
		getActivity().getActionBar().setSubtitle(isEditable ? "Match online" : "Match Details");
		super.setHasOptionsMenu(isEditable);
		setupEventhandlers(rootView, isEditable);
		updateMatchEventTable(rootView, isEditable);
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
			updateMatchStatus(MatchStatus.PLAYED.name());
			Toast.makeText(getActivity().getApplicationContext(), "Finished match!", Toast.LENGTH_SHORT).show();
			super.getActivity().onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void init(View rootView, boolean isEditable) {
		if (match != null) {
			setInputValue(rootView, R.id.matchStartDateTxt, Utility.formatTime(match.getStartTime(), Utility.DATE_PATTERN), isEditable);
			setInputValue(rootView, R.id.matchStartTimeTxt, Utility.formatTime(match.getStartTime(), Utility.TIME_PATTERN), isEditable);
			setInputValue(rootView, R.id.matchVenueTxt, match.getVenue(), isEditable);
			setInputValue(rootView, R.id.matchHomeTeamTxt, match.getHomeTeam().getName(), false);
			setInputValue(rootView, R.id.matchAwayTeamTxt, match.getAwayTeam().getName(), false);
			setInputValue(rootView, R.id.matchGoalsHomeTxt, match.getNumberOfGoalsHome().toString(), false);
			setInputValue(rootView, R.id.matchGoalsAwayTxt, match.getNumberOfGoalsAway().toString(), false);
		}
	}

	private void setupEventhandlers(View rootView, boolean isEditable) {
		String[] matchStatusNames = bandyService.getMatchStatusList();
		Spinner matchStatusSpinner = (Spinner) rootView.findViewById(R.id.matchStatusSpinnerId);
		ArrayAdapter<CharSequence> matchStatusAdapter = new ArrayAdapter<CharSequence>(getActivity().getApplicationContext(),
				android.R.layout.simple_spinner_item, matchStatusNames);
		matchStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		matchStatusSpinner.setAdapter(matchStatusAdapter);
		matchStatusSpinner.setOnItemSelectedListener(new MatchStatusOnItemSelectedListener());
		for (int i = 0; i < matchStatusSpinner.getCount(); i++) {
			if (matchStatusSpinner.getItemAtPosition(i).toString().equals(match.getMatchStatus().name())) {
				matchStatusSpinner.setSelection(i);
				CustomLog.e(this.getClass(), matchStatusSpinner.getItemAtPosition(i).toString());
				break;
			}
		}

		Spinner goalHomeSpinner = (Spinner) rootView.findViewById(R.id.matchGoalHomePlayersSpinnerId);
		Spinner goalAwaySpinner = (Spinner) rootView.findViewById(R.id.matchGoalAwayPlayersSpinnerId);

		if (!isEditable) {
			// turn off status changes
			matchStatusSpinner.setEnabled(false);
			// Hide the spinners the match is not open for any changes
			goalHomeSpinner.setVisibility(View.INVISIBLE);
			goalAwaySpinner.setVisibility(View.INVISIBLE);
		} else {
			// Home team players spinner
			String[] homePlayerNames = bandyService.getPlayerNames(match.getHomeTeam().getName());
			ArrayAdapter<CharSequence> goalHomeAdapter = new ArrayAdapter<CharSequence>(getActivity().getApplicationContext(),
					android.R.layout.simple_spinner_item, homePlayerNames);
			goalHomeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			goalHomeSpinner.setAdapter(goalHomeAdapter);
			goalHomeSpinner.setOnItemSelectedListener(new GoalOnItemSelectedListener(match.getHomeTeam().getName(), MatchEventTypesEnum.GOAL_HOME));

			// Away team spinner
			String[] awayPlayerNames = bandyService.getPlayerNames(match.getAwayTeam().getName());
			ArrayAdapter<CharSequence> goalAwayAdapter = new ArrayAdapter<CharSequence>(getActivity().getApplicationContext(),
					android.R.layout.simple_spinner_item, awayPlayerNames);
			goalAwayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			goalAwaySpinner.setAdapter(goalAwayAdapter);
			goalAwaySpinner.setOnItemSelectedListener(new GoalOnItemSelectedListener(match.getAwayTeam().getName(), MatchEventTypesEnum.GOAL_AWAY));
		}
	}

	private void updateMatchEventTable(int tableId, MatchEvent event) {
		TableLayout table = (TableLayout) getView().findViewById(tableId);
		if (table == null) {
			return;
		}
		table.addView(ViewUtils.createTableRow(getActivity().getApplicationContext(), event, table.getChildCount()));
	}

	private void updateMatchStatus(String status) {
		bandyService.updateMatchStatus(matchId, MatchStatus.valueOf(status));
	}

	private void updateScore(int tableId, int goalTextViewId, String teamName, String playerName, MatchEventTypesEnum eventType) {
		String goals = getInputValue(goalTextViewId);
		Integer newGoals = Integer.parseInt(goals) + 1;
		setInputValue(getView(), goalTextViewId, newGoals.toString());
		MatchEvent matchEvent = createMatchEvent(teamName, playerName, eventType.name(), newGoals.toString());
		updateMatchEventTable(tableId, matchEvent);
		switch (eventType) {
		case GOAL_AWAY:
			bandyService.updateGoalsAwayTeam(matchId, newGoals, -1);
			break;
		case GOAL_HOME:
			bandyService.updateGoalsHomeTeam(matchId, newGoals, -1);
			break;
		}
		bandyService.createMatchEvent(matchEvent);
	}

	private MatchEvent createMatchEvent(String teamName, String playerName, String eventType, String value) {
		return new MatchEvent(match.getId(), getPlayedMinutes(), teamName, playerName, eventType, value);
	}

	private int getPlayedMinutes() {
		return Calendar.getInstance().get(Calendar.MINUTE);
	}

	private void updateMatchEventTable(View rootView, boolean isEditable) {
		if (!isEditable) {
			TableLayout homeTable = (TableLayout) rootView.findViewById(R.id.matchEventAwayTblId);
			TableLayout awayTable = (TableLayout) rootView.findViewById(R.id.matchEventAwayTblId);
			List<MatchEvent> matchEventList = bandyService.getMatchEventList(matchId);
			for (MatchEvent event : matchEventList) {
				if (event.getTeamName().equals(match.getHomeTeam().getName())) {
					homeTable.addView(ViewUtils.createTableRow(rootView.getContext(), event, homeTable.getChildCount()));
				} else if (event.getTeamName().equals(match.getAwayTeam().getName())) {
					awayTable.addView(ViewUtils.createTableRow(rootView.getContext(), event, awayTable.getChildCount()));
				}
			}
		}
	}

	/**
	 * 
	 */
	public class MatchStatusOnItemSelectedListener implements OnItemSelectedListener {

		public MatchStatusOnItemSelectedListener() {
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			if (!isInitMode) {
				String status = parent.getItemAtPosition(pos).toString();
				updateMatchStatus(status);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.
		}
	}

	/**
	 * 
	 */
	public class GoalOnItemSelectedListener implements OnItemSelectedListener {

		private String teamName;
		private MatchEventTypesEnum eventType;

		public GoalOnItemSelectedListener(String teamName, MatchEventTypesEnum eventType) {
			this.teamName = teamName;
			this.eventType = eventType;
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			if (isInitMode) {
				isInitMode = false;
			} else {
				String playerName = parent.getItemAtPosition(pos).toString();
				switch (eventType) {
				case GOAL_HOME:
					updateScore(R.id.matchEventHomeTblId, R.id.matchGoalsHomeTxt, teamName, playerName, MatchEventTypesEnum.GOAL_HOME);
				case GOAL_AWAY:
					updateScore(R.id.matchEventAwayTblId, R.id.matchGoalsAwayTxt, teamName, playerName, MatchEventTypesEnum.GOAL_AWAY);
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.
		}
	}

}
