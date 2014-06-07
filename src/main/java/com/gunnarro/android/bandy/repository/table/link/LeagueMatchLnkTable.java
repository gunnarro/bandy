package com.gunnarro.android.bandy.repository.table.link;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.repository.table.TableHelper;
import com.gunnarro.android.bandy.repository.table.activity.LeaguesTable;
import com.gunnarro.android.bandy.repository.table.activity.MatchesTable;

public class LeagueMatchLnkTable {

	// Database table
	public static final String TABLE_NAME = "league_match_lnk";
	public static final String COLUMN_FK_LEAGUE_ID = "fk_league_id";
	public static final String COLUMN_FK_MATCH_ID = "fk_match_id";

	public static void onCreate(SQLiteDatabase database) {
		LinkTableHelper.onCreate(database, TABLE_NAME, COLUMN_FK_LEAGUE_ID, COLUMN_FK_MATCH_ID, LeaguesTable.TABLE_NAME, MatchesTable.TABLE_NAME);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		LinkTableHelper.onUpgrade(database, TABLE_NAME, COLUMN_FK_LEAGUE_ID, COLUMN_FK_MATCH_ID, LeaguesTable.TABLE_NAME, MatchesTable.TABLE_NAME, oldVersion,
				newVersion);
	}

	public static String[] getTableColumns() {
		return TableHelper.createColumns(new String[] { COLUMN_FK_LEAGUE_ID, COLUMN_FK_MATCH_ID });
	}

	public static ContentValues createContentValues(Integer leagueId, Integer matchId) {
		return LinkTableHelper.createContentValues(new String[] { COLUMN_FK_LEAGUE_ID, COLUMN_FK_MATCH_ID }, new Integer[] { leagueId, matchId });
	}
}
