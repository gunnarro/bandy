package com.gunnarro.android.bandy.domain;

import java.util.ArrayList;
import java.util.List;

import com.gunnarro.android.bandy.domain.view.list.Item;

public class Player extends Contact {

	public enum PlayerStatusEnum {
		ACTIVE, PASSIVE, QUIT;
	}

	private PlayerStatusEnum status = PlayerStatusEnum.ACTIVE;
	private List<Contact> parents;
	private List<Item> parentItemList;
	private long dateOfBirth;
	private String schoolName;

	public Player(int id, Team team, String firstName, String middleName, String lastName, PlayerStatusEnum status, List<Contact> parents, long dateOfBirth,
			Address address) {
		super(id, team, firstName, middleName, lastName, address);
		this.status = status;
		this.parents = parents;
		this.dateOfBirth = dateOfBirth;
	}

	public PlayerStatusEnum getStatus() {
		return status;
	}

	public List<Contact> getParents() {
		return parents;
	}

	public void setParentItemList(List<Item> parentItemList) {
		this.parentItemList = parentItemList;
	}

	public List<Item> getParentItemList() {
		parentItemList = new ArrayList<Item>();
		for (Contact parent : parents) {
			parentItemList.add(new Item(parent.getId(), parent.getFullName(), false));
		}
		return parentItemList;
	}

	public long getDateOfBirth() {
		return dateOfBirth;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public boolean hasParents() {
		return (parents != null && parents.size() > 0);
	}

	@Override
	public String toString() {
		return super.toString() + ", dateOfBirth=" + dateOfBirth + ", parents=" + parents.toString();
	}
}
