package com.gunnarro.android.bandy.repository.table.link;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.repository.table.activity.MatchesTable;
import com.gunnarro.android.bandy.repository.table.party.PlayersTable;

public class PlayerMatchLnkTable {

	// Database table
	public static final String TABLE_NAME = "player_match_lnk";
	public static final String COLUMN_FK_PLAYER_ID = "fk_player_id";
	public static final String COLUMN_FK_MATCH_ID = "fk_match_id";

	public static void onCreate(SQLiteDatabase database) {
		LinkTableHelper.onCreate(database, TABLE_NAME, COLUMN_FK_PLAYER_ID, COLUMN_FK_MATCH_ID, PlayersTable.TABLE_NAME, MatchesTable.TABLE_NAME);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		LinkTableHelper.onUpgrade(database, TABLE_NAME, COLUMN_FK_PLAYER_ID, COLUMN_FK_MATCH_ID, PlayersTable.TABLE_NAME, MatchesTable.TABLE_NAME, oldVersion,
				newVersion);
	}

	public static String[] getTableColumns() {
		return new String[] { LinkTableHelper.COLUMN_ID, COLUMN_FK_PLAYER_ID, COLUMN_FK_MATCH_ID };
	}

	public static ContentValues createContentValues(Integer playerId, Integer matchId) {
		return LinkTableHelper.createContentValues(new String[] { COLUMN_FK_PLAYER_ID, COLUMN_FK_MATCH_ID }, new Integer[] { playerId, matchId });
	}
}
