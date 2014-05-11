package com.gunnarro.android.bandy.domain;

import com.gunnarro.android.bandy.domain.party.Address;

public class Club {

	public static enum DepartmentNamesEnum {
		Bandy, Baseball, Hockey, Soccer, Volleyball, Other;

		public String[] asArray() {
			return new String[] { Bandy.name(), Baseball.name(), Hockey.name(), Soccer.name(), Volleyball.name(), Other.name() };
		}
	}

	private Integer id;
	private String name;
	private String departmentName;
	private String stadiumName;
	private String clubNameAbbreviation;
	private Address address;
	private String homePageUrl;

	public Club(String name, String departmentName) {
		this.name = name;
		this.departmentName = departmentName;
	}

	public Club(String name, String departmentName, String clubNameAbbreviation, String stadiumName, Address address, String homePageUrl) {
		this(name, departmentName);
		this.clubNameAbbreviation = clubNameAbbreviation;
		this.stadiumName = stadiumName;
		this.address = address;
		this.homePageUrl = homePageUrl;
	}

	public Club(Integer id, String name, String departmentName, String clubNameAbbreviation, String stadiumName, Address address, String homepageUrl) {
		this(name, departmentName, clubNameAbbreviation, stadiumName, address, homepageUrl);
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public String getFullName() {
		return name + " " + departmentName;
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
		return "Club [id=" + id + ", name=" + name + ", departmentName=" + departmentName + ", stadiumName=" + stadiumName + ", clubNameAbbreviation="
				+ clubNameAbbreviation + ", homePageUrl=" + homePageUrl + "]";
	}

}
