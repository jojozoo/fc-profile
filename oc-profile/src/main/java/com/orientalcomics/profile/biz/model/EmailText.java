package com.orientalcomics.profile.biz.model;


public class EmailText {
  // -------- { Property Defines
  
  // 自增id,department_id 
  private int id = 0;
  
  // 发送email的类型
  private String emailType;
  
  // 发送email的标题
  private String emailTitle;
  
  // 发送email的内容
  private String emailContent;
  
  // -------- } Property Defines

  // -------- { Property Getter/Setter

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmailType() {
		return emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

	public String getEmailTitle() {
		return emailTitle;
	}

	public void setEmailTitle(String emailTitle) {
		this.emailTitle = emailTitle;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}
	
	 // -------- } Property Getter/Setter
}
