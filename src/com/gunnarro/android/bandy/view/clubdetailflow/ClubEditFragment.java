package com.gunnarro.android.bandy.view.clubdetailflow;

import android.app.Activity;
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
import com.gunnarro.android.bandy.domain.view.list.Item;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.view.clubdetailflow.ClubListFragment.Callbacks;
import com.gunnarro.android.bandy.view.dashboard.CommonFragment;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;
import com.gunnarro.android.bandy.view.listener.ReloadListener;

public class ClubEditFragment extends CommonFragment {

	public boolean isInitMode = true;

	// Container Activity must implement this interface
	public interface CustomOnItemSelectedListener {
		public void onItemSelected(Item item);
	}

	private BandyService bandyService;
	private Integer clubId;
	private Club club;
	private Callbacks callback;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ClubEditFragment() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setHasOptionsMenu(true);
		if (getArguments().containsKey(DashboardActivity.ARG_CLUB_ID)) {
			clubId = getArguments().getInt(DashboardActivity.ARG_CLUB_ID);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.club_edit_layout, container, false);
		if (this.bandyService == null) {
			this.bandyService = new BandyServiceImpl(rootView.getContext());
		}

		if (clubId != null) {
			club = this.bandyService.getClub(clubId);
			init(rootView);
		}
		setupEventHandlers(rootView);
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
			callback.onItemSelected(1000);
			super.getActivity().onBackPressed();
			return true;
		case R.id.action_save:
			save();
			Toast.makeText(getActivity().getApplicationContext(), "Saved club! " +  getActivity().getLocalClassName(), Toast.LENGTH_SHORT).show();
			// Have to refresh the list in order to reflect changes
//			ReloadListener listener = (ReloadListener) getActivity().getSupportFragmentManager().findFragmentById(R.id.club_item_list_id);
//			listener.reloadData();
			callback.onItemSelected(9999);
			super.getActivity().onBackPressed();
			return true;
		default:
			return super.getActivity().onOptionsItemSelected(item);
		}
	}

	public void updateSelectedField(String value, int fieldId) {
		setInputValue(getView(), fieldId, value);
	}

	private void setupEventHandlers(View rootView) {
		// ImageButton leagueBtn = (ImageButton)
		// rootView.findViewById(R.id.selectLeagueBtn);
		// leagueBtn.setOnClickListener(new
		// SelectDialogOnClickListener(getFragmentManager(),
		// bandyService.getLeagueNames(), R.id.teamleagueNameTxt, false));
		//

	}

	private void init(View rootView) {
		if (club != null) {
			setInputValue(rootView, R.id.clubNameTxt, club.getName());
			setInputValue(rootView, R.id.clubDepartmentTxt, club.getDepartmentName());
		}
	}

	private void save() {
		String clubName = getInputValue(R.id.clubNameTxt);
		String department = getInputValue(R.id.clubDepartmentTxt);
		if (club == null) {
			club = new Club(clubName, department);
		}
		if (getInputValue(R.id.clubHomepageTxt) != null) {
			club.setHomePageUrl(getInputValue(R.id.clubHomepageTxt));
		}
		// club.setStadiumName(null);
		bandyService.saveClub(club);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDetach() {
		super.onDetach();
		// Reset the active callbacks interface to the dummy implementation.
		callback = ClubListFragment.sDummyCallbacks;
		CustomLog.e(this.getClass(), "tracing call...");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}
		callback = (Callbacks) activity;
		CustomLog.e(this.getClass(), "tracing call...activity: " + activity.getLocalClassName());
	}
}
