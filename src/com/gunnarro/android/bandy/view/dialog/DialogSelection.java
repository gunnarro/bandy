package com.gunnarro.android.bandy.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.gunnarro.android.bandy.service.exception.ApplicationException;

public class DialogSelection extends DialogFragment implements ItemSelection {

	public final static String DIALOG_ARG_ITEMS_KEY = "dialog_items";
	public final static String DIALOG_ARG_NOTICE_FIELD_ID_KEY = "notice_field_id";
	public final static String DIALOG_ARG_IS_MULTI_SELECTION_KEY = "dialog_is_multi_selection";
	private boolean isMultiSelection = false;
	private String[] items = null;
	private int noticeFieldId;
	private String[] selectedItems = new String[1];

	public String getSelectedItem() {
		if (selectedItems != null && selectedItems.length > 0) {
			return selectedItems[0];
		}
		return null;
	}

	public String[] getSelectedItems() {
		return selectedItems;
	}

	public int getInputFieldId() {
		return noticeFieldId;
	}

	/*
	 * The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks. Each method
	 * passes the DialogFragment in case the host needs to query it.
	 */
	public interface NoticeDialogListener {
		public void onDialogPositiveClick(ItemSelection dialog);

		public void onDialogNegativeClick(ItemSelection dialog);
	}

	// Use this instance of the interface to deliver action events
	private NoticeDialogListener noticeDlgListener;

	/**
	 * Default constructor
	 */
	public DialogSelection() {
	}

	// Override the Fragment.onAttach() method to instantiate the
	// NoticeDialogListener
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the
			// host
			noticeDlgListener = (NoticeDialogListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if (!getArguments().containsKey(DIALOG_ARG_ITEMS_KEY)) {
			throw new ApplicationException("Missing argument! dialog_items must be passesd in as fragment arguments.");
		}

		items = getArguments().getStringArray(DIALOG_ARG_ITEMS_KEY);
		if (items == null) {
			throw new ApplicationException("Argument equal to null! dialog_items must be passesd in as fragment arguments.");
		}

		if (!getArguments().containsKey(DIALOG_ARG_NOTICE_FIELD_ID_KEY)) {
			throw new ApplicationException("Missing argument! notice_field_id must be passesd in as fragment arguments.");
		}
		noticeFieldId = getArguments().getInt(DIALOG_ARG_NOTICE_FIELD_ID_KEY);

		if (getArguments().containsKey(DIALOG_ARG_IS_MULTI_SELECTION_KEY)) {
			isMultiSelection = true;
		}
		selectedItems = new String[items.length];
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		if (isMultiSelection) {
			boolean[] selected = new boolean[items.length];
			for (int i = 0; i < items.length - 1; i++) {
				selected[i] = false;
			}
			builder.setTitle("Selection").setMultiChoiceItems(items, selected, new DialogInterface.OnMultiChoiceClickListener() {
				public void onClick(DialogInterface dialog, int pos, boolean isChecked) {
					selectedItems[pos] = items[pos];
				}
			});

		} else {
			builder.setTitle("Selection").setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int pos) {
					selectedItems[0] = items[pos];
				}
			});
		}

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				noticeDlgListener.onDialogPositiveClick(DialogSelection.this);
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				noticeDlgListener.onDialogNegativeClick(DialogSelection.this);
			}
		});

		return builder.create();
	}
}