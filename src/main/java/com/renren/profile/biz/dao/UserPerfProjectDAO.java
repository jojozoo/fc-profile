package com.renren.profile.biz.dao;

import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.renren.profile.RenrenProfileConstants;
import com.renren.profile.biz.model.UserPerfProject;

@DAO(catalog = RenrenProfileConstants.DB_CATALOG)
public interface UserPerfProjectDAO {
  String TABLE = "user_perf_project";
  // -------- { Column Defines
  String ID = "id";
  String USER_ID = "user_id";
  String USER_PERF_ID = "user_perf_id";
  String PROJECT_NAME = "project_name";
  String EDIT_TIME = "edit_time";
  String PERF_TIME_ID = "perf_time_id";
  String STATUS = "status";
  String ROLE = "role";
  String WEIGHT = "weight";
  String PROJECT_CONTENT = "project_content";
  // -------- } Column Defines
  
     		  			  		  		  			  		  		  		  		  
  String FIELD_PK = "id";
  String FIELDS_WITHOUT_PK = "user_id,user_perf_id,project_name,edit_time,perf_time_id,status,role,weight,project_content";
  String FIELDS_ALL = FIELD_PK+","+FIELDS_WITHOUT_PK;

  @SQL("select "+FIELDS_ALL+" from "+TABLE+" limit :offset,:count")
  public List<UserPerfProject> queryAll(@SQLParam("offset")int offset,@SQLParam("count") int count);
		
  @SQL("select COUNT("+FIELD_PK+") from "+TABLE)
  public int countAll();
  
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `id` = :1")
  public UserPerfProject query(int id);
  
  /**
   * 按绩效记录ID，用户ID，查询用户在此绩效考核中的项目记录
   * @param userId
   * @param perfTimeId
   * @return List<UserPerfProject>
   */
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `user_id` = :1 and `perf_time_id` = :2 order by id;")
  public List<UserPerfProject> queryByUserIdAndPerfTime(int userId,int perfTimeId);
  
  String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "user_id=:model.userId,user_perf_id=:model.userPerfId,project_name=:model.projectName,edit_time=:model.editTime,perf_time_id=:model.perfTimeId,status=:model.status,role=:model.role,weight=:model.weight,project_content=:model.projectContent";
  
  @SQL("update "+TABLE+" set "+SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK+" where id=:1.id")
  public int update(@SQLParam("model") UserPerfProject model);
  
  @SQL("insert ignore into " + TABLE +" ("+FIELDS_WITHOUT_PK+") VALUES (:model.userId,:model.userPerfId,:model.projectName,:model.editTime,:model.perfTimeId,:model.status,:model.role,:model.weight,:model.projectContent)")
	@ReturnGeneratedKeys
  public Integer save(@SQLParam("model") UserPerfProject model);
  
  @SQL("delete from "+TABLE+" where `id` = :1")
  public void delete(int id);
}