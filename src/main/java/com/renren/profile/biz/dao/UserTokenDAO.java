package com.renren.profile.biz.dao;

import com.renren.profile.biz.model.access.UserToken;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;


@DAO
public interface UserTokenDAO {
	String TABLE = "user_token";
	// -------- { Column Defines
	String USER_ID 			= "user_id";
	String TOKEN   			= "token";
	// -------- } Column Defines

	String FIELD_PK = "user_id";
	String FIELDS_WITHOUT_PK = "token,expired_time,update_time";
	String FIELDS_ALL = FIELD_PK + "," + FIELDS_WITHOUT_PK;
	
	@SQL("select " + FIELDS_ALL + " from " + TABLE + " where user_id=:1 limit 1")
	public UserToken query(@SQLParam("userId") int userId);
	
    
    @SQL("delete from " + TABLE + " where user_id=:1")
    public void delete(@SQLParam("userId") int userId);
    
	@SQL("insert into " + TABLE + " (" + FIELDS_ALL + ") VALUES (:model.userId,:model.token,:model.expiredTime,now()) ON DUPLICATE KEY UPDATE token=values(token),expired_time=values(expired_time),update_time=values(update_time)")
	public Integer update(@SQLParam("model") UserToken model);

}
