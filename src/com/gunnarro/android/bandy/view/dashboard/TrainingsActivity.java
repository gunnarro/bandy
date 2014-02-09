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
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Activity.ActivityTypesEnum;
import com.gunnarro.android.bandy.domain.activity.Training;
import com.gunnarro.android.bandy.domain.view.list.Group;
import com.gunnarro.android.bandy.domain.view.list.Item;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.utility.Utility;
import com.gunnarro.android.bandy.view.expandablelist.TrainingExpandableListAdapter;

/**
 * 
 */
public class TrainingsActivity extends DashboardActivity {

	private final static int MIN_NUMBER_OF_SIGNED_PLAYERS = -1;
	// More efficient than HashMap for mapping integers to objects
	SparseArray<Group> groups = new SparseArray<Group>();
	private BandyService bandyService;
	private String teamName;
	private TrainingExpandableListAdapter adapter;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.training_list_layout);
		teamName = getIntent().getStringExtra(DashboardActivity.ARG_TEAM_NAME);
		this.setTitle(ActivityTypesEnum.Training.name() + " " + teamName);
		this.bandyService = new BandyServiceImpl(getApplicationContext());
		populateList(teamName);
		ExpandableListView listView = (ExpandableListView) findViewById(R.id.training_expandable_listView);
		adapter = new TrainingExpandableListAdapter(this, groups, bandyService, MIN_NUMBER_OF_SIGNED_PLAYERS);
		listView.setAdapter(adapter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar_menu, menu);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_create_training:
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			int hours = 2;
			Team team = bandyService.getTeam(teamName, false);
			Training training = new Training(calendar.getTimeInMillis(), hours, team, "Bergbanen");
			int createTraining = bandyService.createTraining(training);
			// adapter.notifyDataSetChanged();
			if (createTraining != 0) {
				Toast.makeText(this, "Created new training:\n" + training.toString() + "\nid=" + createTraining, Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Training already exist:\n" + training.toString() + "\nid=" + createTraining, Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * 
	 * @param v
	 */
	public void onClickCreate(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.create_training_btn:
			Toast.makeText(this, "Create training view Not implements", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	private void populateList(String teamName) {
		Team team = bandyService.getTeam(teamName, true);
		List<Training> trainingList = this.bandyService.getTrainingList(team.getId(), null);
		List<Item> playerList = this.bandyService.getPlayersAsItemList(team.getId());
		int i = 0;
		for (Training training : trainingList) {
			// List<Item> playerSignedList =
			// bandyService.getTrainingRegistreredPlayerList(team.getId(),
			// training.getId());
			Set<Item> itemSet = new HashSet<Item>();
			// itemSet.addAll(playerSignedList);
			itemSet.addAll(playerList);
			ArrayList<Item> players = new ArrayList<Item>(itemSet);
			Collections.sort(players);
			String header = Utility.formatTime(training.getStartDate(), Utility.DATE_TIME_PATTERN) + " - "
					+ Utility.formatTime(training.getEndTime(), Utility.TIME_PATTERN);
			Group group = new Group(training.getId(), header, training.isFinished(), players);
			group.setSubHeader1(training.getName());
			group.setSubHeader2(training.getVenue());
			groups.append(i, group);
			i++;
		}
		// finally, update the action bar sub title with number of players for
		// selected team
		getActionBar().setSubtitle("Number of players: " + playerList.size());
	}
} // end class
