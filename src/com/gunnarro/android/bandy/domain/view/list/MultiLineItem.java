package com.gunnarro.android.bandy.domain.view.list;

/**
 * Class for holding match list items.
 * 
 * @author gunnarro
 * 
 */
public class MultiLineItem extends Item {

	private String subHeader1;
	private String subHeader2;

	public MultiLineItem(Integer id, String header, String subHeader1, String subHeader2, boolean isEnabled) {
		super(id, header, isEnabled);
		this.subHeader1 = subHeader1;
		this.subHeader2 = subHeader2;
	}

	public String getSubHeader1() {
		return subHeader1;
	}

	public String getSubHeader2() {
		return subHeader2;
	}

}
