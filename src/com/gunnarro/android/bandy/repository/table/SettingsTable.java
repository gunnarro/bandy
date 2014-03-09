package com.gunnarro.android.bandy.repository.table;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.custom.CustomLog;

public class SettingsTable {

	public static String DATA_FILE_URL_KEY = "data_file_url";
	public static final String COLUMN_CREATED_DATE = "created_date";
	public static String DATA_FILE_VERSION_KEY = "data_file_version";
	public static String DATA_FILE_RELEASE_DATE_KEY = "data_file_release_date";
	public static String DATA_FILE_LAST_UPDATED_KEY = "data_file_last_updated";
	public static String MAIL_ACCOUNT_KEY = "mail_account";
	public static String MAIL_ACCOUNT_PWD_KEY = "mail_account_pwd";

	// Database table
	public static final String TABLE_NAME = "settings";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_KEY = "key";
	public static final String COLUMN_VALUE = "value";

	public static String[] TABLE_COLUMNS = { COLUMN_CREATED_DATE, COLUMN_ID, COLUMN_KEY, COLUMN_VALUE };

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(COLUMN_CREATED_DATE).append(" INTEGER NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_KEY).append(" TEXT NOT NULL UNIQUE");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_VALUE).append(" TEXT NOT NULL);");
	}

	public static void onCreate(SQLiteDatabase database) {
		CustomLog.i(SettingsTable.class, DATABASE_CREATE_QUERY.toString());
		database.execSQL(DATABASE_CREATE_QUERY.toString());
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		CustomLog.i(SettingsTable.class, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(database);
	}

	public static void checkColumnNames(String[] projection) {
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(TABLE_COLUMNS));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}

	public static ContentValues createContentValues(String key, String value) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_CREATED_DATE, System.currentTimeMillis());
		values.put(COLUMN_KEY, key);
		values.put(COLUMN_VALUE, value);
		return values;
	}

}
