package com.gunnarro.android.bandy.domain;

import java.util.List;

public class Club {
	private Integer id;
	private String name;
	private String stadium;
	private String address;
	private List<Team> teams;
	private String homepage;

	public Club(String name) {
		this.name = name;
	}

	public Club(int id, String name) {
		this(name);
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getStadium() {
		return stadium;
	}

	public String getHomepage() {
		return homepage;
	}

	@Override
	public String toString() {
		return "Club [id=" + id + ", name=" + name + ", stadium=" + stadium + ", address=" + address + ", homepage=" + homepage + "]";
	}

}
