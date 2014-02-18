package com.renren.profile.web.config.dao;

import java.util.Map;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;

import com.renren.profile.web.config.model.ProfileConfig;

@DAO
public interface ProfileConfigDAO {
    String TABLE = "profile_config";
    
    @SQL("select id,name,value,des from "+TABLE+" where id = :1")
    ProfileConfig queryOne(int id);
    
    @SQL("select id,name,value,des from "+TABLE+" order by id asc")
    Map<Integer,ProfileConfig> queryAll();

    @SQL("update "+TABLE+" set value=:2 where id = :1")
    void updateValue(int id, String string);
}
