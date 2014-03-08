package com.gunnarro.android.bandy.repository;

import java.util.List;

import android.database.SQLException;

import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.domain.SearchResult;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Cup;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.activity.Season;
import com.gunnarro.android.bandy.domain.activity.Training;
import com.gunnarro.android.bandy.domain.party.Address;
import com.gunnarro.android.bandy.domain.party.Contact;
import com.gunnarro.android.bandy.domain.party.Player;
import com.gunnarro.android.bandy.domain.party.Role;
import com.gunnarro.android.bandy.domain.statistic.Statistic;
import com.gunnarro.android.bandy.domain.view.list.Item;
import com.gunnarro.android.bandy.repository.impl.BandyRepositoryImpl.PlayerLinkTableTypeEnum;

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

	public void deleteAllTableData();

	public int createClub(Club club);

	public int createSeason(Season season);

	public int createTeam(Team team);

	public int createMatch(Match match);

	public int createCupMatchLnk(int cupId, int matchId);

	public int createCup(Cup cup);

	public int createTraining(Training training);

	public int createPlayer(Player player);

	public int createContact(Contact contact);

	public long createAddress(Address address);

	public String[] getTeamNames(String clubName);

	public Team getTeam(String name);

	public Team getTeam(Integer id);

	public Contact getTeamContactPerson(int teamId, String role);

	public Club getClub(String name);

	public Club getClub(Integer id);

	// ---------------------------------------------------------------------------
	// Match table operations
	// ---------------------------------------------------------------------------

	public Match getMatch(int matchId);

	public List<Match> getMatchList(Integer teamId, Integer periode);

	public List<Item> getMatchPlayerList(int teamId, int matchId);

	public List<Training> getTrainingList(Integer teamId, Integer periode);

	public List<Cup> getCupList(Integer teamId, Integer periode);

	public Season getCurrentSeason();

	public Season getSeason(Integer seasonId);

	public Season getSeason(String period);

	public List<Season> getSeasonList();

	public List<Player> getPlayerList(Integer teamId);

	public List<Item> getPlayersAsItemList(int teamId);

	public List<Contact> getContactList(Integer teamId, String role);

	public Contact getContact(String firstName, String lastName);

	public Player getPlayer(Integer playerId);

	public Player lookupPlayer(String mobileNr);

	public Player lookupPlayerThroughContact(String mobileNr);

	public int lookupMatchId(Integer id, String startDate);

	// ---------------------------------------------------------------------------
	// link table operations
	// ---------------------------------------------------------------------------

	public void createPlayerLink(PlayerLinkTableTypeEnum type, int playerId, int id);

	public void deletePlayerLink(PlayerLinkTableTypeEnum type, int playerId, int id);

	// ---------------------------------------------------------------------------
	// Settings table operations
	// ---------------------------------------------------------------------------

	public String getSetting(String type);

	public void updateSetting(String type, String value);

	public List<Role> getRoleList();

	void listRelationsShips();

	public SearchResult search(String sqlQuery);

	// ---------------------------------------------------------------------------
	// Statistic table operations
	// ---------------------------------------------------------------------------
	public Statistic getPlayerStatistic(int teamId, int playerId, int seasonId);

	public Statistic getTeamStatistic(int teamId, int seasonId);

	public Training getTrainingByDate(int teamId, long startTime);

	public int getNumberOfSignedPlayers(PlayerLinkTableTypeEnum type, int id);

	public String getSqlQuery(String id, String type);

	public String[] getRefereeNames();

	public String[] getMatchTypes();

	public String[] getSeasonPeriodes();

	public void changePlayerStatus(Integer playerId, String status);

	public String[] getPlayerStatusTypes();

}