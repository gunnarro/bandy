package com.gunnarro.android.bandy.domain;

public class Address {

	private long id;
	private String streetName;
	private String streetNumber;
	private String streetNumberPrefix;
	private String postalCode;
	private String city;
	private String country;

	public Address(String streetName, String streetNumber, String streetNumberPrefix, String postalCode, String city, String country) {
		this.streetName = streetName;
		this.streetNumber = streetNumber;
		this.streetNumberPrefix = streetNumberPrefix;
		this.postalCode = postalCode;
		this.city = city;
		this.country = country;
	}

	public long getId() {
		return id;
	}

	public String getStreetName() {
		return streetName;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public String getStreetNumberPrefix() {
		return streetNumberPrefix;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	@Override
	public String toString() {
		return "Address [streetAddess=" + streetName + ", streetNumber=" + streetNumber + ", streetNumberPrefix=" + streetNumberPrefix + ", postalCode="
				+ postalCode + ", city=" + city + ", country=" + country + "]";
	}

}
