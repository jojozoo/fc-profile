package com.orientalcomics.profile.biz.logic; 

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.collections.map.LRUMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orientalcomics.profile.biz.dao.DailyReportDAO;
import com.orientalcomics.profile.biz.model.DailyReport;
import com.orientalcomics.profile.constants.status.DailyReportStatus;
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

    private static Logger LOG = LoggerFactory.getLogger(DailyReportService.class);
    
    public void createEmptyReportsIfNecessary(int userId, Date curDate) {

    	//此时应该写的是哪天的日报
    	Date startDate = generateStartDailyPortTime(curDate);
    	
        String key = userId + "_" + TimeFormatUtils.date(startDate);
        if (dailyReportCache.containsKey(key)) {
            return;
        }
        //当天的日报
        DailyReport report = dailyReportDAO.getReportOfToday(userId, startDate);
        if (report != null) {
        	
        	dailyReportCache.put(key, true);
        	return;
        }

        // 还没有日报，生成一个空白的plan
        DailyReport emptyReport = new DailyReport();
        emptyReport.setUserId(userId);
        emptyReport.setStatus(DailyReportStatus.READY.getId());
        
        String emailTos = dailyReportDAO.getLastestNonBlankEmailTosBefore(userId,curDate);
        // 尝试插入历史空白的
        DailyReport lastestReport = dailyReportDAO.getLastestReportBefore(userId, curDate);
        
        if (lastestReport != null) {
            Date lastedReportDate = lastestReport.getReportDate();
            while (lastedReportDate.before(startDate)) {
                emptyReport.setEmailTos(emailTos);
                emptyReport.setReportDate(curDate);
                dailyReportDAO.insert(emptyReport);
            }
        }

        // 尝试插入本周空白的
        emptyReport.setEmailTos(emailTos);
        emptyReport.setReportDate(curDate);
        dailyReportDAO.insert(emptyReport);
        dailyReportCache.put(key, true);
    }

    
    
    /**
     * 得到当天开始日报的时间,暂定为上午8点,所以如果是没过早上8点，都是前一天
     * @param curDate
     * @return
     */
    public Date generateStartDailyPortTime(Date curDate){
    	
    	
    	Date todayReportTime = getStartTimeOfDate(curDate);
    	
    	//如果在今天日报点之前，就是昨天的
    	if(curDate.before(todayReportTime)){
    		
    		Calendar cal = new GregorianCalendar();
        	cal.setTime(curDate);
        	cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);
			Date yestDate = cal.getTime();
			
			return getStartTimeOfDate(yestDate);
    		
    	}else{
    		return todayReportTime;
    	}
    	
    }
    
    /**
     * 得到当天的8点时间
     * @param date
     * @return
     */
    public Date getStartTimeOfDate(Date date){
    	
    	Date zeroTime = TimeUtils.Floor.floorToDate(date);
    	
    	Calendar cal = new GregorianCalendar();
    	cal.setTime(zeroTime);
    	cal.set(Calendar.HOUR_OF_DAY, 3);
    	
    	Date newDate = cal.getTime();
    	
    	if(LOG.isDebugEnabled()){
    		LOG.debug("日报开始时间为："+newDate);
    	}
    	return newDate;
    }
    
    public void clearCache(){
    	dailyReportCache.clear();;
    }
    
    public static void main(String[] args) {
    	
    	Calendar cal = new GregorianCalendar();
    	cal.setTime(new Date());
    	cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
		Date tomorrow = cal.getTime();
		
    	DailyReportService service = new DailyReportService();
    	Date date = service.generateStartDailyPortTime(tomorrow);
    	System.out.println(date);
        
	}
}
 