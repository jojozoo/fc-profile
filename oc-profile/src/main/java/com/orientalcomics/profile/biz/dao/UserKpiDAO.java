package com.orientalcomics.profile.biz.dao;


import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.model.UserKpi;

@DAO(catalog = OcProfileConstants.DB_CATALOG)
public interface UserKpiDAO {

    String TABLE             = "user_kpi";
    // -------- { Column Defines
    String ID               = "id";
    String USER_ID          = "user_id";
    String PERF_TIME_ID     = "perf_time_id";
    String TARGET           = "target";
    String STANDARD_PLAN    = "standard_plan";
    String WEIGHT           = "weight";
    String ACTION_RESULT    = "action_result";
    String SELF_SCORE       = "self_score";
    String LEADER_SCORE     = "leader_score";
    String STATUS           = "status";
    String EDIT_TIME        = "edit_time";
    // -------- } Column Defines

    String FIELD_PK          = "id";
    String FIELDS_WITHOUT_PK = "user_id,perf_time_id,target,standard_plan,weight,action_result,self_score,leader_score,status,edit_time";
    String FIELDS_ALL        = FIELD_PK + "," + FIELDS_WITHOUT_PK;
    
    // 根据perf_time_id和用户id查询出整个perf_time_id绩效设置的工程kpi
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `user_id` = :1 and `perf_time_id` = :2 and `status` >= :3")
    public List<UserKpi> queryBy(int userId,int perf_time_id,int status);
    
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `id` = :1")
    public UserKpi queryById(int id);
    
   // 根据perf_time_id和用户id查询出整个perf_time_id绩效设置的工程kpi不包含状态的过滤
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `user_id` = :1 and `perf_time_id` = :2")
    public List<UserKpi> queryBy(int userId,int perf_time_id);
    
    // 得到指定绩效设置的工程kpi
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `id` = :1")
    public UserKpi queryBy(int id);
    
    /**
     * 得到用户在perfTimeId中的记录数目
     * 
     * @param userId
     * @param perfTimeId
     * @return
     */
    @SQL("select count(" + FIELD_PK + ")  from " + TABLE + " where `user_id` = :1 and `perf_time_id` = :2")
    public Integer countHavePerfTime(int userId,int perfTimeId);


    //,action_result=:model.actionResult,self_score=:model.selfScore,leader_score=:model.leaderScore,status=:model.status,edit_time=:model.editTime
    String SQL_UPDATE_BASE_FILEDS = "target=:model.target,standard_plan=:model.standardPlan,weight=:model.weight,status=:model.status";
    
    String SQL_UPDATE_SELF_FILEDS = "action_result=:model.actionResult,self_score=:model.selfScore,status=:model.status";
    
    String SQL_UPDATE_LEADER_FILEDS = "leader_score=:model.leaderScore,status=:model.status";


    /**
     * 更新kpi的基本信息、
     * 
     * @param model
     * @return
     */
    @SQL("update " + TABLE + " set " + SQL_UPDATE_BASE_FILEDS + " where id= :1.id")
    public int update(@SQLParam("model") UserKpi model);
    
    /**
     * 更新kpi的目标信息
     * 
     * @param id
     * @param target
     * @return
     */
    @SQL("update " + TABLE + " set target=:2 where id= :1")
    public int updateKpiTarget(int id,String target);
    
    /**
     * 更新kpi的目标信息
     * 
     * @param id
     * @param target
     * @return
     */
    @SQL("update " + TABLE + " set standard_plan=:2 where id= :1")
    public int updateKpiPlan(int id,String target);
    
    /**
     * 更新kpi的权重信息
     * 
     * @param id
     * @param target
     * @return
     */
    @SQL("update " + TABLE + " set weight=:2 where id= :1")
    public int updateKpiWeight(int id,float weigth);
    
    /**
     * 更新用户自评评价的kpi信息
     * 
     * @param model
     * @return
     */
    @SQL("update " + TABLE + " set " + SQL_UPDATE_SELF_FILEDS + " where id= :1.id")
    public int updateSelfAccess(@SQLParam("model") UserKpi model);
    
    /**
     * 更新领导对他的KPI户评价的信息
     * 
     * @param model
     * @return
     */
    @SQL("update " + TABLE + " set " + SQL_UPDATE_LEADER_FILEDS + " where id= :1.id")
    public int updateLeaderAccess(@SQLParam("model") UserKpi model);

    @SQL("insert ignore into " + TABLE + " (" + FIELDS_WITHOUT_PK + ") VALUES (:model.userId,:model.perfTimeId,:model.target,:model.standardPlan,:model.weight,:model.actionResult,:model.selfScore,:model.leaderScore,:model.status,:model.editTime)")
    @ReturnGeneratedKeys
    public Integer save(@SQLParam("model") UserKpi model);

    @SQL("delete from " + TABLE + " where `id` = :1")
    public void delete(int id);

}
