package com.orientalcomics.profile.biz.model;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.orientalcomics.profile.biz.base.PerfProjectWeightAssignable;
import com.orientalcomics.profile.constants.ProfilePerfProjectWeight;
import com.orientalcomics.profile.util.time.TimeUtils;
import com.orientalcomics.profile.util.time.TimeValidator;

public class UserPerfProject implements PerfProjectWeightAssignable {
    // -------- { Property Defines

    // 自增id
    private int       id             = 0;

    // 用户id
    private int       userId         = 0;

    private int       userPerfId     = 0;

    // 项目名称
    private String    projectName    = StringUtils.EMPTY;

    // 记录对应的编辑时间
    private Timestamp editTime       = TimeUtils.Constant.TIME_1970;

    // 对应Q的记录id
    private int       perfTimeId     = 0;

    // 是否已提交，保存不算
    private int       status         = 0;

    // 用户在此项目中的角色
    private String    role           = StringUtils.EMPTY;

    // 用户在此项目中的比重（自评）
    private int       weight         = 0;

    // 用户对此项目的自评
    private String    projectContent = StringUtils.EMPTY;

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

    public int getUserPerfId() {
        return userPerfId;
    }

    public void setUserPerfId(int userPerfId) {
        this.userPerfId = userPerfId;
    }

    /**
     * get projectName<br/>
     * 项目名称
     */
    public String getProjectName() {
        return this.projectName;
    }

    /**
     * set projectName<br/>
     * 项目名称
     */
    public void setProjectName(String projectName) {
        this.projectName = StringUtils.trimToEmpty(projectName);
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
        this.editTime = TimeValidator.defaultIfInvalid(editTime, TimeUtils.Constant.TIME_1970);
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
     * get role<br/>
     * 用户在此项目中的角色
     */
    public String getRole() {
        return this.role;
    }

    /**
     * set role<br/>
     * 用户在此项目中的角色
     */
    public void setRole(String role) {
        this.role = StringUtils.trimToEmpty(role);
    }

    /**
     * get weight<br/>
     * 用户在此项目中的比重（自评）
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * set weight<br/>
     * 用户在此项目中的比重（自评）
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * get projectContent<br/>
     * 用户对此项目的自评
     */
    public String getProjectContent() {
        return this.projectContent;
    }

    /**
     * set projectContent<br/>
     * 用户对此项目的自评
     */
    public void setProjectContent(String projectContent) {
        this.projectContent = StringUtils.trimToEmpty(projectContent);
    }

    // -------- } Property Getter/Setter

    private transient List<PeerPerfProject> peerPerfProjects;
    private transient PeerPerfProject       managerPerfProject;

    public List<PeerPerfProject> peerPerfProjects() {
        return peerPerfProjects;
    }

    public void peerPerfProjects(List<PeerPerfProject> peerPerfProjects) {
        this.peerPerfProjects = peerPerfProjects;
    }

    public PeerPerfProject managerPerfProject() {
        return managerPerfProject;
    }

    public void managerPerfProject(PeerPerfProject managerPerfProject) {
        this.managerPerfProject = managerPerfProject;
    }

    @Override
    public ProfilePerfProjectWeight weight() {
        return ProfilePerfProjectWeight.findById(this.weight);
    }

    @Override
    public void weight(ProfilePerfProjectWeight weight) {
        this.weight = weight == null ? 0 : weight.getId();
    }
}