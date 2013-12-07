package com.gunnarro.android.bandy.domain;

public class Address {

	private String streetAddess;
	private String streetNumber;
	private String postalCode;
	private String city;
	private String state;
	private String country;

	public Address(String streetAddess, String streetNumber, String postalCode, String city, String state, String country) {
		this.streetAddess = streetAddess;
		this.streetNumber = streetNumber;
		this.postalCode = postalCode;
		this.city = city;
		this.state = state;
		this.country = country;
	}

	public String getStreetAddess() {
		return streetAddess;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getCountry() {
		return country;
	}

	@Override
	public String toString() {
		return "streetAddess=" + streetAddess + ", streetNumber=" + streetNumber + ", postalCode=" + postalCode + ", city=" + city + ", state=" + state
				+ ", country=" + country;
	}

}
