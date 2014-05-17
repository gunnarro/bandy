package com.gunnarro.android.bandy.view.clubdetailflow;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.domain.view.list.Item;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.exception.ApplicationException;
import com.gunnarro.android.bandy.service.exception.ValidationException;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.utility.Validator;
import com.gunnarro.android.bandy.view.dashboard.CommonFragment;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

public class ClubEditFragment extends CommonFragment {

	public boolean isInitMode = true;

	// Container Activity must implement this interface
	public interface CustomOnItemSelectedListener {
		public void onItemSelected(Item item);
	}

	private BandyService bandyService;
	private Integer clubId;
	private Club club;

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
			super.getActivity().setResult(ClubListActivity.RESULT_CODE_CLUB_UNCHANGED);
			super.getActivity().onBackPressed();
			return true;
		case R.id.action_save:
			boolean isSaved = save();
			if (isSaved) {
				Toast.makeText(getActivity().getApplicationContext(), "Saved club! " + getActivity().getLocalClassName(), Toast.LENGTH_SHORT).show();
				super.getActivity().setResult(ClubListActivity.RESULT_CODE_CLUB_CHANGED);
				super.getActivity().onBackPressed();
				return true;
			}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void updateSelectedField(String value, int fieldId) {
		setInputValue(getView(), fieldId, value);
	}

	private void setupEventHandlers(View rootView) {
		// Input validation
		final EditText nameTxt = (EditText) rootView.findViewById(R.id.clubNameTxt);
		// TextWatcher would let us check validation error on the fly
		nameTxt.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				Validator.hasText(nameTxt);
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});

		final EditText departmentTxt = (EditText) rootView.findViewById(R.id.clubDepartmentTxt);
		// TextWatcher would let us check validation error on the fly
		departmentTxt.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				Validator.hasText(nameTxt);
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});
	}

	private void init(View rootView) {
		if (club != null) {
			setInputValue(rootView, R.id.clubNameTxt, club.getName());
			setInputValue(rootView, R.id.clubDepartmentTxt, club.getDepartmentName());
		}
	}

	private boolean save() {
		String newClubName = null;
		String newDepartmentName = null;
		try {
			newClubName = getInputValue(R.id.clubNameTxt, true);
			newDepartmentName = getInputValue(R.id.clubDepartmentTxt, true);
		} catch (ValidationException ve) {
			CustomLog.e(this.getClass(), ve.getMessage());
			return false;
		}

		if (club == null) {
			club = new Club(newClubName, newDepartmentName);
		} else {
			club.setName(newClubName);
			club.setDepartmentName(newDepartmentName);
		}

		if (getInputValue(R.id.clubHomepageTxt, false) != null) {
			club.setHomePageUrl(getInputValue(R.id.clubHomepageTxt, false));
		}
		// club.setStadiumName(null);
		try {
			bandyService.saveClub(club);
			return true;
		} catch (ApplicationException ae) {
			String errorMsg = "Failed creating new Club: " + ae.getMessage();
			Toast.makeText(getActivity().getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
			return false;
		}
	}

}
