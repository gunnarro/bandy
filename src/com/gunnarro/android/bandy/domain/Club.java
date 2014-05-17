package com.gunnarro.android.bandy.domain;

import com.gunnarro.android.bandy.domain.party.Address;

public class Club extends BaseDomain {

	public static enum DepartmentNamesEnum {
		Bandy, Baseball, Hockey, Soccer, Volleyball, Other;

		public String[] asArray() {
			return new String[] { Bandy.name(), Baseball.name(), Hockey.name(), Soccer.name(), Volleyball.name(), Other.name() };
		}
	}

	private String name;
	private String departmentName;
	private String stadiumName;
	private String clubNameAbbreviation;
	private Address address;
	private String homePageUrl;

	public Club(Integer id, String name, String departmentName) {
		super(id);
		this.name = name;
		this.departmentName = departmentName;
	}

	public Club(String name, String departmentName) {
		this(null, name, departmentName);
	}

	public Club(Integer id, String name, String departmentName, String clubNameAbbreviation, String stadiumName, Address address, String homePageUrl) {
		this(id, name, departmentName);
		this.clubNameAbbreviation = clubNameAbbreviation;
		this.stadiumName = stadiumName;
		this.address = address;
		this.homePageUrl = homePageUrl;

	}

	public String getFullName() {
		return name + " " + departmentName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public void setClubNameAbbreviation(String clubNameAbbreviation) {
		this.clubNameAbbreviation = clubNameAbbreviation;
	}

	public String getName() {
		return name;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public String getClubNameAbbreviation() {
		return clubNameAbbreviation;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getStadiumName() {
		return stadiumName;
	}

	public String getHomePageUrl() {
		return homePageUrl;
	}

	public void setStadiumName(String stadiumName) {
		this.stadiumName = stadiumName;
	}

	public void setHomePageUrl(String homePageUrl) {
		this.homePageUrl = homePageUrl;
	}

	@Override
	public String toString() {
		return "Club [id=" + getId() + ", name=" + name + ", departmentName=" + departmentName + ", stadiumName=" + stadiumName + ", clubNameAbbreviation="
				+ clubNameAbbreviation + ", homePageUrl=" + homePageUrl + "]";
	}

}
