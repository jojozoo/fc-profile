package com.renren.profile.biz.model.access;


import java.io.Serializable;

/**
 * 持久化的session_key和session_secret
 * 
 * @author haobo.cui@opi-corp.com 2009-3-16 下午12:12:23
 */

public class InfiniteSk implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8413516923655632809L;

	/*session_key 密钥*/
	private int appId;
	
	/*session_secret 密钥*/
	private long userId;
	
	/*sessionKey*/
	private String sessionKey;
	
	/*sessionSecret*/
	private String sessionSecret;

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getSessionSecret() {
		return sessionSecret;
	}

	public void setSessionSecret(String sessionSecret) {
		this.sessionSecret = sessionSecret;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

}
