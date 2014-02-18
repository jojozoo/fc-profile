package com.renren.profile.biz.dao;

import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.renren.profile.RenrenProfileConstants;
import com.renren.profile.biz.model.Resume;

@DAO(catalog = RenrenProfileConstants.DB_CATALOG)
public interface ResumeDAO {
  String TABLE = "resume";
  // -------- { Column Defines
  String USER_ID = "user_id";
  String CONTENT = "content";
  // -------- } Column Defines
  
     		  		  
  String FIELD_PK = "user_id";
  String FIELDS_WITHOUT_PK = "content";
  String FIELDS_ALL = FIELD_PK+","+FIELDS_WITHOUT_PK;

  @SQL("select "+FIELDS_ALL+" from "+TABLE+" limit :offset,:count")
  public List<Resume> queryAll(@SQLParam("offset")int offset,@SQLParam("count") int count);
		
  @SQL("select COUNT("+FIELD_PK+") from "+TABLE)
  public int countAll();
  
  /**
   * 按用户ID查询简历记录
   * @param userId
   * @return
   */
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `user_id` = :1")
  public Resume query(int userId);
  
  
  String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "content=:model.content";
  
  @SQL("update "+TABLE+" set "+SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK+" where user_id=:1.userId")
  public Integer update(@SQLParam("model") Resume model);
  
  @SQL("insert ignore into " + TABLE +" ("+FIELDS_ALL+") VALUES (:model.userId,:model.content)")
  public Integer save(@SQLParam("model") Resume model);
  
  @SQL("delete from "+TABLE+" where `user_id` = :1")
  public void delete(int userId);
}