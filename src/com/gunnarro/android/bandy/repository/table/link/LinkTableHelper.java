package com.gunnarro.android.bandy.repository.table.link;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.repository.table.TableHelper;

public class LinkTableHelper {

	public static void onCreate(SQLiteDatabase database, String tableName, String fkColumnNameId1, String fkColumnNameId2, String fkTableName1,
			String fkTableName2) {
		database.execSQL(databaseCreateQuery(tableName, fkColumnNameId1, fkColumnNameId2, fkTableName1, fkTableName2));
	}

	public static void onUpgrade(SQLiteDatabase database, String tableName, String fkColumnNameId1, String fkColumnNameId2, String fkTableName1,
			String fkTableName2, int oldVersion, int newVersion) {
		CustomLog.i(LinkTableHelper.class, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + tableName);
		onCreate(database, tableName, fkColumnNameId1, fkColumnNameId2, fkTableName1, fkTableName2);
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

	public static ContentValues createContentValues(String[] tableColumns, Integer[] ids) {
		ContentValues values = TableHelper.defaultContentValues();
		values.put(tableColumns[0], ids[0]);
		values.put(tableColumns[1], ids[1]);
		return values;
	}

	/**
	 * Use of ON DELETE CASCADE ensures that row(s) is also deleted from the
	 * link table if id is deleted from the parent table
	 */
	private static String databaseCreateQuery(String tableName, String fkColumnNameId1, String fkColumnNameId2, String fkTableName1, String fkTableName2) {
		StringBuffer databaseCreateQuery = new StringBuffer();
		databaseCreateQuery.append("CREATE TABLE ");
		databaseCreateQuery.append(tableName);
		databaseCreateQuery.append("(").append(TableHelper.createCommonColumnsQuery());
		databaseCreateQuery.append(",").append(fkColumnNameId1).append(" INTEGER NOT NULL").append(" REFERENCES ").append(fkTableName1).append("(")
				.append(TableHelper.COLUMN_ID).append(") ON DELETE CASCADE");
		databaseCreateQuery.append(",").append(fkColumnNameId2).append(" INTEGER NOT NULL").append(" REFERENCES ").append(fkTableName2).append("(")
				.append(TableHelper.COLUMN_ID).append(") ON DELETE CASCADE");
		databaseCreateQuery.append(",").append("UNIQUE (").append(fkColumnNameId1).append(",").append(fkColumnNameId2).append(") ON CONFLICT ABORT");
		databaseCreateQuery.append(", FOREIGN KEY(").append(fkColumnNameId1).append(") REFERENCES ").append(fkTableName1).append("(")
				.append(TableHelper.COLUMN_ID).append(")");
		databaseCreateQuery.append(", FOREIGN KEY(").append(fkColumnNameId2).append(") REFERENCES ").append(fkTableName2).append("(")
				.append(TableHelper.COLUMN_ID).append("));");
		return databaseCreateQuery.toString();
	}
}
