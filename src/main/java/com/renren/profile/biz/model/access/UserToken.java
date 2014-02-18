package com.renren.profile.biz.model.access;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Date;

public class UserToken implements Serializable {

	private static final long serialVersionUID = -3957476375829292798L;
	
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

    @Override
    public String toString() {
        return "UserToken{" +
                "expiredTime=" + expiredTime +
                ", userId=" + userId +
                ", token='" + token + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
