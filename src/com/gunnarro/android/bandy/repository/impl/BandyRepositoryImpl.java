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
import com.gunnarro.android.bandy.domain.Address;
import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.domain.Contact;
import com.gunnarro.android.bandy.domain.Contact.ContactRoleEnum;
import com.gunnarro.android.bandy.domain.Player;
import com.gunnarro.android.bandy.domain.Player.PlayerStatusEnum;
import com.gunnarro.android.bandy.domain.Referee;
import com.gunnarro.android.bandy.domain.Role;
import com.gunnarro.android.bandy.domain.SearchResult;
import com.gunnarro.android.bandy.domain.Setting;
import com.gunnarro.android.bandy.domain.Statistic;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Cup;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.activity.Training;
import com.gunnarro.android.bandy.domain.view.list.Item;
import com.gunnarro.android.bandy.repository.BandyDataBaseHjelper;
import com.gunnarro.android.bandy.repository.BandyRepository;
import com.gunnarro.android.bandy.repository.table.AddressTable;
import com.gunnarro.android.bandy.repository.table.ClubsTable;
import com.gunnarro.android.bandy.repository.table.ContactsTable;
import com.gunnarro.android.bandy.repository.table.PlayersTable;
import com.gunnarro.android.bandy.repository.table.RolesTable;
import com.gunnarro.android.bandy.repository.table.SettingsTable;
import com.gunnarro.android.bandy.repository.table.TeamsTable;
import com.gunnarro.android.bandy.repository.table.activity.CupsTable;
import com.gunnarro.android.bandy.repository.table.activity.MatchesTable;
import com.gunnarro.android.bandy.repository.table.activity.TrainingsTable;
import com.gunnarro.android.bandy.repository.table.link.PlayerContactLnkTable;
import com.gunnarro.android.bandy.repository.table.link.PlayerCupLnkTable;
import com.gunnarro.android.bandy.repository.table.link.PlayerMatchLnkTable;
import com.gunnarro.android.bandy.repository.table.link.PlayerTrainingLnkTable;
import com.gunnarro.android.bandy.service.exception.ApplicationException;
import com.gunnarro.android.bandy.service.exception.ValidationException;

public class BandyRepositoryImpl implements BandyRepository {

	public enum PlayerLinkTableTypeEnum {
		MATCH, CUP, TRAINING, CONTACT;
	}

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
		database.delete(AddressTable.TABLE_NAME, null, null);
		database.delete(ClubsTable.TABLE_NAME, null, null);
		database.delete(ContactsTable.TABLE_NAME, null, null);
		database.delete(CupsTable.TABLE_NAME, null, null);
		database.delete(MatchesTable.TABLE_NAME, null, null);
		database.delete(PlayersTable.TABLE_NAME, null, null);
		database.delete(PlayerContactLnkTable.TABLE_NAME, null, null);
		database.delete(PlayerCupLnkTable.TABLE_NAME, null, null);
		database.delete(PlayerMatchLnkTable.TABLE_NAME, null, null);
		database.delete(PlayerTrainingLnkTable.TABLE_NAME, null, null);
		database.delete(RolesTable.TABLE_NAME, null, null);
		// database.delete(SettingsTable.TABLE_NAME, null, null);
		database.delete(TeamsTable.TABLE_NAME, null, null);
		database.delete(TrainingsTable.TABLE_NAME, null, null);
		CustomLog.i(this.getClass(), "Deleted all data!");
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
	public int createTraining(Training training) {
		ContentValues values = TrainingsTable.createContentValues(training.getTeam().getId(), training.getStartDate(), training.getEndTime(),
				training.getVenue());
		this.database = dbHelper.getWritableDatabase();
		long id = database.insert(TrainingsTable.TABLE_NAME, null, values);
		return Long.valueOf(id).intValue();
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
					createPlayerLink(PlayerLinkTableTypeEnum.CONTACT, Long.valueOf(playerId).intValue(), contact.getId().intValue());
				} else {
					CustomLog.e(this.getClass(), "No contact found for: " + parent.getFirstName() + " " + parent.getLastName());
				}
			}
		}
		CustomLog.d(this.getClass(), player.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createContact(Contact contact) {
		long addressId = createAddress(contact.getAddress());
		ContentValues contactValues = ContactsTable.createContentValues(addressId, contact.getTeam().getId(), contact.getFirstName(), contact.getMiddleName(),
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

	public long createAddress(Address address) {
		ContentValues contactValues = AddressTable.createContentValues(address.getStreetName(), address.getStreetNumber(), address.getStreetNumberPrefix(),
				address.getPostalCode(), address.getCity(), address.getCountry());
		this.database = dbHelper.getWritableDatabase();
		long addressId = database.insert(AddressTable.TABLE_NAME, null, contactValues);
		return addressId;
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
				Team team = getTeam(cursor.getInt(cursor.getColumnIndex(ContactsTable.COLUMN_FK_TEAM_ID)));
				list.add(mapCursorToContact(cursor, team));
				cursor.moveToNext();
			}
		}
		CustomLog.d(this.getClass(), "contacts=" + cursor.getCount());
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
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
		query.append(" ORDER BY ").append(PlayersTable.COLUMN_FIRST_NAME).append(" COLLATE LOCALIZED ASC");

		CustomLog.d(this.getClass(), "query=" + query.toString());
		this.database = dbHelper.getReadableDatabase();
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
				list.add(new Item(cursor.getInt(cursor.getColumnIndex(PlayersTable.COLUMN_ID)), fullName, playerSignedIn));
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

	public String[] getTeamNames(String clubName) {
		if (clubName == null) {
			throw new ValidationException(this.getClass().getName() + ".getTeam() arg clubName is null!");
		}
		String[] teamNames = null;
		StringBuffer query = new StringBuffer();
		query.append("SELECT t.").append(TeamsTable.COLUMN_TEAM_NAME);
		query.append(" FROM ").append(TeamsTable.TABLE_NAME).append(" t, ").append(ClubsTable.TABLE_NAME).append(" c");
		query.append(" WHERE t.").append(TeamsTable.COLUMN_FK_CLUB_ID).append(" = c.").append(ClubsTable.COLUMN_ID);
		query.append(" AND c.").append(ClubsTable.COLUMN_CLUB_NAME).append(" LIKE '").append(clubName).append("'");
		query.append(" ORDER BY t.").append(TeamsTable.COLUMN_TEAM_NAME).append(" COLLATE LOCALIZED ASC");
		CustomLog.d(this.getClass(), "sqlQuery=" + query.toString());
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = this.database.rawQuery(query.toString(), null);
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
		String selection = TeamsTable.COLUMN_ID + " LIKE ?";
		String[] selectionArgs = { id.toString() };
		return getTeam(selection, selectionArgs);
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
		String orderBy = TeamsTable.COLUMN_TEAM_NAME + " COLLATE LOCALIZED ASC";
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(TeamsTable.TABLE_NAME, TeamsTable.TABLE_COLUMNS, selection, selectionArgs, null, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			Club club = getClub(cursor.getInt(cursor.getColumnIndex(TeamsTable.COLUMN_FK_CLUB_ID)));
			team = mapCursorToTeam(cursor, club);
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
			Team team = getTeam(cursor.getInt(cursor.getColumnIndex(ContactsTable.COLUMN_FK_TEAM_ID)));
			contact = mapCursorToContact(cursor, team);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "contactId=" + contactId + ", contact=" + contact);
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
			Team team = getTeam(cursor.getInt(cursor.getColumnIndex(ContactsTable.COLUMN_FK_TEAM_ID)));
			contact = mapCursorToContact(cursor, team);
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
	public Player lookupPlayerThroughContact(String mobileNr) {
		Player player = null;
		StringBuffer query = new StringBuffer();
		query.append("SELECT p.* FROM players p, contacts c, player_contact_lnk l");
		query.append(" WHERE c.mobile LIKE ?");
		query.append(" AND c._id == l.fk_contact_id");
		query.append(" AND p._id == l.fk_player_id");
		String[] selectionArgs = { mobileNr };
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery(query.toString(), selectionArgs);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			Team team = getTeam(cursor.getInt(cursor.getColumnIndex(PlayersTable.COLUMN_FK_TEAM_ID)));
			mapCursorToPlayer(cursor, team);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), "player=" + player);
		return player;

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
		CustomLog.d(this.getClass(), "query=" + query.toString());
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
		CustomLog.d(this.getClass(), "query=" + query.toString());
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
		CustomLog.d(this.getClass(), "playerId=" + playerId + ", player=" + player);
		return player;
	}

	@Override
	public List<Player> getPlayerList(Integer teamId) {
		List<Player> list = new ArrayList<Player>();
		String orderBy = PlayersTable.COLUMN_FIRST_NAME + " COLLATE LOCALIZED ASC";
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

	@Override
	public List<Item> getPlayersAsItemList(int teamId) {
		List<Item> list = new ArrayList<Item>();
		StringBuffer query = new StringBuffer();
		query.append("SELECT * FROM ").append(PlayersTable.TABLE_NAME);
		query.append(" WHERE ").append(PlayersTable.COLUMN_FK_TEAM_ID).append(" = ? ");
		String[] selectionArgs = { Integer.toString(teamId) };
		CustomLog.d(this.getClass(), "query=" + query.toString());
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery(query.toString(), selectionArgs);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String fullName = cursor.getString(cursor.getColumnIndex(PlayersTable.COLUMN_FIRST_NAME)) + " "
						+ cursor.getString(cursor.getColumnIndex(PlayersTable.COLUMN_LAST_NAME));
				CustomLog.d(this.getClass(), "players=" + fullName + " teamId=" + teamId);
				list.add(new Item(cursor.getInt(cursor.getColumnIndex(PlayersTable.COLUMN_ID)), fullName, false));
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
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(PlayersTable.TABLE_NAME, PlayersTable.TABLE_COLUMNS, selection.toString(), selectionArgs, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			int playerId = cursor.getInt(cursor.getColumnIndex(PlayersTable.COLUMN_ID));
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
		String forDaySelection = getPeriodeSelectionClause("day");
		StringBuffer query = new StringBuffer();
		query.append("SELECT _id FROM ").append(MatchesTable.TABLE_NAME);
		query.append(" WHERE").append(MatchesTable.COLUMN_FK_TEAM_ID + " = ?");
		query.append(" AND ").append(forDaySelection);
		String[] selectionArgs = { teamId.toString(), startDate };
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery(query.toString(), selectionArgs);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			matchId = cursor.getInt(cursor.getColumnIndex(MatchesTable.COLUMN_ID));
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SearchResult search(String sqlQuery) {
		StringBuffer value = new StringBuffer();
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery(sqlQuery, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				for (String columnName : cursor.getColumnNames()) {
					if (cursor.getType(cursor.getColumnIndex(columnName)) == Cursor.FIELD_TYPE_INTEGER)
						value.append(cursor.getInt(cursor.getColumnIndex(columnName)));
					else {
						value.append(cursor.getString(cursor.getColumnIndex(columnName)));
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
			selectClause = "strftime('%Y', datetime(start_date, 'unixepoch')) LIKE '" + year + "'";
		} else if (periode.equalsIgnoreCase("month")) {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("MMyyyy", Locale.UK);
			String todayMMYYY = dateFormatter.format(System.currentTimeMillis());
			selectClause = "strftime('%m%Y', datetime(start_date, 'unixepoch')) LIKE '" + todayMMYYY + "'";
		} else if (periode.equalsIgnoreCase("week")) {
			int week = cal.get(Calendar.WEEK_OF_YEAR);
			selectClause = "strftime('%W', datetime(start_date, 'unixepoch')) LIKE '" + week + "'";
		} else if (periode.equalsIgnoreCase("day")) {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("ddMMyyyy", Locale.UK);
			String todayDDMMYYY = dateFormatter.format(System.currentTimeMillis());
			selectClause = "strftime('%d%m%Y', datetime(start_date, 'unixepoch')) LIKE '" + todayDDMMYYY + "'";
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

	private Team mapCursorToTeam(Cursor cursor, Club club) {
		return new Team(cursor.getInt(cursor.getColumnIndex(TeamsTable.COLUMN_ID)), cursor.getString(cursor.getColumnIndex(TeamsTable.COLUMN_TEAM_NAME)), club);
	}

	private Setting mapCursorToSetting(Cursor cursor) {
		return new Setting(cursor.getString(cursor.getColumnIndex(SettingsTable.COLUMN_KEY)), cursor.getString(cursor
				.getColumnIndex(SettingsTable.COLUMN_VALUE)));
	}

	private Contact mapCursorToContact(Cursor cursor, Team team) {
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
		List<Contact> relationsShips = getRelationsShips(cursor.getInt(cursor.getColumnIndex(PlayersTable.COLUMN_ID)), team);
		return new Player(cursor.getInt(cursor.getColumnIndex(PlayersTable.COLUMN_ID)), team, cursor.getString(cursor
				.getColumnIndex(PlayersTable.COLUMN_FIRST_NAME)), cursor.getString(cursor.getColumnIndex(PlayersTable.COLUMN_MIDDLE_NAME)),
				cursor.getString(cursor.getColumnIndex(PlayersTable.COLUMN_LAST_NAME)), PlayerStatusEnum.valueOf(cursor.getString(cursor
						.getColumnIndex(PlayersTable.COLUMN_STATUS))), relationsShips, cursor.getInt(cursor.getColumnIndex(PlayersTable.COLUMN_DATE_OF_BIRTH)));
	}

	// **********************************************************************************************
	// Link tables operations
	// **********************************************************************************************

	//
	// private void listLinkTable(LinkTable table) {
	// String orderBy = table.getColumnName1() + " ASC";
	// this.database = dbHelper.getReadableDatabase();
	// Cursor cursor = database.query(table.getName(), table.getTableColumns(),
	// null, null, null, null, orderBy);
	// if (cursor != null && cursor.getCount() > 0) {
	// cursor.moveToFirst();
	// while (!cursor.isAfterLast()) {
	// CustomLog.e(
	// this.getClass(),
	// cursor.getColumnIndex("_id") + " " +
	// cursor.getColumnIndex(table.getColumnName1()) + " - "
	// + cursor.getInt(cursor.getColumnIndex(table.getColumnName2())));
	// cursor.moveToNext();
	// }
	// }
	// if (cursor != null && !cursor.isClosed()) {
	// cursor.close();
	// }
	// }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deletePlayerLink(PlayerLinkTableTypeEnum type, int playerId, int id) {
		if (type == PlayerLinkTableTypeEnum.CONTACT) {
			deletePlayerLnk(PlayerContactLnkTable.TABLE_NAME, PlayerContactLnkTable.getTableColumns(), playerId, id);
		} else if (type == PlayerLinkTableTypeEnum.MATCH) {
			deletePlayerLnk(PlayerMatchLnkTable.TABLE_NAME, PlayerMatchLnkTable.getTableColumns(), playerId, id);
		} else if (type == PlayerLinkTableTypeEnum.CUP) {
			deletePlayerLnk(PlayerCupLnkTable.TABLE_NAME, PlayerCupLnkTable.getTableColumns(), playerId, id);
		} else if (type == PlayerLinkTableTypeEnum.TRAINING) {
			deletePlayerLnk(PlayerTrainingLnkTable.TABLE_NAME, PlayerTrainingLnkTable.getTableColumns(), playerId, id);
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
			createPlayerLnk(PlayerContactLnkTable.TABLE_NAME, PlayerContactLnkTable.createContentValues(playerId, id));
		} else if (type == PlayerLinkTableTypeEnum.MATCH) {
			createPlayerLnk(PlayerMatchLnkTable.TABLE_NAME, PlayerMatchLnkTable.createContentValues(playerId, id));
		} else if (type == PlayerLinkTableTypeEnum.CUP) {
			createPlayerLnk(PlayerCupLnkTable.TABLE_NAME, PlayerCupLnkTable.createContentValues(playerId, id));
		} else if (type == PlayerLinkTableTypeEnum.TRAINING) {
			createPlayerLnk(PlayerTrainingLnkTable.TABLE_NAME, PlayerTrainingLnkTable.createContentValues(playerId, id));
		} else {
			throw new ApplicationException("Invalid link table type: " + type);
		}
	}

	private void createPlayerLnk(String tableName, ContentValues contentValues) {
		this.database = dbHelper.getWritableDatabase();
		database.insert(tableName, null, contentValues);
		CustomLog.d(this.getClass(), "created=" + tableName + ", values=" + contentValues.toString());
	}

	private void deletePlayerLnk(String tableName, String[] colNames, int id1, int id2) {
		String whereClause = colNames[1] + " = ? AND " + colNames[2] + " = ?";
		String[] whereArgs = { Integer.toString(id1), Integer.toString(id2) };
		this.database = dbHelper.getWritableDatabase();
		database.delete(tableName, whereClause, whereArgs);
		CustomLog.d(this.getClass(), "deleted=" + tableName + ", values=" + id1 + ", " + id2);
	}

	private List<Contact> getRelationsShips(Integer playerId, Team team) {
		CustomLog.d(this.getClass(), "palyerId=" + playerId);
		List<Contact> relationshipList = new ArrayList<Contact>();
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT c.* FROM ").append(ContactsTable.TABLE_NAME).append(" c,").append(PlayerContactLnkTable.TABLE_NAME).append(" p");
		sqlQuery.append(" WHERE p.").append(PlayerContactLnkTable.COLUMN_FK_PLAYER_ID).append(" = ?");
		sqlQuery.append(" AND c.").append(ContactsTable.COLUMN_ID).append(" = p.").append(PlayerContactLnkTable.COLUMN_FK_CONTACT_ID);
		String[] selectionArgs = { playerId.toString() };
		CustomLog.d(this.getClass(), "sqlQuery=" + sqlQuery.toString());
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery(sqlQuery.toString(), selectionArgs);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Contact contact = mapCursorToContact(cursor, team);
				if (contact != null) {
					relationshipList.add(contact);
				}
				cursor.moveToNext();
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), relationshipList.toString());
		return relationshipList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void listRelationsShips() {
		String orderBy = PlayerContactLnkTable.COLUMN_FK_PLAYER_ID + " ASC";
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(PlayerContactLnkTable.TABLE_NAME, PlayerContactLnkTable.TABLE_COLUMNS, null, null, null, null, orderBy);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Player player = getPlayer(cursor.getInt(cursor.getColumnIndex(PlayerContactLnkTable.COLUMN_FK_PLAYER_ID)));
				Contact contact = getContact(cursor.getInt(cursor.getColumnIndex(PlayerContactLnkTable.COLUMN_FK_CONTACT_ID)));
				// CustomLog.e(this.getClass(),
				// cursor.getInt(cursor.getColumnIndex(PlayerContactLnkTable.COLUMN_ID))
				// + " - " + player.getFullName() + " - "
				// + contact.getFullName());
				cursor.moveToNext();
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}

	private List<ContactRoleEnum> getRoles(Integer contactId) {
		List<ContactRoleEnum> roleList = new ArrayList<ContactRoleEnum>();
		String orderBy = null;
		String selection = RolesTable.COLUMN_FK_CONTACT_ID + " = ?";
		String[] selectionArgs = { contactId.toString() };
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(RolesTable.TABLE_NAME, RolesTable.TABLE_COLUMNS, selection, selectionArgs, null, null, orderBy);
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

	// Statistic

	@Override
	public Statistic getPlayerStatistic(int teamId, int playerId) {
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
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery(sqlQuery.toString(), selectionArgs);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			statistic = new Statistic(cursor.getString(cursor.getColumnIndex("clubName")), cursor.getString(cursor.getColumnIndex("teamName")), playerId,
					cursor.getInt(cursor.getColumnIndex("numberOfPlayerMatches")), cursor.getInt(cursor.getColumnIndex("numberOfPlayerCups")),
					cursor.getInt(cursor.getColumnIndex("numberOfPlayerTrainings")), cursor.getInt(cursor.getColumnIndex("numberOfTeamMatches")),
					cursor.getInt(cursor.getColumnIndex("numberOfTeamCups")), cursor.getInt(cursor.getColumnIndex("numberOfTeamTrainings")));
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), statistic.toString());
		return statistic;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Statistic getTeamStatistic(int teamId) {
		return new Statistic("", "", 0, -1, -1, -1, -1, -1, -1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Training getTrainingByDate(int teamId, long currentTimeMillis) {
		Training training = null;
		StringBuffer selection = new StringBuffer();
		selection.append(ContactsTable.COLUMN_ID + " = ?");
		String[] selectionArgs = { Integer.toString(teamId) };
		this.database = dbHelper.getReadableDatabase();
		Cursor cursor = database.query(PlayersTable.TABLE_NAME, PlayersTable.TABLE_COLUMNS, selection.toString(), selectionArgs, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			Team team = getTeam(teamId);
			training = mapCursorToTraining(cursor, team);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		CustomLog.d(this.getClass(), training.toString());
		return training;
	}
}