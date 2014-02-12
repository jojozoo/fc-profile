package com.orientalcomics.profile.biz.model;

import java.util.Date;

/**
 * @author 张浩 E-mail:hao.zhang@renren-inc.com
 * @date 2012-4-18 上午11:48:34
 *
 * 类WeeklyReportComment 周报点评
 */

public class WeeklyReportComment {
	//id
	private int id;
	
	//周报ID
	private int weeklyReportId;
	
	//评论人ID
	private int commentUser;
	
	//评论人姓名
	private String commentUserName;
	
	//评论内容
	private String comment;
	
	//时间
	private Date editTime;
	
	//写周报的那周一
	private Date weekDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getWeeklyReportId() {
		return weeklyReportId;
	}
	public void setWeeklyReportId(int weeklyReportId) {
		this.weeklyReportId = weeklyReportId;
	}
	public int getCommentUser() {
		return commentUser;
	}
	public void setCommentUser(int commentUser) {
		this.commentUser = commentUser;
	}
	public String getCommentUserName() {
		return commentUserName;
	}
	public void setCommentUserName(String commentUserName) {
		this.commentUserName = commentUserName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getEditTime() {
		return editTime;
	}
	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}
	public Date getWeekDate() {
		return weekDate;
	}
	public void setWeekDate(Date weekDate) {
		this.weekDate = weekDate;
	}
	
	
}
