package com.orientalcomics.profile.biz.logic;

import java.util.Date;

import org.apache.commons.collections.map.LRUMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.dao.WeeklyReportDAO;
import com.orientalcomics.profile.biz.model.WeeklyReport;
import com.orientalcomics.profile.constants.status.WeeklyReportStatus;
import com.orientalcomics.profile.util.time.DateTimeUtil;
import com.orientalcomics.profile.util.time.TimeFormatUtils;
import com.orientalcomics.profile.util.time.TimeUtils;

@Service
public class WeeklyReportService {
    @Autowired
    WeeklyReportDAO weeklyReportDAO;

    LRUMap          weeklyReportCache = new LRUMap(1000);

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
}
