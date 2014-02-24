package com.orientalcomics.profile.biz.dao;

import java.util.Collection;
import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.orientalcomics.profile.biz.model.WeeklyReportComment;

/**
 * @author 张浩 E-mail:hao.zhang@renren-inc.com
 * @date 2012-4-18 上午11:51:37
 * 
 *       类WeeklyReportCommentDAO
 */
@DAO
public interface WeeklyReportCommentDAO {
    String TABLE             = "weekly_report_comment";
    // -------- { Column Defines
    String ID                = "id";
    String WEEKLY_REPORT_ID  = "weekly_report_id";
    String COMMENT_USER      = "comment_user";
    String COMMENT_USER_NAME = "comment_user_name";
    String COMMENT           = "comment";
    String EDIT_TIME         = "edit_time";
    String WEEK_DATE         = "week_date";
    // -------- } Column Defines

    String FIELD_PK          = "id";
    String FIELDS_WITHOUT_PK = "weekly_report_id,comment_user,comment_user_name,comment,edit_time,week_date";
    String FIELDS_ALL        = FIELD_PK + "," + FIELDS_WITHOUT_PK;

    @SQL("select $FIELDS_ALL from " + TABLE + " where weekly_report_id in (:1)")
    public List<WeeklyReportComment> queryByWeeklyReportIds(Collection<Integer> weeklyReportIds);

    /**
     * query
     * 
     * @param weeklyCommentId
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where id=:1")
    public WeeklyReportComment query(int weeklyCommentId);

    /**
     * query
     * 
     * @param userId
     * @param reportId
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where comment_user=:1 and weekly_report_id=:2")
    public WeeklyReportComment query(int userId, int reportId);

    @SQL("insert ignore into " + TABLE + " (" + FIELDS_WITHOUT_PK
            + ") VALUES (:model.weeklyReportId,:model.commentUser,:model.commentUserName,:model.comment,:model.editTime,:model.weekDate)")
    @ReturnGeneratedKeys
    public Integer insert(@SQLParam("model") WeeklyReportComment model);

    @SQL("delete from " + TABLE + " where `id` = :1")
    public void delete(int id);
}
