package com.gunnarro.android.bandy.domain.statistic;

import com.gunnarro.android.bandy.domain.Activity.ActivityTypeEnum;

public class MatchStatistic {

	private int matchTypeId;
	private int played;
	private int won;
	private int draw;
	private int loss;
	private int goalsScored;
	private int goalsAgainst;

	public MatchStatistic(int matchTypeId, int played, int won, int draw, int loss, int goalsScored, int goalsAgainst) {
		this.matchTypeId = matchTypeId;
		this.played = played;
		this.won = won;
		this.draw = draw;
		this.loss = loss;
		this.goalsScored = goalsScored;
		this.goalsAgainst = goalsAgainst;
	}

	public String getName() {
		return matchTypeId + "";
	}

	public ActivityTypeEnum getMatchTypeId() {
		return ActivityTypeEnum.MATCH;
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

	@Override
	public String toString() {
		return "MatchStatistic [matchTypeId=" + matchTypeId + ", played=" + played + ", won=" + won + ", draw=" + draw + ", loss=" + loss + ", goalsScored="
				+ goalsScored + ", goalsAgainst=" + goalsAgainst + "]";
	}

}
