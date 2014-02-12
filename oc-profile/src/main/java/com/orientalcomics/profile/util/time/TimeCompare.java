package com.orientalcomics.profile.util.time;

import java.util.Date;

import com.orientalcomics.profile.util.time.TimeFetchUtils.INT;


public class TimeCompare {
	public static boolean eqYear(Date left, Date right) {
		return left == right || left.getYear() == right.getYear();
	}

	public static boolean eqMonth(Date left, Date right) {
		return left == right || left.getMonth() == right.getMonth();
	}

	public static boolean eqYearMonth(Date left, Date right) {
		return left == right || eqYear(left, right) && eqMonth(left, right);
	}

	public static boolean eqYearMonthDate(Date left, Date right) {
		return left == right || INT.days(left) == INT.days(right);
	}

	public static boolean isToday(Date date) {
		return INT.days(date) == INT.nowDays();
	}

	public static boolean isSameDay(Date left, Date right) {
		return INT.days(left) == INT.days(right);
	}
    public static boolean isSameWeek(Date left, Date right) {
        return isSameDay(DateTimeUtil.getMondayOfWeek(left), DateTimeUtil.getMondayOfWeek(right));
    }
}
