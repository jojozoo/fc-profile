package com.renren.profile.util.time;

import java.util.Date;

/**
 * 
 * @author "黄兴海  xinghai.huang@xiaonei.opi.com"
 * @date 2010-12-9 下午02:50:49
 */
public class TimeValidator {
	
	public static boolean isValid(final Date date) {
		return !(date == null || date.before(TimeConstant.TIME_2000));
	}
	public static <T extends Date> T defaultIfInvalid(final T time, final T defaultValue) {
		if (isValid(time)) {
			return time;
		}
		return defaultValue;
	}
}
