package com.orientalcomics.profile.biz.model;

import org.apache.commons.lang.StringUtils;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.base.DepartmentAssignable;


public class User implements DepartmentAssignable {
    // -------- { Property Defines

    // 自增id
    private int    id                  = 0;

    // oa系统id
    private String oaId                = null;

    // 名字
    private String name                = StringUtils.EMPTY;

    // 工号
    private String number              = StringUtils.EMPTY;

    // 该信息是否可用（离职，调离技术部等）
    private int    status              = 0;

    // 权限级别（默认为普通员工权限）
    private int    pemissionLevel      = 0;

    // 职称（初始根据OA来）
    private String jobTitle            = null;

    // 级别(评级)
    private String level               = null;

    // 是否显示个人职称等级
    private int    showLevel           = 0;

    // 所属部门ID
    private int    departmentId        = 0;

    // 直属上司userid
    private int    managerId           = 0;

    // 用户邮件
    private String email               = StringUtils.EMPTY;

    private String tinyUrl             = StringUtils.EMPTY;

    private String mainUrl             = StringUtils.EMPTY;

    // 奖励勋章的url地址
    private int    virtualRewardItemId = 0;
    
    private RewardFlower rewardFlower;
    
    private UserProfile userProfile;
    
    private int kpiOpen   = 0;

    // -------- } Property Defines

    // -------- { Property Getter/Setter

    public int getKpiOpen() {
		return kpiOpen;
	}

	public void setKpiOpen(int kpiOpen) {
		this.kpiOpen = kpiOpen;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public RewardFlower getRewardFlower() {
		return rewardFlower;
	}

	public void setRewardFlower(RewardFlower rewardFlower) {
		this.rewardFlower = rewardFlower;
	}

	public User() {

    }

    public User(int id, String name) {
        setId(id);
        setName(name);
    }

    public User(String email, String name) {
        setEmail(email);
        setName(name);
    }

    public int getVirtualRewardItemId() {
        return virtualRewardItemId;
    }

    public void setVirtualRewardItemId(int virtualRewardItemId) {
        this.virtualRewardItemId = virtualRewardItemId;
    }

    public String getTinyUrl() {
        return tinyUrl;
    }

    public void setTinyUrl(String tinyUrl) {
        this.tinyUrl = StringUtils.trimToEmpty(tinyUrl);
    }

    public String getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = StringUtils.trimToEmpty(mainUrl);
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
        this.oaId = StringUtils.trimToNull(oaId);
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
        this.email = StringUtils.lowerCase(StringUtils.trimToEmpty(email));
    }

    public String shortEmail() {
        return StringUtils.substringBeforeLast(this.email, "@");
    }

    // -------- } Property Getter/Setter

    public String tinyUrl() {
        if (StringUtils.isBlank(tinyUrl)) {
            return OcProfileConstants.EMPTY_TINY_URL;
        }
        return this.tinyUrl;
    }

    public String mainUrl() {
        if (StringUtils.isBlank(mainUrl)) {
            return OcProfileConstants.EMPTY_MAIN_URL;
        }
        return this.mainUrl;
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

    private transient RewardItem virtualRewardItem;

    public void virtualRewardItem(RewardItem item) {
        this.virtualRewardItem = item;
    }

    public RewardItem virRewardItem() {
        return virtualRewardItem;
    }

    private transient int subordinateCount;

    public void subordinateCount(int subordinateCount) {
        this.subordinateCount = subordinateCount;
    }

    public int subordinateCount() {
        return subordinateCount;
    }
}