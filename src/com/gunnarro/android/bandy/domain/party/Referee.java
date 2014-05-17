package com.gunnarro.android.bandy.domain.party;

public class Referee extends Contact {

	public Referee(String firstName, String middleName, String lastName) {
		super(firstName, middleName, lastName);
	}

	public Referee(Integer id, String firstName, String middleName, String lastName, String gender, Address address, String mobileNumber, String emailAddress) {
		super(firstName, middleName, lastName);
	}

}
