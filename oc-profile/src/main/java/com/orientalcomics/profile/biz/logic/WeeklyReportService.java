package com.orientalcomics.profile.biz.logic;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.paoding.rose.web.Invocation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.dao.WeeklyReportCommentDAO;
import com.orientalcomics.profile.biz.dao.WeeklyReportDAO;
import com.orientalcomics.profile.biz.logic.ProfileConfigs.PageSizeConfigView;
import com.orientalcomics.profile.biz.model.WeeklyReport;
import com.orientalcomics.profile.biz.model.WeeklyReportComment;
import com.orientalcomics.profile.constants.status.WeeklyReportStatus;
import com.orientalcomics.profile.core.base.FormValidator;
import com.orientalcomics.profile.core.base.HtmlPage;
import com.orientalcomics.profile.util.time.DateTimeUtil;
import com.orientalcomics.profile.util.time.TimeFormatUtils;
import com.orientalcomics.profile.util.time.TimeHandleUtils;
import com.orientalcomics.profile.util.time.TimeParseUtils;
import com.orientalcomics.profile.util.time.TimeUtils;

@Service
public class WeeklyReportService {
    @Autowired
    WeeklyReportDAO weeklyReportDAO;
    
    @Autowired
    WeeklyReportCommentDAO weeklyReportCommentDAO;

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
    public void renderReports(Invocation inv , int ownerId, Date startMonday, Date endMonday, int curPage,int pageSize,int total) {


        List<WeeklyReport> weeklyReports = weeklyReportDAO.queryByUserBetweenDate(ownerId, startMonday, endMonday, curPage * pageSize, pageSize);
        
        if(LOG.isDebugEnabled()){
        	LOG.debug("### get reports count:"+weeklyReports.size());
        }
        
        inv.addModel("reports", weeklyReports);
        
        Collection<Integer> weeklyReportIds = Collections2.transform(weeklyReports, new Function<WeeklyReport, Integer>() {
            @Override
            public Integer apply(WeeklyReport item) {
                return item == null ? null : item.getId();
            }
        }
                );
        
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
        inv.addModel("reporsComments", weeklyReportCommentMap);
    }
}
