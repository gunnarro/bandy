package com.gunnarro.android.bandy.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.repository.table.ClubsTable;
import com.gunnarro.android.bandy.repository.table.NotificationsTable;
import com.gunnarro.android.bandy.repository.table.SettingsTable;
import com.gunnarro.android.bandy.repository.table.TeamsTable;
import com.gunnarro.android.bandy.repository.table.activity.CupsTable;
import com.gunnarro.android.bandy.repository.table.activity.LeaguesTable;
import com.gunnarro.android.bandy.repository.table.activity.MatchTypesTable;
import com.gunnarro.android.bandy.repository.table.activity.MatchesTable;
import com.gunnarro.android.bandy.repository.table.activity.PlayerPositionTypesTable;
import com.gunnarro.android.bandy.repository.table.activity.SeasonsTable;
import com.gunnarro.android.bandy.repository.table.activity.TrainingsTable;
import com.gunnarro.android.bandy.repository.table.link.CupMatchLnkTable;
import com.gunnarro.android.bandy.repository.table.link.LeagueMatchLnkTable;
import com.gunnarro.android.bandy.repository.table.link.LeagueTeamLnkTable;
import com.gunnarro.android.bandy.repository.table.link.PlayerContactLnkTable;
import com.gunnarro.android.bandy.repository.table.link.PlayerCupLnkTable;
import com.gunnarro.android.bandy.repository.table.link.PlayerMatchLnkTable;
import com.gunnarro.android.bandy.repository.table.link.PlayerTrainingLnkTable;
import com.gunnarro.android.bandy.repository.table.link.TeamContactLnkTable;
import com.gunnarro.android.bandy.repository.table.party.AddressTable;
import com.gunnarro.android.bandy.repository.table.party.ContactsTable;
import com.gunnarro.android.bandy.repository.table.party.PlayersTable;
import com.gunnarro.android.bandy.repository.table.party.RoleTypesTable;
import com.gunnarro.android.bandy.repository.table.party.RolesTable;
import com.gunnarro.android.bandy.repository.table.party.StatusesTable;
import com.gunnarro.android.bandy.repository.view.MatchResultView;
import com.gunnarro.android.bandy.service.impl.DataLoader;

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
	private static final boolean IS_LOAD_FROM_SCRIPT = false;
	private static final String DATABASE_CREATE = "sportsteamdb-create.sql";
	private static final String DATABASE_DROP = "sportsteamdb-drop.sql";
	private static final String DATABASE_NAME = "sportsteam3.db";
	public static final int DATABASE_VERSION = 1;

	public static final String QUERY_PRINT_ALL_CREATE_STATEMENT = "SELECT * FROM sqlite_master";

	private static BandyDataBaseHjelper instance = null;
	private String dbCreate;
	private String dbDrop;

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
		try {
			if (IS_LOAD_FROM_SCRIPT) {
				dbCreate = getQuery(context, DATABASE_CREATE);
				dbDrop = getQuery(context, DATABASE_DROP);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		if (IS_LOAD_FROM_SCRIPT) {
			executeMultipleQueries(database, dbCreate);
		} else {
			AddressTable.onCreate(database);
			ClubsTable.onCreate(database);
			ContactsTable.onCreate(database);
			CupsTable.onCreate(database);
			CupMatchLnkTable.onCreate(database);
			LeaguesTable.onCreate(database);
			LeagueMatchLnkTable.onCreate(database);
			LeagueTeamLnkTable.onCreate(database);
			MatchesTable.onCreate(database);
			MatchTypesTable.onCreate(database);
			PlayersTable.onCreate(database);
			PlayerContactLnkTable.onCreate(database);
			PlayerCupLnkTable.onCreate(database);
			PlayerMatchLnkTable.onCreate(database);
			PlayerPositionTypesTable.onCreate(database);
			PlayerTrainingLnkTable.onCreate(database);
			RolesTable.onCreate(database);
			RoleTypesTable.onCreate(database);
			SeasonsTable.onCreate(database);
			SettingsTable.onCreate(database);
			StatusesTable.onCreate(database);
			TeamsTable.onCreate(database);
			TeamContactLnkTable.onCreate(database);
			TrainingsTable.onCreate(database);
			NotificationsTable.onCreate(database);
			MatchResultView.onCreate(database);
		}
		// init data
		insertDefaultData(database);
		insertTestData(database);

		database.isDatabaseIntegrityOk();
		// database.execSQL("PRAGMA integrity_check;");
		CustomLog.i(this.getClass(), "created and initialized DB tables");
	}

	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		CustomLog.i(this.getClass(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		if (IS_LOAD_FROM_SCRIPT) {
			executeMultipleQueries(database, dbDrop);
			onCreate(database);
		} else {
			AddressTable.onUpgrade(database, oldVersion, newVersion);
			ClubsTable.onUpgrade(database, oldVersion, newVersion);
			ContactsTable.onUpgrade(database, oldVersion, newVersion);
			CupsTable.onUpgrade(database, oldVersion, newVersion);
			CupMatchLnkTable.onUpgrade(database, oldVersion, newVersion);
			LeaguesTable.onUpgrade(database, oldVersion, newVersion);
			LeagueMatchLnkTable.onUpgrade(database, oldVersion, newVersion);
			LeagueTeamLnkTable.onUpgrade(database, oldVersion, newVersion);
			MatchesTable.onUpgrade(database, oldVersion, newVersion);
			MatchTypesTable.onUpgrade(database, oldVersion, newVersion);
			PlayersTable.onUpgrade(database, oldVersion, newVersion);
			PlayerContactLnkTable.onUpgrade(database, oldVersion, newVersion);
			PlayerCupLnkTable.onUpgrade(database, oldVersion, newVersion);
			PlayerMatchLnkTable.onUpgrade(database, oldVersion, newVersion);
			PlayerPositionTypesTable.onUpgrade(database, oldVersion, newVersion);
			PlayerTrainingLnkTable.onUpgrade(database, oldVersion, newVersion);
			RolesTable.onUpgrade(database, oldVersion, newVersion);
			RoleTypesTable.onUpgrade(database, oldVersion, newVersion);
			SeasonsTable.onUpgrade(database, oldVersion, newVersion);
			SettingsTable.onUpgrade(database, oldVersion, newVersion);
			StatusesTable.onUpgrade(database, oldVersion, newVersion);
			TeamsTable.onUpgrade(database, oldVersion, newVersion);
			TeamContactLnkTable.onUpgrade(database, oldVersion, newVersion);
			TrainingsTable.onUpgrade(database, oldVersion, newVersion);
			NotificationsTable.onUpgrade(database, oldVersion, newVersion);
			MatchResultView.onUpgrade(database, oldVersion, newVersion);
		}
		CustomLog.i(this.getClass(), "upgraded DB tables");
	}

	public void insertDefaultData(SQLiteDatabase database) {
		// init settings
		database.execSQL("insert into settings (_id, created_date_time, key, value) values(1, datetime(), '" + SettingsTable.DATA_FILE_URL_KEY + "','"
				+ DataLoader.TEAM_XML_URL + "/uil.xml')");
		database.execSQL("insert into settings (_id, created_date_time, key, value) values(2, datetime(), '" + SettingsTable.DATA_FILE_URL_KEY + "','"
				+ DataLoader.TEAM_XML_URL + "/team-2003.xml')");
		database.execSQL("insert into settings (_id, created_date_time, key, value) values(3, datetime(), '" + SettingsTable.DATA_FILE_LAST_UPDATED_KEY
				+ "','0')");
		database.execSQL("insert into settings (_id, created_date_time, key, value) values(4, datetime(), '" + SettingsTable.DATA_FILE_VERSION_KEY
				+ "','na')");
		database.execSQL("insert into settings (_id, created_date_time, key, value) values(5, datetime(), '" + SettingsTable.MAIL_ACCOUNT_KEY + "','na')");
		database.execSQL("insert into settings (_id, created_date_time, key, value) values(6, datetime(), '" + SettingsTable.MAIL_ACCOUNT_PWD_KEY + "','na')");
		// init match types
		database.execSQL("insert into match_types (_id, created_date_time, match_type_id, match_type_name) values(1, datetime(), 1, 'LEAGUE')");
		database.execSQL("insert into match_types (_id, created_date_time, match_type_id, match_type_name) values(2, datetime(), 2, 'TRAINING')");
		database.execSQL("insert into match_types (_id, created_date_time, match_type_id, match_type_name) values(3, datetime(), 3, 'CUP')");
		database.execSQL("insert into match_types (_id, created_date_time, match_type_id, match_type_name) values(4, datetime(), 4, 'TOURNAMENT')");
		// init player position types
		database.execSQL("insert into position_types (_id, created_date_time, position_type_id, position_type_name) values(1, datetime(), 1, 'GOALKEEPER')");
		database.execSQL("insert into position_types (_id, created_date_time, position_type_id, position_type_name) values(2, datetime(), 2, 'DEFENDER')");
		database.execSQL("insert into position_types (_id, created_date_time, position_type_id, position_type_name) values(3, datetime(), 3, 'MIDFIELDER')");
		database.execSQL("insert into position_types (_id, created_date_time, position_type_id, position_type_name) values(4, datetime(), 4, 'FORWARD')");
		// init statuses
		database.execSQL("insert into statuses (_id, created_date_time, status_id, status_name) values(1, datetime(), 1, 'ACTIVE')");
		database.execSQL("insert into statuses (_id, created_date_time, status_id, status_name) values(2, datetime(), 2, 'PASSIVE')");
		database.execSQL("insert into statuses (_id, created_date_time, status_id, status_name) values(3, datetime(), 3, 'INJURED')");
		database.execSQL("insert into statuses (_id, created_date_time, status_id, status_name) values(4, datetime(), 4, 'QUIT')");
		// init seasons
		database.execSQL("insert into seasons (_id, created_date_time, period, start_date, end_date) values(1, datetime(), '2013/2014', 1, 1)");
		database.execSQL("insert into seasons (_id, created_date_time, period, start_date, end_date) values(2, datetime(), '2014/2015', 1, 1)");
		database.execSQL("insert into seasons (_id, created_date_time, period, start_date, end_date) values(3, datetime(), '2015/2016', 1, 1)");
		database.execSQL("insert into seasons (_id, created_date_time, period, start_date, end_date) values(4, datetime(), '2016/2017', 1, 1)");
		// init. team types
		database.execSQL("insert into leagues (_id, created_date_time, league_name, league_player_age_min, league_player_age_max, league_gender, league_match_period_time_minutes, league_match_extra_period_time_minutes, league_number_of_players, league_description) values(1, datetime(), 'Knøtt', '', '11', 'male', '25', '', '7', 'Spillerne må ikke ha fylt 11 år ved årsskiftet i inneværende sesong')");
		database.execSQL("insert into leagues (_id, created_date_time, league_name, league_player_age_min, league_player_age_max, league_gender, league_match_period_time_minutes, league_match_extra_period_time_minutes, league_number_of_players, league_description) values(2, datetime(), 'Lillegutt', '', '13', 'male', '25', '', '7', 'Spillerne må ikke ha fylt 13 år ved årsskiftet i inneværende sesong')");
		database.execSQL("insert into leagues (_id, created_date_time, league_name, league_player_age_min, league_player_age_max, league_gender, league_match_period_time_minutes, league_match_extra_period_time_minutes, league_number_of_players, league_description) values(3, datetime(), 'Smågutt', '', '15', 'male', '30', '5', '11', 'Spillerne må ikke ha fylt 15 år ved årsskiftet i inneværende sesong')");
		database.execSQL("insert into leagues (_id, created_date_time, league_name, league_player_age_min, league_player_age_max, league_gender, league_match_period_time_minutes, league_match_extra_period_time_minutes, league_number_of_players, league_description) values(4, datetime(), 'Gutt', '', '17', 'male', '40', '10', '11', 'Spillerne må ikke ha fylt 17 år ved årsskiftet i inneværende sesong')");
		database.execSQL("insert into leagues (_id, created_date_time, league_name, league_player_age_min, league_player_age_max, league_gender, league_match_period_time_minutes, league_match_extra_period_time_minutes, league_number_of_players, league_description) values(5, datetime(), 'Junior', '', '20', 'male', '45', '10', '11', 'Spillerne må ikke ha fylt 20 år ved årsskiftet i inneværende sesong')");
		database.execSQL("insert into leagues (_id, created_date_time, league_name, league_player_age_min, league_player_age_max, league_gender, league_match_period_time_minutes, league_match_extra_period_time_minutes, league_number_of_players, league_description) values(6, datetime(), 'Old boys', '35', '50', 'male', '30','5',  '11', 'info')");
		database.execSQL("insert into leagues (_id, created_date_time, league_name, league_player_age_min, league_player_age_max, league_gender, league_match_period_time_minutes, league_match_extra_period_time_minutes, league_number_of_players, league_description) values(7, datetime(),'Veteran', '50', '', 'male', '30', '5', '7', 'info')");
		// init role types
		database.execSQL("insert into role_types (_id, created_date_time, role_type_name, role_type_description) values(1, datetime(), 'DEFAULT', '')");
		database.execSQL("insert into role_types (_id, created_date_time, role_type_name, role_type_description) values(2, datetime(), 'PARENT', '')");
		database.execSQL("insert into role_types (_id, created_date_time, role_type_name, role_type_description) values(3, datetime(), 'TEAMLEAD', '')");
		database.execSQL("insert into role_types (_id, created_date_time, role_type_name, role_type_description) values(4, datetime(), 'COACH', '')");
		database.execSQL("insert into role_types (_id, created_date_time, role_type_name, role_type_description) values(5, datetime(), 'CHAIRMAN', '')");
		database.execSQL("insert into role_types (_id, created_date_time, role_type_name, role_type_description) values(6, datetime(), 'DEPUTY_	CHAIRMAN', '')");
		database.execSQL("insert into role_types (_id, created_date_time, role_type_name, role_type_description) values(7, datetime(), 'BOARD_MEMBER', '')");

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

	private void executeMultipleQueries(SQLiteDatabase database, String queries) {
		for (String query : queries.split(";")) {
			if (query != null && !query.isEmpty()) {
				String q = query.trim().replace("\n", "").replace("\t", "") + ";";
				CustomLog.d(this.getClass(), q);
				database.execSQL(q);
			}
		}
	}

	private String getQuery(Context context, String file) throws IOException {
		InputStream fStream = context.getAssets().open(file);
		StringBuilder sbuilder = new StringBuilder();
		BufferedReader input = new BufferedReader(new InputStreamReader(fStream, "UTF-8"));
		try {
			String str = input.readLine();
			while (str != null) {
				sbuilder.append(str);
				str = input.readLine();
			}
		} finally {
			input.close();
			fStream.close();
		}
		return sbuilder.toString();
	}
}
