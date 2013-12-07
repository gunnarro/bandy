package com.gunnarro.android.bandy.repository;

import java.util.List;

import android.database.SQLException;

import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.domain.Contact;
import com.gunnarro.android.bandy.domain.Cup;
import com.gunnarro.android.bandy.domain.Match;
import com.gunnarro.android.bandy.domain.Player;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.Training;

public interface BandyRepository {

	/**
	 * The repository should be opened by the activity that use the repository.
	 * This should be done in the onCreate() and onResume() methods of the
	 * activity.
	 * 
	 * @throws SQLException
	 */
	public void open() throws SQLException;

	/**
	 * The repository should be closed by the activity that use the repository.
	 * This should be done in the onPause() method of the activity.
	 * 
	 * @throws SQLException
	 */
	public void close();

	public boolean createClub(Club club);

	public boolean createTeam(Team team);

	public boolean createMatch(Match match);

	public boolean createCup(Cup cup);

	public boolean createTraining(Training training);

	public void createPlayer(Player player);

	public boolean createContact(Contact contact);

	public Team getTeam(String name);

	public Team getTeam(Integer id);

	public Club getClub(String name);

	public Club getClub(Integer id);

	public List<Match> getMatchList(Integer teamId, String periode);

	public List<Training> getTrainingList(Integer teamId, String periode);

	public List<Cup> getCupList(Integer teamId, String periode);

	public List<Player> getPlayerList(String teamName);

	public List<Contact> getContactList(Integer teamId, String role);

	Contact getContact(String firstName, String lastName);

	// ---------------------------------------------------------------------------
	// Settings table operations
	// ---------------------------------------------------------------------------
	public void updateDataFileUrl(String url);

	public String getDataFileUrl();

	public void updateDataFileVersion(String version);

	public String getDataFileVersion();

	public void updateDataFileLastUpdated(long lastUpdatedTime);

	public long getDataFileLastUpdated();



}