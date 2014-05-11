package com.gunnarro.android.bandy.view.contactdetailflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.party.Address;
import com.gunnarro.android.bandy.domain.party.Contact;
import com.gunnarro.android.bandy.domain.party.Role.RoleTypesEnum;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.exception.ValidationException;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.view.dashboard.CommonFragment;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

public class ContactEditFragment extends CommonFragment {

	private BandyService bandyService;
	private Integer contactId;
	private Contact contact;
	private String selectedRoleName;

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
			contact = this.bandyService.getContact(contactId);
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
				Toast.makeText(getActivity().getApplicationContext(), "Saved contact!", Toast.LENGTH_SHORT).show();
				super.getActivity().onBackPressed();
				return true;
			}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setupEventhandlers(View rootView) {
		String[] activeRoles = bandyService.getRoleTypeNames();
		Spinner roleSpinner = (Spinner) rootView.findViewById(R.id.contactRoleSpinner);
		ArrayAdapter<CharSequence> roleAdapter = new ArrayAdapter<CharSequence>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item,
				activeRoles);
		roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		roleSpinner.setAdapter(roleAdapter);
		roleSpinner.setOnItemSelectedListener(new RoleOnItemSelectedListener());
	}

	private void init(View rootView) {
		if (contact != null) {
			setInputValue(rootView, R.id.contactFirstNameTxt, contact.getFirstName());
			setInputValue(rootView, R.id.contactMiddleNameTxt, contact.getMiddleName());
			setInputValue(rootView, R.id.contactLastNameTxt, contact.getLastName());
			setInputValue(rootView, R.id.contactMobileNumberTxt, contact.getMobileNumber());
			setInputValue(rootView, R.id.contactEmailAddressTxt, contact.getEmailAddress());
			setGender(rootView, contact.getGender());
			if (contact.getAddress() != null) {
				setInputValue(rootView, R.id.contactStreetNameTxt, contact.getAddress().getStreetName());
				setInputValue(rootView, R.id.contactStreetNumberTxt, contact.getAddress().getStreetNumber());
				setInputValue(rootView, R.id.contactStreetNumberPostfixTxt, contact.getAddress().getStreetNumberPrefix());
				setInputValue(rootView, R.id.contactPostalCodeTxt, contact.getAddress().getPostalCode());
				setInputValue(rootView, R.id.contactCityTxt, contact.getAddress().getCity());
				setInputValue(rootView, R.id.contactCountryTxt, contact.getAddress().getCountry());
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

			if (contact == null) {
				Team team = this.bandyService.getTeam(1);
				Address address = new Address(streetName, streetNumber, streetNumberPostfix, postalCode, city, country);
				contact = new Contact(team, firstName, middleName, lastName, getSelectedGender(), address);
			} else {
				contact.setFirstName(firstName);
				contact.setMiddleName(middleName);
				contact.setLastName(lastName);
				contact.setGender(getSelectedGender());
				contact.getAddress().setStreetName(streetName);
				contact.getAddress().setStreetNumber(streetNumber);
				contact.getAddress().setStreetNumberPrefix(streetNumberPostfix);
				contact.getAddress().setCity(city);
				contact.getAddress().setPostalCode(postalCode);
				contact.getAddress().setCountry(country);
				contact.addRole(RoleTypesEnum.valueOf(selectedRoleName));
			}
			contact.setEmailAddress(emailAddress);
			contact.setMobileNumber(mobileNumber);
			int contactId = bandyService.saveContact(contact);
			CustomLog.e(this.getClass(), contact.toString());
			return true;
		} catch (ValidationException ve) {
			CustomLog.e(this.getClass(), ve.toString());
			return false;
		}
	}

	/**
	 * 
	 */
	public class RoleOnItemSelectedListener implements OnItemSelectedListener {

		public RoleOnItemSelectedListener() {
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			selectedRoleName = parent.getItemAtPosition(pos).toString();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	}

}
