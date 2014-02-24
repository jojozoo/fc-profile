package com.orientalcomics.profile.biz.dao;

import java.util.Collection;
import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.model.RewardFlower;
@DAO(catalog = OcProfileConstants.DB_CATALOG)
public interface RewardFlowerDAO {

	String TABLE = "reward_flower";
	// -------- { Column Defines
	String ID 				= "id";
	String FROM_ID 			= "from_id";
	String TO_ID   			= "to_id";
	String PERF_TIME_ID 	= "perf_time_id";
	String REASON 			= "reason";
	String IMAGE_URL        = "image_url";
	String STATUS 			= "status";
	String USER_NAME 	    = "user_name";
	String EDIT_TIME 		= "edit_time";
	
	// -------- } Column Defines

	String FIELD_PK = "id";
	String FIELDS_WITHOUT_PK = "from_id,to_id,perf_time_id,reason,image_url,status,user_name,edit_time";
	String FIELDS_ALL = FIELD_PK + "," + FIELDS_WITHOUT_PK;
	
	// 我接收的小红花列表只包含主管确认同意和已经兑换的
	@SQL("select " + FIELDS_ALL + " from " + TABLE + " where `to_id`=:1 and `status`>=:2 and `perf_time_id` in (:3)")
	public List<RewardFlower> queryByPerfTimeAndUserIdAndStatus(@SQLParam("toId") int toId,@SQLParam("status") int status,@SQLParam("perfTimeIds") Collection<Integer> perfTimeIds);
	
	@SQL("select " + FIELDS_ALL + " from " + TABLE + " where `to_id`=:1 and `status`>=:2 and `perf_time_id`=:3")
	public List<RewardFlower> queryByPerfTimeOfQuartorAndUserIdAndStatus(int toId,int status,int perfTimeId);
	
	// 查询自己赠送给别人的小红花,指定了季度
	@SQL("select " + TO_ID + " from " + TABLE + " where `from_id`=:1 and `status`>=:2 and `perf_time_id`=:3")
	public List<Integer> queryByPerfTimeOfQuartorAndFromIdAndStatus(int fromId,int status,int perfTimeId);
	
	// 查询自己赠送给别人的小红花,指定了季度
	@SQL("select " + FIELDS_ALL + " from " + TABLE + " where `from_id`=:1 and `to_id`=:2 and `status`>=:3 and `perf_time_id`=:4 limit 1")
	public RewardFlower queryByPerfTimeOfQuartor(int fromId,int toId ,int status,int perfTimeId);
	
	// 查询自己赠送给别人的小红花,指定了年
	@SQL("select " + FIELDS_ALL + " from " + TABLE + " where `from_id`=:1 and `to_id`=:2 and `status`>=:3 and `perf_time_id` in (:4) limit 1")
	public RewardFlower queryByPerfTimeOfYear(int fromId,int toId ,int status,Collection<Integer> perfTimeIds);
	
	/**
	 * 指定季度所有没有确认的小红花
	 * 
	 * @param perfTimeId
	 * @return
	 */
	@SQL("select " + FIELDS_ALL + " from " + TABLE + " where `status`=1 and `perf_time_id`=:1")
	public List<RewardFlower> queryByPerfTime(int perfTimeId);
	
	// 查询自己赠送给别人的小红花,指定了年
	@SQL("select " + TO_ID + " from " + TABLE + " where `from_id`=:1 and `status`>=:2 and `perf_time_id` in (:3)")
	public List<Integer> queryByPerfTimeOfYearAndFromIdAndStatus(int fromId,int status,Collection<Integer> perfTimeIds);
	
	/**
	 * 查询别人在本季度发送小红花给自己
	 * 
	 * @param fromId
	 * @param toId
	 * @param status
	 * @param perfTimeId
	 * @return
	 */
	@SQL("select " + FIELDS_ALL + " from " + TABLE + " where `from_id`=:1 and `to_id`=:2 and `status`>=:3 and `perf_time_id`=:4 limit 1")
	public RewardFlower querySendFlowerFromby(int fromId,int toId,int status,int perfTimeId);
	
	/**
	 * 统计自己获得的小红花
	 * 
	 * @param toId
	 * @param status 3,4：表示统计获得的小红花除了，兑换的小红花
	 * @return
	 */
	@SQL("select count(id) from " + TABLE + " where `to_id`=:1 and `status`=:2")
	public Integer countReward(@SQLParam("toId") int toId,@SQLParam("status") int status);
	
	@SQL("select count(id) from " + TABLE + " where `to_id`=:1 and `status`>=:2")
	public Integer getRewardFlower(@SQLParam("toId") int toId,@SQLParam("status") int status);
	
	/**
	 * 兑换小红花以后，更改状态
	 * @param num
	 */
	/**
	 * This version of MySQL doesn't yet support 'LIMIT & IN/ALL/ANY/SOME subquery'
	 * @SQL("update " + TABLE + " set `status`=4 where id in(select id from " + TABLE + " where to_id=:1 order by edit_time desc limit :2 )")
	 */
	@SQL("update " + TABLE + " set `status`=4 where id in(select id from (select * from " + TABLE + " where to_id=:1 order by edit_time desc limit :2 ) as tmp1) ")
	public void changeStateForNum(int userId,int num);
	
	@SQL("select count(id) from " + TABLE + " where `from_id`=:1 and `status`>=:2 and `perf_time_id`=:3")
	public Integer countFlowerByPerfTimeId(@SQLParam("toId") int toId,@SQLParam("status") int status,@SQLParam("perfTimeId") int perfTimeId);
	
	@SQL("select count(id) from " + TABLE + " where `from_id`=:1 and `status`!=:2 and `perf_time_id`=:3")
	public Integer countFlowerByPerfTimeIdNotContainLeaderDisagree(@SQLParam("toId") int toId,@SQLParam("status") int status,@SQLParam("perfTimeId") int perfTimeId);
	
	@SQL("insert ignore into " + TABLE + " (" + FIELDS_WITHOUT_PK + ") VALUES (:model.fromId,:model.toId,:model.perfTimeId,:model.reason,:model.imageUrl,:model.status,:model.userName,:model.editTime)")
	@ReturnGeneratedKeys
	public Integer save(@SQLParam("model") RewardFlower model);
	
    String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "status=:1";

    @SQL("update " + TABLE + " set " + SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK + " where from_id=:2 and to_id=:3 and perf_time_id=:4")
    public Integer updateStatus(int status,int fromId ,int toId,int perfTimeId);

}
