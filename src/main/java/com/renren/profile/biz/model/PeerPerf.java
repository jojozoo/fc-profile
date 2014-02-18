package com.renren.profile.biz.model;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.renren.profile.util.time.TimeUtils;
import com.renren.profile.util.time.TimeValidator;

public class PeerPerf {
    // -------- { Property Defines

    // 自增主键
    private int             id                   = 0;

    // 评价人id
    private int             peerId               = 0;

    // 用户是否是直属上司
    private boolean         isLeader             = false;

    // 邀请表id
    private int             invitationId         = 0;

    // 自评记录id
    private int             userPerfId           = 0;

    // 对应Q的记录id
    private int             perfTimeId           = 0;

    // 用户对此自评的评价,暂时作为经理整体考核意见了
    private String          content              = StringUtils.EMPTY;

    // 对缺点的评价
    private String          advantageComments    = StringUtils.EMPTY;

    // 对缺点的评价
    private String          disadvantageComments = StringUtils.EMPTY;

    // 是否已提交，默认为0
    private int             status               = 0;

    // 记录的编辑时间
    private Timestamp       editTime             = TimeUtils.Constant.TIME_1970;

    // 是否支持升职
    private boolean         isPromotion          = true;

    private String          promotionComment     = StringUtils.EMPTY;

    // 绩效打分（仅直属上司）,SABC
    private String          perfScore            = null;

    public static final int NOT_LEADER           = 0;
    public static final int IS_LEADER            = 1;

    // -------- } Property Defines

    // -------- { Property Getter/Setter

    /**
     * get id<br/>
     * 自增主键
     */
    public int getId() {
        return this.id;
    }

    /**
     * set id<br/>
     * 自增主键
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * get userId<br/>
     * 评价人id
     */
    public int getPeerId() {
        return this.peerId;
    }

    /**
     * set userId<br/>
     * 评价人id
     */
    public void setPeerId(int userId) {
        this.peerId = userId;
    }

    /**
     * get isLeader<br/>
     * 用户是否是直属上司
     */
    public boolean getIsLeader() {
        return this.isLeader;
    }

    /**
     * set isLeader<br/>
     * 用户是否是直属上司
     */
    public void setIsLeader(boolean isLeader) {
        this.isLeader = isLeader;
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
     * get userProjectId<br/>
     * 自评记录id
     */
    public int getUserPerfId() {
        return this.userPerfId;
    }

    /**
     * set userProjectId<br/>
     * 自评记录id
     */
    public void setUserPerfId(int userProjectId) {
        this.userPerfId = userProjectId;
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
     * 用户对此自评的评价
     */
    public String getContent() {
        return this.content;
    }

    /**
     * set content<br/>
     * 用户对此自评的评价
     */
    public void setContent(String content) {
        this.content = StringUtils.trimToEmpty(content);
    }

    /**
     * get advantageComments<br/>
     * 对缺点的评价
     */
    public String getAdvantageComments() {
        return this.advantageComments;
    }

    /**
     * set advantageComments<br/>
     * 对缺点的评价
     */
    public void setAdvantageComments(String advantageComments) {
        this.advantageComments = StringUtils.trimToEmpty(advantageComments);
    }

    /**
     * get disadvantageComments<br/>
     * 对缺点的评价
     */
    public String getDisadvantageComments() {
        return this.disadvantageComments;
    }

    /**
     * set disadvantageComments<br/>
     * 对缺点的评价
     */
    public void setDisadvantageComments(String disadvantageComments) {
        this.disadvantageComments = StringUtils.trimToEmpty(disadvantageComments);
    }

    /**
     * get status<br/>
     * 是否已提交，默认为0
     */
    public int getStatus() {
        return this.status;
    }

    /**
     * set status<br/>
     * 是否已提交，默认为0
     */
    public void setStatus(int status) {
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

    /**
     * get isPromotion<br/>
     * 是否支持升职,默认0为支持
     */
    public boolean getIsPromotion() {
        return this.isPromotion;
    }

    /**
     * set isPromotion<br/>
     * 是否支持升职,默认0为支持
     */
    public void setIsPromotion(boolean isPromotion) {
        this.isPromotion = isPromotion;
    }

    public String getPromotionComment() {
        return promotionComment;
    }

    public void setPromotionComment(String promotionComment) {
        this.promotionComment = promotionComment;
    }

    /**
     * get perfScore<br/>
     * 绩效打分（仅直属上司）,SABC
     */
    public String getPerfScore() {
        return this.perfScore;
    }

    /**
     * set perfScore<br/>
     * 绩效打分（仅直属上司）,SABC
     */
    public void setPerfScore(String perfScore) {
        this.perfScore = StringUtils.trimToNull(perfScore);
    }

    private transient List<PeerPerfProject> projects;

    public List<PeerPerfProject> projects() {
        if (projects == null) {
            return Collections.emptyList();
        }
        return projects;
    }

    public void projects(List<PeerPerfProject> projects) {
        this.projects = projects;
    }

}