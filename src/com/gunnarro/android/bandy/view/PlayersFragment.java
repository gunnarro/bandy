package com.gunnarro.android.bandy.view;

import java.util.Date;

import android.app.Fragment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Player;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.utility.Utility;

public class PlayersFragment extends Fragment {

	protected BandyService bandyService;
	private String selectedTeamName = "%2003";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		if (container == null) {
			return null;
		}

		View view = inflater.inflate(R.layout.player_list_layout, container, false);

		Spinner teamsSpinner = (Spinner) view.findViewById(R.id.teams_spinner);
		ArrayAdapter<CharSequence> teamsAdapter = ArrayAdapter
				.createFromResource(view.getContext(), R.array.team_options, android.R.layout.simple_spinner_item);
		teamsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		teamsSpinner.setAdapter(teamsAdapter);
		teamsSpinner.setOnItemSelectedListener(new TeamOnItemSelectedListener(view));

		this.bandyService = new BandyServiceImpl(view.getContext());
		setupEventHandlers(view);
		return view;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private void setupEventHandlers(final View statView) {

	}

	private void updatePlayerList(View statView) {
		TableLayout table = (TableLayout) statView.findViewById(R.id.tableLayout);
		if (table == null) {
			return;
		}
		// Remove all rows before updating the table, except for the table
		// header rows.
		clearTableData(statView);
		Team team = this.bandyService.getTeam(selectedTeamName);
		for (Player player : team.getPlayerList()) {
			table.addView(createTableRow(statView, player, table.getChildCount()));
		}
		Utility.getDateFormatter().applyPattern("dd.MM.yyyy");
		TextView tableHeaderTxt = (TextView) statView.findViewById(R.id.tableHeaderPeriod);
		tableHeaderTxt.setText(getResources().getString(R.string.activities_period) + ": ");

	}

	private TableRow createTableRow(View statView, Player player, int rowNumber) {
		TableRow row = new TableRow(statView.getContext());
		int rowBgColor = getResources().getColor(R.color.white);
		int txtColor = getResources().getColor(R.color.black);
		row.addView(createTextView(statView, player.getFullName(), rowBgColor, txtColor, Gravity.LEFT));
		Utility.getDateFormatter().applyPattern("dd.MM.yyyy");
		row.addView(createTextView(statView, Utility.getDateFormatter().format(new Date(player.getDateOfBirth())), rowBgColor, txtColor, Gravity.CENTER));
		row.setBackgroundColor(rowBgColor);
		row.setPadding(1, 1, 1, 1);
		return row;
	}

	private TextView createTextView(View statView, String value, int bgColor, int txtColor, int gravity) {
		TextView txtView = new TextView(statView.getContext());
		txtView.setText(value);
		txtView.setGravity(gravity);
		txtView.setBackgroundColor(bgColor);
		txtView.setTextColor(txtColor);
		txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
		return txtView;
	}

	private void clearTableData(View statView) {
		TableLayout table = (TableLayout) statView.findViewById(R.id.tableLayout);
		if (table == null) {
			return;
		}
		CustomLog.i(this.getClass(), "...child: " + table.getChildCount());
		if (table.getChildCount() > 3) {
			table.removeViews(3, table.getChildCount() - 3);
			CustomLog.i(this.getClass(), "Removed rows from view: " + (table.getChildCount() - 3));
		}
	}

	/**
	 * 
	 * @author gunnarro
	 * 
	 */
	public class TeamOnItemSelectedListener implements OnItemSelectedListener {
		final View activitesView;

		public TeamOnItemSelectedListener(final View activitesView) {
			this.activitesView = activitesView;
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			selectedTeamName = parent.getItemAtPosition(pos).toString();
			// update the table upon item selection
			updatePlayerList(activitesView);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.
		}
	}
}
