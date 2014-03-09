package com.gunnarro.android.bandy.repository.table.link;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.repository.table.TableHelper;
import com.gunnarro.android.bandy.repository.table.activity.TrainingsTable;
import com.gunnarro.android.bandy.repository.table.party.PlayersTable;

public class PlayerTrainingLnkTable {

	// Database table
	public static final String TABLE_NAME = "player_training_lnk";
	public static final String COLUMN_FK_PLAYER_ID = "fk_player_id";
	public static final String COLUMN_FK_TRAINING_ID = "fk_training_id";

	public static void onCreate(SQLiteDatabase database) {
		LinkTableHelper.onCreate(database, TABLE_NAME, COLUMN_FK_PLAYER_ID, COLUMN_FK_TRAINING_ID, PlayersTable.TABLE_NAME, TrainingsTable.TABLE_NAME);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		LinkTableHelper.onUpgrade(database, TABLE_NAME, COLUMN_FK_PLAYER_ID, COLUMN_FK_TRAINING_ID, PlayersTable.TABLE_NAME, TrainingsTable.TABLE_NAME,
				oldVersion, newVersion);
	}

	public static String[] getTableColumns() {
		return TableHelper.createColumns(new String[] { COLUMN_FK_PLAYER_ID, COLUMN_FK_TRAINING_ID });
	}

	public static ContentValues createContentValues(Integer playerId, Integer trainingId) {
		return LinkTableHelper.createContentValues(new String[] { COLUMN_FK_PLAYER_ID, COLUMN_FK_TRAINING_ID }, new Integer[] { playerId, trainingId });
	}
}
