package com.gunnarro.android.bandy.view.statistic;

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Season;
import com.gunnarro.android.bandy.domain.party.Player;
import com.gunnarro.android.bandy.domain.statistic.Statistic;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.utility.Utility;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

public class PlayerStatisticFragment extends Fragment {

	protected BandyService bandyService;
	private Integer clubId;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * /** {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.player_statistic_layout, container, false);
		this.bandyService = new BandyServiceImpl(getActivity());

		Team team = bandyService.getTeam(DashboardActivity.getSelectedClubId(), DashboardActivity.getSelectedTeamName(), false);
		updateData(view, team);
		return view;
	}

	private void updateData(View view, Team team) {
		TableLayout table = (TableLayout) view.findViewById(R.id.playerStatisticTableLayout);
		if (table == null) {
			return;
		}
		// Remove all rows before updating the table, except for the table
		// header rows.
		clearTableData();
		Season season = bandyService.getSeason("2013/2014");
		if (season != null && team != null) {
			List<Player> playerList = this.bandyService.getPlayerList(team.getId());
			for (Player player : playerList) {
				Statistic playerStatistic = this.bandyService.getPlayerStatistic(team.getId(), player.getId(), season.getId());
				playerStatistic.setName(player.getFullName());
				table.addView(createTableRow(playerStatistic, table.getChildCount()));
			}
		}
		TextView tableHeaderTxt = (TextView) view.findViewById(R.id.playerStatisticTableHeaderPeriod);
		tableHeaderTxt.setText("Player statistic for season: " + (season != null ? season.getPeriod() : ""));
	}

	private TableRow createTableRow(Statistic statistic, int rowNumber) {
		TableRow row = new TableRow(getActivity());
		int txtColor = getResources().getColor(R.color.black);
		int rowBgColor = getResources().getColor(R.color.tbl_row_even);
		if (rowNumber % 2 != 0) {
			rowBgColor = getResources().getColor(R.color.tbl_row_odd);
		}
		if (rowNumber == -1) {
			rowBgColor = getResources().getColor(R.color.white);
			txtColor = getResources().getColor(R.color.dark_green);
		}
		int numberColor = getResources().getColor(R.color.number);
		Utility.getDateFormatter().applyPattern("dd.MM.yyyy");

		row.addView(createTextView(statistic.getName(), rowBgColor, txtColor, Gravity.LEFT));
		row.addView(createTextView(statistic.getNumberOfPlayerMatches().toString(), rowBgColor, numberColor, Gravity.RIGHT));
		row.addView(createTextView(statistic.getNumberOfPlayerCups().toString(), rowBgColor, numberColor, Gravity.RIGHT));
		row.addView(createTextView(statistic.getNumberOfPlayerTrainings().toString(), rowBgColor, numberColor, Gravity.RIGHT));

		row.setBackgroundColor(rowBgColor);
		row.setPadding(1, 1, 1, 1);
		return row;
	}

	private TextView createTextView(String value, int bgColor, int txtColor, int gravity) {
		TextView txtView = new TextView(getActivity().getApplicationContext());
		txtView.setText(value);
		txtView.setGravity(gravity);
		txtView.setBackgroundColor(bgColor);
		txtView.setTextColor(txtColor);
		// txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		return txtView;
	}

	private void clearTableData() {
		TableLayout table = (TableLayout) getActivity().findViewById(R.id.playerStatisticTableLayout);
		if (table == null) {
			return;
		}
		CustomLog.i(this.getClass(), "...child: " + table.getChildCount());
		if (table.getChildCount() > 3) {
			table.removeViews(3, table.getChildCount() - 3);
			CustomLog.i(this.getClass(), "Removed rows from view: " + (table.getChildCount() - 3));
		}
	}

}
