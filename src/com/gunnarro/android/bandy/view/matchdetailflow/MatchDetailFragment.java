package com.gunnarro.android.bandy.view.matchdetailflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.activity.MatchEvent;
import com.gunnarro.android.bandy.domain.view.list.Item;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.utility.Utility;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;
import com.gunnarro.android.bandy.view.dashboard.ViewUtils;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link MatchListActivity} in two-pane mode (on tablets) or a
 * {@link MatchDetailActivity} on handsets.
 */
public class MatchDetailFragment extends Fragment {

	private BandyService bandyService;
	private int matchId;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MatchDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setHasOptionsMenu(true);
		if (getArguments().containsKey(DashboardActivity.ARG_MATCH_ID)) {
			matchId = getArguments().getInt(DashboardActivity.ARG_MATCH_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.match_details_layout, container, false);
		if (this.bandyService == null) {
			this.bandyService = new BandyServiceImpl(rootView.getContext());
		}
		Match match = this.bandyService.getMatch(matchId);
		getActivity().setTitle(match.getTeamVersus());
		updateMatchDetails(rootView, match);
		// Do not show the edit options menu if the match is finished.
		// FIXME
		// if (match.isFinished()) {
		super.setHasOptionsMenu(true);
		// }
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
			delete(matchId);
			Toast.makeText(getActivity().getApplicationContext(), "Deleted match!", Toast.LENGTH_SHORT).show();
			super.getActivity().onBackPressed();
		default:
			return false;
		}
	}

	private void delete(Integer matchId) {
		this.bandyService.deleteMatch(matchId);
	}

	private void updateMatchDetails(View rootView, Match match) {
		if (match != null) {
			((TextView) rootView.findViewById(R.id.matchVersusTxt)).setText(match.getTeamVersus());
			if (match.getResult() != null) {
				((TextView) rootView.findViewById(R.id.matchResultTxt)).setText(match.getResult());
			}
			((TextView) rootView.findViewById(R.id.matchStatusTxt)).setText(match.getMatchStatus().name());
			((TextView) rootView.findViewById(R.id.matchVenueTxt)).setText(match.getVenue());
			((TextView) rootView.findViewById(R.id.matchStartTimeTxt)).setText(Utility.formatTime(match.getStartTime(), null));
			((TextView) rootView.findViewById(R.id.matchRefereeTxt)).setText(match.getReferee().getFullName());
			((TextView) rootView.findViewById(R.id.matchRegisteredPlayersTxt)).setText("");

			//
			// ListView playerListView = (ListView)
			// rootView.findViewById(R.id.matchPlayerList);
			// ArrayAdapter<Item> adapter = new
			// ArrayAdapter<Item>(getActivity(),
			// R.layout.custom_simple_list_item,
			// getPlayerList(match.getTeam().getId(),
			// match.getId()));
			// playerListView.setAdapter(adapter);
		}
	}

	private void updateMatchEventTable(Match match) {
//		TableLayout homeTable = (TableLayout) getView().findViewById(tableId);
//		TableLayout awayTable = (TableLayout) getView().findViewById(tableId);
//		List<MatchEvent> matchEventList = bandyService.getMatchEventList(matchId);
//		for (MatchEvent event : matchEventList) {
//			if (event.getTeamName().equals(match.getHomeTeam().getName())) {
//				homeTable.addView(ViewUtils.createTableRow(getActivity().getApplicationContext(), event, homeTable.getChildCount()));
//			} else if (event.getTeamName().equals(match.getAwayTeam().getName())) {
//				awayTable.addView(ViewUtils.createTableRow(getActivity().getApplicationContext(), event, awayTable.getChildCount()));
//			}
//		}
	}

	private List<Item> getPlayerList(int teamId, int matchId) {
		List<Item> playerList = this.bandyService.getPlayersAsItemList(teamId);
		List<Item> playerSignedList = bandyService.getMatchSignedPlayerList(teamId, matchId);
		Set<Item> itemSet = new HashSet<Item>();
		itemSet.addAll(playerSignedList);
		itemSet.addAll(playerList);
		ArrayList<Item> players = new ArrayList<Item>(itemSet);
		Collections.sort(players);
		return players;
	}

}
