package com.gunnarro.android.bandy.domain;

public class Player extends Contact {

	public enum StatusEnum {
		ACTIVE, PASSIVE, QUIT;
	}

	private Integer id;
	private StatusEnum status = StatusEnum.ACTIVE;

	public Player(String firstName, String lastName) {
		super(firstName, lastName);
	}

}
