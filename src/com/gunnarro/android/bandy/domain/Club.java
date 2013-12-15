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
	
	public String getHomepage() {
		return homepage;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [id=").append(id);
		sb.append(", name=").append(name).append("]");
		return sb.toString();
	}
}
