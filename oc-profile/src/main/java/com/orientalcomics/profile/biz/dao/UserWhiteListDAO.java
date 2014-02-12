package com.orientalcomics.profile.biz.dao;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.model.UserWhiteList;

@DAO(catalog = OcProfileConstants.DB_CATALOG)
public interface  UserWhiteListDAO {

    String TABLE             = "user_white_list";
    // -------- { Column Defines
    String ID                = "id";
    String USER_NAME         = "user_name";
    String USER_ID         = "user_id";
    String STATUS          = "status";
    // -------- } Column Defines

    String FIELD_PK          = "id";
    String FIELDS_WITHOUT_PK = "user_name,user_id,status";
    String FIELDS_ALL        = FIELD_PK + "," + FIELDS_WITHOUT_PK;


    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `user_id` = :1 and `status` = :2")
    public UserWhiteList query(int userId,int status);
    
}
