package com.gunnarro.android.bandy.repository.table.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.repository.table.TableHelper;
import com.gunnarro.android.bandy.repository.table.link.LinkTableHelper;

public abstract class TypesHelperTable {

	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_DESCRIPTION = "description";

	public static String[] TABLE_COLUMNS = TableHelper.createColumns(new String[] { COLUMN_TYPE, COLUMN_DESCRIPTION });

	public static void onCreate(SQLiteDatabase database, String tableName) {
		database.execSQL(databaseCreateQuery(tableName));
	}

	public static void onUpgrade(SQLiteDatabase database, String tableName, int oldVersion, int newVersion) {
		CustomLog.i(LinkTableHelper.class, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + tableName);
		onCreate(database, tableName);
	}

	public static void checkColumnNames(String[] projection) {
		TableHelper.checkColumnNames(projection, TABLE_COLUMNS);
	}

	public static ContentValues createContentValues(String type, String description) {
		ContentValues values = TableHelper.defaultContentValues();
		values.put(COLUMN_TYPE, type);
		values.put(COLUMN_DESCRIPTION, description);
		return values;
	}

	/**
	 * Use of ON DELETE CASCADE ensures that row(s) is also deleted from the
	 * link table if id is deleted from the parent table
	 */
	private static String databaseCreateQuery(String tableName) {
		StringBuffer databaseCreateQuery = new StringBuffer();
		databaseCreateQuery.append("create table ");
		databaseCreateQuery.append(tableName);
		databaseCreateQuery.append("(").append(TableHelper.createCommonColumnsQuery());
		databaseCreateQuery.append(",").append(COLUMN_TYPE).append(" TEXT NOT NULL");
		databaseCreateQuery.append(",").append(COLUMN_DESCRIPTION).append(" TEXT);");
		return databaseCreateQuery.toString();
	}
}
