package com.orientalcomics.profile.biz.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.orientalcomics.profile.biz.dao.PeerPerfDAO;
import com.orientalcomics.profile.biz.dao.UserInvitationDAO;
import com.orientalcomics.profile.biz.dao.UserPerfDAO;
import com.orientalcomics.profile.biz.dao.WeeklyReportDAO;
import com.orientalcomics.profile.biz.model.PeerPerf;
import com.orientalcomics.profile.biz.model.PerfTime;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.biz.model.UserPerf;
import com.orientalcomics.profile.constants.status.WeeklyReportStatus;
import com.orientalcomics.profile.util.logging.ILogger;
import com.orientalcomics.profile.util.logging.ProfileLogger;
import com.orientalcomics.profile.util.time.DateTimeUtil;
import com.orientalcomics.profile.util.time.TimeUtils;

/**
 * @author 张浩 E-mail:hao.zhang@renren-inc.com
 * @date 2012-3-21 下午06:59:59
 *
 * 邮件通知的Service
 */
@Service
public class NotifyService {
	
	private final ILogger     log = ProfileLogger.getLogger(this.getClass());
	 
	@Autowired
	private PerfTimeService perfTimeService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserPerfDAO userSelfPerfDAO;
	
	@Autowired
	private WeeklyReportDAO weeklyReportDAO;
	
	@Autowired
	private PeerPerfDAO peerPerfDAO;
	
	@Autowired 
	private UserPerfDAO userPerfDAO;
	
	
	
	@Autowired
	private UserInvitationDAO userInvitationDAO;
	 /**
     * 获取所有当前季度未提交自评的用户列表
     * @return
     */
    public List<User> getNoSelfPerfUsersForCurrentPerf(){
    	try{
    		PerfTime perfTime = perfTimeService.getCurrent();
    		Collection<Integer> perfedIds = userSelfPerfDAO.batchCollectionNoSelfPerfIds(perfTime.getId());
    		return getRestOfAllUser(perfedIds);
    		
    	}catch (Exception e) {
    		log.error(e, e.getMessage());
		}
    	return null;
    }
    
    /**
     * 返回主管没有给下属评价的主管信息<br>
     * 如果返回null，说明已经全部主管提交了对下属的评价
     * 
     * @return
     */
	public List<User> getMyReportUserForNoAccessPerf(){
    	
		// 得到所有的主管信息
    	List<User> userManagerList = userService.queryAllManager();
    	if(CollectionUtils.isEmpty(userManagerList))
    		return null;
    	
    	int perfTimeId              = perfTimeService.getCurrentId();
    	List<User> noAccessUserList = new ArrayList<User>();
    	
    	for(User managerUser : userManagerList){
    		
	    	List<User> myReportUser = userService.queryAllMyFollow(managerUser.getId());
	    	if(CollectionUtils.isEmpty(myReportUser))
	    		continue;
	    	
	        Collection<Integer> myreportUserIds = Collections2.transform(myReportUser, new Function<User, Integer>() {
	
	            @Override
	            public Integer apply(User item) {
	                return item == null ? null : item.getId();
	            }
	        });
	
	        // 查询得到我的下属自己自评已经提交的集合用户selfPerfSumbitUserIds以及用户没有提交自评或者
	        // 还未写自评信息用户selfPerfNoSumbIds;
	        Collection<UserPerf> userPerfList = userPerfDAO.batchQueryByUserIdAndPerfTimeId(perfTimeId, myreportUserIds);
	
	        // 得到所有自评提交id的集合，并且查询出领导对评价人自评信息的评价
	        Collection<Integer> selfPerfSumbitIds = Collections2.transform(userPerfList, new Function<UserPerf, Integer>() {
	
	            @Override
	            public Integer apply(UserPerf item) {
	                return item == null ? null : item.getId();
	            }
	        });
	        Collection<PeerPerf> noAccessPerfList = peerPerfDAO.batchQueryPeerPerfInfo(managerUser.getId(), perfTimeId, 1,2, selfPerfSumbitIds);
	
	        // noAccessPerfIds主管没有对下属的评价
	        Collection<Integer> noAccessPerfIds = Collections2.transform(noAccessPerfList, new Function<PeerPerf, Integer>() {
	
	            @Override
	            public Integer apply(PeerPerf item) {
	                return item == null ? null : item.getUserPerfId();
	            }
	        });
	
	
	        // 得到主管没有对下属进行评价
	        for (UserPerf userPerf : userPerfList )
	            for (int id : noAccessPerfIds) {
	                if (userPerf.getId() == id) {
	                    for (User reportUser : myReportUser)
	                        if (reportUser.getId() == userPerf.getUserId()) {
	                        	noAccessUserList.add(reportUser);
	                            break;
	                        }
	
	                    break;
	                }
	            }
    	}
    	
    	List<User> noAccessManagerUserList = new ArrayList<User>();
    	for(User noAccessUser : noAccessUserList)
    		for(User userManger : userManagerList)
    			if(userManger.getId() == noAccessUser.getManagerId()){
    				noAccessManagerUserList.add(userManger);
    				break;
    			}
    				
    			

    	return noAccessManagerUserList;
    }
    
    /**
     * 获取没有提交周报的User列表
     * @return
     */
    public List<User> getNoWeeklyReprUsersForCurrentWeek(){
    	
    	try {
			Date today = TimeUtils.FetchTime.today();// 今天
			Date monday = DateTimeUtil.getMondayOfWeek(today);// 周一
			Collection<Integer> reportedIds = weeklyReportDAO
					.getReportedIdsOfWeek(WeeklyReportStatus.SUBMITTED, monday);
			return getRestOfAllUser(reportedIds);
		} catch (Exception e) {
			log.error(e, e.getMessage());
		}
		return null;
    }
    
    /**
     * 上周没有提交周报的人
     * 
     * @return
     */
    public List<User> getNoWeeklyReprUsersForLastWeek(){
    	try {
			Date today = TimeUtils.FetchTime.today();// 今天
			Date monday = DateTimeUtil.getMondayOfLastWeek(today);// 上周周一
			Collection<Integer> reportedIds = weeklyReportDAO
					.getReportedIdsOfWeek(WeeklyReportStatus.SUBMITTED, monday);
			return getRestOfAllUser(reportedIds);
		} catch (Exception e) {
			log.error(e, e.getMessage());
		}
		return null;
    }
    /**
     * 返回当前的周报日期区间，"MM月dd日至MM月dd日
     * @return
     */
    public String getCurrentWeeklyTimeRange(){
    	Date today = TimeUtils.FetchTime.today();// 今天
		Date monday = DateTimeUtil.getMondayOfWeek(today);// 周一
		Date sunday = DateTimeUtil.getSundayOfWeek(today);
		
		return DateTimeUtil.getFormatDate(monday, "MM月dd日")+ "至" + DateTimeUtil.getFormatDate(sunday, "MM月dd日");
    }
    
    /**
     * 获取当前邀请别人对自己互评的任务未完成的用户列表
     * @return
     */
    public List<User> getNotPeerPerfsNotCompleteUser(){
    	
    	try{
	    	PerfTime perfTime = perfTimeService.getCurrent();
	        if (perfTime == null) {
	            return null;
	        }
	        Collection<Integer> userIds = userInvitationDAO.queryInvitationNotEnough(perfTime.getId());
	        Collection<Integer> enoughIds = userInvitationDAO.queryInvitationEnough(perfTime.getId());
	        Collection<Integer> notCompleteIds = userInvitationDAO.queryInvitationNotCompelete(perfTime.getId(), enoughIds);
	    	boolean added = userIds.addAll(notCompleteIds);
	    	if(added){
	    		return getRestOfAllUser(userIds);
	    	}
    	}catch (Exception e) {
    		log.error(e, e.getMessage());
    	}
    	return null;
    }
    
    
    /**
     * 获取当前别人邀请自己互评的任务，未完成的用户列表
     * @return
     */
    public List<User> getInvitedSelfNotCompleteUser(){
    	
    	try{
	    	PerfTime perfTime = perfTimeService.getCurrent();
	        if (perfTime == null) {
	            return null;
	        }
	        Collection<Integer> userIds = userInvitationDAO.queryInvitedSelfNotCompelete(perfTime.getId());
	    	return getRestOfAllUser(userIds);
    	}catch (Exception e) {
    		log.error(e, e.getMessage());
    	}
    	return null;
    }
    
    
    
    /**
     * 去除指定的集合，查询出用户列表
     * @param c
     * @return
     */
    private List<User> getRestOfAllUser(Collection<Integer> c){
    	
    	Collection<Integer> allUserIds = userService.getUserIds();
    	boolean isCollection = allUserIds.removeAll(c);
		if(isCollection){
			return userService.queryAllList(allUserIds);
		}
		else{
			return null;
		}
    }
    
    public static void main(String[] args) {
    	Date today = TimeUtils.FetchTime.today();// 今天
		Date monday = DateTimeUtil.getMondayOfLastWeek(today);// 周一
		System.out.println(DateTimeUtil.formatDateToStr(monday));
    }
    
    }