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
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.Toast;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.activity.MatchEvent;
import com.gunnarro.android.bandy.domain.activity.MatchEvent.MatchEventTypesEnum;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl.SelectionListType;
import com.gunnarro.android.bandy.utility.Utility;
import com.gunnarro.android.bandy.view.dashboard.CommonFragment;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;
import com.gunnarro.android.bandy.view.dashboard.ViewUtils;
import com.gunnarro.android.bandy.view.dialog.SelectDialogOnClickListener;

public class MatchEditFragment extends CommonFragment {

	private BandyService bandyService;
	private Integer matchId;
	private Match match;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MatchEditFragment() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setHasOptionsMenu(true);
		if (getArguments().containsKey(DashboardActivity.ARG_MATCH_ID)) {
			matchId = getArguments().getInt(DashboardActivity.ARG_MATCH_ID);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.match_edit_layout, container, false);
		if (this.bandyService == null) {
			this.bandyService = new BandyServiceImpl(rootView.getContext());
		}
		boolean isEditable = true;
		if (matchId != null) {
			match = this.bandyService.getMatch(matchId);
			isEditable = !match.isPlayed();
			init(rootView, isEditable);
		}
		setupEventhandlers(rootView, isEditable);
		updateMatchEventTable(rootView);
		super.setHasOptionsMenu(isEditable);
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
			Toast.makeText(getActivity().getApplicationContext(), "Finished match!", Toast.LENGTH_SHORT).show();
			super.getActivity().onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void init(View rootView, boolean isEditable) {
		if (match != null) {
			setInputValue(rootView, R.id.matchStatusTxt, match.getMatchStatus().name(), isEditable);
			setInputValue(rootView, R.id.matchStartDateTxt, Utility.formatTime(match.getStartTime(), Utility.DATE_PATTERN), isEditable);
			setInputValue(rootView, R.id.matchStartTimeTxt, Utility.formatTime(match.getStartTime(), Utility.TIME_PATTERN), isEditable);
			setInputValue(rootView, R.id.matchVenueTxt, match.getVenue(), isEditable);
			setInputValue(rootView, R.id.matchHomeTeamTxt, match.getHomeTeam().getName(), false);
			setInputValue(rootView, R.id.matchAwayTeamTxt, match.getAwayTeam().getName(), false);
			setInputValue(rootView, R.id.matchGoalsHomeTxt, match.getNumberOfGoalsHome().toString(), isEditable);
			setInputValue(rootView, R.id.matchGoalsAwayTxt, match.getNumberOfGoalsAway().toString(), isEditable);
			setInputValue(rootView, R.id.matchRegisteredPlayersTxt, match.getNumberOfSignedPlayers().toString(), isEditable);
		}
	}

	private void setupEventhandlers(View rootView, boolean isEditable) {
		SelectDialogOnClickListener.turnOnInitMode();
		ImageButton statusBtn = (ImageButton) rootView.findViewById(R.id.matchSelectStatusBtnId);
		statusBtn.setOnClickListener(new SelectDialogOnClickListener(getFragmentManager(), bandyService.getMatchStatusList(), R.id.matchStatusTxt, false));

		ImageButton refereeBtn = (ImageButton) rootView.findViewById(R.id.matchSelectRefereeBtnId);
		refereeBtn.setOnClickListener(new SelectDialogOnClickListener(getFragmentManager(), bandyService, SelectionListType.REFEREE_NAMES, -1,
				R.id.matchRefereeNameTxt, false));

		ImageButton registrerPlayersBtn = (ImageButton) rootView.findViewById(R.id.matchRegistrerPlayersBtnId);
		registrerPlayersBtn.setOnClickListener(new SelectDialogOnClickListener(getFragmentManager(), bandyService, SelectionListType.PLAYER_NAMES, match
				.getTeam().getId(), R.id.matchRegisteredPlayersTxt, true));

		ImageButton goalHomeBtn = (ImageButton) rootView.findViewById(R.id.matchSelectHomeGoalScorerBtnId);
		goalHomeBtn.setOnClickListener(new SelectDialogOnClickListener(getFragmentManager(), bandyService, SelectionListType.PLAYER_NAMES, match.getHomeTeam()
				.getId(), R.id.matchGoalsHomeTxt, false));

		ImageButton goalAwayBtn = (ImageButton) rootView.findViewById(R.id.matchSelectAwayGoalScorerBtnId);
		goalAwayBtn.setOnClickListener(new SelectDialogOnClickListener(getFragmentManager(), bandyService, SelectionListType.PLAYER_NAMES, match.getAwayTeam()
				.getId(), R.id.matchGoalsAwayTxt, false));

		SelectDialogOnClickListener.turnOffInitMode();
	}

	public void updateSelectedField(String[] values, int fieldId) {
		if (fieldId == R.id.matchGoalsHomeTxt) {
			updateScore(R.id.matchEventHomeTblId, R.id.matchGoalsHomeTxt, teamName, values[0], MatchEventTypesEnum.GOAL_HOME);
		} else if (fieldId == R.id.matchGoalsAwayTxt) {
			updateScore(R.id.matchEventAwayTblId, R.id.matchGoalsAwayTxt, teamName, values[0], MatchEventTypesEnum.GOAL_AWAY);
		} else if (fieldId == R.id.matchStatusTxt) {
			setInputValue(getView(), fieldId, values[0]);
			// FIXME
			updateMatchStatus(0);
		} else if (fieldId == R.id.matchRegisteredPlayersTxt) {
			int numRegPlayers = Integer.parseInt(getInputValue(R.id.matchRegisteredPlayersTxt, false));
			for (String player : values) {
				if (player != null)
					numRegPlayers = bandyService.signupForMatch(Integer.parseInt(player.split(":")[0]), match.getId());
			}
			setInputValue(getView(), fieldId, Integer.toString(numRegPlayers));
		} else if (fieldId == R.id.matchSelectRefereeBtnId) {
			bandyService.registrerRefereeForMatch(1, matchId);
		} else {
			CustomLog.e(this.getClass(), fieldId + " <> " + R.id.matchGoalsHomeTxt);
			setInputValue(getView(), fieldId, values[0]);
		}
	}

	private void updateMatchEventTable(int tableId, MatchEvent event) {
		TableLayout table = (TableLayout) getView().findViewById(tableId);
		if (table == null) {
			return;
		}
		table.addView(ViewUtils.createTableRow(getActivity().getApplicationContext(), event, table.getChildCount()));
	}

	private void updateMatchStatus(int statusId) {
		bandyService.updateMatchStatus(matchId, statusId);
	}

	private void updateScore(int tableId, int goalTextViewId, String teamName, String playerName, MatchEventTypesEnum eventType) {
		String goals = getInputValue(goalTextViewId, false);
		Integer newGoals = Integer.parseInt(goals) + 1;
		setInputValue(getView(), goalTextViewId, newGoals.toString());
		MatchEvent matchEvent = createMatchEvent(teamName, playerName, eventType.name(), newGoals.toString());
		updateMatchEventTable(tableId, matchEvent);
		switch (eventType) {
		case GOAL_HOME:
			bandyService.updateGoalsHomeTeam(matchId, newGoals);
			break;
		case GOAL_AWAY:
			bandyService.updateGoalsAwayTeam(matchId, newGoals);
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

	private void updateMatchEventTable(View rootView) {
		TableLayout homeTable = (TableLayout) rootView.findViewById(R.id.matchEventHomeTblId);
		TableLayout awayTable = (TableLayout) rootView.findViewById(R.id.matchEventAwayTblId);
		List<MatchEvent> matchEventList = bandyService.getMatchEventList(matchId);
		for (MatchEvent event : matchEventList) {
			CustomLog.e(this.getClass(), event.toString());
			if (event.getTeamName().equals(match.getHomeTeam().getName())) {
				homeTable.addView(ViewUtils.createTableRow(rootView.getContext(), event, homeTable.getChildCount()));
			} else if (event.getTeamName().equals(match.getAwayTeam().getName())) {
				awayTable.addView(ViewUtils.createTableRow(rootView.getContext(), event, awayTable.getChildCount()));
			}
		}
	}
}
