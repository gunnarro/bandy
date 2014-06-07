package com.gunnarro.android.bandy.view.refereedetailflow;

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
import com.gunnarro.android.bandy.domain.party.Referee;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.view.dashboard.CommonFragment;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link RefereeListActivity} in two-pane mode (on tablets) or a
 * {@link RefereeDetailActivity} on handsets.
 */
public class RefereeDetailFragment extends CommonFragment {

	private BandyService bandyService;
	private int refereeId;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public RefereeDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setHasOptionsMenu(true);
		if (getArguments().containsKey(DashboardActivity.ARG_REFEREE_ID)) {
			refereeId = getArguments().getInt(DashboardActivity.ARG_REFEREE_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.referee_details_layout, container, false);
		if (this.bandyService == null) {
			this.bandyService = new BandyServiceImpl(rootView.getContext());
		}
		Referee referee = this.bandyService.getReferee(refereeId);
		// getActivity().setTitle(contact.getFullName());
		updateRefereeDetails(rootView, referee);
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
			delete(refereeId);
			Toast.makeText(getActivity().getApplicationContext(), "Deleted referee!", Toast.LENGTH_SHORT).show();
			super.getActivity().onBackPressed();
		default:
			return false;
		}
	}

	private void delete(Integer refereeId) {
		this.bandyService.deleteReferee(refereeId);
	}

	private void updateRefereeDetails(View rootView, Referee referee) {
		if (referee != null) {
			((TextView) rootView.findViewById(R.id.contactFullNameTxt)).setText(referee.getFullName());
			((TextView) rootView.findViewById(R.id.contactMobileTxt)).setText(referee.getMobileNumber());
			((TextView) rootView.findViewById(R.id.contactEmailTxt)).setText(referee.getEmailAddress());
			if (referee.getAddress() != null) {
				((TextView) rootView.findViewById(R.id.contactStreetTxt)).setText(referee.getAddress().getFullStreetName());
				((TextView) rootView.findViewById(R.id.contactCityTxt)).setText(referee.getAddress().getPostalCode() + " " + referee.getAddress().getCity());
				((TextView) rootView.findViewById(R.id.contactCountryTxt)).setText(referee.getAddress().getCountry());
			}
		}
	}

}
