package com.gunnarro.android.bandy.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gunnarro.android.bandy.domain.party.Contact;
import com.gunnarro.android.bandy.domain.party.Player;
import com.gunnarro.android.bandy.domain.view.list.Item;

public class Team implements Serializable {

	private static final long serialVersionUID = -7342682849751732634L;

	private Integer id;
	private String name;
	private int teamYearOfBirth;
	private String gender;
	private Club club;
	private League league;
	private List<Contact> conatctList;
	private List<Player> playerList;
	private Contact coach;
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

	public Team(String name, Club club, int teamYearOfBirth, String gender) {
		this(name);
		this.club = club;
		this.teamYearOfBirth = teamYearOfBirth;
		this.gender = gender;
	}

	public Team(int id, String name, Club club, int teamYearOfBirth, String gender) {
		this(name, club, teamYearOfBirth, gender);
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTeamYearOfBirth(int teamYearOfBirth) {
		this.teamYearOfBirth = teamYearOfBirth;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Club getClub() {
		return club;
	}

	public Contact getCoach() {
		return coach;
	}

	public void setCoach(Contact coach) {
		this.coach = coach;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Integer getTeamYearOfBirth() {
		return teamYearOfBirth;
	}

	public String getGender() {
		return gender;
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

	public String[] getMobileNrForContacts() {
		List<String> mobileNumbers = new ArrayList<String>();
		for (Contact contact : conatctList) {
			mobileNumbers.add(contact.getMobileNumber());
		}
		return mobileNumbers.toArray(new String[mobileNumbers.size()]);
	}

	public String[] getEmailAddresseForContacts() {
		List<String> emailAddreses = new ArrayList<String>();
		for (Contact contact : conatctList) {
			emailAddreses.add(contact.getEmailAddress());
		}
		return emailAddreses.toArray(new String[emailAddreses.size()]);
	}

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}

	@Override
	public String toString() {
		return "Team [id=" + id + ", name=" + name + ", teamYearOfBirth=" + teamYearOfBirth + ", gender=" + gender + ", club=" + club + ", league=" + league
				+ ", playerList=" + playerList + ", coach=" + coach + ", teamLead=" + teamLead + "]";
	}

}
