package com.gunnarro.android.bandy.repository.table.activity;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.custom.CustomLog;

public class MatchTypesTable {

	// Database table
	public static final String TABLE_NAME = "match_types";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_MATCH_TYPE_ID = "match_type_id";
	public static final String COLUMN_MATCH_TYPE_NAME = "match_type_name";

	public static String[] TABLE_COLUMNS = { COLUMN_ID, COLUMN_MATCH_TYPE_ID, COLUMN_MATCH_TYPE_NAME };

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_MATCH_TYPE_ID).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_MATCH_TYPE_NAME).append(" TEXT NOT NULL);");
	}

	public static void onCreate(SQLiteDatabase database) {
		CustomLog.i(MatchTypesTable.class, DATABASE_CREATE_QUERY.toString());
		database.execSQL(DATABASE_CREATE_QUERY.toString());
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		CustomLog.i(MatchTypesTable.class, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
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

	public static ContentValues createContentValues(int matchTypeId, String matchTypeName) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_MATCH_TYPE_ID, matchTypeId);
		values.put(COLUMN_MATCH_TYPE_NAME, matchTypeName);
		return values;
	}

}
