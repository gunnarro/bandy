package com.gunnarro.android.bandy.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Activity;
import com.gunnarro.android.bandy.domain.Team;

public class Utility {

	public static SimpleDateFormat dateFormatter;

	public static SimpleDateFormat getDateFormatter() {
		if (dateFormatter == null) {
			dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.UK);
		}
		return dateFormatter;
	}

	public static String formatTime(int hour, int minute) {
		StringBuffer time = new StringBuffer();
		time.append(padTime(hour)).append(":").append(padTime(minute));
		return time.toString();
	}

	/** Add padding to numbers less than ten */
	private static String padTime(int n) {
		if (n >= 10) {
			return String.valueOf(n);
		} else {
			return "0" + String.valueOf(n);
		}
	}

	/**
	 * Extract hour part from time pattern HH:mm
	 * 
	 * @param time
	 * @return
	 */
	public static int getHour(String time) {
		if (time.isEmpty() || time.split(":").length != 2) {
			return 0;
		}
		return Integer.valueOf(time.split(":")[0]);
	}

	/**
	 * Extract hour part from time pattern HH:mm
	 * 
	 * @param time
	 * @return
	 */
	public static int getMinute(String time) {
		if (time.isEmpty() || time.split(":").length != 2) {
			return 0;
		}
		return Integer.valueOf(time.split(":")[1]);
	}

	public static String createSearch(String value) {
		if (value == null || value.isEmpty()) {
			return "";
		}
		String filter = "^" + value.replace("*", "") + ".*";
		if (value.startsWith("+")) {
			filter = "^\\" + value.replace("*", "") + ".*";
		} else if (value.startsWith("hidden")) {
			filter = "[0-9,+]{8,19}";
		}
		return filter;
	}

	public static Date timeToDate(String time, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.UK);
		try {
			return sdf.parse(time);
		} catch (ParseException e) {
			CustomLog.e(Utility.class, "Return: " + new Date(0) + ", Exception: " + e.getMessage());
			return new Date(0);
		}
	}

	public static boolean isInActiveTimePeriode(String fromTime, String toTime) {
		if (fromTime.isEmpty() || toTime.isEmpty()) {
			// time period not set, return true
			return true;
		}
		Calendar currentTime = Calendar.getInstance();
		Calendar fromTimeCal = Calendar.getInstance();
		Calendar toTimeCal = Calendar.getInstance();
		fromTimeCal.setTime(Utility.timeToDate(fromTime, "HH:mm"));
		toTimeCal.setTime(Utility.timeToDate(toTime, "HH:mm"));
		if (currentTime.after(toTimeCal) && currentTime.before(fromTimeCal)) {
			return true;
		}
		// if ((currentTime.get(Calendar.HOUR_OF_DAY) >
		// fromTimeCal.get(Calendar.HOUR_OF_DAY) &&
		// currentTime.get(Calendar.MINUTE) > fromTimeCal
		// .get(Calendar.MINUTE))
		// && (currentTime.get(Calendar.HOUR_OF_DAY) <
		// toTimeCal.get(Calendar.HOUR_OF_DAY) &&
		// currentTime.get(Calendar.MINUTE) < toTimeCal
		// .get(Calendar.MINUTE))) {
		// return true;
		// }
		return false;
	}

	public static String createActivitiesHtmlTable(Team team, List<Activity> activityList) {
//		StringBuffer html = new StringBuffer();
//		html.append("<table><tr>Date</th><th>Time</th><th>Type</th><th>Description</th><th>Location</th></tr>");
//		for (Activity activity : activityList) {
//			html.append("<tr>");
//			html.append("<td>").append(activity.getStartDate()).append("</td>");
//			html.append("<td>").append(activity.getStartDate()).append("</td>");
//			html.append("<td>").append(activity.getType()).append("</td>");
//			html.append("<td>").append(activity.getDescription()).append("</td>");
//			html.append("<td>").append(activity.getPlace()).append("</td>");
//			html.append("</tr>");
//		}
//		html.append("</table>");
		// return html.toString();

		StringBuffer plain = new StringBuffer();
		for (Activity activity : activityList) {
			getDateFormatter().applyPattern("dd.MM.yyyy");
			plain.append(getDateFormatter().format(activity.getStartDate())).append("\t");
			getDateFormatter().applyPattern("HH:mm");
			plain.append(getDateFormatter().format(activity.getStartDate())).append("\t");
			plain.append(activity.getType()).append("\t");
			plain.append(activity.getDescription()).append("\t");
			plain.append(activity.getPlace()).append("\n");
		}
		return plain.toString();
	}
}
