package com.renren.profile.biz.model;

import java.sql.Timestamp;

import com.renren.profile.util.time.TimeUtils;
import com.renren.profile.util.time.TimeValidator;

public class UserInvitation {
  // -------- { Property Defines
  
  // 自增id 
  private int id = 0;
  
  // 发起邀请用户id 
  private int fromId = 0;
  
  // 被邀请用户id 
  private int inviteId = 0;
  
  // 记录对应的编辑时间 
  private Timestamp editTime = TimeUtils.Constant.TIME_1970;
  
  // 对应Q的记录id 
  private int perfTimeId = 0;
  
  // 是否已接受，撤销 ,已提交 ；1：未接受，可以撤销；2：邀请人还未提交;3:邀请人已经提交申请
  private int status = 0;

  // -------- } Property Defines
  
  
  // -------- { Property Getter/Setter
  
  /**
   * get id<br/>
   * 自增id
   */
  public int getId (){
    return this.id;
  }
  
  /**
   * set id<br/>
   * 自增id
   */
  public void setId (int id){
    this.id = id;
  }
  
  /**
   * get fromId<br/>
   * 发起邀请用户id
   */
  public int getFromId (){
    return this.fromId;
  }
  
  /**
   * set fromId<br/>
   * 发起邀请用户id
   */
  public void setFromId (int fromId){
    this.fromId = fromId;
  }
  
  /**
   * get inviteId<br/>
   * 被邀请用户id
   */
  public int getInviteId (){
    return this.inviteId;
  }
  
  /**
   * set inviteId<br/>
   * 被邀请用户id
   */
  public void setInviteId (int inviteId){
    this.inviteId = inviteId;
  }
  
  /**
   * get editTime<br/>
   * 记录对应的编辑时间
   */
  public Timestamp getEditTime (){
    return this.editTime;
  }
  
  /**
   * set editTime<br/>
   * 记录对应的编辑时间
   */
  public void setEditTime (Timestamp editTime){
    this.editTime = TimeValidator.defaultIfInvalid(editTime, TimeUtils.Constant.TIME_1970);
  }
  
  /**
   * get perfTimeId<br/>
   * 对应Q的记录id
   */
  public int getPerfTimeId (){
    return this.perfTimeId;
  }
  
  /**
   * set perfTimeId<br/>
   * 对应Q的记录id
   */
  public void setPerfTimeId (int perfTimeId){
    this.perfTimeId = perfTimeId;
  }
  
  /**
   * get status<br/>
   * 是否已接受，撤销 ,已提交 ，0：撤销；1：接受；2：已提交
   */
  public int getStatus (){
    return this.status;
  }
  
  /**
   * set status<br/>
   * 是否已接受，撤销 ,已提交 ，0：撤销；1：接受；2：已提交
   */
  public void setStatus (int status){
    this.status = status;
  }

  // -------- } Property Getter/Setter
  
}