package com.gunnarro.android.bandy.domain.view.list;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class for holding list items.
 * 
 * @author gunnarro
 * 
 */
public class Item implements Parcelable, Comparable<Item> {

	private Integer id;
	private String value;
	private boolean isEnabled;

	// -------------------------------------------------------------------------
	// Parcelable Interface
	// -------------------------------------------------------------------------

	private Item(Parcel in) {
		this.id = in.readInt();
		this.value = in.readString();
		this.isEnabled = Boolean.valueOf(in.readString());
	}

	public static final Parcelable.Creator<Item> CREATOR = new Creator<Item>() {
		public Item createFromParcel(Parcel in) {
			return new Item(in);
		}

		@Override
		public Item[] newArray(int size) {
			return new Item[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(id);
		parcel.writeString(value);
		parcel.writeString(Boolean.toString(isEnabled));
	}

	// -------------------------------------------------------------------------

	public Item(Integer id, String value, boolean isEnabled) {
		this.id = id;
		this.value = value;
		this.isEnabled = isEnabled;
	}

	public Integer getId() {
		return id;
	}

	public Boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public void toggleEnabled() {
		this.isEnabled = !this.isEnabled;
	}

	public String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int multiplier = 23;
		int hashCode = 0;
		if (hashCode == 0) {
			int code = 133;
			code = multiplier * code + this.value.hashCode();
			hashCode = code;
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Item)) {
			return false;
		}
		final Item other = (Item) obj;
		if (this.value.equals(other.value)) {
			return true;
		}
		return false;
	}

	@Override
	public int compareTo(Item item) {
		return this.value.compareTo(item.getValue());
	}

	/**
	 * The toString method is called by the ArrayAdapter in the
	 * CommonListFragment, which value is viewed in the list.
	 */
	@Override
	public String toString() {
		return this.value;
	}

}
