package com.orientalcomics.profile.web.dto;

import org.apache.commons.lang.StringUtils;

import com.orientalcomics.profile.biz.model.PerfReponse;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.constants.status.KpiStatus;
import com.orientalcomics.profile.constants.status.UserPerfStatus;
import com.orientalcomics.profile.constants.status.WeeklyReportStatus;

/**
 * 
 * 项目名称：renren-profile   <br>
 * 类名称：UserExtendInfoDTO <br>
 * 类描述：用户的扩展信息         <br>
 * 创建人：Administrator     <br>
 * 创建时间：2012-3-16 下午12:04:17 <br>  
 * 
 * @version
 */
public class UserExtendInfoDTO {
	
	// 基本用户信息
	private User user              = null;
	
	// 用户部门名称
	private String deparmentName   = StringUtils.EMPTY;
	
	// 周报状态
	private WeeklyReportStatus  weeklyReportStatus = null;
	
	// 自评状态
	private UserPerfStatus      userPerfStatus     = null;
	
	// Kpi状态
	private KpiStatus           kpiStatus          = null;
	
	// 是否还有下属
	private boolean testManager = false;
	
	private PerfReponse perfResponse               = null;
	
	// 用户等级名称
	private String rankName                        = StringUtils.EMPTY;
	
	// 累积积分
	private int    totalScore                      = 0;
	
	// 排名
	private int     rank                           = 0;
	
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public PerfReponse getPerfResponse() {
		return perfResponse;
	}

	public void setPerfResponse(PerfReponse perfResponse) {
		this.perfResponse = perfResponse;
	}

	public boolean getTestManager(){
		return testManager;
	}
	
	public void setTestManager(boolean testManager){
		this.testManager = testManager;
	}

	public WeeklyReportStatus getWeeklyReportStatus() {
		return weeklyReportStatus;
	}

	public void setWeeklyReportStatus(WeeklyReportStatus weeklyReportStatus) {
		this.weeklyReportStatus = weeklyReportStatus;
	}

	public UserPerfStatus getUserPerfStatus() {
		return userPerfStatus;
	}

	public void setUserPerfStatus(UserPerfStatus userPerfStatus) {
		this.userPerfStatus = userPerfStatus;
	}

	public KpiStatus getKpiStatus() {
		return kpiStatus;
	}

	public void setKpiStatus(KpiStatus kpiStatus) {
		this.kpiStatus = kpiStatus;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public String getDeparmentName() {
		return deparmentName;
	}

	public void setDeparmentName(String deparmentName) {
		this.deparmentName = deparmentName;
	}
}
