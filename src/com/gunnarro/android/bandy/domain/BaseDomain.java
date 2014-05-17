package com.gunnarro.android.bandy.domain;

/**
 * Common domain object
 * 
 * @author admin
 * 
 */
public abstract class BaseDomain {

	private Integer id;
	private String name;
	private long createdTime;

	public BaseDomain() {
	}

	public BaseDomain(Integer id) {
		this.id = id;
	}

	public BaseDomain(Integer id, String name, long createdTime) {
		this(id);
		this.name = name;
		this.createdTime = createdTime;
	}

	public boolean isNew() {
		return this.id == null ? true : false;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getCreatedTime() {
		return createdTime;
	}

}
