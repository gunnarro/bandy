package com.gunnarro.android.bandy.repository.table.party;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.repository.table.TableHelper;

public class StatusesTable {

	// Database table
	public static final String TABLE_NAME = "statuses";
	public static final String COLUMN_STATUS_ID = "status_id";
	public static final String COLUMN_STATUS_NAME = "status_name";

	public static String[] TABLE_COLUMNS = TableHelper.createColumns(new String[] { COLUMN_STATUS_ID, COLUMN_STATUS_NAME });

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(TableHelper.createCommonColumnsQuery());
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_STATUS_ID).append(" INTEGER NOT NULL UNIQUE");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_STATUS_NAME).append(" TEXT NOT NULL);");
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

	public static ContentValues createContentValues(String statusName, Integer statusId) {
		ContentValues values = TableHelper.createContentValues();
		values.put(COLUMN_STATUS_ID, statusId);
		values.put(COLUMN_STATUS_NAME, statusName);
		return values;
	}

}
