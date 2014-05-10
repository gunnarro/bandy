package com.gunnarro.android.bandy.view.clubdetailflow;

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
import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.view.dashboard.CommonFragment;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;
import com.gunnarro.android.bandy.view.teamdetailflow.TeamListActivity;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link TeamListActivity} in two-pane mode (on tablets) or a
 * {@link ClubDetailActivity} on handsets.
 */
public class ClubDetailFragment extends CommonFragment {

	private BandyService bandyService;
	private int clubId;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ClubDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setHasOptionsMenu(true);
		if (getArguments().containsKey(DashboardActivity.ARG_CLUB_ID)) {
			clubId = getArguments().getInt(DashboardActivity.ARG_CLUB_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.club_details_layout, container, false);
		if (this.bandyService == null) {
			this.bandyService = new BandyServiceImpl(rootView.getContext());
		}
		Club club = this.bandyService.getClub(clubId);
		getActivity().setTitle(club.getFullName());
		updateClubDetails(rootView, club);
		setupEventHandlers(rootView);
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
			delete(clubId);
			Toast.makeText(getActivity().getApplicationContext(), "Deleted club!", Toast.LENGTH_SHORT).show();
			super.getActivity().onBackPressed();
		default:
			return false;
		}
	}

	private void delete(Integer clubId) {
		this.bandyService.deleteClub(clubId);
	}

	private void setupEventHandlers(View view) {
	}

	private void updateClubDetails(View rootView, Club club) {
		if (club != null) {
			setTextViewValue(rootView, R.id.clubNameTxt, club.getFullName());
			// setInputValue(rootView, R.id.streetNameTxt,
			// club.getAddress().getFullStreetName(), false);
			// setInputValue(rootView, R.id.postalCodeTxt,
			// club.getAddress().getPostalCode(), false);
			// setInputValue(rootView, R.id.cityTxt,
			// club.getAddress().getCity(), false);
			// setInputValue(rootView, R.id.countryTxt,
			// club.getAddress().getCountry(), false);
		}
	}

}
