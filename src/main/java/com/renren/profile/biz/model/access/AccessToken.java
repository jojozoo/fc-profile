package com.renren.profile.biz.model.access;

import java.io.Serializable;

/**
 * @author Zhang Tielei 代表Session Key信息的类
 * 
 */
public class AccessToken  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -514288145373260782L;
	
	/*key*/
	private String sessionKey;
	
	/*加密串*/
	private String sessionSecret;
	
	/*过期时间*/
	private long expiresTime;

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

	public long getExpiresTime() {
		return expiresTime;
	}

	public void setExpiresTime(long expiresTime) {
		this.expiresTime = expiresTime;
	}
}
