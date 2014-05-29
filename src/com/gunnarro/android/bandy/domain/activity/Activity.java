package com.gunnarro.android.bandy.domain.activity;

public abstract class Activity {

	public static enum ActivityStatusEnum {
		COMPLETED, CANCELLED, BEGIN, NOT_STARTED;
	}

	private Status status = null;
	private Season season;
	private long startTime;
	private long endTime;
	private int numberOfParticipatedPlayers;

	public Activity(Season season) {
		this.season = season;
	}

	public static enum ActivityTypesEnum {
		Training, Match, Cup, Tournament;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getNumberOfParticipatedPlayers() {
		return numberOfParticipatedPlayers;
	}

	public void setNumberOfParticipatedPlayers(int numberOfParticipatedPlayers) {
		this.numberOfParticipatedPlayers = numberOfParticipatedPlayers;
	}

	public Season getSeason() {
		return season;
	}

	public abstract String getName();

	public abstract String getType();

	public boolean isFinished() {
		if (status != null) {
			return status.equals(ActivityStatusEnum.COMPLETED);
		}
		return false;
	}
}
