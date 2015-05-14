package com.qnh.weekly;

import java.util.Calendar;

/**
 * @author E.M.Micklei
 *
 */
public class Utils {
	// Converts hh,mm to minutes
	public static int displayToMinutes(String rawEntry) {
		// Accept comma and dot as separators
		String entry = rawEntry.trim();
		if (entry.length() == 0)
			return 0;
		int dotIndex = entry.indexOf(".");
		dotIndex = Math.max(dotIndex, entry.indexOf(","));
		if (dotIndex == -1)
			return Integer.valueOf(entry).intValue() * 60;
		int hours = Integer.valueOf(entry.substring(0, dotIndex)).intValue();
		int minutes = Integer.valueOf(entry.substring(dotIndex + 1, entry.length())).intValue();
		return hours * 60 + minutes;
	}
	// Converts minutes to hh,mm notation
	public static String minutesToDisplay(int minutes) {
		int hours = minutes / 60;
		int remain = minutes % 60;
		return String.valueOf(hours) + "." + remain;
	}
	public static int dayOfMonday(int year, int week, int dayOffset){
		Calendar now = Calendar.getInstance();
		now.set(Calendar.YEAR,year);
		now.set(Calendar.WEEK_OF_YEAR,week);
		int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
		now.add(Calendar.DATE, 2 - dayOfWeek + dayOffset);
		return now.get(Calendar.DAY_OF_MONTH);
	}
	public static int weekNumber(){
		return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
	}
	public static int year(){
		return Calendar.getInstance().get(Calendar.YEAR);
	}
}
