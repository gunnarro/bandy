package com.gunnarro.android.bandy.repository.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.domain.Club;

public class ClubsTable {

	// Database table
	public static final String TABLE_NAME = "clubs";
	public static final String COLUMN_FK_ADDRESS_ID = "fk_address_id";
	public static final String COLUMN_CLUB_NAME = "club_name";
	public static final String COLUMN_CLUB_DEPARTMENT_NAME = "club_department_name";
	public static final String COLUMN_CLUB_STADIUM_NAME = "club_stadium_name";
	public static final String COLUMN_CLUB_NAME_ABBREVIATION = "club_name_abbreviation";
	public static final String COLUMN_CLUB_URL_HOME_PAGE = "club_url_home_page";

	public static String[] TABLE_COLUMNS = TableHelper.createColumns(new String[] { COLUMN_CLUB_NAME, COLUMN_CLUB_DEPARTMENT_NAME, COLUMN_FK_ADDRESS_ID,
			COLUMN_CLUB_STADIUM_NAME, COLUMN_CLUB_NAME_ABBREVIATION, COLUMN_CLUB_URL_HOME_PAGE });

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("CREATE TABLE ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(TableHelper.createCommonColumnsQuery());
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_ADDRESS_ID).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_CLUB_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_CLUB_DEPARTMENT_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_CLUB_NAME_ABBREVIATION).append(" TEXT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_CLUB_STADIUM_NAME).append(" TEXT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_CLUB_URL_HOME_PAGE).append(" TEXT");
		DATABASE_CREATE_QUERY.append(",UNIQUE (").append(COLUMN_CLUB_NAME).append(",").append(COLUMN_CLUB_DEPARTMENT_NAME).append("));");
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

	public static ContentValues createContentValues(Club club) {
		ContentValues values = TableHelper.defaultContentValues();
		values.put(COLUMN_CLUB_DEPARTMENT_NAME, club.getDepartmentName());
		values.put(COLUMN_CLUB_NAME, club.getName());
		values.put(COLUMN_CLUB_STADIUM_NAME, club.getStadiumName());
		values.put(COLUMN_CLUB_NAME_ABBREVIATION, club.getClubNameAbbreviation());
		values.put(COLUMN_CLUB_URL_HOME_PAGE, club.getHomePageUrl());
		if (club.getAddress() != null) {
			values.put(COLUMN_FK_ADDRESS_ID, club.getAddress().getId());
		}
		return values;
	}

}
