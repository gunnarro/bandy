package com.gunnarro.android.bandy.domain.party;

import java.util.List;

import com.gunnarro.android.bandy.domain.Team;

public class Contact {

	public enum ContactRoleEnum {
		DEFAULT, COACH, TEAMLEAD, PARENT;
	}

	private Integer id;
	private Team team;
	private List<ContactRoleEnum> roles;
	private String firstName;
	private String middleName;
	private String lastName;
	private String mobileNumber;
	private String emailAddress;
	private Address address;

	public Contact(Team team, String firstName, String middleName, String lastName) {
		this.team = team;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
	}

	public Contact(Integer id, Team team, String firstName, String middleName, String lastName) {
		this(team, firstName, middleName, lastName);
		this.id = id;
	}

	public Contact(Team team, String firstName, String middleName, String lastName, Address address) {
		this(team, firstName, middleName, lastName);
		this.address = address;
	}

	public Contact(int id, Team team, String firstName, String middleName, String lastName, Address address) {
		this(id, team, firstName, middleName, lastName);
		this.address = address;
	}

	public Contact(Integer id, Team team, Address address, List<ContactRoleEnum> roles, String firstName, String middleName, String lastName,
			String mobileNumber, String emailAddress) {
		this(id, team, firstName, middleName, lastName);
		this.address = address;
		this.roles = roles;
		this.mobileNumber = mobileNumber;
		this.emailAddress = emailAddress;
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

	public Address getAddress() {
		return address;
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
		return firstName + " " + (middleName != null ? middleName + " " : "") + lastName;
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", team=" + team + ", roles=" + roles + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName="
				+ lastName + ", mobileNumber=" + mobileNumber + ", emailAddress=" + emailAddress + ", address=" + address + "]";
	}

}
