package com.gunnarro.android.bandy.service;

import java.util.List;

import com.gunnarro.android.bandy.domain.Activity;
import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.domain.League;
import com.gunnarro.android.bandy.domain.SearchResult;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Cup;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.activity.Match.MatchStatus;
import com.gunnarro.android.bandy.domain.activity.MatchEvent;
import com.gunnarro.android.bandy.domain.activity.Season;
import com.gunnarro.android.bandy.domain.activity.Training;
import com.gunnarro.android.bandy.domain.party.Address;
import com.gunnarro.android.bandy.domain.party.Contact;
import com.gunnarro.android.bandy.domain.party.Player;
import com.gunnarro.android.bandy.domain.statistic.Statistic;
import com.gunnarro.android.bandy.domain.view.list.Item;

public interface BandyService {

	public String getDBFileName();

	public String getDBUserVersion();

	public String getDBEncoding();

	public void createView();

	public void loadData(String filePath);

	public void saveClub(Club club);

	public int createClub(Club club);

	public int createTeam(Team team);

	public int createMatch(Match match);

	public int createMatchForCup(Match match, int cupId);

	public int createCup(Cup cup);

	public int createTraining(Training training);

	public int createPlayer(Player player);

	public int savePlayer(Player player);

	public int saveContact(Contact contact);

	public int createContact(Contact contact);

	public long createAddress(Address address);

	public int createSeason(Season season);

	// -------------------------------------------------------------------

	public Season getCurrentSeason();

	public Season getSeason(Integer seasonId);

	public Season getSeason(String period);

	public String[] getTeamNames(String clubName);

	public String[] getRoleTypeNames();

	public Team getTeam(String name, boolean isIncludeAll);

	public Team getTeam(Integer id);

	public Club getClub(String name, String departmentName);

	public List<Club> getClubList();

	public Club getClub(Integer id);

	public List<Match> getMatchList(Integer teamId, Integer periode);

	public List<Item> getMatchSignedPlayerList(int teamId, int matchId);

	public List<Training> getTrainingList(Integer teamId, Integer periode);

	public List<Cup> getCupList(Integer teamId, Integer periode);

	public List<Activity> getActivityList(String teamName, String viewBy, String filterBy);

	public List<Player> getPlayerList(Integer teamId);

	public String[] getPlayerNames(int teamId);

	public String[] getPlayerNames(String teamName);

	public List<Item> getPlayersAsItemList(int teamId);

	public List<Item> getContactsAsItemList(Integer id);

	public String[] getContactNames(int clubId);

	public Player lookupPlayer(String mobileNr);

	public boolean registrerOnTraining(Integer playerId, Integer trainingId);

	public boolean unRegistrerTraining(Integer playerId, Integer trainingId);

	// ---------------------------------------------------------------------------
	// Settings table operations
	// ---------------------------------------------------------------------------
	public void updateDataFileUrl(String url);

	public List<String> getDataFileUrlList();

	public String getDataFileUrl();

	public void updateDataFileVersion(String version);

	public String getDataFileVersion();

	public void updateDataFileLastUpdated(long lastUpdatedTime);

	public long getDataFileLastUpdated();

	public String getEmailAccount();

	public String getEmailAccountPwd();

	public void updateEmailAccount(String mailAccount);

	public void updateEmailAccountPwd(String mailAccountPwd);

	public Contact getTeamLead(Integer id);

	public Contact getCoach(Integer id);

	public List<Contact> getContactList(Integer id);

	public Player getPlayer(int playerId);

	public Contact getContact(int contactId);

	public SearchResult search(String sqlQuery);

	// Methods for list operations
	public List<Item> getItemList(String type);

	public void updateItem(Item item);

	public void createItem(String type, Item newItem);

	public void deleteItem(Item item);

	public void deleteTeam(int teamId);

	public int signupForMatch(int playerId, int matchId);

	public int unsignForMatch(int playerId, int matchId);

	/**
	 * Method used from SMS service
	 * 
	 * @param mobileNr
	 *            players mobile number
	 * @param matchDate
	 *            start date of the match to sign up
	 * @return
	 */
	boolean signupForMatch(String mobileNr, String matchDate);

	/**
	 * Method used from SMS service
	 * 
	 * @param mobileNr
	 *            players mobile number
	 * @param matchDate
	 *            start date of the match to unsign
	 * @return
	 */
	boolean unsignForMatch(String mobileNr, String matchDate);

	/**
	 * 
	 * @param matchId
	 * @return
	 */
	public Match getMatch(int matchId);

	// ---------------------------------------------------------------------------
	// Statistic table operations
	// ---------------------------------------------------------------------------
	public Statistic getPlayerStatistic(int teamId, int playerId, int seasonId);

	public Statistic getTeamStatistic(int teamId, int seasonId);

	public String getSqlQuery(String id, String type);

	public String[] getRefereeNames();

	public String[] getMatchTypes();

	public String[] getSeasonPeriodes();

	public void changePlayerStatus(int playerId, String status);

	public String[] getPlayerStatusTypes();

	public int saveMatch(Match match);

	public void deletePlayer(Integer playerId);

	public void deleteMatch(Integer matchId);

	public void deleteTraining(Integer trainingId);

	public void deleteContact(Integer contactId);

	public int saveTeam(Team team, Contact newTeamleader, Contact newCoach);

	public List<Team> getTeamList(String string);

	public String[] getLeagueNames();

	public League getLeague(String name);

	public void updateGoalsAwayTeam(int matchId, int goals);

	public void updateGoalsHomeTeam(int matchId, int goals);

	public List<MatchEvent> getMatchEventList(int matchId);

	public void updateMatchStatus(int matchId, MatchStatus matchStatus);

	public String[] getMatchStatusList();

	public int createMatchEvent(MatchEvent matchEvent);

	public String[] getClubNames();

}
