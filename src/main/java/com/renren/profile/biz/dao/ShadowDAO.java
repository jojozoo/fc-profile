package com.renren.profile.biz.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.renren.profile.biz.model.UserShadow;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

@DAO
public interface ShadowDAO {
	String TABLE             = "shadow";
    // -------- { Column Defines
    String USER_ID           = "user_id";
    String LOGIN_NAME         = "login_name";
    String LOGIN_PASSWD       = "login_passwd";
    // -------- } Column Defines

    String FIELD_PK          = "id";
    String FIELDS_WITHOUT_PK = "user_id,login_name,login_passwd";
    String FIELDS_ALL        = FIELD_PK + "," + FIELDS_WITHOUT_PK;


    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `login_name` = :1 and `login_passwd`=:2")
    public UserShadow query(String loginName,String loginPasswd);

    String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "login_name=:model.loginName,login_passwd=:model.loginPasswd";

    @SQL("update " + TABLE + " set " + SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK + " where user_id=:1.userId")
    public int update(@SQLParam("model") UserShadow model);

    @SQL("insert ignore into " + TABLE + " (" + FIELDS_ALL + ") VALUES (:model.userId,:model.loginName,:model.loginPasswd)")
    @ReturnGeneratedKeys
    public Integer save(@SQLParam("model") UserShadow model);

    @SQL("delete from " + TABLE + " where `user_id` = :1")
    public void delete(int userId);

}
