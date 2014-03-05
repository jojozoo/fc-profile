package com.orientalcomics.profile.biz.logic;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orientalcomics.profile.biz.dao.KpiDAO;
import com.orientalcomics.profile.biz.dao.UserInvitationDAO;
import com.orientalcomics.profile.biz.dao.UserPerfDAO;
import com.orientalcomics.profile.biz.dao.WeeklyReportDAO;
import com.orientalcomics.profile.biz.model.Kpi;
import com.orientalcomics.profile.biz.model.PerfTime;
import com.orientalcomics.profile.biz.model.UserInvitation;
import com.orientalcomics.profile.biz.model.UserPerf;
import com.orientalcomics.profile.biz.model.WeeklyReport;
import com.orientalcomics.profile.constants.status.KpiStatus;
import com.orientalcomics.profile.constants.status.PerfTimeStatus;
import com.orientalcomics.profile.constants.status.UserInvitationStatus;
import com.orientalcomics.profile.constants.status.UserPeerPerfStatus;
import com.orientalcomics.profile.constants.status.UserPerfStatus;
import com.orientalcomics.profile.constants.status.WeeklyReportStatus;
import com.orientalcomics.profile.util.logging.ILogger;
import com.orientalcomics.profile.util.logging.ProfileLogger;
import com.orientalcomics.profile.util.time.DateTimeUtil;


/**
 * @author 张浩 E-mail:hao.zhang@renren-inc.com
 * @date 2012-3-21 上午10:51:48
 * 
 *       状态装饰类
 */

@Service
public final class StatusService {

    private final ILogger     log = ProfileLogger.getLogger(this.getClass());

    @Autowired
    private PerfTimeService   perfTimeService;

    @Autowired
    private UserPerfDAO       userSelfPerfDAO;

    @Autowired
    private UserInvitationDAO userInvitationDAO;

    @Autowired
    private WeeklyReportDAO   weeklyReportDAO;

    @Autowired
    private KpiDAO            kpiDAO;


    /**
     * 获取当前绩效考核的状态
     * 
     * @return {@link PerfTimeStatus}
     */
    public PerfTimeStatus getCurrentPerfStatus() {

        try {
            PerfTime perfTime = perfTimeService.getCurrent();
            if (perfTime == null) {
                return PerfTimeStatus.READY;
            }
            return PerfTimeStatus.findById(perfTime.getStatus());
        } catch (Exception e) {
            log.error(e, e.getMessage());
        }
        return PerfTimeStatus.READY;

    }

    /**
     * 获取用户当前绩效自评的状态
     * 
     * @param userId
     * @return {@link UserPerfStatus}
     */
    public UserPerfStatus getUserSelfPerfStatus(int userId) {

        try {
            PerfTime perfTime = perfTimeService.getCurrent();
            if (perfTime == null) {
                return UserPerfStatus.READY;
            }
            UserPerf userSelfPerf = userSelfPerfDAO.queryByUserIdAndPerfTimeID(userId, perfTime.getId());
            if (userSelfPerf == null) {
                return UserPerfStatus.READY;
            }
            return UserPerfStatus.findById(userSelfPerf.getStatus());
        } catch (Exception e) {
            log.error(e, e.getMessage());
        }
        return UserPerfStatus.READY;
    }
    
    /**
     * 返回用户对别人邀请的状态区别在于没有小于3的判断
     * 
     * @param userId
     * @return {@link UserInvitationStatus}
     */
    public UserInvitationStatus getUserInvitationStatusContainStatus(int userId) {

        PerfTime perfTime = perfTimeService.getCurrent();
        if (perfTime == null) {
            return UserInvitationStatus.READY;
        }
        List<UserInvitation> invitations = userInvitationDAO.queryInvitationInfo(userId, perfTime.getId());

        if (invitations == null) {
            return UserInvitationStatus.NO_INVITATIONS;
        }
        if (invitations.size() == 0) {
            return UserInvitationStatus.NO_INVITATIONS;
        }
        for (UserInvitation userInvitation : invitations) {
            if (userInvitation.getStatus() != UserInvitationStatus.PEER_SUBMIT.getId()) {
                return UserInvitationStatus.ON_INVITATIONS;
            }
        }
        return UserInvitationStatus.COMPLETE_INVITATIONS;
    }

    /**
     * 返回用户对别人邀请的状态
     * 
     * @param userId
     * @return {@link UserInvitationStatus}
     */
    public UserInvitationStatus getUserInvitationStatus(int userId) {

        PerfTime perfTime = perfTimeService.getCurrent();
        if (perfTime == null) {
            return UserInvitationStatus.READY;
        }
        List<UserInvitation> invitations = userInvitationDAO.queryInvitationInfo(userId, perfTime.getId());

        if (invitations == null) {
            return UserInvitationStatus.NO_INVITATIONS;
        }
        if (invitations.size() == 0) {
            return UserInvitationStatus.NO_INVITATIONS;
        }
        if (invitations.size() < 3) {
            return UserInvitationStatus.ON_INVITATIONS;
        }
        for (UserInvitation userInvitation : invitations) {
            if (userInvitation.getStatus() == UserInvitationStatus.PEER_NOT_SUBMIT.getId()) {
                return UserInvitationStatus.ON_INVITATIONS;
            }
        }
        return UserInvitationStatus.COMPLETE_INVITATIONS;
    }

    /**
     * 我的互评任务状态
     * 
     * @param userId
     * @return {@link UserPeerPerfStatus}
     */
    public UserPeerPerfStatus getUserPeerPerfStatus(int userId) {

        PerfTime perfTime = perfTimeService.getCurrent();
        if (perfTime == null) {
            return UserPeerPerfStatus.READY;
        }
        List<UserInvitation> invitations = userInvitationDAO.queryAccessInfo(userId, perfTime.getId());

        if (invitations == null) {
            return UserPeerPerfStatus.NO_INVITATIONS;
        }
        if (invitations.size() == 0) {
            return UserPeerPerfStatus.NO_INVITATIONS;
        }
//        if (invitations.size() < 3) {
//            return UserPeerPerfStatus.ON_INVITATIONS;
//        }
//        for (UserInvitation userInvitation : invitations) {
//            if (userInvitation.getStatus() == UserPeerPerfStatus.SUBMITTED.getId()) {
//                return UserPeerPerfStatus.ON_INVITATIONS;
//            }
//        }
        for(UserInvitation userInvitation : invitations){
        	if(userInvitation.getStatus() == UserPeerPerfStatus.READY.getId() ||
        			userInvitation.getStatus() == UserPeerPerfStatus.SAVED.getId() )
        		 return UserPeerPerfStatus.ON_INVITATIONS;
        }
        
        return UserPeerPerfStatus.COMPLETE_INVITATIONS;

    }

    /**
     * 返回用户的周报状态，当前
     * 
     * @param userId
     * 
     * @return
     */
    public WeeklyReportStatus getUserWeeklyReportStatus(int userId) {

        Date queryDate = DateTimeUtil.compareAccurateToDate(new Date(), DateTimeUtil.getFridayOfWeek(new Date())) > 0 ? DateTimeUtil
                .getMondayOfLastWeek(new Date()) : DateTimeUtil.getMondayOfWeek(new Date());

        try {

            WeeklyReport report = weeklyReportDAO.getReportOfWeek(userId, queryDate);
            if (report != null)
                return report.status();

        } catch (Exception e) {

            log.error(e, e.getMessage());

        }

        return WeeklyReportStatus.READY;

    }

    /**
     * 返回用户的自评状态，当前
     * 
     * @param userId
     * 
     * @return
     */
    public UserPerfStatus getUserPerfStatus(int userId) {

        PerfTime perfTime = perfTimeService.queryLastest();
        
        if(perfTime == null){
        	return UserPerfStatus.READY;
        }
        
        try {

            UserPerf userPerf = userSelfPerfDAO.queryByUserIdAndPerfTimeID(userId, perfTime.getId());
            if (userPerf != null)
                return userPerf.status();

        } catch (Exception e) {

            log.error(e, e.getMessage());

        }

        return UserPerfStatus.READY;

    }

    /**
     * 返回集合内用户的KPI状态，当前
     * 
     * @param userId
     * 
     * @return
     */
    public KpiStatus getUserKpiStatus(int userId) {

        int quarterTime = DateTimeUtil.getIntSeason();

        try {

            Kpi kpi = kpiDAO.queryByQuarterTime(userId, quarterTime);
            if (kpi != null)
                return kpi.status();

        } catch (Exception e) {

            log.error(e, e.getMessage());

        }

        return KpiStatus.READY;

    }

    public static void main(String[] args) {
        Date queryDate = DateTimeUtil.compareAccurateToDate(new Date(), DateTimeUtil.getFridayOfWeek(new Date())) < 0 ? DateTimeUtil
                .getMondayOfLastWeek(new Date()) : DateTimeUtil.getMondayOfWeek(new Date());

        System.out.println(DateTimeUtil.getSimpleDateFormat(queryDate));

        System.out.println(DateTimeUtil.compareAccurateToDate(new Date(), DateTimeUtil.getFridayOfWeek(new Date())));
    }

}
