package com.gunnarro.android.bandy.view.teamdetailflow;

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
import android.widget.ImageButton;
import android.widget.Toast;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.domain.League;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.party.Contact;
import com.gunnarro.android.bandy.domain.view.list.Item;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.exception.ApplicationException;
import com.gunnarro.android.bandy.service.exception.ValidationException;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl.SelectionListType;
import com.gunnarro.android.bandy.utility.Validator;
import com.gunnarro.android.bandy.view.dashboard.CommonFragment;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;
import com.gunnarro.android.bandy.view.dialog.SelectDialogOnClickListener;

public class TeamEditFragment extends CommonFragment {

	public boolean isInitMode = true;

	// Container Activity must implement this interface
	public interface CustomOnItemSelectedListener {
		public void onItemSelected(Item item);
	}

	private BandyService bandyService;
	private Integer teamId;
	private Team team;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TeamEditFragment() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setHasOptionsMenu(true);
		if (getArguments().containsKey(DashboardActivity.ARG_TEAM_ID)) {
			teamId = getArguments().getInt(DashboardActivity.ARG_TEAM_ID);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.team_new_layout, container, false);
		if (this.bandyService == null) {
			this.bandyService = new BandyServiceImpl(rootView.getContext());
		}

		if (teamId != null) {
			team = this.bandyService.getTeam(teamId);
			init(rootView);
			getActivity().getActionBar().setTitle(team.getClub().getName());
			getActivity().getActionBar().setSubtitle(team.getClub().getDepartmentName());
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
			super.getActivity().setResult(TeamListActivity.RESULT_CODE_TEAM_UNCHANGED);
			super.getActivity().onBackPressed();
			return true;
		case R.id.action_save:
			boolean isSaved = save();
			if (isSaved) {
				Toast.makeText(getActivity().getApplicationContext(), "Saved Team!", Toast.LENGTH_SHORT).show();
				super.getActivity().setResult(TeamListActivity.RESULT_CODE_TEAM_CHANGED);
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
		SelectDialogOnClickListener.turnOnInitMode();
		int tmpTeamId = teamId == null ? -1 : teamId;
		ImageButton leagueBtn = (ImageButton) rootView.findViewById(R.id.selectLeagueBtn);
		leagueBtn.setOnClickListener(new SelectDialogOnClickListener(getFragmentManager(), bandyService.getLeagueNames(), R.id.teamleagueNameTxt, false));

		ImageButton teamleaderBtn = (ImageButton) rootView.findViewById(R.id.selectTeamleaderBtn);
		teamleaderBtn.setOnClickListener(new SelectDialogOnClickListener(getFragmentManager(), bandyService.getContactNames(tmpTeamId), R.id.teamTeamleaderTxt,
				false));

		ImageButton coachBtn = (ImageButton) rootView.findViewById(R.id.selectCoachBtn);
		coachBtn.setOnClickListener(new SelectDialogOnClickListener(getFragmentManager(), bandyService.getContactNames(tmpTeamId), R.id.teamCoachTxt, false));

		ImageButton playersBtn = (ImageButton) rootView.findViewById(R.id.selectPlayersBtn);
		playersBtn.setOnClickListener(new SelectDialogOnClickListener(getFragmentManager(), bandyService, SelectionListType.PLAYER_NAMES, tmpTeamId,
				R.id.teamCoachTxt, true));

		SelectDialogOnClickListener.turnOffInitMode();

		// Input validation
		final EditText nameTxt = (EditText) rootView.findViewById(R.id.teamNameTxt);
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
	}

	private void init(View rootView) {
		if (team != null) {
			setInputValue(rootView, R.id.teamNameTxt, team.getName());
			setInputValue(rootView, R.id.teamYearOfBirthTxt, team.getTeamYearOfBirth().toString());
			setGender(rootView, team.getGender());
			if (team.getLeague() != null) {
				setInputValue(rootView, R.id.teamleagueNameTxt, team.getLeague().getName());
			}
			if (team.getTeamLead() != null) {
				setInputValue(rootView, R.id.teamTeamleaderTxt, team.getTeamLead().getFullName());
			}
			if (team.getCoach() != null) {
				setInputValue(rootView, R.id.teamCoachTxt, team.getCoach().getFullName());
			}
			if (team.getPlayerItemList() != null) {
				setInputValue(rootView, R.id.teamPlayersTxt, Integer.toBinaryString(team.getPlayerItemList().size()));
			}
		}
	}

	private boolean save() {
		try {
			String teamName = getInputValue(R.id.teamNameTxt, true);
			String teamYearOfBirth = getInputValue(R.id.teamYearOfBirthTxt, false);

			Club club = this.bandyService.getClub(clubName, "%");
			if (club == null) {
				throw new RuntimeException("Club is not found!");
			}
			if (this.team == null) {
				this.team = new Team(teamName, club, Integer.parseInt(teamYearOfBirth), getSelectedGender());
			} else {
				this.team.setName(teamName);
				this.team.setTeamYearOfBirth(Integer.parseInt(teamYearOfBirth));
			}
			this.team.setGender(getSelectedGender());
		} catch (ValidationException ve) {
			CustomLog.e(this.getClass(), ve.getMessage());
			return false;
		}

		// Check if team name already exist
		try {
			this.bandyService.getTeam(this.team.getName(), false);
			Validator.setErrorMessage(getEditText(R.id.teamNameTxt), "Team name already exist!");
			return false;
		} catch (ValidationException ve) {
			// Ignore, team name do not exist in the DB.
		}

		try {
			String selectedLeagueName = getInputValue(R.id.teamleagueNameTxt, false);
			if (selectedLeagueName != null) {
				if (this.team.getLeague() == null || !this.team.getLeague().getName().equalsIgnoreCase(selectedLeagueName)) {
					League league = this.bandyService.getLeague(selectedLeagueName);
					this.team.setLeague(league);
				}
			}

			Contact newTeamleader = null;
			String selectedTeamleaderName = getInputValue(R.id.teamTeamleaderTxt, false);
			if (selectedTeamleaderName != null) {
				if (this.team.getTeamLead() == null) {
					this.team.setTeamLead(Contact.createContact(selectedTeamleaderName));
				} else if (!this.team.getTeamLead().getFullName().equalsIgnoreCase(selectedTeamleaderName)) {
					newTeamleader = Contact.createContact(selectedTeamleaderName);
				}
			}

			Contact newCoach = null;
			String selectedCoachName = getInputValue(R.id.teamCoachTxt, false);
			if (selectedCoachName != null) {
				if (this.team.getCoach() == null) {
					this.team.setCoach(Contact.createContact(selectedCoachName));
				} else if (!this.team.getCoach().getFullName().equalsIgnoreCase(selectedCoachName)) {
					newCoach = Contact.createContact(selectedCoachName);
				}
			}
			this.bandyService.saveTeam(this.team, newTeamleader, newCoach);
			return true;
		} catch (ApplicationException ae) {
			String errorMsg = "Failed creating new Team: " + ae.getMessage();
			Toast.makeText(getActivity().getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
			return false;
		}
	}

}
