package com.gunnarro.android.bandy.view.teamdetailflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
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
			super.getActivity().onBackPressed();
			return true;
		case R.id.action_save:
			save();
			Toast.makeText(getActivity().getApplicationContext(), "Saved Team!", Toast.LENGTH_SHORT).show();
			super.getActivity().onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void updateSelectedField(String value, int fieldId) {
		setInputValue(getView(), fieldId, value);
	}

	private void setupEventHandlers(View rootView) {
		ImageButton leagueBtn = (ImageButton) rootView.findViewById(R.id.selectLeagueBtn);
		leagueBtn.setOnClickListener(new SelectDialogOnClickListener(getFragmentManager(), bandyService.getLeagueNames(), R.id.teamleagueNameTxt, false));

		ImageButton teamleaderBtn = (ImageButton) rootView.findViewById(R.id.selectTeamleaderBtn);
		teamleaderBtn.setOnClickListener(new SelectDialogOnClickListener(getFragmentManager(), bandyService.getContactNames(teamId == null ? -1 : teamId),
				R.id.teamTeamleaderTxt, false));

		ImageButton coachBtn = (ImageButton) rootView.findViewById(R.id.selectCoachBtn);
		coachBtn.setOnClickListener(new SelectDialogOnClickListener(getFragmentManager(), bandyService.getContactNames(teamId == null ? -1 : teamId),
				R.id.teamCoachTxt, false));
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
		}
	}

	private void save() {
		String teamName = getInputValue(R.id.teamNameTxt);
		String teamYearOfBirth = getInputValue(R.id.teamYearOfBirthTxt);

		Club club = bandyService.getClub("Ulle%", "Bandy");
		if (club == null) {
			throw new RuntimeException("Club is not found!");
		}
		if (team == null) {
			team = new Team(teamName, club, Integer.parseInt(teamYearOfBirth), getSelectedGender());
		} else {
			team.setName(teamName);
			team.setTeamYearOfBirth(Integer.parseInt(teamYearOfBirth));
		}
		team.setGender(getSelectedGender());

		String selectedLeagueName = getInputValue(R.id.teamleagueNameTxt);
		if (selectedLeagueName != null) {
			if (team.getLeague() == null || !team.getLeague().getName().equalsIgnoreCase(selectedLeagueName)) {
				League league = bandyService.getLeague(selectedLeagueName);
				team.setLeague(league);
			}
		}

		Contact newTeamleader = null;
		String selectedTeamleaderName = getInputValue(R.id.teamTeamleaderTxt);
		if (selectedTeamleaderName != null) {
			if (team.getTeamLead() == null) {
				team.setTeamLead(Contact.createContact(selectedTeamleaderName));
			} else if (!team.getTeamLead().getFullName().equalsIgnoreCase(selectedTeamleaderName)) {
				newTeamleader = Contact.createContact(selectedTeamleaderName);
			}
		}

		Contact newCoach = null;
		String selectedCoachName = getInputValue(R.id.teamCoachTxt);
		if (selectedCoachName != null) {
			if (team.getCoach() == null) {
				team.setCoach(Contact.createContact(selectedCoachName));
			} else if (!team.getCoach().getFullName().equalsIgnoreCase(selectedCoachName)) {
				newCoach = Contact.createContact(selectedCoachName);
			}
		}
		int id = bandyService.saveTeam(team, newTeamleader, newCoach);
	}

}
