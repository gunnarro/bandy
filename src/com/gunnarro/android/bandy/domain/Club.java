package com.gunnarro.android.bandy.domain;

import java.util.List;

import com.gunnarro.android.bandy.domain.party.Address;

public class Club {
	private Integer id;
	private String name;
	private String departmentName;
	private String stadiumName;
	private String clubNameAbbreviation;
	private Address address;
	private String homePageUrl;
	private List<Team> teams;

	public Club(String name, String departmentName) {
		this.name = name;
		this.departmentName = departmentName;
	}

	public Club(String name, String departmentName, String clubNameAbbreviation, String stadiumName, Address address, String homePageUrl) {
		this(stadiumName, departmentName);
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

}
