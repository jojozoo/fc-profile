package com.orientalcomics.profile.biz.model;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.renren.profile.constants.status.WeeklyReportStatus;
import com.renren.profile.util.DateTimeUtil;
import com.renren.profile.util.time.TimeUtils;
import com.renren.profile.util.time.TimeValidator;

public class WeeklyReport {
    // -------- { Property Defines

    // 自增id
    private int     id              = 0;

    // 对应user的Id
    private int     userId          = 0;

    // 周报对应的星期的周一日期
    private Date    weekDate        = TimeUtils.Constant.TIME_1970;

    // 状态码
    private int     status          = 0;

    // 是补充的
    private boolean isSupplementary = false;

    // Email发给谁
    private String  emailTos        = StringUtils.EMPTY;

    // 周本已做
    private String  contentDone     = StringUtils.EMPTY;

    // 下周工作计划
    private String  contentPlan     = StringUtils.EMPTY;

    // 遇到的问题和解决方法
    private String  qa              = StringUtils.EMPTY;

    // -------- } Property Defines

    // -------- { Property Getter/Setter

    /**
     * get id<br/>
     * 自增id
     */
    public int getId() {
        return this.id;
    }

    /**
     * set id<br/>
     * 自增id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * get userId<br/>
     * 对应user的Id
     */
    public int getUserId() {
        return this.userId;
    }

    /**
     * set userId<br/>
     * 对应user的Id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * get weekDate<br/>
     * 周报对应的星期的周一日期
     */
    public Date getWeekDate() {
        return this.weekDate;
    }

    /**
     * set weekDate<br/>
     * 周报对应的星期的周一日期
     */
    public void setWeekDate(Date weekDate) {
        this.weekDate = TimeValidator.defaultIfInvalid(weekDate, TimeUtils.Constant.TIME_1970);
    }

    /**
     * get status<br/>
     * 状态码
     */
    public int getStatus() {
        return this.status;
    }

    /**
     * set status<br/>
     * 状态码
     */
    public void setStatus(int status) {
        this.status = status;
    }

    public String getEmailTos() {
        return emailTos;
    }

    public void setEmailTos(String emailTos) {
        this.emailTos = StringUtils.trimToEmpty(emailTos);
    }

    /**
     * get contentDone<br/>
     * 周本已做
     */
    public String getContentDone() {
        return this.contentDone;
    }

    /**
     * set contentDone<br/>
     * 周本已做
     */
    public void setContentDone(String contentDone) {
        this.contentDone = StringUtils.trimToEmpty(contentDone);
    }

    /**
     * get contentPlan<br/>
     * 下周工作计划
     */
    public String getContentPlan() {
        return this.contentPlan;
    }

    /**
     * set contentPlan<br/>
     * 下周工作计划
     */
    public void setContentPlan(String contentPlan) {
        this.contentPlan = StringUtils.trimToEmpty(contentPlan);
    }

    /**
     * get Qa<br/>
     * 遇到的问题和解决方法
     */
    public String getQa() {
        return qa;
    }

    /**
     * set Qa<br/>
     * 遇到的问题和解决方法
     */
    public void setQa(String qa) {
        this.qa = qa;
    }

    public boolean isSupplementary() {
        return isSupplementary;
    }

    public void setSupplementary(boolean isSupplementary) {
        this.isSupplementary = isSupplementary;
    }

    public boolean getIsSupplementary() {
        return isSupplementary;
    }

    public void setIsSupplementary(boolean isSupplementary) {
        this.isSupplementary = isSupplementary;
    }

    public Date startDate() {
        return DateTimeUtil.getMondayOfWeek(weekDate);
    }

    public Date endDate() {
        return DateTimeUtil.getSundayOfWeek(weekDate);
    }

    // -------- } Property Getter/Setter
    public WeeklyReportStatus status() {
        return WeeklyReportStatus.findById(this.status);
    }

    public void status(WeeklyReportStatus status) {
        this.status = status == null ? 0 : status.getId();
    }

}