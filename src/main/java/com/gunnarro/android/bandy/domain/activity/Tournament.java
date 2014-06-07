package com.gunnarro.android.bandy.domain.activity;

public class Tournament extends Activity {

	private Integer id;
	private long startDate;
	private String tournamentName;
	private String organizerName;
	private String venue;
	private long deadlineDate;

	public Tournament(Season season, long startDate, String tournamentName, String organizerName, String venue, long deadlineDate) {
		super(season);
		this.startDate = startDate;
		this.tournamentName = tournamentName;
		this.organizerName = organizerName;
		this.venue = venue;
		this.deadlineDate = deadlineDate;
	}

	public Tournament(Integer id, Season season, long startDate, String tournamentName, String organizerName, String venue, long deadlineDate) {
		this(season, startDate, tournamentName, organizerName, venue, deadlineDate);
		this.id = id;
	}

	public String getTournamentName() {
		return tournamentName;
	}

	public String getOrganizerName() {
		return organizerName;
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

	public String getVenue() {
		return venue;
	}

	@Override
	public String getName() {
		return getType();
	}

	@Override
	public String getType() {
		return ActivityTypesEnum.Tournament.name();
	}
}
