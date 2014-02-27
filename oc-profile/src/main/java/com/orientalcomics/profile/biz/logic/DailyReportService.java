package com.orientalcomics.profile.biz.logic; 

import java.util.Date;

import org.apache.commons.collections.map.LRUMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.dao.DailyReportDAO;
import com.orientalcomics.profile.biz.model.DailyReport;
import com.orientalcomics.profile.constants.status.DailyReportStatus;
import com.orientalcomics.profile.util.time.DateTimeUtil;
import com.orientalcomics.profile.util.time.TimeFormatUtils;
import com.orientalcomics.profile.util.time.TimeUtils;

/** 
 * @author 张浩 E-mail:zhanghao@foundercomics.com 
 * @version 创建时间：2014年2月27日 下午6:04:41 
 * 类说明 
 */
@Service
public class DailyReportService {
	@Autowired
    DailyReportDAO dailyReportDAO;

    LRUMap          dailyReportCache = new LRUMap(1000);

    public void createEmptyReportsIfNecessary(int userId, Date curDate) {

        String key = userId + "_" + TimeFormatUtils.date(curDate);
        if (dailyReportCache.containsKey(key)) {
            return;
        }
        DailyReport report = dailyReportDAO.getReportOfWeek(userId, curDate);
        if (report != null) {
        	dailyReportCache.put(key, true);
            return;
        }

        // 还没有周报
        DailyReport emptyReport = new DailyReport();
        emptyReport.setUserId(userId);
        emptyReport.setStatus(DailyReportStatus.READY.getId());
        
        String emailTos = dailyReportDAO.getLastestNonBlankEmailTosBefore(userId,curDate);
        // 尝试插入历史空白的
        DailyReport lastestReport = dailyReportDAO.getLastestReportBefore(userId, curDate);
        
        if (lastestReport != null) {
//            Date lastedWeekDate = lastestReport.getCurrentDate();
//            while (weekDate.before(curDate)) {
//                emptyReport.setEmailTos(emailTos);
//                emptyReport.setCurrentDate(curDate);
//                dailyReportDAO.insert(emptyReport);
//            }
        }

        // 尝试插入本周空白的
        emptyReport.setEmailTos(emailTos);
        emptyReport.setCurrentDate(curDate);
        dailyReportDAO.insert(emptyReport);
        dailyReportCache.put(key, true);
    }

    public void clearCache(){
    	dailyReportCache.clear();;
    }
}
 