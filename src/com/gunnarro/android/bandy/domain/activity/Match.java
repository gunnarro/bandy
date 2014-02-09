package com.gunnarro.android.bandy.domain.activity;

import java.util.Date;

import com.gunnarro.android.bandy.domain.Referee;
import com.gunnarro.android.bandy.domain.Team;

public class Match {
	private Integer id;
	private long startTime;
	private Team team;
	private Team homeTeam;
	private Team awayTeam;
	private String venue;
	private Referee referee;
	private Integer numberOfGoalsHome;
	private Integer numberOfGoalsAway;

	public Match(long startTime, Team team, Team homeTeam, Team awayTeam, String venue, Referee referee) {
		this.startTime = startTime;
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

	public Long getStartTime() {
		return startTime;
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

	public boolean isPlayed() {
		return System.currentTimeMillis() > startTime;
	}

	public Integer getNumberOfGoalsHome() {
		return numberOfGoalsHome;
	}

	public Integer getNumberOfGoalsAway() {
		return numberOfGoalsAway;
	}

	public String getResult() {
		if (numberOfGoalsHome != null && numberOfGoalsAway != null) {
			return numberOfGoalsHome + " - " + numberOfGoalsAway;
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [id=").append(id);
		sb.append(", startDate=").append(new Date(startTime).toString());
		sb.append(", versus=").append(getTeamVersus());
		sb.append(", venue=").append(venue).append("]");
		return sb.toString();
	}
}
