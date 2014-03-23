package com.gunnarro.android.bandy.repository.table.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.repository.table.TableHelper;

public class LeaguesTable {

	// Database table
	public static final String TABLE_NAME = "leagues";
	public static final String COLUMN_LEAGUE_NAME = "league_name";
	public static final String COLUMN_LEAGUE_PLAYER_AGE_MIN = "league_player_age_min";
	public static final String COLUMN_LEAGUE_PLAYER_AGE_MAX = "league_player_age_max";
	public static final String COLUMN_LEAGUE_GENDER = "league_gender";
	public static final String COLUMN_LEAGUE_MATCH_PERIOD_TIME_MINUTES = "league_match_period_time_minutes";
	public static final String COLUMN_LEAGUE_MATCH_EXTRA_PERIOD_TIME_MINUTES = "league_match_extra_period_time_minutes";
	public static final String COLUMN_LEAGUE_NUMBER_OF_PLAYERS = "league_number_of_players";
	public static final String COLUMN_LEAGUE_DESCRIPTION = "league_description";

	public static String[] TABLE_COLUMNS = TableHelper.createColumns(new String[] { COLUMN_LEAGUE_NAME, COLUMN_LEAGUE_PLAYER_AGE_MIN,
			COLUMN_LEAGUE_PLAYER_AGE_MAX, COLUMN_LEAGUE_GENDER, COLUMN_LEAGUE_MATCH_PERIOD_TIME_MINUTES, COLUMN_LEAGUE_MATCH_EXTRA_PERIOD_TIME_MINUTES,
			COLUMN_LEAGUE_NUMBER_OF_PLAYERS, COLUMN_LEAGUE_DESCRIPTION });

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(TableHelper.createCommonColumnsQuery());
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_LEAGUE_NAME).append(" TEXT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_LEAGUE_PLAYER_AGE_MIN).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_LEAGUE_PLAYER_AGE_MAX).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_LEAGUE_GENDER).append(" TEXT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_LEAGUE_MATCH_PERIOD_TIME_MINUTES).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_LEAGUE_MATCH_EXTRA_PERIOD_TIME_MINUTES).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_LEAGUE_NUMBER_OF_PLAYERS).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_LEAGUE_DESCRIPTION).append(" TEXT);");
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

	public static ContentValues createContentValues(String leagueName, String leagueGender, int playerAgeMin, int playerAgeMax, int matchPeriodeTime,
			String description) {
		ContentValues values = TableHelper.createContentValues();
		values.put(COLUMN_LEAGUE_NAME, leagueName);
		values.put(COLUMN_LEAGUE_GENDER, leagueGender);
		values.put(COLUMN_LEAGUE_PLAYER_AGE_MIN, playerAgeMin);
		values.put(COLUMN_LEAGUE_PLAYER_AGE_MAX, playerAgeMax);
		values.put(COLUMN_LEAGUE_MATCH_PERIOD_TIME_MINUTES, matchPeriodeTime);
		values.put(COLUMN_LEAGUE_DESCRIPTION, description);
		return values;
	}

}
