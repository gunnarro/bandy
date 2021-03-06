package com.gunnarro.android.bandy.domain.activity;

import java.util.Date;

public class Cup extends Activity {
	private Integer id;
	private long startDate;
	private String cupName;
	private String clubName;
	private String venue;
	private long deadlineDate;

	public Cup(Season season, long startDate, String cupName, String clubName, String venue, long deadlineDate) {
		super(season);
		this.startDate = startDate;
		this.cupName = cupName;
		this.clubName = clubName;
		this.venue = venue;
		this.deadlineDate = deadlineDate;
	}

	public Cup(Integer id, Season season, long startDate, String cupName, String clubName, String venue, long deadlineDate) {
		this(season, startDate, cupName, clubName, venue, deadlineDate);
		this.id = id;
	}

	public long getDeadlineDate() {
		return deadlineDate;
	}

	public Integer getId() {
		return id;
	}

	public long getStartDate() {
		return startDate;
	}

	public String getCupName() {
		return cupName;
	}

	public String getClubName() {
		return clubName;
	}

	public String getVenue() {
		return venue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [id=").append(id);
		sb.append(", startDate=").append(new Date(startDate).toString());
		sb.append(", deadLine=").append(new Date(deadlineDate).toString());
		sb.append(", cupName=").append(cupName);
		sb.append(", clubName=").append(clubName);
		sb.append(", venue=").append(venue).append("]");
		return sb.toString();
	}

	@Override
	public String getName() {
		return getType();
	}

	@Override
	public String getType() {
		return ActivityTypesEnum.Cup.name();
	}
}
