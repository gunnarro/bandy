package com.gunnarro.android.bandy.repository.table.link;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.repository.table.TableHelper;
import com.gunnarro.android.bandy.repository.table.TeamsTable;
import com.gunnarro.android.bandy.repository.table.activity.TournamentsTable;

public class TeamTournamentLnkTable {

	// Database table
	public static final String TABLE_NAME = "team_tournament_lnk";
	public static final String COLUMN_FK_TEAM_ID = "fk_team_id";
	public static final String COLUMN_FK_TOURNAMENT_ID = "fk_tournament_id";

	public static void onCreate(SQLiteDatabase database) {
		LinkTableHelper.onCreate(database, TABLE_NAME, COLUMN_FK_TEAM_ID, COLUMN_FK_TOURNAMENT_ID, TeamsTable.TABLE_NAME, TournamentsTable.TABLE_NAME);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		LinkTableHelper.onUpgrade(database, TABLE_NAME, COLUMN_FK_TEAM_ID, COLUMN_FK_TOURNAMENT_ID, TeamsTable.TABLE_NAME, TournamentsTable.TABLE_NAME,
				oldVersion, newVersion);
	}

	public static String[] getTableColumns() {
		return TableHelper.createColumns(getTableFkKeyColumns());
	}

	public static String[] getTableFkKeyColumns() {
		return new String[] { COLUMN_FK_TEAM_ID, COLUMN_FK_TOURNAMENT_ID };
	}

	public static ContentValues createContentValues(Integer teamId, Integer tournamentId) {
		return LinkTableHelper.createContentValues(getTableFkKeyColumns(), new Integer[] { teamId, tournamentId });
	}
}
