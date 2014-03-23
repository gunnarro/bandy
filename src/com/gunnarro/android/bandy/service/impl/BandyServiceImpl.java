package com.gunnarro.android.bandy.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Activity;
import com.gunnarro.android.bandy.domain.Activity.ActivityTypeEnum;
import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.domain.SearchResult;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Cup;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.activity.Season;
import com.gunnarro.android.bandy.domain.activity.Training;
import com.gunnarro.android.bandy.domain.party.Address;
import com.gunnarro.android.bandy.domain.party.Contact;
import com.gunnarro.android.bandy.domain.party.Contact.ContactRoleEnum;
import com.gunnarro.android.bandy.domain.party.Player;
import com.gunnarro.android.bandy.domain.party.Role;
import com.gunnarro.android.bandy.domain.statistic.Statistic;
import com.gunnarro.android.bandy.domain.view.list.Item;
import com.gunnarro.android.bandy.repository.BandyRepository;
import com.gunnarro.android.bandy.repository.impl.BandyRepositoryImpl;
import com.gunnarro.android.bandy.repository.impl.BandyRepositoryImpl.PlayerLinkTableTypeEnum;
import com.gunnarro.android.bandy.repository.table.SettingsTable;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.exception.ApplicationException;
import com.gunnarro.android.bandy.service.exception.ValidationException;

public class BandyServiceImpl implements BandyService {

	private static Map<String, Integer> datePeriodeMap = new HashMap<String, Integer>();
	{
		datePeriodeMap.put("Year", Calendar.YEAR);
		datePeriodeMap.put("Month", Calendar.MONTH);
		datePeriodeMap.put("Week", Calendar.WEEK_OF_YEAR);
		datePeriodeMap.put("Day", Calendar.DAY_OF_YEAR);
	}

	private Context context;
	private BandyRepository bandyRepository;
	private XmlDocumentParser xmlParser;

	/**
	 * default constructor, used for unit testing only.
	 */
	public BandyServiceImpl() {
		this.xmlParser = new XmlDocumentParser();
	}

	/**
	 * 
	 * @param context
	 */
	public BandyServiceImpl(Context context) {
		this();
		this.context = context;
		this.bandyRepository = new BandyRepositoryImpl(this.context);
		this.bandyRepository.open();
	}

	public Context getContext() {
		return context;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDBFileName() {
		return bandyRepository.getDBFileName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDBUserVersion() {
		return bandyRepository.getDBUserVersion();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDBEncoding() {
		return bandyRepository.getDBEncoding();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createView() {
		bandyRepository.createView();
		CustomLog.d(this.getClass(), "Created View");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadData(String filePath) {
		try {
			CustomLog.d(this.getClass(), "Start loading data into DB: " + filePath);
			bandyRepository.deleteAllTableData();
			CustomLog.d(this.getClass(), "Deleted all current stored DB data...");
			this.xmlParser.testParseByXpath(filePath, this);
			CustomLog.d(this.getClass(), "Finished loading data into DB");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void listRelationsShips() {
		this.bandyRepository.listRelationsShips();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createClub(Club club) {
		return this.bandyRepository.createClub(club);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createTeam(Team team) {
		return this.bandyRepository.createTeam(team);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createMatch(Match match) {
		return this.bandyRepository.createMatch(match);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createMatchForCup(Match match, int cupId) {
		// first, create the match
		int matchId = createMatch(match);
		// then link this cup to the newly created match
		return this.bandyRepository.createCupMatchLnk(cupId, matchId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createCup(Cup cup) {
		return this.bandyRepository.createCup(cup);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createTraining(Training training) {
		Training existingTraning = this.bandyRepository.getTrainingByDate(training.getTeam().getId(), training.getStartTime());
		if (existingTraning == null) {
			return this.bandyRepository.createTraining(training);
		}
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int savePlayer(Player player) {
		if (player.getId() == null) {
			return bandyRepository.createPlayer(player);
		} else {
			return bandyRepository.updatePlayer(player);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createPlayer(Player player) {
		return this.bandyRepository.createPlayer(player);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changePlayerStatus(int playerId, String status) {
		this.bandyRepository.changePlayerStatus(playerId, status);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createContact(Contact contact) {
		return this.bandyRepository.createContact(contact);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long createAddress(Address address) {
		return this.bandyRepository.createAddress(address);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createSeason(Season season) {
		return this.bandyRepository.createSeason(season);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Season getSeason(Integer seasonId) {
		return this.bandyRepository.getSeason(seasonId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Season getCurrentSeason() {
		return this.bandyRepository.getCurrentSeason();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Season getSeason(String period) {
		return this.bandyRepository.getSeason(period);
	}

	// ------------------------------------------------------------------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getTeamNames(String clubName) {
		return bandyRepository.getTeamNames(clubName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getMatchTypes() {
		return bandyRepository.getMatchTypes();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getSeasonPeriodes() {
		return bandyRepository.getSeasonPeriodes();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getRefereeNames() {
		return bandyRepository.getRefereeNames();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Contact getTeamLead(Integer teamId) {
		return this.bandyRepository.getTeamContactPerson(teamId, ContactRoleEnum.TEAMLEAD.name());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Contact getCoach(Integer teamId) {
		return this.bandyRepository.getTeamContactPerson(teamId, ContactRoleEnum.COACH.name());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Team getTeam(String name, boolean isIncludeAll) {
		try {
			Team team = this.bandyRepository.getTeam(name);
			if (team == null) {
				throw new ApplicationException("Team not found, team name=" + name);
			}
			if (isIncludeAll) {
				team.setTeamLead(this.getTeamLead(team.getId()));
				team.setConatctList(this.getContactList(team.getId()));
				team.setPlayerList(this.getPlayerList(team.getId()));
			}
			return team;
		} catch (ValidationException ve) {
			CustomLog.e(this.getClass(), ve.getMessage());
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Team getTeam(Integer id) {
		Team team = this.bandyRepository.getTeam(id);
		if (team == null) {
			throw new ApplicationException("Team not found, team id=" + id);
		}
		team.setTeamLead(this.getTeamLead(team.getId()));
		team.setConatctList(this.getContactList(team.getId()));
		team.setPlayerList(getPlayerList(team.getId()));
		return team;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Club getClub(String name) {
		return this.bandyRepository.getClub(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Club getClub(Integer id) {
		return this.bandyRepository.getClub(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Activity> getActivityList(String teamName, String period, String filterBy) {
		List<Activity> list = new ArrayList<Activity>();
		Team team = null;
		try {
			team = this.bandyRepository.getTeam(teamName);
			if (team == null) {
				CustomLog.e(this.getClass(), "No team found for: " + teamName);
				return list;
			}
		} catch (ApplicationException ae) {
			// Simply ignore, no data found
			return list;
		}

		if (filterBy.equalsIgnoreCase("All") || filterBy.equalsIgnoreCase(Activity.ActivityTypeEnum.MATCH.name())) {
			for (Match match : getMatchList(team.getId(), datePeriodeMap.get(period))) {
				list.add(new Activity(ActivityTypeEnum.MATCH, match.getStartTime(), match.getVenue(), match.getTeamVersus(), match.getMatchType()));
			}
		}
		if (filterBy.equalsIgnoreCase("All") || filterBy.equalsIgnoreCase(Activity.ActivityTypeEnum.TRAINING.name())) {
			for (Training training : getTrainingList(team.getId(), datePeriodeMap.get(period))) {
				list.add(new Activity(ActivityTypeEnum.TRAINING, training.getStartTime(), training.getVenue(), training.getTeam().getName()));
			}
		}
		if (filterBy.equalsIgnoreCase("All") || filterBy.equalsIgnoreCase(Activity.ActivityTypeEnum.CUP.name())) {
			for (Cup cup : getCupList(team.getId(), datePeriodeMap.get(period))) {
				list.add(new Activity(ActivityTypeEnum.CUP, cup.getStartDate(), cup.getVenue(), cup.getCupName()));
			}
		}
		Collections.sort(list);
		return list;
	}

	@Override
	public Match getMatch(int matchId) {
		return this.bandyRepository.getMatch(matchId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public List<Match> getMatchList(Integer teamId, Integer periode) {
		return this.bandyRepository.getMatchList(teamId, periode);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public List<Item> getMatchSignedPlayerList(int teamId, int matchId) {
		return this.bandyRepository.getMatchPlayerList(teamId, matchId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public List<Training> getTrainingList(Integer teamId, Integer periode) {
		return this.bandyRepository.getTrainingList(teamId, periode);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public List<Contact> getContactList(Integer teamId) {
		return this.bandyRepository.getContactList(teamId, "%");
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public List<Cup> getCupList(Integer teamId, Integer periode) {
		return this.bandyRepository.getCupList(teamId, periode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Player lookupPlayer(String mobileNr) {
		Player player = bandyRepository.lookupPlayer(mobileNr);
		if (player == null) {
			player = bandyRepository.lookupPlayerThroughContact(mobileNr);
			// No player with this mobile number was found, so
			// try to lookup player through contacts, i.e. registered parents
		}
		return player;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int signupForMatch(int playerId, int matchId) {
		bandyRepository.createPlayerLink(PlayerLinkTableTypeEnum.MATCH, playerId, matchId);
		return bandyRepository.getNumberOfSignedPlayers(PlayerLinkTableTypeEnum.MATCH, matchId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int unsignForMatch(int playerId, int matchId) {
		bandyRepository.deletePlayerLink(PlayerLinkTableTypeEnum.MATCH, playerId, matchId);
		return bandyRepository.getNumberOfSignedPlayers(PlayerLinkTableTypeEnum.MATCH, matchId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean signupForMatch(String mobileNr, String startDate) {
		Player player = lookupPlayer(mobileNr);
		int matchId = bandyRepository.lookupMatchId(player.getTeam().getId(), startDate);
		if (matchId != -1) {
			bandyRepository.createPlayerLink(PlayerLinkTableTypeEnum.MATCH, player.getId(), matchId);
		} else {
			throw new ApplicationException("No match found for: date=" + startDate + ", teamId=" + player.getTeam().getId());
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public boolean unsignForMatch(String mobileNr, String startDate) {
		Player player = lookupPlayer(mobileNr);
		int matchId = bandyRepository.lookupMatchId(player.getTeam().getId(), startDate);
		if (matchId != -1) {
			bandyRepository.deletePlayerLink(PlayerLinkTableTypeEnum.MATCH, player.getId(), matchId);
		} else {
			throw new ApplicationException("No match found for: date=" + startDate + ", teamId=" + player.getTeam().getId());
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean registrerOnTraining(Integer playerId, Integer trainingId) {
		Integer id = trainingId;
		if (id == null) {
			Player player = this.getPlayer(playerId);
			Training training = bandyRepository.getTrainingByDate(player.getTeam().getId(), System.currentTimeMillis());
			if (training == null) {
				id = createTraining(Training.createTraining(player.getTeam()));
			}
		}
		this.bandyRepository.createPlayerLink(PlayerLinkTableTypeEnum.TRAINING, playerId, id);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean unRegistrerTraining(Integer playerId, Integer trainingId) {
		this.bandyRepository.deletePlayerLink(PlayerLinkTableTypeEnum.TRAINING, playerId, trainingId);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Player> getPlayerList(Integer teamId) {
		return this.bandyRepository.getPlayerList(teamId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Item> getPlayersAsItemList(int teamId) {
		return bandyRepository.getPlayersAsItemList(teamId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Player getPlayer(int playerId) {
		return this.bandyRepository.getPlayer(playerId);
	}

	// ---------------------------------------------------------------------------
	// Settings table operations
	// ---------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateDataFileUrl(String url) {
		this.bandyRepository.updateSetting(SettingsTable.DATA_FILE_URL_KEY, url);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDataFileUrl() {
		return this.bandyRepository.getSetting(SettingsTable.DATA_FILE_URL_KEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateDataFileVersion(String version) {
		this.bandyRepository.updateSetting(SettingsTable.DATA_FILE_VERSION_KEY, version);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDataFileVersion() {
		return this.bandyRepository.getSetting(SettingsTable.DATA_FILE_VERSION_KEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateDataFileLastUpdated(long lastUpdatedTime) {
		this.bandyRepository.updateSetting(SettingsTable.DATA_FILE_LAST_UPDATED_KEY, Long.toString(lastUpdatedTime));
		;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getDataFileLastUpdated() {
		String lastUpdatedTime = this.bandyRepository.getSetting(SettingsTable.DATA_FILE_LAST_UPDATED_KEY);
		long time = lastUpdatedTime != null ? Long.parseLong(lastUpdatedTime) : 0;
		return time;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getEmailAccount() {
		return this.bandyRepository.getSetting(SettingsTable.MAIL_ACCOUNT_KEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getEmailAccountPwd() {
		return this.bandyRepository.getSetting(SettingsTable.MAIL_ACCOUNT_PWD_KEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateEmailAccount(String mailAccount) {
		this.bandyRepository.updateSetting(SettingsTable.MAIL_ACCOUNT_KEY, mailAccount);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateEmailAccountPwd(String mailAccountPwd) {
		this.bandyRepository.updateSetting(SettingsTable.MAIL_ACCOUNT_PWD_KEY, mailAccountPwd);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Role> getRoleList() {
		return this.bandyRepository.getRoleList();
	}

	public SearchResult search(String sqlQuery) {
		CustomLog.e(this.getClass(), "sql=" + sqlQuery);
		try {
			return this.bandyRepository.search(sqlQuery);
		} catch (Exception e) {
			CustomLog.e(this.getClass(), e.getMessage());
			return new SearchResult(e.getMessage());
		}
	}

	// TODO
	public List<Item> getItemList(String type) {
		List<Item> list = new ArrayList<Item>();
		list.add(new Item(0, type + " test item 1", true));
		list.add(new Item(0, type + " test item 2", true));
		return list;
	}

	// TODO
	public void updateItem(Item item) {

	}

	// TODO
	public void createItem(String type, Item newItem) {

	}

	// TODO
	public void deleteItem(Item item) {

	}

	// ---------------------------------------------------------------------------
	// Statistic table operations
	// ---------------------------------------------------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Statistic getPlayerStatistic(int teamId, int playerId, int seasonId) {
		return bandyRepository.getPlayerStatistic(teamId, playerId, seasonId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Statistic getTeamStatistic(int teamId, int seasonId) {
		return bandyRepository.getTeamStatistic(teamId, seasonId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSqlQuery(String id, String type) {
		return this.bandyRepository.getSqlQuery(id, type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getPlayerStatusTypes() {
		return this.bandyRepository.getPlayerStatusTypes();
	}
}
