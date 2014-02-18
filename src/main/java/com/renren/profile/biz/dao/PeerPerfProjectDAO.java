package com.renren.profile.biz.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.renren.profile.RenrenProfileConstants;
import com.renren.profile.biz.model.PeerPerfProject;

@DAO(catalog = RenrenProfileConstants.DB_CATALOG)
public interface PeerPerfProjectDAO {
  String TABLE = "peer_perf_project";
  // -------- { Column Defines
  String ID = "id";
  String PEER_ID = "peer_id";
  String PEER_PERF_ID = "peer_perf_id";
  String IS_MANAGER = "is_manager";
  String INVITATION_ID = "invitation_id";
  String PROJECT_PERF_ID = "project_perf_id";
  String PERF_TIME_ID = "perf_time_id";
  String CONTENT = "content";
  String WEIGHT = "weight";
  String STATUS = "status";
  String EDIT_TIME = "edit_time";
  // -------- } Column Defines
  
     		  			  		  			  			  			  		  		  		  		  
  String FIELD_PK = "id";
  String FIELDS_WITHOUT_PK = "peer_id,peer_perf_id,is_manager,invitation_id,project_perf_id,perf_time_id,content,weight,status,edit_time";
  String FIELDS_ALL = FIELD_PK+","+FIELDS_WITHOUT_PK;

  @SQL("select "+FIELDS_ALL+" from "+TABLE+" limit :offset,:count")
  public List<PeerPerfProject> queryAll(@SQLParam("offset")int offset,@SQLParam("count") int count);
		
  @SQL("select COUNT("+FIELD_PK+") from "+TABLE)
  public int countAll();
  
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `id` = :1")
  public PeerPerfProject query(int id);
  
  /**
   * 按Collection<{@link Integer}> ProjectPerfId 查询记录
   * @param projectPerfIds
   * @return Map<{@link Integer},{@link List}<{@link PeerPerfProject}>>
   */
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `project_perf_id` in (:1) order by peer_id")
  public Map<Integer,List<PeerPerfProject>> query(Collection<Integer> projectPerfIds);
  
  /**
   * 按评价人ID，评价时间查询记录
   * @param peerId
   * @param perfProjectIds
   * @return
   */
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where peer_perf_id=:1")
  public List<PeerPerfProject> queryListByPeerPerfId(int peerPerfId);
  
 @SQL("select "+FIELDS_ALL+" from "+TABLE+" where project_perf_id = :1")
 public List<PeerPerfProject> queryListByPerfProjectId(int perfProjectId);
  
  
  String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "peer_id=:model.peerId,peer_perf_id=:model.peerPerfId,is_manager=:model.isManager,invitation_id=:model.invitationId,project_perf_id=:model.projectPerfId,perf_time_id=:model.perfTimeId,content=:model.content,weight=:model.weight,status=:model.status,edit_time=:model.editTime";
  
  @SQL("update "+TABLE+" set "+SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK+" where id=:1.id")
  public int update(@SQLParam("model") PeerPerfProject model);
  
  @SQL("insert ignore into " + TABLE +" ("+FIELDS_WITHOUT_PK+") VALUES (:model.peerId,:model.peerPerfId,:model.isManager,:model.invitationId,:model.projectPerfId,:model.perfTimeId,:model.content,:model.weight,:model.status,:model.editTime)")
	@ReturnGeneratedKeys
  public Integer save(@SQLParam("model") PeerPerfProject model);
  
  @SQL("delete from "+TABLE+" where `id` = :1")
  public void delete(int id);
}