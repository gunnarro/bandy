package com.gunnarro.android.bandy.domain.statistic;

import com.gunnarro.android.bandy.domain.activity.Type.MatchTypesEnum;

public class MatchStatistic {

	private String teamName = null;
	private int seasonId = 0;
	private MatchTypesEnum type = MatchTypesEnum.LEAGUE;
	private int played;
	private int won;
	private int draw;
	private int loss;
	private int goalsScored;
	private int goalsAgainst;

	public MatchStatistic(MatchTypesEnum type) {
		this.type = type;
	}

	public MatchStatistic(int seasonId, String teamName, MatchTypesEnum type, int played, int won, int draw, int loss, int goalsScored, int goalsAgainst) {
		this(type);
		this.seasonId = seasonId;
		this.teamName = teamName;
		this.played = played;
		this.won = won;
		this.draw = draw;
		this.loss = loss;
		this.goalsScored = goalsScored;
		this.goalsAgainst = goalsAgainst;
	}

	public String getTeamName() {
		return teamName;
	}

	public int getSeasonId() {
		return seasonId;
	}

	public String getName() {
		return type.getName();
	}

	public MatchTypesEnum getMatchType() {
		return MatchTypesEnum.valueOf(getName());
	}

	public Integer getPlayed() {
		return played;
	}

	public Integer getWon() {
		return won;
	}

	public Integer getDraw() {
		return draw;
	}

	public Integer getLoss() {
		return loss;
	}

	public Integer getGoalsScored() {
		return goalsScored;
	}

	public Integer getGoalsAgainst() {
		return goalsAgainst;
	}

	public String getGoals() {
		return goalsScored + " - " + goalsAgainst;
	}

	public void incrementPlayed(int n) {
		this.played = this.played + n;
	}

	public void incrementWon(int n) {
		this.won = this.won + n;
	}

	public void incrementDraw(int n) {
		this.draw = this.draw + n;
	}

	public void incrementLoss(int n) {
		this.loss = this.loss + n;
	}

	public void incrementGoalsScored(int n) {
		this.goalsScored = this.goalsScored + n;
	}

	public void incrementGoalsAgainst(int n) {
		this.goalsAgainst = this.goalsAgainst + n;
	}

	@Override
	public String toString() {
		return "MatchStatistic [matchTypeId=" + type.getName() + ", played=" + played + ", won=" + won + ", draw=" + draw + ", loss=" + loss + ", goalsScored="
				+ goalsScored + ", goalsAgainst=" + goalsAgainst + "]";
	}

}
