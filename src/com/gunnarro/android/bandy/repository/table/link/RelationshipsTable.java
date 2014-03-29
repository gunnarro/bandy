package com.gunnarro.android.bandy.repository.table.link;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.test.suitebuilder.annotation.LargeTest;

import com.gunnarro.android.bandy.custom.CustomLog;

@Deprecated
public class RelationshipsTable {

	// Database table
	public static final String TABLE_NAME = "relationships";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_FK_PLAYER_ID = "fk_player_id";
	public static final String COLUMN_FK_CONTACT_ID = "fk_contact_id";

	public static String[] TABLE_COLUMNS = { COLUMN_ID, COLUMN_FK_PLAYER_ID, COLUMN_FK_CONTACT_ID };

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_PLAYER_ID).append(" INTEGER NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_CONTACT_ID).append(" INTEGER NOT NULL);");
	}

	public static void onCreate(SQLiteDatabase database) {
		CustomLog.i(RelationshipsTable.class, DATABASE_CREATE_QUERY.toString());
		database.execSQL(DATABASE_CREATE_QUERY.toString());
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		CustomLog.i(RelationshipsTable.class, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(database);
	}

	public static void checkColumnNames(String[] projection) {
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(TABLE_COLUMNS));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}

	public static ContentValues createContentValues(Integer playerId, Integer contactId) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_FK_PLAYER_ID, playerId);
		values.put(COLUMN_FK_CONTACT_ID, contactId);
		return values;
	}

}
