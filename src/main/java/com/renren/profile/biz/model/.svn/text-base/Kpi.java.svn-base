package com.renren.profile.biz.model;

import org.apache.commons.lang.StringUtils;

public class Kpi {
  // -------- { Property Defines
  
  // 自增id 
  private int id = 0;
  
  // 季度时间（YYYYMM） 
  private int quarterTime = 0;
  
  // 对应user的Id 
  private int userId = 0;
  
  // 季度描述(2011Q1 KPI) 
  private String title = StringUtils.EMPTY;
  
  // 是否已提交，保存不算 
  private int status = 0;
  
  // KPI内容 
  private String content = StringUtils.EMPTY;

  // -------- } Property Defines
  public static final int STATUS_SUBMIT = 1;
  
  public static final int STATUS_SAVE = 0;
  
  public static final int INSERT_ID = 0;
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
   * get quarterTime<br/>
   * 季度时间（YYYYMM）
   */
  public int getQuarterTime (){
    return this.quarterTime;
  }
  
  /**
   * set quarterTime<br/>
   * 季度时间（YYYYMM）
   */
  public void setQuarterTime (int quarterTime){
    this.quarterTime = quarterTime;
  }
  
  /**
   * get userId<br/>
   * 对应user的Id
   */
  public int getUserId (){
    return this.userId;
  }
  
  /**
   * set userId<br/>
   * 对应user的Id
   */
  public void setUserId (int userId){
    this.userId = userId;
  }
  
  /**
   * get title<br/>
   * 季度描述(2011Q1 KPI)
   */
  public String getTitle (){
    return this.title;
  }
  
  /**
   * set title<br/>
   * 季度描述(2011Q1 KPI)
   */
  public void setTitle (String title){
    this.title = StringUtils.trimToEmpty(title);
  }
  
  /**
   * get status<br/>
   * 是否已提交，保存不算
   */
  public int getStatus (){
    return this.status;
  }
  
  /**
   * set status<br/>
   * 是否已提交，保存不算
   */
  public void setStatus (int status){
    this.status = status;
  }
  
  /**
   * get content<br/>
   * KPI内容
   */
  public String getContent (){
    return this.content;
  }
  
  /**
   * set content<br/>
   * KPI内容
   */
  public void setContent (String content){
    this.content = StringUtils.trimToEmpty(content);
  }

  // -------- } Property Getter/Setter
  
}