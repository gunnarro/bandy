package com.gunnarro.android.bandy.repository.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class ClubsTable {

	// Database table
	public static final String TABLE_NAME = "clubs";
	public static final String COLUMN_CLUB_NAME = "club_name";
	public static final String COLUMN_STADIUM = "club_stadium";

	public static String[] TABLE_COLUMNS = TableHelper.createColumns(new String[] { COLUMN_CLUB_NAME });

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("CREATE TABLE ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(TableHelper.createCommonColumnsQuery());
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_CLUB_NAME).append(" TEXT NOT NULL UNIQUE);");
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

	public static ContentValues createContentValues(String clubName) {
		ContentValues values = TableHelper.createContentValues();
		values.put(COLUMN_CLUB_NAME, clubName);
		return values;
	}

}
