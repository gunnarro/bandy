package com.gunnarro.android.bandy.repository.table.party;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.domain.party.Contact;
import com.gunnarro.android.bandy.repository.table.TableHelper;

public class ContactsTable {

	public static enum GenderEnum {
		MALE, FEMALE;
	}

	// Database table
	public static final String TABLE_NAME = "contacts";
	public static final String COLUMN_FK_ADDRESS_ID = "fk_address_id";
	public static final String COLUMN_FK_TEAM_ID = "fk_team_id";
	public static final String COLUMN_FIRST_NAME = "first_name";
	public static final String COLUMN_MIDDLE_NAME = "middle_name";
	public static final String COLUMN_LAST_NAME = "last_name";
	public static final String COLUMN_GENDER = "gender";
	public static final String COLUMN_MOBILE = "mobile";
	public static final String COLUMN_EMAIL = "email";

	public static String[] TABLE_COLUMNS = TableHelper.createColumns(new String[] { COLUMN_FK_ADDRESS_ID, COLUMN_FK_TEAM_ID, COLUMN_FIRST_NAME,
			COLUMN_MIDDLE_NAME, COLUMN_LAST_NAME, COLUMN_GENDER, COLUMN_MOBILE, COLUMN_EMAIL });

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(TableHelper.createCommonColumnsQuery());
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_ADDRESS_ID).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_TEAM_ID).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FIRST_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_MIDDLE_NAME).append(" TEXT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_LAST_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_GENDER).append(" TEXT NOT NULL");
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

	public static ContentValues createContentValues(Long addressId, Contact contact) {
		ContentValues values = updateContentValues(contact);
		values.put(COLUMN_FK_ADDRESS_ID, addressId);
		values.put(COLUMN_FK_TEAM_ID, contact.getTeam().getId());
		values.put(COLUMN_GENDER, contact.getGender());
		return values;
	}

	public static ContentValues updateContentValues(Contact contact) {
		ContentValues values = TableHelper.defaultContentValues();
		values.put(COLUMN_FIRST_NAME, contact.getFirstName());
		values.put(COLUMN_MIDDLE_NAME, contact.getMiddleName());
		values.put(COLUMN_LAST_NAME, contact.getLastName());
		values.put(COLUMN_MOBILE, contact.getMobileNumber());
		values.put(COLUMN_EMAIL, contact.getEmailAddress());
		return values;
	}

}
