package com.renren.profile.util.time;

import java.util.Date;

public class TimeRange {
    private Date beginTime, endTime;

    /**
     * 单一时间
     * 
     * @param singleTime
     */
    public TimeRange(Date singleTime) {
        this(singleTime, singleTime);
    }

    /**
     * 时间段
     * 
     * @param beginTime
     *            null 表示负无穷。应该小于endTime（不强制）。
     * @param endTime
     *            null表示正无穷
     */
    public TimeRange(final Date beginTime, final Date endTime) {
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    /**
     * 单一时间（不是时间段）。
     * 
     * @return 如果为null，则为false。
     */
    public boolean isSingle() {
        return this.beginTime == null ? false : this.beginTime.equals(endTime);
    }

    public Date getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
