package com.gunnarro.android.bandy.view.contactdetailflow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.party.Address;
import com.gunnarro.android.bandy.domain.party.Contact;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

public class ContactEditFragment extends Fragment {

	private BandyService bandyService;
	private String teamName;
	private Integer contactId;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ContactEditFragment() {
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
		if (getArguments().containsKey(DashboardActivity.ARG_CONTACT_ID)) {
			contactId = getArguments().getInt(DashboardActivity.ARG_CONTACT_ID);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.contact_new_layout, container, false);
		if (this.bandyService == null) {
			this.bandyService = new BandyServiceImpl(rootView.getContext());
		}

		if (contactId != null) {
			Contact contact = this.bandyService.getContact(contactId);
			init(rootView, contact);
			getActivity().getActionBar().setSubtitle(contact.getFullName());
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

	private void init(View rootView, Contact contact) {
		if (contact != null) {
			setInputValue(rootView, R.id.contactFirstNameTxt, contact.getFirstName());
			setInputValue(rootView, R.id.contactMiddleNameTxt, contact.getMiddleName());
			setInputValue(rootView, R.id.contactLastNameTxt, contact.getLastName());
			setInputValue(rootView, R.id.contactMobileNumberTxt, contact.getMobileNumber());
			setInputValue(rootView, R.id.contactEmailAddressTxt, contact.getEmailAddress());
			if (contact.getAddress() != null) {
				setInputValue(rootView, R.id.contactStreetNameTxt, contact.getAddress().getStreetName());
				setInputValue(rootView, R.id.contactStreetNumberTxt, contact.getAddress().getStreetNumber());
				setInputValue(rootView, R.id.contactStreetNumberPostfixTxt, contact.getAddress().getStreetNumberPrefix());
				setInputValue(rootView, R.id.contactPostalCodeTxt, contact.getAddress().getPostalCode());
				setInputValue(rootView, R.id.contactCityTxt, contact.getAddress().getCity());
				setInputValue(rootView, R.id.contactCountryTxt, contact.getAddress().getCountry());
			}
		}
	}

	private void save() {
		String firstName = getInputValue(R.id.contactFirstNameTxt);
		String middleName = getInputValue(R.id.contactMiddleNameTxt);
		String lastName = getInputValue(R.id.contactLastNameTxt);
		String streetName = getInputValue(R.id.contactStreetNameTxt);
		String streetNumber = getInputValue(R.id.contactStreetNumberTxt);
		String streetNumberPostfix = getInputValue(R.id.contactStreetNumberPostfixTxt);
		String postalCode = getInputValue(R.id.contactPostalCodeTxt);
		String city = getInputValue(R.id.contactCityTxt);
		String country = getInputValue(R.id.contactCountryTxt);
		String mobileNumber = getInputValue(R.id.contactMobileNumberTxt);
		String emailAddress = getInputValue(R.id.contactEmailAddressTxt);

		String gender = "M";
		boolean isFemale = ((RadioButton) getView().findViewById(R.id.femaleRadioBtn)).isSelected();
		if (isFemale) {
			gender = "F";
		}

		Address address = new Address(streetName, streetNumber, streetNumberPostfix, postalCode, city, country);
		Team team = this.bandyService.getTeam(1);
		Contact contact = new Contact(team, firstName, middleName, lastName, gender, address);
		if (contactId > 0) {
			contact = new Contact(contactId, team, firstName, middleName, lastName, gender, address);
		}
		contact.setEmailAddress(emailAddress);
		contact.setMobileNumber(mobileNumber);
		int trainingId = bandyService.saveContact(contact);
		CustomLog.e(this.getClass(), contact.toString());
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
