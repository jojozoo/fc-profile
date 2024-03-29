package com.renren.profile.biz.dao; 

import java.util.List;

import com.renren.profile.biz.model.Shadow;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;


/** 
 * @author 张浩 E-mail:zhanghao@foundercomics.com 
 * @version 创建时间：2014年2月17日 上午11:56:06 
 * 类说明 
 */

@DAO
public interface ShadowDAO {
	String TABLE             = "shadow";
    // -------- { Column Defines
    String ID                = "user_id";
    String NAME              = "login_name";
    String EMAIL	         = "email";
    String EDIT_TIME         = "login_passwd";
    String EDITOR_ID         = "create_date";
    // -------- } Column Defines

    String FIELD_PK          = "user_id";
    String FIELDS_WITHOUT_PK = "login_name,email,login_passwd";
    String FIELDS_ALL        = FIELD_PK + "," + FIELDS_WITHOUT_PK;

    @SQL("select " + FIELDS_ALL + " from " + TABLE)
    public List<Shadow> queryAll();

    @SQL("select COUNT(" + FIELD_PK + ") from " + TABLE)
    public int countAll();

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `user_id` = :1")
    public Shadow queryById(int id);

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `email` = :1")
    public Shadow queryByEmail(String email);
    
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `login_name` = :1")
    public Shadow queryByName(String name);
    
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `login_name` = :1,`login_passwd`=:2")
    public Shadow query(String name,String passwd);
    
    String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "login_name=:model.loginName,email=:model.email,login_passwd=:model.loginPasswd,create_date=:createDate";

    @SQL("update " + TABLE + " set " + SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK + " where user_id=:1.userId")
    public int update(@SQLParam("model") Shadow model);

    @SQL("insert ignore into " + TABLE + " (" + FIELDS_WITHOUT_PK + ") VALUES (:model.loginName,:model.email,:model.loginPasswd")
    @ReturnGeneratedKeys
    public Integer save(@SQLParam("model") Shadow model);

    @SQL("delete from " + TABLE + " where `user_id` = :1")
    public void delete(int id);
}
 