package com.orientalcomics.profile.biz.dao;

import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.model.Honor;
import com.orientalcomics.profile.biz.model.RewardFlower;
@DAO(catalog = OcProfileConstants.DB_CATALOG)
public interface HonorDAO {
	
	String TABLE = "honor";
	// -------- { Column Defines
	String ID 				= "id";
	String USER_ID 			= "user_id";
	String NAME   			= "honor_name";
	String REWARD_ITEM_ID   = "reward_item_id";
	String REWARD_DATE      = "reward_date";
	// -------- } Column Defines

	String FIELD_PK = "id";
	String FIELDS_WITHOUT_PK = "user_id,honor_name,reward_item_id,reward_date";
	String FIELDS_ALL = FIELD_PK + "," + FIELDS_WITHOUT_PK;
	
	@SQL("select " + FIELDS_ALL + " from " + TABLE + " where user_id=:1")
	public List<RewardFlower> queryByUserId(@SQLParam("userId") int userId);
	
	@SQL("insert ignore into " + TABLE + " (" + FIELDS_WITHOUT_PK + ") VALUES (:model.userId,:model.honorName,:model.rewardItemId,:model.rewardDate)")
	@ReturnGeneratedKeys
	public Integer save(@SQLParam("model") Honor model);

}
