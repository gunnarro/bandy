package com.gunnarro.android.bandy.repository.table.activity;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.repository.table.TeamsTable;

public class MatchesTable {

	// Database table
	public static final String TABLE_NAME = "matches";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_FK_SEASON_ID = "fk_season_id";
	public static final String COLUMN_FK_TEAM_ID = "fk_team_id";
	public static final String COLUMN_START_DATE = "start_date";
	public static final String COLUMN_HOME_TEAM = "home_team";
	public static final String COLUMN_AWAY_TEAM = "away_team";
	public static final String COLUMN_NUMBER_OF_GOALS_HOME_TEAM = "goals_home_team";
	public static final String COLUMN_NUMBER_OF_GOALS_AWAY_TEAM = "goals_away_team";
	public static final String COLUMN_VENUE = "venue";
	public static final String COLUMN_REFEREE = "referee";
	public static final String COLUMN_MATCH_TYPE_ID = "match_type_id";

	public static String[] TABLE_COLUMNS = { COLUMN_ID, COLUMN_FK_SEASON_ID, COLUMN_FK_TEAM_ID, COLUMN_START_DATE, COLUMN_HOME_TEAM, COLUMN_AWAY_TEAM,
			COLUMN_NUMBER_OF_GOALS_HOME_TEAM, COLUMN_NUMBER_OF_GOALS_AWAY_TEAM, COLUMN_VENUE, COLUMN_REFEREE, COLUMN_MATCH_TYPE_ID };

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_SEASON_ID).append(" INTEGER NOT NULL DEFAULT 1");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_TEAM_ID).append(" INTEGER NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_START_DATE).append(" INTEGER NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_HOME_TEAM).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_AWAY_TEAM).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_NUMBER_OF_GOALS_HOME_TEAM).append(" INTEGER DEFAULT -1");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_NUMBER_OF_GOALS_AWAY_TEAM).append(" INETGER DEFAULT -1");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_VENUE).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_REFEREE).append(" TEXT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_MATCH_TYPE_ID).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(", FOREIGN KEY(").append(COLUMN_FK_TEAM_ID).append(") REFERENCES ").append(TeamsTable.TABLE_NAME).append("(")
				.append(TeamsTable.COLUMN_ID).append(")");
		DATABASE_CREATE_QUERY.append(",").append("UNIQUE (").append(COLUMN_HOME_TEAM).append(",").append(COLUMN_START_DATE).append(") ON CONFLICT ABORT);");
	}

	public static void onCreate(SQLiteDatabase database) {
		CustomLog.i(MatchesTable.class, DATABASE_CREATE_QUERY.toString());
		database.execSQL(DATABASE_CREATE_QUERY.toString());
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		CustomLog.i(MatchesTable.class, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
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

	public static ContentValues createContentValues(int seasonId, int fkTeamId, long startDate, String homeTeam, String awayTeam, int goalsHomeTeam,
			int goalsAwayTeam, String venue, String referee, int matchTypeId) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_FK_SEASON_ID, seasonId);
		values.put(COLUMN_FK_TEAM_ID, fkTeamId);
		values.put(COLUMN_START_DATE, (int) (startDate / 1000));
		values.put(COLUMN_HOME_TEAM, homeTeam);
		values.put(COLUMN_AWAY_TEAM, awayTeam);
		values.put(COLUMN_NUMBER_OF_GOALS_HOME_TEAM, goalsHomeTeam);
		values.put(COLUMN_NUMBER_OF_GOALS_AWAY_TEAM, goalsAwayTeam);
		values.put(COLUMN_VENUE, venue);
		values.put(COLUMN_REFEREE, referee);
		values.put(COLUMN_MATCH_TYPE_ID, matchTypeId);
		return values;
	}

}
