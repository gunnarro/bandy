package com.gunnarro.android.bandy.repository.table.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.domain.activity.Cup;
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
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_SEASON_ID)
				.append(" INTEGER NOT NULL DEFAULT 1 UNIQUE ON CONFLICT FAIL REFERENCES seasons(_id) ON DELETE CASCADE ON UPDATE CASCADE");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_START_DATE).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_CUP_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_CLUB_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_VENUE).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_DEADLINE_DATE).append(" INTEGER);");
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

	public static ContentValues createContentValues(Cup cup) {
		ContentValues values = TableHelper.defaultContentValues();
		values.put(COLUMN_FK_SEASON_ID, cup.getSeason().getId());
		values.put(COLUMN_START_DATE, (int) (cup.getStartDate() / 1000));
		values.put(COLUMN_CUP_NAME, cup.getCupName());
		values.put(COLUMN_CLUB_NAME, cup.getClubName());
		values.put(COLUMN_VENUE, cup.getVenue());
		values.put(COLUMN_DEADLINE_DATE, (int) (cup.getDeadlineDate() / 1000));
		return values;
	}

}
