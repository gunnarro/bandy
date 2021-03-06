package com.gunnarro.android.bandy.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.TestConstants;
import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Cup;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.activity.MatchEvent;
import com.gunnarro.android.bandy.domain.activity.MatchEvent.MatchEventTypesEnum;
import com.gunnarro.android.bandy.domain.activity.Season;
import com.gunnarro.android.bandy.domain.activity.Status;
import com.gunnarro.android.bandy.domain.activity.Tournament;
import com.gunnarro.android.bandy.domain.activity.Training;
import com.gunnarro.android.bandy.domain.activity.Type.MatchTypesEnum;
import com.gunnarro.android.bandy.domain.party.Address;
import com.gunnarro.android.bandy.domain.party.Contact;
import com.gunnarro.android.bandy.domain.party.Player;
import com.gunnarro.android.bandy.domain.party.Player.PlayerStatusEnum;
import com.gunnarro.android.bandy.domain.party.Referee;
import com.gunnarro.android.bandy.domain.party.Role.RoleTypesEnum;
import com.gunnarro.android.bandy.domain.statistic.Statistic;
import com.gunnarro.android.bandy.domain.view.list.Item;
import com.gunnarro.android.bandy.repository.impl.BandyRepositoryImpl;
import com.gunnarro.android.bandy.repository.impl.BandyRepositoryImpl.PlayerLinkTableTypeEnum;
import com.gunnarro.android.bandy.repository.table.SettingsTable;
import com.gunnarro.android.bandy.service.exception.ApplicationException;
import com.gunnarro.android.bandy.service.impl.DataLoader;

@RunWith(RobolectricTestRunner.class)
public class BandyRepositoryTest {

	private BandyRepository bandyRepository;
	SQLiteDatabase db;

	@Before
	public void setUp() throws Exception {
		File dbFile = new File(TestConstants.DB_PATH);
		if (!dbFile.exists()) {
			throw new RuntimeException("Sqlite DB file not found! " + TestConstants.DB_PATH);
		}
		String dbPath = dbFile.getAbsolutePath();
		db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
		bandyRepository = new BandyRepositoryImpl(db);
		// Must be set for each new db connection!
		db.execSQL("PRAGMA foreign_keys=\"ON\";");
		// bandyRepository.setForeignKeyConstraintsEnabled(true);
		// Cleanup after each test case
	}

	@After
	public void tearDown() throws Exception {
		// Perform any necessary clean-up operations...
		bandyRepository.deleteAllTableData();
	}

	@Ignore
	@Test
	public void rawSQLqueryTest() {
		// bandyRepository.deleteClub(7);
		// bandyRepository.deletePlayer(1);
		for (String name : bandyRepository.getClubNames()) {
			System.out.println("CLUB: " + name);
		}

		for (String name : bandyRepository.getTeamNames("%")) {
			System.out.println("TEAM: " + name);
		}

		for (Player player : bandyRepository.getPlayerList(1)) {
			System.out.println("PLAYER: " + player.getId() + " " + player.getFullName());
		}

		for (Contact contact : bandyRepository.getContactList(1)) {
			System.out.println("PLAYER: " + contact.getId() + " " + contact.getFullName());
		}
	}

	@Test
	public void databaseSettings() throws Exception {
		assertThat(bandyRepository.getSqliteVersion(), equalTo("3.7.2"));
		assertThat(bandyRepository.getDBUserVersion(), equalTo("1"));
		assertThat(bandyRepository.getDBFileName(), equalTo("0"));
		assertThat(bandyRepository.getDBEncoding(), equalTo("UTF-8"));
		assertThat(bandyRepository.getDBforeignkeys(), equalTo("1"));
	}

	@Test
	public void verifySettings() {
		assertThat(DataLoader.TEAM_XML_URL + "/uil.xml", equalTo(bandyRepository.getSetting(SettingsTable.DATA_FILE_URL_KEY)));
		// assertThat("0.1",
		// equalTo(bandyRepository.getSetting(SettingsTable.DATA_FILE_VERSION_KEY)));
		// assertThat("0",
		// equalTo(bandyRepository.getSetting(SettingsTable.DATA_FILE_LAST_UPDATED_KEY)));
		assertThat("na", equalTo(bandyRepository.getSetting(SettingsTable.MAIL_ACCOUNT_KEY)));
		assertThat("na", equalTo(bandyRepository.getSetting(SettingsTable.MAIL_ACCOUNT_PWD_KEY)));
	}

	@Test
	public void checkInitializedData() {
		assertArrayEquals(new Item[] { new Item(0, "CUP"), new Item(0, "LEAGUE"), new Item(0, "TOURNAMENT"), new Item(0, "TRAINING") },
				bandyRepository.getMatchTypes());
		assertArrayEquals(new Item[] { new Item(0, "CANCELLED"), new Item(0, "NOT PLAYED"), new Item(0, "ONGOING"), new Item(0, "PLAYED"),
				new Item(0, "POSTPONED") }, bandyRepository.getMatchStatusList());
		assertArrayEquals(new Item[] { new Item(0, "BANDY"), new Item(0, "SOCCER") }, bandyRepository.getSportTypes());
		assertArrayEquals(new String[] { "Gutt", "Junior", "Knøtt", "Lillegutt", "Old boys", "Smågutt", "Veteran" }, bandyRepository.getLeagueNames());
		assertArrayEquals(new String[] { "ACTIVE", "INJURED", "PASSIVE", "QUIT" }, bandyRepository.getPlayerStatusTypes());
		assertArrayEquals(new String[] { "BOARD MEMBER", "CHAIRMAN", "COACH", "DEFAULT", "DEPUTY CHAIRMAN", "PARENT", "TEAMLEAD" },
				bandyRepository.getRoleTypeNames());
		assertArrayEquals(new String[] { "2013/2014", "2014/2015", "2015/2016", "2016/2017" }, bandyRepository.getSeasonPeriodes());
	}

	@Test
	public void verifyGettersNoMatch() {
		assertNull(bandyRepository.getAddress(1));
		assertNull(bandyRepository.getContact(1));
		assertNull(bandyRepository.getClub(1));
		assertNull(bandyRepository.getCup(1));
		assertNotNull(bandyRepository.getCurrentSeason());
		assertNull(bandyRepository.getMatch(1));
		assertNull(bandyRepository.getPlayer(1));
		assertNull(bandyRepository.getLeague("invalid"));
		assertNull(bandyRepository.getReferee(1));
		assertNull(bandyRepository.getTeam(1));
		assertNull(bandyRepository.getTraining(1));
		// assertNull(bandyRepository.getSetting("invalid"));
	}

	@Test
	public void verifyGettersNoMatchException() {
		try {
			bandyRepository.getSeason(0);
			assertTrue(false);
		} catch (ApplicationException ae) {
			assertEquals("No Season found! query: _id = ?, arg:0", ae.getMessage());
		}
	}

	@Test
	public void updateClub() {
		Long addressId = bandyRepository.createAddress(new Address("Stavangergt", "22", null, "9090", "oslo", "norge"));
		Address clubAddress = bandyRepository.getAddress(addressId.intValue());
		assertNotNull(clubAddress);
		int clubId = bandyRepository.createClub(new Club(null, "newSportsClub", "newBandy", "CK", "bandyStadium", clubAddress, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		assertEquals("newSportsClub", club.getName());
		club.setName("changedSportsClub");
		bandyRepository.updateClub(club);
		Club updatedClub = bandyRepository.getClub(clubId);
		assertEquals("changedSportsClub", updatedClub.getName());

		int deletedClubRows = bandyRepository.deleteClub(clubId);
		assertTrue(deletedClubRows == 1);
	}

	@Test
	public void updateAddress() {
		Long addressId = bandyRepository.createAddress(new Address("Stavangergt", "22", null, "9090", "oslo", "norge"));
		Address address = bandyRepository.getAddress(addressId.intValue());
		assertNotNull(address);
		assertEquals("Stavangergt 22", address.getFullStreetName());
		address.setStreetName("oslogata");
		address.setStreetNumber("34");
		address.setStreetNumberPrefix("a");
		bandyRepository.updateAddress(address);
		Address updatedAddress = bandyRepository.getAddress(addressId.intValue());
		assertEquals("Oslogata 34A", updatedAddress.getFullStreetName());
		int deletedAddressRows = bandyRepository.deleteAddress(addressId.intValue());
		assertTrue(deletedAddressRows == 1);
	}

	@Test
	public void updateMatchStatusInvalidId() {
		try {
			int updateMatchStatusRows = bandyRepository.updateMatchStatus(1, 1);
			assertTrue(updateMatchStatusRows == 0);
		} catch (Exception e) {
			assertEquals("sss", e.getMessage());
		}
	}

	@Test
	public void verifyGettersNoMatchReturnEmptyList() {
		assertEquals(0, bandyRepository.getClubAsItems().length);
		assertEquals(0, bandyRepository.getClubNames().length);
		assertEquals(0, bandyRepository.getClubList().size());
		assertEquals(0, bandyRepository.getContactList(1, RoleTypesEnum.DEFAULT.name()).size());
		assertEquals(0, bandyRepository.getContactList(1).size());
		assertEquals(0, bandyRepository.getContactsAsItemList(1).size());
		assertEquals(0, bandyRepository.getCupList(1, 2014).size());
		assertEquals(0, bandyRepository.getMatchEventList(0).size());
		assertEquals(0, bandyRepository.getMatchList(1, 2014).size());
		assertEquals(0, bandyRepository.getMatchPlayerList(1, 1).size());
		assertEquals(0, bandyRepository.getNumberOfSignedPlayers(PlayerLinkTableTypeEnum.MATCH, 1));
		assertEquals(0, bandyRepository.getPlayerList(1).size());
		assertEquals(0, bandyRepository.getPlayersAsItemList(1).size());
		assertEquals(0, bandyRepository.getRefereeNames().length);
		assertEquals(0, bandyRepository.getTeamAsItems(1).length);
		assertEquals(0, bandyRepository.getTeamList("unkown_club_name").size());
		assertEquals(0, bandyRepository.getTeamNames("unkown_club_name").length);
		assertEquals(0, bandyRepository.getTeamNames("unkown_club_name").length);
		assertEquals(0, bandyRepository.getTrainingList(1, 2014).size());
	}

	@Test
	public void newClubTeamPlayerAndReferee() {
		Long addressId = bandyRepository.createAddress(new Address("Stavangergt", "22", null, "9090", "oslo", "norge"));
		Address clubAddress = bandyRepository.getAddress(addressId.intValue());
		assertNotNull(clubAddress);
		int clubId = bandyRepository.createClub(new Club(null, "newSportsClub", "newBandy", "CK", "bandyStadium", clubAddress, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		assertNotNull(club);
		assertEquals("newSportsClub", club.getName());
		assertEquals("newBandy", club.getDepartmentName());
		assertEquals("CK", club.getClubNameAbbreviation());
		assertEquals("bandyStadium", club.getStadiumName());
		assertEquals("http://club.homepage.org", club.getHomePageUrl());
		assertEquals("Stavangergt 22", club.getAddress().getFullStreetName());
		assertEquals("9090", club.getAddress().getPostalCode());
		assertEquals("Oslo", club.getAddress().getCity());
		assertEquals("Norge", club.getAddress().getCountry());

		// Create team
		int teamId = bandyRepository.createTeam(new Team("newTeam", club, 2004, "Male"));
		Team team = bandyRepository.getTeam(teamId);
		assertNotNull(team);
		assertEquals("newTeam", team.getName());
		assertEquals("Male", team.getGender());
		assertEquals(2004, team.getTeamYearOfBirth().intValue());

		// Create player
		long dateOfBirth = System.currentTimeMillis();
		List<Contact> parents = new ArrayList<Contact>();
		Address playerAddress = new Address("streetname", "25", "c", "postalcode", "city", "country");
		int playerId = bandyRepository.createPlayer(new Player(team, "newPlayerFirstname", null, "newPlayerLastName", "M", PlayerStatusEnum.ACTIVE, parents,
				dateOfBirth, playerAddress));

		Player player = bandyRepository.getPlayer(playerId);
		assertNotNull(player);
		assertEquals("Newplayerfirstname Newplayerlastname", player.getFullName());
		assertEquals("M", player.getGender());
		assertFalse(player.hasParents());
		assertEquals(PlayerStatusEnum.ACTIVE, player.getStatus());
		assertEquals("City", player.getAddress().getCity());
		assertEquals("Country", player.getAddress().getCountry());
		assertEquals("Streetname 25C", player.getAddress().getFullStreetName());
		assertEquals("postalcode", player.getAddress().getPostalCode());
		assertTrue(player.getAddress().isAddressValid());

		// Create referee
		Referee newReferee = new Referee("refFirstname", "refMiddlename", "refLastname");
		newReferee.setClub(club);
		newReferee.setGender("M");
		newReferee.setMobileNumber("12345678");
		newReferee.setEmailAddress("r@mail.com");
		int refereeId = bandyRepository.createReferee(newReferee);
		Referee referee = bandyRepository.getReferee(refereeId);
		assertNotNull(referee);
		// Must cleanup, so the test case can be run multiple times
		// int deletedPlayerRows = bandyRepository.deletePlayer(playerId);
		// int deletedTeamRows = bandyRepository.deleteTeam(teamId);
		int deletedClubRows = bandyRepository.deleteClub(clubId);
		// assertTrue(deletedPlayerRows == 1);
		// assertTrue(deletedTeamRows == 1);
		assertTrue(deletedClubRows == 1);
		Team deletedTeam = bandyRepository.getTeam(teamId);
		assertNull(deletedTeam);
		Player deletedPlayer = bandyRepository.getPlayer(playerId);
		assertNull(deletedPlayer);
		referee = bandyRepository.getReferee(refereeId);
		assertNull(referee);
	}

	@Test
	public void deleteClub() {
		// Create new club with address
		Address address = new Address("clubstreetname", "45", "b", "0889", "oslo", "Norway");
		int clubId = bandyRepository.createClub(new Club(null, "newSportsClub", "newBandy", "CK", "bandyStadium", address, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		assertNotNull(club);
		int addressId = club.getAddress().getId();
		assertNotNull(addressId);

		// Check that when deleting address do not delete the club
		int deletedAddressRows = bandyRepository.deleteAddress(addressId);
		assertTrue(deletedAddressRows == 1);
		club = bandyRepository.getClub(clubId);
		assertNotNull(club);
		// Delete the club and check that the address is also deleted
		int deletedClubRows = bandyRepository.deleteClub(clubId);
		assertTrue(deletedClubRows == 1);
		club = bandyRepository.getClub(clubId);
		assertNull(club);
		// verify that club address is deleted
		Address addressDeleted = bandyRepository.getAddress(addressId);
		assertNull(addressDeleted);
	}

	@Test
	public void newClubWithServeralDepartments() {
		int clubIdDep1 = bandyRepository.createClub(new Club(null, "clubname", "department1", "CK", "bandyStadium", null, "http://club.homepage.org"));
		int clubIdDep2 = bandyRepository.createClub(new Club(null, "clubname", "department2", "CK", "bandyStadium", null, "http://club.homepage.org"));
		assertTrue(clubIdDep1 != clubIdDep2);
		// Clean up
		int deletedClubRows = bandyRepository.deleteClub(clubIdDep1);
		assertTrue(deletedClubRows == 1);

		deletedClubRows = bandyRepository.deleteClub(clubIdDep2);
		assertTrue(deletedClubRows == 1);
	}

	@Test
	public void newClubSeveralTeams() {
		int clubId = bandyRepository.createClub(new Club(null, "newSportsClub", "newBandy", "CK", "bandyStadium", null, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		int teamId1 = bandyRepository.createTeam(new Team("team1", club, 2004, "Male"));
		int teamId2 = bandyRepository.createTeam(new Team("team2", club, 2004, "Female"));
		assertTrue(teamId1 != teamId2);

		// Clean up
		int deletedClubRows = bandyRepository.deleteClub(clubId);
		assertTrue(deletedClubRows == 1);
		// Teams should also be deleted
		assertTrue(bandyRepository.getTeamAsItems(clubId).length == 0);
	}

	@Test
	public void newClubsSameDepartments() {
		int clubId1 = bandyRepository.createClub(new Club(null, "club1", "bandy", "CK", "bandyStadium", null, "http://club.homepage.org"));
		int clubId2 = bandyRepository.createClub(new Club(null, "club2", "bandy", "CK", "bandyStadium", null, "http://club.homepage.org"));
		assertTrue(clubId1 != clubId2);
		// Clean up
		int deletedClubRows = bandyRepository.deleteClub(clubId1);
		assertTrue(deletedClubRows == 1);
		deletedClubRows = bandyRepository.deleteClub(clubId2);
		assertTrue(deletedClubRows == 1);
	}

	@Test
	public void newClubDuplicate() {
		int clubId = bandyRepository.createClub(new Club(null, "clubname", "duplicate", "CK", "bandyStadium", null, "http://club.homepage.org"));
		try {
			bandyRepository.createClub(new Club(null, "clubname", "duplicate", "CK", "bandyStadium", null, "http://club.homepage.org"));
		} catch (ApplicationException ae) {
			assertEquals("[SQLITE_CONSTRAINT]  Abort due to constraint violation (columns club_name, club_department_name are not unique)", ae.getMessage());
		}
		// Clean up
		int deletedClubRows = bandyRepository.deleteClub(clubId);
		assertTrue(deletedClubRows == 1);
	}

	@Test
	public void newClubsSameTeamName() {
		int clubId1 = bandyRepository.createClub(new Club(null, "clubname1", "bandy", "CK1", "bandyStadium1", null, "http://club1.homepage.org"));
		assertTrue(clubId1 > 0);
		Club club1 = bandyRepository.getClub(clubId1);
		bandyRepository.createTeam(new Team("newTeam", club1, 2004, "Male"));
		int clubId2 = bandyRepository.createClub(new Club(null, "clubname2", "bandy", "CK2", "bandyStadium2", null, "http://club2.homepage.org"));
		assertTrue(clubId2 > 0);
		assertTrue(clubId1 != clubId2);
		Club club2 = bandyRepository.getClub(clubId2);
		bandyRepository.createTeam(new Team("newTeam", club2, 2004, "Male"));
		// Clean up
		int deletedClubRows = bandyRepository.deleteClub(clubId1);
		assertTrue(deletedClubRows == 1);
		deletedClubRows = bandyRepository.deleteClub(clubId2);
		assertTrue(deletedClubRows == 1);
	}

	@Test(expected = ApplicationException.class)
	public void newTeamInvalidClub() {
		try {
			bandyRepository.createTeam(new Team("newTeam", new Club("name", "department"), 2004, "Male"));
			assertTrue(false);
		} catch (ApplicationException ae) {
			assertEquals("Club must be set for creating new Team!", ae.getMessage());
			throw ae;
		}
	}

	@Test
	public void newTeamDuplicate() {
		int clubId = bandyRepository.createClub(new Club(null, "newSportsClub", "newBandy", "CK", "bandyStadium", null, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		bandyRepository.createTeam(new Team("newTeam", club, 2004, "Male"));
		try {
			bandyRepository.createTeam(new Team("newTeam", club, 2004, "Male"));
			assertTrue(false);
		} catch (ApplicationException ae) {
			assertEquals("[SQLITE_CONSTRAINT]  Abort due to constraint violation (columns fk_club_id, team_name are not unique)", ae.getMessage());
		}

		// Clean up
		int deletedClubRows = bandyRepository.deleteClub(clubId);
		assertTrue(deletedClubRows == 1);
	}

	@Test
	public void newAddress() {
		Address newAddress = new Address("streetname", "45", "b", "0889", "oslo", "Norway");
		long addressId = bandyRepository.createAddress(newAddress);
		Address address = bandyRepository.getAddress(Long.valueOf(addressId).intValue());
		assertTrue(address.isAddressValid());
		assertEquals("Streetname 45B", address.getFullStreetName());
		assertEquals("0889", address.getPostalCode());
		assertEquals("Oslo", address.getCity());
		assertEquals("Norway", address.getCountry());
		// Clean up
		int deletedAddressRows = bandyRepository.deleteAddress(Long.valueOf(addressId).intValue());
		assertTrue(deletedAddressRows == 1);
	}

	@Test
	public void newContact() {
		int clubId = bandyRepository.createClub(new Club(null, "club1", "football", "CK", "bandyStadium", null, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		int teamId = bandyRepository.createTeam(new Team("team1", club, 2003, "Male"));
		Team team = bandyRepository.getTeam(teamId);
		Address address = new Address("streetname", "25", "c", "postalcode", "city", "country");
		// Create contact 1
		int contactId1 = bandyRepository.createContact(new Contact(team, null, "firstName", "middleName", "lastName", "M", "11111111", "p1@email.no", address));
		Contact contact1 = bandyRepository.getContact(contactId1);
		assertNotNull(contact1);
		assertNotNull(contact1.getAddress());
		assertFalse(contact1.hasTeamRoles());
		assertEquals("Firstname Middlename Lastname", contact1.getFullName());
		assertEquals("M", contact1.getGender());
		assertEquals("Country", contact1.getAddress().getCountry());
		assertEquals("Streetname 25C", contact1.getAddress().getFullStreetName());
		assertEquals("postalcode", contact1.getAddress().getPostalCode());
		assertTrue(contact1.getAddress().isAddressValid());

		// update contact
		contact1.setEmailAddress("new@email.com");
		contact1.setMobileNumber("11223344");
		bandyRepository.updateContact(contact1);
		Contact updatedContact = bandyRepository.getContact(contactId1);
		assertEquals("new@email.com", updatedContact.getEmailAddress());
		assertEquals("11223344", updatedContact.getMobileNumber());

		// Must cleanup
		int deletedContactRows = bandyRepository.deleteContact(contactId1);
		assertTrue(deletedContactRows == 1);
		Address addressDeleted = bandyRepository.getAddress(contact1.getAddress().getId());
		assertNull(addressDeleted);
	}

	@Test
	public void newContactTwoNew() {
		int clubId = bandyRepository.createClub(new Club(null, "club1", "football", "CK", "bandyStadium", null, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		int teamId = bandyRepository.createTeam(new Team("team1", club, 2003, "Male"));
		Team team = bandyRepository.getTeam(teamId);
		Address address = new Address("streetname", "25", "c", "postalcode", "city", "country");
		// Create contact 1
		int contactId1 = bandyRepository
				.createContact(new Contact(team, null, "firstName1", "middleName", "lastName1", "M", "11111111", "p1@email.no", address));
		assertTrue(contactId1 > 0);
		// Create contact 2
		int contactId2 = bandyRepository
				.createContact(new Contact(team, null, "firstName2", "middleName", "lastName2", "M", "22334455", "p2@email.no", address));
		assertTrue(contactId2 > 0);
		assertTrue(contactId1 != contactId2);

		// Must cleanup
		int deletedContactRows = bandyRepository.deleteContact(contactId1);
		assertTrue(deletedContactRows == 1);
		deletedContactRows = bandyRepository.deleteContact(contactId2);
		assertTrue(deletedContactRows == 1);
		Address addressDeleted = bandyRepository.getAddress(address.getId());
		assertNull(addressDeleted);
		int deletedTeamRows = bandyRepository.deleteTeam(teamId);
		assertTrue(deletedTeamRows == 1);
		int deletedClubRows = bandyRepository.deleteClub(clubId);
		assertTrue(deletedClubRows == 1);
	}

	@Test
	public void newPlayer() {
		int clubId = bandyRepository.createClub(new Club(null, "newSportsClub", "newBandy", "CK", "bandyStadium", null, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		int teamId = bandyRepository.createTeam(new Team("team name", club, 2004, "Male"));
		Team team = bandyRepository.getTeam(teamId);
		Address address = new Address("streetname", "25", "c", "postalcode", "city", "country");
		int playerId = bandyRepository.createPlayer(new Player(team, "firstName", "middleName", "lastName", "M", PlayerStatusEnum.ACTIVE, null, System
				.currentTimeMillis(), address));

		Player player = bandyRepository.getPlayer(playerId);
		assertNotNull(player);
		assertEquals("Firstname Middlename Lastname", player.getFullName());
		assertEquals("M", player.getGender());
		assertEquals("Country", player.getAddress().getCountry());
		assertEquals("Streetname 25C", player.getAddress().getFullStreetName());
		assertEquals("postalcode", player.getAddress().getPostalCode());
		assertTrue(player.getAddress().isAddressValid());
		assertNull(player.getEmailAddress());
		assertNull(player.getMobileNumber());

		// Update player
		player.setEmailAddress("new@email.com");
		player.setMobileNumber("11223344");
		bandyRepository.updatePlayer(player);
		Player updatedPlayer = bandyRepository.getPlayer(playerId);
		assertEquals("new@email.com", updatedPlayer.getEmailAddress());
		assertEquals("11223344", updatedPlayer.getMobileNumber());

		// Must cleanup
		int deletedPlayerRows = bandyRepository.deletePlayer(playerId);
		assertTrue(deletedPlayerRows == 1);
		Address addressDeleted = bandyRepository.getAddress(player.getAddress().getId());
		assertNull(addressDeleted);
		int deletedTeamRows = bandyRepository.deleteTeam(teamId);
		assertTrue(deletedTeamRows == 1);
		int deletedClubRows = bandyRepository.deleteClub(clubId);
		assertTrue(deletedClubRows == 1);
	}

	@Test
	public void newPlayerTwoNew() {
		int clubId = bandyRepository.createClub(new Club(null, "newSportsClub", "newBandy", "CK", "bandyStadium", null, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		int teamId = bandyRepository.createTeam(new Team("team name", club, 2004, "Male"));
		Team team = bandyRepository.getTeam(teamId);
		Address address = new Address("streetname", "25", "c", "postalcode", "city", "country");
		int playerId1 = bandyRepository.createPlayer(new Player(team, "firstName1", "middleName", "lastName1", "M", PlayerStatusEnum.ACTIVE, null, System
				.currentTimeMillis(), address));
		assertTrue(playerId1 > 0);
		int playerId2 = bandyRepository.createPlayer(new Player(team, "firstName2", "middleName", "lastName2", "M", PlayerStatusEnum.ACTIVE, null, System
				.currentTimeMillis(), address));
		assertTrue(playerId2 > 0);
		assertTrue(playerId1 != playerId2);

		// Clean up
		int deletedPlayerRows = bandyRepository.deletePlayer(playerId1);
		assertTrue(deletedPlayerRows == 1);
		deletedPlayerRows = bandyRepository.deletePlayer(playerId2);
		assertTrue(deletedPlayerRows == 1);
		Address addressDeleted = bandyRepository.getAddress(address.getId());
		assertNull(addressDeleted);
		int deletedTeamRows = bandyRepository.deleteTeam(teamId);
		assertTrue(deletedTeamRows == 1);
		int deletedClubRows = bandyRepository.deleteClub(clubId);
		assertTrue(deletedClubRows == 1);
	}

	@Test
	public void newReferee() {
		Address address = new Address("streetname", "25", "c", "postalcode", "city", "country");
		Referee newReferee = new Referee("firstName", "middleName", "lastName");
		newReferee.setAddress(address);
		newReferee.setMobileNumber("+4712345678");
		newReferee.setEmailAddress("new.referee@mail.com");
		newReferee.setGender("M");
		int refereeId = bandyRepository.createReferee(newReferee);
		Referee referee = bandyRepository.getReferee(refereeId);
		assertNotNull(referee);
		assertEquals("Firstname Middlename Lastname", referee.getFullName());
		assertEquals("M", referee.getGender());
		assertEquals("+4712345678", referee.getMobileNumber());
		assertEquals("new.referee@mail.com", referee.getEmailAddress());
		assertTrue(referee.getAddress().isAddressValid());
		assertNotNull(referee.getAddress().getId());
		assertEquals("Country", referee.getAddress().getCountry());
		assertEquals("Streetname 25C", referee.getAddress().getFullStreetName());
		assertEquals("postalcode", referee.getAddress().getPostalCode());

		// update referee
		referee.setMobileNumber("77665544");
		referee.setEmailAddress("new@mail.org");
		bandyRepository.updateReferee(referee);
		Referee updatedReferee = bandyRepository.getReferee(refereeId);
		assertEquals("77665544", updatedReferee.getMobileNumber());
		assertEquals("new@mail.org", updatedReferee.getEmailAddress());

		// Must cleanup
		int addressId = referee.getAddress().getId();
		int deletedRefereeRows = bandyRepository.deleteReferee(refereeId);
		assertTrue(deletedRefereeRows == 1);
		Address addressDeleted = bandyRepository.getAddress(addressId);
		assertNull(addressDeleted);
	}

	@Test
	public void newRefereeTwoNew() {
		Address address = new Address("streetname", "25", "c", "postalcode", "city", "country");
		Referee newReferee1 = new Referee("firstName1", "middleName", "lastName1");
		newReferee1.setAddress(address);
		newReferee1.setMobileNumber("+4712345678");
		newReferee1.setEmailAddress("new.referee@mail.com");
		newReferee1.setGender("M");
		int refereeId1 = bandyRepository.createReferee(newReferee1);
		assertTrue(refereeId1 > 0);

		Referee newReferee2 = new Referee("firstName2", "middleName", "lastName2");
		newReferee2.setAddress(address);
		newReferee2.setMobileNumber("+4712345678");
		newReferee2.setEmailAddress("new.referee@mail.com");
		newReferee2.setGender("M");
		int refereeId2 = bandyRepository.createReferee(newReferee2);
		assertTrue(refereeId2 > 0);
		assertTrue(refereeId2 != refereeId1);

		// Must cleanup
		int deletedRefereeRows = bandyRepository.deleteReferee(refereeId1);
		assertTrue(deletedRefereeRows == 1);
		deletedRefereeRows = bandyRepository.deleteReferee(refereeId2);
		assertTrue(deletedRefereeRows == 1);
		Address addressDeleted = bandyRepository.getAddress(address.getId());
		assertNull(addressDeleted);
	}

	@Test
	public void newTrainingTwoNew() {
		Season season = bandyRepository.getSeason("2014/2015");
		assertNotNull(season.getId());
		int clubId = bandyRepository.createClub(new Club(null, "newSportsClub", "newBandy", "CK", "bandyStadium", null, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		int teamId = bandyRepository.createTeam(new Team("team name", club, 2004, "Male"));
		Team team = bandyRepository.getTeam(teamId);
		long startTime = System.currentTimeMillis();
		// new training 1
		Training newTraining1 = new Training(season, startTime, System.currentTimeMillis(), team, "place");
		int newTrainingId1 = bandyRepository.createTraining(newTraining1);
		// new training 2
		Training newTraining2 = new Training(season, startTime + TestConstants.DAY_MS, System.currentTimeMillis(), team, "place");
		int newTrainingId2 = bandyRepository.createTraining(newTraining2);
		assertTrue(newTrainingId2 > 0);
		assertTrue(newTrainingId2 != newTrainingId1);

		Training training = bandyRepository.getTrainingByDate(team.getId(), startTime);
		assertEquals("2014/2015", training.getSeason().getPeriod());
		assertEquals("place", training.getVenue());
		// assertEquals(startTime, training.getStartTime());
		assertEquals("team name", training.getTeam().getName());
		assertEquals(0, training.getNumberOfParticipatedPlayers().intValue());

		int playerId = bandyRepository.createPlayer(new Player(team, "firstName", "middleName", "lastName", "M", PlayerStatusEnum.ACTIVE, null, System
				.currentTimeMillis(), null));

		Player player = bandyRepository.getPlayer(playerId);
		assertNotNull(player);
		// register player for training
		this.bandyRepository.createPlayerLink(PlayerLinkTableTypeEnum.TRAINING, playerId, newTrainingId1);
		training = bandyRepository.getTraining(newTrainingId1);
		assertEquals(1, training.getNumberOfParticipatedPlayers().intValue());

		// unregister player for training
		this.bandyRepository.deletePlayerLink(PlayerLinkTableTypeEnum.TRAINING, playerId, newTrainingId1);
		training = bandyRepository.getTraining(newTrainingId1);
		assertEquals(0, training.getNumberOfParticipatedPlayers().intValue());

		// Reasign player
		this.bandyRepository.createPlayerLink(PlayerLinkTableTypeEnum.TRAINING, playerId, newTrainingId1);
		assertEquals(1, bandyRepository.getNumberOfSignedPlayers(PlayerLinkTableTypeEnum.TRAINING, newTrainingId1));

		// delete training
		int deletedTrainingRows = bandyRepository.deleteTraining(newTrainingId1);
		assertTrue(deletedTrainingRows == 1);

		// check that team and player are not deleted upon training delete
		assertNotNull(bandyRepository.getTeam(teamId));
		assertNotNull(bandyRepository.getPlayer(playerId));
		assertEquals(0, bandyRepository.getNumberOfSignedPlayers(PlayerLinkTableTypeEnum.TRAINING, newTrainingId1));

		// Clean up after test case
		int deleteClubRows = bandyRepository.deleteClub(clubId);
		assertTrue(deleteClubRows == 1);
		// int deletedTeamRows = bandyRepository.deleteTeam(teamId);
		// assertTrue(deletedTeamRows == 1);
		// int deletedPlayerRows = bandyRepository.deletePlayer(playerId);
		// assertTrue(deletedPlayerRows == 1);
	}

	@Test
	public void newMatchTwoNew() {
		int clubId = bandyRepository.createClub(new Club(null, "club1", "football", "CK", "bandyStadium", null, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		int teamId = bandyRepository.createTeam(new Team("team1", club, 2003, "Male"));
		Team team = bandyRepository.getTeam(teamId);
		Season season = bandyRepository.getSeason("2013/2014");
		// Match 1
		Match newMatch1 = new Match(season, System.currentTimeMillis() - TestConstants.DAY_MS, team, new Team("homeTeam"), new Team("awayTeam"), "home-venue",
				new Referee("referee-firstname", null, "referee-lastname"));
		int matchId1 = bandyRepository.createMatch(newMatch1);

		// Match 2
		Match newMatch2 = new Match(season, System.currentTimeMillis() + TestConstants.DAY_MS, team, new Team("awayTeam"), new Team("homeTeam"), "away-venue",
				new Referee("referee-firstname", null, "referee-lastname"));
		int matchId2 = bandyRepository.createMatch(newMatch2);
		assertTrue(matchId2 > 0);
		assertTrue(matchId1 != matchId2);

		Match match = bandyRepository.getMatch(matchId1);
		// assertEquals("teamName", match.getTeam().getName());
		assertEquals("homeTeam - awayTeam", match.getTeamVersus());
		assertEquals("NOT PLAYED", match.getMatchStatus().getName());
		assertEquals(MatchTypesEnum.LEAGUE, match.getMatchType());
		assertEquals("0 - 0", match.getResult());
		assertNull(match.getReferee());
		assertEquals(0, match.getNumberOfSignedPlayers().intValue());
		assertEquals(0, match.getNumberOfGoalsHome().intValue());
		assertEquals(0, match.getNumberOfGoalsAway().intValue());
		assertEquals("0 - 0", match.getResult());
		assertFalse(match.isPlayed());
		assertEquals(0, match.getNumberOfSignedPlayers().intValue());

		// Registrer referee
		Referee referee = new Referee(null, "rfirstName", "rmiddleName", "rlastName", "M");
		referee.setMobileNumber("+4711223344");
		referee.setEmailAddress("referee@mail.com");
		int refereeId = bandyRepository.createReferee(referee);
		bandyRepository.registrerRefereeForMatch(refereeId, matchId1);
		match = bandyRepository.getMatch(matchId1);
		assertEquals("Rfirstname Rmiddlename Rlastname", match.getReferee().getFullName());

		// Registrer player
		int playerId = bandyRepository.createPlayer(new Player(team, "pfirstName", "pmiddleName", "plastName", "M", PlayerStatusEnum.ACTIVE, null, System
				.currentTimeMillis(), null));
		bandyRepository.createPlayerLink(PlayerLinkTableTypeEnum.MATCH, playerId, matchId1);
		match = bandyRepository.getMatch(matchId1);
		assertEquals(1, match.getNumberOfSignedPlayers().intValue());

		// Unregistrer player
		int deletePlayerLinkRows = bandyRepository.deletePlayerLink(PlayerLinkTableTypeEnum.MATCH, playerId, matchId1);
		assertEquals(1, deletePlayerLinkRows);
		match = bandyRepository.getMatch(matchId1);
		assertEquals(0, match.getNumberOfSignedPlayers().intValue());

		// Then recreate player link in order to verify that it is removed upon
		// match delete at end of this test
		bandyRepository.createPlayerLink(PlayerLinkTableTypeEnum.MATCH, playerId, matchId1);

		// Match update score
		bandyRepository.updateGoals(matchId1, 2, true);
		bandyRepository.createMatchEvent(new MatchEvent(matchId1, 23, "newTeam", "player hometeam", MatchEventTypesEnum.GOAL_HOME.toString(), "2"));
		bandyRepository.updateGoals(matchId1, 3, false);
		bandyRepository.createMatchEvent(new MatchEvent(matchId1, 43, "newTeam", "player awayteam", MatchEventTypesEnum.GOAL_AWAY.toString(), "3"));

		match = bandyRepository.getMatch(matchId1);
		assertEquals("2 - 3", match.getResult());

		List<MatchEvent> matchEventList = bandyRepository.getMatchEventList(matchId1);
		assertEquals(2, matchEventList.size());
		assertEquals(matchId1, matchEventList.get(0).getMatchId());
		assertEquals("newTeam", matchEventList.get(0).getTeamName());
		assertEquals("player hometeam", matchEventList.get(0).getPlayerName());
		assertEquals(MatchEventTypesEnum.GOAL_HOME.name(), matchEventList.get(0).getEventTypeName());
		assertEquals(23, matchEventList.get(0).getPlayedMinutes().intValue());
		assertEquals("2", matchEventList.get(0).getValue());

		assertEquals(matchId1, matchEventList.get(1).getMatchId());
		assertEquals("newTeam", matchEventList.get(1).getTeamName());
		assertEquals("player awayteam", matchEventList.get(1).getPlayerName());
		assertEquals(MatchEventTypesEnum.GOAL_AWAY.name(), matchEventList.get(1).getEventTypeName());
		assertEquals(43, matchEventList.get(1).getPlayedMinutes().intValue());
		assertEquals("3", matchEventList.get(1).getValue());

		Status matchStatus = bandyRepository.getMatchStatus(2);
		int updateDMatchStatusRows = bandyRepository.updateMatchStatus(matchId1, matchStatus.getId());
		assertTrue(updateDMatchStatusRows == 1);
		match = bandyRepository.getMatch(matchId1);
		assertEquals("PLAYED", match.getStatus().getName());

		// Clean up
		// int deletedMatchEventsRows =
		// bandyRepository.deleteMatchEvents(matchId);
		// assertTrue(deletedMatchEventsRows == 2);
		int deletedMatchRows = bandyRepository.deleteMatch(matchId1);
		// Should delete, match, match events, and player link
		assertEquals(1, deletedMatchRows);
		deletedMatchRows = bandyRepository.deleteMatch(matchId2);
		assertEquals(1, deletedMatchRows);

		// Check that team and player not are deleted upon match delete
		assertNotNull(bandyRepository.getTeam(teamId));
		assertNotNull(bandyRepository.getPlayer(playerId));

		// int deletedPlayerRows = bandyRepository.deletePlayer(playerId);
		// assertTrue(deletedPlayerRows == 1);
		// int deletedRefereeRows = bandyRepository.deleteReferee(refereeId);
		// assertTrue(deletedRefereeRows == 1);
		// int deletedTeamRows = bandyRepository.deleteTeam(teamId);
		// assertTrue(deletedTeamRows == 1);
		// Everything will be deleted upon club delete
		int deletedClubRows = bandyRepository.deleteClub(clubId);
		assertTrue(deletedClubRows == 1);
	}

	@Test
	public void newCup() {
		Season season = bandyRepository.getSeason("2014/2015");
		Cup newCup = new Cup(season, System.currentTimeMillis(), "Cup Name", "arranging club name", "cup-venue", System.currentTimeMillis());
		int cupId = bandyRepository.createCup(newCup);
		Cup cup = bandyRepository.getCup(cupId);
		assertEquals("Cup Name", cup.getCupName());
		assertEquals("arranging club name", cup.getClubName());
		assertEquals("cup-venue", cup.getVenue());
		assertEquals(0, cup.getNumberOfParticipatedPlayers().intValue());

		int clubId = bandyRepository.createClub(new Club(null, "newSportsClub", "newBandy", "CK", "bandyStadium", null, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		int teamId = bandyRepository.createTeam(new Team("team name", club, 2004, "Male"));
		Team team = bandyRepository.getTeam(teamId);
		Match cupMatch = Match.createCupMatch();
		cupMatch.setSeason(season);
		cupMatch.setStartTime(System.currentTimeMillis());
		cupMatch.setTeam(team);
		cupMatch.setHomeTeam(new Team("home-team"));
		cupMatch.setAwayTeam(new Team("away-team"));
		cupMatch.setVenue("venue");
		int matchId = bandyRepository.createMatch(cupMatch);
		int createCupMatchLnk = bandyRepository.createCupMatchLnk(cupId, matchId);
		assertTrue(createCupMatchLnk > -1);
		int deletedCupRows = bandyRepository.deleteCup(cupId);
		assertTrue(deletedCupRows == 1);
	}

	@Test
	public void newTournament() {
		Season season = bandyRepository.getSeason("2014/2015");
		assertNotNull(season.getId());
		Tournament newTournament = new Tournament(season, System.currentTimeMillis(), "Tournament Name", "Organizer club name", "place",
				System.currentTimeMillis());
		int tournamentId = bandyRepository.createTournament(newTournament);
		Tournament tournament = bandyRepository.getTournament(tournamentId);
		assertEquals("Tournament Name", tournament.getTournamentName());
		assertEquals("Organizer club name", tournament.getOrganizerName());
		assertEquals("place", tournament.getVenue());

		int clubId = bandyRepository.createClub(new Club(null, "newSportsClub", "newBandy", "CK", "bandyStadium", null, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		Integer teamId = bandyRepository.createTeam(new Team("team name", club, 2004, "Male"));
		assertTrue(teamId.intValue() > 0);

		List<Tournament> list = bandyRepository.getTournaments(teamId, 2014);
		assertEquals(0, list.size());
		bandyRepository.tournamentRegistration(tournamentId, teamId);
		List<Tournament> tournaments = bandyRepository.getTournaments(teamId, 2014);
		assertEquals(1, tournaments.size());
		assertEquals("Tournament Name", tournaments.get(0).getTournamentName());
		int tournamentUnRegistrationRows = bandyRepository.tournamentUnRegistration(tournamentId, teamId);
		assertEquals(1, tournamentUnRegistrationRows);
		tournaments = bandyRepository.getTournaments(teamId, 2014);
		assertEquals(0, tournaments.size());

		int deletedTournamentRows = bandyRepository.deleteTournament(tournamentId);
		assertTrue(deletedTournamentRows == 1);
	}

	@Test
	public void addToSeason() {
		Season season = bandyRepository.getSeason("2014/2015");
		int clubId = bandyRepository.createClub(new Club(null, "newSportsClub", "newBandy", "CK", "bandyStadium", null, "http://club.homepage.org"));
		Club club = bandyRepository.getClub(clubId);
		int teamId = bandyRepository.createTeam(new Team("team name", club, 2004, "Male"));
		Team team = bandyRepository.getTeam(teamId);
		Match newMatch = new Match(season, System.currentTimeMillis() - 60000, team, new Team("homeTeam"), new Team("awayTeam"), "venue", new Referee(
				"referee-firstname", null, "referee-lastname"));
		bandyRepository.createMatch(newMatch);
		Training newTraining = new Training(season, System.currentTimeMillis(), System.currentTimeMillis(), team, "place");
		bandyRepository.createTraining(newTraining);
		// FIXME
	}

	@Test
	public void teamStatistic() {
		Statistic teamStatistic = bandyRepository.getTeamStatistic(1, 1);
		assertNotNull(teamStatistic);
		assertEquals(0, teamStatistic.getMatchStatisticList().size());
		assertEquals(0, teamStatistic.getNumberOfTeamCups().intValue());
		assertEquals(0, teamStatistic.getNumberOfTeamMatches().intValue());
		assertEquals(0, teamStatistic.getNumberOfTeamTrainings().intValue());
	}

	@Test
	public void playerStatistic() {
		Statistic playerStatistic = bandyRepository.getPlayerStatistic(1, 1, 1);
		assertNotNull(playerStatistic);
		assertEquals(0, playerStatistic.getNumberOfPlayerCups().intValue());
		assertEquals(0, playerStatistic.getNumberOfPlayerMatches().intValue());
		assertEquals(0, playerStatistic.getNumberOfPlayerTrainings().intValue());
	}

	@Ignore
	@Test
	public void recreateDatabaseTabels() {
		try {
			// Use transaction, all or nothing
			// db.beginTransaction();
			// BandyDataBaseHjelper.dropAllTables(db);
			BandyDataBaseHjelper.createTables(db);
			BandyDataBaseHjelper.insertDefaultData(db);
			// db.isDatabaseIntegrityOk();
			// db.endTransaction();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
