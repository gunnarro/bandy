package com.gunnarro.android.bandy.domain.activity;

public class Season {

	private Integer id;
	private String period;
	private long startTime;
	private long endTime;

	public Season(String period, long startTime, long endTime) {
		this.period = period;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Season(int id, String period, long startTime, long endTime) {
		this(period, startTime, endTime);
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public String getPeriod() {
		return period;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	@Override
	public String toString() {
		return "Season [period=" + period + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}

}
