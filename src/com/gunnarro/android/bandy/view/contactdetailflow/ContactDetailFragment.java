package com.gunnarro.android.bandy.view.contactdetailflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.party.Contact;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.view.dashboard.CommonFragment;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ContactListActivity} in two-pane mode (on tablets) or a
 * {@link ContactDetailActivity} on handsets.
 */
public class ContactDetailFragment extends CommonFragment {

	private BandyService bandyService;
	private int contactId;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ContactDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setHasOptionsMenu(true);
		if (getArguments().containsKey(DashboardActivity.ARG_CONTACT_ID)) {
			contactId = getArguments().getInt(DashboardActivity.ARG_CONTACT_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.contact_details_layout, container, false);
		if (this.bandyService == null) {
			this.bandyService = new BandyServiceImpl(rootView.getContext());
		}
		Contact contact = this.bandyService.getContact(contactId);
		// getActivity().setTitle(contact.getFullName());
		updateContactDetails(rootView, contact);
		return rootView;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.actionbar_menu_edit, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	/**
	 * Note that the parent Activity’s onOptionsItemSelected() method is called
	 * first. Your fragment’s method is called only, when the Activity didn’t
	 * consume the event! {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		CustomLog.e(this.getClass(), item.toString());
		// handle item selection
		switch (item.getItemId()) {
		// This is consumed in the parent activity
		// case R.id.action_edit:
		// return true;
		case R.id.action_delete:
			delete(contactId);
			Toast.makeText(getActivity().getApplicationContext(), "Deleted contact!", Toast.LENGTH_SHORT).show();
			super.getActivity().onBackPressed();
		default:
			return false;
		}
	}

	private void delete(Integer contactId) {
		this.bandyService.deleteContact(contactId);
	}

	private void updateContactDetails(View rootView, Contact contact) {
		if (contact != null) {
			((TextView) rootView.findViewById(R.id.contactFullNameTxt)).setText(contact.getFullName());
			if (contact.hasTeamRoles()) {
				((TextView) rootView.findViewById(R.id.contactRolesTxt)).setText(contact.getRoles().toString());
			}
			((TextView) rootView.findViewById(R.id.contactMobileTxt)).setText(contact.getMobileNumber());
			((TextView) rootView.findViewById(R.id.contactEmailTxt)).setText(contact.getEmailAddress());
			if (contact.getAddress() != null) {
				((TextView) rootView.findViewById(R.id.contactStreetTxt)).setText(contact.getAddress().getFullStreetName());
				((TextView) rootView.findViewById(R.id.contactCityTxt)).setText(contact.getAddress().getPostalCode() + " " + contact.getAddress().getCity());
				((TextView) rootView.findViewById(R.id.contactCountryTxt)).setText(contact.getAddress().getCountry());
			}
		}
	}

}
