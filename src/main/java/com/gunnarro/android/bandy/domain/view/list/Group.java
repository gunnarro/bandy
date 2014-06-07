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
	private String header;
	private String subHeader1;
	private String subHeader2;
	private boolean isEnabled;
	private int numberOfSelectedChildren;
	private final List<Item> children;

	public Group(Integer id, String header, boolean isEnabled, final List<Item> children) {
		this.id = id;
		this.header = header;
		this.isEnabled = isEnabled;
		this.children = children;
	}

	public String getHeader() {
		return header;
	}

	public String getSubHeader1() {
		return subHeader1;
	}

	public void setSubHeader1(String subHeader1) {
		this.subHeader1 = subHeader1;
	}

	public String getSubHeader2() {
		return subHeader2;
	}

	public void setSubHeader2(String subHeader2) {
		this.subHeader2 = subHeader2;
	}

	public Integer getId() {
		return id;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public List<Item> getChildren() {
		return children;
	}

	public int getNumberOfSelectedChildren() {
		return numberOfSelectedChildren;
	}

	public void setNumberOfSelectedChildren(int numberOfSelectedChildren) {
		this.numberOfSelectedChildren = numberOfSelectedChildren;
	}

	@Override
	public String toString() {
		return "Group [id=" + id + ", header=" + header + ", subHeader1=" + subHeader1 + ", subHeader2=" + subHeader2 + ", isEnabled=" + isEnabled
				+ ", numberOfSelectedChildren=" + numberOfSelectedChildren + "]";
	}

}
