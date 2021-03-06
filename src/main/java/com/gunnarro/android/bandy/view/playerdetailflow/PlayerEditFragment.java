package com.gunnarro.android.bandy.view.playerdetailflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.party.Address;
import com.gunnarro.android.bandy.domain.party.Player;
import com.gunnarro.android.bandy.domain.party.Player.PlayerStatusEnum;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.exception.ApplicationException;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.utility.Utility;
import com.gunnarro.android.bandy.view.dashboard.CommonFragment;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

public class PlayerEditFragment extends CommonFragment {

	private BandyService bandyService;
	private Integer playerId;
	private Player player;

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
			player = this.bandyService.getPlayer(playerId);
			init(rootView);
			getActivity().getActionBar().setTitle(player.getTeam().getClub().getName());
			getActivity().getActionBar().setSubtitle(player.getTeam().getClub().getDepartmentName());
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
			super.getActivity().setResult(PlayerListActivity.RESULT_CODE_PLAYER_UNCHANGED);
			super.getActivity().onBackPressed();
			return true;
		case R.id.action_save:
			boolean isSaved = save();
			if (isSaved) {
				Toast.makeText(getActivity().getApplicationContext(), "Saved player!", Toast.LENGTH_SHORT).show();
				super.getActivity().setResult(PlayerListActivity.RESULT_CODE_PLAYER_CHANGED);
				super.getActivity().onBackPressed();
				return true;
			}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void init(View rootView) {
		if (player != null) {
			setInputValue(rootView, R.id.playerFirstNameTxt, player.getFirstName());
			setInputValue(rootView, R.id.playerMiddleNameTxt, player.getMiddleName());
			setInputValue(rootView, R.id.playerLastNameTxt, player.getLastName());
			setInputValue(rootView, R.id.playerDateOfBirthTxt, Utility.formatTime(player.getDateOfBirth(), Utility.DATE_PATTERN));
			setInputValue(rootView, R.id.playerMobileNumberTxt, player.getMobileNumber());
			setInputValue(rootView, R.id.playerEmailAddressTxt, player.getEmailAddress());
			setInputValue(rootView, R.id.playerSchoolNameTxt, player.getSchoolName());
			setGender(rootView, player.getGender());
			if (player.getAddress() != null) {
				setInputValue(rootView, R.id.playerStreetNameTxt, player.getAddress().getStreetName());
				setInputValue(rootView, R.id.playerStreetNumberTxt, player.getAddress().getStreetNumber());
				setInputValue(rootView, R.id.playerStreetNumberPostfixTxt, player.getAddress().getStreetNumberPrefix());
				setInputValue(rootView, R.id.playerPostalCodeTxt, player.getAddress().getPostalCode());
				setInputValue(rootView, R.id.playerCityTxt, player.getAddress().getCity());
				setInputValue(rootView, R.id.playerCountryTxt, player.getAddress().getCountry());
			}
		} else {
			setGender(rootView, "Male");
		}
	}

	private void setupEventHandlers(View rootView) {

	}

	private boolean save() {
		try {
			String firstName = getInputValue(R.id.playerFirstNameTxt, true);
			String middleName = getInputValue(R.id.playerMiddleNameTxt, false);
			String lastName = getInputValue(R.id.playerLastNameTxt, true);
			String dateOfBirth = getInputValue(R.id.playerDateOfBirthTxt, false);
			String streetName = getInputValue(R.id.playerStreetNameTxt, false);
			String streetNumber = getInputValue(R.id.playerStreetNumberTxt, false);
			String streetNumberPostfix = getInputValue(R.id.playerStreetNumberPostfixTxt, false);
			String postalCode = getInputValue(R.id.playerPostalCodeTxt, false);
			String city = getInputValue(R.id.playerCityTxt, false);
			String country = getInputValue(R.id.playerCountryTxt, false);
			String mobileNumber = getInputValue(R.id.playerMobileNumberTxt, false);
			String emailAddress = getInputValue(R.id.playerEmailAddressTxt, false);
			String schoolName = getInputValue(R.id.playerSchoolNameTxt, false);

			if (player == null) {
				Team team = this.bandyService.getTeam(clubId, teamName, false);
				Address address = new Address(streetName, streetNumber, streetNumberPostfix, postalCode, city, country);
				player = new Player(team, firstName, middleName, lastName, getSelectedGender(), PlayerStatusEnum.ACTIVE, null, Utility.timeToDate(dateOfBirth,
						"dd.mm.yyyy").getTime(), address);
			} else {
				// Update player
				player.setFirstName(firstName);
				player.setMiddleName(middleName);
				player.setLastName(lastName);
				player.setGender(getSelectedGender());
				player.getAddress().setStreetName(streetName);
				player.getAddress().setStreetNumber(streetNumber);
				player.getAddress().setStreetNumberPrefix(streetNumberPostfix);
				player.getAddress().setCity(city);
				player.getAddress().setPostalCode(postalCode);
				player.getAddress().setCountry(country);
			}
			player.setEmailAddress(emailAddress);
			player.setMobileNumber(mobileNumber);
			player.setSchoolName(schoolName);
			int playerId = bandyService.savePlayer(player);
			CustomLog.e(this.getClass(), "Saved: " + player.toString());
			return true;
		} catch (ApplicationException ae) {
			String errorMsg = "Failed saving player: " + ae.getMessage();
			Toast.makeText(getActivity().getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
			return false;
		}
	}
}
