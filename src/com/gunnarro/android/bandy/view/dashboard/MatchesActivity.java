package com.gunnarro.android.bandy.view.dashboard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Activity.ActivityTypesEnum;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.view.list.Group;
import com.gunnarro.android.bandy.domain.view.list.Item;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.utility.Utility;
import com.gunnarro.android.bandy.view.expandablelist.MatchExpandableListAdapter;

/**
 * @deprecated
 */
public class MatchesActivity extends DashboardActivity {

	private final static int MIN_NUMBER_OF_SIGNED_PLAYERS = 7;
	// More efficient than HashMap for mapping integers to objects
	SparseArray<Group> groups = new SparseArray<Group>();
	private BandyService bandyService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.match_list_layout);
		String teamName = getIntent().getStringExtra(DashboardActivity.ARG_TEAM_NAME);
		this.setTitle(ActivityTypesEnum.Match.name() + " " + teamName);
		this.bandyService = new BandyServiceImpl(getApplicationContext());
		populateList(teamName);
		ExpandableListView listView = (ExpandableListView) findViewById(R.id.match_expandable_listView);
		MatchExpandableListAdapter adapter = new MatchExpandableListAdapter(this, groups, bandyService, MIN_NUMBER_OF_SIGNED_PLAYERS);
		listView.setAdapter(adapter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar_menu_create, menu);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_new_activity:
			startActivity(new Intent(getApplicationContext(), CreateMatchActivity.class));
			break;
		default:
			startActivity(new Intent(getApplicationContext(), HomeActivity.class));
			break;
		}
		CustomLog.d(this.getClass(), "clicked on: " + item.getItemId());
		return true;
	}

	private void populateList(String teamName) {
		Team team = bandyService.getTeam(teamName, true);
		List<Match> matchList = this.bandyService.getMatchList(team.getId(), Calendar.YEAR);
		List<Item> playerList = this.bandyService.getPlayersAsItemList(team.getId());
		int i = 0;
		for (Match match : matchList) {
			List<Item> playerSignedList = bandyService.getMatchSignedPlayerList(team.getId(), match.getId());
			Set<Item> itemSet = new HashSet<Item>();
			itemSet.addAll(playerSignedList);
			itemSet.addAll(playerList);
			ArrayList<Item> players = new ArrayList<Item>(itemSet);
			Collections.sort(players);
			String header = Utility.formatTime(match.getStartTime(), Utility.DATE_DEFAULT_PATTERN);
			Group group = new Group(match.getId(), header, match.isPlayed(), players);
			group.setSubHeader1(match.getTeamVersus());
			group.setSubHeader2("Signed players: " + playerSignedList.size());
			groups.append(i, group);
			i++;
		}
		// finally, update the action bar sub title with number of players for
		// selected team
		getActionBar().setSubtitle("Number of players: " + playerList.size());
	}
} // end class
