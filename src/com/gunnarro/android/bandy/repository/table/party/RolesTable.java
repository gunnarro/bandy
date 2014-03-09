package com.gunnarro.android.bandy.repository.table.party;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.repository.table.TableHelper;

public class RolesTable {

	// Database table
	public static final String TABLE_NAME = "roles";
	public static final String COLUMN_ROLE = "role";
	public static final String COLUMN_FK_CONTACT_ID = "fk_contact_id";

	public static String[] TABLE_COLUMNS = TableHelper.createColumns(new String[] { COLUMN_ROLE, COLUMN_FK_CONTACT_ID });

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(TableHelper.createCommonColumnsQuery());
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_ROLE).append(" TEXT NOT NULL UNIQUE");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_CONTACT_ID).append(" INTEGER NOT NULL);");
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

	public static ContentValues createContentValues(String role, Integer contactId) {
		ContentValues values = TableHelper.createContentValues();
		values.put(COLUMN_ROLE, role);
		values.put(COLUMN_FK_CONTACT_ID, contactId);
		return values;
	}

}
