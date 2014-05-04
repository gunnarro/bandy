package com.gunnarro.android.bandy.domain.activity;

public abstract class Activity {

	public static enum ActivityStatusEnum {
		COMPLETED, CANCELLED, BEGIN;
	}

	private ActivityStatusEnum status = ActivityStatusEnum.BEGIN;
	private Season season;

	public Activity(Season season) {
		this.season = season;
	}

	public static enum ActivityTypesEnum {
		Training, Match, Cup;
	}

	public ActivityStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ActivityStatusEnum status) {
		this.status = status;
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
