package com.renren.profile.biz.model.access;

import java.io.Serializable;

public class SessionKeyInfo implements Serializable{
	
	private static final long serialVersionUID = -5840606474463974953L;
	
	/**/
	private int skType;
	
	/**/
	private String skSig;
	
	
	private long skLife;
	
	
	private long skExpires;
	
	
	private int uid;
	
	
	private int appId;
	
	
	private String appSecret;
	
	
	private String skEncryptionKey;
	
	
	private String ssEncryptionKey;

	public int getSkType() {
		return skType;
	}

	public void setSkType(int skType) {
		this.skType = skType;
	}

	public long getSkLife() {
		return skLife;
	}

	public void setSkLife(long skLife) {
		this.skLife = skLife;
	}

	public long getSkExpires() {
		return skExpires;
	}

	public void setSkExpires(long skExpires) {
		this.skExpires = skExpires;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getSkSig() {
		return skSig;
	}

	public void setSkSig(String skSig) {
		this.skSig = skSig;
	}

	public String getSkEncryptionKey() {
		return skEncryptionKey;
	}

	/**
	 * @param skKey
	 *            Session Key的随机key，每天变化一次
	 */
	public void setSkEncryptionKey(String skEncryptionKey) {
		this.skEncryptionKey = skEncryptionKey;
	}

	public String getSsEncryptionKey() {
		return ssEncryptionKey;
	}

	/**
	 * @param ssKey
	 *            Session Secret的随机key，每天变化一次
	 */
	public void setSsEncryptionKey(String ssEncryptionKey) {
		this.ssEncryptionKey = ssEncryptionKey;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}
}
