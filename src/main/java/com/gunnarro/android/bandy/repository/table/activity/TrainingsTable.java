package com.gunnarro.android.bandy.repository.table.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.domain.activity.Training;
import com.gunnarro.android.bandy.repository.table.TableHelper;

public class TrainingsTable {

	// Database table
	public static final String TABLE_NAME = "trainings";
	public static final String COLUMN_FK_SEASON_ID = "fk_season_id";
	public static final String COLUMN_FK_TEAM_ID = "fk_team_id";
	public static final String COLUMN_START_DATE = "start_date";
	public static final String COLUMN_END_TIME = "end_time";
	public static final String COLUMN_PLACE = "place";

	public static String[] TABLE_COLUMNS = TableHelper.createColumns(new String[] { COLUMN_FK_SEASON_ID, COLUMN_FK_TEAM_ID, COLUMN_START_DATE, COLUMN_END_TIME,
			COLUMN_PLACE });

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(TableHelper.createCommonColumnsQuery());
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_TEAM_ID).append(" INTEGER NOT NULL ON CONFLICT FAIL REFERENCES teams(_id) ON DELETE CASCADE ON UPDATE CASCADE");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_SEASON_ID).append(" INTEGER NOT NULL ON CONFLICT FAIL REFERENCES seasons(_id) ON DELETE CASCADE ON UPDATE CASCADE");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_START_DATE).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_END_TIME).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_PLACE).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append("UNIQUE (").append(COLUMN_FK_TEAM_ID).append(",").append(COLUMN_START_DATE).append(") ON CONFLICT FAIL);");
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

	public static ContentValues createContentValues(Training training) {
		ContentValues values = TableHelper.defaultContentValues();
		values.put(COLUMN_FK_SEASON_ID, training.getSeason().getId());
		values.put(COLUMN_FK_TEAM_ID, training.getTeam().getId());
		values.put(COLUMN_START_DATE, (int) (training.getStartTime() / 1000));
		values.put(COLUMN_END_TIME, (int) (training.getEndTime() / 1000));
		values.put(COLUMN_PLACE, training.getVenue());
		return values;
	}

}
