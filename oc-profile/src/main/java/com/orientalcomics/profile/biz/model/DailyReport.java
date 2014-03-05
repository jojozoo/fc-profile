package com.orientalcomics.profile.biz.model; 

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.orientalcomics.profile.util.time.TimeUtils;

/** 
 * @author 张浩 E-mail:zhanghao@foundercomics.com 
 * @version 创建时间：2014年2月27日 下午5:51:15 
 * 类说明 日报的model
 */
public class DailyReport {
	// 自增id
    private int     id              = 0;

    // 对应user的Id
    private int     userId          = 0;

    // 日报对应的日期
    private Date    reportDate        = TimeUtils.Constant.TIME_1970;

    // 状态码
    private int     status          = 0;

    // Email发给谁
    private String  emailTos        = StringUtils.EMPTY;

    // 实际结果
    private String  contentDone     = StringUtils.EMPTY;

    // 工作计划
    private String  contentPlan     = StringUtils.EMPTY;

    // 遇到的问题和解决方法
    private String  qa              = StringUtils.EMPTY;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getEmailTos() {
		return emailTos;
	}

	public void setEmailTos(String emailTos) {
		this.emailTos = emailTos;
	}

	public String getContentDone() {
		return contentDone;
	}

	public void setContentDone(String contentDone) {
		this.contentDone = contentDone;
	}

	public String getContentPlan() {
		return contentPlan;
	}

	public void setContentPlan(String contentPlan) {
		this.contentPlan = contentPlan;
	}

	public String getQa() {
		return qa;
	}

	public void setQa(String qa) {
		this.qa = qa;
	}
    
    
    
}
 