package com.gunnarro.android.bandy.domain.activity;

public class MatchEvent {

//	 Goal
//	 Own Goal
//	 Penalty
//	 Penalty Missed
//	 Assist
//	 Penalty Shootout Goal
//	 Penalty Shootout Miss
//	 Yellow Card
//	 Red Card
//	 Substitution IN
//	 Substitution OUT
//	 Injury
	 
	public enum MatchEventTypesEnum {
		GOAL_HOME, GOAL_AWAY;
	}

	private int id;
	private int matchId;
	private long eventTime;
	private Integer playedMinutes;
	private String teamName;
	private String playerName;
	private String eventTypeName;
	private String value;

	public MatchEvent(int matchId, int playedMinutes, String teamName, String playerName, String eventTypeName, String value) {
		this.matchId = matchId;
		this.playedMinutes = playedMinutes;
		this.teamName = teamName;
		this.playerName = playerName;
		this.eventTypeName = eventTypeName;
		this.value = value;
	}

	public MatchEvent(int id, int matchId, int playedMinutes, String teamName, String playerName, String eventTypeName, String value) {
		this( matchId, playedMinutes, teamName, playerName, eventTypeName, value);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public long getEventTime() {
		return eventTime;
	}

	public int getMatchId() {
		return matchId;
	}

	public String getTeamName() {
		return teamName;
	}

	public Integer getPlayedMinutes() {
		return playedMinutes;
	}

	public String getPlayerName() {
		return playerName;
	}

	public String getEventTypeName() {
		return eventTypeName;
	}

	public String getValue() {
		return value;
	}

	public String getInfo() {
		return playedMinutes + ":" + playerName + " " + value;
	}

	@Override
	public String toString() {
		return "MatchEvent [id=" + id + ", matchId=" + matchId + ", teamName=" + teamName + ", eventTime=" + eventTime + ", playedMinutes=" + playedMinutes
				+ ", playerName=" + playerName + ", eventTypeName=" + eventTypeName + ", value=" + value + "]";
	}

}
