package com.gunnarro.android.bandy.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.repository.table.ClubsTable;
import com.gunnarro.android.bandy.repository.table.ContactsTable;
import com.gunnarro.android.bandy.repository.table.CupsTable;
import com.gunnarro.android.bandy.repository.table.MatchesTable;
import com.gunnarro.android.bandy.repository.table.NotificationsTable;
import com.gunnarro.android.bandy.repository.table.PlayersTable;
import com.gunnarro.android.bandy.repository.table.RelationshipsTable;
import com.gunnarro.android.bandy.repository.table.RolesTable;
import com.gunnarro.android.bandy.repository.table.SettingsTable;
import com.gunnarro.android.bandy.repository.table.TeamsTable;
import com.gunnarro.android.bandy.repository.table.TrainingsTable;

public class BandyDataBaseHjelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "bandy.db";
	private static final int DATABASE_VERSION = 34;

	private static BandyDataBaseHjelper instance = null;

	/**
	 * Declare your database helper as a static instance variable and use the
	 * Abstract Factory pattern to guarantee the singleton property. The static
	 * factory getInstance method ensures that only one FilterDataBaseHjelper
	 * will ever exist at any given time. If the mInstance object has not been
	 * initialized, one will be created. If one has already been created then it
	 * will simply be returned. You should not initialize your helper object
	 * using with new FilterDataBaseHjelper(context)!. Instead, always use
	 * FilterDataBaseHjelper.getInstance(context), as it guarantees that only
	 * one database helper will exist across the entire application's lifecycle.
	 * {@link <a href=
	 * "http://www.androiddesignpatterns.com/2012/05/correctly-managing-your-
	 * sqlite-database.html
	 * ">http://www.androiddesignpatterns.com/2012/05/correctly-managing-your-sqlite-database.html</a>
	 */
	public static BandyDataBaseHjelper getInstance(Context ctx) {
		// Use the application context, which will ensure that you
		// don't accidentally leak an Activity's context.
		if (instance == null) {
			instance = new BandyDataBaseHjelper(ctx.getApplicationContext());
		}
		return instance;
	}

	private BandyDataBaseHjelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		if (!database.isReadOnly()) {
			// Enable foreign key constraints
			database.execSQL("PRAGMA foreign_keys=ON;");
		}
		ClubsTable.onCreate(database);
		ContactsTable.onCreate(database);
		CupsTable.onCreate(database);
		MatchesTable.onCreate(database);
		PlayersTable.onCreate(database);
		RelationshipsTable.onCreate(database);
		RolesTable.onCreate(database);
		SettingsTable.onCreate(database);
		TeamsTable.onCreate(database);
		TrainingsTable.onCreate(database);
		NotificationsTable.onCreate(database);
		// init data
		insertDefaultData(database);
		insertTestData(database);
		CustomLog.i(this.getClass(), "created and initialized DB tables");
	}

	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		ClubsTable.onUpgrade(database, oldVersion, newVersion);
		ContactsTable.onUpgrade(database, oldVersion, newVersion);
		CupsTable.onUpgrade(database, oldVersion, newVersion);
		MatchesTable.onUpgrade(database, oldVersion, newVersion);
		PlayersTable.onUpgrade(database, oldVersion, newVersion);
		RelationshipsTable.onUpgrade(database, oldVersion, newVersion);
		RolesTable.onUpgrade(database, oldVersion, newVersion);
		SettingsTable.onUpgrade(database, oldVersion, newVersion);
		TeamsTable.onUpgrade(database, oldVersion, newVersion);
		TrainingsTable.onUpgrade(database, oldVersion, newVersion);
		NotificationsTable.onUpgrade(database, oldVersion, newVersion);
		insertDefaultData(database);
		// for testing only
		insertTestData(database);
		CustomLog.i(this.getClass(), "upgraded DB tables");

	}

	public void insertDefaultData(SQLiteDatabase database) {
		// init settings
		database.execSQL("insert into settings (_id, key, value) values(1,'" + SettingsTable.DATA_FILE_LAST_UPDATED + "','0')");
		database.execSQL("insert into settings (_id, key, value) values(2,'" + SettingsTable.DATA_FILE_VERSION + "','na')");
		database.execSQL("insert into settings (_id, key, value) values(3,'" + SettingsTable.MAIL_ACCOUNT + "','na')");
		database.execSQL("insert into settings (_id, key, value) values(4,'" + SettingsTable.MAIL_ACCOUNT_PWD + "','na')");

		// Init. available filter types, which are all deactivated as default.
		// database.execSQL("insert into filters (_id, filter_name, activated) values(1,'"
		// + FilterTypeEnum.SMS_BLACK_LIST.name() + "','true')");
		CustomLog.i(this.getClass(), "inserted default test data");
	}

	/**
	 * For testing only
	 * 
	 * @param database
	 */
	private void insertTestData(SQLiteDatabase database) {
		// database.execSQL("insert into matches (_id, start_date, start_time, home_team, away_team, veneu, referee) values(1,'"
		// + (System.currentTimeMillis() / 1000) +
		// "','12345678975', Ullevål 1', 'Røa 03', 'Bergbanen','dommer ukjent'");
		// database.execSQL("insert into matches (_id, start_date, start_time, home_team, away_team, veneu, referee) values(1,'"
		// + (System.currentTimeMillis() / 1000) +
		// "','12347457457', Ullevål 1', 'Ready 2', 'Bergbanen','dommer ukjent'");
	}
}
