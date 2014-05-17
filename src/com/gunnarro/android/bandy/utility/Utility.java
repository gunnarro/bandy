package com.gunnarro.android.bandy.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Activity;
import com.gunnarro.android.bandy.domain.Team;

public class Utility {

	public static final String DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm";
	public static final String DATE_PATTERN = "dd.MM.yyyy";
	public static final String TIME_PATTERN = "hh:mm";
	public static final String DATE_DEFAULT_PATTERN = DATE_TIME_PATTERN;

	public static SimpleDateFormat dateFormatter;

	public static SimpleDateFormat getDateFormatter() {
		if (dateFormatter == null) {
			dateFormatter = new SimpleDateFormat(DATE_DEFAULT_PATTERN, Locale.UK);
		}
		return dateFormatter;
	}

	public static String capitalizationWord(String s) {
		if (s == null || s.isEmpty()) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (String w : s.toLowerCase().split(" ")) {
			sb.append(w.substring(0, 1).toUpperCase()).append(w.substring(1, w.length())).append(" ");
		}
		return sb.toString().trim();
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

	public static String formatTime(long time, String pattern) {
		String p = pattern;
		if (p == null) {
			p = DATE_DEFAULT_PATTERN;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(p, Locale.UK);
		try {
			return sdf.format(new Date(time));
		} catch (Exception e) {
			CustomLog.e(Utility.class, "Return: " + new Date(0) + ", Exception: " + e.getMessage());
			return "";
		}
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

	public static String createActivitiesHtmlTable(Team team, List<Activity> activityList) {
		// StringBuffer html = new StringBuffer();
		// html.append("<table><tr>Date</th><th>Time</th><th>Type</th><th>Description</th><th>Location</th></tr>");
		// for (Activity activity : activityList) {
		// html.append("<tr>");
		// html.append("<td>").append(activity.getStartDate()).append("</td>");
		// html.append("<td>").append(activity.getStartDate()).append("</td>");
		// html.append("<td>").append(activity.getType()).append("</td>");
		// html.append("<td>").append(activity.getDescription()).append("</td>");
		// html.append("<td>").append(activity.getPlace()).append("</td>");
		// html.append("</tr>");
		// }
		// html.append("</table>");
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

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0 || str.equals(" ");
	}
}
