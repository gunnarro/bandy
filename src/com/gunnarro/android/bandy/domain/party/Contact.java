package com.gunnarro.android.bandy.domain.party;

import java.util.ArrayList;
import java.util.List;

import com.gunnarro.android.bandy.domain.BaseDomain;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.party.Role.RoleTypesEnum;
import com.gunnarro.android.bandy.utility.Utility;

public class Contact extends BaseDomain {

	private Team team;
	private List<RoleTypesEnum> roles;
	private String firstName;
	private String middleName;
	private String lastName;
	private String gender;
	private String mobileNumber;
	private String emailAddress;
	private Address address;

	public Contact(String firstName, String middleName, String lastName) {
		this.firstName = firstName.toUpperCase();
		this.middleName = middleName != null ? middleName.toUpperCase() : null;
		this.lastName = lastName.toUpperCase();
	}

	public Contact(Integer id, Team team, String firstName, String middleName, String lastName, String gender) {
		super(id);
		this.team = team;
		this.firstName = firstName != null ? firstName.toUpperCase() : null;
		this.middleName = middleName != null ? middleName.toUpperCase() : null;
		this.lastName = lastName != null ? lastName.toUpperCase() : null;
		this.gender = gender;
	}

	public Contact(Team team, String firstName, String middleName, String lastName, String gender, Address address) {
		this(null, team, firstName, middleName, lastName, gender);
		this.address = address;
	}

	public Contact(int id, Team team, String firstName, String middleName, String lastName, String gender, Address address) {
		this(id, team, firstName, middleName, lastName, gender);
		this.address = address;
	}

	public Contact(Integer id, Team team, Address address, List<RoleTypesEnum> roles, String firstName, String middleName, String lastName, String gender,
			String mobileNumber, String emailAddress) {
		this(id, team, firstName, middleName, lastName, gender);
		this.address = address;
		this.roles = roles;
		this.mobileNumber = mobileNumber;
		this.emailAddress = emailAddress;
	}

	public Contact(Team team, List<RoleTypesEnum> roles, String firstName, String middleName, String lastName, String gender, String mobileNumber,
			String emailAddress, Address address) {
		this.team = team;
		this.roles = roles;
		this.firstName = firstName.toUpperCase();
		this.middleName = middleName != null ? middleName.toUpperCase() : null;
		this.lastName = lastName.toUpperCase();
		this.gender = gender;
		this.mobileNumber = mobileNumber;
		this.emailAddress = emailAddress;
		this.address = address;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Team getTeam() {
		return team;
	}

	public List<RoleTypesEnum> getRoles() {
		return roles;
	}

	public void addRole(RoleTypesEnum newRole) {
		if (newRole != null) {
			if (roles == null) {
				roles = new ArrayList<RoleTypesEnum>();
			}
			roles.add(newRole);
		}
	}

	public String getFirstName() {
		return Utility.capitalizationWord(firstName);
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName.toUpperCase();
	}

	public String getMiddleName() {
		return Utility.capitalizationWord(middleName);
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName.toUpperCase();
	}

	public String getLastName() {
		return Utility.capitalizationWord(lastName);
	}

	public void setLastName(String lastName) {
		this.lastName = lastName.toUpperCase();
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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
		return getFirstName() + " " + (middleName != null ? getMiddleName() + " " : "") + getLastName();
	}

	public boolean hasTeamRoles() {
		return roles != null && roles.size() > 0 ? true : false;
	}

	public static Contact createContact(String fullName) {
		String[] split = fullName.split(" ");
		Contact contact = null;
		if (split.length == 3) {
			contact = new Contact(split[0], split[2], split[2]);
		} else if (split.length == 2) {
			contact = new Contact(split[0], null, split[1]);
		}
		return contact;
	}

	@Override
	public String toString() {
		return "Contact [id=" + getId() + ", team=" + team + ", roles=" + roles + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName="
				+ lastName + ", mobileNumber=" + mobileNumber + ", emailAddress=" + emailAddress + ", address=" + address + "]";
	}

}
