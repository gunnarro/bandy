package com.gunnarro.android.bandy.domain;

public class Statistic {

	private String clubName;
	private String teamName;
	private int playerId;
	private int numberOfPlayerMatches;
	private int numberOfPlayerCups;
	private int numberOfPlayerTrainings;
	private int numberOfTeamMatches;
	private int numberOfTeamCups;
	private int numberOfTeamTrainings;

	public Statistic(String clubName, String teamName, int playerId, int numberOfPlayerMatches, int numberOfPlayerCups, int numberOfPlayerTrainings,
			int numberOfTeamMatches, int numberOfTeamCups, int numberOfTeamTrainings) {
		this.clubName = clubName;
		this.teamName = teamName;
		this.playerId = playerId;
		this.numberOfPlayerMatches = numberOfPlayerMatches;
		this.numberOfPlayerCups = numberOfPlayerCups;
		this.numberOfPlayerTrainings = numberOfPlayerTrainings;
		this.numberOfTeamMatches = numberOfTeamMatches;
		this.numberOfTeamCups = numberOfTeamCups;
		this.numberOfTeamTrainings = numberOfTeamTrainings;
	}

	public String getClubName() {
		return clubName;
	}

	public String getTeamName() {
		return teamName;
	}

	public int getPlayerId() {
		return playerId;
	}

	public int getNumberOfTeamMatches() {
		return numberOfTeamMatches;
	}

	public int getNumberOfTeamCups() {
		return numberOfTeamCups;
	}

	public int getNumberOfTeamTrainings() {
		return numberOfTeamTrainings;
	}

	public int getNumberOfPlayerMatches() {
		return numberOfPlayerMatches;
	}

	public int getNumberOfPlayerCups() {
		return numberOfPlayerCups;
	}

	public int getNumberOfPlayerTrainings() {
		return numberOfPlayerTrainings;
	}

	@Override
	public String toString() {
		return "Statistic [clubName=" + clubName + ", teamName=" + teamName + ", playerId=" + playerId + ", numberOfPlayerMatches=" + numberOfPlayerMatches
				+ ", numberOfPlayerCups=" + numberOfPlayerCups + ", numberOfPlayerTrainings=" + numberOfPlayerTrainings + ", numberOfTeamMatches="
				+ numberOfTeamMatches + ", numberOfTeamCups=" + numberOfTeamCups + ", numberOfTeamTrainings=" + numberOfTeamTrainings + "]";
	}

}
