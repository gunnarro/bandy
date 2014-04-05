package com.gunnarro.android.bandy.repository.table.party;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.repository.table.TableHelper;

public class BoardMembersTable {

	// Database table
	public static final String TABLE_NAME = "board_mambers";
	public static final String COLUMN_FK_ADDRESS_ID = "fk_address_id";
	public static final String COLUMN_FK_CLUB_ID = "fk_club_id";
	public static final String COLUMN_FIRST_NAME = "first_name";
	public static final String COLUMN_MIDDLE_NAME = "middle_name";
	public static final String COLUMN_LAST_NAME = "last_name";
	public static final String COLUMN_MOBILE = "mobile";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_ROLE_TYPE = "role_type";

	public static String[] TABLE_COLUMNS = TableHelper.createColumns(new String[] { COLUMN_FK_ADDRESS_ID, COLUMN_FK_CLUB_ID, COLUMN_FIRST_NAME,
			COLUMN_MIDDLE_NAME, COLUMN_LAST_NAME, COLUMN_MOBILE, COLUMN_EMAIL });

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(TableHelper.createCommonColumnsQuery());
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_ADDRESS_ID).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_CLUB_ID).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FIRST_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_MIDDLE_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_LAST_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_MOBILE).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_EMAIL).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append("UNIQUE (").append(COLUMN_FIRST_NAME).append(",").append(COLUMN_LAST_NAME).append(") ON CONFLICT ABORT);");
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

	public static ContentValues createContentValues(Long addressId, Integer teamId, String firstName, String middleName, String lastName, String mobile,
			String epostAddress) {
		ContentValues values = TableHelper.createContentValues();
		values.put(COLUMN_FK_ADDRESS_ID, addressId);
		values.put(COLUMN_FK_CLUB_ID, teamId);
		values.put(COLUMN_FIRST_NAME, firstName);
		values.put(COLUMN_MIDDLE_NAME, middleName);
		values.put(COLUMN_LAST_NAME, lastName);
		values.put(COLUMN_MOBILE, mobile);
		values.put(COLUMN_EMAIL, epostAddress);
		return values;
	}

	public static ContentValues updateContentValues(String firstName, String middleName, String lastName, String emailAddress, String mobileNumber) {
		ContentValues values = TableHelper.createContentValues();
		values.put(COLUMN_FIRST_NAME, firstName);
		values.put(COLUMN_MIDDLE_NAME, middleName);
		values.put(COLUMN_LAST_NAME, lastName);
		values.put(COLUMN_MOBILE, mobileNumber);
		values.put(COLUMN_EMAIL, emailAddress);
		return values;
	}

}
