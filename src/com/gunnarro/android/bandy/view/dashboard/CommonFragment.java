package com.gunnarro.android.bandy.view.dashboard;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class CommonFragment extends Fragment {

	protected String teamName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setHasOptionsMenu(true);
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
		EditText inputView = (EditText) rootView.findViewById(id);
		if (inputView != null) {
			inputView.setText(value.trim());
		} else {
			CustomLog.e(this.getClass(), "No input field found for id: " + id + ", value: " + value);
		}
	}

	protected String getSelectedGender() {
		RadioButton femaleRadioBtn = (RadioButton) getView().findViewById(R.id.femaleRadioBtn);
		if (femaleRadioBtn.isSelected()) {
			return femaleRadioBtn.getText().toString();
		}
		return ((RadioButton) getView().findViewById(R.id.maleRadioBtn)).getText().toString();
	}
}
