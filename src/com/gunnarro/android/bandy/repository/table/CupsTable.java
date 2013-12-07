package com.gunnarro.android.bandy.repository.table;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.custom.CustomLog;

public class CupsTable {

	// Database table
	public static final String TABLE_NAME = "cups";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_START_DATE = "start_date";
	public static final String COLUMN_CUP_NAME = "cup_name";
	public static final String COLUMN_CLUB_NAME = "club_name";
	public static final String COLUMN_VENUE = "venue";
	public static final String COLUMN_DEADLINE_DATE = "deadline_date";

	public static String[] TABLE_COLUMNS = { COLUMN_ID, COLUMN_START_DATE, COLUMN_CUP_NAME, COLUMN_CLUB_NAME, COLUMN_VENUE, COLUMN_DEADLINE_DATE };

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_START_DATE).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_CUP_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_CLUB_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_VENUE).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_DEADLINE_DATE).append(" INTEGER);");

	}

	public static void onCreate(SQLiteDatabase database) {
		CustomLog.i(CupsTable.class, DATABASE_CREATE_QUERY.toString());
		database.execSQL(DATABASE_CREATE_QUERY.toString());
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		CustomLog.i(CupsTable.class, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
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

	public static ContentValues createContentValues(long startDate, String cupName, String clubName, String venue, long deadlineDate) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_START_DATE, (int) (startDate / 1000));
		values.put(COLUMN_CUP_NAME, cupName);
		values.put(COLUMN_CLUB_NAME, clubName);
		values.put(COLUMN_VENUE, venue);
		values.put(COLUMN_DEADLINE_DATE, (int) (deadlineDate / 1000));
		return values;
	}

}
