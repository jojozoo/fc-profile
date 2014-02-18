package com.renren.profile.util.time;

import java.util.Date;

import org.apache.commons.lang.time.FastDateFormat;

/**
 * 
 * @author xinghai.huang@xiaonei.opi.com
 * @date 2010-11-22 下午04:37:32
 */
public class TimeFormatUtils {
    public static final FastDateFormat ALL           = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss SSS");

    public static final FastDateFormat SIMPLE        = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

    public static final FastDateFormat DATE          = FastDateFormat.getInstance("yyyy-MM-dd");

    public static final FastDateFormat DATE_HOUR_MIN = FastDateFormat.getInstance("yyyy-MM-dd HH:mm");

    public static final FastDateFormat YEAR_MONTH    = FastDateFormat.getInstance("yyyy-MM");
    public static final FastDateFormat MONTH_DATE    = FastDateFormat.getInstance("MM-dd");
    public static final FastDateFormat HOUR_MIN      = FastDateFormat.getInstance("HH:mm");

    public static final FastDateFormat TIME          = FastDateFormat.getInstance("HH:mm:ss");

    public static final FastDateFormat DATE_TIME     = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

    public static final FastDateFormat DATE_HUMAN    = FastDateFormat.getInstance("yyyy年M月d日");

    /**
     * 
     * @param time
     *            时间
     * @return 2010-04-28 07:00:00
     */
    public static String dateTime(Date time) {
        return time == null ? null : DATE_TIME.format(time);
    }

    /**
     * 
     * @param time
     *            时间
     * @return 2010-04-28
     */
    public static String date(Date time) {
        return time == null ? null : DATE.format(time);
    }

    /**
     * 
     * @param time
     *            时间
     * @return 07:00:00
     */
    public static String time(Date time) {
        return time == null ? null : TIME.format(time);
    }

    /**
     * 
     * @param time
     *            时间
     * @return 2010年4月28日
     */
    public static String dateHuman(Date time) {
        return time == null ? null : DATE_HUMAN.format(time);
    }

    /**
     * 自动格式化时间<br/>
     * <ol>
     * <li>今日：显示“时分”
     * <li>今年：显示“月日”
     * <li>往年:如果<code>showDayIfPossible</code>为<code>true</code>
     * ，显示“年月日”，否则显示“年月”
     * </ol>
     * 
     * @param time
     *            时间
     * @param showDayIfPossible
     *            当是往年时，是否显示“日”
     * @return 格式化后的字符串
     */
    public static String auto(Date time, boolean showDayIfPossible) {
        if (time == null) {
            return null;
        }
        FastDateFormat format = null;
        Date now = TimeUtils.FetchTime.now();
        if (!TimeUtils.Compare.eqYear(time, now)) {// 往年：年月
            if (showDayIfPossible) {
                format = TimeUtils.Format.DATE;
            } else {
                format = TimeUtils.Format.YEAR_MONTH;
            }
        } else if (!TimeUtils.Compare.eqYearMonthDate(time, now)) { // 往日：月日
            format = TimeUtils.Format.MONTH_DATE;
        } else {
            format = TimeUtils.Format.HOUR_MIN; // 其他：时分
        }
        return format.format(time);
    }

    /**
     * 自动格式化时间<br/>
     * <ol>
     * <li>今日：显示“时分”
     * <li>今年：显示“月日”
     * <li>往年:显示“年月”
     * </ol>
     * 
     * @param time
     *            时间
     * @return 格式化后的字符串
     */
    public static String auto(Date time) {
        return auto(time, false);
    }

    public static String format(FormatType type, Date time) {
        if (type == null) {
            type = FormatType.DATE;
        }
        switch (type) {
            case DATE:
                return date(time);
            case TIME:
                return time(time);
            case DATE_TIME:
                return dateTime(time);
            case DATE_HUMAN:
                return dateHuman(time);
            case AUTO:
                return auto(time, false);
            case AUTO_SHOW_DAY_IF_POSSIBLE:
                return auto(time, true);
        }
        return null;
    }

    public static enum FormatType {
        DATE, //
        TIME, //
        DATE_TIME, //
        DATE_HUMAN, //
        AUTO, //
        AUTO_SHOW_DAY_IF_POSSIBLE, //
        ;
    }
}
