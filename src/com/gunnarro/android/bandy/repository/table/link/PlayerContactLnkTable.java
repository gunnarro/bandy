package com.gunnarro.android.bandy.repository.table.link;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.repository.table.party.ContactsTable;
import com.gunnarro.android.bandy.repository.table.party.PlayersTable;

public class PlayerContactLnkTable {

	// Database table
	public static final String TABLE_NAME = "player_contact_lnk";
	public static final String COLUMN_FK_PLAYER_ID = "fk_player_id";
	public static final String COLUMN_FK_CONTACT_ID = "fk_contact_id";
	public static final String[] TABLE_COLUMNS = { COLUMN_FK_PLAYER_ID, COLUMN_FK_CONTACT_ID };

	public static void onCreate(SQLiteDatabase database) {
		LinkTableHelper.onCreate(database, TABLE_NAME, COLUMN_FK_PLAYER_ID, COLUMN_FK_CONTACT_ID, PlayersTable.TABLE_NAME, ContactsTable.TABLE_NAME);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		LinkTableHelper.onUpgrade(database, TABLE_NAME, COLUMN_FK_PLAYER_ID, COLUMN_FK_CONTACT_ID, PlayersTable.TABLE_NAME, ContactsTable.TABLE_NAME,
				oldVersion, newVersion);
	}

	public static String[] getTableFkKeyColumns() {
		return new String[] { COLUMN_FK_PLAYER_ID, COLUMN_FK_CONTACT_ID };
	}

	public static ContentValues createContentValues(Integer playerId, Integer contactId) {
		return LinkTableHelper.createContentValues(getTableFkKeyColumns(), new Integer[] { playerId, contactId });
	}
}
