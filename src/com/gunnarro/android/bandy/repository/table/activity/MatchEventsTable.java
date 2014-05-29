package com.gunnarro.android.bandy.repository.table.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.activity.MatchEvent;
import com.gunnarro.android.bandy.repository.table.TableHelper;

public class MatchEventsTable {

	// Database table
	public static final String TABLE_NAME = "match_events";
	public static final String COLUMN_FK_MATCH_ID = "fk_match_id";
	// Use player name, since we don not have all teams registered
	public static final String COLUMN_TEAM_NAME = "team_name";
	// Use player name, since we don not have all player names for foriegn teams
	// registered
	public static final String COLUMN_PLAYER_NAME = "player_name";
	public static final String COLUMN_PLAYED_MINUTES = "played_minutes";
	public static final String COLUMN_MATCH_EVENT_TYPE_NAME = "match_event_type_name";
	public static final String COLUMN_KEY = "key";
	public static final String COLUMN_VALUE = "value";
	public static final String COLUMN_DESCRIPTION = "description";

	public static String[] TABLE_COLUMNS = TableHelper.createColumns(new String[] { COLUMN_FK_MATCH_ID, COLUMN_PLAYED_MINUTES, COLUMN_TEAM_NAME,
			COLUMN_PLAYER_NAME, COLUMN_MATCH_EVENT_TYPE_NAME, COLUMN_KEY, COLUMN_VALUE, COLUMN_DESCRIPTION });

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(TableHelper.createCommonColumnsQuery());
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_MATCH_ID)
				.append(" INTEGER NOT NULL ON CONFLICT FAIL REFERENCES matches(_id) ON DELETE CASCADE ON UPDATE CASCADE");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_PLAYED_MINUTES).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_TEAM_NAME).append(" TEXT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_PLAYER_NAME).append(" TEXT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_MATCH_EVENT_TYPE_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_KEY).append(" TEXT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_VALUE).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_DESCRIPTION).append(" TEXT);");
		// DATABASE_CREATE_QUERY.append(", FOREIGN KEY(").append(COLUMN_FK_MATCH_ID).append(") REFERENCES ").append(MatchesTable.TABLE_NAME).append("(")
		// .append(TableHelper.COLUMN_ID).append(") ON DELETE CASCADE);");
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

	public static ContentValues createContentValues(MatchEvent matchEvent) {
		ContentValues values = TableHelper.defaultContentValues();
		values.put(COLUMN_FK_MATCH_ID, matchEvent.getMatchId());
		values.put(COLUMN_PLAYED_MINUTES, matchEvent.getPlayedMinutes());
		values.put(COLUMN_TEAM_NAME, matchEvent.getTeamName());
		values.put(COLUMN_PLAYER_NAME, matchEvent.getPlayerName());
		values.put(COLUMN_MATCH_EVENT_TYPE_NAME, matchEvent.getEventTypeName());
		values.put(COLUMN_VALUE, matchEvent.getValue());
		return values;
	}

	public static ContentValues updateContentValues(Match match) {
		ContentValues values = TableHelper.defaultContentValues();
		return values;
	}

}
