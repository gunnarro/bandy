package com.gunnarro.android.bandy.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gunnarro.android.bandy.domain.party.Coach;
import com.gunnarro.android.bandy.domain.party.Contact;
import com.gunnarro.android.bandy.domain.party.Player;
import com.gunnarro.android.bandy.domain.view.list.Item;

public class Team implements Serializable {

	private static final long serialVersionUID = -7342682849751732634L;

	private Integer id = -1;
	private String name;
	private Coach coach;
	private Club club;
	private List<Contact> conatctList;
	private List<Player> playerList;
	private Contact teamLead;

	public Team(String name) {
		this.name = name;
	}

	public Team(int id, String name) {
		this(name);
		this.id = id;
	}

	public Team(int id, String name, Club club) {
		this(id, name);
		this.club = club;
	}

	public Team(String name, Club club) {
		this(name);
		this.club = club;
	}

	public Club getClub() {
		return club;
	}

	public Coach getCoach() {
		return coach;
	}

	public void setCoach(Coach coach) {
		this.coach = coach;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Contact> getConatctList() {
		return conatctList;
	}

	public void setConatctList(List<Contact> conatctList) {
		this.conatctList = conatctList;
	}

	public List<Player> getPlayerList() {
		return playerList;
	}

	public List<Item> getPlayerItemList() {
		List<Item> playerItemList = new ArrayList<Item>();
		for (Player player : playerList) {
			playerItemList.add(new Item(player.getId(), player.getFullName(), false));
		}
		return playerItemList;
	}

	public void setPlayerList(List<Player> playerList) {
		this.playerList = playerList;
	}

	public Contact getTeamLead() {
		return teamLead;
	}

	public void setTeamLead(Contact teamLead) {
		this.teamLead = teamLead;
	}

	public List<String> getMobileNrForContacts() {
		List<String> mobileNumbers = new ArrayList<String>();
		for (Contact contact : conatctList) {
			mobileNumbers.add(contact.getMobileNumber());
		}
		return mobileNumbers;
	}

	public List<String> getEmailAddresseForContacts() {
		List<String> emailsNumbers = new ArrayList<String>();
		for (Contact contact : conatctList) {
			emailsNumbers.add(contact.getEmailAddress());
		}
		return emailsNumbers;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [id=").append(id);
		sb.append(", name=").append(name).append("]");
		return sb.toString();
	}
}
