package com.gunnarro.android.bandy.view.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.party.Address;
import com.gunnarro.android.bandy.domain.party.Player;
import com.gunnarro.android.bandy.domain.party.Player.PlayerStatusEnum;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.utility.Utility;

public class EditPlayerActivity extends Activity {

	private BandyService bandyService;
	private String teamName;
	private Integer playerId;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_create_layout);
		// selectedTeamName =
		// getIntent().getStringExtra(DashboardActivity.ARG_TEAM_NAME);
		this.bandyService = new BandyServiceImpl(getApplicationContext());
		setupEventHandlers();
		teamName = getIntent().getStringExtra(DashboardActivity.ARG_TEAM_NAME);
		playerId = getIntent().getIntExtra(DashboardActivity.ARG_PLAYER_ID, -1);
		if (playerId > 0) {
			Player player = this.bandyService.getPlayer(playerId);
			init(player);
			this.getActionBar().setSubtitle(player.getFullName());
		}
		this.setTitle(teamName);
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
		/**
		 * findViewById(R.id.cancel_create_player_btn).setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { returnToParentView(); } });
		 * 
		 *           findViewById(R.id.create_player_btn).setOnClickListener(new
		 *           OnClickListener() {
		 * @Override public void onClick(View v) { savePlayer();
		 *           returnToParentView(); } });
		 **/
	}

	private void returnToParentView() {
		Intent trainingsIntent = new Intent(getApplicationContext(), TrainingsActivity.class);
		trainingsIntent.putExtra(DashboardActivity.ARG_TEAM_NAME, DashboardActivity.DEFAULT_TEAM_NAME);
		startActivity(trainingsIntent);
	}

	private void init(Player player) {
		if (player != null) {
			setInputValue(R.id.playerFirstNameTxt, player.getFirstName());
			setInputValue(R.id.playerMiddleNameTxt, player.getMiddleName());
			setInputValue(R.id.playerLastNameTxt, player.getLastName());
			setInputValue(R.id.playerDateOfBirthTxt, Utility.formatTime(player.getDateOfBirth(), Utility.DATE_PATTERN));
			setInputValue(R.id.playerMobileNumberTxt, player.getMobileNumber());
			setInputValue(R.id.playerEmailAddressTxt, player.getEmailAddress());
			setInputValue(R.id.playerStatusTxt, player.getStatus().toString());
			setInputValue(R.id.playerSchoolNameTxt, player.getSchoolName());
			if (player.getAddress() != null) {
				setInputValue(R.id.playerStreetNameTxt, player.getAddress().getStreetName());
				setInputValue(R.id.playerStreetNumberTxt, player.getAddress().getStreetNumber());
				setInputValue(R.id.playerStreetNumberPostfixTxt, player.getAddress().getStreetNumberPrefix());
				setInputValue(R.id.playerPostalCodeTxt, player.getAddress().getPostalCode());
				setInputValue(R.id.playerCityTxt, player.getAddress().getCity());
				setInputValue(R.id.playerCountryTxt, player.getAddress().getCountry());
			}
		}
	}

	private void savePlayer() {
		String firstName = getInputValue(R.id.playerFirstNameTxt);
		String middleName = getInputValue(R.id.playerMiddleNameTxt);
		String lastName = getInputValue(R.id.playerLastNameTxt);
		String dateOfBirth = getInputValue(R.id.playerDateOfBirthTxt);
		String streetName = getInputValue(R.id.playerStreetNameTxt);
		String streetNumber = getInputValue(R.id.playerStreetNumberTxt);
		String streetNumberPostfix = getInputValue(R.id.playerStreetNumberPostfixTxt);
		String postalCode = getInputValue(R.id.playerPostalCodeTxt);
		String city = getInputValue(R.id.playerCityTxt);
		String country = getInputValue(R.id.playerCountryTxt);
		String mobileNumber = getInputValue(R.id.playerMobileNumberTxt);
		String emailAddress = getInputValue(R.id.playerEmailAddressTxt);
		String schoolName = getInputValue(R.id.playerSchoolNameTxt);
		Address address = new Address(streetName, streetNumber, streetNumberPostfix, postalCode, city, country);
		Team team = this.bandyService.getTeam(1);
		Player player = new Player(team, firstName, middleName, lastName, PlayerStatusEnum.ACTIVE, null, 0, address);
		if (playerId > 0) {
			player = new Player(playerId, team, firstName, middleName, lastName, PlayerStatusEnum.ACTIVE, null, 0, address);
		}
		player.setEmailAddress(emailAddress);
		player.setMobileNumber(mobileNumber);
		player.setSchoolName(schoolName);
		int trainingId = bandyService.savePlayer(player);
	}

	private String getInputValue(int id) {
		EditText inputView = (EditText) findViewById(id);
		if (inputView != null) {
			return inputView.getText().toString();
		} else {
			CustomLog.e(this.getClass(), "No input field found for id: " + id);
		}
		return null;
	}

	private void setInputValue(int id, String value) {
		EditText inputView = (EditText) findViewById(id);
		if (inputView != null) {
			inputView.setText(value);
		} else {
			CustomLog.e(this.getClass(), "No input field found for id: " + id + ", value: " + value);
		}
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
			savePlayer();
			returnToParentView();
		default:
			startActivity(new Intent(getApplicationContext(), HomeActivity.class));
			break;
		}
		CustomLog.d(this.getClass(), "clicked on: " + item.getItemId());
		return true;
	}
}
