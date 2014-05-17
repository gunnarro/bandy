package com.gunnarro.android.bandy.repository.table.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.repository.table.TableHelper;

public class CupsTable {

	// Database table
	public static final String TABLE_NAME = "cups";
	public static final String COLUMN_FK_SEASON_ID = "fk_season_id";
	public static final String COLUMN_START_DATE = "start_date";
	public static final String COLUMN_CUP_NAME = "cup_name";
	public static final String COLUMN_CLUB_NAME = "club_name";
	public static final String COLUMN_VENUE = "venue";
	public static final String COLUMN_DEADLINE_DATE = "deadline_date";

	public static String[] TABLE_COLUMNS = TableHelper.createColumns(new String[] { COLUMN_FK_SEASON_ID, COLUMN_START_DATE, COLUMN_CUP_NAME, COLUMN_CLUB_NAME,
			COLUMN_VENUE, COLUMN_DEADLINE_DATE });

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(TableHelper.createCommonColumnsQuery());
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_SEASON_ID).append(" INTEGER NOT NULL DEFAULT 1");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_START_DATE).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_CUP_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_CLUB_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_VENUE).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_DEADLINE_DATE).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(", FOREIGN KEY(").append(COLUMN_FK_SEASON_ID).append(") REFERENCES ").append(SeasonsTable.TABLE_NAME).append("(")
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

	public static ContentValues createContentValues(int seasonId, long startDate, String cupName, String clubName, String venue, long deadlineDate) {
		ContentValues values = TableHelper.defaultContentValues();
		values.put(COLUMN_FK_SEASON_ID, seasonId);
		values.put(COLUMN_START_DATE, (int) (startDate / 1000));
		values.put(COLUMN_CUP_NAME, cupName);
		values.put(COLUMN_CLUB_NAME, clubName);
		values.put(COLUMN_VENUE, venue);
		values.put(COLUMN_DEADLINE_DATE, (int) (deadlineDate / 1000));
		return values;
	}

}
