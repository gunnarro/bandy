package com.gunnarro.android.bandy.repository.table;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.custom.CustomLog;

public class NotificationsTable {

	// Database table
	public static final String TABLE_NAME = "notifications";
	public static final String COLUMN_CREATED_DATE = "created_date";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_SENT_DATE = "sent_date";
	public static final String COLUMN_RECIPIENTS = "recipients";
	public static final String COLUMN_SUBJECT = "subject";
	public static final String COLUMN_MESSAGE = "message";
	public static final String COLUMN_STATUS = "status";

	public static String[] TABLE_COLUMNS = { COLUMN_CREATED_DATE, COLUMN_ID, COLUMN_SENT_DATE, COLUMN_RECIPIENTS, COLUMN_SUBJECT, COLUMN_MESSAGE, COLUMN_STATUS };

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(COLUMN_CREATED_DATE).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_SENT_DATE).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_RECIPIENTS).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_SUBJECT).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_MESSAGE).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_STATUS).append(" INTEGER);");
	}

	public static void onCreate(SQLiteDatabase database) {
		CustomLog.i(NotificationsTable.class, DATABASE_CREATE_QUERY.toString());
		database.execSQL(DATABASE_CREATE_QUERY.toString());
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		CustomLog.i(NotificationsTable.class, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
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

	public static ContentValues createContentValues(long createdDate, long sentDate, String recipients, String subject, String message, Integer status) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_CREATED_DATE, (int) (createdDate / 1000));
		values.put(COLUMN_SENT_DATE, (int) (sentDate / 1000));
		values.put(COLUMN_RECIPIENTS, recipients);
		values.put(COLUMN_SUBJECT, subject);
		values.put(COLUMN_MESSAGE, message);
		values.put(COLUMN_STATUS, status.toString());
		return values;
	}

}
