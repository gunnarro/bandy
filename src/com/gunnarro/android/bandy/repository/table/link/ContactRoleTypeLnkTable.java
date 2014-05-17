package com.gunnarro.android.bandy.repository.table.link;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.repository.table.TableHelper;
import com.gunnarro.android.bandy.repository.table.party.ContactsTable;
import com.gunnarro.android.bandy.repository.table.party.RoleTypesTable;

public class ContactRoleTypeLnkTable {

	// Database table
	public static final String TABLE_NAME = "contact_role_type_lnk";
	public static final String COLUMN_FK_CONTACT_ID = "fk_contact_id";
	public static final String COLUMN_FK_ROLE_TYPE_ID = "fk_role_type_id";
	public static final String[] TABLE_COLUMNS = { COLUMN_FK_CONTACT_ID, COLUMN_FK_CONTACT_ID };

	public static void onCreate(SQLiteDatabase database) {
		LinkTableHelper.onCreate(database, TABLE_NAME, COLUMN_FK_CONTACT_ID, COLUMN_FK_ROLE_TYPE_ID, ContactsTable.TABLE_NAME, RoleTypesTable.TABLE_NAME);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		LinkTableHelper.onUpgrade(database, TABLE_NAME, COLUMN_FK_CONTACT_ID, COLUMN_FK_ROLE_TYPE_ID, ContactsTable.TABLE_NAME, RoleTypesTable.TABLE_NAME,
				oldVersion, newVersion);
	}

	public static String[] getIdTableColumns() {
		return new String[] { COLUMN_FK_CONTACT_ID, COLUMN_FK_ROLE_TYPE_ID };
	}

	public static String[] getTableColumns() {
		return TableHelper.createColumns(getIdTableColumns());
	}

	public static ContentValues createContentValues(Integer contactId, Integer roleTypeId) {
		return LinkTableHelper.createContentValues(new String[] { COLUMN_FK_CONTACT_ID, COLUMN_FK_ROLE_TYPE_ID }, new Integer[] { contactId, roleTypeId });
	}
}
