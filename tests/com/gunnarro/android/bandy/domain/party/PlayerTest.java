package com.gunnarro.android.bandy.domain.party;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.party.Contact.ContactRoleEnum;
import com.gunnarro.android.bandy.domain.party.Player.PlayerStatusEnum;

public class PlayerTest extends TestCase {

	// @Test
	public void testConstructor() {
		Address address = new Address("streetname", "25", "c", "postalcode", "city", "country");
		List<Contact> parents = new ArrayList<Contact>();
		List<ContactRoleEnum> p1roles = new ArrayList<ContactRoleEnum>();
		p1roles.add(ContactRoleEnum.COACH);
		List<ContactRoleEnum> p2roles = new ArrayList<ContactRoleEnum>();
		p2roles.add(ContactRoleEnum.TEAMLEAD);
		parents.add(new Contact(new Team("team name"), p1roles, "p1firstname", "p1middleName", "p1lastName", "M", "11111111", "p1@email.no", address));
		parents.add(new Contact(new Team("team name"), p2roles, "p2firstname", "p2middleName", "p2lastName", "M", "22222222", "p2@email.no", address));
		long dateOfBirth = System.currentTimeMillis();
		Player player = new Player(909, new Team("team name"), "firstname", "middlename", "lastname", "M", PlayerStatusEnum.ACTIVE, parents, dateOfBirth,
				address);
		assertEquals("909", player.getId().toString());
		assertEquals("firstname lastname", player.getFullName());
		assertEquals("M", player.getGender());
		assertTrue(player.hasParents());
		assertEquals("COACH", player.getParents().get(0).getRoles().get(0).name());
		assertEquals("p1firstname p1lastName", player.getParents().get(0).getFullName());
		assertEquals("p1@email.no", player.getParents().get(0).getEmailAddress());
		assertEquals("11111111", player.getParents().get(0).getMobileNumber());
		assertEquals(PlayerStatusEnum.ACTIVE, player.getStatus());
		assertEquals("firstname lastname", player.getFullName());
		assertEquals("city", player.getAddress().getCity());
		assertEquals("country", player.getAddress().getCountry());
		assertEquals("streetname 25c", player.getAddress().getFullStreetName());
		assertEquals("postalcode", player.getAddress().getPostalCode());
		assertTrue(player.getAddress().isAddressValid());

		player = new Player(new Team("team name"), "firstname", "middlename", "lastname", "M", PlayerStatusEnum.ACTIVE, parents, dateOfBirth, address);
		assertNull(player.getId());
	}

}
