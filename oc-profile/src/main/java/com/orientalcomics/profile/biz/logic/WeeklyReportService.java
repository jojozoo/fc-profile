package com.orientalcomics.profile.biz.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.paoding.rose.web.Invocation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.LRUMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.dao.WeeklyReportCommentDAO;
import com.orientalcomics.profile.biz.dao.WeeklyReportDAO;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.biz.model.WeeklyReport;
import com.orientalcomics.profile.biz.model.WeeklyReportComment;
import com.orientalcomics.profile.constants.status.WeeklyReportStatus;
import com.orientalcomics.profile.util.common.Collections0;
import com.orientalcomics.profile.util.time.DateTimeUtil;
import com.orientalcomics.profile.util.time.TimeFormatUtils;
import com.orientalcomics.profile.util.time.TimeUtils;

@Service
public class WeeklyReportService {
    @Autowired
    WeeklyReportDAO weeklyReportDAO;
    
    @Autowired
    WeeklyReportCommentDAO weeklyReportCommentDAO;
    
    @Autowired
    UserService userService;

    LRUMap          weeklyReportCache = new LRUMap(1000);

    private static Logger LOG = LoggerFactory.getLogger(WeeklyReportService.class);
    
    public void createEmptyReportsIfNecessary(int userId, Date monday) {
        Date thisWeekMonday = DateTimeUtil.getMondayOfWeek(new Date());
        if (monday.after(thisWeekMonday)) {
            monday = thisWeekMonday;
        }
        if (monday.before(OcProfileConstants.ONLINE_TIME)) {
            monday = DateTimeUtil.getMondayOfWeek(OcProfileConstants.ONLINE_TIME);
        }

        String key = userId + "_" + TimeFormatUtils.date(monday);
        if (weeklyReportCache.containsKey(key)) {
            return;
        }
        WeeklyReport report = weeklyReportDAO.getReportOfWeek(userId, monday);
        if (report != null) {
            weeklyReportCache.put(key, true);
            return;
        }

        // 还没有周报
        WeeklyReport emptyReport = new WeeklyReport();
        emptyReport.setUserId(userId);
        emptyReport.status(WeeklyReportStatus.READY);
        String emailTos = weeklyReportDAO.getLastestNonBlankEmailTosBefore(userId,monday);
        // 尝试插入历史空白的
        WeeklyReport lastestReport = weeklyReportDAO.getLastestReportBefore(userId, monday);
        if (lastestReport != null) {
            Date lastedWeekDate = lastestReport.getWeekDate();
            Date weekDate = DateTimeUtil.getMondayOfNextWeek(lastedWeekDate);
            while (weekDate.before(monday)) {
                emptyReport.setEmailTos(emailTos);
                emptyReport.setWeekDate(weekDate);
                weeklyReportDAO.insert(emptyReport);
                TimeUtils.Operate.addDate(weekDate, 7);// 加一周
            }
        }

        // 尝试插入本周空白的
        emptyReport.setEmailTos(emailTos);
        emptyReport.setWeekDate(monday);
        weeklyReportDAO.insert(emptyReport);
        weeklyReportCache.put(key, true);
    }

    public void clearCache(){
        weeklyReportCache.clear();;
    }
    
    
    /**
     * 加载下属周报
     * @param inv
     * @param page
     * @param ownerId
     * @param pStartDate
     * @param pEndDate
     * @param curPage
     */
    public boolean renderSubReports(Invocation inv , int ownerId, Date weekDate, int curPage,int pageSize,int total) {

        
    	List<User> subordinates = userService.querySubordinates(ownerId, curPage * pageSize, pageSize);
        if (subordinates.isEmpty()) {
        	LOG.error("没有下属信息");
            return false;
        }

        // 抽取每个用户的周报列表
        Collection<Integer> subordinateIds = Collections2.transform(subordinates, new Function<User, Integer>() {

            @Override
            public Integer apply(User user) {
                return user.getId();
            }
        });

        for (int subordinateId : subordinateIds) {
            createEmptyReportsIfNecessary(subordinateId, weekDate);
        }

        List<WeeklyReport> weeklyReports = weeklyReportDAO.queryByUserIdsInWeekDate(subordinateIds, weekDate);
        
        if(LOG.isDebugEnabled()){
        	LOG.debug("#####get sub weekly reports count:"+weeklyReports.size());
        }
        
        Map<Integer/* 用户ID */, WeeklyReport> weeklyReportMap = Collections0.packageMapByField(weeklyReports, new Function<WeeklyReport, Integer>() {

            @Override
            public Integer apply(WeeklyReport weeklyReport) {
                return weeklyReport.getUserId();
            }
        });
        
        if(LOG.isDebugEnabled()){
        	LOG.debug("#####get sub weekly reports map count:"+weeklyReportMap.size());
        }
        
        Collection<Integer> weeklyReportIds = Collections2.transform(weeklyReports, new Function<WeeklyReport, Integer>() {

            @Override
            public Integer apply(WeeklyReport weeklyReport) {
                return weeklyReport.getId();
            }
        });
        
        
        Map<Integer/* 周报的ID */, List<WeeklyReportComment>> weeklyReportCommentMap = new HashMap<Integer/* 周报的ID */, List<WeeklyReportComment>>();
        if (CollectionUtils.isNotEmpty(weeklyReportIds)) {
        	 List<WeeklyReportComment> weeklyReportComments = weeklyReportCommentDAO.queryByWeeklyReportIds(weeklyReportIds);
        	 for (WeeklyReportComment comment : weeklyReportComments) {
        		 List<WeeklyReportComment> comments = new ArrayList<WeeklyReportComment>(3); 
        		 if (weeklyReportCommentMap.containsKey(comment.getWeeklyReportId())) {
        			 weeklyReportCommentMap.get(comment.getWeeklyReportId()).add(comment);
        		 }else {
        			 comments.add(comment);
        			 weeklyReportCommentMap.put(comment.getWeeklyReportId(), comments);
        		 }
        	 }
        }
        
        inv.addModel("subordinates", subordinates);
        inv.addModel("weeklyreport_map", weeklyReportMap);
        inv.addModel("weeklyreport_comment_map", weeklyReportCommentMap);
        
        return true;
    }
}
