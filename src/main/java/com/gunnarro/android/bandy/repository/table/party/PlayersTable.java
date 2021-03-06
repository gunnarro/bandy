package com.gunnarro.android.bandy.repository.table.party;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.domain.party.Player;
import com.gunnarro.android.bandy.repository.table.TableHelper;

public class PlayersTable {

	// Database table
	public static final String TABLE_NAME = "players";
	public static final String COLUMN_FK_TEAM_ID = "fk_team_id";
	public static final String COLUMN_FK_ADDRESS_ID = "fk_address_id";
	public static final String COLUMN_STATUS = "status";
	public static final String COLUMN_FIRST_NAME = "first_name";
	public static final String COLUMN_MIDDLE_NAME = "middle_name";
	public static final String COLUMN_LAST_NAME = "last_name";
	public static final String COLUMN_GENDER = "gender";
	public static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_MOBILE = "mobile";
	public static final String COLUMN_COUNTRY_OF_BIRTH = "country_of_birth";
	public static final String COLUMN_NATIONAL_TEAM = "national_team";
	public static final String COLUMN_SCHOOL_NAME = "school_name";
	public static final String COLUMN_JERSEY_NUMBER = " jersey_number";

	public static String[] TABLE_COLUMNS = TableHelper.createColumns(new String[] { COLUMN_FK_TEAM_ID, COLUMN_FK_ADDRESS_ID, COLUMN_STATUS, COLUMN_FIRST_NAME,
			COLUMN_MIDDLE_NAME, COLUMN_LAST_NAME, COLUMN_GENDER, COLUMN_DATE_OF_BIRTH, COLUMN_EMAIL, COLUMN_MOBILE, COLUMN_JERSEY_NUMBER, COLUMN_SCHOOL_NAME });

	// Database creation SQL statement
	private static final StringBuffer DATABASE_CREATE_QUERY;
	static {
		DATABASE_CREATE_QUERY = new StringBuffer();
		DATABASE_CREATE_QUERY.append("create table ");
		DATABASE_CREATE_QUERY.append(TABLE_NAME);
		DATABASE_CREATE_QUERY.append("(").append(TableHelper.createCommonColumnsQuery());
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_TEAM_ID).append(" INTEGER NOT NULL ON CONFLICT FAIL REFERENCES teams(_id) ON DELETE CASCADE ON UPDATE CASCADE");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FK_ADDRESS_ID).append(" INTEGER REFERENCES addresses(_id) ON DELETE SET NULL ON UPDATE CASCADE");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_STATUS).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_FIRST_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_MIDDLE_NAME).append(" TEXT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_LAST_NAME).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_GENDER).append(" TEXT NOT NULL");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_DATE_OF_BIRTH).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_EMAIL).append(" TEXT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_MOBILE).append(" TEXT");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_JERSEY_NUMBER).append(" INTEGER");
		DATABASE_CREATE_QUERY.append(",").append(COLUMN_SCHOOL_NAME).append(" TEXT");
		DATABASE_CREATE_QUERY.append(",").append("UNIQUE (").append(COLUMN_FIRST_NAME).append(",").append(COLUMN_LAST_NAME).append(") ON CONFLICT FAIL);");
//		DATABASE_CREATE_QUERY.append(", FOREIGN KEY(").append(COLUMN_FK_TEAM_ID).append(") REFERENCES ").append(TeamsTable.TABLE_NAME).append("(")
//				.append(TableHelper.COLUMN_ID).append("));");
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

	public static ContentValues createContentValues(Long addressId, Player player) {
		ContentValues values = updateContentValues(player);
		if (addressId != null) {
			values.put(COLUMN_FK_ADDRESS_ID, addressId);
		}
		values.put(COLUMN_FK_TEAM_ID, player.getTeam().getId());
		values.put(COLUMN_GENDER, player.getGender());
		return values;
	}

	public static ContentValues updateContentValues(Player player) {
		ContentValues values = TableHelper.defaultContentValues();
		values.put(COLUMN_STATUS, player.getStatus().name());
		values.put(COLUMN_FIRST_NAME, player.getFirstName());
		values.put(COLUMN_MIDDLE_NAME, player.getMiddleName());
		values.put(COLUMN_LAST_NAME, player.getLastName());
		values.put(COLUMN_EMAIL, player.getEmailAddress());
		values.put(COLUMN_MOBILE, player.getMobileNumber());
		values.put(COLUMN_DATE_OF_BIRTH, (int) (player.getDateOfBirth() / 1000));
		return values;
	}

}
