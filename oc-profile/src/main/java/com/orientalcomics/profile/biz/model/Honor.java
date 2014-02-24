package com.orientalcomics.profile.biz.model;

import java.util.Date;


/**
 * @author 张浩 E-mail:hao.zhang@renren-inc.com
 * @date 2012-4-16 下午02:41:31
 *
 * 用户兑换的奖励
 */

public class Honor {
	
	private int id;
	private int userId;
	//奖励的名字
	private String honorName;
	//对应的奖励ID
	private int rewardItemId;
	//兑换奖励的日期
	private Date rewardDate;
	
	
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
	public String getHonorName() {
		return honorName;
	}
	public void setHonorName(String honorName) {
		this.honorName = honorName;
	}
	public int getRewardItemId() {
		return rewardItemId;
	}
	public void setRewardItemId(int rewardItemId) {
		this.rewardItemId = rewardItemId;
	}
	public Date getRewardDate() {
		return rewardDate;
	}
	public void setRewardDate(Date rewardDate) {
		this.rewardDate = rewardDate;
	}
	
	
}
