package com.gunnarro.android.bandy.domain.view.list;

import java.util.List;

/**
 * Domain model class for the ExpandableListView
 * 
 * @author admin
 * 
 */
public class Group {
	private Integer id;
	private String name;
	private String information;
	private final List<Item> children;;

	public Group(Integer id, String name, final List<Item> children) {
		this.id = id;
		this.name = name;
		this.children = children;
	}

	public String getName() {
		return name;
	}

	public Integer getId() {
		return id;
	}

	public List<Item> getChildren() {
		return children;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

}
