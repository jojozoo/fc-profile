package com.renren.profile.biz.dao;

import java.util.Collection;
import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.renren.profile.RenrenProfileConstants;
import com.renren.profile.biz.model.PerfTime;

@DAO(catalog = RenrenProfileConstants.DB_CATALOG)
public interface PerfTimeDAO {
  String TABLE = "perf_time";
  // -------- { Column Defines
  String ID = "id";
  String PERF_TITLE = "perf_title";
  String IS_PROMOTION = "is_promotion";
  String START_TIME = "start_time";
  String END_TIME = "end_time";
  String EDITOR_ID = "editor_id";
  String STATUS = "status";
  String PERF_YEAR = "perf_year";
  // -------- } Column Defines
  
     		  		  		  		  		  		  
  String FIELD_PK = "id";
  String FIELDS_WITHOUT_PK = "perf_title,is_promotion,start_time,end_time,editor_id,status,perf_year";
  String FIELDS_ALL = FIELD_PK+","+FIELDS_WITHOUT_PK;

  /**
   * 按偏移量查询绩效时间表
   * @param offset
   * @param count
   * @return
   */
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" order by start_time desc limit :offset,:count")
  public List<PerfTime> queryAll(@SQLParam("offset")int offset,@SQLParam("count") int count);
		
  @SQL("select COUNT("+FIELD_PK+") from "+TABLE)
  public int countAll();
  
  /**
   * 按年去重后，查询总数，分页用
   * @return
   */
  @SQL("select COUNT("+PERF_YEAR+") from  (select perf_year from "+ TABLE +" group by perf_year) as tmp")
  public int countAllYear();
  
  /**
   * 按偏移量查询出当前页的年份集合
   * @param offset
   * @param count
   * @return
   */
  @SQL("select "+PERF_YEAR+" from "+TABLE+" group by perf_year limit :offset,:count")
  public List<Integer> queryYears(@SQLParam("offset")int offset,@SQLParam("count") int count);
  
  /**
   * 当年的绩效列表
   * @param perfYear
   * @return
   */
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `perf_year` = :1")
  public List<PerfTime> queryByYear(int perfYear);
  
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `id` = :1")
  public PerfTime query(int id);
  
  
  /**
   * 查询当前最新的绩效记录,未开始的不算
   * @return
   */
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where status > 0 order by id desc limit 1")
  public PerfTime queryNewestPerfItem();
  
  
  String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "perf_title=:model.perfTitle,is_promotion=:model.isPromotion,start_time=:model.startTime,end_time=:model.endTime,editor_id=:model.editorId,status=:model.status,perf_year=:model.perfYear";
  
  @SQL("update "+TABLE+" set "+SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK+" where id=:1.id")
  public int update(@SQLParam("model") PerfTime model);
  
  @SQL("insert ignore into " + TABLE +" ("+FIELDS_WITHOUT_PK+") VALUES (:model.perfTitle,:model.isPromotion,:model.startTime,:model.endTime,:model.editorId,:model.status,:model.perfYear)")
	@ReturnGeneratedKeys
  public Integer save(@SQLParam("model") PerfTime model);
  
  @SQL("delete from "+TABLE+" where `id` = :1")
  public void delete(int id);
}