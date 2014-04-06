package com.gunnarro.android.bandy.repository.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.domain.Team;

public class TeamsTable {

	// Database table
	public static final String TABLE_NAME = "teams";
	public static final String COLUMN_FK_CLUB_ID = "fk_club_id";
	public static final String COLUMN_FK_LEAGUE_ID = "fk_league_id";
	public static final String COLUMN_TEAM_NAME = "team_name";
	public static final String COLUMN_TEAM_YEAR_OF_BIRTH = "team_year_of_birth";
	public static final String COLUMN_TEAM_GENDER = "team_gender";

	public static String[] TABLE_COLUMNS = TableHelper.createColumns(new String[] { COLUMN_FK_CLUB_ID, COLUMN_FK_LEAGUE_ID, COLUMN_TEAM_NAME,
			COLUMN_TEAM_YEAR_OF_BIRTH, COLUMN_TEAM_GENDER });

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(TableHelper.createCommonColumnsQuery());
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_CLUB_ID).append(" INTEGER NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_LEAGUE_ID).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_TEAM_NAME).append(" TEXT NOT NULL UNIQUE");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_TEAM_YEAR_OF_BIRTH).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_TEAM_GENDER).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(", FOREIGN KEY(").append(COLUMN_FK_CLUB_ID).append(") REFERENCES ").append(ClubsTable.TABLE_NAME).append("(")
				.append(TableHelper.COLUMN_ID).append("));");
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

	public static ContentValues createContentValues(Team team) {
		ContentValues values = TableHelper.createContentValues();
		values.put(COLUMN_FK_CLUB_ID, team.getClub().getId());
		if (team.getLeague() != null) {
			values.put(COLUMN_FK_LEAGUE_ID, team.getLeague().getId());
		}
		values.put(COLUMN_TEAM_NAME, team.getName());
		values.put(COLUMN_TEAM_YEAR_OF_BIRTH, team.getTeamYearOfBirth());
		values.put(COLUMN_TEAM_GENDER, team.getGender());
		return values;
	}

	public static ContentValues updateContentValues(String teamName) {
		ContentValues values = TableHelper.createContentValues();
		values.put(COLUMN_TEAM_NAME, teamName);
		return values;
	}

}
