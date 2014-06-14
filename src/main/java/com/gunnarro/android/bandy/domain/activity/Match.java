package com.gunnarro.android.bandy.domain.activity;

import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Type.MatchTypesEnum;
import com.gunnarro.android.bandy.domain.party.Referee;
import com.gunnarro.android.bandy.utility.Utility;

public class Match extends Activity {

	public static enum MatchStatus {
		PLAYED("PLAYED"), NOT_PLAYED("NOT PLAYED"), CANCELLED("CANCELLED"), POSTPONED("POSTPONED"), ONGOING("ONGOING");
		private String name;

		MatchStatus(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	private Integer id;
	private long startTime;
	private Team team;
	private Team homeTeam;
	private Team awayTeam;
	private String venue;
	private MatchStatus matchStatus = MatchStatus.NOT_PLAYED;
	private Referee referee;
	private Integer numberOfGoalsHome;
	private Integer numberOfGoalsAway;
	private Integer numberOfSignedPlayers;
	private MatchTypesEnum matchType = MatchTypesEnum.LEAGUE;

	private Match(MatchTypesEnum type, MatchStatus status) {
		super(null);
		this.matchType = type;
		this.matchStatus = status;
	}

	public Match(Season season, long startTime, Team team, Team homeTeam, Team awayTeam, String venue, Referee referee) {
		super(season);
		this.startTime = startTime;
		this.team = team;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.venue = venue;
		this.referee = referee;
	}

	public Match(Integer id, Season season, long startDate, Team team, Team homeTeam, Team awayTeam, String venue, Referee referee, MatchTypesEnum matchType) {
		this(season, startDate, team, homeTeam, awayTeam, venue, referee);
		this.id = id;
		this.matchType = matchType;
	}

	public Match(Integer id, Season season, long startTime, Team team, Team homeTeam, Team awayTeam, Integer numberOfGoalsHome, Integer numberOfGoalsAway,
			String venue, Referee referee, MatchTypesEnum matchType) {
		this(id, season, startTime, team, homeTeam, awayTeam, venue, referee, matchType);
		this.numberOfGoalsHome = numberOfGoalsHome;
		this.numberOfGoalsAway = numberOfGoalsAway;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public void setHomeTeam(Team homeTeam) {
		this.homeTeam = homeTeam;
	}

	public void setAwayTeam(Team awayTeam) {
		this.awayTeam = awayTeam;
	}

	public void setVenue(String venue) {
		this.venue = venue;
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

	public void setStartTime(long startTime) {
		this.startTime = startTime;
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

	public void setReferee(Referee referee) {
		this.referee = referee;
	}

	public String getTeamVersus() {
		return this.homeTeam.getName() + " - " + this.awayTeam.getName();
	}

	public MatchStatus getMatchStatus() {
		return matchStatus;
	}

	public void setMatchStatus(MatchStatus matchStatus) {
		this.matchStatus = matchStatus;
	}

	public boolean isPlayed() {
		return matchStatus.equals(MatchStatus.PLAYED);
	}

	public Integer getNumberOfGoalsHome() {
		return numberOfGoalsHome;
	}

	public Integer getNumberOfGoalsAway() {
		return numberOfGoalsAway;
	}

	public void setNumberOfGoalsHome(Integer numberOfGoalsHome) {
		this.numberOfGoalsHome = numberOfGoalsHome;
	}

	public void setNumberOfGoalsAway(Integer numberOfGoalsAway) {
		this.numberOfGoalsAway = numberOfGoalsAway;
	}

	public Integer getNumberOfSignedPlayers() {
		return numberOfSignedPlayers;
	}

	public void setNumberOfSignedPlayers(int numberOfSignedPlayers) {
		this.numberOfSignedPlayers = numberOfSignedPlayers;
	}

	public String getResult() {
		if (numberOfGoalsHome != null && numberOfGoalsAway != null) {
			return numberOfGoalsHome + " - " + numberOfGoalsAway;
		} else {
			return null;
		}
	}

	public MatchTypesEnum getMatchType() {
		return matchType;
	}

	public static Match createCupMatch() {
		return new Match(MatchTypesEnum.CUP, MatchStatus.NOT_PLAYED);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [id=").append(id);
		sb.append(", startDate=").append(Utility.formatTime(startTime, Utility.DATE_TIME_PATTERN));
		sb.append(", versus=").append(getTeamVersus());
		sb.append(", result=").append(getResult());
		sb.append(", venue=").append(venue).append("]");
		return sb.toString();
	}

	@Override
	public String getName() {
		return getTeamVersus();
	}

	@Override
	public String getType() {
		return ActivityTypesEnum.Match.name();
	}
}
