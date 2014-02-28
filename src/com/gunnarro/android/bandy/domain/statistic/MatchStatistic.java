package com.gunnarro.android.bandy.domain.statistic;

public class MatchStatistic {

	public enum MatchTypesEnum {
		TOTAL(0), LEAGUE(1), TRAINING(2), CUP(3);

		private int code;

		MatchTypesEnum(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}

		public static String getName(int code) {
			for (MatchTypesEnum m : MatchTypesEnum.values()) {
				if (m.code == code) {
					return m.name();
				}
			}
			return "unkown code " + code;
		}
	}

	private int matchTypeId = 9;
	private int played;
	private int won;
	private int draw;
	private int loss;
	private int goalsScored;
	private int goalsAgainst;

	public MatchStatistic(int matchTypeId) {
		this.matchTypeId = matchTypeId;
	}

	public MatchStatistic(int matchTypeId, int played, int won, int draw, int loss, int goalsScored, int goalsAgainst) {
		this(matchTypeId);
		this.played = played;
		this.won = won;
		this.draw = draw;
		this.loss = loss;
		this.goalsScored = goalsScored;
		this.goalsAgainst = goalsAgainst;
	}

	public int getMatchTypeId() {
		return matchTypeId;
	}

	public String getName() {
		return MatchTypesEnum.getName(matchTypeId);
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
		return "MatchStatistic [matchTypeId=" + matchTypeId + ", played=" + played + ", won=" + won + ", draw=" + draw + ", loss=" + loss + ", goalsScored="
				+ goalsScored + ", goalsAgainst=" + goalsAgainst + "]";
	}

}
