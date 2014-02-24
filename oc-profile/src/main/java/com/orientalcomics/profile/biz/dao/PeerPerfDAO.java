package com.orientalcomics.profile.biz.dao;

import java.util.Collection;
import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.orientalcomics.profile.biz.model.PeerPerf;

@DAO
public interface PeerPerfDAO {
  String TABLE = "peer_perf";
  // -------- { Column Defines
  String ID = "id";
  String PEER_ID = "peer_id";
  String IS_LEADER = "is_leader";
  String INVITATION_ID = "invitation_id";
  String USER_PERF_ID = "user_perf_id";
  String PERF_TIME_ID = "perf_time_id";
  String CONTENT = "content";
  String ADVANTAGE_COMMENTS = "advantage_comments";
  String DISADVANTAGE_COMMENTS = "disadvantage_comments";
  String STATUS = "status";
  String EDIT_TIME = "edit_time";
  String IS_PROMOTION = "is_promotion";
  String PROMOTION_COMMENT = "promotion_comment";
  String PERF_SCORE = "perf_score";
  String PEER_NAME  = "peer_name";
  
  // -------- } Column Defines
  
     		  			  		  			  			  			  		  		  		  		  		  		  		  
  String FIELD_PK = "id";
  String FIELDS_WITHOUT_PK = "peer_id,is_leader,invitation_id,user_perf_id,perf_time_id,content,advantage_comments,disadvantage_comments,status,edit_time,is_promotion,promotion_comment,perf_score,peer_name";
  String FIELDS_ALL = FIELD_PK+","+FIELDS_WITHOUT_PK;

  @SQL("select "+FIELDS_ALL+" from "+TABLE+" limit :offset,:count")
  public List<PeerPerf> queryAll(@SQLParam("offset")int offset,@SQLParam("count") int count);
		
  @SQL("select COUNT("+FIELD_PK+") from "+TABLE)
  public int countAll();
  
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `id` = :1")
  public PeerPerf query(int id);
  
  /***
   * 批量查询上级没有给下属评价，根据条件userid，perfTimeId，isLeader，status，perfIds
   * 
   * @param userid 评价人的id
   * @param perfTimeId 当前考核季度的id
   * @param isLeader  是否是直接主管
   * @param perfIds   自评记录集合
   * @return
   */
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `peer_id` = :1 and  `perf_time_id` = :2 and `is_leader` = :3 and `status` = 0 and  `user_perf_id` in (:4)")
  public Collection<PeerPerf> batchQueryPeerPerfInfoForNoAccess(int userid,int perfTimeId,int isLeader,Collection<Integer> perfIds);
  
  /***
   * 批量查询互评信息，根据条件userid，perfTimeId，isLeader，status，perfIds
   * 
   * @param userid 评价人的id
   * @param perfTimeId 当前考核季度的id
   * @param isLeader  是否是直接主管
   * @param perfIds   自评记录集合
   * @return
   */
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `peer_id` = :1 and  `perf_time_id` = :2 and `is_leader` = :3 and `status` != :4 and  `user_perf_id` in (:5)")
  public Collection<PeerPerf> batchQueryPeerPerfInfo(int userid,int perfTimeId,int isLeader,int status,Collection<Integer> perfIds);
  /** 
   * 根据评价人id和perfTimeId查询评价的信息
   * @param id
   * @param perfTimeId
   * @return
   */
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `user_id` = :1 and `perf_time_id` = :2")
  public PeerPerf queryPeerPerfInfo(int id,int perfTimeId);
  

  /**
   * 按user_perf_id查询互评记录
   * @param userPerfId
   * @return List<PeerPerf>
   */
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `user_perf_id` = :1")
  public List<PeerPerf> queryByUserPerfId(int userPerfId);
 
  /**
   * 按user_perf_id查询非上司互评记录
   * @param userPerfId
   * @return List<PeerPerf>
   */
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `user_perf_id` = :1 and is_leader = 0;")
  public List<PeerPerf> queryNoManagerByUserPerfId(int userPerfId);
  
  /**
   * 根据评价人ID，自评记录ID，查询单个记录
   * @param userPerfId
   * @return {@link PeerPerf}
   * @author hao.zhang
   */
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `peer_id` = :1 and `user_perf_id`=:2;")
  public PeerPerf queryPeerPerfByUserPerfIdAndPeerId(int peerid,int userPerfId);
  
  /**
   * 按user_perf_id查询上司互评记录
   * @param userPerfId
   * @return
   */
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `user_perf_id` = :1 and is_leader = 1;")
  public PeerPerf queryManagerPerfByUserPerfId(int userPerfId);
  
  String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "peer_id=:model.peerId,is_leader=:model.isLeader,invitation_id=:model.invitationId,user_perf_id=:model.userPerfId,perf_time_id=:model.perfTimeId,content=:model.content,advantage_comments=:model.advantageComments,disadvantage_comments=:model.disadvantageComments,status=:model.status,edit_time=:model.editTime,is_promotion=:model.isPromotion,promotion_comment=:model.promotionComment,perf_score=:model.perfScore,peer_name=:model.peerName";
  
  @SQL("update "+TABLE+" set "+SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK+" where id=:1.id")
  public int update(@SQLParam("model") PeerPerf model);
  
  @SQL("insert ignore into " + TABLE +" ("+FIELDS_WITHOUT_PK+") VALUES (:model.peerId,:model.isLeader,:model.invitationId,:model.userPerfId,:model.perfTimeId,:model.content,:model.advantageComments,:model.disadvantageComments,:model.status,:model.editTime,:model.isPromotion,:model.promotionComment,:model.perfScore,:model.peerName)")
	@ReturnGeneratedKeys
  public Integer save(@SQLParam("model") PeerPerf model);
  
  @SQL("delete from "+TABLE+" where `id` = :1")
  public void delete(int id);
}