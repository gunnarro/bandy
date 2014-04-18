package com.gunnarro.android.bandy.domain;

import java.io.Serializable;

public class League implements Serializable {

	private static final long serialVersionUID = 243232L;

	private Integer id;
	private String name;

	public League(Integer id, String name) {
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
		return "League [id=" + id + ", name=" + name + "]";
	}

}
