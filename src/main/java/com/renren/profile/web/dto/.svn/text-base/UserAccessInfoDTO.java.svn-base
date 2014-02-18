package com.renren.profile.web.dto;

import org.apache.commons.lang.StringUtils;

import com.renren.profile.biz.model.User;
import com.renren.profile.util.sys.status.ReportStatus;

public class UserAccessInfoDTO {
	
	// 部门名称
	private String deparmentName = StringUtils.EMPTY;
	
	// 用户状态  1：表示未开始评价；2：表示开始评价；3：表示主管已经对自己开始评价；4：表示主管已经对评价进行提交，可以查看
	private int status  = ReportStatus.ON_START.getName();
	
	private  User user = null ;
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
	public String getDeparmentName() {
		return deparmentName;
	}

	public void setDeparmentName(String deparmentName) {
		this.deparmentName = deparmentName;
	}

}
