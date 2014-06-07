package com.gunnarro.android.bandy.view.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.service.exception.ValidationException;
import com.gunnarro.android.bandy.utility.Validator;

public class CommonFragment extends Fragment {

	protected Integer clubId;
	protected Integer teamId;
	protected String clubName;
	protected String teamName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setHasOptionsMenu(true);
		if (getArguments().containsKey(DashboardActivity.ARG_CLUB_ID)) {
			clubId = getArguments().getInt(DashboardActivity.ARG_CLUB_ID);
		}
		if (getArguments().containsKey(DashboardActivity.ARG_TEAM_ID)) {
			teamId = getArguments().getInt(DashboardActivity.ARG_TEAM_ID);
		}
		if (getArguments().containsKey(DashboardActivity.ARG_CLUB_NAME)) {
			clubName = getArguments().getString(DashboardActivity.ARG_CLUB_NAME);
		}
		if (getArguments().containsKey(DashboardActivity.ARG_TEAM_NAME)) {
			teamName = getArguments().getString(DashboardActivity.ARG_TEAM_NAME);
		}
		getActivity().getActionBar().setTitle(clubName);
		getActivity().getActionBar().setSubtitle(teamName);
	}

	protected EditText getEditText(int id) {
		return (EditText) getView().findViewById(id);
	}

	protected String getInputValue(int id, boolean isRequired) {
		EditText inputView = getEditText(id);
		if (isRequired) {
			if (!Validator.hasText(inputView)) {
				throw new ValidationException("Invalid name!");
			}
		}
		if (inputView != null) {
			return inputView.getText().toString().trim();
		} else {
			CustomLog.e(this.getClass(), "No input field found for id: " + id);
		}
		return null;
	}

	protected void setInputValue(View rootView, int id, String value) {
		if (value != null) {
			setInputValue(rootView, id, value, true);
		}
	}

	protected void setInputValue(View rootView, int id, String value, boolean isEditable) {
		EditText inputView = (EditText) rootView.findViewById(id);
		if (inputView != null && value != null) {
			inputView.setText(value.trim());
			inputView.setEnabled(isEditable);
		} else {
			CustomLog.e(this.getClass(), "No input field found or value is equal to null, for id: " + id + ", value: " + value);
		}
	}

	protected void setTextViewValue(View rootView, int id, String value) {
		TextView textView = (TextView) rootView.findViewById(id);
		if (textView != null && value != null) {
			textView.setText(value.trim());
		} else {
			CustomLog.e(this.getClass(), "No text view found or value is equal to null, for id: " + id + ", value: " + value);
		}
	}

	protected String getSelectedGender() {
		RadioButton femaleRadioBtn = (RadioButton) getView().findViewById(R.id.femaleRadioBtn);
		if (femaleRadioBtn.isChecked()) {
			return femaleRadioBtn.getText().toString();
		}
		return ((RadioButton) getView().findViewById(R.id.maleRadioBtn)).getText().toString();
	}

	protected void setGender(View rootView, String gender) {
		if (gender != null) {
			RadioButton femaleRadioBtn = (RadioButton) rootView.findViewById(R.id.femaleRadioBtn);
			RadioButton maleRadioBtn = (RadioButton) rootView.findViewById(R.id.maleRadioBtn);
			if (gender.startsWith("F")) {
				femaleRadioBtn.setSelected(true);
				femaleRadioBtn.setChecked(true);
				maleRadioBtn.setChecked(false);
				maleRadioBtn.setSelected(false);
			} else {
				femaleRadioBtn.setSelected(false);
				femaleRadioBtn.setChecked(false);
				maleRadioBtn.setChecked(true);
				maleRadioBtn.setSelected(true);
			}
		}
	}
}
