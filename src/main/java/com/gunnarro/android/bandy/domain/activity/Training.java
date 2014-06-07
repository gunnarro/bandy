package com.gunnarro.android.bandy.domain.activity;

import java.util.Date;

import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.utility.Utility;

public class Training extends Activity {

	private Integer id;
	private long startTime;
	private long endTime;
	private Team team;
	private String venue;

	public Training(Season season, long startTime, long endTime, Team team, String venue) {
		super(season);
		this.startTime = startTime;
		this.endTime = endTime;
		this.team = team;
		this.venue = venue;
	}

	public Training(int id, Season season, long startDate, long endTime, Team team, String venue) {
		this(season, startDate, endTime, team, venue);
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

	public long getStartTime() {
		return startTime;
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
		return new Training(null, System.currentTimeMillis(), System.currentTimeMillis(), team, team.getClub().getStadiumName());
	}

	@Override
	public boolean isFinished() {
		return new Date(startTime).before(new Date(System.currentTimeMillis()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [id=").append(id);
		sb.append(", startDate=").append(Utility.formatTime(startTime, Utility.DATE_PATTERN));
		sb.append(", endTime=").append(Utility.formatTime(endTime, Utility.TIME_PATTERN));
		sb.append(", team=").append(getTeam().getName());
		sb.append(", venue=").append(venue).append("]");
		return sb.toString();
	}
}
