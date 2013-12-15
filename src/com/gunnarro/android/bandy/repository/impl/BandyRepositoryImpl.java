package com.gunnarro.android.bandy.repository.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.domain.Contact;
import com.gunnarro.android.bandy.domain.Contact.ContactRoleEnum;
import com.gunnarro.android.bandy.domain.Cup;
import com.gunnarro.android.bandy.domain.Match;
import com.gunnarro.android.bandy.domain.Player;
import com.gunnarro.android.bandy.domain.Player.PlayerStatusEnum;
import com.gunnarro.android.bandy.domain.Referee;
import com.gunnarro.android.bandy.domain.Role;
import com.gunnarro.android.bandy.domain.Setting;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.Training;
import com.gunnarro.android.bandy.repository.BandyDataBaseHjelper;
import com.gunnarro.android.bandy.repository.BandyRepository;
import com.gunnarro.android.bandy.repository.table.ClubsTable;
import com.gunnarro.android.bandy.repository.table.ContactsTable;
import com.gunnarro.android.bandy.repository.table.CupsTable;
import com.gunnarro.android.bandy.repository.table.MatchesTable;
import com.gunnarro.android.bandy.repository.table.PlayersTable;
import com.gunnarro.android.bandy.repository.table.RelationshipsTable;
import com.gunnarro.android.bandy.repository.table.RolesTable;
import com.gunnarro.android.bandy.repository.table.SettingsTable;
import com.gunnarro.android.bandy.repository.table.TeamsTable;
import com.gunnarro.android.bandy.repository.table.TrainingsTable;
import com.gunnarro.android.bandy.service.exception.ApplicationException;

public class BandyRepositoryImpl implements BandyRepository {

	// Database fields
	private SQLiteDatabase database;
	private BandyDataBaseHjelper dbHelper;

	public BandyRepositoryImpl(Context context) {
		dbHelper = BandyDataBaseHjelper.getInstance(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
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
		this.database = dbHelper.getWritableDatabase();
		database.delete(ClubsTable.TABLE_NAME, null, null);
		database.delete(ContactsTable.TABLE_NAME, null, null);
		database.delete(CupsTable.TABLE_NAME, null, null);
		database.delete(MatchesTable.TABLE_NAME, null, null);
		database.delete(PlayersTable.TABLE_NAME, null, null);
		database.delete(RelationshipsTable.TABLE_NAME, null, null);
		database.delete(RolesTable.TABLE_NAME, null, null);
		// database.delete(SettingsTable.TABLE_NAME, null, null);
		database.delete(TeamsTable.TABLE_NAME, null, null);
		database.delete(TrainingsTable.TABLE_NAME, null, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createClub(Club club) {
		ContentValues values = ClubsTable.createContentValues(club.getName());
		this.database = dbHelper.getWritableDatabase();
		database.insert(ClubsTable.TABLE_NAME, null, values);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createTeam(Team team) {
		ContentValues values = TeamsTable.createContentValues(team.getClub().getId(), team.getName());
		this.database = dbHelper.getWritableDatabase();
		database.insert(TeamsTable.TABLE_NAME, null, values);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createMatch(Match match) {
		ContentValues values = MatchesTable.createContentValues(match.getTeam().getId(), match.getStartDate(), match.getHomeTeam().getName(), match
				.getAwayTeam().getName(), match.getVenue(), match.getReferee().getFullName());
		this.database = dbHelper.getWritableDatabase();
		database.insert(MatchesTable.TABLE_NAME, null, values);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createCup(Cup cup) {
		ContentValues values = CupsTable.createContentValues(cup.getStartDate(), cup.getCupName(), cup.getClubName(), cup.getVenue(), cup.getDeadlineDate());
		this.database = dbHelper.getWritableDatabase();
		database.insert(CupsTable.TABLE_NAME, null, values);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createTraining(Training training) {
		ContentValues values = TrainingsTable.createContentValues(training.getTeam().getId(), training.getStartDate(), training.getEndTime(),
				training.getVenue());
		this.database = dbHelper.getWritableDatabase();
		database.insert(TrainingsTable.TABLE_NAME, null, values);
		return true;
	}

	@Override
	public void createPlayer(Player player) {
		ContentValues playerValues = PlayersTable.createContentValues(player.getTeam().getId(), player.getStatus().name(), player.getFirstName(),
				player.getMiddleName(), player.getLastName(), player.getDateOfBirth());
		this.database = dbHelper.getWritableDatabase();
		long playerId = database.insert(PlayersTable.TABLE_NAME, null, playerValues);
		if (player.getParents() != null) {
			for (Contact parent : player.getParents()) {
				Contact contact = getContact(parent.getFirstName(), parent.getLastName());
				if (contact != null) {
					ContentValues relationshipsValues = RelationshipsTable.createContentValues(Long.valueOf(playerId).intValue(), contact.getId());
					database.insert(RelationshipsTable.TABLE_NAME, null, relationshipsValues);
				} else {
					CustomLog.e(this.getClass(), "No contact found for: " + parent.getFirstName() + " " + parent.getLastName());
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createContact(Contact contact) {
		ContentValues contactValues = ContactsTable.createContentValues(contact.getTeam().getId(), contact.getFirstName(), contact.getMiddleName(),
				contact.getLastName(), contact.getMobileNumber(), contact.getEmailAddress());
		this.database = dbHelper.getWritableDatabase();
		long contactId = database.insert(ContactsTable.TABLE_NAME, null, contactValues);
		if (contact.getRoles() != null) {
			for (ContactRoleEnum role : contact.getRoles()) {
				ContentValues roleValues = RolesTable.createContentValues(role.name(), Long.valueOf(contactId).intValue());
				database.insert(RolesTable.TABLE_NAME, null, roleValues);
			}
		}
		return true;
	}

	// -------------------------------------------------------------------------------------------------

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public List<Contact> getContactList(Integer teamId, String role) {
		List<Contact> list = new ArrayList<Contact>();
		String orderBy = ContactsTable.COLUMN_LAST_NAME + " COLLATE LOCALIZED ASC";
		String selection = ContactsTable.COLUMN_FK_TEAM_ID + " = ?";
		String[] selectionArgs = { teamId.toString() };
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(ContactsTable.TABLE_NAME, ContactsTable.TABLE_COLUMNS, selection, selectionArgs, null, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				list.add(mapCursorToContact(cursor));
				cursor.moveToNext();
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "contacts=" + cursor.getCount());
		return list;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public List<Match> getMatchList(Integer teamId, String periode) {
		List<Match> list = new ArrayList<Match>();
		String periodeSelection = getPeriodeSelectionClause(periode);
		StringBuffer query = new StringBuffer();
		query.append("SELECT *");
		query.append(" FROM ").append(MatchesTable.TABLE_NAME);
		query.append(" WHERE ").append(MatchesTable.COLUMN_FK_TEAM_ID).append(" = ").append(teamId);
		query.append(" AND ").append(periodeSelection);
		query.append(" ORDER BY ").append(MatchesTable.COLUMN_START_DATE).append(" COLLATE LOCALIZED ASC");
		CustomLog.d(this.getClass(), "query=" + query.toString());
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery(query.toString(), null);
		Team team = getTeam(teamId);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				list.add(mapCursorToMatch(cursor, team));
				cursor.moveToNext();
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "matches=" + cursor.getCount());
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Club getClub(String name) {
		String selection = ClubsTable.COLUMN_CLUB_NAME + " LIKE ?";
		String[] selectionArgs = { name };
		return getClub(selection, selectionArgs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Club getClub(Integer id) {
		String selection = ClubsTable.COLUMN_ID + " LIKE ?";
		String[] selectionArgs = { id.toString() };
		return getClub(selection, selectionArgs);
	}

	private Club getClub(String selection, String[] selectionArgs) {
		Club club = null;
		String orderBy = ClubsTable.COLUMN_CLUB_NAME + " COLLATE LOCALIZED ASC";
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(ClubsTable.TABLE_NAME, ClubsTable.TABLE_COLUMNS, selection, selectionArgs, null, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			club = mapCursorToClub(cursor);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "club=" + club);
		return club;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Team getTeam(Integer id) {
		String selection = TeamsTable.COLUMN_ID + " LIKE ?";
		String[] selectionArgs = { id.toString() };
		return getTeam(selection, selectionArgs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Team getTeam(String name) {
		String selection = TeamsTable.COLUMN_TEAM_NAME + " LIKE ?";
		String[] selectionArgs = { name };
		CustomLog.d(this.getClass(), "team=" + name);
		return getTeam(selection, selectionArgs);
	}

	private Team getTeam(String selection, String[] selectionArgs) {
		Team team = null;
		String orderBy = TeamsTable.COLUMN_TEAM_NAME + " COLLATE LOCALIZED ASC";
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(TeamsTable.TABLE_NAME, TeamsTable.TABLE_COLUMNS, selection, selectionArgs, null, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			team = mapCursorToTeam(cursor);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "team=" + team);
		return team;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Contact getTeamContactPerson(int teamId, String role) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT c.").append(ContactsTable.COLUMN_ID);
		query.append(" FROM ").append(ContactsTable.TABLE_NAME).append(" c,").append(RolesTable.TABLE_NAME).append(" r");
		query.append(" WHERE c.").append(ContactsTable.COLUMN_FK_TEAM_ID).append(" = ").append(teamId);
		query.append(" AND c.").append(ContactsTable.COLUMN_ID).append(" = r.").append(RolesTable.COLUMN_FK_CONTACT_ID);
		query.append(" AND r.").append(RolesTable.COLUMN_ROLE).append(" LIKE '").append(role.toLowerCase()).append("'");
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = this.database.rawQuery(query.toString(), null);
		int contactId = -1;
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			contactId = cursor.getInt(cursor.getColumnIndex(TeamsTable.COLUMN_ID));
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		Contact teamlead = getContact(contactId);
		CustomLog.d(this.getClass(), "role=" + role + ", contact=" + teamlead);
		return teamlead;
	}

	private Contact getContact(int contactId) {
		Contact contact = null;
		StringBuffer selection = new StringBuffer();
		selection.append(ContactsTable.COLUMN_ID + " = ?");
		String[] selectionArgs = { Integer.toString(contactId) };
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(ContactsTable.TABLE_NAME, ContactsTable.TABLE_COLUMNS, selection.toString(), selectionArgs, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			contact = mapCursorToContact(cursor);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "contact=" + contact);
		return contact;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Contact getContact(String firstName, String lastName) {
		Contact contact = null;
		StringBuffer selection = new StringBuffer();
		selection.append(ContactsTable.COLUMN_FIRST_NAME + " LIKE ?");
		selection.append(" AND ").append(ContactsTable.COLUMN_LAST_NAME + " LIKE ?");
		String[] selectionArgs = { firstName, lastName };
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(ContactsTable.TABLE_NAME, ContactsTable.TABLE_COLUMNS, selection.toString(), selectionArgs, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			contact = mapCursorToContact(cursor);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "contact=" + contact);
		return contact;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Training> getTrainingList(Integer teamId, String periode) {
		List<Training> list = new ArrayList<Training>();
		String periodeSelection = getPeriodeSelectionClause(periode);
		StringBuffer query = new StringBuffer();
		query.append("SELECT *");
		query.append(" FROM ").append(TrainingsTable.TABLE_NAME);
		query.append(" WHERE ").append(TrainingsTable.COLUMN_FK_TEAM_ID).append(" = ").append(teamId);
		query.append(" AND ").append(periodeSelection);
		query.append(" ORDER BY ").append(TrainingsTable.COLUMN_START_DATE).append(" COLLATE LOCALIZED ASC");
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = this.database.rawQuery(query.toString(), null);
		Team team = getTeam(teamId);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				list.add(mapCursorToTraining(cursor, team));
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
	public List<Cup> getCupList(Integer teamId, String periode) {
		List<Cup> list = new ArrayList<Cup>();
		String periodeSelection = getPeriodeSelectionClause(periode);
		StringBuffer query = new StringBuffer();
		query.append("SELECT *");
		query.append(" FROM ").append(CupsTable.TABLE_NAME);
		query.append(" WHERE ").append(periodeSelection);
		query.append(" ORDER BY ").append(CupsTable.COLUMN_START_DATE).append(" COLLATE LOCALIZED ASC");
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery(query.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				list.add(mapCursorToCup(cursor));
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
	public List<Role> getRoleList() {
		List<Role> list = new ArrayList<Role>();
		String selection = RolesTable.COLUMN_ID + " > ?";
		String[] selectionArgs = { "-1" };
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(RolesTable.TABLE_NAME, RolesTable.TABLE_COLUMNS, selection, selectionArgs, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				list.add(mapCursorToRole(cursor));
				cursor.moveToNext();
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.e(this.getClass(), "roles=" + list);
		return list;
	}

	public Player getPlayer(Integer playerId) {
		Player player = null;
		StringBuffer selection = new StringBuffer();
		selection.append(ContactsTable.COLUMN_ID + " = ?");
		String[] selectionArgs = { playerId.toString() };
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(PlayersTable.TABLE_NAME, PlayersTable.TABLE_COLUMNS, selection.toString(), selectionArgs, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			Team team = getTeam(cursor.getInt(cursor.getColumnIndex(PlayersTable.COLUMN_FK_TEAM_ID)));
			player = mapCursorToPlayer(cursor, team);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "player=" + player);
		return player;
	}

	@Override
	public List<Player> getPlayerList(Integer teamId) {
		List<Player> list = new ArrayList<Player>();
		String orderBy = PlayersTable.COLUMN_LAST_NAME + " COLLATE LOCALIZED ASC";
		String selection = PlayersTable.COLUMN_FK_TEAM_ID + " = ?";
		String[] selectionArgs = { teamId.toString() };
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(PlayersTable.TABLE_NAME, PlayersTable.TABLE_COLUMNS, selection, selectionArgs, null, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			Team team = getTeam(teamId);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				list.add(mapCursorToPlayer(cursor, team));
				cursor.moveToNext();
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "players=" + cursor.getCount());
		return list;
	}

	// -----------------------------------------------------------------------------------------------
	// SettingsTable operations
	// -----------------------------------------------------------------------------------------------

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
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(SettingsTable.TABLE_NAME, SettingsTable.TABLE_COLUMNS, selection, selectionArgs, groupBy, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			setting = mapCursorToSetting(cursor);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		if (setting == null) {
			throw new RuntimeException("DB not initialize! Please report bug!");
		}
		CustomLog.d(this.getClass(), setting.toString());
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
		this.database = dbHelper.getWritableDatabase();
		database.update(SettingsTable.TABLE_NAME, values, where.toString(), selectionArgs);
		CustomLog.d(this.getClass(), "Updated " + type + " = " + value);
	}

	private String getPeriodeSelectionClause(String periode) {
		String selectClause = null;
		Calendar cal = Calendar.getInstance();
		if (periode.equalsIgnoreCase("all")) {
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.MILLISECOND, 0);
			// only those newer than today
			selectClause = "start_date > " + Long.toString(cal.getTimeInMillis());
		} else if (periode.equalsIgnoreCase("year")) {
			int year = cal.get(Calendar.YEAR);
			selectClause = "strftime('%Y', datetime(start_date, 'unixepoch')) LIKE " + year;
		} else if (periode.equalsIgnoreCase("month")) {
			int mmonthNumber = cal.get(Calendar.MONTH);
			selectClause = "strftime('%m%Y', datetime(start_date, 'unixepoch')) LIKE " + (mmonthNumber + 1);
		} else if (periode.equalsIgnoreCase("week")) {
			int week = cal.get(Calendar.WEEK_OF_YEAR);
			selectClause = "strftime('%W', datetime(start_date, 'unixepoch')) LIKE " + week;
		} else if (periode.equalsIgnoreCase("day")) {
			int day = cal.get(Calendar.DAY_OF_MONTH);
			selectClause = "strftime('%d%m%Y', datetime(start_date, 'unixepoch')) LIKE " + day;
		}
		return selectClause;
	}

	// -----------------------------------------------------------------------------------------------
	// Mappings
	// -----------------------------------------------------------------------------------------------

	private Cup mapCursorToCup(Cursor cursor) {
		long start_date_ms = ((long) cursor.getInt(cursor.getColumnIndex(CupsTable.COLUMN_START_DATE))) * 1000L;
		long deadline_date_ms = ((long) cursor.getInt(cursor.getColumnIndex(CupsTable.COLUMN_DEADLINE_DATE))) * 1000L;
		return new Cup(cursor.getInt(cursor.getColumnIndex(CupsTable.COLUMN_ID)), start_date_ms, cursor.getString(cursor
				.getColumnIndex(CupsTable.COLUMN_CUP_NAME)), cursor.getString(cursor.getColumnIndex(CupsTable.COLUMN_CLUB_NAME)), cursor.getString(cursor
				.getColumnIndex(CupsTable.COLUMN_VENUE)), deadline_date_ms);
	}

	private Match mapCursorToMatch(Cursor cursor, Team team) {
		long start_date_ms = ((long) cursor.getInt(cursor.getColumnIndex(MatchesTable.COLUMN_START_DATE))) * 1000L;
		return new Match(cursor.getInt(cursor.getColumnIndex(MatchesTable.COLUMN_ID)), start_date_ms, team, new Team(cursor.getString(cursor
				.getColumnIndex(MatchesTable.COLUMN_HOME_TEAM))), new Team(cursor.getString(cursor.getColumnIndex(MatchesTable.COLUMN_AWAY_TEAM))),
				cursor.getString(cursor.getColumnIndex(MatchesTable.COLUMN_VENUE)), new Referee(cursor.getString(cursor
						.getColumnIndex(MatchesTable.COLUMN_REFEREE))));
	}

	private Training mapCursorToTraining(Cursor cursor, Team team) {
		long start_date_ms = ((long) cursor.getInt(cursor.getColumnIndex(TrainingsTable.COLUMN_START_DATE))) * 1000L;
		long end_time_ms = ((long) cursor.getInt(cursor.getColumnIndex(TrainingsTable.COLUMN_END_TIME))) * 1000L;
		return new Training(cursor.getInt(cursor.getColumnIndex(TrainingsTable.COLUMN_ID)), start_date_ms, end_time_ms, team, cursor.getString(cursor
				.getColumnIndex(TrainingsTable.COLUMN_PLACE)));
	}

	private Club mapCursorToClub(Cursor cursor) {
		return new Club(cursor.getInt(cursor.getColumnIndex(ClubsTable.COLUMN_ID)), cursor.getString(cursor.getColumnIndex(ClubsTable.COLUMN_CLUB_NAME)));

	}

	private Team mapCursorToTeam(Cursor cursor) {
		Club club = getClub(cursor.getInt(cursor.getColumnIndex(TeamsTable.COLUMN_FK_CLUB_ID)));
		return new Team(cursor.getInt(cursor.getColumnIndex(TeamsTable.COLUMN_ID)), cursor.getString(cursor.getColumnIndex(TeamsTable.COLUMN_TEAM_NAME)), club);
	}

	private Setting mapCursorToSetting(Cursor cursor) {
		return new Setting(cursor.getString(cursor.getColumnIndex(SettingsTable.COLUMN_KEY)), cursor.getString(cursor
				.getColumnIndex(SettingsTable.COLUMN_VALUE)));
	}

	private Contact mapCursorToContact(Cursor cursor) {
		Team team = getTeam(cursor.getInt(cursor.getColumnIndex(ContactsTable.COLUMN_FK_TEAM_ID)));
		List<ContactRoleEnum> roles = getRoles(cursor.getInt(cursor.getColumnIndex(ContactsTable.COLUMN_ID)));
		return new Contact(cursor.getInt(cursor.getColumnIndex(ContactsTable.COLUMN_ID)), team, roles, cursor.getString(cursor
				.getColumnIndex(ContactsTable.COLUMN_FIRST_NAME)), cursor.getString(cursor.getColumnIndex(ContactsTable.COLUMN_MIDDLE_NAME)),
				cursor.getString(cursor.getColumnIndex(ContactsTable.COLUMN_LAST_NAME)), cursor.getString(cursor.getColumnIndex(ContactsTable.COLUMN_MOBILE)),
				cursor.getString(cursor.getColumnIndex(ContactsTable.COLUMN_EMAIL)));
	}

	private Role mapCursorToRole(Cursor cursor) {
		return new Role(cursor.getInt(cursor.getColumnIndex(RolesTable.COLUMN_ID)), cursor.getInt(cursor.getColumnIndex(RolesTable.COLUMN_FK_CONTACT_ID)),
				cursor.getString(cursor.getColumnIndex(RolesTable.COLUMN_ROLE)));
	}

	private Player mapCursorToPlayer(Cursor cursor, Team team) {
		List<Contact> relationsShips = getRelationsShips(cursor.getInt(cursor.getColumnIndex(PlayersTable.COLUMN_ID)));
		return new Player(team, cursor.getString(cursor.getColumnIndex(PlayersTable.COLUMN_FIRST_NAME)), cursor.getString(cursor
				.getColumnIndex(PlayersTable.COLUMN_MIDDLE_NAME)), cursor.getString(cursor.getColumnIndex(PlayersTable.COLUMN_LAST_NAME)),
				PlayerStatusEnum.valueOf(cursor.getString(cursor.getColumnIndex(PlayersTable.COLUMN_STATUS))), relationsShips, cursor.getInt(cursor
						.getColumnIndex(PlayersTable.COLUMN_DATE_OF_BIRTH)));
	}

	private List<Contact> getRelationsShips(Integer playerId) {
		List<Contact> relationshipList = new ArrayList<Contact>();
		String groupBy = null;
		String orderBy = null;
		String selection = RelationshipsTable.COLUMN_FK_PLAYER_ID + " = ?";
		String[] selectionArgs = { playerId.toString() };
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(RelationshipsTable.TABLE_NAME, RelationshipsTable.TABLE_COLUMNS, selection, selectionArgs, groupBy, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Contact contact = getContact(cursor.getColumnIndex(RelationshipsTable.COLUMN_FK_CONTACT_ID));
				relationshipList.add(contact);
				cursor.moveToNext();
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), relationshipList.toString());
		return relationshipList;
	}

	private List<ContactRoleEnum> getRoles(Integer contactId) {
		List<ContactRoleEnum> roleList = new ArrayList<ContactRoleEnum>();
		String groupBy = null;
		String orderBy = null;
		String selection = RolesTable.COLUMN_FK_CONTACT_ID + " = ?";
		String[] selectionArgs = { contactId.toString() };
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(RolesTable.TABLE_NAME, RolesTable.TABLE_COLUMNS, selection, selectionArgs, groupBy, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String role = cursor.getString(cursor.getColumnIndex(RolesTable.COLUMN_ROLE));
				roleList.add(ContactRoleEnum.valueOf(role.toUpperCase()));
				cursor.moveToNext();
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), roleList.toString());
		return roleList;
	}
}
