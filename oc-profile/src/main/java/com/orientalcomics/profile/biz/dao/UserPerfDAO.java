package com.orientalcomics.profile.biz.dao;

import java.util.Collection;
import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.model.UserPerf;

@DAO(catalog = OcProfileConstants.DB_CATALOG)
public interface UserPerfDAO {
    String TABLE             = "user_perf";
    // -------- { Column Defines
    String ID                = "id";
    String USER_ID           = "user_id";
    String EDIT_TIME         = "edit_time";
    String PERF_TIME_ID      = "perf_time_id";
    String STATUS            = "status";
    String ADVANTAGE         = "advantage";
    String DISADVANTAGE      = "disadvantage";
    String IS_PROMOTION      = "is_promotion";
    String PROMOTION_REASON  = "promotion_reason";
    String USER_NAME         = "user_name";
    String PERF_SCORE        = "perf_score";
    String LEADER_ID         = "leader_id";
    String LEADER_NAME       = "leader_name";
    String ADJUST_SCORE      = "adjust_score";
    // -------- } Column Defines

    String FIELD_PK          = "id";
    String FIELDS_WITHOUT_PK = "user_id,edit_time,perf_time_id,status,advantage,disadvantage,is_promotion,promotion_reason,user_name,perf_score,leader_id,leader_name,adjust_score";
    String FIELDS_ALL        = FIELD_PK + "," + FIELDS_WITHOUT_PK;

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " limit :offset,:count")
    public List<UserPerf> queryAll(@SQLParam("offset") int offset, @SQLParam("count") int count);

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where user_id = :1 order by id desc limit :offset,:count")
    public List<UserPerf> queryAllByUser(int userId, @SQLParam("offset") int offset, @SQLParam("count") int count);

    @SQL("select COUNT(" + FIELD_PK + ") from " + TABLE + " where user_id=:1")
    public int countAllByUserId(int userId);

    @SQL("select COUNT(" + FIELD_PK + ") from " + TABLE)
    public int countAll();

    /**
     * 查询当前perf申请升职的数量
     * 
     * @param perf_time_id
     * @return
     */
    @SQL("select COUNT(" + FIELD_PK + ") from " + TABLE + " where `is_promotion` = 1 and `perf_time_id`=:perfTimeId ;")
    public int countPromotion(@SQLParam("perfTimeId") int perf_time_id);

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `id` = :1")
    public UserPerf query(int id);

    // 得到用户自评的状态信息
    @SQL("select status from " + TABLE + " where `user_id` = :1  and `perf_time_id` = :2 order by id desc limit 1")
    public int queryPeerPerfStatus(int id, int perfId);

    /**
     * 按userId,perfTimeId查询
     * 
     * @param userId
     * @param perfTimeId
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `user_id` = :1 and `perf_time_id` = :2")
    public UserPerf queryByUserIdAndPerfTimeID(int userId, int perfTimeId);

    /**
     * 批量查询当前季度哪些用户对自己的自评已经提交
     * 
     * @param perfTimeId
     * @param userIds
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `perf_time_id` = :1 and `status` = 2 and `user_id` in  (:2) ")
    public Collection<UserPerf> batchQueryByUserIdAndPerfTimeId(int perfTimeId, Collection<Integer> userIds);
    
    /**
     * 批量查询当前季度哪些用户对自己的自评已经提交
     * 
     * @param perfTimeId
     * @param userIds
     * @return
     */
    @SQL("select " + USER_ID + " from " + TABLE + " where `perf_time_id` = :1 and `status` = 1")
    public Collection<Integer> batchQueryByUserIdAndPerfTimeId(int perfTimeId);
    
    
    /**
     * 批量查询当前季度哪些用户IDs对自己的自评还未提交
     * 
     * @param perfTimeId
     * @return
     */
    @SQL("select " + USER_ID + " from " + TABLE + " where `perf_time_id` = :1 and `status` = 0 ")
    public Collection<Integer> batchCollectionNoSelfPerfIds(int perfTimeId);

    /**
     * 按页码查询申请升职列表
     * 
     * @param perfTimeId
     * @param offset
     * @param count
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `is_promotion` = 1 and `perf_time_id`=:1 order by id limit :2,:3 ;")
    public List<UserPerf> queryPromotionList(int perfTimeId, int offset, int count);

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `user_id` = :1 and `perf_time_id` in (:2)")
    public List<UserPerf> queryAllByPerfTimes(int userId, Collection<Integer> perfTimeIds);

    String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "user_id=:model.userId,edit_time=:model.editTime,perf_time_id=:model.perfTimeId,status=:model.status,advantage=:model.advantage,disadvantage=:model.disadvantage,is_promotion=:model.isPromotion,promotion_reason=:model.promotionReason,user_name=:model.userName,perf_score=:model.perfScore,leader_id=:model.leaderId,leader_name=:model.leaderName,adjust_score=:model.adjustScore";

    @SQL("update " + TABLE + " set " + SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK + " where id=:1.id")
    public Integer update(@SQLParam("model") UserPerf model);
    
    @SQL("update " + TABLE + " set perf_score=:1 where user_id=:2 and perf_time_id=:3")
    public Integer updateByPerfScore(String perfScore,int userId,int perfTimeId);
    
    @SQL("update " + TABLE + " set adjust_score=:1 where user_id=:2 and perf_time_id=:3")
    public Integer updateAdjustScore(int perfScore,int userId,int perfTimeId);
    

    @SQL("insert ignore into "
            + TABLE
            + " ("
            + FIELDS_WITHOUT_PK
            + ") VALUES (:model.userId,:model.editTime,:model.perfTimeId,:model.status,:model.advantage,:model.disadvantage,:model.isPromotion,:model.promotionReason,:model.userName,:model.perfScore,:model.leaderId,:model.leaderName,:model.adjustScore)")
    @ReturnGeneratedKeys
    public Integer save(@SQLParam("model") UserPerf model);

    @SQL("delete from " + TABLE + " where `id` = :1")
    public void delete(int id);

}