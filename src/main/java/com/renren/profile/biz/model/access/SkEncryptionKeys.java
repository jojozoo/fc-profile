package com.renren.profile.biz.model.access;

import java.io.Serializable;
import java.sql.Date;

/**
 * 生成SessionKey和SessionSecret的加密密钥
 * 
 * @author haobo.cui@opi-corp.com 2009-3-16 下午12:06:46
 */
public class SkEncryptionKeys implements Serializable{
	
	private static final long serialVersionUID = 3388284831083173052L;
	
	/*session_key 密钥*/
	private String skKey;
	
	/*session_secret 密钥*/
	private String ssKey;
	
	/*日期*/
	private Date date;

	public String getSkKey() {
		return skKey;
	}

	public void setSkKey(String skKey) {
		this.skKey = skKey;
	}

	public String getSsKey() {
		return ssKey;
	}

	public void setSsKey(String ssKey) {
		this.ssKey = ssKey;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
