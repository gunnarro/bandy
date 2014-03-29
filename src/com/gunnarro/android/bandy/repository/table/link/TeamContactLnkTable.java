package com.gunnarro.android.bandy.repository.table.link;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.repository.table.TableHelper;
import com.gunnarro.android.bandy.repository.table.TeamsTable;
import com.gunnarro.android.bandy.repository.table.party.ContactsTable;

public class TeamContactLnkTable {

	// Database table
	public static final String TABLE_NAME = "team_contact_lnk";
	public static final String COLUMN_FK_TEAM_ID = "fk_team_id";
	public static final String COLUMN_FK_CONTACT_ID = "fk_contact_id";
	public static final String[] TABLE_COLUMNS = { COLUMN_FK_TEAM_ID, COLUMN_FK_CONTACT_ID };

	public static void onCreate(SQLiteDatabase database) {
		LinkTableHelper.onCreate(database, TABLE_NAME, COLUMN_FK_TEAM_ID, COLUMN_FK_CONTACT_ID, TeamsTable.TABLE_NAME, ContactsTable.TABLE_NAME);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		LinkTableHelper.onUpgrade(database, TABLE_NAME, COLUMN_FK_TEAM_ID, COLUMN_FK_CONTACT_ID, TeamsTable.TABLE_NAME, ContactsTable.TABLE_NAME, oldVersion,
				newVersion);
	}

	public static String[] getTableColumns() {
		return TableHelper.createColumns(new String[] { COLUMN_FK_TEAM_ID, COLUMN_FK_CONTACT_ID });
	}

	public static ContentValues createContentValues(Integer teamId, Integer contactId) {
		return LinkTableHelper.createContentValues(new String[] { COLUMN_FK_TEAM_ID, COLUMN_FK_CONTACT_ID }, new Integer[] { teamId, contactId });
	}
}
