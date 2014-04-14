package com.gunnarro.android.bandy.repository.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class SettingsTable {

	public static String DATA_FILE_URL_KEY = "data_file_url";
	public static String DATA_FILE_VERSION_KEY = "data_file_version";
	public static String DATA_FILE_RELEASE_DATE_KEY = "data_file_release_date";
	public static String DATA_FILE_LAST_UPDATED_KEY = "data_file_last_updated";
	public static String MAIL_ACCOUNT_KEY = "mail_account";
	public static String MAIL_ACCOUNT_PWD_KEY = "mail_account_pwd";

	// Database table
	public static final String TABLE_NAME = "settings";
	public static final String COLUMN_KEY = "key";
	public static final String COLUMN_VALUE = "value";

	public static String[] TABLE_COLUMNS = TableHelper.createColumns(new String[] { COLUMN_KEY, COLUMN_VALUE });

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(TableHelper.createCommonColumnsQuery());
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_KEY).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_VALUE).append(" TEXT NOT NULL);");
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

	public static ContentValues createContentValues(String key, String value) {
		ContentValues values = TableHelper.defaultContentValues();
		values.put(COLUMN_KEY, key);
		values.put(COLUMN_VALUE, value);
		return values;
	}

}
