package com.orientalcomics.profile.biz.model;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.orientalcomics.profile.biz.base.PerfTimeAssignable;
import com.orientalcomics.profile.constants.status.UserPerfStatus;
import com.orientalcomics.profile.util.time.TimeUtils;
import com.orientalcomics.profile.util.time.TimeValidator;


public class UserPerf implements PerfTimeAssignable {
    // -------- { Property Defines

    // 自增id
    private int       id              = 0;

    // 用户id
    private int       userId          = 0;

    // 记录对应的编辑时间
    private Timestamp editTime        = TimeUtils.Constant.TIME_1970;

    // 对应Q的记录id
    private int       perfTimeId      = 0;

    // 是否已提交，保存不算
    private int       status          = 0;

    // 优点
    private String    advantage       = StringUtils.EMPTY;

    // 缺点
    private String    disadvantage    = StringUtils.EMPTY;

    // 是否提交了升职申请
    private boolean   isPromotion     = false;

    // 升职理由
    private String    promotionReason = null;

    // 用户姓名，免得每次都联查表
    private String    userName        = StringUtils.EMPTY;

    // 绩效打分（仅直属上司）,SABC
    private String    perfScore       = StringUtils.EMPTY;

    private int       leaderId        = 0;
    private String    leaderName      = StringUtils.EMPTY;
    
    private int       adjustScore     = 0;
    
    private float    leaderScore     = 0.0f;

    // -------- } Property Defines

    // -------- { Property Getter/Setter

    public float getLeaderScore() {
		return leaderScore;
	}

	public void setLeaderScore(float leaderScore) {
		this.leaderScore = leaderScore;
	}

	public int getAdjustScore() {
		return adjustScore;
	}

	public void setAdjustScore(int adjustScore) {
		this.adjustScore = adjustScore;
	}

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
     * 用户id
     */
    public int getUserId() {
        return this.userId;
    }

    /**
     * set userId<br/>
     * 用户id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * get editTime<br/>
     * 记录对应的编辑时间
     */
    public Timestamp getEditTime() {
        return this.editTime;
    }

    /**
     * set editTime<br/>
     * 记录对应的编辑时间
     */
    public void setEditTime(Timestamp editTime) {
        this.editTime = TimeValidator.defaultIfInvalid(editTime,
                TimeUtils.Constant.TIME_1970);
    }

    /**
     * get perfTimeId<br/>
     * 对应Q的记录id
     */
    public int getPerfTimeId() {
        return this.perfTimeId;
    }

    /**
     * set perfTimeId<br/>
     * 对应Q的记录id
     */
    public void setPerfTimeId(int perfTimeId) {
        this.perfTimeId = perfTimeId;
    }

    /**
     * get status<br/>
     * 是否已提交，保存不算
     */
    public int getStatus() {
        return this.status;
    }

    /**
     * set status<br/>
     * 是否已提交，保存不算
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * get advantage<br/>
     * 优点
     */
    public String getAdvantage() {
        return this.advantage;
    }

    /**
     * set advantage<br/>
     * 优点
     */
    public void setAdvantage(String advantage) {
        this.advantage = StringUtils.trimToEmpty(advantage);
    }

    /**
     * get disadvantage<br/>
     * 缺点
     */
    public String getDisadvantage() {
        return this.disadvantage;
    }

    /**
     * set disadvantage<br/>
     * 缺点
     */
    public void setDisadvantage(String disadvantage) {
        this.disadvantage = StringUtils.trimToEmpty(disadvantage);
    }

    /**
     * get isPromotion<br/>
     * 是否提交了升职申请
     */
    public boolean getIsPromotion() {
        return this.isPromotion;
    }

    /**
     * set isPromotion<br/>
     * 是否提交了升职申请
     */
    public void setIsPromotion(boolean isPromotion) {
        this.isPromotion = isPromotion;
    }

    /**
     * get promotionReason<br/>
     * 升职理由
     */
    public String getPromotionReason() {
        return this.promotionReason;
    }

    /**
     * set promotionReason<br/>
     * 升职理由
     */
    public void setPromotionReason(String promotionReason) {
        this.promotionReason = StringUtils.trimToNull(promotionReason);
    }

    /**
     * 用户姓名
     * 
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 用户姓名
     * 
     * @return
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 绩效打分
     * 
     * @return
     */
    public String getPerfScore() {
        return perfScore;
    }

    /**
     * 绩效打分
     * 
     * @return
     */
    public void setPerfScore(String perfScore) {
        this.perfScore = perfScore;
    }

    public int getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = StringUtils.trimToEmpty(leaderName);
    }

    // -------- } Property Getter/Setter

    private transient List<UserPerfProject> projects;

    public List<UserPerfProject> projects() {
        if (projects == null) {
            return Collections.emptyList();
        }
        return projects;
    }

    public void projects(List<UserPerfProject> projects) {
        this.projects = projects;
    }

    private transient PerfTime perfTime; // 考核的时间

    public int perfTimeId() {
        return perfTimeId;
    }

    public PerfTime perfTime() {
        return perfTime;
    }

    public void perfTime(PerfTime perfTime) {
        this.perfTime = perfTime;
    }

    public UserPerfStatus status() {
        return UserPerfStatus.findById(this.status);
    }

    public void status(UserPerfStatus status) {
        this.status = status == null ? 0 : status.getId();
    }
}