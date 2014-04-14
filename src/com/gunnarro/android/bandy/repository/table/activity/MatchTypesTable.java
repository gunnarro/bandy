package com.gunnarro.android.bandy.repository.table.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.repository.table.TableHelper;

public class MatchTypesTable {

	// Database table
	public static final String TABLE_NAME = "match_types";
	public static final String COLUMN_MATCH_TYPE_ID = "match_type_id";
	public static final String COLUMN_MATCH_TYPE_NAME = "match_type_name";

	public static String[] TABLE_COLUMNS = TableHelper.createColumns(new String[] { COLUMN_MATCH_TYPE_ID, COLUMN_MATCH_TYPE_NAME });

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(TableHelper.createCommonColumnsQuery());
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_MATCH_TYPE_ID).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_MATCH_TYPE_NAME).append(" TEXT NOT NULL);");
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

	public static ContentValues createContentValues(int matchTypeId, String matchTypeName) {
		ContentValues values = TableHelper.defaultContentValues();
		values.put(COLUMN_MATCH_TYPE_ID, matchTypeId);
		values.put(COLUMN_MATCH_TYPE_NAME, matchTypeName);
		return values;
	}

}
