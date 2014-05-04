package com.gunnarro.android.bandy.view.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;

public class SelectDialogOnClickListener implements OnClickListener {
	private boolean isInitMode = true;
	private String[] items;
	private int inputFieldId;
	boolean isMultiSelection = false;
	private FragmentManager fragmentManager;

	public SelectDialogOnClickListener(FragmentManager fragmentManager, String[] items, int inputFieldId, boolean isMultiSelection) {
		this.fragmentManager = fragmentManager;
		this.items = items;
		this.inputFieldId = inputFieldId;
		this.isMultiSelection = isMultiSelection;
		isInitMode = true;
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
		DialogFragment dialogFragment = new DialogSelection();
		Bundle arguments = new Bundle();
		arguments.putStringArray(DialogSelection.DIALOG_ARG_ITEMS_KEY, items);
		arguments.putInt(DialogSelection.DIALOG_ARG_NOTICE_FIELD_ID_KEY, inputFieldId);
		if (isMultiSelection) {
			arguments.putBoolean(DialogSelection.DIALOG_ARG_IS_MULTI_SELECTION_KEY, isMultiSelection);
		}
		dialogFragment.setArguments(arguments);
		dialogFragment.show(fragmentManager, "SelectionDialogFragment");
	}
}
