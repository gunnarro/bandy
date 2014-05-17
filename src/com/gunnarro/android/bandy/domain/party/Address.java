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
		this.streetName = streetName.toUpperCase();
		this.streetNumber = streetNumber;
		this.streetNumberPrefix = streetNumberPrefix != null ? streetNumberPrefix.toUpperCase() : null;
		this.postalCode = postalCode;
		this.city = city.toUpperCase();
		this.country = country.toUpperCase();
	}

	public Address(int id, String streetName, String streetNumber, String streetNumberPrefix, String postalCode, String city, String country) {
		this(streetName, streetNumber, streetNumberPrefix, postalCode, city, country);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getStreetName() {
		return Utility.capitalizationWord(streetName);
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName.toUpperCase();
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getStreetNumberPrefix() {
		return Utility.capitalizationWord(streetNumberPrefix);
	}

	public void setStreetNumberPrefix(String streetNumberPrefix) {
		this.streetNumberPrefix = streetNumberPrefix.toUpperCase();
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return Utility.capitalizationWord(city);
	}

	public void setCity(String city) {
		this.city = city.toUpperCase();
	}

	public String getCountry() {
		return Utility.capitalizationWord(country);
	}

	public void setCountry(String country) {
		this.country = country.toUpperCase();
	}

	public String getFullStreetName() {
		return getStreetName() + " " + streetNumber + (streetNumberPrefix != null ? getStreetNumberPrefix() : "");
	}

	public String getFullAddress() {
		StringBuffer sb = new StringBuffer();
		sb.append(getFullStreetName()).append("\n");
		sb.append(getPostalCode()).append(" ").append(getCity()).append("\n");
		sb.append(getCountry());
		return sb.toString();
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
