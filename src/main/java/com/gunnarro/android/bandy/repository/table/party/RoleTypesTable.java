package com.gunnarro.android.bandy.repository.table.party;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.repository.table.activity.TypesHelperTable;

public class RoleTypesTable {

	// Database table
	public static final String TABLE_NAME = "role_types";

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
