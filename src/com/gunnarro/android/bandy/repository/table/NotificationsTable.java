package com.gunnarro.android.bandy.repository.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class NotificationsTable {

	// Database table
	public static final String TABLE_NAME = "notifications";
	public static final String COLUMN_SENT_DATE = "sent_date";
	public static final String COLUMN_RECIPIENTS = "recipients";
	public static final String COLUMN_SUBJECT = "subject";
	public static final String COLUMN_MESSAGE = "message";
	public static final String COLUMN_STATUS = "status";

	public static String[] TABLE_COLUMNS = TableHelper.createColumns(new String[] { COLUMN_SENT_DATE, COLUMN_RECIPIENTS, COLUMN_SUBJECT, COLUMN_MESSAGE,
			COLUMN_STATUS });

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(TableHelper.createCommonColumnsQuery());
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_SENT_DATE).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_RECIPIENTS).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_SUBJECT).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_MESSAGE).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_STATUS).append(" INTEGER);");
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

	public static ContentValues createContentValues(long createdDate, long sentDate, String recipients, String subject, String message, Integer status) {
		ContentValues values = TableHelper.createContentValues();
		values.put(COLUMN_SENT_DATE, (int) (sentDate / 1000));
		values.put(COLUMN_RECIPIENTS, recipients);
		values.put(COLUMN_SUBJECT, subject);
		values.put(COLUMN_MESSAGE, message);
		values.put(COLUMN_STATUS, status.toString());
		return values;
	}

}
