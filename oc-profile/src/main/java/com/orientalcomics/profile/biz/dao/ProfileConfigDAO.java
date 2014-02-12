package com.orientalcomics.profile.biz.dao;

import java.util.List;
import java.util.Map;

import com.orientalcomics.profile.biz.model.ProfileConfig;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;


@DAO
public interface ProfileConfigDAO {
    String TABLE = "profile_config";
    
    @SQL("select id,name,value,des from "+TABLE+" where id = :1")
    ProfileConfig queryOne(int id);
    
    @SQL("select id,name,value,des from "+TABLE+" where id <= 15 order by id asc")
    Map<Integer,ProfileConfig> queryAll();
    
    @SQL("select id,name,value,des from "+TABLE+" where id > 15 order by id asc")
    List<ProfileConfig> queryNav();

    @SQL("update "+TABLE+" set value=:2 where id = :1")
    void updateValue(int id, String string);
}
