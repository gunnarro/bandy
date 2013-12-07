package com.gunnarro.android.bandy.repository.table;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.custom.CustomLog;

public class TraningsTable {

	// Database table
	public static final String TABLE_NAME = "trainings";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_FK_TEAM_ID = "fk_team_id";
	public static final String COLUMN_START_DATE = "start_date";
	public static final String COLUMN_END_TIME = "end_time";
	public static final String COLUMN_PLACE = "place";

	public static String[] TABLE_COLUMNS = { COLUMN_ID, COLUMN_FK_TEAM_ID, COLUMN_START_DATE, COLUMN_END_TIME, COLUMN_PLACE };

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_TEAM_ID).append(" INTEGER NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_START_DATE).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_END_TIME).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_PLACE).append(" TEXT NOT NULL);");
	}

	public static void onCreate(SQLiteDatabase database) {
		CustomLog.i(TraningsTable.class, DATABASE_CREATE_QUERY.toString());
		database.execSQL(DATABASE_CREATE_QUERY.toString());
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		CustomLog.i(TraningsTable.class, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
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

	public static ContentValues createContentValues(int fkTeamId, long startDate, long endTime, String place) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_FK_TEAM_ID, fkTeamId);
		values.put(COLUMN_START_DATE, (int) (startDate / 1000));
		values.put(COLUMN_END_TIME, (int) (endTime / 1000));
		values.put(COLUMN_PLACE, place);
		return values;
	}

}
