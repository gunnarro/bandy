package com.gunnarro.android.bandy.domain.party;

import com.gunnarro.android.bandy.utility.Utility;

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

	public Address(int id, String streetName, String streetNumber, String streetNumberPrefix, String postalCode, String city, String country) {
		this(streetName, streetNumber, streetNumberPrefix, postalCode, city, country);
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getStreetName() {
		return streetName;
	}

	public String getFullStreetName() {
		return streetName + " " + streetNumber + (streetNumberPrefix != null ? streetNumberPrefix : "");
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

	public boolean isAddressValid() {
		return !Utility.isEmpty(streetName) && !Utility.isEmpty(streetNumber) && !Utility.isEmpty(postalCode) && !Utility.isEmpty(country);
	}

	public static Address createEmptyAddress() {
		return new Address("na", "na", "na", "na", "na", "na");
	}

	@Override
	public String toString() {
		return "Address [isEmpty=" + this.isAddressValid() + ", streetName=" + streetName + ", streetNumber=" + streetNumber + ", streetNumberPrefix="
				+ streetNumberPrefix + ", postalCode=" + postalCode + ", city=" + city + ", country=" + country + "]";
	}

}
