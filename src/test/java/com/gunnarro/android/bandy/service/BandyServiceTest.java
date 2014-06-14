package com.gunnarro.android.bandy.service;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.TestConstants;
import com.gunnarro.android.bandy.repository.impl.BandyRepositoryImpl;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;

@RunWith(RobolectricTestRunner.class)
public class BandyServiceTest {

	private BandyService bandyService;

	@Before
	public void setUp() throws Exception {
		File dbFile = new File(TestConstants.DB_PATH);
		if (!dbFile.exists()) {
			throw new RuntimeException("Sqlite DB file not found! " + TestConstants.DB_PATH);
		}
		String dbPath = dbFile.getAbsolutePath();
		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
		bandyService = new BandyServiceImpl(Robolectric.application, new BandyRepositoryImpl(db));
	}

	@Test
	public void loadData() {
		bandyService.loadData("uil/club.xml");
	}
}
