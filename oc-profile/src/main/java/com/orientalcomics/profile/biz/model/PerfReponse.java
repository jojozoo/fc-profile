package com.orientalcomics.profile.biz.model;

import org.apache.commons.lang.StringUtils;

public class PerfReponse {
	private int id;
	
	private int userId;
	
	private int selfId;
	
	private String interviewContent = StringUtils.EMPTY;
	
	private int interviewStatus = 0;
	
	private String appealContent = StringUtils.EMPTY;
	
	private int appealStatus = 0;
	
	private int perfTimeId = 0;

	public int getPerfTimeId() {
		return perfTimeId;
	}

	public void setPerfTimeId(int perfTimeId) {
		this.perfTimeId = perfTimeId;
	}

	public int getInterviewStatus() {
		return interviewStatus;
	}

	public void setInterviewStatus(int interviewStatus) {
		this.interviewStatus = interviewStatus;
	}

	public String getAppealContent() {
		return appealContent;
	}

	public void setAppealContent(String appealContent) {
		this.appealContent = appealContent;
	}

	public int getAppealStatus() {
		return appealStatus;
	}

	public void setAppealStatus(int appealStatus) {
		this.appealStatus = appealStatus;
	}

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

	public int getSelfId() {
		return selfId;
	}

	public void setSelfId(int selfId) {
		this.selfId = selfId;
	}

	public String getInterviewContent() {
		return interviewContent;
	}

	public void setInterviewContent(String interviewContent) {
		this.interviewContent = interviewContent;
	}

}
