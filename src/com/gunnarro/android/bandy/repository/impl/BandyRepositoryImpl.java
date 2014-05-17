package com.gunnarro.android.bandy.repository.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.domain.League;
import com.gunnarro.android.bandy.domain.SearchResult;
import com.gunnarro.android.bandy.domain.Setting;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Cup;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.activity.Match.MatchStatus;
import com.gunnarro.android.bandy.domain.activity.Match.MatchTypesEnum;
import com.gunnarro.android.bandy.domain.activity.MatchEvent;
import com.gunnarro.android.bandy.domain.activity.Season;
import com.gunnarro.android.bandy.domain.activity.Training;
import com.gunnarro.android.bandy.domain.party.Address;
import com.gunnarro.android.bandy.domain.party.Contact;
import com.gunnarro.android.bandy.domain.party.Player;
import com.gunnarro.android.bandy.domain.party.Player.PlayerStatusEnum;
import com.gunnarro.android.bandy.domain.party.Referee;
import com.gunnarro.android.bandy.domain.party.Role.RoleTypesEnum;
import com.gunnarro.android.bandy.domain.statistic.MatchStatistic;
import com.gunnarro.android.bandy.domain.statistic.Statistic;
import com.gunnarro.android.bandy.domain.view.list.Item;
import com.gunnarro.android.bandy.repository.BandyDataBaseHjelper;
import com.gunnarro.android.bandy.repository.BandyRepository;
import com.gunnarro.android.bandy.repository.table.ClubsTable;
import com.gunnarro.android.bandy.repository.table.SettingsTable;
import com.gunnarro.android.bandy.repository.table.TableHelper;
import com.gunnarro.android.bandy.repository.table.TeamsTable;
import com.gunnarro.android.bandy.repository.table.activity.CupsTable;
import com.gunnarro.android.bandy.repository.table.activity.LeaguesTable;
import com.gunnarro.android.bandy.repository.table.activity.MatchEventsTable;
import com.gunnarro.android.bandy.repository.table.activity.MatchStatusTypesTable;
import com.gunnarro.android.bandy.repository.table.activity.MatchTypesTable;
import com.gunnarro.android.bandy.repository.table.activity.MatchesTable;
import com.gunnarro.android.bandy.repository.table.activity.SeasonsTable;
import com.gunnarro.android.bandy.repository.table.activity.TrainingsTable;
import com.gunnarro.android.bandy.repository.table.link.ContactRoleTypeLnkTable;
import com.gunnarro.android.bandy.repository.table.link.CupMatchLnkTable;
import com.gunnarro.android.bandy.repository.table.link.PlayerContactLnkTable;
import com.gunnarro.android.bandy.repository.table.link.PlayerCupLnkTable;
import com.gunnarro.android.bandy.repository.table.link.PlayerMatchLnkTable;
import com.gunnarro.android.bandy.repository.table.link.PlayerTrainingLnkTable;
import com.gunnarro.android.bandy.repository.table.party.AddressTable;
import com.gunnarro.android.bandy.repository.table.party.ContactsTable;
import com.gunnarro.android.bandy.repository.table.party.PlayersTable;
import com.gunnarro.android.bandy.repository.table.party.RefereesTable;
import com.gunnarro.android.bandy.repository.table.party.RoleTypesTable;
import com.gunnarro.android.bandy.repository.table.party.StatusesTable;
import com.gunnarro.android.bandy.repository.view.MatchResultView;
import com.gunnarro.android.bandy.service.exception.ApplicationException;
import com.gunnarro.android.bandy.service.exception.ValidationException;

public class BandyRepositoryImpl implements BandyRepository {

	private final static String WILD_CARD = "%";

	public enum PlayerLinkTableTypeEnum {
		MATCH, CUP, TRAINING, CONTACT;
	}

	// Database fields
	private SQLiteDatabase database;
	private BandyDataBaseHjelper dbHelper;

	public BandyRepositoryImpl(SQLiteDatabase database) {
		this.database = database;
	}

	public BandyRepositoryImpl(Context context) {
		this.dbHelper = BandyDataBaseHjelper.getInstance(context);
	}

	private SQLiteDatabase getDatabase(boolean writable) {
		if (dbHelper != null) {
			if (writable) {
				return dbHelper.getWritableDatabase();
			}
			return dbHelper.getReadableDatabase();
		} else {
			// A hack Just for unit testing only.
			return database;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void open() throws SQLException {
		this.database = dbHelper.getWritableDatabase();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDBFileName() {
		String dbFileName = null;
		Cursor cursor = this.database.rawQuery("PRAGMA database_list;", null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			dbFileName = cursor.getString(0);
		}
		CustomLog.d(this.getClass(), "hit=" + cursor.getCount());
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return dbFileName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDBUserVersion() {
		String dbUserName = null;
		Cursor cursor = this.database.rawQuery("PRAGMA user_version;", null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			dbUserName = cursor.getString(0);
		}
		CustomLog.d(this.getClass(), "hit=" + cursor.getCount());
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return dbUserName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDBEncoding() {
		String dbEncoding = null;
		Cursor cursor = this.database.rawQuery("PRAGMA encoding;", null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			dbEncoding = cursor.getString(0);
		}
		CustomLog.d(this.getClass(), "hit=" + cursor.getCount());
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return dbEncoding;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		dbHelper.close();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteAllTableData() {
		BandyDataBaseHjelper.deleteAllTableData(getDatabase(true), false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createMatchEvent(MatchEvent matchEvent) {
		ContentValues values = MatchEventsTable.createContentValues(matchEvent);
		this.database = getDatabase(true);
		long id = database.insertOrThrow(MatchEventsTable.TABLE_NAME, null, values);
		CustomLog.d(this.getClass(), "Created: " + matchEvent);
		return Long.valueOf(id).intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createClub(Club club) {
		try {
			ContentValues values = ClubsTable.createContentValues(club);
			this.database = getDatabase(true);
			long id = database.insertOrThrow(ClubsTable.TABLE_NAME, null, values);
			CustomLog.d(this.getClass(), "Created club: " + club);
			return Long.valueOf(id).intValue();
		} catch (SQLException sqle) {
			CustomLog.e(getClass(), "Error creating: " + club);
			CustomLog.e(getClass(), sqle.getMessage());
			throw new ApplicationException(sqle.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteClub(Integer clubId) {
		String whereClause = TableHelper.COLUMN_ID + " = ?";
		String[] whereArgs = { clubId.toString() };
		int id = database.delete(ClubsTable.TABLE_NAME, whereClause, whereArgs);
		CustomLog.d(this.getClass(), "deleted club, id=" + id);
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createTeam(Team team) {
		try {
			if (team.getClub() == null || team.getClub().getId() == null) {
				throw new ApplicationException("Club must be set for creating new Team!");
			}
			ContentValues values = TeamsTable.createContentValues(team);
			this.database = getDatabase(true);
			long id = database.insertOrThrow(TeamsTable.TABLE_NAME, null, values);
			return Long.valueOf(id).intValue();
		} catch (SQLException sqle) {
			CustomLog.e(getClass(), "Error creating: " + team);
			CustomLog.e(getClass(), sqle.getMessage());
			throw new ApplicationException(sqle.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createMatch(Match match) {
		if (match.getTeam() == null || match.getTeam().getId() == null) {
			throw new ApplicationException("Match must be asigned to a team. Missing valid team!");
		}
		try {
			CustomLog.d(this.getClass(), match.toString());
			ContentValues values = MatchesTable.createContentValues(match);
			this.database = getDatabase(true);
			long id = database.insertOrThrow(MatchesTable.TABLE_NAME, null, values);
			return Long.valueOf(id).intValue();
		} catch (SQLException sqle) {
			CustomLog.e(getClass(), "Error creating: " + match);
			CustomLog.e(getClass(), sqle.getMessage());
			throw new ApplicationException(sqle.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createCup(Cup cup) {
		try {
			ContentValues values = CupsTable.createContentValues(cup.getSeason().getId(), cup.getStartDate(), cup.getCupName(), cup.getClubName(),
					cup.getVenue(), cup.getDeadlineDate());
			this.database = getDatabase(true);
			long id = database.insertOrThrow(CupsTable.TABLE_NAME, null, values);
			return Long.valueOf(id).intValue();
		} catch (SQLException sqle) {
			CustomLog.e(getClass(), "Error creating: " + cup);
			CustomLog.e(getClass(), sqle.getMessage());
			throw new ApplicationException(sqle.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createSeason(Season season) {
		ContentValues values = SeasonsTable.createContentValues(season.getPeriod(), season.getStartTime(), season.getEndTime());
		this.database = getDatabase(true);
		long id = database.insertOrThrow(SeasonsTable.TABLE_NAME, null, values);
		CustomLog.d(this.getClass(), season);
		return Long.valueOf(id).intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createTraining(Training training) {
		ContentValues values = TrainingsTable.createContentValues(training);
		this.database = getDatabase(true);
		long id = database.insertOrThrow(TrainingsTable.TABLE_NAME, null, values);
		CustomLog.d(this.getClass(), training);
		return Long.valueOf(id).intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changePlayerStatus(Integer playerId, String status) {
		StringBuffer query = new StringBuffer();
		query.append(TableHelper.COLUMN_ID).append(" = ?");
		CustomLog.d(this.getClass(), "query=" + query.toString());
		ContentValues values = new ContentValues();
		values.put(PlayersTable.COLUMN_STATUS, status);
		this.database = getDatabase(false);
		int id = database.update(PlayersTable.TABLE_NAME, values, query.toString(), new String[] { playerId.toString() });
		CustomLog.d(this.getClass(), "player=" + playerId + ", new status=" + status);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteTeam(Integer teamId) {
		String whereClause = TableHelper.COLUMN_ID + " = ?";
		String[] whereArgs = { teamId.toString() };
		int id = database.delete(TeamsTable.TABLE_NAME, whereClause, whereArgs);
		CustomLog.d(this.getClass(), "deleted team, id=" + id);
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deletePlayer(Integer playerId) {
		String whereClause = TableHelper.COLUMN_ID + " = ?";
		String[] whereArgs = { playerId.toString() };
		int id = database.delete(PlayersTable.TABLE_NAME, whereClause, whereArgs);
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteContact(Integer contactId) {
		String whereClause = TableHelper.COLUMN_ID + " = ?";
		String[] whereArgs = { contactId.toString() };
		int id = database.delete(ContactsTable.TABLE_NAME, whereClause, whereArgs);
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteReferee(Integer refereeId) {
		String whereClause = TableHelper.COLUMN_ID + " = ?";
		String[] whereArgs = { refereeId.toString() };
		int id = database.delete(RefereesTable.TABLE_NAME, whereClause, whereArgs);
		return id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteMatch(Integer matchId) {
		String whereClause = TableHelper.COLUMN_ID + " = ?";
		String[] whereArgs = { matchId.toString() };
		int id = database.delete(MatchesTable.TABLE_NAME, whereClause, whereArgs);
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteTraining(Integer trainingId) {
		String whereClause = TableHelper.COLUMN_ID + " = ?";
		String[] whereArgs = { trainingId.toString() };
		int id = database.delete(TrainingsTable.TABLE_NAME, whereClause, whereArgs);
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int updateTeam(Team team) {
		ContentValues teamUpdateValues = TeamsTable.updateContentValues(team);
		String whereClause = TableHelper.COLUMN_ID + " = ?";
		String[] whereArgs = { team.getId().toString() };
		long teamId = database.update(TeamsTable.TABLE_NAME, teamUpdateValues, whereClause, whereArgs);
		return Long.valueOf(teamId).intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int updatePlayer(Player player) {
		ContentValues playerUpdateValues = PlayersTable.updateContentValues(player);
		String whereClause = TableHelper.COLUMN_ID + " = ?";
		String[] whereArgs = { player.getId().toString() };
		long playerId = database.update(PlayersTable.TABLE_NAME, playerUpdateValues, whereClause, whereArgs);
		CustomLog.d(this.getClass(), player);
		return Long.valueOf(playerId).intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int updateContact(Contact contact) {
		ContentValues contactUpdateValues = ContactsTable.updateContentValues(contact);
		String whereClause = TableHelper.COLUMN_ID + " = ?";
		String[] whereArgs = { contact.getId().toString() };
		long contactId = database.update(ContactsTable.TABLE_NAME, contactUpdateValues, whereClause, whereArgs);
		return Long.valueOf(contactId).intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int updateAddress(Address address) {
		ContentValues addressUpdateValues = AddressTable.updateContentValues(address);
		String whereClause = TableHelper.COLUMN_ID + " = ?";
		String[] whereArgs = { Integer.toString(address.getId()) };
		long addressId = database.update(AddressTable.TABLE_NAME, addressUpdateValues, whereClause, whereArgs);
		CustomLog.e(this.getClass(), address.toString());
		return Long.valueOf(addressId).intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateGoals(Integer matchId, Integer goals, boolean isHomeTeam) {
		String whereClause = TableHelper.COLUMN_ID + " = ?";
		ContentValues updateContentValues = MatchesTable.updateContentValues(MatchesTable.COLUMN_NUMBER_OF_GOALS_AWAY_TEAM, goals);
		if (isHomeTeam) {
			updateContentValues = MatchesTable.updateContentValues(MatchesTable.COLUMN_NUMBER_OF_GOALS_HOME_TEAM, goals);
		}
		String[] whereArgs = { matchId.toString() };
		int id = database.update(MatchesTable.TABLE_NAME, updateContentValues, whereClause, whereArgs);
		CustomLog.e(this.getClass(), "update goals, isHomeTeam=" + isHomeTeam + ", matchId=" + matchId + ", goals=" + goals);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int updateMatch(Match match) {
		ContentValues matchUpdateValues = MatchesTable.updateContentValues(match);
		String whereClause = TableHelper.COLUMN_ID + " = ?";
		String[] whereArgs = { match.getId().toString() };
		long matchId = database.update(MatchesTable.TABLE_NAME, matchUpdateValues, whereClause, whereArgs);
		CustomLog.e(this.getClass(), match.toString());
		return Long.valueOf(matchId).intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int updateMatchStatus(Integer matchId, MatchStatus status) {
		ContentValues values = TableHelper.defaultContentValues();
		values.put(MatchesTable.COLUMN_MATCH_STATUS_NAME, status.name());
		String whereClause = TableHelper.COLUMN_ID + " = ?";
		String[] whereArgs = { matchId.toString() };
		long id = database.update(MatchesTable.TABLE_NAME, values, whereClause, whereArgs);
		CustomLog.e(this.getClass(), "updated match status to: " + status);
		return Long.valueOf(id).intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createPlayer(Player player) {
		if (player.getTeam() == null || player.getTeam().getId() == null) {
			throw new ApplicationException("Player must be asigned to a team. Missing valid team!");
		}
		try {
			long addressId = createAddress(player.getAddress());
			ContentValues playerValues = PlayersTable.createContentValues(addressId, player);
			this.database = getDatabase(true);
			long playerId = database.insertOrThrow(PlayersTable.TABLE_NAME, null, playerValues);
			if (player.getParents() != null) {
				for (Contact parent : player.getParents()) {
					Contact contact = getContact(parent.getFirstName(), parent.getLastName());
					if (contact != null) {
						createPlayerLink(PlayerLinkTableTypeEnum.CONTACT, Long.valueOf(playerId).intValue(), contact.getId().intValue());
					} else {
						CustomLog.e(this.getClass(), "No contact found for: " + parent.getFirstName() + " " + parent.getLastName());
					}
				}
			}
			CustomLog.d(this.getClass(), player);
			return Long.valueOf(playerId).intValue();
		} catch (SQLException sqle) {
			CustomLog.e(getClass(), "Error creating: " + player);
			CustomLog.e(getClass(), sqle.getMessage());
			throw new ApplicationException(sqle.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createContact(Contact contact) {
		try {
			long addressId = createAddress(contact.getAddress());
			ContentValues contactValues = ContactsTable.createContentValues(addressId, contact);
			this.database = getDatabase(true);
			long contactId = database.insertOrThrow(ContactsTable.TABLE_NAME, null, contactValues);
			if (contact.getRoles() != null) {
				for (RoleTypesEnum role : contact.getRoles()) {
					createContactRoleTypeLnk(Long.valueOf(contactId).intValue(), role.getId());
				}
			}
			return Long.valueOf(contactId).intValue();
		} catch (SQLException sqle) {
			CustomLog.e(getClass(), "Error creating: " + contact);
			CustomLog.e(getClass(), sqle.getMessage());
			throw new ApplicationException(sqle.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createReferee(Referee referee) {
		try {
			long addressId = createAddress(referee.getAddress());
			ContentValues contactValues = RefereesTable.createContentValues(addressId, referee);
			this.database = getDatabase(true);
			long refereeId = database.insertOrThrow(RefereesTable.TABLE_NAME, null, contactValues);
			return Long.valueOf(refereeId).intValue();
		} catch (SQLException sqle) {
			CustomLog.e(getClass(), "Error creating: " + referee);
			CustomLog.e(getClass(), sqle.getMessage());
			throw new ApplicationException(sqle.getMessage());
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public long createAddress(Address address) {
		ContentValues contactValues = AddressTable.createContentValues(address);
		this.database = getDatabase(true);
		long addressId = database.insertOrThrow(AddressTable.TABLE_NAME, null, contactValues);
		// Check if this was an existing address, in that case return that
		// address id
		if (addressId == -1) {
			Address addr = getAddress(address.getStreetName(), address.getStreetNumber(), address.getStreetNumberPrefix(), address.getPostalCode());
			if (addr != null) {
				addressId = addr.getId();
			}
		}
		return addressId;
	}

	// -------------------------------------------------------------------------------------------------

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public List<Contact> getContactList(Integer clubId) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT c.* ");
		query.append(" FROM contacts c, teams t, clubs cl");
		query.append(" WHERE cl._id = ?");
		query.append(" AND c.fk_team_id = t._id");
		query.append(" AND t.fk_club_id = cl._id");
		query.append(" ORDER BY c.last_name ASC");
		String[] selectionArgs = { clubId.toString() };
		return getContactList(query.toString(), selectionArgs);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public List<Contact> getContactList(Integer teamId, String role) {
		String query = "SELECT * from contacts WHERE fk_team_id = ? ORDER BY last_name ASC";
		String[] selectionArgs = { teamId.toString() };
		return getContactList(query, selectionArgs);
	}

	private List<Contact> getContactList(String query, String[] selectionArgs) {
		List<Contact> list = new ArrayList<Contact>();
		this.database = getDatabase(false);
		Cursor cursor = database.rawQuery(query, selectionArgs);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Team team = getTeam(cursor.getInt(cursor.getColumnIndex(ContactsTable.COLUMN_FK_TEAM_ID)));
				list.add(mapCursorToContact(cursor, team));
				cursor.moveToNext();
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	@Override
	public Match getMatch(int matchId) {
		Match match = null;
		StringBuffer selection = new StringBuffer();
		selection.append(TableHelper.COLUMN_ID + " = ?");
		String[] selectionArgs = { Integer.toString(matchId) };
		this.database = getDatabase(false);
		Cursor cursor = database.query(MatchesTable.TABLE_NAME, MatchesTable.TABLE_COLUMNS, selection.toString(), selectionArgs, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			Team team = getTeam(cursor.getInt(cursor.getColumnIndex(MatchesTable.COLUMN_FK_TEAM_ID)));
			Season season = getSeason(cursor.getInt(cursor.getColumnIndex(MatchesTable.COLUMN_FK_SEASON_ID)));
			match = mapCursorToMatch(cursor, team, season);
			int numberOfSignedPlayers = getNumberOfSignedPlayers(PlayerLinkTableTypeEnum.MATCH, match.getId());
			match.setNumberOfSignedPlayers(numberOfSignedPlayers);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "playerId=" + matchId + ", match=" + match);
		return match;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public List<Match> getMatchList(Integer teamId, Integer period) {
		List<Match> list = new ArrayList<Match>();
		String periodSelection = getPeriodeSelectionClause(period, System.currentTimeMillis());
		StringBuffer query = new StringBuffer();
		query.append("SELECT *");
		query.append(" FROM ").append(MatchesTable.TABLE_NAME);
		query.append(" WHERE ").append(MatchesTable.COLUMN_FK_TEAM_ID).append(" = ").append(teamId);
		query.append(" AND ").append(periodSelection);
		query.append(" ORDER BY ").append(MatchesTable.COLUMN_START_DATE).append(" ASC");
		CustomLog.d(this.getClass(), "query=" + query.toString());
		this.database = getDatabase(false);
		Cursor cursor = database.rawQuery(query.toString(), null);
		Team team = getTeam(teamId);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Season season = getSeason(cursor.getInt(cursor.getColumnIndex(MatchesTable.COLUMN_FK_SEASON_ID)));
				Match match = mapCursorToMatch(cursor, team, season);
				int numberOfSignedPlayers = getNumberOfSignedPlayers(PlayerLinkTableTypeEnum.MATCH, match.getId());
				match.setNumberOfSignedPlayers(numberOfSignedPlayers);
				list.add(match);
				cursor.moveToNext();
			}
		}
		CustomLog.d(this.getClass(), "matches=" + cursor.getCount());
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Item> getMatchPlayerList(int teamId, int matchId) {
		List<Item> list = new ArrayList<Item>();
		StringBuffer subQuery = new StringBuffer();
		subQuery.append("SELECT l.fk_match_id FROM ").append(PlayerMatchLnkTable.TABLE_NAME).append(" l");
		subQuery.append(" WHERE p._id = l_fk_player_id");
		subQuery.append(" AND l.fk_match_id = ").append(matchId);

		StringBuffer query = new StringBuffer();
		query.append("SELECT p._id, p.first_name, p.last_name, l.fk_match_id");
		query.append(" FROM ").append(PlayersTable.TABLE_NAME).append(" p, ").append(PlayerMatchLnkTable.TABLE_NAME).append(" l");
		query.append(" WHERE ").append(PlayersTable.COLUMN_FK_TEAM_ID).append(" = ").append(teamId);
		query.append(" AND p._id = l.fk_player_id");
		query.append(" AND l.fk_match_id = ").append(matchId);
		query.append(" ORDER BY ").append(PlayersTable.COLUMN_FIRST_NAME).append(" ASC");

		CustomLog.d(this.getClass(), "query=" + query.toString());
		this.database = getDatabase(false);
		Cursor cursor = database.rawQuery(query.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String fullName = cursor.getString(cursor.getColumnIndex(PlayersTable.COLUMN_FIRST_NAME)) + " "
						+ cursor.getString(cursor.getColumnIndex(PlayersTable.COLUMN_LAST_NAME));
				boolean playerSignedIn = true;
				try {
					cursor.getInt(cursor.getColumnIndex(PlayerMatchLnkTable.COLUMN_FK_MATCH_ID));
				} catch (Exception e) {
					CustomLog.d(this.getClass(), "players=" + fullName + " not signed in for matchId=" + matchId);
				}
				list.add(new Item(cursor.getInt(cursor.getColumnIndex(TableHelper.COLUMN_ID)), fullName, playerSignedIn));
				cursor.moveToNext();
			}
		}
		CustomLog.d(this.getClass(), "players=" + cursor.getCount());
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Club> getClubList() {
		List<Club> clubs = new ArrayList<Club>();
		String selection = ClubsTable.COLUMN_CLUB_NAME + " LIKE ?";
		String[] selectionArgs = new String[] { "%" };
		String orderBy = ClubsTable.COLUMN_CLUB_NAME + "," + ClubsTable.COLUMN_CLUB_DEPARTMENT_NAME + " ASC";
		this.database = getDatabase(false);
		Cursor cursor = this.database.query(ClubsTable.TABLE_NAME, ClubsTable.TABLE_COLUMNS, selection, selectionArgs, null, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				clubs.add(mapCursorToClub(cursor));
				cursor.moveToNext();
			}
		}
		CustomLog.d(this.getClass(), "clubs=" + cursor.getCount());
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return clubs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getClubNames() {
		String[] clubNames = new String[] {};
		String orderBy = ClubsTable.COLUMN_CLUB_NAME + "," + ClubsTable.COLUMN_CLUB_DEPARTMENT_NAME + " ASC";
		String selection = ClubsTable.COLUMN_CLUB_NAME + " LIKE ?";
		String[] selectionArgs = new String[] { "%" };
		this.database = getDatabase(false);
		Cursor cursor = this.database.query(ClubsTable.TABLE_NAME, ClubsTable.TABLE_COLUMNS, selection, selectionArgs, null, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			clubNames = new String[cursor.getCount()];
			int i = 0;
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				clubNames[i] = cursor.getString(cursor.getColumnIndex(ClubsTable.COLUMN_CLUB_NAME)) + " "
						+ cursor.getString(cursor.getColumnIndex(ClubsTable.COLUMN_CLUB_DEPARTMENT_NAME));
				cursor.moveToNext();
				i++;
			}
		}
		CustomLog.d(this.getClass(), "club names=" + cursor.getCount());
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return clubNames;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Club getClub(String name, String departmentName) {
		String selection = ClubsTable.COLUMN_CLUB_NAME + " LIKE ? AND " + ClubsTable.COLUMN_CLUB_DEPARTMENT_NAME + " LIKE ?";
		String[] selectionArgs = { name, departmentName };
		return getClub(selection, selectionArgs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Club getClub(Integer id) {
		String selection = TableHelper.COLUMN_ID + " LIKE ?";
		String[] selectionArgs = { id.toString() };
		return getClub(selection, selectionArgs);
	}

	private Club getClub(String selection, String[] selectionArgs) {
		Club club = null;
		this.database = getDatabase(false);
		Cursor cursor = database.query(ClubsTable.TABLE_NAME, ClubsTable.TABLE_COLUMNS, selection, selectionArgs, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			club = mapCursorToClub(cursor);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), club);
		return club;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getLeagueNames() {
		String[] leagueNames = new String[] {};
		String orderBy = LeaguesTable.COLUMN_LEAGUE_NAME + " ASC";
		String selection = LeaguesTable.COLUMN_LEAGUE_NAME + " LIKE ?";
		String[] selectionArgs = { "%" };
		this.database = getDatabase(false);
		Cursor cursor = database.query(LeaguesTable.TABLE_NAME, LeaguesTable.TABLE_COLUMNS, selection, selectionArgs, null, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			leagueNames = new String[cursor.getCount()];
			int i = 0;
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				leagueNames[i] = cursor.getString(cursor.getColumnIndex(LeaguesTable.COLUMN_LEAGUE_NAME));
				cursor.moveToNext();
				i++;
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return leagueNames;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getPlayerStatusTypes() {
		String orderBy = StatusesTable.COLUMN_STATUS_NAME + " ASC";
		String[] statusTypes = new String[] {};
		String selection = StatusesTable.COLUMN_STATUS_NAME + " LIKE ?";
		String[] selectionArgs = { "%" };
		this.database = getDatabase(false);
		Cursor cursor = database.query(StatusesTable.TABLE_NAME, StatusesTable.TABLE_COLUMNS, selection, selectionArgs, null, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			statusTypes = new String[cursor.getCount()];
			int i = 0;
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				statusTypes[i] = cursor.getString(cursor.getColumnIndex(StatusesTable.COLUMN_STATUS_NAME));
				cursor.moveToNext();
				i++;
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return statusTypes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getMatchTypes() {
		String orderBy = MatchTypesTable.COLUMN_MATCH_TYPE_NAME + " ASC";
		String[] matchTypes = new String[] {};
		String selection = MatchTypesTable.COLUMN_MATCH_TYPE_NAME + " LIKE ?";
		String[] selectionArgs = { "%" };
		this.database = getDatabase(false);
		Cursor cursor = database.query(MatchTypesTable.TABLE_NAME, MatchTypesTable.TABLE_COLUMNS, selection, selectionArgs, null, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			matchTypes = new String[cursor.getCount()];
			int i = 0;
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				matchTypes[i] = cursor.getString(cursor.getColumnIndex(MatchTypesTable.COLUMN_MATCH_TYPE_NAME));
				cursor.moveToNext();
				i++;
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return matchTypes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MatchEvent> getMatchEventList(Integer matchId) {
		List<MatchEvent> matchEvents = new ArrayList<MatchEvent>();
		String selection = MatchEventsTable.COLUMN_FK_MATCH_ID + " LIKE ?";
		String[] selectionArgs = { matchId.toString() };
		this.database = getDatabase(false);
		Cursor cursor = database.query(MatchEventsTable.TABLE_NAME, MatchEventsTable.TABLE_COLUMNS, selection, selectionArgs, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				matchEvents.add(mapCursorToMatchEvent(cursor));
				cursor.moveToNext();
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return matchEvents;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getMatchStatusList() {
		String orderBy = MatchStatusTypesTable.COLUMN_MATCH_STATUS_NAME + " ASC";
		String[] matchStatuses = new String[] {};
		String selection = MatchStatusTypesTable.COLUMN_MATCH_STATUS_NAME + " LIKE ?";
		String[] selectionArgs = { "%" };
		this.database = getDatabase(false);
		Cursor cursor = database.query(MatchStatusTypesTable.TABLE_NAME, MatchStatusTypesTable.TABLE_COLUMNS, selection, selectionArgs, null, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			matchStatuses = new String[cursor.getCount()];
			int i = 0;
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				matchStatuses[i] = cursor.getString(cursor.getColumnIndex(MatchStatusTypesTable.COLUMN_MATCH_STATUS_NAME));
				cursor.moveToNext();
				i++;
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return matchStatuses;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getSeasonPeriodes() {
		String orderBy = SeasonsTable.COLUMN_PERIOD + " ASC";
		String[] seasonPeriodes = new String[] {};
		String selection = SeasonsTable.COLUMN_PERIOD + " LIKE ?";
		String[] selectionArgs = { "%" };
		this.database = getDatabase(false);
		Cursor cursor = database.query(SeasonsTable.TABLE_NAME, SeasonsTable.TABLE_COLUMNS, selection, selectionArgs, null, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			seasonPeriodes = new String[cursor.getCount()];
			int i = 0;
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				seasonPeriodes[i] = cursor.getString(cursor.getColumnIndex(SeasonsTable.COLUMN_PERIOD));
				cursor.moveToNext();
				i++;
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return seasonPeriodes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getRoleTypeNames() {
		String[] roleTypeNames = new String[] {};
		String selection = RoleTypesTable.COLUMN_ROLE_TYPE_NAME + " LIKE ?";
		String[] selectionArgs = { "%" };
		String orderBy = RoleTypesTable.COLUMN_ROLE_TYPE_NAME + " ASC";
		this.database = getDatabase(false);
		Cursor cursor = database.query(RoleTypesTable.TABLE_NAME, RoleTypesTable.TABLE_COLUMNS, selection, selectionArgs, null, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			roleTypeNames = new String[cursor.getCount()];
			int i = 0;
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				roleTypeNames[i] = cursor.getString(cursor.getColumnIndex(RoleTypesTable.COLUMN_ROLE_TYPE_NAME));
				cursor.moveToNext();
				i++;
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return roleTypeNames;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getRefereeNames() {
		String[] refereeNames = new String[] {};
		String selection = RefereesTable.COLUMN_FK_SPORT_TYPE_ID + " = ?";
		String[] selectionArgs = { "0" };
		this.database = getDatabase(false);
		Cursor cursor = database.query(RefereesTable.TABLE_NAME, RefereesTable.TABLE_COLUMNS, selection, selectionArgs, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			refereeNames = new String[cursor.getCount()];
			int i = 0;
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				refereeNames[i] = cursor.getString(cursor.getColumnIndex(RefereesTable.COLUMN_FIRST_NAME)) + " "
						+ cursor.getString(cursor.getColumnIndex(RefereesTable.COLUMN_LAST_NAME));
				cursor.moveToNext();
				i++;
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return refereeNames;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Team> getTeamList(String clubName) {
		List<Team> teams = new ArrayList<Team>();
		StringBuffer query = new StringBuffer();
		query.append("SELECT t.").append(TeamsTable.COLUMN_TEAM_NAME);
		query.append(", t.").append(TableHelper.COLUMN_ID);
		query.append(" FROM ").append(TeamsTable.TABLE_NAME).append(" t, ").append(ClubsTable.TABLE_NAME).append(" c");
		query.append(" WHERE t.").append(TeamsTable.COLUMN_FK_CLUB_ID).append(" = c.").append(TableHelper.COLUMN_ID);
		query.append(" AND c.").append(ClubsTable.COLUMN_CLUB_NAME).append(" LIKE '").append(clubName).append("'");
		query.append(" ORDER BY t.").append(TeamsTable.COLUMN_TEAM_NAME).append(" ASC");
		CustomLog.d(this.getClass(), "sqlQuery=" + query.toString());
		this.database = getDatabase(false);
		Cursor cursor = this.database.rawQuery(query.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				teams.add(new Team(cursor.getInt(cursor.getColumnIndex(TableHelper.COLUMN_ID)), cursor.getString(cursor
						.getColumnIndex(TeamsTable.COLUMN_TEAM_NAME))));
				cursor.moveToNext();
			}
		}
		CustomLog.d(this.getClass(), "teams=" + cursor.getCount());
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return teams;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getTeamNames(Integer clubId) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT t.").append(TeamsTable.COLUMN_TEAM_NAME);
		query.append(" FROM ").append(TeamsTable.TABLE_NAME).append(" t, ").append(ClubsTable.TABLE_NAME).append(" c");
		query.append(" WHERE t.").append(TeamsTable.COLUMN_FK_CLUB_ID).append(" = c.").append(TableHelper.COLUMN_ID);
		query.append(" AND c.").append(TableHelper.COLUMN_ID).append(" = ?");
		query.append(" ORDER BY t.").append(TeamsTable.COLUMN_TEAM_NAME).append(" ASC");
		return getTeamNames(query.toString(), new String[] { clubId.toString() });
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getTeamNames(String clubName) {
		if (clubName == null) {
			throw new ValidationException(this.getClass().getName() + ".getTeam() arg clubName is null!");
		}
		StringBuffer query = new StringBuffer();
		query.append("SELECT t.").append(TeamsTable.COLUMN_TEAM_NAME);
		query.append(" FROM ").append(TeamsTable.TABLE_NAME).append(" t, ").append(ClubsTable.TABLE_NAME).append(" c");
		query.append(" WHERE t.").append(TeamsTable.COLUMN_FK_CLUB_ID).append(" = c.").append(TableHelper.COLUMN_ID);
		query.append(" AND c.").append(ClubsTable.COLUMN_CLUB_NAME).append(" LIKE ?");
		query.append(" ORDER BY t.").append(TeamsTable.COLUMN_TEAM_NAME).append(" ASC");
		return getTeamNames(query.toString(), new String[] { clubName });
	}

	private String[] getTeamNames(String sqlQuery, String[] selectionArgs) {
		String[] teamNames = new String[] {};
		CustomLog.d(this.getClass(), "sqlQuery=" + sqlQuery);
		this.database = getDatabase(false);
		Cursor cursor = this.database.rawQuery(sqlQuery, selectionArgs);
		if (cursor != null && cursor.getCount() > 0) {
			teamNames = new String[cursor.getCount()];
			int i = 0;
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				teamNames[i] = cursor.getString(cursor.getColumnIndex(TeamsTable.COLUMN_TEAM_NAME));
				cursor.moveToNext();
				i++;
			}
		}
		CustomLog.d(this.getClass(), "team names=" + cursor.getCount());
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return teamNames;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Team getTeam(Integer id) {
		if (id == null) {
			throw new ValidationException(this.getClass().getName() + ".getTeam() arg id is null!");
		}
		String selection = TableHelper.COLUMN_ID + " LIKE ?";
		String[] selectionArgs = { id.toString() };
		return getTeam(selection, selectionArgs);
	}

	public League getLeague(String name) {
		League league = null;
		String orderBy = LeaguesTable.COLUMN_LEAGUE_NAME + " ASC";
		String selection = LeaguesTable.COLUMN_LEAGUE_NAME + " = ?";
		String[] selectionArgs = { name };
		this.database = getDatabase(false);
		Cursor cursor = database.query(LeaguesTable.TABLE_NAME, LeaguesTable.TABLE_COLUMNS, selection, selectionArgs, null, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			league = mapCursorToLeague(cursor);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), league);
		return league;
	}

	private League getLeague(Integer id) {
		League league = null;
		String orderBy = LeaguesTable.COLUMN_LEAGUE_NAME + " ASC";
		String selection = TableHelper.COLUMN_ID + " = ?";
		String[] selectionArgs = { id.toString() };
		this.database = getDatabase(false);
		Cursor cursor = database.query(LeaguesTable.TABLE_NAME, LeaguesTable.TABLE_COLUMNS, selection, selectionArgs, null, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			league = mapCursorToLeague(cursor);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), league);
		return league;
	}

	private MatchEvent mapCursorToMatchEvent(Cursor cursor) {
		return new MatchEvent(cursor.getInt(cursor.getColumnIndex(TableHelper.COLUMN_ID)), cursor.getInt(cursor
				.getColumnIndex(MatchEventsTable.COLUMN_FK_MATCH_ID)), cursor.getInt(cursor.getColumnIndex(MatchEventsTable.COLUMN_PLAYED_MINUTES)),
				cursor.getString(cursor.getColumnIndex(MatchEventsTable.COLUMN_TEAM_NAME)), cursor.getString(cursor
						.getColumnIndex(MatchEventsTable.COLUMN_PLAYER_NAME)), cursor.getString(cursor
						.getColumnIndex(MatchEventsTable.COLUMN_MATCH_EVENT_TYPE_NAME)), cursor.getString(cursor.getColumnIndex(MatchEventsTable.COLUMN_VALUE)));
	}

	private League mapCursorToLeague(Cursor cursor) {
		return new League(cursor.getInt(cursor.getColumnIndex(TableHelper.COLUMN_ID)), cursor.getString(cursor.getColumnIndex(LeaguesTable.COLUMN_LEAGUE_NAME)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Team getTeam(String name) {
		if (name == null) {
			throw new ValidationException(this.getClass().getName() + ".getTeam() arg name is null!");
		}
		String selection = TeamsTable.COLUMN_TEAM_NAME + " LIKE ?";
		String[] selectionArgs = { name };
		CustomLog.d(this.getClass(), "team=" + name);
		return getTeam(selection, selectionArgs);
	}

	private Team getTeam(String selection, String[] selectionArgs) {
		Team team = null;
		String orderBy = TeamsTable.COLUMN_TEAM_NAME + " ASC";
		this.database = getDatabase(true);
		Cursor cursor = database.query(TeamsTable.TABLE_NAME, TeamsTable.TABLE_COLUMNS, selection, selectionArgs, null, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			Club club = getClub(cursor.getInt(cursor.getColumnIndex(TeamsTable.COLUMN_FK_CLUB_ID)));
			team = mapCursorToTeam(cursor, club);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.e(this.getClass(), "team=" + team);
		return team;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Contact getTeamContactPerson(int teamId, String roleTypeName) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT DISTINCT c._id");
		query.append(" FROM contacts c, contact_role_type_lnk l");
		query.append(" WHERE c.fk_team_id = ?");
		query.append(" AND l.fk_contact_id = c._id");
		query.append(" AND l.fk_role_type_id = ?");
		String[] selectionArgs = { Integer.toString(teamId), Integer.toString(RoleTypesEnum.valueOf(roleTypeName).getId()) };
		this.database = getDatabase(false);
		Cursor cursor = this.database.rawQuery(query.toString(), selectionArgs);
		int contactId = -1;
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			contactId = cursor.getInt(cursor.getColumnIndex(TableHelper.COLUMN_ID));
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		Contact contact = getContact(contactId);
		CustomLog.d(this.getClass(), "role=" + roleTypeName + ", contact=" + contact);
		return contact;
	}

	private Contact getContact(int contactId) {
		Contact contact = null;
		StringBuffer selection = new StringBuffer();
		selection.append(TableHelper.COLUMN_ID + " = ?");
		String[] selectionArgs = { Integer.toString(contactId) };
		this.database = getDatabase(false);
		Cursor cursor = database.query(ContactsTable.TABLE_NAME, ContactsTable.TABLE_COLUMNS, selection.toString(), selectionArgs, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			Team team = getTeam(cursor.getInt(cursor.getColumnIndex(ContactsTable.COLUMN_FK_TEAM_ID)));
			contact = mapCursorToContact(cursor, team);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "contactId=" + contactId + ", contact=" + contact);
		return contact;
	}

	@Override
	public Referee getReferee(int refereeId) {
		Referee referee = null;
		StringBuffer selection = new StringBuffer();
		selection.append(TableHelper.COLUMN_ID + " = ?");
		String[] selectionArgs = { Integer.toString(refereeId) };
		this.database = getDatabase(false);
		Cursor cursor = database.query(RefereesTable.TABLE_NAME, RefereesTable.TABLE_COLUMNS, selection.toString(), selectionArgs, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			referee = mapCursorToReferee(cursor);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "refereeId=" + refereeId + ", referee=" + referee);
		return referee;
	}

	/**
	 * 
	 * @param addressId
	 * @return
	 */
	@Override
	public Address getAddress(int addressId) {
		Address address = null;
		StringBuffer selection = new StringBuffer();
		selection.append(TableHelper.COLUMN_ID + " = ?");
		String[] selectionArgs = { Integer.toString(addressId) };
		this.database = getDatabase(false);
		Cursor cursor = database.query(AddressTable.TABLE_NAME, AddressTable.TABLE_COLUMNS, selection.toString(), selectionArgs, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			address = mapCursorToAddress(cursor);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "addressId=" + addressId + ", address=" + address);
		return address;
	}

	private Address getAddress(String streetName, String streetNumber, String streetNumberPostfix, String zipCode) {
		Address address = null;
		StringBuffer selection = new StringBuffer();
		selection.append(AddressTable.COLUMN_STREET_NAME + " = ?");
		selection.append(" AND ").append(AddressTable.COLUMN_STREET_NUMBER + " = ?");
		selection.append(" AND ").append(AddressTable.COLUMN_STREET_NUMBER_POSTFIX + " LIKE ?");
		selection.append(" AND ").append(AddressTable.COLUMN_ZIP_CODE + " = ?");

		String[] selectionArgs = { getValue(streetName), getValue(streetNumber), getValue(streetNumberPostfix), getValue(zipCode) };
		this.database = getDatabase(false);
		Cursor cursor = database.query(AddressTable.TABLE_NAME, AddressTable.TABLE_COLUMNS, selection.toString(), selectionArgs, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			address = mapCursorToAddress(cursor);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), address);
		return address;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Contact getContact(String firstName, String lastName) {
		Contact contact = null;
		CustomLog.d(this.getClass(), "firstName=" + firstName + ", lastName=" + lastName);
		StringBuffer selection = new StringBuffer();
		selection.append(ContactsTable.COLUMN_FIRST_NAME + " LIKE ?");
		selection.append(" AND ").append(ContactsTable.COLUMN_LAST_NAME + " LIKE ?");
		String[] selectionArgs = { firstName, lastName };
		this.database = getDatabase(false);
		Cursor cursor = database.query(ContactsTable.TABLE_NAME, ContactsTable.TABLE_COLUMNS, selection.toString(), selectionArgs, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			Team team = getTeam(cursor.getInt(cursor.getColumnIndex(ContactsTable.COLUMN_FK_TEAM_ID)));
			contact = mapCursorToContact(cursor, team);
			CustomLog.e(this.getClass(), contact.getFirstName() + " " + contact.getLastName() + " <> " + firstName + " " + lastName);
		}
		// FIXME Validate result, due to bug !
		if (contact != null) {
			if (!contact.getFirstName().equalsIgnoreCase(firstName) || !contact.getLastName().equalsIgnoreCase(lastName)) {
				CustomLog.e(this.getClass(), contact.getFirstName() + " " + contact.getLastName() + " <> " + firstName + " " + lastName);
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), contact);
		return contact;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Player lookupPlayerThroughContact(String mobileNr) {
		Player player = null;
		StringBuffer query = new StringBuffer();
		query.append("SELECT p.* FROM players p, contacts c, player_contact_lnk l");
		query.append(" WHERE c.mobile LIKE ?");
		query.append(" AND c._id == l.fk_contact_id");
		query.append(" AND p._id == l.fk_player_id");
		String[] selectionArgs = { mobileNr };
		this.database = getDatabase(false);
		Cursor cursor = database.rawQuery(query.toString(), selectionArgs);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			Address address = getAddress(cursor.getInt(cursor.getColumnIndex(PlayersTable.COLUMN_FK_ADDRESS_ID)));
			Team team = getTeam(cursor.getInt(cursor.getColumnIndex(PlayersTable.COLUMN_FK_TEAM_ID)));
			mapCursorToPlayer(cursor, team, address);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), player);
		return player;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Training> getTrainingList(Integer teamId, Integer periode) {
		List<Training> list = new ArrayList<Training>();
		String periodeSelection = getPeriodeSelectionClause(periode, System.currentTimeMillis());
		StringBuffer query = new StringBuffer();
		query.append("SELECT *");
		query.append(" FROM ").append(TrainingsTable.TABLE_NAME);
		query.append(" WHERE ").append(TrainingsTable.COLUMN_FK_TEAM_ID).append(" = ").append(teamId);
		query.append(" AND ").append(periodeSelection);
		query.append(" ORDER BY ").append(TrainingsTable.COLUMN_START_DATE).append(" ASC");
		CustomLog.d(this.getClass(), "query=" + query.toString());
		this.database = getDatabase(false);
		CustomLog.e(this.getClass(), query.toString());
		Cursor cursor = this.database.rawQuery(query.toString(), null);
		Team team = getTeam(teamId);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			Season season = getSeason(cursor.getInt(cursor.getColumnIndex(MatchesTable.COLUMN_FK_SEASON_ID)));
			while (!cursor.isAfterLast()) {
				list.add(mapCursorToTraining(cursor, team, season));
				cursor.moveToNext();
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "trainings=" + cursor.getCount());
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Training getTrainingByDate(int teamId, long startTime) {
		Training training = null;
		StringBuffer selection = new StringBuffer();
		selection.append(TrainingsTable.COLUMN_FK_TEAM_ID + " = ?");
		selection.append(" AND ").append(getPeriodeSelectionClause(Calendar.DAY_OF_YEAR, startTime));
		String[] selectionArgs = { Integer.toString(teamId) };
		this.database = getDatabase(false);
		Cursor cursor = database.query(TrainingsTable.TABLE_NAME, TrainingsTable.TABLE_COLUMNS, selection.toString(), selectionArgs, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			Team team = getTeam(teamId);
			Season season = getSeason(cursor.getInt(cursor.getColumnIndex(MatchesTable.COLUMN_FK_SEASON_ID)));
			training = mapCursorToTraining(cursor, team, season);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), training);
		return training;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Cup> getCupList(Integer teamId, Integer periode) {
		List<Cup> list = new ArrayList<Cup>();
		String periodeSelection = getPeriodeSelectionClause(periode, System.currentTimeMillis());
		StringBuffer query = new StringBuffer();
		query.append("SELECT *");
		query.append(" FROM ").append(CupsTable.TABLE_NAME);
		query.append(" WHERE ").append(periodeSelection);
		query.append(" ORDER BY ").append(CupsTable.COLUMN_START_DATE).append(" ASC");
		CustomLog.d(this.getClass(), "query=" + query.toString());
		this.database = getDatabase(false);
		Cursor cursor = database.rawQuery(query.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Season season = getSeason(cursor.getInt(cursor.getColumnIndex(MatchesTable.COLUMN_FK_SEASON_ID)));
				list.add(mapCursorToCup(cursor, season));
				cursor.moveToNext();
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "cups=" + cursor.getCount());
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Season> getSeasonList() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Season getCurrentSeason() {
		String selection = SeasonsTable.COLUMN_PERIOD + " LIKE ?";
		String[] selectionArgs = { "2013/2014" };
		return getSeason(selection, selectionArgs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Season getSeason(String period) {
		if (period == null) {
			CustomLog.e(this.getClass(), "periode is equal to null!");
			return null;
		}
		String selection = SeasonsTable.COLUMN_PERIOD + " LIKE ?";
		String[] selectionArgs = { period };
		return getSeason(selection, selectionArgs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Season getSeason(Integer seasonId) {
		String selection = TableHelper.COLUMN_ID + " = ?";
		String[] selectionArgs = { seasonId.toString() };
		return getSeason(selection, selectionArgs);
	}

	private Season getSeason(String selection, String[] selectionArgs) {
		Season season = null;
		CustomLog.d(this.getClass(), "selection=" + selection + ", args=" + selectionArgs[0]);
		this.database = getDatabase(false);
		Cursor cursor = database.query(SeasonsTable.TABLE_NAME, SeasonsTable.TABLE_COLUMNS, selection.toString(), selectionArgs, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			season = mapCursorToSeason(cursor);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), season);
		return season;
	}

	public Contact getContact(Integer contactId) {
		Contact contact = null;
		StringBuffer selection = new StringBuffer();
		selection.append(TableHelper.COLUMN_ID + " = ?");
		String[] selectionArgs = { contactId.toString() };
		this.database = getDatabase(false);
		Cursor cursor = database.query(ContactsTable.TABLE_NAME, ContactsTable.TABLE_COLUMNS, selection.toString(), selectionArgs, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			Team team = getTeam(cursor.getInt(cursor.getColumnIndex(ContactsTable.COLUMN_FK_TEAM_ID)));
			contact = mapCursorToContact(cursor, team);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "contactId=" + contactId + ", contace=" + contact);
		return contact;
	}

	public Player getPlayer(Integer playerId) {
		Player player = null;
		StringBuffer selection = new StringBuffer();
		selection.append(TableHelper.COLUMN_ID + " = ?");
		String[] selectionArgs = { playerId.toString() };
		this.database = getDatabase(false);
		Cursor cursor = database.query(PlayersTable.TABLE_NAME, PlayersTable.TABLE_COLUMNS, selection.toString(), selectionArgs, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			Address address = getAddress(cursor.getInt(cursor.getColumnIndex(PlayersTable.COLUMN_FK_ADDRESS_ID)));
			Team team = getTeam(cursor.getInt(cursor.getColumnIndex(PlayersTable.COLUMN_FK_TEAM_ID)));
			player = mapCursorToPlayer(cursor, team, address);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "playerId=" + playerId + ", player=" + player);
		return player;
	}

	@Override
	public List<Player> getPlayerList(Integer teamId) {
		List<Player> list = new ArrayList<Player>();
		String orderBy = PlayersTable.COLUMN_FIRST_NAME + " ASC";
		String selection = PlayersTable.COLUMN_FK_TEAM_ID + " = ?";
		String[] selectionArgs = { teamId.toString() };
		this.database = getDatabase(false);
		Cursor cursor = database.query(PlayersTable.TABLE_NAME, PlayersTable.TABLE_COLUMNS, selection, selectionArgs, null, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			Team team = getTeam(teamId);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Address address = getAddress(cursor.getInt(cursor.getColumnIndex(PlayersTable.COLUMN_FK_ADDRESS_ID)));
				list.add(mapCursorToPlayer(cursor, team, address));
				cursor.moveToNext();
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "players=" + cursor.getCount());
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Item> getContactsAsItemList(Integer teamId) {
		List<Item> list = new ArrayList<Item>();
		StringBuffer query = new StringBuffer();
		query.append("SELECT * FROM ").append(ContactsTable.TABLE_NAME);
		query.append(" WHERE ").append(ContactsTable.COLUMN_FK_TEAM_ID).append(" = ? ");
		String[] selectionArgs = { teamId.toString() };
		CustomLog.d(this.getClass(), "query=" + query.toString());
		this.database = getDatabase(false);
		Cursor cursor = database.rawQuery(query.toString(), selectionArgs);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String fullName = cursor.getString(cursor.getColumnIndex(ContactsTable.COLUMN_FIRST_NAME)) + " "
						+ cursor.getString(cursor.getColumnIndex(ContactsTable.COLUMN_LAST_NAME));
				list.add(new Item(cursor.getInt(cursor.getColumnIndex(TableHelper.COLUMN_ID)), fullName, false));
				cursor.moveToNext();
			}
		}
		CustomLog.d(this.getClass(), "contacts=" + cursor.getCount());
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	@Override
	public List<Item> getPlayersAsItemList(int teamId) {
		List<Item> list = new ArrayList<Item>();
		StringBuffer query = new StringBuffer();
		query.append("SELECT * FROM ").append(PlayersTable.TABLE_NAME);
		query.append(" WHERE ").append(PlayersTable.COLUMN_FK_TEAM_ID).append(" = ? ");
		String[] selectionArgs = { Integer.toString(teamId) };
		CustomLog.d(this.getClass(), "query=" + query.toString());
		this.database = getDatabase(false);
		Cursor cursor = database.rawQuery(query.toString(), selectionArgs);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String fullName = cursor.getString(cursor.getColumnIndex(PlayersTable.COLUMN_FIRST_NAME)) + " "
						+ cursor.getString(cursor.getColumnIndex(PlayersTable.COLUMN_LAST_NAME));
				list.add(new Item(cursor.getInt(cursor.getColumnIndex(TableHelper.COLUMN_ID)), fullName, false));
				cursor.moveToNext();
			}
		}
		CustomLog.d(this.getClass(), "players=" + cursor.getCount());
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	public Player lookupPlayer(String mobileNr) {
		Player player = null;
		StringBuffer selection = new StringBuffer();
		selection.append(ContactsTable.COLUMN_MOBILE + " LIKE ?");
		String[] selectionArgs = { mobileNr };
		this.database = getDatabase(false);
		Cursor cursor = database.query(PlayersTable.TABLE_NAME, PlayersTable.TABLE_COLUMNS, selection.toString(), selectionArgs, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			int playerId = cursor.getInt(cursor.getColumnIndex(TableHelper.COLUMN_ID));
			player = getPlayer(playerId);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "mobile=" + mobileNr + ", player=" + player);
		return player;
	}

	public int lookupMatchId(Integer teamId, String startDate) {
		int matchId = -1;
		String forDaySelection = getPeriodeSelectionClause(Calendar.DAY_OF_YEAR, System.currentTimeMillis());
		StringBuffer query = new StringBuffer();
		query.append("SELECT _id FROM ").append(MatchesTable.TABLE_NAME);
		query.append(" WHERE").append(MatchesTable.COLUMN_FK_TEAM_ID + " = ?");
		query.append(" AND ").append(forDaySelection);
		String[] selectionArgs = { teamId.toString(), startDate };
		this.database = getDatabase(false);
		Cursor cursor = database.rawQuery(query.toString(), selectionArgs);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			matchId = cursor.getInt(cursor.getColumnIndex(TableHelper.COLUMN_ID));
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "teamId=" + teamId + "startDate=" + startDate + ", matchId=" + matchId);
		return matchId;
	}

	// -----------------------------------------------------------------------------------------------
	// SettingsTable operations
	// -----------------------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getSettings(String type) {
		List<String> settings = new ArrayList<String>();
		String groupBy = null;
		String orderBy = null;
		String selection = SettingsTable.COLUMN_KEY + " LIKE ?";
		String[] selectionArgs = { type };
		this.database = getDatabase(false);
		Cursor cursor = database.query(SettingsTable.TABLE_NAME, SettingsTable.TABLE_COLUMNS, selection, selectionArgs, groupBy, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				settings.add(cursor.getString(cursor.getColumnIndex(SettingsTable.COLUMN_VALUE)));
				cursor.moveToNext();
			}
		}
		CustomLog.d(this.getClass(), settings);
		return settings;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSetting(String type) {
		Setting setting = null;
		String groupBy = null;
		String orderBy = null;
		String selection = SettingsTable.COLUMN_KEY + " LIKE ?";
		String[] selectionArgs = { type };
		this.database = getDatabase(false);
		Cursor cursor = database.query(SettingsTable.TABLE_NAME, SettingsTable.TABLE_COLUMNS, selection, selectionArgs, groupBy, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			setting = mapCursorToSetting(cursor);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		if (setting == null) {
			throw new RuntimeException("DB not initialized! Please report bug! Missing key in Settings table: " + type);
		}
		CustomLog.d(this.getClass(), setting);
		return setting.getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateSetting(String type, String value) {
		StringBuffer where = new StringBuffer();
		where.append(SettingsTable.COLUMN_KEY).append(" LIKE ?");
		String[] selectionArgs = { type };
		ContentValues values = SettingsTable.createContentValues(type, value);
		this.database = getDatabase(true);
		database.update(SettingsTable.TABLE_NAME, values, where.toString(), selectionArgs);
		CustomLog.d(this.getClass(), "Updated " + type + " = " + value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SearchResult search(String sqlQuery) {
		StringBuffer value = new StringBuffer();
		this.database = getDatabase(false);
		Cursor cursor = database.rawQuery(sqlQuery, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				for (String columnName : cursor.getColumnNames()) {
					if (cursor.getType(cursor.getColumnIndex(columnName)) == Cursor.FIELD_TYPE_INTEGER)
						value.append(columnName).append("=").append(cursor.getInt(cursor.getColumnIndex(columnName)));
					else {
						value.append(columnName).append("=").append(cursor.getString(cursor.getColumnIndex(columnName)));
					}
					value.append("\t");
				}
				value.append("\n");
				cursor.moveToNext();
			}
		} else {
			value.append("No hits for: " + sqlQuery);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "search=" + cursor.getCount());
		return new SearchResult(value.toString());
	}

	private String getPeriodeSelectionClause(Integer period, Long time) {
		String selectClause = null;
		Calendar cal = Calendar.getInstance();
		// Period equal to null, then select all time
		if (period == null) {
			// only those newer than today
			cal.setTimeInMillis(System.currentTimeMillis());
			selectClause = "start_date > " + (int) (cal.getTimeInMillis() / 1000);
		} else if (period == Calendar.YEAR) {
			int year = cal.get(Calendar.YEAR);
			selectClause = "strftime('%Y', datetime(start_date, 'unixepoch')) LIKE '" + year + "'";
		} else if (period == Calendar.MONTH) {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("MMyyyy", Locale.UK);
			String todayMMYYY = dateFormatter.format(time);
			selectClause = "strftime('%m%Y', datetime(start_date, 'unixepoch')) LIKE '" + todayMMYYY + "'";
		} else if (period == Calendar.WEEK_OF_YEAR) {
			int week = cal.get(Calendar.WEEK_OF_YEAR);
			selectClause = "strftime('%W', datetime(start_date, 'unixepoch')) LIKE '" + week + "'";
		} else if (period == Calendar.DAY_OF_YEAR) {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("ddMMyyyy", Locale.UK);
			String todayDDMMYYY = dateFormatter.format(time);
			selectClause = "strftime('%d%m%Y', datetime(start_date, 'unixepoch')) LIKE '" + todayDDMMYYY + "'";
		}
		return selectClause;
	}

	// -----------------------------------------------------------------------------------------------
	// Mappings
	// -----------------------------------------------------------------------------------------------

	private Cup mapCursorToCup(Cursor cursor, Season season) {
		long start_date_ms = ((long) cursor.getLong(cursor.getColumnIndex(CupsTable.COLUMN_START_DATE))) * 1000L;
		long deadline_date_ms = ((long) cursor.getLong(cursor.getColumnIndex(CupsTable.COLUMN_DEADLINE_DATE))) * 1000L;
		return new Cup(cursor.getInt(cursor.getColumnIndex(TableHelper.COLUMN_ID)), season, start_date_ms, cursor.getString(cursor
				.getColumnIndex(CupsTable.COLUMN_CUP_NAME)), cursor.getString(cursor.getColumnIndex(CupsTable.COLUMN_CLUB_NAME)), cursor.getString(cursor
				.getColumnIndex(CupsTable.COLUMN_VENUE)), deadline_date_ms);
	}

	private Match mapCursorToMatch(Cursor cursor, Team team, Season season) {
		long start_date_ms = ((long) cursor.getLong(cursor.getColumnIndex(MatchesTable.COLUMN_START_DATE))) * 1000L;
		MatchTypesEnum matchType = MatchTypesEnum.toType(cursor.getInt(cursor.getColumnIndex(MatchesTable.COLUMN_FK_MATCH_TYPE_ID)));
		Referee referee = getReferee(cursor.getInt(cursor.getColumnIndex(MatchesTable.COLUMN_FK_REFEREE_ID)));
		return new Match(cursor.getInt(cursor.getColumnIndex(TableHelper.COLUMN_ID)), season, start_date_ms, team, new Team(cursor.getString(cursor
				.getColumnIndex(MatchesTable.COLUMN_HOME_TEAM_NAME))), new Team(cursor.getString(cursor.getColumnIndex(MatchesTable.COLUMN_AWAY_TEAM_NAME))),
				cursor.getInt(cursor.getColumnIndex(MatchesTable.COLUMN_NUMBER_OF_GOALS_HOME_TEAM)), cursor.getInt(cursor
						.getColumnIndex(MatchesTable.COLUMN_NUMBER_OF_GOALS_AWAY_TEAM)), cursor.getString(cursor.getColumnIndex(MatchesTable.COLUMN_VENUE)),
				referee, matchType, cursor.getString(cursor.getColumnIndex(MatchesTable.COLUMN_MATCH_STATUS_NAME)));
	}

	private Training mapCursorToTraining(Cursor cursor, Team team, Season season) {
		long start_date_ms = ((long) cursor.getLong(cursor.getColumnIndex(TrainingsTable.COLUMN_START_DATE))) * 1000L;
		long end_time_ms = ((long) cursor.getInt(cursor.getColumnIndex(TrainingsTable.COLUMN_END_TIME))) * 1000L;
		return new Training(cursor.getInt(cursor.getColumnIndex(TableHelper.COLUMN_ID)), season, start_date_ms, end_time_ms, team, cursor.getString(cursor
				.getColumnIndex(TrainingsTable.COLUMN_PLACE)));
	}

	private Club mapCursorToClub(Cursor cursor) {
		Address address = getAddress(cursor.getInt(cursor.getColumnIndex(ClubsTable.COLUMN_FK_ADDRESS_ID)));
		return new Club(cursor.getInt(cursor.getColumnIndex(TableHelper.COLUMN_ID)), cursor.getString(cursor.getColumnIndex(ClubsTable.COLUMN_CLUB_NAME)),
				cursor.getString(cursor.getColumnIndex(ClubsTable.COLUMN_CLUB_DEPARTMENT_NAME)), cursor.getString(cursor
						.getColumnIndex(ClubsTable.COLUMN_CLUB_NAME_ABBREVIATION)),
				cursor.getString(cursor.getColumnIndex(ClubsTable.COLUMN_CLUB_STADIUM_NAME)), address, cursor.getString(cursor
						.getColumnIndex(ClubsTable.COLUMN_CLUB_URL_HOME_PAGE))

		);
	}

	private Team mapCursorToTeam(Cursor cursor, Club club) {
		Team team = new Team(cursor.getInt(cursor.getColumnIndex(TableHelper.COLUMN_ID)), cursor.getString(cursor.getColumnIndex(TeamsTable.COLUMN_TEAM_NAME)),
				club);
		team.setGender(cursor.getString(cursor.getColumnIndex(TeamsTable.COLUMN_TEAM_GENDER)));
		team.setTeamYearOfBirth(cursor.getInt(cursor.getColumnIndex(TeamsTable.COLUMN_TEAM_YEAR_OF_BIRTH)));
		League league = getLeague(cursor.getInt(cursor.getColumnIndex(TeamsTable.COLUMN_FK_LEAGUE_ID)));
		team.setLeague(league);
		return team;
	}

	private Setting mapCursorToSetting(Cursor cursor) {
		return new Setting(cursor.getString(cursor.getColumnIndex(SettingsTable.COLUMN_KEY)), cursor.getString(cursor
				.getColumnIndex(SettingsTable.COLUMN_VALUE)));
	}

	private Season mapCursorToSeason(Cursor cursor) {
		return new Season(cursor.getInt(cursor.getColumnIndex(TableHelper.COLUMN_ID)), cursor.getString(cursor.getColumnIndex(SeasonsTable.COLUMN_PERIOD)),
				cursor.getLong(cursor.getColumnIndex(SeasonsTable.COLUMN_START_DATE)), cursor.getLong(cursor.getColumnIndex(SeasonsTable.COLUMN_END_DATE)));
	}

	private Address mapCursorToAddress(Cursor cursor) {
		return new Address(cursor.getInt(cursor.getColumnIndex(TableHelper.COLUMN_ID)),
				cursor.getString(cursor.getColumnIndex(AddressTable.COLUMN_STREET_NAME)), cursor.getString(cursor
						.getColumnIndex(AddressTable.COLUMN_STREET_NUMBER)),
				cursor.getString(cursor.getColumnIndex(AddressTable.COLUMN_STREET_NUMBER_POSTFIX)), cursor.getString(cursor
						.getColumnIndex(AddressTable.COLUMN_ZIP_CODE)), cursor.getString(cursor.getColumnIndex(AddressTable.COLUMN_CITY)),
				cursor.getString(cursor.getColumnIndex(AddressTable.COLUMN_COUNTRY)));
	}

	private Contact mapCursorToContact(Cursor cursor, Team team) {
		Address address = getAddress(cursor.getInt(cursor.getColumnIndex(ContactsTable.COLUMN_FK_ADDRESS_ID)));
		List<RoleTypesEnum> roles = getRoles(cursor.getInt(cursor.getColumnIndex(TableHelper.COLUMN_ID)));
		Contact contact = new Contact(cursor.getInt(cursor.getColumnIndex(TableHelper.COLUMN_ID)), team, address, roles, cursor.getString(cursor
				.getColumnIndex(ContactsTable.COLUMN_FIRST_NAME)), cursor.getString(cursor.getColumnIndex(ContactsTable.COLUMN_MIDDLE_NAME)),
				cursor.getString(cursor.getColumnIndex(ContactsTable.COLUMN_LAST_NAME)), cursor.getString(cursor.getColumnIndex(ContactsTable.COLUMN_GENDER)),
				cursor.getString(cursor.getColumnIndex(ContactsTable.COLUMN_MOBILE)), cursor.getString(cursor.getColumnIndex(ContactsTable.COLUMN_EMAIL)));
		return contact;
	}

	private Referee mapCursorToReferee(Cursor cursor) {
		Address address = getAddress(cursor.getInt(cursor.getColumnIndex(RefereesTable.COLUMN_FK_ADDRESS_ID)));
		Referee referee = new Referee(cursor.getInt(cursor.getColumnIndex(TableHelper.COLUMN_ID)), cursor.getString(cursor
				.getColumnIndex(RefereesTable.COLUMN_FIRST_NAME)), cursor.getString(cursor.getColumnIndex(RefereesTable.COLUMN_MIDDLE_NAME)),
				cursor.getString(cursor.getColumnIndex(RefereesTable.COLUMN_LAST_NAME)), cursor.getString(cursor.getColumnIndex(RefereesTable.COLUMN_GENDER)),
				address, cursor.getString(cursor.getColumnIndex(RefereesTable.COLUMN_MOBILE)), cursor.getString(cursor
						.getColumnIndex(RefereesTable.COLUMN_EMAIL)));
		return referee;
	}

	private Player mapCursorToPlayer(Cursor cursor, Team team, Address address) {
		List<Contact> parents = getRelations(cursor.getInt(cursor.getColumnIndex(TableHelper.COLUMN_ID)), team);
		long dateOfBirth = ((long) cursor.getLong(cursor.getColumnIndex(PlayersTable.COLUMN_DATE_OF_BIRTH))) * 1000L;

		Player player = new Player(cursor.getInt(cursor.getColumnIndex(TableHelper.COLUMN_ID)), team, cursor.getString(cursor
				.getColumnIndex(PlayersTable.COLUMN_FIRST_NAME)), cursor.getString(cursor.getColumnIndex(PlayersTable.COLUMN_MIDDLE_NAME)),
				cursor.getString(cursor.getColumnIndex(PlayersTable.COLUMN_LAST_NAME)), cursor.getString(cursor.getColumnIndex(PlayersTable.COLUMN_GENDER)),
				PlayerStatusEnum.valueOf(cursor.getString(cursor.getColumnIndex(PlayersTable.COLUMN_STATUS))), parents, dateOfBirth, address);
		player.setEmailAddress(cursor.getString(cursor.getColumnIndex(PlayersTable.COLUMN_EMAIL)));
		player.setMobileNumber(cursor.getString(cursor.getColumnIndex(PlayersTable.COLUMN_MOBILE)));
		player.setSchoolName(null);
		return player;

	}

	// **********************************************************************************************
	// Link tables operations
	// **********************************************************************************************

	@Override
	public int getNumberOfSignedPlayers(PlayerLinkTableTypeEnum type, int id) {
		int numberOfPlayers = 0;
		StringBuffer sqlQuery = new StringBuffer();
		if (type == PlayerLinkTableTypeEnum.MATCH) {
			sqlQuery.append("SELECT count(fk_match_id) AS numberOfPlayers FROM player_match_lnk WHERE fk_match_id = ?");
		} else if (type == PlayerLinkTableTypeEnum.CUP) {
			sqlQuery.append("SELECT count(fk_cup_id) AS numberOfPlayers FROM player_cup_lnk WHERE fk_cup_id = ?");
		} else if (type == PlayerLinkTableTypeEnum.TRAINING) {
			sqlQuery.append("SELECT count(fk_training_id) AS numberOfPlayers FROM player_training_lnk WHERE fk_training_id = ?");
		} else {
			throw new ApplicationException("Invalid link table type: " + type);
		}
		String[] selectionArgs = { Integer.toString(id) };
		CustomLog.d(this.getClass(), "sqlQuery=" + sqlQuery.toString());
		this.database = getDatabase(false);
		Cursor cursor = database.rawQuery(sqlQuery.toString(), selectionArgs);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			numberOfPlayers = cursor.getInt(cursor.getColumnIndex("numberOfPlayers"));
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "number of players: " + numberOfPlayers);
		return numberOfPlayers;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deletePlayerLink(PlayerLinkTableTypeEnum type, Integer playerId, Integer id) {
		String linkToId = WILD_CARD;
		if (id != null) {
			linkToId = id.toString();
		}

		if (type == PlayerLinkTableTypeEnum.CONTACT) {
			return deleteLink(PlayerContactLnkTable.TABLE_NAME, PlayerContactLnkTable.getTableColumns(), playerId.toString(), linkToId);
		} else if (type == PlayerLinkTableTypeEnum.MATCH) {
			return deleteLink(PlayerMatchLnkTable.TABLE_NAME, PlayerMatchLnkTable.getTableColumns(), playerId.toString(), linkToId);
		} else if (type == PlayerLinkTableTypeEnum.CUP) {
			return deleteLink(PlayerCupLnkTable.TABLE_NAME, PlayerCupLnkTable.getTableColumns(), playerId.toString(), linkToId);
		} else if (type == PlayerLinkTableTypeEnum.TRAINING) {
			return deleteLink(PlayerTrainingLnkTable.TABLE_NAME, PlayerTrainingLnkTable.getTableColumns(), playerId.toString(), linkToId);
		} else {
			throw new ApplicationException("Invalid link table type: " + type);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createPlayerLink(PlayerLinkTableTypeEnum type, int playerId, int id) {
		if (type == PlayerLinkTableTypeEnum.CONTACT) {
			createLink(PlayerContactLnkTable.TABLE_NAME, PlayerContactLnkTable.createContentValues(playerId, id));
		} else if (type == PlayerLinkTableTypeEnum.MATCH) {
			createLink(PlayerMatchLnkTable.TABLE_NAME, PlayerMatchLnkTable.createContentValues(playerId, id));
		} else if (type == PlayerLinkTableTypeEnum.CUP) {
			createLink(PlayerCupLnkTable.TABLE_NAME, PlayerCupLnkTable.createContentValues(playerId, id));
		} else if (type == PlayerLinkTableTypeEnum.TRAINING) {
			createLink(PlayerTrainingLnkTable.TABLE_NAME, PlayerTrainingLnkTable.createContentValues(playerId, id));
		} else {
			throw new ApplicationException("Invalid link table type: " + type);
		}
	}

	private long createLink(String tableName, ContentValues contentValues) {
		this.database = getDatabase(true);
		long id = database.insertOrThrow(tableName, null, contentValues);
		CustomLog.d(this.getClass(), "created=" + tableName + ", values=" + contentValues.toString());
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long createContactRoleTypeLnk(int contactId, int roleTypeIdId) {
		ContentValues contentValues = ContactRoleTypeLnkTable.createContentValues(contactId, roleTypeIdId);
		return createLink(ContactRoleTypeLnkTable.TABLE_NAME, contentValues);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteContactRoleTypeLnk(int contactId, int roleTypeIdId) {
		return deleteLink(ContactRoleTypeLnkTable.TABLE_NAME, ContactRoleTypeLnkTable.getIdTableColumns(), Integer.toString(contactId),
				Integer.toString(roleTypeIdId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createCupMatchLnk(int cupId, int matchId) {
		this.database = getDatabase(true);
		ContentValues contentValues = CupMatchLnkTable.createContentValues(cupId, matchId);
		long id = database.insertOrThrow(CupMatchLnkTable.TABLE_NAME, null, contentValues);
		CustomLog.d(this.getClass(), "created=" + CupMatchLnkTable.TABLE_NAME + ", values=" + contentValues.toString());
		return Long.valueOf(id).intValue();
	}

	private int deleteLink(String tableName, String[] colNames, String id1, String id2) {
		String whereClause = colNames[0] + " LIKE ? AND " + colNames[1] + " LIKE ?";
		String[] whereArgs = { id1, id2 };
		this.database = getDatabase(true);
		int id = database.delete(tableName, whereClause, whereArgs);
		CustomLog.d(this.getClass(), "deleted=" + tableName + ", values=" + id1 + ", " + id2);
		return id;
	}

	private List<Contact> getRelations(Integer id, Team team) {
		CustomLog.d(this.getClass(), "id=" + id);
		List<Contact> relationList = new ArrayList<Contact>();
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT c.* FROM ").append(ContactsTable.TABLE_NAME).append(" c,").append(PlayerContactLnkTable.TABLE_NAME).append(" p");
		sqlQuery.append(" WHERE p.").append(PlayerContactLnkTable.COLUMN_FK_PLAYER_ID).append(" = ?");
		sqlQuery.append(" AND c.").append(TableHelper.COLUMN_ID).append(" = p.").append(PlayerContactLnkTable.COLUMN_FK_CONTACT_ID);
		String[] selectionArgs = { id.toString() };
		CustomLog.d(this.getClass(), "sqlQuery=" + sqlQuery.toString());
		this.database = getDatabase(false);
		Cursor cursor = database.rawQuery(sqlQuery.toString(), selectionArgs);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Contact contact = mapCursorToContact(cursor, team);
				if (contact != null) {
					relationList.add(contact);
				}
				cursor.moveToNext();
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), relationList);
		return relationList;
	}

	private List<RoleTypesEnum> getRoles(Integer contactId) {
		List<RoleTypesEnum> roleList = new ArrayList<RoleTypesEnum>();
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT r.role_type_name AS role_type_name");
		sqlQuery.append(" FROM contact_role_type_lnk l, role_types r");
		sqlQuery.append(" WHERE l.fk_contact_id = ?");
		sqlQuery.append(" AND r._id = l.fk_role_type_id");
		sqlQuery.append(" ORDER BY r.role_type_name ASC");
		String[] selectionArgs = { contactId.toString() };
		this.database = getDatabase(false);
		Cursor cursor = database.rawQuery(sqlQuery.toString(), selectionArgs);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String roleTypeName = cursor.getString(cursor.getColumnIndex("role_type_name"));
				roleList.add(RoleTypesEnum.valueOf(roleTypeName.toUpperCase()));
				cursor.moveToNext();
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), roleList);
		return roleList;
	}

	// Statistic

	@Override
	public Statistic getPlayerStatistic(int teamId, int playerId, int seasonId) {
		Statistic statistic = null;
		CustomLog.d(this.getClass(), "teamId=" + teamId + ", playerId=" + playerId);
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT");
		sqlQuery.append(" (SELECT c.club_name FROM clubs c, teams t WHERE t._id = ? AND t.fk_club_id = c._id) AS clubName,");
		sqlQuery.append(" (SELECT t.team_name FROM teams t WHERE t._id = ?) AS teamName,");
		sqlQuery.append(" (SELECT count(fk_player_id) FROM player_match_lnk WHERE fk_player_id = ?) AS numberOfPlayerMatches,");
		sqlQuery.append(" (SELECT count(fk_player_id) FROM player_cup_lnk WHERE fk_player_id = ?) AS numberOfPlayerCups,");
		sqlQuery.append(" (SELECT count(fk_player_id) FROM player_training_lnk WHERE fk_player_id = ?) AS numberOfPlayerTrainings,");
		sqlQuery.append(" (SELECT count(fk_team_id) FROM matches WHERE fk_team_id = ?) AS numberOfTeamMatches,");
		sqlQuery.append(" (SELECT count(_id) FROM cups WHERE _id = ?) AS numberOfTeamCups,");
		sqlQuery.append(" (SELECT count(fk_team_id) FROM trainings WHERE fk_team_id = ?) AS numberOfTeamTrainings");
		String[] selectionArgs = { Integer.toString(teamId), Integer.toString(teamId), Integer.toString(playerId), Integer.toString(playerId),
				Integer.toString(playerId), Integer.toString(teamId), Integer.toString(teamId), Integer.toString(teamId) };
		CustomLog.d(this.getClass(), "sqlQuery=" + sqlQuery.toString());
		this.database = getDatabase(false);
		Cursor cursor = database.rawQuery(sqlQuery.toString(), selectionArgs);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				statistic = new Statistic(cursor.getString(cursor.getColumnIndex("clubName")), cursor.getString(cursor.getColumnIndex("teamName")), playerId,
						cursor.getInt(cursor.getColumnIndex("numberOfPlayerMatches")), cursor.getInt(cursor.getColumnIndex("numberOfPlayerCups")),
						cursor.getInt(cursor.getColumnIndex("numberOfPlayerTrainings")), cursor.getInt(cursor.getColumnIndex("numberOfTeamMatches")),
						cursor.getInt(cursor.getColumnIndex("numberOfTeamCups")), cursor.getInt(cursor.getColumnIndex("numberOfTeamTrainings")));
				cursor.moveToNext();
			}
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), statistic);
		return statistic;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Statistic getTeamStatistic(int teamId, int seasonId) {
		List<MatchStatistic> matchStatisticList = new ArrayList<MatchStatistic>();
		CustomLog.d(this.getClass(), "teamId=" + teamId + ", seasonId=" + seasonId);
		String sqlQuery = createTeamMatchStatisticQuery();
		String[] selectionArgs = { Integer.toString(seasonId), Integer.toString(teamId) };
		CustomLog.d(this.getClass(), "sqlQuery=" + sqlQuery);
		this.database = getDatabase(false);
		Cursor cursor = database.rawQuery(sqlQuery, selectionArgs);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				MatchStatistic matchStatistic = new MatchStatistic(cursor.getInt(cursor.getColumnIndex("seasonId")), cursor.getString(cursor
						.getColumnIndex("teamName")), cursor.getInt(cursor.getColumnIndex("matchTypeId")), cursor.getInt(cursor
						.getColumnIndex("numberOfPlayedMatches")), cursor.getInt(cursor.getColumnIndex("numberOfWonMatches")), cursor.getInt(cursor
						.getColumnIndex("numberOfDrawMatches")), cursor.getInt(cursor.getColumnIndex("numberOfLossMatches")), cursor.getInt(cursor
						.getColumnIndex("numberOfGoalsScored")), cursor.getInt(cursor.getColumnIndex("numberOfGoalsAgainst")));
				CustomLog.d(this.getClass(), matchStatistic);
				matchStatisticList.add(matchStatistic);
				cursor.moveToNext();
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		Statistic statistic = new Statistic(matchStatisticList);
		CustomLog.d(this.getClass(), statistic);
		return statistic;
	}

	private String createTeamMatchStatisticQuery() {
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT season_id AS seasonId, team_name AS teamName, match_type_id AS matchTypeId,");
		sqlQuery.append(" sum( numberOfPlayedMatches ) AS numberOfPlayedMatches,");
		sqlQuery.append(" sum( numberOfWonMatches ) AS numberOfWonMatches,");
		sqlQuery.append(" sum( numberOfDrawMatches ) AS numberOfDrawMatches,");
		sqlQuery.append(" sum( numberOfLossMatches ) AS numberOfLossMatches,");
		sqlQuery.append(" sum( scored ) AS numberOfGoalsScored,");
		sqlQuery.append(" sum( against ) AS numberOfGoalsAgainst");
		sqlQuery.append(" FROM match_result_view");
		sqlQuery.append(" WHERE season_id = ?");
		sqlQuery.append(" AND team_id = ?");
		sqlQuery.append(" GROUP BY match_type_id;");
		return sqlQuery.toString();
	}

	/**
	 * 
	 * @return
	 */
	@Deprecated
	private String createTeamMatchStatisticQueryDeprecated() {
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT fk_match_type_id AS matchTypeId, count(fk_team_id) AS numberOfMatches,");
		sqlQuery.append(" sum(goals_home_team > goals_away_team) AS numberOfWonMatches,");
		sqlQuery.append(" sum(goals_home_team = goals_away_team) AS numberOfDrawMatches,");
		sqlQuery.append(" sum(goals_home_team < goals_away_team) AS numberOfLossMatches,");
		sqlQuery.append(" sum(goals_home_team) AS numberOfGoalsScored,");
		sqlQuery.append(" sum(goals_away_team) AS numberOfGoalsAgainst");
		sqlQuery.append(" FROM matches");
		sqlQuery.append("WHERE fk_team_id = ?");
		sqlQuery.append(" AND fk_season_id = ?");
		sqlQuery.append(" GROUP BY fk_match_type_id");
		return sqlQuery.toString();
	}

	@Deprecated
	private String createTeamMatchStatisticQueryDeprecated2() {
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT m.match_type_id AS matchTypeId, count(m.fk_team_id) AS numberOfMatches,");
		sqlQuery.append(" sum(m.home_team = t.team_name AND m.goals_home_team > m.goals_away_team) AS numberOfWonHomeMatches,");
		sqlQuery.append(" sum(m.home_team = t.team_name AND m.goals_home_team = m.goals_away_team) AS numberOfDrawHomeMatches,");
		sqlQuery.append(" sum(m.home_team = t.team_name AND m.goals_home_team < m.goals_away_team) AS numberOfLossHomeMatches,");
		sqlQuery.append(" sum(m.away_team = t.team_name AND m.goals_away_team > m.goals_home_team) AS numberOfWonAwayMatches,");
		sqlQuery.append(" sum(m.away_team = t.team_name AND m.goals_away_team = m.goals_home_team) AS numberOfDrawAwayMatches,");
		sqlQuery.append(" sum(m.away_team = t.team_name AND m.goals_away_team < m.goals_home_team) AS numberOfLossAwayMatches,");
		sqlQuery.append(" FROM matches m, teams t");
		sqlQuery.append(" WHERE m.fk_team_id = ?");
		sqlQuery.append(" AND m.fk_team_id = t._id");
		sqlQuery.append(" AND m.fk_season_id = ?");
		sqlQuery.append(" GROUP BY m.match_type_id");
		return sqlQuery.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createView() {
		MatchResultView.onUpgrade(this.database, BandyDataBaseHjelper.DATABASE_VERSION, BandyDataBaseHjelper.DATABASE_VERSION);
	}

	@Override
	public String getSqlQuery(String id, String type) {
		return createTeamMatchStatisticQuery();
	}

	private String createTeamScoredGoalsQuery(String teamColumnName) {
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT m.match_type_id AS matchTypeId,");
		sqlQuery.append(" sum(m.goals_home_team) AS numberOfGoalsHome");
		sqlQuery.append(" sum(m.goals_away_team) AS numberOfGoalsAway");
		sqlQuery.append(" FROM matches m");
		sqlQuery.append(" WHERE m.fk_team_id = ?");
		sqlQuery.append(" AND m.fk_season_id = ?");
		sqlQuery.append(" AND m.").append(teamColumnName).append(" = ?");
		sqlQuery.append(" GROUP BY m.match_type_id");
		return sqlQuery.toString();
	}

	private String createAwayTeamScoredGoalsQuery() {
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT m.match_type_id AS matchTypeId,");
		sqlQuery.append(" sum(m.goals_home_team) AS numberOfGoalsAgainst");
		sqlQuery.append(" sum(m.goals_away_team) AS numberOfGoalsScored");
		sqlQuery.append(" FROM matches m");
		sqlQuery.append(" WHERE m.fk_team_id = ?");
		sqlQuery.append(" AND m.fk_season_id = ?");
		sqlQuery.append(" AND m.away_team = ?");
		sqlQuery.append(" GROUP BY m.match_type_id");
		return sqlQuery.toString();
	}

	private String getValue(String param) {
		String value = WILD_CARD;
		if (param != null && param.length() > 0) {
			value = param;
		}
		return value;
	}
}
