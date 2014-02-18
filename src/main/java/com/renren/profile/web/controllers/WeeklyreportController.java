package com.renren.profile.web.controllers;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.renren.profile.biz.dao.WeeklyReportDAO;
import com.renren.profile.biz.model.WeeklyReport;
import com.renren.profile.constants.ProfileAction;
import com.renren.profile.constants.status.WeeklyReportStatus;
import com.renren.profile.util.DateTimeUtil;
import com.renren.profile.util.time.TimeHandleUtils;
import com.renren.profile.util.time.TimeParseUtils;
import com.renren.profile.util.time.TimeUtils;
import com.renren.profile.web.annotations.AjaxJson;
import com.renren.profile.web.annotations.ProfileHtmlCorrect;
import com.renren.profile.web.annotations.ProfileSecurity;
import com.renren.profile.web.base.FormValidator;
import com.renren.profile.web.base.HtmlPage;
import com.renren.profile.web.config.ProfileConfigs.PageSizeConfigView;
import com.renren.profile.web.controllers.internal.LoginRequiredController;

public class WeeklyreportController extends LoginRequiredController {
    @Autowired
    private WeeklyReportDAO weeklyReportDAO;

    @Get("my")
    public String get_my(Invocation inv, HtmlPage page, @Param("startdate") String pStartDate, @Param("enddate") String pEndDate,
            @Param("curpage") int curPage) {
        int userId = currentUserId();
        // 读取当周日期
        Date today = TimeUtils.FetchTime.today();// 今天
        Date monday = DateTimeUtil.getMondayOfWeek(today);// 周一
        // 判断用户是否已写本周周报
        WeeklyReport editedReport = null;
        WeeklyReport report = weeklyReportDAO.getReportOfWeek(userId, monday);
        if (report == null) {// 还没有周报
            // 尝试插入一条空白的
            WeeklyReport emptyReport = new WeeklyReport();
            emptyReport.setUserId(userId);
            emptyReport.setWeekDate(monday);
            emptyReport.status(WeeklyReportStatus.READY);
            Integer reportId = weeklyReportDAO.insert(emptyReport);
            if (reportId == null) {// 触发重复键
                report = weeklyReportDAO.getReportOfWeek(userId, monday);
                if (report == null) {
                    // DB发生异常
                    // TODO 处理异常哇
                }
            } else {
                emptyReport.setId(reportId);
                report = emptyReport;
            }
        }
        if (report != null) {
            if (report.status() != WeeklyReportStatus.SUBMITTED) {// 如果未提交，则还需要继续编辑
                editedReport = report;
            }
        }
        inv.addModel("editedReport", editedReport);
        // 获取用户的周报记录
        renderReports(inv, page, userId, pStartDate, pEndDate, curPage);
        inv.addModel("editableIds", getEditableReportIds(userId));
        return "weeklyreport_my";
    }

    @Get("my/edit")
    public String get_my(Invocation inv, HtmlPage page, @Param("id") int id, @Param("curpage") int curPage) {
        int userId = currentUserId();
        $: {
            // 查询
            if (id <= 0) {
                return "e:404";
            }
            WeeklyReport report = weeklyReportDAO.query(id);
            if (report == null) {
                return "e:404";
            }
            if (report.getUserId() != userId) {
                return "e:403";
            }

            Set<Integer> editableIds = getEditableReportIds(userId);
            if (!editableIds.contains(id)) {
                return "e:403";
            }

            inv.addModel("editedReport", report);
            inv.addModel("editmode", true);
        }
        return "weeklyreport_my";
    }

    @Post("my/{action:save|submit}")
    @AjaxJson
    public void post_my(HtmlPage page, @Param("action") final String action, @Param("id") final int id,
            @ProfileHtmlCorrect @Param("content_done") final String contentDone,
            @ProfileHtmlCorrect @Param("content_plan") final String contentPlan) {
        int userId = currentUserId();

        $: try {
            // validate
            FormValidator fv = page.formValidator();
            fv.min(id, 1, "week_time", "表单提交错误");
            fv.notEmpty(contentDone, "content_done", "需要填写本周已做的内容");
            fv.notEmpty(contentPlan, "content_plan", "需要填写下周计划的内容");

            if (fv.isFailed()) {
                break $;
            }

            // 查询
            WeeklyReport report = weeklyReportDAO.query(id);
            if (report == null) {
                page.expired();
                break $;
            }
            // Date weekDate = report.getWeekDate();
            boolean isSaveAction = "save".equalsIgnoreCase(action);
            boolean isSubmitAction = "submit".equalsIgnoreCase(action);
            if (report.getUserId() != userId) {
                page.alert("只允许对自己的周报进行编辑操作");
                break $;
            }

            Set<Integer> editableIds = getEditableReportIds(userId);
            if (!editableIds.contains(id)) {
                page.alert("不能编辑这个周报");
                break $;
            }

            if (report.status() == WeeklyReportStatus.SUBMITTED) {// 旧的report已经提交，则要求新的必须是提交操作
                if (!isSubmitAction) {
                    page.alert("此周报已经提交过，只允许再次提交！");
                    break $;
                }
            }
            // 入库
            WeeklyReportStatus newStatus = isSubmitAction ? WeeklyReportStatus.SUBMITTED : isSaveAction ? WeeklyReportStatus.SAVED : WeeklyReportStatus.READY;

            report.setContentDone(contentDone);
            report.setContentPlan(contentPlan);
            report.setStatus(newStatus.getId());
            weeklyReportDAO.update(report);
            // success
            if (isSubmitAction) {
                page.redirect("/weeklyreport/my");
            } else {
                page.info("保存成功");
            }
        } catch (Exception e) {
            LOG.error(e, "id", id, "content_done", StringUtils.abbreviate(contentDone, 10), "content_plan",
                    StringUtils.abbreviate(contentPlan, 10));
            page.error("服务端发生错误");
        }
    }

    @ProfileSecurity(ProfileAction.VIEW_USER_WEEKLY_REPORT)
    @Get("{owner:\\d+}")
    public String get_other(Invocation inv, HtmlPage page, @Param("owner") int owner, @Param("startdate") String pStartDate,
            @Param("enddate") String pEndDate, @Param("curpage") int curPage) {
        int userId = currentUserId();
        if (owner == userId) {
            return "r:my";
        }
        renderReports(inv, page, owner, pStartDate, pEndDate, curPage);
        return "weeklyreport_other";
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

        int total = weeklyReportDAO.countByUserBetweenDate(ownerId, startMonday, endMonday);
        int pageSize = getPageSize(PageSizeConfigView.WEEKLY_REPORT);
        curPage = checkAndReturnPage(curPage, total, pageSize);

        inv.addModel("total", total);
        inv.addModel("pagesize", pageSize);
        inv.addModel("curpage", curPage);

        List<WeeklyReport> weeklyReports = weeklyReportDAO.queryByUserBetweenDate(ownerId, startMonday, endMonday, curPage * pageSize, pageSize);
        inv.addModel("reports", weeklyReports);
    }

    private Set<Integer> getEditableReportIds(int userId) {
        Date today = TimeUtils.FetchTime.today();// 今天
        Date monday = DateTimeUtil.getMondayOfWeek(today);// 周一
        // 确定可以编辑哪些周报
        // 1. 如果本周周报已经提交，则只允许编辑本周的周报
        // 2. 如果本周周报没有提交，则可以编辑上周的周报
        Set<Integer> editableIds = new HashSet<Integer>();
        WeeklyReport workReport = weeklyReportDAO.getReportOfWeek(userId, monday);
        if (workReport != null && workReport.status() == WeeklyReportStatus.SUBMITTED) {// 本周周报已经提交
            editableIds.add(workReport.getId());
        } else {// 本周周报没有提交
            Date lastMonday = TimeUtils.Operate.subDate(new Date(monday.getTime()), 7);// 上周一
            Set<Integer> ids = weeklyReportDAO.getIdSetAfterWeek(userId, lastMonday);
            if (ids != null) {
                editableIds.addAll(ids);
            }
        }
        return editableIds;
    }
}
