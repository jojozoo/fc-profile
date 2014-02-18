package com.renren.profile.web.util;

import java.sql.Date;
import java.util.Calendar;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.renren.profile.biz.model.access.SessionKeyInfo;


/**
 * 维护sessionKey机制的工具类，包括拼接sessionKey、取得过期时间、反解sessionKey串
 * 
 * @author haobo.cui@opi-corp.com 2009-3-16 下午07:44:32
 */
public class SKUtil {
	
	private static final Logger logger = Logger.getLogger(SKUtil.class);

	/**
	 * @return 取当前Date对象，不受时分秒的影响
	 */
	public static Date getCurdate() {
		Calendar c = Calendar.getInstance();
		c = ignoreHMS(c);
		return new Date(c.getTimeInMillis());
	}
	
	/**
	 * 
	 * 按传入的time参数构造Date对象，把时分秒全部置0
	 * 
	 * @param time 表示时间的long
	 * @return
	 */
	public static Date getDateIgnoreHMS(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		c = ignoreHMS(c);
		return new Date(c.getTimeInMillis());
	}
	
	/**
	 * 将传入Calendar对象的时分秒置0
	 * @param c
	 * @return
	 */
	private static Calendar ignoreHMS(Calendar c) {
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}
	
	
	/**
	 * 返回对外提供的sessionKey格式
	 * 
	 * @param skInfo
	 * @param sigSessionKey
	 * @return
	 */
	public static String getSessionKey(SessionKeyInfo skInfo,
			String sigSessionKey) {
		StringBuffer sb = new StringBuffer();
		sb.append(skInfo.getSkType());
		sb.append(".");
		sb.append(sigSessionKey);
		sb.append(".");
		sb.append(skInfo.getSkLife());
		sb.append(".");
		sb.append(skInfo.getSkExpires());
		sb.append("-");
		sb.append(skInfo.getUid());
		return sb.toString();
	}

//	/**
//	 * 得到sessionKey的过期时间
//	 * 
//	 * @param lifeTime
//	 * @return
//	 */
//	public static long getExpiresTime(int skType,long lifeTime, long currentTime) {
//		// 取得取整以后的过期时间,例如当前时间为1237189468，实际整取过期时间为1237194000
//		long expiresTime;
//		if (skType == AccessTokenManager.SESSION_KEY_PROFILE) {//profile应用直接60s
//			expiresTime = currentTime+lifeTime;
//		}
//		else {
//			expiresTime = ((currentTime + 3599)/ 3600) * 3600 + lifeTime;
//		}
//		
//		return expiresTime;
//		
//		
//	}

	/**
	 * @param sessionKey
	 * @return 解析sessionKey，返回一个SessionKeyInfo对象；解析失败返回null
	 */
	public static SessionKeyInfo parseFiniteSessionKey(String sessionKey) {
		if (logger.isInfoEnabled()) {
			logger.info("SKUtil: start to parse finite session key -- " + sessionKey);
		}
		SessionKeyInfo skInfo = new SessionKeyInfo();
		// sessionKey的格式为 <skType>.<32byte-sig>.<skLife>.<skExpires>-<uid>
		String[] fields = sessionKey.trim().split("\\.");
		if (fields.length != 4) {
			return null;
		}
		int skType = Integer.parseInt(fields[0]);
		String skSig = fields[1];
		long skLife = Long.parseLong(fields[2]);

		String[] subFields = fields[3].split("-");
		if (subFields.length != 2) {
			return null;
		}
		long skExpires = -1; 
		int uid = -1;
		try{
			skExpires = Long.parseLong(subFields[0]);
			uid = Integer.parseInt(subFields[1]);
		}catch (Exception e) {
			// DO nothing
		}
		if(skExpires < 0 || uid < 0){
			return null;
		}

		skInfo.setSkType(skType);
		skInfo.setSkSig(skSig);
		skInfo.setSkLife(skLife);
		skInfo.setSkExpires(skExpires);
		skInfo.setUid(uid);
		return skInfo;
	}

	/**
	 * @param sessionKey
	 * @return 解析sessionKey，返回一个SessionKeyInfo对象（返回的SessionInfo对象中只有两项有效：skSig和uid）；解析失败返回null
	 */
	public static SessionKeyInfo parseInfiniteSessionKey(String sessionKey) {
		if (logger.isDebugEnabled()) {
			logger.debug("SKUtil: start to parse infinite session key -- " + sessionKey);
		}
		SessionKeyInfo skInfo = new SessionKeyInfo();
		// sessionKey的格式为 <32byte-sig>-<uid>
		String[] fields = sessionKey.trim().split("-");
		if (fields.length != 2) {
			return null;
		}
		String skSig = fields[0];
		int uid = Integer.parseInt(fields[1]);
		skInfo.setSkSig(skSig);
		skInfo.setUid(uid);
		skInfo.setSkLife(-1);
		return skInfo;
	}

	
	/**
	 * 得到一个uuid的串，生成skKey，ssKey等
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static void main(String[] args) throws InterruptedException {
		Date d1 = getCurdate();
		Thread.sleep(1001);
		Date d2 = getCurdate();
		
		System.out.println(d1.toGMTString());
		System.out.println(d2.toGMTString());
		System.out.println(d1.equals(d2));
		
		Date d3 = getDateIgnoreHMS(System.currentTimeMillis());
		Thread.sleep(1001);
		Date d4 = getDateIgnoreHMS(System.currentTimeMillis());
		
		System.out.println(d3.toGMTString());
		System.out.println(d4.toGMTString());
		System.out.println(d3.equals(d4));
	}
	
	
}
