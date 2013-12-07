package com.gunnarro.android.bandy.domain;

import java.util.Date;

public class Activity implements Comparable<Activity> {

	public enum ActivityTypeEnum {
		MATCH, TRAINING, CUP;
	}

	private ActivityTypeEnum type;
	private long startDate;
	private String place;
	private String description;

	public Activity(ActivityTypeEnum type, long startDate, String place, String description) {
		this.type = type;
		this.startDate = startDate;
		this.place = place;
		this.description = description;
	}

	public ActivityTypeEnum getType() {
		return type;
	}

	public long getStartDate() {
		return startDate;
	}

	public String getPlace() {
		return place;
	}

	public String getDescription() {
		return description;
	}

	public static Activity createMatchActivity() {
		return null;// new Activity();
	}

	@Override
	public int compareTo(Activity a) {
		return Long.valueOf(startDate).compareTo(a.startDate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(", startDate=").append(new Date(startDate).toString());
		sb.append(", type=").append(type.name());
		sb.append(", place=").append(place);
		sb.append(", description=").append(description);
		return sb.toString();
	}
}
