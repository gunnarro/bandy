package com.gunnarro.android.bandy.repository.table.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class MatchTypesTable {

	// Database table
	public static final String TABLE_NAME = "match_types";

	public static void onCreate(SQLiteDatabase database) {
		TypesHelperTable.onCreate(database, TABLE_NAME);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		TypesHelperTable.onUpgrade(database, TABLE_NAME, oldVersion, newVersion);
	}

	public static void checkColumnNames(String[] projection) {
		TypesHelperTable.checkColumnNames(projection);
	}

	public static ContentValues createContentValues(String type, String description) {
		return TypesHelperTable.createContentValues(type, description);
	}

}
