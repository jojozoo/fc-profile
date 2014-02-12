package com.orientalcomics.profile.biz.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.orientalcomics.profile.util.time.TimeUtils;
import com.orientalcomics.profile.util.time.TimeValidator;

public class RewardFlower {

	// -------- { Property Defines

	private int id 		   = 0;
	
	private int fromId     = 0;

	private int toId       = 0;
	
	private int perfTimeId = 0;

	
	private String reason        = StringUtils.EMPTY;
	
	private int status        = 1;
	
	private String imageUrl   = StringUtils.EMPTY;
	
	private String userName       = StringUtils.EMPTY;
	
	private String receiveName       = StringUtils.EMPTY;

	private Timestamp editTime = TimeUtils.Constant.TIME_1970;

	// -------- } Property Defines

	// -------- { Property Getter/Setter
	

	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	
	/**
	 * get id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * set id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	public int getFromId() {
		return fromId;
	}

	public void setFromId(int fromId) {
		this.fromId = fromId;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getToId() {
		return toId;
	}

	public void setToId(int toId) {
		this.toId = toId;
	}

	public int getPerfTimeId() {
		return perfTimeId;
	}

	public void setPerfTimeId(int perfTimeId) {
		this.perfTimeId = perfTimeId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * get editTime
	 */
	public Timestamp getEditTime() {
		return this.editTime;
	}

	/**
	 * set editTime
	 */
	public void setEditTime(Timestamp editTime) {
		this.editTime = TimeValidator.defaultIfInvalid(editTime,
				TimeUtils.Constant.TIME_1970);
	}

	// -------- } Property Getter/Setter

}
