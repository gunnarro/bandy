package com.gunnarro.android.bandy.view.playerdetailflow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.party.Address;
import com.gunnarro.android.bandy.domain.party.Player;
import com.gunnarro.android.bandy.domain.party.Player.PlayerStatusEnum;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.utility.Utility;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

public class PlayerEditFragment extends Fragment {

	private BandyService bandyService;
	private String teamName;
	private Integer playerId;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public PlayerEditFragment() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setHasOptionsMenu(true);
		if (getArguments().containsKey(DashboardActivity.ARG_TEAM_NAME)) {
			teamName = getArguments().getString(DashboardActivity.ARG_TEAM_NAME);
		}
		if (getArguments().containsKey(DashboardActivity.ARG_PLAYER_ID)) {
			playerId = getArguments().getInt(DashboardActivity.ARG_PLAYER_ID);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.player_new_layout, container, false);
		if (this.bandyService == null) {
			this.bandyService = new BandyServiceImpl(rootView.getContext());
		}

		if (playerId != null) {
			Player player = this.bandyService.getPlayer(playerId);
			init(rootView, player);
			getActivity().getActionBar().setSubtitle(player.getFullName());
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

	private void init(View rootView, Player player) {
		if (player != null) {
			setInputValue(rootView, R.id.playerFirstNameTxt, player.getFirstName());
			setInputValue(rootView, R.id.playerMiddleNameTxt, player.getMiddleName());
			setInputValue(rootView, R.id.playerLastNameTxt, player.getLastName());
			setInputValue(rootView, R.id.playerDateOfBirthTxt, Utility.formatTime(player.getDateOfBirth(), Utility.DATE_PATTERN));
			setInputValue(rootView, R.id.playerMobileNumberTxt, player.getMobileNumber());
			setInputValue(rootView, R.id.playerEmailAddressTxt, player.getEmailAddress());
			setInputValue(rootView, R.id.playerSchoolNameTxt, player.getSchoolName());
			if (player.getAddress() != null) {
				setInputValue(rootView, R.id.playerStreetNameTxt, player.getAddress().getStreetName());
				setInputValue(rootView, R.id.playerStreetNumberTxt, player.getAddress().getStreetNumber());
				setInputValue(rootView, R.id.playerStreetNumberPostfixTxt, player.getAddress().getStreetNumberPrefix());
				setInputValue(rootView, R.id.playerPostalCodeTxt, player.getAddress().getPostalCode());
				setInputValue(rootView, R.id.playerCityTxt, player.getAddress().getCity());
				setInputValue(rootView, R.id.playerCountryTxt, player.getAddress().getCountry());
			}
		}
	}

	private void save() {
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
		Player player = new Player(team, firstName, middleName, lastName, PlayerStatusEnum.ACTIVE, null, Utility.timeToDate(dateOfBirth, "dd.mm.yyyy")
				.getTime(), address);
		if (playerId > 0) {
			player = new Player(playerId, team, firstName, middleName, lastName, PlayerStatusEnum.ACTIVE, null, Utility.timeToDate(dateOfBirth, "dd.mm.yyyy")
					.getTime(), address);
		}
		player.setEmailAddress(emailAddress);
		player.setMobileNumber(mobileNumber);
		player.setSchoolName(schoolName);
		int trainingId = bandyService.savePlayer(player);
		CustomLog.e(this.getClass(), player.toString());
	}

	private String getInputValue(int id) {
		EditText inputView = (EditText) getView().findViewById(id);
		if (inputView != null) {
			return inputView.getText().toString();
		} else {
			CustomLog.e(this.getClass(), "No input field found for id: " + id);
		}
		return null;
	}

	private void setInputValue(View rootView, int id, String value) {
		EditText inputView = (EditText) rootView.findViewById(id);
		if (inputView != null) {
			inputView.setText(value);
		} else {
			CustomLog.e(this.getClass(), "No input field found for id: " + id + ", value: " + value);
		}
	}

}
