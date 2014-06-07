package com.gunnarro.android.bandy.domain.activity;

public class Status {
	private Integer id;
	private String name;

	public Status(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
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
