package com.gunnarro.android.bandy.repository.table;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.custom.CustomLog;

public class PlayersTable {

	// Database table
	public static final String TABLE_NAME = "players";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TEAM_ID = "team_id";
	public static final String COLUMN_FIRST_NAME = "first_name";
	public static final String COLUMN_MIDDLE_NAME = "middle_name";
	public static final String COLUMN_LAST_NAME = "last_name";
	public static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_MOBILE = "mobile";

	public static String[] TABLE_COLUMNS = { COLUMN_ID, COLUMN_FIRST_NAME, COLUMN_MIDDLE_NAME, COLUMN_LAST_NAME, COLUMN_DATE_OF_BIRTH, COLUMN_EMAIL,
			COLUMN_MOBILE };

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT");
		// DATABASE_CREATE_QUERY.append(",").append(COLUMN_FILTER_NAME).append(" TEXT NOT NULL");
		// DATABASE_CREATE_QUERY.append(",").append(COLUMN_ACTIVATED).append(" TEXT NOT NULL);");
	}

	public static void onCreate(SQLiteDatabase database) {
		CustomLog.i(PlayersTable.class, DATABASE_CREATE_QUERY.toString());
		database.execSQL(DATABASE_CREATE_QUERY.toString());
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		CustomLog.i(PlayersTable.class, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
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

	public static ContentValues createContentValues(String fistName, String middleName, String lastName, String isActive) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_FIRST_NAME, fistName);
		values.put(COLUMN_MIDDLE_NAME, middleName);
		values.put(COLUMN_LAST_NAME, lastName);
		values.put(COLUMN_ID, isActive);
		return values;
	}

}