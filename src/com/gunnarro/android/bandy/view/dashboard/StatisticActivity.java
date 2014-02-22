package com.gunnarro.android.bandy.view.dashboard;

import java.util.Date;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Activity.ActivityTypeEnum;
import com.gunnarro.android.bandy.domain.statistic.MatchStatistic;
import com.gunnarro.android.bandy.domain.statistic.Statistic;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.utility.Utility;

public class StatisticActivity extends DashboardActivity {

	protected BandyService bandyService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_table_layout);
		this.bandyService = new BandyServiceImpl(getApplicationContext());
		setupEventHandlers();
		addTestData();
		updateData();
	}

	private void setupEventHandlers() {
	}

	private void updateData() {
		TableLayout table = (TableLayout) findViewById(R.id.statTableLayout);
		if (table == null) {
			return;
		}
		// Remove all rows before updating the table, except for the table
		// header rows.
		clearTableData();
		Statistic teamStatistic = this.bandyService.getTeamStatistic(1);

		Date startDate = new Date(teamStatistic.getStartTime());
		Date endDate = new Date(teamStatistic.getEndTime());
		for (MatchStatistic statistic : teamStatistic.getMatchStatisticList()) {
			table.addView(createTableRow(statistic, table.getChildCount()));
		}
		Utility.getDateFormatter().applyPattern("dd.MM.yyyy");
		String periode = Utility.getDateFormatter().format(startDate) + " - " + Utility.getDateFormatter().format(endDate);
		TextView tableHeaderTxt = (TextView) findViewById(R.id.tableHeaderPeriod);
		tableHeaderTxt.setText(getResources().getString(R.string.activities_period) + ": " + periode);
	}

	private TableRow createTableRow(MatchStatistic statistic, int rowNumber) {
		TableRow row = new TableRow(getApplicationContext());
		int rowBgColor = getResources().getColor(R.color.white);
		int txtColor = getResources().getColor(R.color.black);
		int numberColor = getActivityTypeColor(statistic.getMatchTypeId());
		Utility.getDateFormatter().applyPattern("dd.MM.yyyy");

		row.addView(createTextView(statistic.getName(), rowBgColor, txtColor, Gravity.LEFT));
		row.addView(createTextView(statistic.getPlayed().toString(), rowBgColor, txtColor, Gravity.RIGHT));
		row.addView(createTextView(statistic.getWon().toString(), rowBgColor, txtColor, Gravity.RIGHT));
		row.addView(createTextView(statistic.getDraw().toString(), rowBgColor, numberColor, Gravity.RIGHT));
		row.addView(createTextView(statistic.getLoss().toString(), rowBgColor, numberColor, Gravity.RIGHT));
		row.addView(createTextView(statistic.getGoals(), rowBgColor, numberColor, Gravity.CENTER));

		row.setBackgroundColor(rowBgColor);
		row.setPadding(1, 1, 1, 1);
		return row;
	}

	private TextView createTextView(String value, int bgColor, int txtColor, int gravity) {
		TextView txtView = new TextView(getApplicationContext());
		txtView.setText(value);
		txtView.setGravity(gravity);
		txtView.setBackgroundColor(bgColor);
		txtView.setTextColor(txtColor);
		txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
		return txtView;
	}

	private void clearTableData() {
		TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
		if (table == null) {
			return;
		}
		CustomLog.i(this.getClass(), "...child: " + table.getChildCount());
		if (table.getChildCount() > 3) {
			table.removeViews(3, table.getChildCount() - 3);
			CustomLog.i(this.getClass(), "Removed rows from view: " + (table.getChildCount() - 3));
		}
	}

	private int getActivityTypeColor(ActivityTypeEnum type) {
		switch (type) {
		case CUP:
			return getResources().getColor(R.color.dark_green);
		case MATCH:
			return getResources().getColor(R.color.dark_blue);
		case TRAINING:
			return getResources().getColor(R.color.black);
		default:
			return getResources().getColor(R.color.black);
		}
	}

	/**
	 * for unit testing only
	 */
	@Deprecated
	private void addTestData() {
	}
}
