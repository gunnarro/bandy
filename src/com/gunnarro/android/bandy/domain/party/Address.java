package com.gunnarro.android.bandy.domain.party;

import com.gunnarro.android.bandy.utility.Utility;

public class Address {

	private int id;
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

	public Address(int id, String streetName, String streetNumber, String streetNumberPrefix, String postalCode, String city, String country) {
		this(streetName, streetNumber, streetNumberPrefix, postalCode, city, country);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getStreetNumberPrefix() {
		return streetNumberPrefix;
	}

	public void setStreetNumberPrefix(String streetNumberPrefix) {
		this.streetNumberPrefix = streetNumberPrefix;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFullStreetName() {
		return streetName + " " + streetNumber + (streetNumberPrefix != null ? streetNumberPrefix : "");
	}

	public boolean isAddressValid() {
		return !Utility.isEmpty(streetName) && !Utility.isEmpty(streetNumber) && !Utility.isEmpty(postalCode) && !Utility.isEmpty(country);
	}

	public static Address createEmptyAddress() {
		return new Address(-1, "na", "na", "na", "na", "na", "na");
	}

	@Override
	public String toString() {
		return "Address [isAddressValid=" + this.isAddressValid() + ", id=" + id + ", streetName=" + streetName + ", streetNumber=" + streetNumber
				+ ", streetNumberPrefix=" + streetNumberPrefix + ", postalCode=" + postalCode + ", city=" + city + ", country=" + country + "]";
	}

}
