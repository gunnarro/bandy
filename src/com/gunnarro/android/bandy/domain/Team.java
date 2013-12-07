package com.gunnarro.android.bandy.domain;

import java.util.Date;
import java.util.List;

public class Team {
	private Integer id = -1;
	private String version;
	private Date releaseDate;
	private String name;
	private Coach coach;
	private Club club;
	private List<Player> players;
	private List<Match> matches;
	private List<Cup> cups;
	private List<Traning> tranings;

	public Team(String name) {
		this.name = name;
	}

	public Team(int id, String name) {
		this(name);
		this.id = id;
	}

	public Team(String name, String version) {
		this(name);
		this.version = version;
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

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public List<Match> getMatches() {
		return matches;
	}

	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}

	public List<Cup> getCups() {
		return cups;
	}

	public void setCups(List<Cup> cups) {
		this.cups = cups;
	}

	public List<Traning> getTranings() {
		return tranings;
	}

	public void setTranings(List<Traning> tranings) {
		this.tranings = tranings;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
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
