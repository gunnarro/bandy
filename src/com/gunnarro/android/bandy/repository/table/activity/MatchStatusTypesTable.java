package com.gunnarro.android.bandy.repository.table.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.repository.table.TableHelper;

public class MatchStatusTypesTable {

	// Database table
	public static final String TABLE_NAME = "match_status_types";
	public static final String COLUMN_MATCH_STATUS_NAME = "match_status_name";
	public static final String COLUMN_MATCH_STATUS_DESCRIPTION = "match_status_description";

	public static String[] TABLE_COLUMNS = TableHelper.createColumns(new String[] { COLUMN_MATCH_STATUS_NAME, COLUMN_MATCH_STATUS_DESCRIPTION });

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(TableHelper.createCommonColumnsQuery());
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_MATCH_STATUS_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_MATCH_STATUS_DESCRIPTION).append(" TEXT);");
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

	public static ContentValues createContentValues(String statusName, String statusDescription) {
		ContentValues values = TableHelper.defaultContentValues();
		values.put(COLUMN_MATCH_STATUS_NAME, statusName);
		values.put(COLUMN_MATCH_STATUS_DESCRIPTION, statusDescription);
		return values;
	}

}
