package com.orientalcomics.profile.biz.dao;

import java.util.Collection;
import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.model.UserInvitation;

@DAO(catalog = OcProfileConstants.DB_CATALOG)
public interface UserInvitationDAO {
  String TABLE = "user_invitation";
  // -------- { Column Defines
  String ID = "id";
  String FROM_ID = "from_id";
  String INVITE_ID = "invite_id";
  String EDIT_TIME = "edit_time";
  String PERF_TIME_ID = "perf_time_id";
  String STATUS = "status";
  // -------- } Column Defines
  
     		  			  			  		  			  		  
  String FIELD_PK = "id";
  String FIELDS_WITHOUT_PK = "from_id,invite_id,edit_time,perf_time_id,status";
  String FIELDS_ALL = FIELD_PK+","+FIELDS_WITHOUT_PK;

  @SQL("select "+FIELDS_ALL+" from "+TABLE+" limit :offset,:count")
  public List<UserInvitation> queryAll(@SQLParam("offset")int offset,@SQLParam("count") int count);
		
  // 统计出本人邀请好友的数目
  @SQL("select COUNT("+FIELD_PK+") from "+TABLE + " where `from_id` = :1  and perf_time_id = :2")
  public Integer countInviteFriendsNumber(int fromId, int perfTimeId);
  
 
  @SQL("select COUNT("+FIELD_PK+") from "+TABLE + "")
  public int countAll();
  
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `id` = :1")
  public UserInvitation query(int id);
  
  /**
   *  根据用户id和perf_time_id查询用户邀请别人的信息
   * @param from_id
   * @param perfId
   * @return
   */
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `from_id` = :1 and `perf_time_id` = :2")
  public List<UserInvitation> queryInvitationInfo(int from_id,int perfId);
  
  
  /**
   * 筛选出全局邀请别人的记录不够三个的用户id
   * @param perfTimeId
   * @return
   */
  @SQL("select from_id from (select from_id,count(id) as count from user_invitation where perf_time_id=:1 group by from_id )as tmp where count<3")
  public Collection<Integer> queryInvitationNotEnough(int perfTimeId);
  
  
  /**
   * 筛选出全局邀请别人的记录够三个的用户id
   * @param perfTimeId
   * @return
   */
  @SQL("select from_id from (select from_id,count(id) as count from user_invitation where perf_time_id=:1 group by from_id )as tmp where count=3")
  public Collection<Integer> queryInvitationEnough(int perfTimeId);
  
  /**
   * 查询全局邀请别人互评，还未全部完成的用户
   * @param perfTimeId
   * @param ids
   * @return
   */
  @SQL("select from_id from "+ TABLE +" where from_id in(:2) and status <6  and perf_time_id = :1 group by from_id")
  public Collection<Integer> queryInvitationNotCompelete(int perfTimeId,Collection<Integer> ids);
  
  /**
   * 查询全局，邀请自己的互评还未完成的用户
   * @param perfTimeId
   * @return
   */
  @SQL("select invite_id from "+ TABLE +" where perf_time_id = :1 and status <6 group by invite_id")
  public Collection<Integer> queryInvitedSelfNotCompelete(int perfTimeId);
  
  
  /**
   *  根据用户id和perf_time_id查询用户要评价的信息
   * @param invite_id
   * @param perfId
   * @return
   */
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `invite_id` = :1 and `perf_time_id` = :2")
  public List<UserInvitation> queryAccessInfo(int invite_id,int perfId);
  
  /**
   * 根据邀请人和被邀请人ID，和perfTimeId,查看是否有邀请记录
   * @param fromId
   * @param invitedId
   * @param perfTimeId
   * @return {@link UserInvitation} 
   */
  @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `from_id` = :1 and `invite_id` = :2 and perf_time_id = :3")
  public UserInvitation queryInvitationItem(int fromId,int invitedId,int perfTimeId);
  
  String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "from_id=:model.fromId,invite_id=:model.inviteId,edit_time=:model.editTime,perf_time_id=:model.perfTimeId,status=:model.status";
  
  @SQL("update "+TABLE+" set "+SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK+" where id=:1.id")
  public int update(@SQLParam("model") UserInvitation model);
  
  @SQL("insert ignore into " + TABLE +" ("+FIELDS_WITHOUT_PK+") VALUES (:model.fromId,:model.inviteId,:model.editTime,:model.perfTimeId,:model.status)")
	@ReturnGeneratedKeys
  public Integer save(@SQLParam("model") UserInvitation model);
  
  @SQL("delete from "+TABLE+" where `id` = :1")
  public void delete(int id);
  
  @SQL("delete from "+TABLE+" where `from_id` = :1 and `invite_id` = :2 and `perf_time_id` = :3")
  public Integer deleteUserInviteInfo(int currentUserId, int inviteId, int perf_time_id);
}