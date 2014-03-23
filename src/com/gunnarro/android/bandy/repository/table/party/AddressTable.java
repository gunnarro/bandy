package com.gunnarro.android.bandy.repository.table.party;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.repository.table.TableHelper;

public class AddressTable {

	// Database table
	public static final String TABLE_NAME = "addresses";
	public static final String COLUMN_STREET_NAME = "street_name";
	public static final String COLUMN_STREET_NUMBER = "street_number";
	public static final String COLUMN_STREET_NUMBER_POSTFIX = "street_number_postfix";
	public static final String COLUMN_ZIP_CODE = "zip_code";
	public static final String COLUMN_CITY = "city";
	public static final String COLUMN_POST_CODE = "post_code";
	public static final String COLUMN_POST_BOX = "post_box";
	public static final String COLUMN_COUNTRY = "country";

	public static String[] TABLE_COLUMNS = TableHelper.createColumns(new String[] { COLUMN_STREET_NAME, COLUMN_STREET_NUMBER, COLUMN_STREET_NUMBER_POSTFIX,
			COLUMN_ZIP_CODE, COLUMN_CITY, COLUMN_POST_CODE, COLUMN_POST_BOX, COLUMN_COUNTRY });

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(TableHelper.createCommonColumnsQuery());
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_STREET_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_STREET_NUMBER).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_STREET_NUMBER_POSTFIX).append(" TEXT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_ZIP_CODE).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_CITY).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_POST_CODE).append(" TEXT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_POST_BOX).append(" TEXT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_COUNTRY).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append("UNIQUE (").append(COLUMN_STREET_NAME).append(",").append(COLUMN_STREET_NUMBER).append(",")
				.append(COLUMN_STREET_NUMBER_POSTFIX).append(",").append(COLUMN_ZIP_CODE).append(") ON CONFLICT ABORT);");
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

	public static ContentValues createContentValues(String streetName, String streetNumber, String streetNumberPostfix, String zipCode, String city,
			String country) {
		ContentValues values = TableHelper.createContentValues();
		values.put(COLUMN_STREET_NAME, streetName);
		values.put(COLUMN_STREET_NUMBER, streetNumber);
		values.put(COLUMN_STREET_NUMBER_POSTFIX, streetNumberPostfix);
		values.put(COLUMN_ZIP_CODE, zipCode);
		values.put(COLUMN_CITY, city);
		values.put(COLUMN_COUNTRY, country);
		return values;
	}

}
