/*
 * Copyright (C) 2011 Wglxy.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gunnarro.android.bandy.view.dashboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.os.Bundle;
import android.util.SparseArray;
import android.widget.ExpandableListView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.view.list.Group;
import com.gunnarro.android.bandy.domain.view.list.Item;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.view.expandablelist.MyExpandableListAdapter;

/**
 * 
 */
public class MatchesActivity extends DashboardActivity {

	// More efficient than HashMap for mapping integers to objects
	SparseArray<Group> groups = new SparseArray<Group>();
	private BandyService bandyService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.matches_list_layout);
		String teamName = getIntent().getStringExtra(DashboardActivity.ARG_TEAM_NAME);
		this.setTitle(teamName);
		this.bandyService = new BandyServiceImpl(getApplicationContext());
		populateList(teamName);
		ExpandableListView listView = (ExpandableListView) findViewById(R.id.matches_expandable_listView);
		MyExpandableListAdapter adapter = new MyExpandableListAdapter(this, groups, bandyService);
		listView.setAdapter(adapter);
		setupEventHandlers();
	}

	private void setupEventHandlers() {
	}

	private void populateList(String teamName) {
		Team team = bandyService.getTeam(teamName, true);
		List<Match> matchList = this.bandyService.getMatchList(team.getId(), "year");
		List<Item> playerList = this.bandyService.getPlayersAsItemList(team.getId());
		int i = 0;
		for (Match match : matchList) {
			List<Item> playerSignedList = bandyService.getMatchSignedPlayerList(team.getId(), match.getId());
			Set<Item> itemSet = new HashSet<Item>();
			itemSet.addAll(playerSignedList);
			itemSet.addAll(playerList);
			ArrayList<Item> players = new ArrayList<Item>(itemSet);
			Collections.sort(players);
			Group group = new Group(match.getId(), match.getTeamVersus(), players);
			String info = "(" + playerSignedList.size() + "," + playerList.size() + ")";
			group.setInformation(info);
			groups.append(i, group);
			i++;
		}
	}
} // end class
