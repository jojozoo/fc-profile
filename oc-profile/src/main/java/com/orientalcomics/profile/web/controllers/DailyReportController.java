package com.orientalcomics.profile.web.controllers; 

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.orientalcomics.profile.biz.dao.DailyReportDAO;
import com.orientalcomics.profile.biz.dao.WeeklyReportCommentDAO;
import com.orientalcomics.profile.biz.logic.AsyncSendEmailService;
import com.orientalcomics.profile.biz.logic.ProfileConfigHelper;
import com.orientalcomics.profile.biz.logic.ProfileConfigs;
import com.orientalcomics.profile.biz.logic.ProfileConfigs.PageSizeConfigView;
import com.orientalcomics.profile.biz.logic.UserService;
import com.orientalcomics.profile.biz.logic.WeeklyReportService;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.biz.model.WeeklyReport;
import com.orientalcomics.profile.biz.model.WeeklyReportComment;
import com.orientalcomics.profile.constants.ProfileAction;
import com.orientalcomics.profile.constants.status.WeeklyReportStatus;
import com.orientalcomics.profile.core.base.FormValidator;
import com.orientalcomics.profile.core.base.HtmlPage;
import com.orientalcomics.profile.util.common.Collections0;
import com.orientalcomics.profile.util.common.JsonUtils;
import com.orientalcomics.profile.util.time.DateTimeUtil;
import com.orientalcomics.profile.util.time.TimeFetchUtils;
import com.orientalcomics.profile.util.time.TimeFormatUtils;
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
	
//	@Autowired
//    private     DailyReportDAO    dailyReportDAO;
//
//    @Autowired
//    private WeeklyReportCommentDAO weeklyReportCommentDAO;
//
//    @Autowired
//    private WeeklyReportService    weeklyReportService;
//
//    @Autowired
//    private UserService            userService;
//
//    @Autowired
//    private AsyncSendEmailService  sendEmailService;
//    
//
//    @ProfileSecurity(ProfileAction.VIEW_WEEKLY_REPORT)
//    @Get("my")
//    public String get_my(Invocation inv, HtmlPage page, @Param("startdate") String pStartDate, @Param("enddate") String pEndDate,
//            @Param("curpage") int curPage) {
//        int userId = currentUserId();
//        // 读取当周日期
//        Date today = TimeUtils.FetchTime.today();// 今天
//        Date monday = DateTimeUtil.getMondayOfWeek(today);// 周一
//        weeklyReportService.createEmptyReportsIfNecessary(userId, monday);// 创建Ta的周报
//
//        // 判断用户是否已写本周周报
//        WeeklyReport editedReport = null;
//        WeeklyReport report = dailyReportDAO.getReportOfWeek(userId, monday);
//        if (report != null) {
//            if (report.status() != WeeklyReportStatus.SUBMITTED) {// 如果未提交，则还需要继续编辑
//                editedReport = report;
//            }
//        }
//        inv.addModel("editedReport", editedReport);
//        // 获取用户的周报记录
//        renderReports(inv, page, userId, pStartDate, pEndDate, curPage);
//        inv.addModel("editableIds", getEditableReportIds(userId));
//        return "weeklyreport_my";
//    }
//
//    @ProfileSecurity(ProfileAction.VIEW_WEEKLY_REPORT)
//    @Get("{owner:\\d+}")
//    public String get_other(Invocation inv, HtmlPage page, @Param("owner") int owner, @Param("startdate") String pStartDate,
//            @Param("enddate") String pEndDate, @Param("curpage") int curPage) {
//        Date today = TimeUtils.FetchTime.today();// 今天
//        Date monday = DateTimeUtil.getMondayOfWeek(today);// 周一
//        weeklyReportService.createEmptyReportsIfNecessary(owner, monday);// 创建Ta的周报
//        renderReports(inv, page, owner, pStartDate, pEndDate, curPage);
//        Set<Integer> rootUserIds = ProfileConfigHelper.ins().getValue(ProfileConfigs.IntegerSetConfigView.ROOT_USERS);
//        inv.addModel("isRoot", rootUserIds.contains(this.currentUserId()));
//        return "weeklyreport_other";
//    }
//
//    @ProfileSecurity(ProfileAction.VIEW_WEEKLY_REPORT)
//    @Get("my/subordinates")
//    public String get_subordinates(Invocation inv, HtmlPage page, @Param("date") String pDate, @Param("curpage") int curPage) {
//        User user = currentUser();
//        int userId = user.getId();
//        renderSubordinateReports(inv, page, userId, pDate, curPage);
//        Set<Integer> rootUserIds = ProfileConfigHelper.ins().getValue(ProfileConfigs.IntegerSetConfigView.ROOT_USERS);
//        inv.addModel("isRoot", rootUserIds.contains(this.currentUserId()));
//        return "weeklyreport_subordinates_my";
//    }
//
//    @ProfileSecurity(ProfileAction.VIEW_WEEKLY_REPORT)
//    @Get("{owner:\\d+}/subordinates")
//    public String get_subordinates(Invocation inv, HtmlPage page, @Param("owner") int ownerId, @Param("date") String pDate, @Param("curpage") int curPage) {
//        renderSubordinateReports(inv, page, ownerId, pDate, curPage);
//        Set<Integer> rootUserIds = ProfileConfigHelper.ins().getValue(ProfileConfigs.IntegerSetConfigView.ROOT_USERS);
//        inv.addModel("isRoot", rootUserIds.contains(this.currentUserId()));
//        return "weeklyreport_subordinates_other";
//    }
//
//    // @ProfileSecurity(ProfileAction.EDIT_WEEKLY_REPORT)
//    @Get("my/edit")
//    public String get_my(Invocation inv, HtmlPage page, @Param("id") int id) {
//        int userId = currentUserId();
//        $: { // 查询
//            if (id <= 0) {
//                page.error("不存在此周报");
//                break $;
//            }
//            WeeklyReport report = weeklyReportDAO.query(id);
//            if (report == null) {
//                page.error("不存在此周报");
//                break $;
//            }
//            if (report.getUserId() != userId) {
//                page.error("不能编辑别人的周报");
//                break $;
//            }
//
//            Set<Integer> editableIds = getEditableReportIds(userId);
//            if (!editableIds.contains(id)) {
//                page.error("不能编辑此周报");
//                break $;
//            }
//
//            inv.addModel("editedReport", report);
//            inv.addModel("editmode", true);
//        }
//        return "weeklyreport_my";
//    }
//
//    // @ProfileSecurity(ProfileAction.EDIT_WEEKLY_REPORT)
//    @Post("my/{action:preview|save|submit}")
//    @Ajaxable(AjaxType.JSON)
//    public void post_my(HtmlPage page, @Param("action") final String action, @Param("id") final int id,
//    		@ProfileHtmlCorrect @Param("content_done") final String contentDone,
//            @ProfileHtmlCorrect @Param("content_plan") final String contentPlan,
//            @ProfileHtmlCorrect @Param("q_a") String qA,
//            @Param("qa_changed")boolean qaChanged,
//            @ProfileHtmlEscape @Param("emailtos") final String emailTos
//            ) {
//        int userId = currentUserId();
//
//        $: try {
//            // validate
//            FormValidator fv = page.formValidator();
//            fv.min(id, 1, "week_time", "表单提交错误");
//            fv.notEmpty(contentDone, "content_done", "需要填写本周已做的内容");
//            fv.notEmpty(contentPlan, "content_plan", "需要填写下周计划的内容");
//            if(!qaChanged){
//                qA = StringUtils.EMPTY;
//            }else{
//                qA = StringUtils.trimToEmpty(qA);// 可以不填
//            }
////            String[] emailToArray = null;
////            try {
////                emailToArray = EmailUtils.correctEmails(StringUtils.split(emailTos, ";"), EMAIL_DOMAIN, true);
////            } catch (Exception e) {
////            	LOG.error("emailtos", "Email必须满足<" + EMAIL_MAIN_PATTERN + ">@" + EMAIL_DOMAIN);
////                fv.error("emailtos", "Email必须满足<" + EMAIL_MAIN_PATTERN + ">@" + EMAIL_DOMAIN);
////            }
////            String correctedEmailTos = StringUtils.join(emailToArray, ";");
////            if (fv.isFailed()) {
////            	LOG.error("error", "参数验证失败");
////                break $;
////            }
//            boolean isPreviewAction = "preview".equalsIgnoreCase(action);
//            boolean isSaveAction = "save".equalsIgnoreCase(action);
//            boolean isSubmitAction = "submit".equalsIgnoreCase(action);
//
//            // 查询
//            WeeklyReport report = weeklyReportDAO.query(id);
//            if (report == null) {
//                page.expired();
//                break $;
//            }
//            // Date weekDate = report.getWeekDate();
//            if (report.getUserId() != userId) {
//                page.alert("只允许对自己的周报进行编辑操作");
//                break $;
//            }
//
//            Set<Integer> editableIds = getEditableReportIds(userId);
//            if (!editableIds.contains(id)) {
//                page.alert("不能编辑这个周报");
//                break $;
//            }
//
//            if (isPreviewAction) {// 预览这个周报
//                page.data(JsonUtils.builder().put("action", "preview").put("content_done", contentDone).put("content_plan", contentPlan).put("qa", qA).build());
//                break $;
//            }
//
//            // 入库
//            WeeklyReportStatus newStatus = isSubmitAction ? WeeklyReportStatus.SUBMITTED : isSaveAction ? WeeklyReportStatus.SAVED : WeeklyReportStatus.READY;
//
//            Date today = TimeUtils.FetchTime.today();// 今天
//            Date monday = DateTimeUtil.getMondayOfWeek(today);// 周一
//            boolean isSupplementary = report.getWeekDate().before(monday);// 如果是以前的，则设置为补充的
//
//            report.setContentDone(contentDone);
//            report.setContentPlan(contentPlan);
//            report.setStatus(newStatus.getId());
//            report.setSupplementary(isSupplementary);
//            report.setQa(qA);
//            report.setEmailTos("zhanghao@foundercomics.com");
//            dailyReportDAO.update(report);
//            
//            // success
//            if (isSubmitAction) {
//                sendWorklyReportEmail(report, this.currentUser());
//                page.redirect("/weeklyreport/my");
//            } else {
//                page.info("保存成功");
//            }
//        } catch (Exception e) {
//            LOG.error("id:" +id + ",content_done:" + StringUtils.abbreviate(contentDone, 10) + ",content_plan:" +
//                    StringUtils.abbreviate(contentPlan, 10));
//            page.error("服务端发生错误");
//        }
//    }
//
//    // /***
//    // * 查看我的下属周报汇总
//    // * @param inv
//    // * @return
//    // */
//    // @Get("my/reporters")
//    // public String viewReportersWeekly(Invocation inv){
//    //
//    // //判断是看这周还是上周
//    // Date today = DateTimeUtil.getCurrDate();
//    // Date friday = DateTimeUtil.getFridayOfWeek(today);
//    // Date monday = DateTimeUtil.getMondayOfWeek(today);
//    // if(DateTimeUtil.compareAccurateToDate(today, friday) < 0){
//    // monday = DateTimeUtil.getMondayOfLastWeek(today);
//    // }
//    // inv.addModel("monday", monday);
//    // inv.addModel("sunday", DateTimeUtil.getSundayOfWeek(monday));
//    //
//    // //取到周报和点评数据
//    // Collection<Integer> followIds =
//    // userService.queryAllMyFollow(currentUserId());
//    // if(followIds != null){
//    // List<WeeklyReport> reports =
//    // weeklyReportDAO.queryAllByCollection(followIds, monday);
//    // inv.addModel("reports", reports);
//    //
//    // // Map<Integer,WeeklyReportComment> comments =
//    // weeklyReportCommentDAO.queryCommets(followIds,monday);
//    // // if(comments != null){
//    // // inv.addModel("comments", comments);
//    // // }
//    // }else{
//    // inv.addModel("message", "您没有下属信息！");
//    // }
//    // return "weekly_reporters";
//    // }
//
//    /**
//     * 提交对下属的周报点评
//     * 
//     * @param inv
//     * @param page
//     * @param reporterId
//     * @param weeklyReportId
//     * @param comment
//     */
//    @SuppressWarnings("unchecked")
//    @Post("comment")
//    @Ajaxable(AjaxType.JSON)
//    public void submitComments(Invocation inv,
//            HtmlPage page,
//            @Param("reporter_id") int reporterId,
//            @Param("weekly_report_id") int weeklyReportId,
//            @ProfileHtmlCorrect @Param("comment") final String comment) {
//
//        $: {
//            FormValidator fv = page.formValidator();
//            fv.notEmpty(comment, "comment", "点评必须填写");
//            fv.min(reporterId, 0, "reporter_id", "错误!");
//            fv.min(weeklyReportId, 0, "weekly_report_id", "错误!");
//
//            if (fv.isFailed()) {
//                page.error("内容出错！");
//                break $;
//            }
//            User user = currentUser();
//            try {
//                WeeklyReport report = weeklyReportDAO.query(weeklyReportId);
//                if (report == null) {
//                    page.error("该周报不存在，无法点评！");
//                    break $;
//                }
//                User owner = userService.query(report.getUserId());
//                if (owner == null) {
//                    page.error("这个周报已过期~");
//                    break $;
//                }
////                if (owner.getManagerId() != user.getId()) {
////                    page.error("周报只能由上级点评~");
////                    break $;
////                }
//
//                WeeklyReportComment commentItem = weeklyReportCommentDAO.query(user.getId(), reporterId);
//                if (commentItem != null) {
//                    page.error("该周报已点评，无法再点评！");
//                    break $;
//                }
//
//                commentItem = new WeeklyReportComment();
//                commentItem.setComment(comment);
//                commentItem.setCommentUser(user.getId());
//                commentItem.setCommentUserName(user.getName());
//                commentItem.setEditTime(new Date());
//                commentItem.setWeekDate(report.getWeekDate());
//                commentItem.setWeeklyReportId(report.getId());
//
//                Integer id = weeklyReportCommentDAO.insert(commentItem);
//                if (id == null) {
//                    WeeklyReportComment exsitsItem = weeklyReportCommentDAO.query(user.getId(), reporterId);
//                    if (exsitsItem != null) {
//                        page.error("DB出异常了");
//                        break $;
//                    }
//                }
//                JSONObject object = new JSONObject();
//                object.put("content", comment);
//                page.data(object);
//
//                // send email
//                sendEmailService.sendWeelyAccessEmail(user, owner, comment);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//    /**
//     * 发送邮件给自己和主管,此周报是本周提交的，还是补交提交的周报
//     * 
//     * @param report
//     * @param user
//     */
//    private void sendWorklyReportEmail(WeeklyReport report, User user) {
//
//        // 发送的周报是不是当前本周发送，由于本周发送和补交发送的邮件内容不一样
//        int betweenDay = DateTimeUtil.compareAccurateToDate(DateTimeUtil.getCurrDate(), report.getWeekDate());
//
//        if (betweenDay >= 7)
//            sendEmailService.sendSubmmitedPayWeeklyReportEmail(report, user);
//        else
//            sendEmailService.sendSubmmitedWeeklyReportEmail(report, user);
//
//    }
//
//    private void renderReports(Invocation inv, HtmlPage page, int ownerId, String pStartDate, String pEndDate, int curPage) {
//        FormValidator fv = page.formValidator();
//        // 时间
//        final int timeField = Calendar.DATE;
//        Timestamp startDate = TimeHandleUtils.FLOOR.floorTo(TimeParseUtils.TIMESTAMP.parse(pStartDate), timeField); //
//        Timestamp endDate = TimeHandleUtils.FLOOR.floorTo(TimeParseUtils.TIMESTAMP.parse(pEndDate), timeField); //
//        fv.assertFalse(StringUtils.isNotBlank(pStartDate) && startDate == null, "date", "起始时间格式不对");
//        fv.assertFalse(StringUtils.isNotBlank(pEndDate) && endDate == null, "date", "结束时间格式不对");
//        fv.assertFalse(startDate != null && endDate != null && endDate.before(startDate), "date", "结束的创建时间不能早于开始时间");
//        if (fv.isFailed()) {
//            return;
//        }
//        // 修正时间
//        Date startMonday = startDate == null ? null : DateTimeUtil.getMondayOfWeek(startDate);
//        Date endMonday = endDate == null ? null : DateTimeUtil.getMondayOfWeek(endDate);
//
//        int total = weeklyReportDAO.countByUserBetweenDate(ownerId, startMonday, endMonday);
//        int pageSize = getPageSize(PageSizeConfigView.WEEKLY_REPORT);
//        curPage = checkAndReturnPage(curPage, total, pageSize);
//
//        inv.addModel("total", total);
//        inv.addModel("pagesize", pageSize);
//        inv.addModel("curpage", curPage);
//
//        List<WeeklyReport> weeklyReports = weeklyReportDAO.queryByUserBetweenDate(ownerId, startMonday, endMonday, curPage * pageSize, pageSize);
//        inv.addModel("reports", weeklyReports);
//        Collection<Integer> weeklyReportIds = Collections2.transform(weeklyReports, new Function<WeeklyReport, Integer>() {
//            @Override
//            public Integer apply(WeeklyReport item) {
//                return item == null ? null : item.getId();
//            }
//        }
//                );
////        Map<Integer/* 周报的ID */, WeeklyReportComment> weeklyReportCommentMap = null;
////        if (CollectionUtils.isNotEmpty(weeklyReportIds)) {
////            List<WeeklyReportComment> weeklyReportComments = weeklyReportCommentDAO.queryByWeeklyReportIds(weeklyReportIds);
////            weeklyReportCommentMap = Collections0.packageMapByField(weeklyReportComments, new Function<WeeklyReportComment, Integer>() {
////
////                @Override
////                public Integer apply(WeeklyReportComment weeklyReportComment) {
////                    return weeklyReportComment.getWeeklyReportId();
////                }
////            });
////        }
//        
//        Map<Integer/* 周报的ID */, List<WeeklyReportComment>> weeklyReportCommentMap = new HashMap<Integer/* 周报的ID */, List<WeeklyReportComment>>();
//        if (CollectionUtils.isNotEmpty(weeklyReportIds)) {
//        	 List<WeeklyReportComment> weeklyReportComments = weeklyReportCommentDAO.queryByWeeklyReportIds(weeklyReportIds);
//        	 for (WeeklyReportComment comment : weeklyReportComments) {
//        		 List<WeeklyReportComment> comments = new ArrayList<WeeklyReportComment>(3); 
//        		 if (weeklyReportCommentMap.containsKey(comment.getWeeklyReportId())) {
//        			 weeklyReportCommentMap.get(comment.getWeeklyReportId()).add(comment);
//        		 }else {
//        			 comments.add(comment);
//        			 weeklyReportCommentMap.put(comment.getWeeklyReportId(), comments);
//        		 }
//        		 
//        	 }
//        }
//        inv.addModel("reporsComments", weeklyReportCommentMap);
//    }
//
//    private boolean renderSubordinateReports(Invocation inv, HtmlPage page, int userId, String pDate, int curPage) {
//        Date weekDate = TimeParseUtils.DATE.parse(pDate);
//        if (StringUtils.isNotEmpty(pDate) && weekDate == null) {
//            page.warning("日期格式错误，默认为上周");
//        }
//        Date today = TimeFetchUtils.DATE.today();
//        Date monday = DateTimeUtil.getMondayOfWeek(today);// 本周一
//        Date lastMonday = DateTimeUtil.getMondayOfLastWeek(monday);// 本周一
//        if (weekDate == null) {
//            if (DateTimeUtil.getDayOfWeekFromDate(today) >= 5 - 1) {// 周五、周六、周日
//                // 默认为本周
//                weekDate = monday;
//            } else {
//                // 否则默认为上周
//                weekDate = lastMonday;
//            }
//        } else {
//            weekDate = DateTimeUtil.getMondayOfWeek(weekDate);// 周一
//            if (weekDate.after(monday)) {
//                page.warning("最晚只能选择本周");
//                weekDate = monday;
//                inv.addModel("date", TimeFormatUtils.date(weekDate));
//            }
//            if (weekDate.before(ONLINE_TIME)) {
//                weekDate = DateTimeUtil.getMondayOfWeek(ONLINE_TIME);//
//                page.warning("最早只能选择" + TimeFormatUtils.date(weekDate));
//                inv.addModel("date", TimeFormatUtils.date(weekDate));
//            }
//        }
//        inv.addModel("start_date", DateTimeUtil.getMondayOfWeek(weekDate));
//        inv.addModel("end_date", DateTimeUtil.getSundayOfWeek(weekDate));
//        // 添加额外的描述
//        if (weekDate.equals(monday)) {
//            inv.addModel("week_date_desc", "本周");
//        } else if (weekDate.equals(lastMonday)) {
//            inv.addModel("week_date_desc", "上周");
//        }
//
//        int total = userService.countSubordinates(userId);
//        int pageSize = getPageSize(PageSizeConfigView.SUBORDINATE_WEEKLY_REPORT);
//        curPage = checkAndReturnPage(curPage, total, pageSize);
//        inv.addModel("total", total);
//        inv.addModel("pagesize", pageSize);
//        inv.addModel("curpage", curPage);
//        List<User> subordinates = userService.querySubordinates(userId, curPage * pageSize, pageSize);
//        if (subordinates.isEmpty()) {
//            page.error("没有下属信息");
//            return false;
//        }
//        // 抽取每个用户的周报列表
//        Collection<Integer> subordinateIds = Collections2.transform(subordinates, new Function<User, Integer>() {
//
//            @Override
//            public Integer apply(User user) {
//                return user.getId();
//            }
//        });
//
//        for (int subordinateId : subordinateIds) {
//            weeklyReportService.createEmptyReportsIfNecessary(subordinateId, weekDate);
//        }
//
//        List<WeeklyReport> weeklyReports = weeklyReportDAO.queryByUserIdsInWeekDate(subordinateIds, weekDate);
//        Map<Integer/* 用户ID */, WeeklyReport> weeklyReportMap = Collections0.packageMapByField(weeklyReports, new Function<WeeklyReport, Integer>() {
//
//            @Override
//            public Integer apply(WeeklyReport weeklyReport) {
//                return weeklyReport.getUserId();
//            }
//        });
//        Collection<Integer> weeklyReportIds = Collections2.transform(weeklyReports, new Function<WeeklyReport, Integer>() {
//
//            @Override
//            public Integer apply(WeeklyReport weeklyReport) {
//                return weeklyReport.getId();
//            }
//        });
////        Map<Integer/* 周报的ID */, WeeklyReportComment> weeklyReportCommentMap = null;
////        if (CollectionUtils.isNotEmpty(weeklyReportIds)) {
////            List<WeeklyReportComment> weeklyReportComments = weeklyReportCommentDAO.queryByWeeklyReportIds(weeklyReportIds);
////            weeklyReportCommentMap = Collections0.packageMapByField(weeklyReportComments, new Function<WeeklyReportComment, Integer>() {
////
////                @Override
////                public Integer apply(WeeklyReportComment weeklyReportComment) {
////                    return weeklyReportComment.getWeeklyReportId();
////                }
////            });
////        }
//        
//        Map<Integer/* 周报的ID */, List<WeeklyReportComment>> weeklyReportCommentMap = new HashMap<Integer/* 周报的ID */, List<WeeklyReportComment>>();
//        if (CollectionUtils.isNotEmpty(weeklyReportIds)) {
//        	 List<WeeklyReportComment> weeklyReportComments = weeklyReportCommentDAO.queryByWeeklyReportIds(weeklyReportIds);
//        	 for (WeeklyReportComment comment : weeklyReportComments) {
//        		 List<WeeklyReportComment> comments = new ArrayList<WeeklyReportComment>(3); 
//        		 if (weeklyReportCommentMap.containsKey(comment.getWeeklyReportId())) {
//        			 weeklyReportCommentMap.get(comment.getWeeklyReportId()).add(comment);
//        		 }else {
//        			 comments.add(comment);
//        			 weeklyReportCommentMap.put(comment.getWeeklyReportId(), comments);
//        		 }
//        		 
//        	 }
//        }
//        
//        inv.addModel("subordinates", subordinates);
//        inv.addModel("weeklyreport_map", weeklyReportMap);
//        inv.addModel("weeklyreport_comment_map", weeklyReportCommentMap);
//        return true;
//    }
//
//    private Set<Integer> getEditableReportIds(int userId) {
//        Date today = TimeUtils.FetchTime.today();// 今天
//        Date monday = DateTimeUtil.getMondayOfWeek(today);// 周一
//        // 确定可以编辑哪些周报
//        // 1. 只允许编辑“本周”和“上周”未提交的周报
//        Set<Integer> editableIds = new HashSet<Integer>();
//        int weekCount = 2;// 两周
//        Date date = monday;
//        while ((--weekCount) >= 0) {
//            WeeklyReport workReport = weeklyReportDAO.getReportOfWeek(userId, date);
//            if (workReport != null && workReport.status() != WeeklyReportStatus.SUBMITTED) {// 周报没有提交
//                editableIds.add(workReport.getId());
//            }
//            date = TimeUtils.Operate.subDate(date, 7);// 上周
//        }
//
//        return editableIds;
//    }
}
 