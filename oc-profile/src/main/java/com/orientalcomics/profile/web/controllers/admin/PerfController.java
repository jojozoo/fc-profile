package com.orientalcomics.profile.web.controllers.admin;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.orientalcomics.profile.biz.logic.AsyncSendEmailService;
import com.orientalcomics.profile.biz.logic.PerfTimeService;
import com.orientalcomics.profile.biz.logic.ProfileConfigs.PageSizeConfigView;
import com.orientalcomics.profile.biz.model.PerfTime;
import com.orientalcomics.profile.constants.ProfileAction;
import com.orientalcomics.profile.constants.status.PerfPromotionStatus;
import com.orientalcomics.profile.constants.status.PerfTimeStatus;
import com.orientalcomics.profile.util.time.DateTimeUtil;
import com.orientalcomics.profile.web.annotations.ProfileSecurity;
import com.orientalcomics.profile.web.controllers.internal.LoginRequiredController;
import com.orientalcomics.profile.web.interceptors.message.PageMessages;

@ProfileSecurity(ProfileAction.PERF_MANAGE)
public class PerfController extends LoginRequiredController {

    @Autowired
    private PerfTimeService perfTimeService;
    
    @Autowired
    private AsyncSendEmailService sendEmailService;

    /**
     * 绩效周期管理
     * 
     * @author 张浩 hao.zhang@renren-inc.com
     * @param inv
     * @return
     */
    @Get({ "" })
    public String perf(Invocation inv,@Param("curpage") int curPage) {
    	
    	curPage = (curPage < 0? 0:curPage);
    	int total = perfTimeService.countAll();
		int pageSize = getPageSize(PageSizeConfigView.PERF_TIME);
		curPage = checkAndReturnPage(curPage, total, pageSize);
    		
		inv.addModel("total", total);
		inv.addModel("pagesize", pageSize);
		inv.addModel("curpage", curPage);
		
        // 首先查询绩效列表
        List<PerfTime> perfList = perfTimeService.query(curPage * pageSize, pageSize);
        if (perfList == null || perfList.size() == 0) {
            // 如果绩效记录为空，就生成一份新的记录
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            int month = now.get(Calendar.MONTH);
            int season = month % 3 > 0 ? month / 3 + 1 : month / 3;
            LOG.info("当前season:", season);
            try {
                PerfTime perfTime = new PerfTime();
                perfTime.setPerfTitle(now.get(Calendar.YEAR) + "Q" + season);
                perfTime.setStartTime(new Timestamp(new Date().getTime()));
                perfTime.setPerfYear(now.get(Calendar.YEAR));
                perfTime.setEditorId(currentUserId());
                perfTime.setStatus(PerfTimeStatus.READY.getId());
                int perfTimeId = perfTimeService.save(perfTime);
                perfTime.setId(perfTimeId);
                perfList.add(0, perfTime);
                inv.addModel("showStart", DateTimeUtil.compareAccurateToDate(new Date(),perfTimeService.getPerfTimeTitleLastDay(perfTime.getPerfTitle())) > 0);
            } catch (Exception e) {
                LOG.error(e, e.getMessage());
                LOG.error("插入数据出错");
            }
           
            inv.addModel("currentTitle", now.get(Calendar.YEAR) + "Q" + season);
            inv.addModel("perfList", perfList);
        } else {
            // 如果有记录，并且符合2012Q1格式，title为自动生成，不会出现其他格式
            String perfTitle = perfList.get(0).getPerfTitle();
            String currentTitle = "";
            int year = 1970;
            if (StringUtils.isNotEmpty(perfTitle)) {
                String[] titles = perfTitle.split("Q");
                int season = Integer.parseInt(titles[titles.length - 1]);
                year = Integer.parseInt(titles[0]);
                if (season == 4) {
                    currentTitle = Integer.parseInt(titles[0]) + 1 + "Q1";
                    year++;
                } else
                    currentTitle = titles[0] + "Q" + (season + 1);
            }
            // 如果第一个绩效已结束，就生成一份新的记录
            if (perfList.get(0).getStatus() == 3) {
                PerfTime perfTime = new PerfTime();
                perfTime.setStartTime(new Timestamp(new Date().getTime()));
                perfTime.setPerfTitle(currentTitle);
                perfTime.setEditorId(currentUserId());
                perfTime.setPerfYear(year);
                perfTime.setStatus(PerfTimeStatus.READY.getId());
                try {
                    int perfTimeId = perfTimeService.save(perfTime);
                    perfTime.setId(perfTimeId);
                    perfList.add(0, perfTime);
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error(e, e.getMessage());
                    LOG.error("插入记录出错");
                }
            }
            if(DateTimeUtil.compareAccurateToDate(new Date(),perfTimeService.getPerfTimeTitleLastDay(perfTitle)) < 0){
            	inv.addModel("showStart", false);
            }
            inv.addModel("perfList", perfList);
        }
       
        return "perf_time";
    }

    /**
     * 开始绩效考核
     * 
     * @param inv
     * @return
     */
    @Get({ "/start/{perfId:\\d+}", "/end/{perfId:\\d+}","/display/{perfId:\\d+}" })
    @Post({ "/start/{perfId:\\d+}", "/end/{perfId:\\d+}","/display/{perfId:\\d+}" })
    public String perfStartEnd(Invocation inv, PageMessages pageMsg, @Param("perfId") int perfId, @Param("perf_promotion") String promotion) {

        int promotionStatus = 0;
        if (StringUtils.isNotBlank(promotion) && promotion.equals("true")) {
            System.out.println(promotion);
            promotionStatus = 1;
        }
        try {
            PerfTime perfTime = perfTimeService.getPerfTime(perfId);
            if(DateTimeUtil.compareAccurateToDate(new Date(),perfTimeService.getPerfTimeTitleLastDay(perfTime.getPerfTitle())) < 0){
            	return "@错误的操作";
            }
            switch (perfTime.getStatus()) {
                case 0: {
                    // 开始考核
                	perfTime.setStartTime(new Timestamp(new Date().getTime()));
                	perfTime.setStatus(PerfTimeStatus.STARTED.getId());
                    perfTime.setPromotionStatus(promotionStatus);
                    
                    // 更新考核表成功后表示已经开始考核，给所有的员工发送邮件已经开始考核
//                    if( perfTimeService.update(perfTime) == 1)
//                    	sendEmailService.sendPerfStartEmail();
                    perfTimeService.update(perfTime);
                    
                    break;
                }
                case 1: {
                    // 结束考核
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.DAY_OF_MONTH, 3);// 加1天结束
                    perfTime.setEndTime(new Timestamp(calendar.getTimeInMillis()));
                    perfTime.setStatus(PerfTimeStatus.END.getId());
                    
                    perfTime.setIsPublic(0);
                    perfTimeService.update(perfTime);
                    
                 //   sendEmailService.sendPerfDeadlineEmail();
                    
                    break;
                }
                case 3: {
                    // 结束考核
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.DAY_OF_MONTH, 3);// 加1天结束
                    perfTime.setEndTime(new Timestamp(calendar.getTimeInMillis()));
                    perfTime.setStatus(PerfTimeStatus.END.getId());
                    perfTime.setIsPublic(1);
                    perfTimeService.update(perfTime);
                    
                 //   sendEmailService.sendPerfDeadlineEmail();
                    
                    break;
                }
                default:
                    break;
            }

        } catch (Exception e) {
            LOG.error(e, e.getMessage());
        }
        return "r:/admin/perf";
    }
    
    
	/**
     * 结束升职评定
     * 
     * @param inv
     * @param perfTimeId
     * @return
     */
    @Post("endPromotion/{perftimeid:\\d+}")
    public String endPromotion(Invocation inv, @Param("perftimeid") int perfTimeId) {

        try {
            PerfTime perf = perfTimeService.getPerfTime(perfTimeId);
            if (perf != null) {
                perf.promotionStatus(PerfPromotionStatus.END);
                perfTimeService.update(perf);
            }
        } catch (Exception e) {
            LOG.error(e, e.getMessage());
        }
        return "r:/admin/perf";
    }
    
}
