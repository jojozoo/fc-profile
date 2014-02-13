package com.orientalcomics.profile.biz.dao;

import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;

import com.orientalcomics.profile.biz.model.SystemPage;

@DAO
public interface SystemPageDAO {
	
	String TABLE             = "system_page";
	
	// -------- { Column Defines
    String ID                = "id";
    String KEY          	 = "system_key";
    String VALUE             = "system_value";
    String MARK	             = "mark";
    String DESCRIPT          = "descript";
    // -------- } Column Defines
    String FIELD_PK          = "id";
    String FIELDS_WITHOUT_PK = "system_key,system_value,descript";
    String FIELDS_ALL        = FIELD_PK + "," + FIELDS_WITHOUT_PK;
    
    // 根据不同的mark，查询出对应的value/value值
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `mark`=:1 order by system_value")
    public List<SystemPage> queryScoreCountByUserId(int mark);
    
    //根据主键查询
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `id`=:1")
    public SystemPage getSystemPageById(int id);

}
