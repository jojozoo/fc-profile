package com.orientalcomics.profile.biz.dao;

import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.model.PerfTime;

@DAO(catalog = OcProfileConstants.DB_CATALOG)
public interface PerfTimeDAO {
    String TABLE             = "perf_time";
    // -------- { Column Defines
    String ID                = "id";
    String PERF_TITLE        = "perf_title";
    String PROMOTION_STATUS  = "promotion_status";
    String START_TIME        = "start_time";
    String END_TIME          = "end_time";
    String EDITOR_ID         = "editor_id";
    String STATUS            = "status";
    String PERF_YEAR         = "perf_year";
    String IS_PUBLIC         = "is_public";
    // -------- } Column Defines

    String FIELD_PK          = "id";
    String FIELDS_WITHOUT_PK = "perf_title,promotion_status,start_time,end_time,editor_id,status,perf_year,is_public";
    String FIELDS_ALL        = FIELD_PK + "," + FIELDS_WITHOUT_PK;

    /**
     * 按偏移量查询绩效时间表
     * 
     * @param offset
     * @param count
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " order by start_time desc limit :offset,:count")
    public List<PerfTime> queryAll(@SQLParam("offset") int offset, @SQLParam("count") int count);

    @SQL("select COUNT(" + FIELD_PK + ") from " + TABLE)
    public int countAll();

    /**
     * 按年去重后，查询总数，分页用
     * 
     * @return
     */
    @SQL("select COUNT(DISTINCT " + PERF_YEAR + ") from  " + TABLE)
    public int countAllYear();

    /**
     * 按偏移量查询出当前页的年份集合
     * 
     * @param offset
     * @param count
     * @return
     */
    @SQL("select " + PERF_YEAR + " from " + TABLE + " where status>=0 group by perf_year desc limit :offset,:count")
    public List<Integer> queryYears(@SQLParam("offset") int offset, @SQLParam("count") int count);

    /**
     * 按年的绩效列表
     * 
     * @param perfYear
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where status>=0 and `perf_year` = :1")
    public List<PerfTime> queryByYear(int perfYear);
    
    /**
     * 
     * @param perfTimeId
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where status>0 and `id` = :1")
    public PerfTime queryBy(int perfTimeId);
    
    /**
     * 
     * @param perfTimeId
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where status>=0 and `id` = :1")
    public PerfTime queryByNOStatus(int perfTimeId);
    
    
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where status>0 and `perf_title` = :1 limit 1")
    public PerfTime queryByQuartor(String perfQuartor);

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `id` = :1")
    public PerfTime query(int id);

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where status in (:1) order by id desc limit 1")
    public PerfTime queryLastestWithStatuses(List<Integer> statuses);

    /**
     * 查询当前最新的绩效记录,未开始的不算
     * 
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where status != 0 order by id desc limit 1")
    public PerfTime queryLastest();

    String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "perf_title=:model.perfTitle,promotion_status=:model.promotionStatus,start_time=:model.startTime,end_time=:model.endTime,editor_id=:model.editorId,status=:model.status,perf_year=:model.perfYear,is_public=:model.isPublic";

    @SQL("update " + TABLE + " set " + SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK + " where id=:1.id")
    public int update(@SQLParam("model") PerfTime model);

    @SQL("insert ignore into " + TABLE + " (" + FIELDS_WITHOUT_PK
            + ") VALUES (:model.perfTitle,:model.promotionStatus,:model.startTime,:model.endTime,:model.editorId,:model.status,:model.perfYear,:model.isPublic)")
    @ReturnGeneratedKeys
    public Integer save(@SQLParam("model") PerfTime model);

    @SQL("delete from " + TABLE + " where `id` = :1")
    public void delete(int id);

}