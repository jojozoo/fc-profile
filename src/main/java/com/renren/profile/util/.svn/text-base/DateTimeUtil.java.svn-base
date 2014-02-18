package com.renren.profile.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/***
 * 公共的日期处理类
 * 
 * @author wen.he1
 * 
 */
public class DateTimeUtil {

    // 默认日期格式化格式:"yyyy-MM-dd HH:mm:ss"
    private static final String DEFAULT_FORMAT     = "yyyy-MM-dd HH:mm:ss";

    private static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";

    private static Date         date               = new Date();

    private static String       dayNames[]         = { "星期日 ", "星期一 ", "星期二 ", "星期三 ", "星期四 ", "星期五 ", "星期六 " };

    private static Set<Integer> compareFields      = new TreeSet<Integer>(Arrays
                                                           .asList(Calendar.YEAR, Calendar.MONTH, Calendar.DATE,
                                                                   Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND,
                                                                   Calendar.MILLISECOND));

    /**
     * Date型日期转换成字符串，转换的格式是"yyyy-MM-dd"；如：2012-02-19
     * 
     * @param date
     *            要转换的日期类型
     * @return 返回格式化的日期字符串
     */
    public static String getSimpleDateFormat(Date date) {
        DateFormat df = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        return df.format(date);
    }

    public static List<Date> getDaysBetweenGivenRange(Date start, Date end) {
        if (start.after(end))
            throw new IllegalArgumentException("参数不合法");
        List<Date> result = new ArrayList<Date>();
        do {
            result.add(start);
            start = getDateBeforeOrAfter(start, 1);
        } while (compareAccurateToDate(start, end) <= 0);
        return result;
    }

    /***
     * 得到日期的前几天还是后几天的日期，具体由参数offset决定
     * 
     * @param date
     *            对应的日期
     * @param offset
     *            负数代表date的前几天，整数代表date的后几天
     * @return 返回一个日期型
     */
    public static Date getDateBeforeOrAfter(Date date, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, offset);
        return calendar.getTime();
    }

    /**
     * 参数start和end日期进行比较大小，比较的精度是天。
     * 
     * @param start
     * @param end
     * 
     * @return 如果start>end，整数比end早多少天；否则返回就是负数
     */
    public static int compareAccurateToDate(Date start, Date end) {
        Calendar startCalendar = getCalendar(start);
        Calendar endCalendar = getCalendar(end);
        return compareTimeAccurateTo(startCalendar, endCalendar, Calendar.DATE);
    }

    private static int compareTimeAccurateTo(Calendar startCalendar,
            Calendar endCalendar, Integer field) {
        Iterator<Integer> iter = compareFields.iterator();
        while (iter.hasNext()) {
            Integer compareField = iter.next();
            if (field.compareTo(compareField) < 0)
                break;
            int result = compareDateWithField(startCalendar, endCalendar,
                    compareField);
            if (result != 0)
                return result;
        }
        return 0;
    }

    private static int compareDateWithField(Calendar startCalendar,
            Calendar endCalendar, Integer compareField) {
        return startCalendar.get(compareField) - endCalendar.get(compareField);
    }

    private static Calendar getCalendar(Date start) {
        Calendar result = Calendar.getInstance();
        result.setTime(start);
        return result;
    }

    /**
     * 确定现在是否是上午
     * 
     * @return 不是上午返回false，否则返回true
     */
    public static boolean nowIsPM() {
        Calendar now = Calendar.getInstance();
        int amOrPM = now.get(Calendar.AM_PM);
        return amOrPM == Calendar.AM ? false : true;
    }

    /***
     * 将参数currDate字符串格式化成日期型，格式化的格式为:"yyyy-MM-dd"
     * 
     * @param currDate
     * @return 返回一个格式化的日期
     */
    public static Date formatStrToDate(String currDate) {
        return getFormatDate(currDate, "yyyy-MM-dd");
    }

    /**
     * 将参数currDate日期型格式化成字符串，格式化的格式为："yyyy-MM-dd"
     * 
     * @param currDate
     * @return 返回一个格式化的日期字符串
     */
    public static String formatDateToStr(Date currDate) {
        return getFormatDate(currDate, "yyyy-MM-dd");
    }

    /***
     * 将参数currDate日期型格式化的成日期字符串，格式化的格式：参数format决定
     * 
     * @param currDate
     *            要格式化的日期
     * @param format
     *            格式化的格式如："yyyy-MM-dd"，"yyyy-MM-dd HH:mm:ss"
     * 
     * @return 返回格式化的日期字符串
     */
    public static String getFormatDate(Date currDate, String format) {
        if (currDate == null)
            return "";
        SimpleDateFormat dtFormatdB = null;
        String string;
        try {
            dtFormatdB = new SimpleDateFormat(format);
            string = dtFormatdB.format(currDate);
        } catch (Exception e) {
            dtFormatdB = new SimpleDateFormat("yyyy-MM-dd");
            String string_0_;
            try {
                string_0_ = dtFormatdB.format(currDate);
            } catch (Exception exception) {
                return null;
            }
            return string_0_;
        }
        return string;
    }

    /**
     * 将参数currDate字符串格式化的成日期型，格式化的格式：参数format决定
     * 
     * @param currDate
     *            要格式化的日期字符串
     * @param format
     *            格式化的格式如："yyyy-MM-dd"，"yyyy-MM-dd HH:mm:ss"
     * 
     * @return 格式化的日期Date
     */
    public static Date getFormatDate(String currDate, String format) {
        if (currDate == null)
            return null;
        SimpleDateFormat dtFormatdB = null;
        Date date;
        try {
            dtFormatdB = new SimpleDateFormat(format);
            date = dtFormatdB.parse(currDate);
        } catch (Exception e) {
            dtFormatdB = new SimpleDateFormat("yyyy-MM-dd");
            Date date_2_;
            try {
                date_2_ = dtFormatdB.parse(currDate);
            } catch (Exception exception) {
                return null;
            }
            return date_2_;
        }
        return date;
    }

    public static Date getFormatDateEndfix(String dateStr) {
        dateStr = addDateEndfix(dateStr);
        return getFormatDateTime(dateStr);
    }

    public static String addDateEndfix(String datestring) {
        if (datestring == null || datestring.equals(""))
            return null;
        return new StringBuilder().append(datestring).append(" 23:59:59")
                .toString();
    }

    public static Date getFormatDateTime(String currDate) {
        return getFormatDate(currDate, DEFAULT_FORMAT);
    }

    public static String getFormatDateTime(Date currDate) {
        return getFormatDate(currDate, DEFAULT_FORMAT);
    }

    public static List<Object> getDaysListBetweenDates(Date first, Date second) {

        List<Object> dateList = new ArrayList<Object>();
        Date d1 = getFormatDate(formatDateToStr(first), "yyyy-MM-dd");
        Date d2 = getFormatDate(formatDateToStr(second), "yyyy-MM-dd");

        if (d1.compareTo(d2) > 0)
            return dateList;
        do {
            dateList.add(d1);
            d1 = getDateBeforeOrAfter(d1, 1);
        } while (d1.compareTo(d2) <= 0);

        return dateList;
    }

    public static String getCurrDateStr() {
        return formatDateToStr(getCurrDate());
    }

    public static Date getCurrDate() {
        return date;
    }

    /**
     * 指定日期str得到对应星期几
     * 
     * @param str
     * @return 返回星期几
     */
    public static String getDayOfWeek(String str) {

        Calendar calendar = Calendar.getInstance();
        Date date1 = formatStrToDate(str);

        calendar.setTime(date1);

        return dayNames[calendar.get(Calendar.DAY_OF_WEEK) - 1];
    }

    /**
     * 获取当前Q Title 2011Q1
     * 
     * @author hao.zhang
     * @return
     */
    public static String getCurrentSeason() {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        int month = now.get(Calendar.MONTH);
        int season = month % 3 > 0 ? month / 3 + 1 : month / 3;
        return "" + now.get(Calendar.YEAR) + "Q" + season;
    }
	
	/**
	 * 获取当前Q Title 2011Q1
	 * @author hao.zhang
	 * @return
	 */
	public static String getCurrentSeasonString(){
		 Calendar now = Calendar.getInstance();
         now.setTime(new Date());
         int month = now.get(Calendar.MONTH);
         int season = month % 3 > 0 ? month / 3 + 1 : month / 3;
         return "" + now.get(Calendar.YEAR) + "年Q" + season + "季度";
	}
	
	/**
	 * 获取当前Q int值
	 * @author hao.zhang
	 * @return
	 */
	public static int getIntSeason(){
		 Calendar now = Calendar.getInstance();
         now.setTime(new Date());
         int month = now.get(Calendar.MONTH);
         int season = month % 3 > 0 ? month / 3 + 1 : month / 3;
         return now.get(Calendar.YEAR) * 10  + season;
	}


    /**
     * 获取本周(周一到周日)的周一
     * 
     * @param today
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Date getMondayOfWeek(Date today) {
        Date result = new Date(today.getTime());
        int weekDay = (result.getDay() +6) % 7; // 修改为周一到周日
        result.setDate(result.getDate() - weekDay);
        return result;
    }

    /**
     * 获取本周(周一到周日)的周五
     * 
     * @param today
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Date getFridayOfWeek(Date today) {
        Date result = new Date(today.getTime());
        int weekDay = (result.getDay() +6) % 7; // 修改为周一到周日
        result.setDate(result.getDate() - weekDay + 4);
        return result;
    }

    /**
     * 获取下周(周一到周日)的周日
     * 
     * @param today
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Date getSundayOfWeek(Date today) {
        Date result = new Date(today.getTime());
        int weekDay = (result.getDay() +6) % 7; // 修改为周一到周日
        result.setDate(result.getDate() - weekDay + 6);
        return result;
    }

    public static void main(String[] args) {

        System.out.println(getDayOfWeek("2012-02-29"));
    }
}
