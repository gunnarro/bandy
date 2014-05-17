package com.gunnarro.android.bandy.domain.activity;

public class Status {
	private int id;
	private String name;

	public Status(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Status [id=" + id + ", name=" + name + "]";
	}

}
