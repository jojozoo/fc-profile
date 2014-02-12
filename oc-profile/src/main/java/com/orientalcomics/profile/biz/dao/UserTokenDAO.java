package com.orientalcomics.profile.biz.dao;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.model.UserToken;

@DAO(catalog = OcProfileConstants.DB_CATALOG)
public interface UserTokenDAO {
	String TABLE = "user_token";
	// -------- { Column Defines
	String USER_ID 			= "user_id";
	String TOKEN   			= "token";
    String EXPIRED_TIME            = "expired_time";
    String UPDATE_TIME            = "update_time";
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
