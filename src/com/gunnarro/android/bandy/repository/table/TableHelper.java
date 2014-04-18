package com.gunnarro.android.bandy.repository.table;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.custom.CustomLog;

public class TableHelper {

	// Common database table columns
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_CREATED_DATETIME = "created_date_time";
	public static final String COLUMN_LAST_MODIFIED_DATETIME = "last_modified_date_time";

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

	public static String[] createColumns(String[] columns) {
		ArrayList<String> columnNames = new ArrayList<String>();
		columnNames.add(COLUMN_ID);
		columnNames.add(COLUMN_CREATED_DATETIME);
		columnNames.add(COLUMN_LAST_MODIFIED_DATETIME);
		for (String name : columns) {
			columnNames.add(name);
		}
		return columnNames.toArray(new String[columnNames.size()]);
	}

	public static String createCommonColumnsQuery() {
		StringBuffer query = new StringBuffer();
		query.append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT");
		query.append(",").append(COLUMN_CREATED_DATETIME).append(" DATETIME DEFAULT CURRENT_TIMESTAMP");
		query.append(",").append(COLUMN_LAST_MODIFIED_DATETIME).append(" DATETIME DEFAULT CURRENT_TIMESTAMP");
		return query.toString();
	}

	public static void onCreate(SQLiteDatabase database, String query) {
		CustomLog.i(TableHelper.class, query);
		database.execSQL(query);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion, String tableName, String query) {
		CustomLog.i(TableHelper.class, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + tableName);
		onCreate(database, query);
	}

	public static void checkColumnNames(String[] projection, String[] tableColumns) {
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(tableColumns));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}

	public static ContentValues defaultContentValues() {
		ContentValues values = new ContentValues();
		values.put(COLUMN_LAST_MODIFIED_DATETIME, getCurrentDateTime());
		return values;
	}

	private static String getCurrentDateTime() {
		return dateFormat.format(new Date());
	}
}
