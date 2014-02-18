package com.renren.profile.biz.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.renren.profile.util.time.TimeUtils;
import com.renren.profile.util.time.TimeValidator;

public class UserRole {
  // -------- { Property Defines
  
  
  private int id = 0;
  
  
  private int userId = 0;
  
  
  private int roleId = 0;
  
  
  private Timestamp editTime = TimeUtils.Constant.TIME_1970;
  
  
  private int editorId = 0;
  
  
  private String editorName = StringUtils.EMPTY;

  // -------- } Property Defines
  
  
  // -------- { Property Getter/Setter
  
  /**
   * get id
   */
  public int getId (){
    return this.id;
  }
  
  /**
   * set id
   */
  public void setId (int id){
    this.id = id;
  }
  
  /**
   * get userId
   */
  public int getUserId (){
    return this.userId;
  }
  
  /**
   * set userId
   */
  public void setUserId (int userId){
    this.userId = userId;
  }
  
  /**
   * get roleId
   */
  public int getRoleId (){
    return this.roleId;
  }
  
  /**
   * set roleId
   */
  public void setRoleId (int roleId){
    this.roleId = roleId;
  }
  
  /**
   * get editTime
   */
  public Timestamp getEditTime (){
    return this.editTime;
  }
  
  /**
   * set editTime
   */
  public void setEditTime (Timestamp editTime){
    this.editTime = TimeValidator.defaultIfInvalid(editTime, TimeUtils.Constant.TIME_1970);
  }
  
  /**
   * get editorId
   */
  public int getEditorId (){
    return this.editorId;
  }
  
  /**
   * set editorId
   */
  public void setEditorId (int editorId){
    this.editorId = editorId;
  }
  
  /**
   * get editorName
   */
  public String getEditorName (){
    return this.editorName;
  }
  
  /**
   * set editorName
   */
  public void setEditorName (String editorName){
    this.editorName = StringUtils.trimToEmpty(editorName);
  }

  // -------- } Property Getter/Setter
  
}