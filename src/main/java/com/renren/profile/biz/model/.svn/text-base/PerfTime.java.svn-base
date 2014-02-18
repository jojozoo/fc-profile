package com.renren.profile.biz.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.renren.profile.constants.status.PerfTimeStatus;
import com.renren.profile.util.time.TimeUtils;
import com.renren.profile.util.time.TimeValidator;

public class PerfTime {
    // -------- { Property Defines

    // 自增id
    private int       id          = 0;

    // 绩效周期名称 （2011Q1）
    private String    perfTitle   = StringUtils.EMPTY;

    // 本次绩效是否包含升职 0为不升职，1为包含升职
    private boolean   isPromotion = false;

    // 绩效对应的起始时间
    private Timestamp startTime   = TimeUtils.Constant.TIME_1970;

    // 绩效对应的结束时间
    private Timestamp endTime     = null;

    // 编辑的用户id
    private int       editorId    = 0;

    // 当前绩效周期状态，0:未开始.1:已开始未结束,2:已结束
    private int       status      = 0;

    // 绩效周期对应的年份
    private int       perfYear    = 1970;

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
     * get perfTitle<br/>
     * 绩效周期名称 （2011Q1）
     */
    public String getPerfTitle() {
        return this.perfTitle;
    }

    /**
     * set perfTitle<br/>
     * 绩效周期名称 （2011Q1）
     */
    public void setPerfTitle(String perfTitle) {
        this.perfTitle = StringUtils.trimToEmpty(perfTitle);
    }

    /**
     * get isPromotion<br/>
     * 本次绩效是否包含升职
     */
    public boolean getIsPromotion() {
        return this.isPromotion;
    }

    /**
     * set isPromotion<br/>
     * 本次绩效是否包含升职
     */
    public void setIsPromotion(boolean isPromotion) {
        this.isPromotion = isPromotion;
    }

    /**
     * get startTime<br/>
     * 绩效对应的起始时间
     */
    public Timestamp getStartTime() {
        return this.startTime;
    }

    /**
     * set startTime<br/>
     * 绩效对应的起始时间
     */
    public void setStartTime(Timestamp startTime) {
        this.startTime = TimeValidator.defaultIfInvalid(startTime, TimeUtils.Constant.TIME_1970);
    }

    /**
     * get endTime<br/>
     * 绩效对应的结束时间
     */
    public Timestamp getEndTime() {
        return this.endTime;
    }

    /**
     * set endTime<br/>
     * 绩效对应的结束时间
     */
    public void setEndTime(Timestamp endTime) {
        this.endTime = TimeValidator.defaultIfInvalid(endTime, null);
    }

    /**
     * get editorId<br/>
     * 编辑的用户id
     */
    public int getEditorId() {
        return this.editorId;
    }

    /**
     * set editorId<br/>
     * 编辑的用户id
     */
    public void setEditorId(int editorId) {
        this.editorId = editorId;
    }

    /**
     * 当前状态
     * 
     * @return
     */
    public int getStatus() {
        return status;
    }

    /**
     * 当前状态
     * 
     * @return
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 是否包含升职
     * 
     * @return
     */
    public void setPromotion(boolean isPromotion) {
        this.isPromotion = isPromotion;
    }

    /**
     * 绩效对应的年份
     * 
     * @return
     */
    public int getPerfYear() {
        return perfYear;
    }

    /**
     * 绩效对应的年份
     * 
     * @return
     */
    public void setPerfYear(int year) {
        this.perfYear = year;
    }

    // -------- } Property Getter/Setter
    public PerfTimeStatus status() {
        return PerfTimeStatus.findById(this.status);
    }

    public void status(PerfTimeStatus status) {
        this.status = status == null ? 0 : status.getId();
    }

}