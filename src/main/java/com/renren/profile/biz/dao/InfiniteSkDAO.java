package com.renren.profile.biz.dao;


import com.renren.profile.biz.model.access.InfiniteSk;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;


@DAO
public interface InfiniteSkDAO {

	
    final String SELECT_FILEDS = " user_id, app_id, session_key, session_secret ";
    
	final String INSERT_FILEDS = " user_id, app_id, session_key, session_secret ";
	
	final String TABLE = " sk_infinite ";
	
	@SQL("insert into  " +TABLE + "(" + INSERT_FILEDS + ")"+
    " values (:inSK.userId,:inSK.appId,:inSK.sessionKey,:inSK.sessionSecret)")
	public int generateKeys(@SQLParam("inSK") InfiniteSk inSK) ;
	
	@SQL("delete from  " +TABLE + " where user_id=:userid and app_id=:appId " )
	public void delete(@SQLParam("userId") long userId, @SQLParam("appId") int appId);

	@SQL("select  " + SELECT_FILEDS + " from "+ TABLE + " where user_id=:userId and app_id=:appId " )
	public InfiniteSk get(@SQLParam("userId") long userId, @SQLParam("appId") int appId);
	

}
