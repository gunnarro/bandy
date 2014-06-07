package com.gunnarro.android.bandy.view.refereedetailflow;

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
import com.gunnarro.android.bandy.domain.party.Referee;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.exception.ValidationException;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.view.dashboard.CommonFragment;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

public class RefereeEditFragment extends CommonFragment {

	private BandyService bandyService;
	private Integer refereeId;
	private Referee referee;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public RefereeEditFragment() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setHasOptionsMenu(true);
		if (getArguments().containsKey(DashboardActivity.ARG_REFEREE_ID)) {
			refereeId = getArguments().getInt(DashboardActivity.ARG_REFEREE_ID);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.referee_new_layout, container, false);
		if (this.bandyService == null) {
			this.bandyService = new BandyServiceImpl(rootView.getContext());
		}

		if (refereeId != null) {
			referee = this.bandyService.getReferee(refereeId);
			init(rootView);
		}
		setupEventhandlers(rootView);
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
			boolean isSaved = save();
			if (isSaved) {
				Toast.makeText(getActivity().getApplicationContext(), "Saved referee!", Toast.LENGTH_SHORT).show();
				super.getActivity().onBackPressed();
				return true;
			}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setupEventhandlers(View rootView) {
	}

	private void init(View rootView) {
		if (referee != null) {
			setInputValue(rootView, R.id.contactFirstNameTxt, referee.getFirstName());
			setInputValue(rootView, R.id.contactMiddleNameTxt, referee.getMiddleName());
			setInputValue(rootView, R.id.contactLastNameTxt, referee.getLastName());
			setInputValue(rootView, R.id.contactMobileNumberTxt, referee.getMobileNumber());
			setInputValue(rootView, R.id.contactEmailAddressTxt, referee.getEmailAddress());
			setGender(rootView, referee.getGender());
			if (referee.getAddress() != null) {
				setInputValue(rootView, R.id.contactStreetNameTxt, referee.getAddress().getStreetName());
				setInputValue(rootView, R.id.contactStreetNumberTxt, referee.getAddress().getStreetNumber());
				setInputValue(rootView, R.id.contactStreetNumberPostfixTxt, referee.getAddress().getStreetNumberPrefix());
				setInputValue(rootView, R.id.contactPostalCodeTxt, referee.getAddress().getPostalCode());
				setInputValue(rootView, R.id.contactCityTxt, referee.getAddress().getCity());
				setInputValue(rootView, R.id.contactCountryTxt, referee.getAddress().getCountry());
			}
		} else {
			setGender(rootView, "Male");
		}
	}

	private boolean save() {
		try {
			String firstName = getInputValue(R.id.contactFirstNameTxt, true);
			String middleName = getInputValue(R.id.contactMiddleNameTxt, false);
			String lastName = getInputValue(R.id.contactLastNameTxt, true);
			String streetName = getInputValue(R.id.contactStreetNameTxt, false);
			String streetNumber = getInputValue(R.id.contactStreetNumberTxt, false);
			String streetNumberPostfix = getInputValue(R.id.contactStreetNumberPostfixTxt, false);
			String postalCode = getInputValue(R.id.contactPostalCodeTxt, false);
			String city = getInputValue(R.id.contactCityTxt, false);
			String country = getInputValue(R.id.contactCountryTxt, false);
			String mobileNumber = getInputValue(R.id.contactMobileNumberTxt, false);
			String emailAddress = getInputValue(R.id.contactEmailAddressTxt, false);

			if (referee == null) {
				Team team = this.bandyService.getTeam(1);
				Address address = new Address(streetName, streetNumber, streetNumberPostfix, postalCode, city, country);
				referee = new Referee(firstName, middleName, lastName);
			} else {
				referee.setFirstName(firstName);
				referee.setMiddleName(middleName);
				referee.setLastName(lastName);
				referee.setGender(getSelectedGender());
				referee.getAddress().setStreetName(streetName);
				referee.getAddress().setStreetNumber(streetNumber);
				referee.getAddress().setStreetNumberPrefix(streetNumberPostfix);
				referee.getAddress().setCity(city);
				referee.getAddress().setPostalCode(postalCode);
				referee.getAddress().setCountry(country);
			}
			referee.setEmailAddress(emailAddress);
			referee.setMobileNumber(mobileNumber);
			int refereeId = bandyService.saveReferee(referee);
			CustomLog.e(this.getClass(), referee.toString());
			return true;
		} catch (ValidationException ve) {
			CustomLog.e(this.getClass(), ve.toString());
			return false;
		}
	}

}
