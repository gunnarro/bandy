package com.gunnarro.android.bandy.repository;

import java.util.List;

import android.database.SQLException;

import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.domain.League;
import com.gunnarro.android.bandy.domain.SearchResult;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Cup;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.activity.MatchEvent;
import com.gunnarro.android.bandy.domain.activity.Season;
import com.gunnarro.android.bandy.domain.activity.Status;
import com.gunnarro.android.bandy.domain.activity.Tournament;
import com.gunnarro.android.bandy.domain.activity.Training;
import com.gunnarro.android.bandy.domain.party.Address;
import com.gunnarro.android.bandy.domain.party.Contact;
import com.gunnarro.android.bandy.domain.party.Player;
import com.gunnarro.android.bandy.domain.party.Referee;
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

	public boolean setForeignKeyConstraintsEnabled(boolean enable);

	public String getDBFileName();

	public String getDBUserVersion();

	public String getDBEncoding();

	public void createView();

	public void deleteAllTableData();

	public int createClub(Club club);

	public int deleteClub(Integer clubId);

	public int createSeason(Season season);

	public int createTeam(Team team);

	public int createMatch(Match match);

	public int createCupMatchLnk(int cupId, int matchId);

	public int createCup(Cup cup);

	public int createTraining(Training training);

	public int createPlayer(Player player);

	public int createContact(Contact contact);

	public long createAddress(Address address);

	public int createReferee(Referee referee);

	public Referee getReferee(int refereeId);

	public String[] getRefereeNames();

	public String[] getTeamNames(String clubName);

	public String[] getRoleTypeNames();

	public Team getTeam(String name);

	public Team getTeam(Integer id);

	public Contact getTeamContactPerson(int teamId, String role);

	public Item[] getClubAsItems();

	public String[] getClubNames();

	public Club getClub(String name, String departmentName);

	public Club getClub(Integer id);

	public Address getAddress(int addressId);

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

	public List<Item> getContactsAsItemList(Integer teamId);

	public List<Contact> getContactList(Integer clubId);

	public List<Contact> getContactList(Integer teamId, String role);

	public Contact getContact(String firstName, String lastName);

	public Contact getContact(Integer contactId);

	public Player getPlayer(Integer playerId);

	public Player lookupPlayer(String mobileNr);

	public Player lookupPlayerThroughContact(String mobileNr);

	public int lookupMatchId(Integer id, String startDate);

	// ---------------------------------------------------------------------------
	// link table operations
	// ---------------------------------------------------------------------------

	public void createPlayerLink(PlayerLinkTableTypeEnum type, int playerId, int id);

	public int deletePlayerLink(PlayerLinkTableTypeEnum type, Integer playerId, Integer id);

	public long createContactRoleTypeLnk(int contactId, int roleTypeIdId);

	public int deleteContactRoleTypeLnk(Integer contactId, Integer roleTypeIdId);

	// ---------------------------------------------------------------------------
	// Settings table operations
	// ---------------------------------------------------------------------------

	public String getSetting(String type);

	public List<String> getSettings(String type);

	public void updateSetting(String type, String value);

	public SearchResult search(String sqlQuery);

	// ---------------------------------------------------------------------------
	// Statistic table operations
	// ---------------------------------------------------------------------------
	public Statistic getPlayerStatistic(int teamId, int playerId, int seasonId);

	public Statistic getTeamStatistic(int teamId, int seasonId);

	public Training getTrainingByDate(int teamId, long startTime);

	public int getNumberOfSignedPlayers(PlayerLinkTableTypeEnum type, int id);

	public String getSqlQuery(String id, String type);

	public Item[] getMatchTypes();

	public String[] getSeasonPeriodes();

	public void changePlayerStatus(Integer playerId, String status);

	public String[] getPlayerStatusTypes();

	public int updatePlayer(Player player);

	public int updateMatch(Match match);

	public int updateMatchStatus(Integer matchId, Integer statusId);

	public int deletePlayer(Integer playerId);

	public int deleteMatch(Integer matchId);

	public int deleteMatchEvents(Integer matchId);

	public int deleteTraining(Integer trainingId);

	public int deleteContact(Integer contactId);

	public int deleteReferee(Integer refereeId);

	public int updateContact(Contact contact);

	public int updateAddress(Address address);

	public int updateTeam(Team team);

	public List<Team> getTeamList(String clubName);

	public String[] getLeagueNames();

	public League getLeague(String name);

	public void updateGoals(Integer matchId, Integer goals, boolean isHomeTeam);

	public Item[] getMatchStatusList();

	public int createMatchEvent(MatchEvent matchEvent);

	public List<MatchEvent> getMatchEventList(Integer matchId);

	public int deleteTeam(Integer teamId);

	public List<Club> getClubList();

	public String[] getTeamNames(Integer clubId);

	public Training getTraining(int id);

	public void registrerRefereeForMatch(int refereeId, int matchId);

	public Status getMatchStatus(Integer statusName);

	public int deleteAddress(Integer addressId);

	public String getSqliteVersion();

	public String getDBforeignkeys();

	public int updateReferee(Referee referee);

	public int deleteSeason(Integer id);

	public int deleteCup(Integer cupId);

	public Cup getCup(Integer cupId);

	public void tournamentRegistration(Integer tournamentId, Integer teamId);

	public int tournamentUnRegistration(Integer tournamentId, Integer teamId);

	public int createTournament(Tournament newTournament);

	public List<Tournament> getTournaments(Integer teamId, Integer periode);

	public Tournament getTournament(Integer tournamentId);

	public int deleteTournament(Integer tournamentId);

	public Item[] getTeamAsItems(Integer clubId);

	public Item[] getSportTypes();

	public int updateClub(Club club);

}