package com.gunnarro.android.bandy.view.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;

public class CommonFragment extends Fragment {

	protected String clubName;
	protected String teamName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setHasOptionsMenu(true);
		if (getArguments().containsKey(DashboardActivity.ARG_CLUB_NAME)) {
			clubName = getArguments().getString(DashboardActivity.ARG_CLUB_NAME);
		}
		if (getArguments().containsKey(DashboardActivity.ARG_TEAM_NAME)) {
			teamName = getArguments().getString(DashboardActivity.ARG_TEAM_NAME);
		}
	}

	protected String getInputValue(int id) {
		EditText inputView = (EditText) getView().findViewById(id);
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
				femaleRadioBtn.setChecked(true);
				maleRadioBtn.setChecked(false);
			} else {
				femaleRadioBtn.setChecked(false);
				maleRadioBtn.setChecked(true);
			}
		}
	}
}
