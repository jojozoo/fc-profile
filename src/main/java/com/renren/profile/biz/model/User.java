package com.renren.profile.biz.model;

import org.apache.commons.lang.StringUtils;

import com.renren.profile.RenrenProfileConstants;
import com.renren.profile.biz.base.DepartmentAssignable;

public class User implements DepartmentAssignable {
    // -------- { Property Defines

    // 自增id
    private int    id             = 0;

    // oa系统id
    private String oaId           = StringUtils.EMPTY;

    // 名字
    private String name           = StringUtils.EMPTY;

    // 工号
    private String number         = StringUtils.EMPTY;

    // 该信息是否可用（离职，调离技术部等）
    private int    status         = 0;

    // 权限级别（默认为普通员工权限）
    private int    pemissionLevel = 0;

    // 职称（初始根据OA来）
    private String jobTitle       = null;

    // 级别(评级)
    private String level          = null;

    // 是否显示个人职称等级
    private int    showLevel      = 0;

    // 所属部门ID
    private int    departmentId   = 0;

    // 直属上司userid
    private int    managerId      = 0;

    // 用户邮件
    private String email          = StringUtils.EMPTY;

    // 用户图片地址
    private String headPic        = StringUtils.EMPTY;

    // -------- } Property Defines

    // -------- { Property Getter/Setter

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
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
     * get oaId<br/>
     * oa系统id
     */
    public String getOaId() {
        return this.oaId;
    }

    /**
     * set oaId<br/>
     * oa系统id
     */
    public void setOaId(String oaId) {
        this.oaId = StringUtils.trimToEmpty(oaId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = StringUtils.trimToEmpty(name);
    }

    /**
     * get number<br/>
     * 工号
     */
    public String getNumber() {
        return this.number;
    }

    /**
     * set number<br/>
     * 工号
     */
    public void setNumber(String number) {
        this.number = StringUtils.trimToEmpty(number);
    }

    /**
     * get status<br/>
     * 该信息是否可用（离职，调离技术部等）
     */
    public int getStatus() {
        return this.status;
    }

    /**
     * set status<br/>
     * 该信息是否可用（离职，调离技术部等）
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * get pemissionLevel<br/>
     * 权限级别（默认为普通员工权限）
     */
    public int getPemissionLevel() {
        return this.pemissionLevel;
    }

    /**
     * set pemissionLevel<br/>
     * 权限级别（默认为普通员工权限）
     */
    public void setPemissionLevel(int pemissionLevel) {
        this.pemissionLevel = pemissionLevel;
    }

    /**
     * get jobTitle<br/>
     * 职称（初始根据OA来）
     */
    public String getJobTitle() {
        return this.jobTitle;
    }

    /**
     * set jobTitle<br/>
     * 职称（初始根据OA来）
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = StringUtils.trimToNull(jobTitle);
    }

    /**
     * get level<br/>
     * 级别(评级)
     */
    public String getLevel() {
        return this.level;
    }

    /**
     * set level<br/>
     * 级别(评级)
     */
    public void setLevel(String level) {
        this.level = StringUtils.trimToNull(level);
    }

    /**
     * get showLevel<br/>
     * 是否显示个人职称等级
     */
    public int getShowLevel() {
        return this.showLevel;
    }

    /**
     * set showLevel<br/>
     * 是否显示个人职称等级
     */
    public void setShowLevel(int showLevel) {
        this.showLevel = showLevel;
    }

    /**
     * get departmentId<br/>
     * 所属部门ID
     */
    public int getDepartmentId() {
        return this.departmentId;
    }

    /**
     * set departmentId<br/>
     * 所属部门ID
     */
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * get managerId<br/>
     * 直属上司userid
     */
    public int getManagerId() {
        return this.managerId;
    }

    /**
     * set managerId<br/>
     * 直属上司userid
     */
    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    /**
     * get Email
     * 
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * set email
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    // -------- } Property Getter/Setter

    public String headPic() {
        if (StringUtils.isBlank(headPic)) {
            return RenrenProfileConstants.EMPTY_HEAD_PIC_URL;
        }
        return this.headPic;
    }

    private transient Department department;
    @Override
    public int departmentId() {
        return this.departmentId;
    }
    
    @Override
    public void department(Department department) {
        this.department = department;
    }
    
    public Department department() {
        return department;
    }
}