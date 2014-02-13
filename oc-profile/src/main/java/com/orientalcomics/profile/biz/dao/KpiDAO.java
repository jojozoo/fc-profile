package com.orientalcomics.profile.biz.dao;

import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.model.Kpi;

@DAO
public interface KpiDAO {
  String TABLE = "kpi";
  // -------- { Column Defines
  String ID = "id";
  String QUARTER_TIME = "quarter_time";
  String USER_ID = "user_id";
  String TITLE = "title";
  String STATUS = "status";
  String CONTENT = "content";
  // -------- } Column Defines
  
     		  		  			  		  		  		  
  String FIELD_PK = "id";
  String FIELDS_WITHOUT_PK = "quarter_time,user_id,title,status,content";
  String FIELDS_ALL = FIELD_PK+","+FIELDS_WITHOUT_PK;

  @SQL("select "+FIELDS_ALL+" from "+TABLE+" limit :offset,:count")
  public List<Kpi> queryAll(@SQLParam("offset")int offset,@SQLParam("count") int count);
	
  /**
   * 返回totalCount
   * @return
   */
  @SQL("select COUNT("+FIELD_PK+") from "+TABLE)
  public int countAll();
  
  /**
  * 返回用户的KpiCount
  * @param userId
  * @return
  */
  @SQL("select COUNT("+FIELD_PK+") from "+TABLE + " where user_id=:1")
  public int countUserKpi(int userId);
  
  /**
   * 查询特定的KPI记录
   * @param id
   * @return
   */
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where id = :1")
  public Kpi query(int id);
  
  /**
   * 按用户和KPI时间查询，保证每个KPI时间都只有一条记录
   * @param userId
   * @param QuarterTime
   * @return
   */
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where user_id = :1 and quarter_time = :2")
  public Kpi  queryByQuarterTime(int userId,int quarterTime);
  
  
  
  /**
   * 查询用户最后一条记录的ID，用于验证是否可以edit
   * @param id
   * @return
   */
  @SQL("select "+ID+" from "+TABLE+" where user_id = :1 order by id desc limit 1;")
  public int queryFirstKpiId(int userId);
  
  /**
   * 查询某一用户的KPI记录
   * @param user_id
   * @return
   */
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `user_id` = :1 order by id desc")
  public List<Kpi> queryList(int user_id);
  
 /**
  * 查询某一用户的KPI记录,pageSize
  * @param user_id
  * @param offset
  * @param count
  * @return
  */
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `user_id` = :1 order by id desc limit :2,:3")
  public List<Kpi> queryUserKpiByPage(int user_id,int offset,int count);
  
  String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "quarter_time=:model.quarterTime,user_id=:model.userId,title=:model.title,status=:model.status,content=:model.content";
  
  @SQL("update "+TABLE+" set "+SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK+" where id=:1.id")
  public int update(@SQLParam("model") Kpi model);
  
  @SQL("insert ignore into " + TABLE +" ("+FIELDS_WITHOUT_PK+") VALUES (:model.quarterTime,:model.userId,:model.title,:model.status,:model.content)")
  @ReturnGeneratedKeys
  public Integer save(@SQLParam("model") Kpi model);
  
  @SQL("delete from "+TABLE+" where `id` = :1")
  public void delete(int id);
}