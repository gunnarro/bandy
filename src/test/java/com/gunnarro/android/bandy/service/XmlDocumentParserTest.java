package com.gunnarro.android.bandy.service;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Season;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.service.impl.XmlDocumentParser;

/**
 * mvn clean -Dtest=XmlDocumentParserTest test -P local
 * 
 * @author admin
 * 
 */
@RunWith(RobolectricTestRunner.class)
public class XmlDocumentParserTest {

	@Mock
	BandyServiceImpl bandyServiceMock;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		setupBandyServiceMock();
	}

	private void setupBandyServiceMock() {
		// Mockito.when(bandyServiceMock.createClub(null)).thenReturn(1);
		// Mockito.when(bandyServiceMock.createTeam(null)).thenReturn(1);
		// Mockito.when(bandyServiceMock.createPlayer(null)).thenReturn(1);
		// Mockito.when(bandyServiceMock.createContact(null)).thenReturn(1);
		// Mockito.when(bandyServiceMock.createMatch(null)).thenReturn(1);
		// Mockito.when(bandyServiceMock.createCup(null)).thenReturn(1);
		// Mockito.when(bandyServiceMock.createTraining(null)).thenReturn(1);
		// Mockito.when(bandyServiceMock.createSeason(null)).thenReturn(1);
		// Mockito.when(bandyServiceMock.createAddress(null)).thenReturn(1L);
		Mockito.when(bandyServiceMock.updateDataFileVersion("1.1-SNAPSHOT")).thenReturn(true);
		Mockito.when(bandyServiceMock.getClub("Ullevål Idretts Lag", "Bandy")).thenReturn(new Club(1, "Ullevål Idretts Lag", "Bandy"));
		Mockito.when(bandyServiceMock.getClub("Sagene Idrettsforening", "Fotball")).thenReturn(new Club(1, "Sagene Idrettsforening", "Fotball"));
		Mockito.when(bandyServiceMock.getTeam(999)).thenReturn(null);
		Mockito.when(bandyServiceMock.getTeam(1, "Knøtt 2002", false)).thenReturn(new Team(1, "Knøtt 2002"));
		Mockito.when(bandyServiceMock.getTeam(1, "Knøtt 2003", false)).thenReturn(new Team(1, "Knøtt 2003"));
		Mockito.when(bandyServiceMock.getTeam(1, "Jenter 2002", false)).thenReturn(new Team(1, "Jenter 2002"));
		Mockito.when(bandyServiceMock.getSeason("2013/2014")).thenReturn(null);
		Mockito.when(bandyServiceMock.getSeason(0)).thenReturn(new Season(1, "2013/2014", 0, 0));

	}

	@Test
	public void loadDataUIL() throws Exception {
		XmlDocumentParser parser = new XmlDocumentParser();
		boolean downloadAndUpdateDB = parser.downloadAndUpdateDB(Robolectric.application, "uil/club.xml", bandyServiceMock);
		assertTrue(downloadAndUpdateDB);
	}

	@Test
	public void loadDataSIF() throws Exception {
		XmlDocumentParser parser = new XmlDocumentParser();
		boolean downloadAndUpdateDB = parser.downloadAndUpdateDB(Robolectric.application, "sif/club.xml", bandyServiceMock);
		assertTrue(downloadAndUpdateDB);
	}
}
