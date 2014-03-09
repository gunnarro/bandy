package com.gunnarro.android.bandy.repository.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class TeamsTable {

	// Database table
	public static final String TABLE_NAME = "teams";
	public static final String COLUMN_FK_CLUB_ID = "fk_club_id";
	public static final String COLUMN_TEAM_NAME = "team_name";

	public static String[] TABLE_COLUMNS = TableHelper.createColumns(new String[] { COLUMN_FK_CLUB_ID, COLUMN_TEAM_NAME });

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(TableHelper.createCommonColumnsQuery());
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_CLUB_ID).append(" INTEGER NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_TEAM_NAME).append(" TEXT NOT NULL UNIQUE");
		DATABASE_CREATE_QUERY.append(", FOREIGN KEY(").append(COLUMN_FK_CLUB_ID).append(") REFERENCES ").append(ClubsTable.TABLE_NAME).append("(")
				.append(TableHelper.COLUMN_ID).append("));");
	}

	public static void onCreate(SQLiteDatabase database) {
		TableHelper.onCreate(database, DATABASE_CREATE_QUERY.toString());
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		TableHelper.onUpgrade(database, oldVersion, newVersion, TABLE_NAME, DATABASE_CREATE_QUERY.toString());
	}

	public static void checkColumnNames(String[] projection) {
		TableHelper.checkColumnNames(projection, TABLE_COLUMNS);
	}

	public static ContentValues createContentValues(Integer fkClubId, String teamName) {
		ContentValues values = TableHelper.createContentValues();
		values.put(COLUMN_FK_CLUB_ID, fkClubId);
		values.put(COLUMN_TEAM_NAME, teamName);
		return values;
	}

}
