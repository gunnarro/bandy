package com.gunnarro.android.bandy.domain;

import java.util.List;

public class Player extends Contact {

	public enum PlayerStatusEnum {
		ACTIVE, PASSIVE, QUIT;
	}

	private PlayerStatusEnum status = PlayerStatusEnum.ACTIVE;
	private List<Contact> parents;
	private long dateOfBirth;

	public Player(String firstName, String lastName, PlayerStatusEnum status, List<Contact> parents, long dateOfBirth) {
		super(firstName, lastName);
		this.status = status;
		this.parents = parents;
		this.dateOfBirth = dateOfBirth;
	}

	public PlayerStatusEnum getStatus() {
		return status;
	}

	public List<Contact> getParents() {
		return parents;
	}

	public long getDateOfBirth() {
		return dateOfBirth;
	}

}
