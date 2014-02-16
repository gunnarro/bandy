package com.gunnarro.android.bandy.domain.activity;

public abstract class Activity {

	private ActivityStatusEnum status = ActivityStatusEnum.BEGIN;

	public static enum ActivityStatusEnum {
		COMPLETED, CANCELLED, BEGIN;
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

	public abstract String getName();

	public abstract String getType();

	public boolean isFinished() {
		if (status != null) {
			return status.equals(ActivityStatusEnum.COMPLETED);
		}
		return false;
	}
}
