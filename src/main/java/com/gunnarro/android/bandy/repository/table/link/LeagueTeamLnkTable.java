package com.gunnarro.android.bandy.repository.table.link;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.repository.table.TableHelper;
import com.gunnarro.android.bandy.repository.table.TeamsTable;
import com.gunnarro.android.bandy.repository.table.activity.LeaguesTable;

public class LeagueTeamLnkTable {

	// Database table
	public static final String TABLE_NAME = "league_team_lnk";
	public static final String COLUMN_FK_LEAGUE_ID = "fk_league_id";
	public static final String COLUMN_FK_TEAM_ID = "fk_team_id";

	public static void onCreate(SQLiteDatabase database) {
		LinkTableHelper.onCreate(database, TABLE_NAME, COLUMN_FK_LEAGUE_ID, COLUMN_FK_TEAM_ID, LeaguesTable.TABLE_NAME, TeamsTable.TABLE_NAME);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		LinkTableHelper.onUpgrade(database, TABLE_NAME, COLUMN_FK_LEAGUE_ID, COLUMN_FK_TEAM_ID, LeaguesTable.TABLE_NAME, TeamsTable.TABLE_NAME, oldVersion,
				newVersion);
	}

	public static String[] getTableColumns() {
		return TableHelper.createColumns(new String[] { COLUMN_FK_LEAGUE_ID, COLUMN_FK_TEAM_ID });
	}

	public static ContentValues createContentValues(Integer leagueId, Integer teamId) {
		return LinkTableHelper.createContentValues(new String[] { COLUMN_FK_LEAGUE_ID, COLUMN_FK_TEAM_ID }, new Integer[] { leagueId, teamId });
	}
}
