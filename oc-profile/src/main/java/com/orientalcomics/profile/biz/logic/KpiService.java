package com.orientalcomics.profile.biz.logic;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orientalcomics.profile.biz.dao.UserKpiDAO;
import com.orientalcomics.profile.biz.model.PerfTime;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.biz.model.UserKpi;
import com.orientalcomics.profile.constants.status.KpiStatus;
import com.orientalcomics.profile.util.common.Collections0;


@Service
public class KpiService {
    
	@Autowired
	private UserKpiDAO userKpiDAO;
	
	@Autowired
	private PerfTimeService perfTimeService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 更新用户自己填写的基本信息
	 * 
	 * @param userKpi
	 * @return
	 */
	public boolean updateKpiUserBaseInfo(UserKpi userKpi){
		
		if( userKpi == null )
			return false;
		
		UserKpi kpi = userKpiDAO.queryBy(userKpi.getId());
		if( kpi != null ){
			
			return  userKpiDAO.update(userKpi) == 1 ? true : false;
			
		}else{
			
			return false;
			
		}
		
	}
	
	/**
	 * userId是否参加了perfTimeId
	 * 
	 * @param userId
	 * @param perfTimeId
	 * @return
	 */
	public boolean isHaveBy(int userId,int perfTimeId) {
		Integer count = userKpiDAO.countHavePerfTime(userId, perfTimeId);
		return count.intValue() >0 ? true : false;
	}
	
	/**
	 * 根据用户id查询当前绩效的成绩
	 * 
	 * @param userId
	 * @return
	 */
	public float getPerfTimeScore(int userId){
		
		List<UserKpi> list = userKpiDAO.queryBy(userId,perfTimeService.getCurrentId());
		float sum = 0.0f;
		if (Collections0.isEmpty(list)) {
			return sum;
		}
		for (UserKpi userKpi : list) {
			sum += userKpi.getWeight()*userKpi.getLeaderScore()/100;
		}
		
		return sum;
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public float getUserselfAccessWeight(int userId){
		
		List<UserKpi> list = userKpiDAO.queryBy(userId,perfTimeService.getCurrentId());
		float sum = 0.0f;
		if (Collections0.isEmpty(list)) {
			return sum;
		}
		for (UserKpi userKpi : list) {
			sum += userKpi.getWeight()/100;
		}
		
		return sum;
	}
	
	public UserKpi getKpiInfoBy(int id){
			
		return userKpiDAO.queryBy(id);
	}
	
	public String  getPerTimeObtainScore(int userId) {
		
		float sum = this.getPerfTimeScore(userId);
		if (sum >= 95) {
			return "S";
		}else if (sum >= 90 && sum < 95) {
			return "A";
		}else if (sum >= 70 && sum < 90) {
			return "B";
		}else {
			return "C";
		}
		
	}
	
	/***
	 * 统计我的下属kpi
	 * 
	 * @param userId
	 * @return
	 */
	public int countAll(int userId){
		
		int count = 0;
		
		List<User> userList = userService.getSubordinatesIds(userId);
		if(Collections0.isEmpty(userList))
			return count;
		
		for(User user : userList){
		    List<UserKpi> kpiList = getUserKpiInfoNotContainStatus(user.getId());
		    if(Collections0.isNotEmpty(kpiList))
		    	count++;
		}
		
		return count;
	}
	
	public boolean addKpiUserBaseInfo(UserKpi userKpi){
		
		if( userKpi == null )
			return false;
		
	    int perfTimeId = 0;
	    perfTimeId = perfTimeService.getCurrentId();
	    if (perfTimeId == 0) {
	    	PerfTime perfTime = perfTimeService.getCurrentWithoutStatus();
	    	if (perfTime != null) {
	    		perfTimeId = perfTime.getId();
	    	}
	    }
		userKpi.setPerfTimeId(perfTimeId);
		userKpi.setStatus(1);
		userKpi.setEditTime(new Timestamp(new Date().getTime()));
		
		return userKpiDAO.save(userKpi) != 0 ? true : false;
			
		
	}
	
	/**
	 * 更新kpi目标信息
	 * 
	 * @param id
	 * @param target
	 * @return
	 */
	public boolean updateKpiUserTargetInfo(int id,String target){
		
		UserKpi kpi = userKpiDAO.queryBy(id);
		if( kpi != null ){
			
			return  userKpiDAO.updateKpiTarget(id, target) == 1 ? true : false;
			
		}else{
			
			return false;
			
		}
		
	}
	
	/**
	 * 更新kpi衡量标准和行动计划信息
	 * 
	 * @param id
	 * @param plan
	 * @return
	 */
	public boolean updateKpiUserPlanInfo(int id,String plan){
		
		UserKpi kpi = userKpiDAO.queryBy(id);
		if( kpi != null ){
			
			return  userKpiDAO.updateKpiPlan(id, plan) == 1 ? true : false;
			
		}else{
			
			return false;
			
		}
		
	}
	
	/**
	 * 更新kpi权重信息
	 * 
	 * @param id
	 * @param plan
	 * @return
	 */
	public boolean updateKpiUserWeigthInfo(int id,String weigth){
		
		UserKpi kpi = userKpiDAO.queryBy(id);
		if( kpi != null ){
			
			return  userKpiDAO.updateKpiWeight(id, Float.valueOf(weigth).floatValue()) == 1 ? true : false;
			
		}else{
			
			return false;
			
		}
		
	}
	
	/**
	 * 更新用户自评信息
	 * 
	 * @param id
	 * @param result      结果/实际完成情况
	 * @param selfScore   自评分
	 * @return
	 */
	public boolean updateKpiUserSelfInfo(int id,String result,String selfScore){
		
		UserKpi kpi = userKpiDAO.queryBy(id);
		if( kpi != null ){
			
			kpi.setActionResult(result);
			kpi.setSelfScore(Float.valueOf(selfScore).floatValue());
			kpi.setStatus(2);
			
			return  userKpiDAO.updateSelfAccess(kpi) == 1 ? true : false;
			
		}else{
			
			return false;
			
		}
		
	}
	
	/**
	 * 更新用户上级评价信息
	 * 
	 * @param id
	 * @param result      结果/实际完成情况
	 * @param selfScore   自评分
	 * @return
	 */
	public boolean updateKpiUserLeaderInfo(int id,String leaderScore){
		
		UserKpi kpi = userKpiDAO.queryBy(id);
		if( kpi != null ){
			
			kpi.setLeaderScore(Float.valueOf(leaderScore).floatValue());
			kpi.setStatus(3);
			
			return  userKpiDAO.updateLeaderAccess(kpi) == 1 ? true : false;
			
		}else{
			
			return false;
			
		}
		
	}
	
	/**
	 * 删除Kpi用户的基本信息条件是指定id
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteKpiUserBaseInfo(int id){
		
		if( id == 0 )
		   return false;
		
		userKpiDAO.delete(id);
		
		return true;
		
	}
	
	/**
	 * 得到当前季度用户所有的kpi信息
	 * 
	 * @param userId
	 * @param status
	 * @return
	 */
	public List<UserKpi> getUserAllKpiInfo(int userId,int status){
		
		return userKpiDAO.queryBy(userId, perfTimeService.getCurrentId(),status);
		
	}
	
	public List<UserKpi> getUserKpiInfoBy(int userId,int perfTimeId){
		return userKpiDAO.queryBy(userId, perfTimeId);
	}
	
	
	
	/**
	 * 得到当前季度用户所有的kpi信息,不包含状态的过滤
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserKpi> getUserKpiInfoNotContainStatus(int userId){
		
		int perfTimeId = perfTimeService.getCurrentId();
		if (perfTimeId == 0) {
			PerfTime perfTime = perfTimeService.getCurrentWithoutStatus();
			if (perfTime != null) 
				perfTimeId = perfTime.getId();
		}
		
		return userKpiDAO.queryBy(userId, perfTimeId);
		
	}
	
	public KpiStatus getUserKpiStatus(int userId) {
		
		List<UserKpi> kpiList = this.getUserKpiInfoNotContainStatus(userId);
		if (Collections0.isEmpty(kpiList)) {
			return KpiStatus.READY;
		}
		
		float total = 0.0f;
		for (UserKpi kpi : kpiList) {
			total += kpi.getWeight();
		}
		
		return total >= 100.0f ? KpiStatus.SUBMITTED : KpiStatus.SAVED;
	}
	
	/**
	 * 得到当前季度用户所有的kpi信息,包含状态的过滤
	 * 
	 * @param userId
	 * @param  status
	 * @return
	 */
	public List<UserKpi> getUserKpiInfoContainStatus(int userId,int status){
		
		return userKpiDAO.queryBy(userId, perfTimeService.getCurrentId(),status);
		
	}
	
	public List<UserKpi> getUserKpiInfoContainStatusWithoutPerfTimeStatus(int userId,int status){
		int perfTimeId = perfTimeService.getCurrentId();
		if (perfTimeId == 0) {
			PerfTime perfTime = perfTimeService.getCurrentWithoutStatus();
			if (perfTime != null) 
				perfTimeId = perfTime.getId();
		}
		return userKpiDAO.queryBy(userId, perfTimeId,status);
		
	}
	
	public UserKpi getUserKpiInfoById(int id){
		
		return userKpiDAO.queryById(id);
		
	}
	
	/**
	 * <p>分页显示我的下属KPI内容返回的结果是map
	 * 
	 * <p>key:下属用户的Id；value：对应用户的kpi内容
	 * 
	 * @param userId
	 * @return
	 */
	public Map<Integer,List<UserKpi>> getUserSubordinatesKpiInfo(int userId){
		
		List<User> userList = userService.getSubordinatesIds(userId);
		if(Collections0.isEmpty(userList))
			return null;
		
		Map<Integer,List<UserKpi>> mapKpi = new HashMap<Integer,List<UserKpi>>();
		for(User user : userList){
			List<UserKpi> kpiList = getUserKpiInfoNotContainStatus(user.getId());
			if(Collections0.isNotEmpty(kpiList)){
				
				for(UserKpi userKpi : kpiList)
					userKpi.setName(user.getName());
				mapKpi.put(Integer.valueOf(user.getId()), kpiList);
				
			}
		}
		
		
		return mapKpi;
	}
	
	
	
	public List<UserKpi> getUserDownloadKpiInfo(User user){
		
		List<UserKpi> kpiList = getUserKpiInfoNotContainStatus(user.getId());
		if(Collections0.isNotEmpty(kpiList)){
			for(UserKpi userKpi : kpiList)
				userKpi.setName(user.getName());
		}
		
		return kpiList;
	}
	
}
