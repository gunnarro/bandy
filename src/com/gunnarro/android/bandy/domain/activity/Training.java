package com.gunnarro.android.bandy.domain.activity;

import java.util.Date;

import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.utility.Utility;

public class Training extends Activity {

	private Integer id;
	private long startDate;
	private long endTime;
	private Team team;
	private String venue;

	public Training(long startTime, long length, Team team) {
		this.startDate = startTime;
		this.endTime = this.startDate + length * 60 * 60 * 1000;
		this.team = team;
		this.venue = team.getClub().getStadium();
	}

	public Training(long startDate, long endTime, Team team, String venue) {
		this.startDate = startDate;
		this.endTime = endTime;
		this.team = team;
		this.venue = venue;
	}

	public Training(int id, long startDate, long endTime, Team team, String venue) {
		this(startDate, endTime, team, venue);
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return ActivityTypesEnum.Training.name();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getType() {
		return ActivityTypesEnum.Training.name();
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

	@Override
	public boolean isFinished() {
		return new Date(startDate).before(new Date(System.currentTimeMillis()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [id=").append(id);
		sb.append(", startDate=").append(Utility.formatTime(startDate, Utility.DATE_PATTERN));
		sb.append(", endTime=").append(Utility.formatTime(endTime, Utility.TIME_PATTERN));
		sb.append(", team=").append(getTeam().getName());
		sb.append(", venue=").append(venue).append("]");
		return sb.toString();
	}
}
