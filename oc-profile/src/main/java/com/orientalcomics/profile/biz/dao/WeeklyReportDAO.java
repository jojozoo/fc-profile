package com.orientalcomics.profile.biz.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.orientalcomics.profile.biz.model.WeeklyReport;
import com.orientalcomics.profile.constants.status.WeeklyReportStatus;

@DAO
public interface WeeklyReportDAO {
    String TABLE             = "weekly_report";
    // -------- { Column Defines
    String ID                = "id";
    String USER_ID           = "user_id";
    String WEEK_DATE         = "week_date";
    String STATUS            = "status";
    String EMAIL_TOS         = "email_tos";
    String IS_SUPPLEMENTARY  = "is_supplementary";
    String CONTENT_DONE      = "content_done";
    String CONTENT_PLAN      = "content_plan";
    String Q_A               = "qa";
    // -------- } Column Defines

    String FIELD_PK          = "id";
    String FIELDS_WITHOUT_PK = "user_id,week_date,status,email_tos,is_supplementary,content_done,content_plan,qa";
    String FIELDS_ALL        = FIELD_PK + "," + FIELDS_WITHOUT_PK;

    String BETWEEN_DATE      = " #if(:start!=null){ and week_date >= date(:start) } #if(:end!=null){ and week_date <= date(:end) }";

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " limit :offset,:count")
    public List<WeeklyReport> queryAll(@SQLParam("offset") int offset, @SQLParam("count") int count);

    @SQL("select COUNT(" + FIELD_PK + ") from " + TABLE)
    public int countAll();

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `id` = :1")
    public WeeklyReport query(int id);

    @SQL("select $FIELDS_ALL from " + TABLE + " where `user_id` = :1 order by week_date desc limit 1")
    public WeeklyReport getLastestReport(int userId);

    @SQL("select $FIELDS_ALL from " + TABLE + " where `user_id` = :1 and week_date < date(:2) order by week_date desc limit 1")
    public WeeklyReport getLastestReportBefore(int userId, Date weekDate);

    @SQL("select email_tos from " + TABLE + " where `user_id` = :1 and week_date < date(:2) and email_tos!='' order by week_date desc limit 1")
    public String getLastestNonBlankEmailTosBefore(int userId, Date weekDate);

    /**
     * 判断是否存在某周的周报
     * 
     * @param userId
     * @param weekMonday
     *            星期的周一时间
     * @return
     */
    @SQL("select $FIELDS_ALL from " + TABLE + " where `user_id` = :1 and week_date = date(:2) limit 1")
    public WeeklyReport getReportOfWeek(int userId, Date weekMonday);

    /**
     * 查出当前周，写了周报的人的id
     * 
     * @param status
     * @param weekMonday
     * @return
     */
    @SQL("select user_id from " + TABLE + " where status=:1.id and week_date = date(:2)")
    public Collection<Integer> getReportedIdsOfWeek(WeeklyReportStatus status, Date weekMonday);

    @SQL("select id from " + TABLE + " where `user_id` = :1 and week_date >= date(:2)")
    public Set<Integer> getIdSetAfterWeek(int userId, Date startWeekMonday);

    @SQL("select count(1) from $TABLE where `user_id` = :1 " + BETWEEN_DATE)
    public int countByUserBetweenDate(int userId, @SQLParam("start") Date startWeekMonday, @SQLParam("end") Date endWeekMonday);

    @SQL("select $FIELDS_ALL from $TABLE where `user_id` in (:1) and week_date = date(:2)")
    public List<WeeklyReport> queryByUserIdsInWeekDate(Collection<Integer> userIds, Date weekMonday);

    /**
     * 获取用户的周报记录（按时间降序排列）
     * 
     * @param userId
     *            用户的Id
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `user_id` = :1  " + BETWEEN_DATE + " order by week_date desc limit :offset,:count")
    public List<WeeklyReport> queryByUserBetweenDate(int userId, @SQLParam("start") Date startWeekMonday, @SQLParam("end") Date endWeekMonday,
            @SQLParam("offset") int offset, @SQLParam("count") int count);

    String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "user_id=:model.userId,week_date=:model.weekDate,status=:model.status,email_tos=:model.emailTos,is_supplementary=:model.isSupplementary,content_done=:model.contentDone,content_plan=:model.contentPlan,qa=:model.qa";

    @SQL("update " + TABLE + " set " + SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK + " where id=:1.id")
    public int update(@SQLParam("model") WeeklyReport model);

    @SQL("insert ignore into " + TABLE + " (" + FIELDS_WITHOUT_PK
            + ") VALUES (:model.userId,:model.weekDate,:model.status,:model.emailTos,:model.isSupplementary,:model.contentDone,:model.contentPlan,:model.qa)")
    @ReturnGeneratedKeys
    public Integer insert(@SQLParam("model") WeeklyReport model);

    @SQL("delete from " + TABLE + " where `id` = :1")
    public void delete(int id);

}