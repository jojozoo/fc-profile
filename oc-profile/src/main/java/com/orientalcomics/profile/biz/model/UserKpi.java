package com.orientalcomics.profile.biz.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.orientalcomics.profile.util.time.TimeUtils;

public class UserKpi {

	  // -------- { Property Defines
	  
	  // 自增id 
	  private int id = 0;
	  
	  // 对应user的Id 
	  private int userId = 0;
	  
	  // 绩效考核id
	  private int perfTimeId = 0;
	  
	  // kpi中项目设置的目标
	  private String target = StringUtils.EMPTY;
	  
	  // 衡量标准和行动计划
	  private String standardPlan = StringUtils.EMPTY;
	  
	  
	  private float weight = 0.0f;
	  
	  // 自己完成目标的结果或完成情况
	  private String actionResult = StringUtils.EMPTY;
	  
	  // 自评项目分数
	  private float selfScore = 0.0f;
	  
	  // 领导评价项目的分数
	  private float leaderScore = 0.0f;
	  
	  // 1:自己kpi项目是保存；2：自己kpi提交且自评分已经打完；3：自己主管对下属kpi项目的评分
	  private int status = 0;
	  
	  // 编辑时间
	  private Timestamp editTime = TimeUtils.Constant.TIME_1970;
	  
	  private String name = StringUtils.EMPTY;
	  
	  // -------- { Property Getter/Setter
	  
	  public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPerfTimeId() {
		return perfTimeId;
	}

	public void setPerfTimeId(int perfTimeId) {
		this.perfTimeId = perfTimeId;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getStandardPlan() {
		return standardPlan;
	}

	public void setStandardPlan(String standardPlan) {
		this.standardPlan = standardPlan;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public String getActionResult() {
		return actionResult;
	}

	public void setActionResult(String actionResult) {
		this.actionResult = actionResult;
	}

	public float getSelfScore() {
		return selfScore;
	}

	public void setSelfScore(float selfScore) {
		this.selfScore = selfScore;
	}

	public float getLeaderScore() {
		return leaderScore;
	}

	public void setLeaderScore(float leaderScore) {
		this.leaderScore = leaderScore;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Timestamp getEditTime() {
		return editTime;
	}

	public void setEditTime(Timestamp editTime) {
		this.editTime = editTime;
	}

	/**
	   * get id<br/>
	   * 自增id
	   */
	  public int getId (){
	    return this.id;
	  }
	  
	  /**
	   * set id<br/>
	   * 自增id
	   */
	  public void setId (int id){
	    this.id = id;
	  }
	  
	  /**
	   * get userId<br/>
	   * 对应user的Id
	   */
	  public int getUserId (){
	    return this.userId;
	  }
	  
	  /**
	   * set userId<br/>
	   * 对应user的Id
	   */
	  public void setUserId (int userId){
	    this.userId = userId;
	  }
	  
	  
	  // -------- } Property Getter/Setter
	  


}
