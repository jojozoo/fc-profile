package com.orientalcomics.profile.biz.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.orientalcomics.profile.util.time.TimeValidator;


public class BusinessTag {
    // -------- { Property Defines

    private int       id         = 0;

    // 业务标签
    private String    tagName    = StringUtils.EMPTY;

    // 信息编辑时间
    private Timestamp editTime   = null;

    // 当前负责人
    private int       chargeUser = 0;

    // -------- } Property Defines

    // -------- { Property Getter/Setter

    /**
     * get id
     */
    public int getId() {
        return this.id;
    }

    /**
     * set id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * get tagName<br/>
     * 业务标签
     */
    public String getTagName() {
        return this.tagName;
    }

    /**
     * set tagName<br/>
     * 业务标签
     */
    public void setTagName(String tagName) {
        this.tagName = StringUtils.trimToEmpty(tagName);
    }

    /**
     * get editTime<br/>
     * 信息编辑时间
     */
    public Timestamp getEditTime() {
        return this.editTime;
    }

    /**
     * set editTime<br/>
     * 信息编辑时间
     */
    public void setEditTime(Timestamp editTime) {
        this.editTime = TimeValidator.defaultIfInvalid(editTime, null);
    }

    /**
     * get chargeUser<br/>
     * 当前负责人
     */
    public int getChargeUser() {
        return this.chargeUser;
    }

    /**
     * set chargeUser<br/>
     * 当前负责人
     */
    public void setChargeUser(int chargeUser) {
        this.chargeUser = chargeUser;
    }

    // -------- } Property Getter/Setter
    @Override
    public String toString() {
        return this.tagName;
    }
}