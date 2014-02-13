package com.orientalcomics.profile.biz.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.orientalcomics.profile.util.time.TimeUtils;
import com.orientalcomics.profile.util.time.TimeValidator;

public class Role {
  // -------- { Property Defines
  
  
  private int id = 0;
  
  
  private String name = StringUtils.EMPTY;
  
  
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
   * get name
   */
  public String getName (){
    return this.name;
  }
  
  /**
   * set name
   */
  public void setName (String name){
    this.name = StringUtils.trimToEmpty(name);
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