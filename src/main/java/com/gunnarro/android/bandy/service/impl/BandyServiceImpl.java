package com.gunnarro.android.bandy.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import android.content.Context;

import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Activity;
import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.domain.League;
import com.gunnarro.android.bandy.domain.SearchResult;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Cup;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.activity.MatchEvent;
import com.gunnarro.android.bandy.domain.activity.Season;
import com.gunnarro.android.bandy.domain.activity.Training;
import com.gunnarro.android.bandy.domain.party.Address;
import com.gunnarro.android.bandy.domain.party.Contact;
import com.gunnarro.android.bandy.domain.party.Player;
import com.gunnarro.android.bandy.domain.party.Referee;
import com.gunnarro.android.bandy.domain.party.Role.RoleTypesEnum;
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

	public static enum SelectionListType {
		CLUB_NAMES, TEAM_NAMES, PLAYER_NAMES, CONTACT_NAMES, LEAGUE_NAMES, MATCH_TYPES_NAMES, SEASONS_TYPES, SPORT_TYPES, REFEREE_NAMES;
	};

	private static Map<String, Integer> datePeriodeMap = new HashMap<String, Integer>();
	{
		datePeriodeMap.put("Year", Calendar.YEAR);
		datePeriodeMap.put("Month", Calendar.MONTH);
		datePeriodeMap.put("Week", Calendar.WEEK_OF_YEAR);
		datePeriodeMap.put("Day", Calendar.DAY_OF_YEAR);
	}

	@Inject
	Context context;
	// @Inject
	private BandyRepository bandyRepository;
	@Inject
	XmlDocumentParser xmlParser;

	/**
	 * default constructor, used for unit testing only.
	 */
	@Inject
	public BandyServiceImpl() {
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
			this.bandyRepository.deleteAllTableData();
			CustomLog.d(this.getClass(), "Deleted all current stored DB data...");
			// for (String file : getDataFileUrlList()) {
			// this.xmlParser.downloadAndUpdateDB(file, this);
			// }
			this.xmlParser.downloadAndUpdateDB(this.context, getDataFileUrl(), this);
			CustomLog.d(this.getClass(), "Finished loading data into DB");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Only dynamic lists {@inheritDoc}
	 */
	@Override
	public Item[] getSeletionList(Integer id, SelectionListType type) {
		String[] list = null;
		switch (type) {
		case CLUB_NAMES:
			return bandyRepository.getClubAsItems();
		case CONTACT_NAMES:
			list = getContactNames(id);
			break;
		case PLAYER_NAMES:
			list = getPlayerNames(id);
			break;
		case TEAM_NAMES:
			return bandyRepository.getTeamAsItems(id);
		case REFEREE_NAMES:
			list = getRefereeNames();
			break;
		case SEASONS_TYPES:
			list = getSeasonPeriodes();
			break;
		case SPORT_TYPES:
			return bandyRepository.getSportTypes();
		default:
			list = new String[] {};
		}

		Item[] items = new Item[list.length];
		for (int i = 0; i < list.length; i++) {
			items[i] = new Item(i, list[i], false);
		}

		CustomLog.e(this.getClass(), "type=" + type + ", id=" + id + ", hits=" + list.length);
		return items;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteClub(Integer clubId) {
		return bandyRepository.deleteClub(clubId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveClub(Club club) {
		if (club.getId() == null) {
			bandyRepository.createClub(club);
		} else {
			bandyRepository.updateClub(club);
		}
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
	public int saveMatch(Match match) {
		if (match.getId() == null) {
			return bandyRepository.createMatch(match);
		} else {
			return bandyRepository.updateMatch(match);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int saveTeam(Team team, Contact newTeamleader, Contact newCoach) {
		try {
			int teamId;
			if (team.getId() == null) {
				// This was a new team
				teamId = bandyRepository.createTeam(team);
				// Create team contact persons
				if (team.getTeamLead() != null) {
					Contact contact = bandyRepository.getContact(team.getTeamLead().getFirstName(), team.getTeamLead().getLastName());
					bandyRepository.createContactRoleTypeLnk(contact.getId(), RoleTypesEnum.TEAMLEAD.getId());
				}
				if (team.getCoach() != null) {
					Contact contact = bandyRepository.getContact(team.getCoach().getFirstName(), team.getCoach().getLastName());
					bandyRepository.createContactRoleTypeLnk(contact.getId(), RoleTypesEnum.COACH.getId());
				}
				return teamId;
			} else {
				teamId = bandyRepository.updateTeam(team);
				// Change team contact persons
				if (newTeamleader != null) {
					Contact newTeamleaderContact = bandyRepository.getContact(newTeamleader.getFirstName(), newTeamleader.getLastName());
					if (newTeamleaderContact != null) {
						bandyRepository.createContactRoleTypeLnk(newTeamleaderContact.getId(), RoleTypesEnum.TEAMLEAD.getId());
						bandyRepository.deleteContactRoleTypeLnk(team.getTeamLead().getId(), RoleTypesEnum.TEAMLEAD.getId());
					} else {
						CustomLog.e(this.getClass(), "No contact found for: " + newTeamleader.getFirstName() + " " + newTeamleader.getLastName());
					}
				}

				if (newCoach != null) {
					Contact newCoachContact = bandyRepository.getContact(newCoach.getFirstName(), newCoach.getLastName());
					if (newCoachContact != null) {
						bandyRepository.createContactRoleTypeLnk(newCoachContact.getId(), RoleTypesEnum.COACH.getId());
						bandyRepository.deleteContactRoleTypeLnk(team.getCoach().getId(), RoleTypesEnum.COACH.getId());
					} else {
						CustomLog.e(this.getClass(), "No contact found for: " + newCoach.getFirstName() + " " + newCoach.getLastName());
					}
				}
			}
			return teamId;
		} catch (Exception e) {
			CustomLog.e(this.getClass(), e.getMessage());
			throw new ApplicationException(e.getMessage());
		}
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
			bandyRepository.updateAddress(player.getAddress());
			return bandyRepository.updatePlayer(player);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int saveContact(Contact contact) {
		if (contact.getId() == null) {
			return bandyRepository.createContact(contact);
		} else {
			bandyRepository.updateAddress(contact.getAddress());
			return bandyRepository.updateContact(contact);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int saveReferee(Referee referee) {
		if (referee.getId() == null) {
			return bandyRepository.createReferee(referee);
		} else {
			bandyRepository.updateAddress(referee.getAddress());
			return bandyRepository.updateReferee(referee);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTeam(int teamId) {
		this.bandyRepository.deleteTeam(teamId);
	}

	public int deleteReferee(Integer refereeId) {
		return bandyRepository.deleteReferee(refereeId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deletePlayer(Integer playerId) {
		// for (PlayerLinkTableTypeEnum type : PlayerLinkTableTypeEnum.values())
		// {
		// this.bandyRepository.deletePlayerLink(type, playerId, null);
		// }
		this.bandyRepository.deletePlayer(playerId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteContact(Integer contactId) {
		this.bandyRepository.deleteContact(contactId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteMatch(Integer matchId) {
		this.bandyRepository.deleteMatchEvents(matchId);
		this.bandyRepository.deleteMatch(matchId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTraining(Integer trainingId) {
		this.bandyRepository.deleteTraining(trainingId);
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

	public int createMatchEvent(MatchEvent matchEvent) {
		return this.bandyRepository.createMatchEvent(matchEvent);
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
	public String[] getRoleTypeNames() {
		return bandyRepository.getRoleTypeNames();
	}

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
	public String[] getTeamNames(Integer clubId) {
		return bandyRepository.getTeamNames(clubId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Team> getTeamList(String clubName) {
		return bandyRepository.getTeamList(clubName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Item[] getTeamAsItems(Integer clubId) {
		return bandyRepository.getTeamAsItems(clubId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Item[] getMatchTypes() {
		return bandyRepository.getMatchTypes();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Item[] getMatchStatusList() {
		return bandyRepository.getMatchStatusList();
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
	public Referee getReferee(int refereeId) {
		return bandyRepository.getReferee(refereeId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Contact getTeamLead(Integer teamId) {
		return this.bandyRepository.getTeamContactPerson(teamId, RoleTypesEnum.TEAMLEAD.name());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Contact getCoach(Integer teamId) {
		return this.bandyRepository.getTeamContactPerson(teamId, RoleTypesEnum.COACH.name());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Team getTeam(Integer clubId, String name, boolean isIncludeAll) {
		try {
			Team team = this.bandyRepository.getTeam(clubId, name);
			if (team == null) {
				throw new ValidationException("Team not found, clubId=" + clubId + ", team name=" + name);
			}
			if (isIncludeAll) {
				team.setTeamLead(this.getTeamLead(team.getId()));
				team.setConatctList(this.getContactList(team.getId()));
				team.setPlayerList(this.getPlayerList(team.getId()));
			}
			return team;
		} catch (ApplicationException ve) {
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
		team.setTeamLead(this.getTeamLead(id));
		team.setCoach(this.getCoach(id));
		team.setConatctList(this.getContactList(id));
		team.setPlayerList(getPlayerList(id));
		return team;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Club getClub(String name, String departmentName) {
		return this.bandyRepository.getClub(name, departmentName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Club> getClubList() {
		return this.bandyRepository.getClubList();
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
	public List<Activity> getActivityList(Integer clubId, String teamName, String period, String filterBy) {
		List<Activity> list = new ArrayList<Activity>();
		Team team = null;
		try {
			team = this.bandyRepository.getTeam(clubId, teamName);
			if (team == null) {
				CustomLog.e(this.getClass(), "No team found for: " + clubId + ", " + teamName);
				return list;
			}
		} catch (ApplicationException ae) {
			// Simply ignore, no data found
			return list;
		}
		// FIXME

		// if (filterBy.equalsIgnoreCase("All") ||
		// filterBy.equalsIgnoreCase(Activity.ActivityTypeEnum.MATCH.name())) {
		// for (Match match : getMatchList(team.getId(),
		// datePeriodeMap.get(period))) {
		// list.add(new Activity(ActivityTypeEnum.MATCH, match.getStartTime(),
		// match.getVenue(), match.getTeamVersus(), match.getMatchType()));
		// }
		// }
		// if (filterBy.equalsIgnoreCase("All") ||
		// filterBy.equalsIgnoreCase(Activity.ActivityTypeEnum.TRAINING.name()))
		// {
		// for (Training training : getTrainingList(team.getId(),
		// datePeriodeMap.get(period))) {
		// list.add(new Activity(ActivityTypeEnum.TRAINING,
		// training.getStartTime(), training.getVenue(),
		// training.getTeam().getName()));
		// }
		// }
		// if (filterBy.equalsIgnoreCase("All") ||
		// filterBy.equalsIgnoreCase(Activity.ActivityTypeEnum.CUP.name())) {
		// for (Cup cup : getCupList(team.getId(), datePeriodeMap.get(period)))
		// {
		// list.add(new Activity(ActivityTypeEnum.CUP, cup.getStartDate(),
		// cup.getVenue(), cup.getCupName()));
		// }
		// }
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
	public String[] getContactNames(int teamId) {
		List<Contact> contactList;
		if (teamId == -1) {
			contactList = this.bandyRepository.getContactList(1);
		} else {
			contactList = this.bandyRepository.getContactList(teamId, "%");
		}
		List<String> names = new ArrayList<String>();
		for (Contact contact : contactList) {
			if (!contact.hasTeamRoles()) {
				names.add(contact.getFullName());
			}
		}
		String[] nameArray = names.toArray(new String[names.size()]);
		return nameArray;
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
	public void registrerRefereeForMatch(int refereeId, int matchId) {
		bandyRepository.registrerRefereeForMatch(refereeId, matchId);
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
	public String[] getPlayerNames(int teamId) {
		List<Item> playersAsItemList = bandyRepository.getPlayersAsItemList(teamId);
		String[] list = new String[playersAsItemList.size()];
		for (int i = 0; i < playersAsItemList.size(); i++) {
			list[i] = playersAsItemList.get(i).getValue();
		}
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getPlayerNames(Integer clubId, String teamName) {
		Team team = bandyRepository.getTeam(clubId, teamName);
		if (team != null) {
			List<Item> playersAsItemList = bandyRepository.getPlayersAsItemList(team.getId());
			String[] list = new String[playersAsItemList.size()];
			for (int i = 0; i < playersAsItemList.size(); i++) {
				// TODO HacK
				list[i] = playersAsItemList.get(i).getId() + ":" + playersAsItemList.get(i).getValue();
			}
			return list;
		}
		return new String[] {};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Item> getContactsAsItemList(Integer teamId) {
		return bandyRepository.getContactsAsItemList(teamId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public League getLeague(String name) {
		return bandyRepository.getLeague(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Player getPlayer(int playerId) {
		return this.bandyRepository.getPlayer(playerId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Contact getContact(int contactId) {
		return this.bandyRepository.getContact(contactId);
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
	public List<String> getDataFileUrlList() {
		return bandyRepository.getSettings(SettingsTable.DATA_FILE_URL_KEY);
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
	public boolean updateDataFileVersion(String version) {
		this.bandyRepository.updateSetting(SettingsTable.DATA_FILE_VERSION_KEY, version);
		return true;
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

	public String[] getLeagueNames() {
		return this.bandyRepository.getLeagueNames();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateGoalsHomeTeam(int matchId, int goals) {
		this.bandyRepository.updateGoals(matchId, goals, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateGoalsAwayTeam(int matchId, int goals) {
		this.bandyRepository.updateGoals(matchId, goals, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MatchEvent> getMatchEventList(int matchId) {
		return bandyRepository.getMatchEventList(matchId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateMatchStatus(int matchId, int statusId) {
		bandyRepository.updateMatchStatus(matchId, statusId);
	}

	/**
	 * {@inheritDoc}
	 */
	// @Override
	// public List<Item> getSettingGroups() {
	// return null;//bandyRepository.getSettings("%");
	// }
}
