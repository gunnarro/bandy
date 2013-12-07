package com.gunnarro.android.bandy.domain;

import java.util.List;

public class Contact {
	
	public enum ContactRoleEnum {
		DEFAULT, COACH, TEAM_LEAD, PARENT;
	}

	private Integer id = -1;
	private Team team;
	private List<ContactRoleEnum> roles;
	private String firstName;
	private String middleName;
	private String lastName;
	private String mobileNumber;
	private String emailAddress;
	private Address address;

	public Contact(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Contact(Team team, List<ContactRoleEnum> roles, String firstName, String middleName, String lastName, String mobileNumber, String emailAddress,
			Address address) {
		this.team = team;
		this.roles = roles;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.mobileNumber = mobileNumber;
		this.emailAddress = emailAddress;
		this.address = address;
	}

	public Integer getId() {
		return id;
	}

	public Team getTeam() {
		return team;
	}

	public List<ContactRoleEnum> getRoles() {
		return roles;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}
}
