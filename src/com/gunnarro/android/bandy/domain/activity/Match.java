package com.gunnarro.android.bandy.domain.activity;

import java.util.Date;

import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.party.Referee;

public class Match extends Activity {

	private Integer id;
	private long startTime;
	private Team team;
	private Team homeTeam;
	private Team awayTeam;
	private String venue;
	private Referee referee;
	private Integer numberOfGoalsHome;
	private Integer numberOfGoalsAway;
	private Integer matchTypeId;

	private Match(Season season, long startTime, Team team, Team homeTeam, Team awayTeam, String venue, Referee referee) {
		super(season);
		this.startTime = startTime;
		this.team = team;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.venue = venue;
		this.referee = referee;
	}

	public Match(Integer id, Season season, long startDate, Team team, Team homeTeam, Team awayTeam, String venue, Referee referee, Integer matchTypeId) {
		this(season, startDate, team, homeTeam, awayTeam, venue, referee);
		this.id = id;
		this.matchTypeId = matchTypeId;
	}

	public Match(Integer id, Season season, long startTime, Team team, Team homeTeam, Team awayTeam, Integer numberOfGoalsHome, Integer numberOfGoalsAway,
			String venue, Referee referee, Integer matchTypeId) {
		this(id, season, startTime, team, homeTeam, awayTeam, venue, referee, matchTypeId);
		this.numberOfGoalsHome = numberOfGoalsHome;
		this.numberOfGoalsAway = numberOfGoalsAway;

	}

	// public Match(Integer id, Season season, long startTime, Team team, Team
	// homeTeam, Team awayTeam, Integer numberOfGoalsHome, Integer
	// numberOfGoalsAway,
	// String venue, Referee referee, Integer matchTypeId) {
	// this(startTime, team, homeTeam, awayTeam, numberOfGoalsHome,
	// numberOfGoalsAway, venue, referee, matchTypeId);
	// this.id = id;
	// }

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
		return isFinished() && numberOfGoalsHome != null && numberOfGoalsAway != null;
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

	public Integer getMatchTypeId() {
		return matchTypeId;
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

	@Override
	public boolean isFinished() {
		return new Date(startTime).before(new Date(System.currentTimeMillis()));
	}

	@Override
	public String getName() {
		return getType();
	}

	@Override
	public String getType() {
		return ActivityTypesEnum.Match.name();
	}
}
