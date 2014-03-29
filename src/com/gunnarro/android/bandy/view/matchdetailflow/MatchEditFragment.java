package com.gunnarro.android.bandy.view.matchdetailflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.party.Referee;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.utility.Utility;
import com.gunnarro.android.bandy.view.dashboard.CommonFragment;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link MatchListActivity} in two-pane mode (on tablets) or a
 * {@link MatchDetailActivity} on handsets.
 */
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments().containsKey(DashboardActivity.ARG_MATCH_ID)) {
			matchId = getArguments().getInt(DashboardActivity.ARG_MATCH_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.match_edit_layout, container, false);
		if (this.bandyService == null) {
			this.bandyService = new BandyServiceImpl(rootView.getContext());
		}
		if (matchId != null) {
			match = this.bandyService.getMatch(matchId);
			init(rootView);
			getActivity().getActionBar().setSubtitle(match.getTeamVersus());
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

	private void init(View rootView) {
		if (match != null) {
			setInputValue(rootView, R.id.matchStartDateTxt, Utility.formatTime(match.getStartTime(), Utility.DATE_PATTERN));
			setInputValue(rootView, R.id.matchStartTimeTxt, Utility.formatTime(match.getStartTime(), Utility.TIME_PATTERN));
			setInputValue(rootView, R.id.matchHomeTeamTxt, match.getHomeTeam().getName());
			setInputValue(rootView, R.id.matchAwayTeamTxt, match.getAwayTeam().getName());
			setInputValue(rootView, R.id.matchGoalsHomeTxt, match.getNumberOfGoalsHome().toString());
			setInputValue(rootView, R.id.matchGoalsAwayTxt, match.getNumberOfGoalsAway().toString());
			setInputValue(rootView, R.id.venueTxt, match.getVenue());
			// matchRefereeSpinnerId
		}
	}

	private void save() {
		String startDateStr = getInputValue(R.id.matchStartDateTxt);
		String startTimeStr = getInputValue(R.id.matchStartTimeTxt);
		String goalsHome = getInputValue(R.id.matchGoalsHomeTxt);
		String goalsAway = getInputValue(R.id.matchGoalsAwayTxt);
		if (!startDateStr.isEmpty() && !startTimeStr.isEmpty()) {
			long startTime = Utility.timeToDate(startDateStr + " " + startTimeStr, Utility.DATE_TIME_PATTERN).getTime();
			match.setStartTime(startTime);
		}
		match.setNumberOfGoalsHome(Integer.parseInt(goalsHome));
		match.setNumberOfGoalsAway(Integer.parseInt(goalsAway));
		Referee referee = new Referee("none", "none");
		match.setReferee(referee);
		this.bandyService.saveMatch(match);
	}

}
