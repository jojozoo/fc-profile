package com.renren.profile.web.dto;


import com.renren.profile.biz.model.User;
import com.renren.profile.util.sys.status.InvitationStatus;

public class UserInviteInfoDTO {
	
	// 邀请人的状态
	private int status = InvitationStatus.ON_ACCEPT.getName();
	
	// 用户信息
	private User user = null;

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
	
	
}
