package com.gunnarro.android.bandy.repository.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.domain.Team;

/**
 * CREATE TABLE clubs(_id INTEGER PRIMARY KEY AUTOINCREMENT, created_date_time
 * DATETIME DEFAULT 'CURRENT_TIMESTAMP', last_modified_date_time DATETIME
 * DEFAULT 'CURRENT_TIMESTAMP', fk_address_id INTEGER UNIQUE ON CONFLICT FAIL
 * REFERENCES addresses(_id) ON DELETE SET NULL ON UPDATE CASCADE, club_name
 * TEXT NOT NULL, club_department_name TEXT NOT NULL, club_name_abbreviation
 * TEXT, club_stadium_name TEXT, club_url_home_page TEXT,
 * UNIQUE(club_name,club_department_name));
 */
public class ClubsTable {

	// Database table
	public static final String TABLE_NAME = "clubs";
	public static final String COLUMN_FK_ADDRESS_ID = "fk_address_id";
	public static final String COLUMN_CLUB_NAME = "club_name";
	public static final String COLUMN_CLUB_DEPARTMENT_NAME = "club_department_name";
	public static final String COLUMN_CLUB_STADIUM_NAME = "club_stadium_name";
	public static final String COLUMN_CLUB_NAME_ABBREVIATION = "club_name_abbreviation";
	public static final String COLUMN_CLUB_URL_HOME_PAGE = "club_url_home_page";

	public static String CREATE_INDEX = "CREATE INDEX " + TABLE_NAME + "index ON " + TABLE_NAME + "(" + TableHelper.COLUMN_ID + ");";

	public static String[] TABLE_COLUMNS = TableHelper.createColumns(new String[] { COLUMN_CLUB_NAME, COLUMN_CLUB_DEPARTMENT_NAME, COLUMN_FK_ADDRESS_ID,
			COLUMN_CLUB_STADIUM_NAME, COLUMN_CLUB_NAME_ABBREVIATION, COLUMN_CLUB_URL_HOME_PAGE });

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("CREATE TABLE ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(TableHelper.createCommonColumnsQuery());
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_ADDRESS_ID)
				.append(" INTEGER UNIQUE ON CONFLICT FAIL REFERENCES addresses(_id) ON DELETE SET NULL ON UPDATE CASCADE");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_CLUB_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_CLUB_DEPARTMENT_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_CLUB_NAME_ABBREVIATION).append(" TEXT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_CLUB_STADIUM_NAME).append(" TEXT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_CLUB_URL_HOME_PAGE).append(" TEXT");
		DATABASE_CREATE_QUERY.append(", UNIQUE (").append(COLUMN_CLUB_NAME).append(",").append(COLUMN_CLUB_DEPARTMENT_NAME).append("));");
		// DATABASE_CREATE_QUERY.append(", FOREIGN KEY(").append(COLUMN_FK_ADDRESS_ID).append(") REFERENCES ").append(AddressTable.TABLE_NAME).append("(")
		// .append(TableHelper.COLUMN_ID).append("));");
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

	public static ContentValues createContentValues(Long addressId, Club club) {
		ContentValues values = TableHelper.defaultContentValues();
		values.put(COLUMN_CLUB_DEPARTMENT_NAME, club.getDepartmentName());
		values.put(COLUMN_CLUB_NAME, club.getName());
		values.put(COLUMN_CLUB_STADIUM_NAME, club.getStadiumName());
		values.put(COLUMN_CLUB_NAME_ABBREVIATION, club.getClubNameAbbreviation());
		values.put(COLUMN_CLUB_URL_HOME_PAGE, club.getHomePageUrl());
		if (addressId != null) {
			values.put(COLUMN_FK_ADDRESS_ID, addressId);
		}
		return values;
	}

	public static ContentValues updateContentValues(Club club) {
		ContentValues values = TableHelper.defaultContentValues();
		values.put(COLUMN_CLUB_NAME, club.getName());
		return values;
	}

}