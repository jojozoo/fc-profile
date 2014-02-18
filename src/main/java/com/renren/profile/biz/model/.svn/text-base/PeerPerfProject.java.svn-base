package com.renren.profile.biz.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.renren.profile.biz.base.PerfProjectWeightAssignable;
import com.renren.profile.constants.ProfilePerfProjectWeight;
import com.renren.profile.util.time.TimeUtils;
import com.renren.profile.util.time.TimeValidator;

public class PeerPerfProject implements PerfProjectWeightAssignable {
    // -------- { Property Defines

    // 自增id
    private int       id            = 0;

    // 评价人id
    private int       peerId        = 0;

    private int       peerPerfId    = 0;

    // 用户是否是直属上司
    private boolean   isManager     = false;

    // 邀请表id
    private int       invitationId  = 0;

    // 项目自评记录id
    private int       projectPerfId = 0;

    // 对应Q的记录id
    private int       perfTimeId    = 0;

    // 用户对此项目的评价
    private String    content       = StringUtils.EMPTY;

    // 用户对个人项目影响度的评价
    private int       weight        = 0;

    // 是否已提交，保存不算
    private boolean   status        = false;

    // 记录的编辑时间
    private Timestamp editTime      = TimeUtils.Constant.TIME_1970;

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

    public int getPeerId() {
        return this.peerId;
    }

    public void setPeerId(int peerId) {
        this.peerId = peerId;
    }

    public int getPeerPerfId() {
        return peerPerfId;
    }

    public void setPeerPerfId(int peerPerfId) {
        this.peerPerfId = peerPerfId;
    }

    /**
     * get isManager<br/>
     * 用户是否是直属上司
     */
    public boolean getIsManager() {
        return this.isManager;
    }

    /**
     * set isManager<br/>
     * 用户是否是直属上司
     */
    public void setIsManager(boolean isManager) {
        this.isManager = isManager;
    }

    /**
     * get invitationId<br/>
     * 邀请表id
     */
    public int getInvitationId() {
        return this.invitationId;
    }

    /**
     * set invitationId<br/>
     * 邀请表id
     */
    public void setInvitationId(int invitationId) {
        this.invitationId = invitationId;
    }

    /**
     * get projectPerfId<br/>
     * 项目自评记录id
     */
    public int getProjectPerfId() {
        return this.projectPerfId;
    }

    /**
     * set projectPerfId<br/>
     * 项目自评记录id
     */
    public void setProjectPerfId(int projectPerfId) {
        this.projectPerfId = projectPerfId;
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
     * get content<br/>
     * 用户对此项目的评价
     */
    public String getContent() {
        return this.content;
    }

    /**
     * set content<br/>
     * 用户对此项目的评价
     */
    public void setContent(String content) {
        this.content = StringUtils.trimToEmpty(content);
    }

    /**
     * get weight<br/>
     * 用户对个人项目影响度的评价
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * set weight<br/>
     * 用户对个人项目影响度的评价
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * get status<br/>
     * 是否已提交，保存不算
     */
    public boolean getStatus() {
        return this.status;
    }

    /**
     * set status<br/>
     * 是否已提交，保存不算
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * get editTime<br/>
     * 记录的编辑时间
     */
    public Timestamp getEditTime() {
        return this.editTime;
    }

    /**
     * set editTime<br/>
     * 记录的编辑时间
     */
    public void setEditTime(Timestamp editTime) {
        this.editTime = TimeValidator.defaultIfInvalid(editTime, TimeUtils.Constant.TIME_1970);
    }

    // -------- } Property Getter/Setter

    @Override
    public ProfilePerfProjectWeight weight() {
        return ProfilePerfProjectWeight.findById(this.weight);
    }

    @Override
    public void weight(ProfilePerfProjectWeight weight) {
        this.weight = weight == null ? 0 : weight.getId();
    }
}