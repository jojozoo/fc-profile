package com.orientalcomics.profile.biz.model;

import org.apache.commons.lang.StringUtils;

public class UserProfile {
    // -------- { Property Defines

    private int    userId    = 0;

    // 性别
    private int    gender    = 0;

    private int    rr        = 0;

    // QQ
    private long   qq        = 0;

    // 分机号
    private int    extNumber = 0;

    // 业余爱好
    private String hobby     = null;

    // 手机号
    private String    mobile          = StringUtils.EMPTY;
    
    private String    renrenLink      = StringUtils.EMPTY;
    
    private String    graduateSchool  = StringUtils.EMPTY;
    
    private String    birthday        = StringUtils.EMPTY;
    
    public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	private int       isDisplay       = 0;


    // -------- } Property Defines

    // -------- { Property Getter/Setter

    public String getGraduateSchool() {
		return graduateSchool;
	}

	public void setGraduateSchool(String graduateSchool) {
		this.graduateSchool = graduateSchool;
	}

	public int getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(int isDisplay) {
		this.isDisplay = isDisplay;
	}


	public String getRenrenLink() {
		return renrenLink;
	}

	public void setRenrenLink(String renrenLink) {
		this.renrenLink = renrenLink;
	}

	/**
     * get userId
     */
    public int getUserId() {
        return this.userId;
    }

    /**
     * set userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }


    /**
     * get gender<br/>
     * 性别
     */
    public int getGender() {
        return this.gender;
    }

    /**
     * set gender<br/>
     * 性别
     */
    public void setGender(int gender) {
        this.gender = gender;
    }

    /**
     * get rr
     */
    public int getRr() {
        return this.rr;
    }

    /**
     * set rr
     */
    public void setRr(int rr) {
        this.rr = rr;
    }

    /**
     * get qq<br/>
     * QQ
     */
    public long getQq() {
        return this.qq;
    }

    /**
     * set qq<br/>
     * QQ
     */
    public void setQq(long qq) {
        this.qq = qq;
    }

    /**
     * get extNumber<br/>
     * 分机号
     */
    public int getExtNumber() {
        return this.extNumber;
    }

    /**
     * set extNumber<br/>
     * 分机号
     */
    public void setExtNumber(int extNumber) {
        this.extNumber = extNumber;
    }

    /**
     * get hobby<br/>
     * 业余爱好
     */
    public String getHobby() {
        return this.hobby;
    }

    /**
     * set hobby<br/>
     * 业余爱好
     */
    public void setHobby(String hobby) {
        this.hobby = StringUtils.trimToNull(hobby);
    }

    /**
     * get mobile<br/>
     * 手机号
     */
    public String getMobile() {
        return this.mobile;
    }

    /**
     * set mobile<br/>
     * 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    // -------- } Property Getter/Setter

}