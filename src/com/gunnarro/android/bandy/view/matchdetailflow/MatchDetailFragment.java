package com.gunnarro.android.bandy.view.matchdetailflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.view.list.Item;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.utility.Utility;

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
		if (getArguments().containsKey(MatchDetailActivity.ARG_MATCH_ID)) {
			matchId = getArguments().getInt(MatchDetailActivity.ARG_MATCH_ID);
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
		return rootView;
	}

	private void updateMatchDetails(View rootView, Match match) {
		if (match != null) {
			String matchInfo = match.getTeamVersus();
			if (match.getResult() != null) {
				matchInfo = matchInfo + " - " + match.getResult();
			}
			((TextView) rootView.findViewById(R.id.matchNameTxt)).setText(matchInfo);
			((TextView) rootView.findViewById(R.id.matchVenueTxt)).setText(match.getVenue());
			((TextView) rootView.findViewById(R.id.matchStartTimeTxt)).setText(Utility.formatTime(match.getStartTime(), null));
			((TextView) rootView.findViewById(R.id.matchRefereeTxt)).setText(match.getReferee().getFullName());
			((TextView) rootView.findViewById(R.id.matchRegisteredPlayersTxt)).setText("");

			ListView playerListView = (ListView) rootView.findViewById(R.id.matchPlayerList);
			ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(getActivity(), R.layout.custom_simple_list_item, getPlayerList(match.getTeam().getId(),
					match.getId()));
			playerListView.setAdapter(adapter);
		}
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
