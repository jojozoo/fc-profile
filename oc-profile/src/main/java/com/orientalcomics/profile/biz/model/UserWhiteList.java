package com.orientalcomics.profile.biz.model;

import org.apache.commons.lang.StringUtils;

public class UserWhiteList {

	    // -------- { Property Defines

	    private int       id         = 0;
	    
	    private int       userId     = 0;
	    
	    private int       status    = 2;

	    // 名字
	    private String    userName    = StringUtils.EMPTY;

	    

	    // -------- } Property Defines

	    // -------- { Property Getter/Setter



		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		/**
	     * get id
	     */
	    public int getId() {
	        return this.id;
	    }

	    /**
	     * set id
	     */
	    public void setId(int id) {
	        this.id = id;
	    }



}
