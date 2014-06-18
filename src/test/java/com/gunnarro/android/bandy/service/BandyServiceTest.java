package com.gunnarro.android.bandy.service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.TestConstants;
import com.gunnarro.android.bandy.repository.impl.BandyRepositoryImpl;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import static org.junit.Assert.assertTrue;

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

	@After
	public void tearDown() throws Exception {
		// Perform any necessary clean-up operations...
		bandyService.cleanData();
	}

	@Ignore
	@Test
	public void clearAllTableData() {
		assertTrue(bandyService.cleanData());
	}

	// @Ignore
	@Test
	public void loadData() {
		boolean isLoaded = bandyService.loadData("uil/club.xml");
		assertTrue(isLoaded);

		assertEquals(1, bandyService.getClubList().size());
		assertEquals(2, bandyService.getTeamList("%").size());
//		assertEquals(1, bandyService.getPlayerList(1));
//		assertEquals(1, bandyService.getContactNames(1));
//		assertEquals(1, bandyService.getRefereeNames());
//		assertEquals(1, bandyService.getCupList(1, 2014));
//		assertEquals(1, bandyService.getTrainingList(1, 2014));
//		assertEquals(1, bandyService.getMatchList(1, 2014));
	}
}
