package com.gunnarro.android.bandy.repository.table;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.custom.CustomLog;

public class TeamsTable {

	// Database table
	public static final String TABLE_NAME = "teams";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_FK_CLUB_ID = "fk_club_id";
	public static final String COLUMN_TEAM_NAME = "team_name";

	public static String[] TABLE_COLUMNS = { COLUMN_ID, COLUMN_FK_CLUB_ID, COLUMN_TEAM_NAME };

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(COLUMN_ID).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_CLUB_ID).append(" INTEGER NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_TEAM_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(", FOREIGN KEY(").append(COLUMN_FK_CLUB_ID).append(") REFERENCES ").append(ClubsTable.TABLE_NAME).append("(")
				.append(ClubsTable.COLUMN_ID).append(")");
		DATABASE_CREATE_QUERY.append(", PRIMARY KEY (").append(COLUMN_TEAM_NAME).append("));");
	}

	public static void onCreate(SQLiteDatabase database) {
		CustomLog.i(TeamsTable.class, DATABASE_CREATE_QUERY.toString());
		database.execSQL(DATABASE_CREATE_QUERY.toString());
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		CustomLog.i(TeamsTable.class, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
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

	public static ContentValues createContentValues(Integer fkClubId, String teamName) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_FK_CLUB_ID, fkClubId);
		values.put(COLUMN_TEAM_NAME, teamName);
		return values;
	}

}
