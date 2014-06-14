package com.gunnarro.android.bandy.view.dashboard;

import java.util.ArrayList;
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
	private SparseArray<Group> groups = new SparseArray<Group>();
	private BandyService bandyService;
	private String teamName;
	private Integer clubId;
	private TrainingExpandableListAdapter adapter;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.training_list_layout);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		teamName = getIntent().getStringExtra(DashboardActivity.ARG_TEAM_NAME);
		this.setTitle(teamName);
		this.bandyService = new BandyServiceImpl(getApplicationContext());
		int size = populateList(teamName);
		getActionBar().setSubtitle("Number of trainings: " + size);
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
			startActivity(new Intent(getApplicationContext(), CreateTrainingActivity.class));
			break;
		default:
			startActivity(new Intent(getApplicationContext(), HomeActivity.class));
			break;
		}
		CustomLog.d(this.getClass(), "clicked on: " + item.getItemId());
		return true;
	}

	private int populateList(String teamName) {
		List<Training> trainingList = new ArrayList<Training>();
		try {
			Team team = bandyService.getTeam(clubId, teamName, true);
			trainingList = this.bandyService.getTrainingList(team.getId(), null);
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
				String header = Utility.formatTime(training.getStartTime(), Utility.DATE_TIME_PATTERN) + " - "
						+ Utility.formatTime(training.getEndTime(), Utility.TIME_PATTERN) + " " + !training.isFinished();
				Group group = new Group(training.getId(), header, !training.isFinished(), players);
				group.setSubHeader1(training.getName());
				group.setSubHeader2(training.getVenue());
				groups.append(i, group);
				i++;
			}
		} catch (Exception e) {
			CustomLog.e(this.getClass(), e.getMessage());
		}
		// finally, update the action bar sub title with number of players for
		// selected team
		return trainingList.size();
	}

} // end class
