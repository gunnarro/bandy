package com.gunnarro.android.bandy.view.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.gunnarro.android.bandy.domain.party.Role.RoleTypesEnum;

public class LeagueDialogSelection extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final List<String> mSelectedItems = new ArrayList<String>(); // Where we
																		// track
																		// the
																		// selected
																		// items
		final String[] items = RoleTypesEnum.names();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Set the dialog title
		builder.setTitle("Select League")
		// Specify the list array, the items to be selected by default (null for
		// none),
		// and the listener through which to receive callbacks when items are
		// selected
				.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int pos, boolean isChecked) {
						if (isChecked) {
							// If the user checked the item, add it to the
							// selected items
							mSelectedItems.add(items[pos]);
						} else if (mSelectedItems.contains(pos)) {
							// Else, if the item is already in the array, remove
							// it
							mSelectedItems.remove(Integer.valueOf(pos));
						}
					}
				})
				// Set the action buttons
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// User clicked OK, so save the mSelectedItems results
						// somewhere
						// or return them to the component that opened the
						// dialog
					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
					}
				});

		return builder.create();
	}
}