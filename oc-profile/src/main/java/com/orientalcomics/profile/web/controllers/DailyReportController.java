package com.orientalcomics.profile.web.controllers; 

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.orientalcomics.profile.biz.dao.DailyReportDAO;
import com.orientalcomics.profile.biz.logic.AsyncSendEmailService;
import com.orientalcomics.profile.biz.logic.DailyReportService;
import com.orientalcomics.profile.biz.logic.ProfileConfigHelper;
import com.orientalcomics.profile.biz.logic.ProfileConfigs;
import com.orientalcomics.profile.biz.logic.ProfileConfigs.PageSizeConfigView;
import com.orientalcomics.profile.biz.logic.UserService;
import com.orientalcomics.profile.biz.model.DailyReport;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.constants.ProfileAction;
import com.orientalcomics.profile.constants.status.DailyReportStatus;
import com.orientalcomics.profile.core.base.FormValidator;
import com.orientalcomics.profile.core.base.HtmlPage;
import com.orientalcomics.profile.util.common.Collections0;
import com.orientalcomics.profile.util.common.JsonUtils;
import com.orientalcomics.profile.util.time.DateTimeUtil;
import com.orientalcomics.profile.util.time.TimeFetchUtils;
import com.orientalcomics.profile.util.time.TimeHandleUtils;
import com.orientalcomics.profile.util.time.TimeParseUtils;
import com.orientalcomics.profile.util.time.TimeUtils;
import com.orientalcomics.profile.web.annotations.Ajaxable;
import com.orientalcomics.profile.web.annotations.ProfileHtmlCorrect;
import com.orientalcomics.profile.web.annotations.ProfileHtmlEscape;
import com.orientalcomics.profile.web.annotations.ProfileSecurity;
import com.orientalcomics.profile.web.constants.AjaxType;
import com.orientalcomics.profile.web.controllers.internal.LoginRequiredController;

/** 
 * @author 张浩 E-mail:zhanghao@foundercomics.com 
 * @version 创建时间：2014年2月27日 下午6:00:25 
 * 类说明 :日报页面
 */
public class DailyReportController extends LoginRequiredController{
	
	@Autowired
    private     DailyReportDAO    dailyReportDAO;

    @Autowired
    private DailyReportService    dailyReportService;

    @Autowired
    private UserService            userService;

    @Autowired
    private AsyncSendEmailService  sendEmailService;
    

    /**
     * 权限就引用周报的
     * @param inv
     * @param page
     * @param pStartDate
     * @param pEndDate
     * @param curPage
     * @return
     */
    @ProfileSecurity(ProfileAction.VIEW_WEEKLY_REPORT)
    @Get("my")
    public String get_my(Invocation inv, HtmlPage page, @Param("startdate") String pStartDate, @Param("enddate") String pEndDate,
            @Param("curpage") int curPage) {
        int userId = currentUserId();
        // 读取当前日期
        Date curDate = new Date();
        
        dailyReportService.createEmptyReportsIfNecessary(userId, curDate);// 如果需要就创建Ta的日报

       Date reportStoreDate =  dailyReportService.generateStartDailyPortTime(curDate);
        
        // 判断用户是否已写当天的日报
        DailyReport editedReport = null;
        DailyReport report = dailyReportDAO.getReportOfToday(userId, reportStoreDate);
        if (report != null) {
            if (report.getStatus() != DailyReportStatus.SUBMITTED.getId() || report.getContentDone() == null 
            		|| report.getContentDone().length() < 2) {// 如果未提交，则还需要继续编辑
                editedReport = report;
            }
        }
        inv.addModel("editedReport", editedReport);
        
        // 获取用户的日报记录
        renderReports(inv, page, userId, pStartDate, pEndDate, curPage);
        inv.addModel("editableIds", getEditableReportIds(userId));
        return "dailyreport_my";
    }

    @ProfileSecurity(ProfileAction.VIEW_WEEKLY_REPORT)
    @Get("{owner:\\d+}")
    public String get_other(Invocation inv, HtmlPage page, @Param("owner") int owner, @Param("startdate") String pStartDate,
            @Param("enddate") String pEndDate, @Param("curpage") int curPage) {
        Date today = TimeUtils.FetchTime.today();// 今天
        Date monday = DateTimeUtil.getMondayOfWeek(today);// 周一
        dailyReportService.createEmptyReportsIfNecessary(owner, monday);// 创建Ta的周报
        renderReports(inv, page, owner, pStartDate, pEndDate, curPage);
        Set<Integer> rootUserIds = ProfileConfigHelper.ins().getValue(ProfileConfigs.IntegerSetConfigView.ROOT_USERS);
        inv.addModel("isRoot", rootUserIds.contains(this.currentUserId()));
        return "dailyreport_other";
    }

    @ProfileSecurity(ProfileAction.VIEW_WEEKLY_REPORT)
    @Get("my/subordinates")
    public String get_subordinates(Invocation inv, HtmlPage page, @Param("date") String pDate, @Param("curpage") int curPage) {
        User user = currentUser();
        int userId = user.getId();
        renderSubordinateReports(inv, page, userId, pDate, curPage);
        Set<Integer> rootUserIds = ProfileConfigHelper.ins().getValue(ProfileConfigs.IntegerSetConfigView.ROOT_USERS);
        inv.addModel("isRoot", rootUserIds.contains(this.currentUserId()));
        return "dailyreport_subordinates_my";
    }

    @ProfileSecurity(ProfileAction.VIEW_WEEKLY_REPORT)
    @Get("{owner:\\d+}/subordinates")
    public String get_subordinates(Invocation inv, HtmlPage page, @Param("owner") int ownerId, @Param("date") String pDate, @Param("curpage") int curPage) {
        renderSubordinateReports(inv, page, ownerId, pDate, curPage);
        Set<Integer> rootUserIds = ProfileConfigHelper.ins().getValue(ProfileConfigs.IntegerSetConfigView.ROOT_USERS);
        inv.addModel("isRoot", rootUserIds.contains(this.currentUserId()));
        return "dailyreport_subordinates_other";
    }

    @Get("my/edit")
    public String get_my(Invocation inv, HtmlPage page, @Param("id") int id) {
        int userId = currentUserId();
        $: { // 查询
            if (id <= 0) {
                page.error("不存在此周报");
                break $;
            }
            DailyReport report = dailyReportDAO.query(id);
            if (report == null) {
                page.error("不存在此周报");
                break $;
            }
            if (report.getUserId() != userId) {
                page.error("不能编辑别人的周报");
                break $;
            }

            Set<Integer> editableIds = getEditableReportIds(userId);
            if (!editableIds.contains(id)) {
                page.error("不能编辑此周报");
                break $;
            }

            inv.addModel("editedReport", report);
            inv.addModel("editmode", true);
        }
        return "dailyreport_my";
    }

    // @ProfileSecurity(ProfileAction.EDIT_WEEKLY_REPORT)
    @Post("my/{action:preview|save|submit}")
    @Ajaxable(AjaxType.JSON)
    public void post_my(HtmlPage page, @Param("action") final String action, @Param("id") final int id,
    		@ProfileHtmlCorrect @Param("content_done") final String contentDone,
            @ProfileHtmlCorrect @Param("content_plan") final String contentPlan,
            @ProfileHtmlCorrect @Param("q_a") String qA,
            @Param("qa_changed")boolean qaChanged,
            @ProfileHtmlEscape @Param("emailtos") final String emailTos
            ) {
        int userId = currentUserId();

        $: try {
            // validate
            FormValidator fv = page.formValidator();
            fv.min(id, 1, "week_time", "表单提交错误");
            fv.notEmpty(contentDone, "content_done", "需要填写本周已做的内容");
            fv.notEmpty(contentPlan, "content_plan", "需要填写下周计划的内容");
            if(!qaChanged){
                qA = StringUtils.EMPTY;
            }else{
                qA = StringUtils.trimToEmpty(qA);// 可以不填
            }

            boolean isPreviewAction = "preview".equalsIgnoreCase(action);
            boolean isSaveAction = "save".equalsIgnoreCase(action);
            boolean isSubmitAction = "submit".equalsIgnoreCase(action);

            // 查询
            DailyReport report = dailyReportDAO.query(id);
            if (report == null) {
                page.expired();
                break $;
            }
            // Date weekDate = report.getWeekDate();
            if (report.getUserId() != userId) {
                page.alert("只允许对自己的周报进行编辑操作");
                break $;
            }

            Set<Integer> editableIds = getEditableReportIds(userId);
            if (!editableIds.contains(id)) {
                page.alert("不能编辑这个周报");
                break $;
            }

            if (isPreviewAction) {// 预览这个周报
                page.data(JsonUtils.builder().put("action", "preview").put("content_done", contentDone).put("content_plan", contentPlan).put("qa", qA).build());
                break $;
            }

            // 入库
            DailyReportStatus newStatus = isSubmitAction ? DailyReportStatus.SUBMITTED : isSaveAction ? DailyReportStatus.SAVED : DailyReportStatus.READY;

//            Date today = TimeUtils.FetchTime.today();// 今天
//            Date monday = DateTimeUtil.getMondayOfWeek(today);// 周一

            report.setContentDone(contentDone);
            report.setContentPlan(contentPlan);
            report.setStatus(newStatus.getId());
            report.setQa(qA);
            report.setEmailTos("zhanghao@foundercomics.com");
            dailyReportDAO.update(report);
            
            // success
            if (isSubmitAction) {
                sendWorklyReportEmail(report, this.currentUser());
                page.redirect("/dailyReport/my");
            } else {
                page.info("保存成功");
            }
        } catch (Exception e) {
            LOG.error("id:" +id + ",content_done:" + StringUtils.abbreviate(contentDone, 10) + ",content_plan:" +
                    StringUtils.abbreviate(contentPlan, 10));
            page.error("服务端发生错误");
        }
    }

    // /***
    // * 查看我的下属周报汇总
    // * @param inv
    // * @return
    // */
    // @Get("my/reporters")
    // public String viewReportersWeekly(Invocation inv){
    //
    // //判断是看这周还是上周
    // Date today = DateTimeUtil.getCurrDate();
    // Date friday = DateTimeUtil.getFridayOfWeek(today);
    // Date monday = DateTimeUtil.getMondayOfWeek(today);
    // if(DateTimeUtil.compareAccurateToDate(today, friday) < 0){
    // monday = DateTimeUtil.getMondayOfLastWeek(today);
    // }
    // inv.addModel("monday", monday);
    // inv.addModel("sunday", DateTimeUtil.getSundayOfWeek(monday));
    //
    // //取到周报和点评数据
    // Collection<Integer> followIds =
    // userService.queryAllMyFollow(currentUserId());
    // if(followIds != null){
    // List<WeeklyReport> reports =
    // weeklyReportDAO.queryAllByCollection(followIds, monday);
    // inv.addModel("reports", reports);
    //
    // // Map<Integer,WeeklyReportComment> comments =
    // weeklyReportCommentDAO.queryCommets(followIds,monday);
    // // if(comments != null){
    // // inv.addModel("comments", comments);
    // // }
    // }else{
    // inv.addModel("message", "您没有下属信息！");
    // }
    // return "weekly_reporters";
    // }



    /**
     * 发送邮件给自己和主管,此周报是本周提交的，还是补交提交的周报
     * 
     * @param report
     * @param user
     */
    private void sendWorklyReportEmail(DailyReport report, User user) {

        // 发送的日报是否有content_done，早报和晚报不一样

        if (report.getContentDone() != null && report.getContentDone().length() > 2)
            sendEmailService.sendSubmmitedDoneDailyReport(report, user);
        else
            sendEmailService.sendSubmmitedPlanDailyReport(report, user);

    }

    private void renderReports(Invocation inv, HtmlPage page, int ownerId, String pStartDate, String pEndDate, int curPage) {
        FormValidator fv = page.formValidator();
        // 时间
        final int timeField = Calendar.DATE;
        Timestamp startDate = TimeHandleUtils.FLOOR.floorTo(TimeParseUtils.TIMESTAMP.parse(pStartDate), timeField); //
        Timestamp endDate = TimeHandleUtils.FLOOR.floorTo(TimeParseUtils.TIMESTAMP.parse(pEndDate), timeField); //
        fv.assertFalse(StringUtils.isNotBlank(pStartDate) && startDate == null, "date", "起始时间格式不对");
        fv.assertFalse(StringUtils.isNotBlank(pEndDate) && endDate == null, "date", "结束时间格式不对");
        fv.assertFalse(startDate != null && endDate != null && endDate.before(startDate), "date", "结束的创建时间不能早于开始时间");
        if (fv.isFailed()) {
            return;
        }
        // 修正时间
        Date startMonday = startDate == null ? null : DateTimeUtil.getMondayOfWeek(startDate);
        Date endMonday = endDate == null ? null : DateTimeUtil.getMondayOfWeek(endDate);

        int total = dailyReportDAO.countByUserBetweenDate(ownerId, startMonday, endMonday);
        int pageSize = getPageSize(PageSizeConfigView.DAILY_REPORT);
        curPage = checkAndReturnPage(curPage, total, pageSize);

        inv.addModel("total", total);
        inv.addModel("pagesize", pageSize);
        inv.addModel("curpage", curPage);

        List<DailyReport> weeklyReports = dailyReportDAO.queryByUserBetweenDate(ownerId, startMonday, endMonday, curPage * pageSize, pageSize);
        inv.addModel("reports", weeklyReports);

    }

    private boolean renderSubordinateReports(Invocation inv, HtmlPage page, int userId, String pDate, int curPage) {
    	
        Date curDate = TimeParseUtils.DATE.parse(pDate);
        if (StringUtils.isNotEmpty(pDate) && curDate == null) {
            page.warning("日期格式错误，默认为昨天");
        }
//        Date today = TimeFetchUtils.DATE.today();
        Date lastDay = TimeFetchUtils.DATE.yestoday();
        
        
        Date reportDate = null;
        
        if (curDate == null) {
        	//默认是昨天
        	reportDate = dailyReportService.generateStartDailyPortTime(lastDay);
        } else {
        	reportDate = dailyReportService.generateStartDailyPortTime(curDate);
        }
        inv.addModel("report_date", reportDate);

        int total = userService.countSubordinates(userId);
        int pageSize = getPageSize(PageSizeConfigView.SUBORDINATE_DAILY_REPORT);
        curPage = checkAndReturnPage(curPage, total, pageSize);
        inv.addModel("total", total);
        inv.addModel("pagesize", pageSize);
        inv.addModel("curpage", curPage);
        List<User> subordinates = userService.querySubordinates(userId, curPage * pageSize, pageSize);
        if (subordinates.isEmpty()) {
            page.error("没有下属信息");
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
            dailyReportService.createEmptyReportsIfNecessary(subordinateId, reportDate);
        }

        List<DailyReport> dailyReports = dailyReportDAO.queryByUserIdsByReportDate(subordinateIds, reportDate);
        Map<Integer/* 用户ID */, DailyReport> dailyReportMap = Collections0.packageMapByField(dailyReports, new Function<DailyReport, Integer>() {

            @Override
            public Integer apply(DailyReport dailyReport) {
                return dailyReport.getUserId();
            }
        });

        inv.addModel("subordinates", subordinates);
        inv.addModel("weeklyreport_map", dailyReportMap);
        return true;
    }

    private Set<Integer> getEditableReportIds(int userId) {
        // 1. 只允许编辑当天没有写晚报的日报
        Set<Integer> editableIds = new HashSet<Integer>();
        
        Date date = dailyReportService.generateStartDailyPortTime(new Date());
        DailyReport workReport = dailyReportDAO.getReportOfToday(userId, date);
          if (workReport != null && workReport.getStatus() != DailyReportStatus.SUBMITTED.getId()
        		  && workReport.getContentDone() != null && workReport.getContentDone().length() > 2) {// 晚报没有提交
        	  
               editableIds.add(workReport.getId());
          }

        return editableIds;
    }
}
 