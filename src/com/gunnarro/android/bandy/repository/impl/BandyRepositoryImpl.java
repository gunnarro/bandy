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
import com.gunnarro.android.bandy.domain.Referee;
import com.gunnarro.android.bandy.domain.Setting;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.Traning;
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
import com.gunnarro.android.bandy.repository.table.TraningsTable;

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
	public boolean createTraning(Traning traning) {
		ContentValues values = TraningsTable.createContentValues(traning.getTeam().getId(), traning.getStartDate(), traning.getEndTime(), traning.getVenue());
		this.database = dbHelper.getWritableDatabase();
		database.insert(TraningsTable.TABLE_NAME, null, values);
		return true;
	}

	@Override
	public void createPlayer(Player player) {
		ContentValues playerValues = PlayersTable.createContentValues(player.getFirstName(), player.getMiddleName(), player.getLastName(),
				Boolean.toString(true));
		this.database = dbHelper.getWritableDatabase();
		long playerId = database.insert(PlayersTable.TABLE_NAME, null, playerValues);
		if (player.getParents() != null) {
			for (Contact parent : player.getParents()) {
				ContentValues relationshipsValues = RelationshipsTable.createContentValues(Long.valueOf(playerId).intValue(), parent.getId());
				database.insert(RelationshipsTable.TABLE_NAME, null, relationshipsValues);
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
		String[] selectionArgs = { teamId.toString(), role };
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
		CustomLog.d(this.getClass(), name);
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
	public List<Traning> getTraningList(Integer teamId, String periode) {
		List<Traning> list = new ArrayList<Traning>();
		String periodeSelection = getPeriodeSelectionClause(periode);
		StringBuffer query = new StringBuffer();
		query.append("SELECT *");
		query.append(" FROM ").append(TraningsTable.TABLE_NAME);
		query.append(" WHERE ").append(TraningsTable.COLUMN_FK_TEAM_ID).append(" = ").append(teamId);
		query.append(" AND ").append(periodeSelection);
		query.append(" ORDER BY ").append(TraningsTable.COLUMN_START_DATE).append(" COLLATE LOCALIZED ASC");
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = this.database.rawQuery(query.toString(), null);
		Team team = getTeam(teamId);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				list.add(mapCursorToTraning(cursor, team));
				cursor.moveToNext();
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "tranings=" + cursor.getCount());
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

	@Override
	public List<Player> getPlayerList(String teamName) {
		return null;
	}

	// -----------------------------------------------------------------------------------------------
	// SettingsTable operations
	// -----------------------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateDataFileUrl(String url) {
		updateSetting(SettingsTable.DATA_FILE_URL, url);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDataFileUrl() {
		String selection = SettingsTable.COLUMN_KEY + " LIKE ?";
		String[] selectionArgs = { SettingsTable.DATA_FILE_URL };
		return getSetting(selection, selectionArgs).getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateDataFileVersion(String version) {
		updateSetting(SettingsTable.DATA_FILE_VERSION, version);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDataFileVersion() {
		String selection = SettingsTable.COLUMN_KEY + " LIKE ?";
		String[] selectionArgs = { SettingsTable.DATA_FILE_VERSION };
		return getSetting(selection, selectionArgs).getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateDataFileLastUpdated(long lastUpdatedTime) {
		updateSetting(SettingsTable.DATA_FILE_LAST_UPDATED, Long.toString(lastUpdatedTime));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getDataFileLastUpdated() {
		String selection = SettingsTable.COLUMN_KEY + " LIKE ?";
		String[] selectionArgs = { SettingsTable.DATA_FILE_LAST_UPDATED };
		return Long.parseLong(getSetting(selection, selectionArgs).getValue());
	}

	private Setting getSetting(String selection, String[] selectionArgs) {
		Setting setting = null;
		String groupBy = null;
		String orderBy = null;
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
		return setting;
	}

	private void updateSetting(String selectionArg, String value) {
		StringBuffer where = new StringBuffer();
		where.append(SettingsTable.COLUMN_KEY).append(" LIKE ?");
		String[] selectionArgs = { selectionArg };
		ContentValues values = SettingsTable.createContentValues(selectionArg, value);
		this.database = dbHelper.getWritableDatabase();
		database.update(SettingsTable.TABLE_NAME, values, where.toString(), selectionArgs);
		CustomLog.d(this.getClass(), "Updated " + selectionArg + ": " + value);
	}

	private String getPeriodeSelectClause(String periode) {
		String selectClause = null;
		if (periode.equalsIgnoreCase("year")) {
			selectClause = "strftime('%Y', datetime(start_date, 'unixepoch'))";
		} else if (periode.equalsIgnoreCase("month")) {
			selectClause = "strftime('%m.%Y', datetime(start_date, 'unixepoch'))";
		} else if (periode.equalsIgnoreCase("week")) {
			selectClause = "strftime('%W', datetime(start_date, 'unixepoch'))";
		} else if (periode.equalsIgnoreCase("day")) {
			selectClause = "strftime('%d.%m', datetime(start_date, 'unixepoch'))";
		}
		return selectClause;
	}

	private String getPeriodeSelectionClause(String periode) {
		String selectClause = null;
		if (periode.equalsIgnoreCase("year")) {
			int year = Calendar.getInstance().get(Calendar.YEAR);
			selectClause = "strftime('%Y', datetime(start_date, 'unixepoch')) LIKE " + year;
		} else if (periode.equalsIgnoreCase("month")) {
			int mmonthNumber = Calendar.getInstance().get(Calendar.MONTH);
			selectClause = "strftime('%m', datetime(start_date, 'unixepoch')) LIKE " + mmonthNumber;
		} else if (periode.equalsIgnoreCase("week")) {
			int week = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
			selectClause = "strftime('%W', datetime(start_date, 'unixepoch')) LIKE " + week;
		} else if (periode.equalsIgnoreCase("day")) {
			int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			selectClause = "strftime('%d', datetime(start_date, 'unixepoch')) LIKE " + day;
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

	private Traning mapCursorToTraning(Cursor cursor, Team team) {
		long start_date_ms = ((long) cursor.getInt(cursor.getColumnIndex(TraningsTable.COLUMN_START_DATE))) * 1000L;
		long end_time_ms = ((long) cursor.getInt(cursor.getColumnIndex(TraningsTable.COLUMN_END_TIME))) * 1000L;
		return new Traning(cursor.getInt(cursor.getColumnIndex(TraningsTable.COLUMN_ID)), start_date_ms, end_time_ms, team, cursor.getString(cursor
				.getColumnIndex(TraningsTable.COLUMN_PLACE)));
	}

	private Club mapCursorToClub(Cursor cursor) {
		return new Club(cursor.getInt(cursor.getColumnIndex(ClubsTable.COLUMN_ID)), cursor.getString(cursor.getColumnIndex(ClubsTable.COLUMN_CLUB_NAME)));

	}

	private Team mapCursorToTeam(Cursor cursor) {
		return new Team(cursor.getInt(cursor.getColumnIndex(TeamsTable.COLUMN_ID)), cursor.getString(cursor.getColumnIndex(TeamsTable.COLUMN_TEAM_NAME)));
	}

	private Setting mapCursorToSetting(Cursor cursor) {
		return new Setting(cursor.getString(cursor.getColumnIndex(SettingsTable.COLUMN_KEY)), cursor.getString(cursor
				.getColumnIndex(SettingsTable.COLUMN_VALUE)));
	}

	private Contact mapCursorToContact(Cursor cursor) {
		return null;
	}

}
