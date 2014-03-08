package com.gunnarro.android.bandy.view.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;

public class CreatePlayerActivity extends Activity {

	private BandyService bandyService;
	private Bundle bundle;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_create_layout);
		// selectedTeamName =
		// getIntent().getStringExtra(DashboardActivity.ARG_TEAM_NAME);
		this.setTitle("Team ");
		this.getActionBar().setSubtitle("New player");
		this.bandyService = new BandyServiceImpl(getApplicationContext());
		setupEventHandlers();
		bundle = getIntent().getExtras();
		init();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPause() {
		super.onPause();
	}

	private void setupEventHandlers() {

		findViewById(R.id.cancel_create_player_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				returnToParentView();
			}
		});

		findViewById(R.id.create_player_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createPlayer();
				returnToParentView();
			}
		});

	}

	private void returnToParentView() {
		Intent trainingsIntent = new Intent(getApplicationContext(), TrainingsActivity.class);
		trainingsIntent.putExtra(DashboardActivity.ARG_TEAM_NAME, DashboardActivity.DEFAULT_TEAM_NAME);
		startActivity(trainingsIntent);
	}

	private void init() {
	}

	private void createPlayer() {
		String firstName = getInputValue(R.id.playerFirstNameTxt);
		String middleName = getInputValue(R.id.playerMiddleNameTxt);
		String lastName = getInputValue(R.id.playerLastNameTxt);
		// Player player = new Player();
		// int trainingId = bandyService.createPlayer(player);
	}

	private String getInputValue(int id) {
		return ((EditText) findViewById(id)).getText().toString();
	}

}
