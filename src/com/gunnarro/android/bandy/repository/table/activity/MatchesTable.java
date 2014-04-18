package com.gunnarro.android.bandy.repository.table.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.repository.table.TableHelper;
import com.gunnarro.android.bandy.repository.table.TeamsTable;

public class MatchesTable {

	// Database table
	public static final String TABLE_NAME = "matches";
	public static final String COLUMN_FK_SEASON_ID = "fk_season_id";
	public static final String COLUMN_FK_TEAM_ID = "fk_team_id";
	public static final String COLUMN_START_DATE = "start_date";
	public static final String COLUMN_HOME_TEAM_NAME = "home_team";
	public static final String COLUMN_AWAY_TEAM_NAME = "away_team";
	public static final String COLUMN_NUMBER_OF_GOALS_HOME_TEAM = "goals_home_team";
	public static final String COLUMN_NUMBER_OF_GOALS_AWAY_TEAM = "goals_away_team";
	public static final String COLUMN_VENUE = "venue";
	public static final String COLUMN_REFEREE = "referee";
	public static final String COLUMN_MATCH_TYPE_ID = "match_type_id";
	public static final String COLUMN_MATCH_STATUS_NAME = "match_status_name";

	public static String[] TABLE_COLUMNS = TableHelper.createColumns(new String[] { COLUMN_FK_SEASON_ID, COLUMN_FK_TEAM_ID, COLUMN_START_DATE,
			COLUMN_HOME_TEAM_NAME, COLUMN_AWAY_TEAM_NAME, COLUMN_NUMBER_OF_GOALS_HOME_TEAM, COLUMN_NUMBER_OF_GOALS_AWAY_TEAM, COLUMN_VENUE, COLUMN_REFEREE,
			COLUMN_MATCH_TYPE_ID, COLUMN_MATCH_STATUS_NAME });

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(TableHelper.createCommonColumnsQuery());
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_SEASON_ID).append(" INTEGER NOT NULL DEFAULT 1");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_TEAM_ID).append(" INTEGER NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_START_DATE).append(" INTEGER NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_HOME_TEAM_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_AWAY_TEAM_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_NUMBER_OF_GOALS_HOME_TEAM).append(" INTEGER DEFAULT 0");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_NUMBER_OF_GOALS_AWAY_TEAM).append(" INETGER DEFAULT 0");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_VENUE).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_REFEREE).append(" TEXT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_MATCH_TYPE_ID).append(" INTEGER DEFAULT 1");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_MATCH_STATUS_NAME).append(" STRING DEFAULT NOT_PLAYED");
		DATABASE_CREATE_QUERY.append(", FOREIGN KEY(").append(COLUMN_FK_TEAM_ID).append(") REFERENCES ").append(TeamsTable.TABLE_NAME).append("(")
				.append(TableHelper.COLUMN_ID).append(")");
		DATABASE_CREATE_QUERY.append(",").append("UNIQUE (").append(COLUMN_HOME_TEAM_NAME).append(",").append(COLUMN_START_DATE)
				.append(") ON CONFLICT ABORT);");
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

	public static ContentValues createContentValues(Match match) {
		ContentValues values = TableHelper.defaultContentValues();
		values.put(COLUMN_FK_SEASON_ID, match.getSeason().getId());
		values.put(COLUMN_FK_TEAM_ID, match.getTeam().getId());
		values.put(COLUMN_START_DATE, (int) (match.getStartTime() / 1000));
		values.put(COLUMN_HOME_TEAM_NAME, match.getHomeTeam().getName());
		values.put(COLUMN_AWAY_TEAM_NAME, match.getAwayTeam().getName());
		values.put(COLUMN_NUMBER_OF_GOALS_HOME_TEAM, match.getNumberOfGoalsHome());
		values.put(COLUMN_NUMBER_OF_GOALS_AWAY_TEAM, match.getNumberOfGoalsAway());
		values.put(COLUMN_VENUE, match.getVenue());
		values.put(COLUMN_REFEREE, match.getReferee().getFullName());
		values.put(COLUMN_MATCH_TYPE_ID, match.getMatchType().getCode());
		return values;
	}

	public static ContentValues updateContentValues(Match match) {
		ContentValues values = TableHelper.defaultContentValues();
		values.put(COLUMN_START_DATE, (int) (match.getStartTime() / 1000));
		values.put(COLUMN_NUMBER_OF_GOALS_HOME_TEAM, match.getNumberOfGoalsHome());
		values.put(COLUMN_NUMBER_OF_GOALS_AWAY_TEAM, match.getNumberOfGoalsAway());
		values.put(COLUMN_REFEREE, match.getReferee().getFullName());
		values.put(COLUMN_VENUE, match.getVenue());
		return values;
	}

	public static ContentValues updateContentValues(String columnName, Integer goals) {
		ContentValues values = TableHelper.defaultContentValues();
		values.put(columnName, goals);
		return values;
	}
}
