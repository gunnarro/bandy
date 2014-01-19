package com.gunnarro.android.bandy.domain.activity;

import java.util.Date;

import com.gunnarro.android.bandy.domain.Team;

public class Training {
	private Integer id;
	private long startDate;
	private long endTime;
	private Team team;
	private String venue;

	public Training(long startDate, long endTime, Team team, String venue) {
		this.startDate = startDate;
		this.endTime = endTime;
		this.endTime = endTime;
		this.team = team;
		this.venue = venue;
	}

	public Training(int id, long startDate, long endTime, Team team, String venue) {
		this(startDate, endTime, team, venue);
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public long getStartDate() {
		return startDate;
	}

	public long getEndTime() {
		return endTime;
	}

	public Team getTeam() {
		return team;
	}

	public String getVenue() {
		return venue;
	}

	public static Training createTraining(Team team) {
		return new Training(System.currentTimeMillis(), System.currentTimeMillis(), team, team.getClub().getStadium());
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
		sb.append(", endTime=").append(new Date(endTime).toString());
		sb.append(", team=").append(getTeam().getName());
		sb.append(", venue=").append(venue).append("]");
		return sb.toString();
	}
}
