package com.gunnarro.android.bandy.repository.table.link;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.repository.table.TableHelper;
import com.gunnarro.android.bandy.repository.table.activity.CupsTable;
import com.gunnarro.android.bandy.repository.table.party.PlayersTable;

public class TeamCupLnkTable {

	// Database table
	public static final String TABLE_NAME = "team_cup_lnk";
	public static final String COLUMN_FK_TEAM_ID = "fk_team_id";
	public static final String COLUMN_FK_CUP_ID = "fk_cup_id";

	public static void onCreate(SQLiteDatabase database) {
		LinkTableHelper.onCreate(database, TABLE_NAME, COLUMN_FK_TEAM_ID, COLUMN_FK_CUP_ID, PlayersTable.TABLE_NAME, CupsTable.TABLE_NAME);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		LinkTableHelper.onUpgrade(database, TABLE_NAME, COLUMN_FK_TEAM_ID, COLUMN_FK_CUP_ID, PlayersTable.TABLE_NAME, CupsTable.TABLE_NAME, oldVersion,
				newVersion);
	}

	public static String[] getTableColumns() {
		return TableHelper.createColumns(new String[] { COLUMN_FK_TEAM_ID, COLUMN_FK_CUP_ID });
	}

	public static ContentValues createContentValues(Integer playerId, Integer cupId) {
		return LinkTableHelper.createContentValues(new String[] { COLUMN_FK_TEAM_ID, COLUMN_FK_CUP_ID }, new Integer[] { playerId, cupId });
	}
}
