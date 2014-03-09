package com.gunnarro.android.bandy.repository.table.activity;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.custom.CustomLog;

public class PlayerPositionTypesTable {

	// Database table
	public static final String TABLE_NAME = "position_types";
	public static final String COLUMN_CREATED_DATE = "created_date";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_POSITION_TYPE_ID = "position_type_id";
	public static final String COLUMN_POSITION_TYPE_NAME = "position_type_name";

	public static String[] TABLE_COLUMNS = { COLUMN_CREATED_DATE, COLUMN_ID, COLUMN_POSITION_TYPE_ID, COLUMN_POSITION_TYPE_NAME };

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(COLUMN_CREATED_DATE).append(" INTEGER NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_POSITION_TYPE_ID).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_POSITION_TYPE_NAME).append(" TEXT NOT NULL);");
	}

	public static void onCreate(SQLiteDatabase database) {
		CustomLog.i(PlayerPositionTypesTable.class, DATABASE_CREATE_QUERY.toString());
		database.execSQL(DATABASE_CREATE_QUERY.toString());
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		CustomLog
				.i(PlayerPositionTypesTable.class, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
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

	public static ContentValues createContentValues(int positionTypeId, String positionTypeName) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_CREATED_DATE, System.currentTimeMillis());
		values.put(COLUMN_POSITION_TYPE_ID, positionTypeId);
		values.put(COLUMN_POSITION_TYPE_NAME, positionTypeName);
		return values;
	}

}
