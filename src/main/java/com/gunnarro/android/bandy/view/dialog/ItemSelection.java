package com.gunnarro.android.bandy.view.dialog;

import com.gunnarro.android.bandy.domain.view.list.Item;


public interface ItemSelection {
	public Item getSelectedItem();

	public String[] getSelectedItems();
	
	public Item[] getSelectedItemList();

	public int getInputFieldId();
}
