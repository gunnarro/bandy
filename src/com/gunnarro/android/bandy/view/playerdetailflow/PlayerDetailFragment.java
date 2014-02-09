package com.gunnarro.android.bandy.view.playerdetailflow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.domain.Player;
import com.gunnarro.android.bandy.domain.Statistic;
import com.gunnarro.android.bandy.domain.view.list.Item;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.utility.Utility;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link PlayerListActivity} in two-pane mode (on tablets) or a
 * {@link PlayerDetailActivity} on handsets.
 */
public class PlayerDetailFragment extends Fragment {

	private BandyService bandyService;
	private int playerId;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public PlayerDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments().containsKey(PlayerDetailActivity.ARG_PLAYER_ID)) {
			playerId = getArguments().getInt(PlayerDetailActivity.ARG_PLAYER_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.player_details_layout, container, false);
		if (this.bandyService == null) {
			this.bandyService = new BandyServiceImpl(rootView.getContext());
		}
		Player player = this.bandyService.getPlayer(playerId);
		getActivity().setTitle(player.getFullName());
		updatePlayerDetails(rootView, player);
		Statistic playerStatistic = bandyService.getPlayerStatistic(player.getTeam().getId(), player.getId());
		updatePlayerStatistic(rootView, playerStatistic);
		return rootView;
	}

	private void updatePlayerDetails(View rootView, Player player) {
		if (player != null) {
			((TextView) rootView.findViewById(R.id.playerFullNameTxt)).setText(player.getFullName());
			((TextView) rootView.findViewById(R.id.playerDateOfBirthTxt)).setText(Utility.formatTime(player.getDateOfBirth(), Utility.DATE_PATTERN));
			((TextView) rootView.findViewById(R.id.playerMobileTxt)).setText(player.getMobileNumber());
			((TextView) rootView.findViewById(R.id.playerEmailTxt)).setText(player.getEmailAddress());
			((TextView) rootView.findViewById(R.id.playerStatusTxt)).setText(player.getStatus().toString());
			((TextView) rootView.findViewById(R.id.playerSchoolTxt)).setText(player.getSchoolName());
			if (player.getAddress() != null) {
				((TextView) rootView.findViewById(R.id.playerStreetTxt)).setText(player.getAddress().getFullStreetName());
				((TextView) rootView.findViewById(R.id.playerCityTxt)).setText(player.getAddress().getPostalCode() + " " + player.getAddress().getCity());
				((TextView) rootView.findViewById(R.id.playerCountryTxt)).setText(player.getAddress().getCountry());
			}
			ListView parentListView = (ListView) rootView.findViewById(R.id.player_parent_list);
			ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(getActivity(), R.layout.custom_simple_list_item, player.getParentItemList());
			parentListView.setAdapter(adapter);
		}
	}

	private void updatePlayerStatistic(View rootView, Statistic statistic) {
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
