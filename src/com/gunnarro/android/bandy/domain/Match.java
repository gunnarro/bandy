package com.gunnarro.android.bandy.domain;

import java.util.Date;

public class Match {
	private Integer id;
	private long startDate;
	private Team team;
	private Team homeTeam;
	private Team awayTeam;
	private String venue;
	private Referee referee;

	public Match(long startDate, Team team, Team homeTeam, Team awayTeam, String venue, Referee referee) {
		this.startDate = startDate;
		this.team = team;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.venue = venue;
		this.referee = referee;
	}

	public Match(Integer id, long startDate, Team team, Team homeTeam, Team awayTeam, String venue, Referee referee) {
		this(startDate, team, homeTeam, awayTeam, venue, referee);
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public Team getTeam() {
		return team;
	}

	public long getStartDate() {
		return startDate;
	}

	public Team getHomeTeam() {
		return homeTeam;
	}

	public Team getAwayTeam() {
		return awayTeam;
	}

	public String getVenue() {
		return venue;
	}

	public Referee getReferee() {
		return referee;
	}

	public String getTeamVersus() {
		return this.homeTeam.getName() + " - " + this.awayTeam.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [id=").append(id);
		sb.append(", startDate=").append(new Date(startDate).toString());
		sb.append(", versus=").append(getTeamVersus());
		sb.append(", venue=").append(venue).append("]");
		return sb.toString();
	}
}
