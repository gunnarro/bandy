package com.gunnarro.android.bandy.view.dialog;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;

import com.gunnarro.android.bandy.domain.view.list.Item;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl.SelectionListType;

public class SelectDialogOnClickListener implements OnClickListener {
	private static boolean isInitMode = true;
	private String[] items;
	private int inputFieldId;
	boolean isMultiSelection = false;
	private FragmentManager fragmentManager;
	private BandyService bandyService;
	private SelectionListType type;
	private Integer id;

	public SelectDialogOnClickListener(FragmentManager fragmentManager, String[] items, int inputFieldId, boolean isMultiSelection) {
		this.fragmentManager = fragmentManager;
		this.items = items;
		this.inputFieldId = inputFieldId;
		this.isMultiSelection = isMultiSelection;
		isInitMode = true;
	}

	public SelectDialogOnClickListener(FragmentManager fragmentManager, BandyService bandyService, SelectionListType type, int id, int inputFieldId,
			boolean isMultiSelection) {
		this(fragmentManager, new String[] {}, inputFieldId, isMultiSelection);
		this.bandyService = bandyService;
		this.type = type;
		this.id = id;
	}

	@Override
	public void onClick(View view) {
		if (isInitMode) {
			isInitMode = false;
		} else {
			showSelectionDialog();
		}
	}

	private void showSelectionDialog() {
		// Create an instance of the dialog fragment and show it
		if (bandyService != null && type != null) {
			items = bandyService.getSeletionList(id, type);
		}

		DialogFragment dialogFragment = new DialogSelection();
		Bundle arguments = new Bundle();
		Item[] list = new Item[] { new Item(1, "item1", false), new Item(2, "item2", false), new Item(3, "item3", false), new Item(4, "item4", false) };
		arguments.putParcelableArray(DialogSelection.DIALOG_ARG_ITEMS_KEY + "_test", list);
		arguments.putStringArray(DialogSelection.DIALOG_ARG_ITEMS_KEY, items);
		arguments.putInt(DialogSelection.DIALOG_ARG_NOTICE_FIELD_ID_KEY, inputFieldId);
		if (isMultiSelection) {
			arguments.putBoolean(DialogSelection.DIALOG_ARG_IS_MULTI_SELECTION_KEY, isMultiSelection);
		}
		dialogFragment.setArguments(arguments);
		dialogFragment.show(fragmentManager, "SelectionDialogFragment");
	}

	public static void turnOnInitMode() {
		isInitMode = true;
	}

	public static void turnOffInitMode() {
		isInitMode = false;
	}
}
