package com.gunnarro.android.bandy.view.dashboard;

import android.content.Context;
import android.view.Gravity;
import android.widget.TableRow;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.domain.activity.MatchEvent;

public class ViewUtils {

	public static TextView createTextView(Context context, String value, int bgColor, int txtColor, int gravity) {
		TextView txtView = new TextView(context);
		txtView.setText(value);
		txtView.setGravity(gravity);
		// txtView.setBackgroundColor(bgColor);
		txtView.setTextColor(txtColor);
		// txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
		return txtView;
	}

	public static TableRow createTableRow(Context context, MatchEvent event, int rowNumber) {
		TableRow row = new TableRow(context);
		int rowBgColor = context.getResources().getColor(R.color.black);
		int txtColor = context.getResources().getColor(R.color.dark_green);
		row.addView(ViewUtils.createTextView(context, event.getInfo(), rowBgColor, txtColor, Gravity.LEFT));
		row.setPadding(1, 1, 1, 1);
		return row;
	}
}
