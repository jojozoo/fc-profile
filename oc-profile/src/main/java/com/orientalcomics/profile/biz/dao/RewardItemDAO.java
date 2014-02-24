package com.orientalcomics.profile.biz.dao;

import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.model.RewardItem;

/**
 * @author 张浩 E-mail:hao.zhang@renren-inc.com
 * @date 2012-4-16 下午12:29:49
 *
 * 可以兑换的奖励列表,DAO
 */

@DAO(catalog = OcProfileConstants.DB_CATALOG)
public interface RewardItemDAO {
	
	String TABLE = "reward_items";
	// -------- { Column Defines
	String ID 				= "id";
	String NAME 			= "name";
	String TYPE   			= "type";
	String NEED_NUM 		= "need_num";
	String IMG 				= "img";
	// -------- } Column Defines

	String FIELD_PK = "id";
	String FIELDS_WITHOUT_PK = "name,type,need_num,img";
	String FIELDS_ALL = FIELD_PK + "," + FIELDS_WITHOUT_PK;
	
//	// 我接收的小红花列表只包含主管确认同意和已经兑换的
//	@SQL("select " + FIELDS_ALL + " from " + TABLE + " where `to_id`=:1 and `status`>=:2 and `perf_time_id` in (:3)")
//	public List<RewardItem> queryByPerfTimeAndUserIdAndStatus(@SQLParam("toId") int toId,@SQLParam("status") String status,@SQLParam("perfTimeIds") Collection<Integer> perfTimeIds);
//	
//	@SQL("select " + FIELDS_ALL + " from " + TABLE + " where `to_id`=:1 and `status`>=:2 and `perf_time_id`=:3")
//	public List<RewardItem> queryByPerfTimeOfQuartorAndUserIdAndStatus(int toId,String status,int perfTimeId);
//	
//	@SQL("select count(id) from " + TABLE + " where `from_id`=:1 and `status`>=:2 and `perf_time_id`=:3")
//	public Integer countFlowerByPerfTimeId(@SQLParam("toId") int toId,@SQLParam("status") String status,@SQLParam("perfTimeId") int perfTimeId);
	
	@SQL("select " + FIELDS_ALL +  " from " + TABLE + " where `id`=:1")
	public RewardItem query(int rewardItemId);
	
	
	/**
	 * 查询小红花对应的奖励列表
	 */
	@SQL("select " + FIELDS_ALL +  " from " + TABLE + " where `need_num`<=:1 and `type`=:2")
	public List<RewardItem> queryByNeedNum(int needNum,int type);
	
	@SQL("insert ignore into " + TABLE + " (" + FIELDS_WITHOUT_PK + ") VALUES (:model.name,:model.type,:model.need_num,:mode.img)")
	@ReturnGeneratedKeys
	public Integer save(@SQLParam("model") RewardItem model);
}
