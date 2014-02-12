package com.orientalcomics.profile.biz.model;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class UserToken {

    private int    userId = 0;
    private String token  = StringUtils.EMPTY;
    private Date   expiredTime;
    private Date   updateTime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
