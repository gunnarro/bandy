package com.gunnarro.android.bandy.repository;

import java.util.Locale;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.repository.table.ClubsTable;
import com.gunnarro.android.bandy.repository.table.ContactsTable;
import com.gunnarro.android.bandy.repository.table.NotificationsTable;
import com.gunnarro.android.bandy.repository.table.PlayersTable;
import com.gunnarro.android.bandy.repository.table.RolesTable;
import com.gunnarro.android.bandy.repository.table.SettingsTable;
import com.gunnarro.android.bandy.repository.table.TeamsTable;
import com.gunnarro.android.bandy.repository.table.activity.CupsTable;
import com.gunnarro.android.bandy.repository.table.activity.MatchesTable;
import com.gunnarro.android.bandy.repository.table.activity.TrainingsTable;
import com.gunnarro.android.bandy.repository.table.link.PlayerContactLnkTable;
import com.gunnarro.android.bandy.repository.table.link.PlayerCupLnkTable;
import com.gunnarro.android.bandy.repository.table.link.PlayerMatchLnkTable;
import com.gunnarro.android.bandy.repository.table.link.PlayerTrainingLnkTable;

/**
 * Sqlite: PRAGMA table_info(table_name); PRAGMA integrity_check; PRAGMA
 * encoding; select * from sqlite_master; -- shows all create statements SELECT
 * * FROM sqlite_master -- shows all table schemas
 * http://www.coderzheaven.com/2011/04/18/sqlitemanager-plugin-for-eclipse/
 * 
 * @author admin
 * 
 */
public class BandyDataBaseHjelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "uilbandy3.db";
	private static final int DATABASE_VERSION = 2;

	public static final String QUERY_PRINT_ALL_CREATE_STATEMENT = "SELECT * FROM sqlite_master";

	private static BandyDataBaseHjelper instance = null;

	/**
	 * Declare your database helper as a static instance variable and use the
	 * Abstract Factory pattern to guarantee the singleton property. The static
	 * factory getInstance method ensures that only one BandyDataBaseHjelper
	 * will ever exist at any given time. If the mInstance object has not been
	 * initialized, one will be created. If one has already been created then it
	 * will simply be returned. You should not initialize your helper object
	 * using with new BandyDataBaseHjelper(context)!. Instead, always use
	 * BandyDataBaseHjelper.getInstance(context), as it guarantees that only one
	 * database helper will exist across the entire application's lifecycle.
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
		database.setLocale(new Locale("no", "NO"));
		if (!database.isReadOnly()) {
			// Enable foreign key constraints
			// database.setForeignKeyConstraintsEnabled(true);
			database.execSQL("PRAGMA foreign_keys=\"ON\";");
		}
		// Set encoding
		database.execSQL("PRAGMA encoding=\"UTF-8\";");
		ClubsTable.onCreate(database);
		ContactsTable.onCreate(database);
		CupsTable.onCreate(database);
		MatchesTable.onCreate(database);
		PlayersTable.onCreate(database);
		PlayerContactLnkTable.onCreate(database);
		PlayerCupLnkTable.onCreate(database);
		PlayerMatchLnkTable.onCreate(database);
		PlayerTrainingLnkTable.onCreate(database);
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
		PlayerContactLnkTable.onUpgrade(database, oldVersion, newVersion);
		PlayerCupLnkTable.onUpgrade(database, oldVersion, newVersion);
		PlayerMatchLnkTable.onUpgrade(database, oldVersion, newVersion);
		PlayerTrainingLnkTable.onUpgrade(database, oldVersion, newVersion);
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
		// "','12345678975', Ullevål 1', 'R�a 03', 'Bergbanen','dommer ukjent'");
		// database.execSQL("insert into matches (_id, start_date, start_time, home_team, away_team, veneu, referee) values(1,'"
		// + (System.currentTimeMillis() / 1000) +
		// "','12347457457', Ullevå1', 'Ready 2', 'Bergbanen','dommer ukjent'");
		// add signed player to a match
		// database.execSQL("insert into player_match_lnk (_id, fk_player_id, fk_match_id) values(1, 2, 4)");
	}
}
