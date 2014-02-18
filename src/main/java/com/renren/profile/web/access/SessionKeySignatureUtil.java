package com.renren.profile.web.access;

import com.renren.profile.biz.model.access.SessionKeyInfo;
import com.renren.profile.web.util.MD5;



/**
 * @author Zhang Tielei
 * 与Session Key有关的加密算法
 *
 */
public class SessionKeySignatureUtil {

	public static int[] permutation1 = {1, 8, 27, 14, 15, 29, 25, 28, 21, 3, 5, 4, 
			11, 10, 31, 18, 2, 7, 24, 17, 26, 30, 13, 19,
			20, 9, 22, 23, 0, 12, 6, 16};
	
	/**
	 * @param skInfo 保存Session Key相关信息的结构 已经过期
	 * @return 根据私有的置换（permutation1）规则生成Session Key的签名部分
	 * 
	 */
//	public static String sigSessionKey(SessionKeyInfo skInfo){
//		String strToSig = "" + skInfo.getSkType() + skInfo.getSkLife() + 
//			skInfo.getSkExpires() + skInfo.getUid() + skInfo.getAppSecret() +
//			skInfo.getSkEncryptionKey();
//		return sig(strToSig, SessionKeySignatureUtil.permutation1);
//	}
	/**
	 * 
	 * @param skInfo 保存Session Key相关信息的结构,主要将原有的secretKey改为了appId
	 * @return 根据私有的置换（permutation1）规则生成Session Key的签名部分
	 */
	public static String sigSessionKey(SessionKeyInfo skInfo){
		String stringToSig = constructStringToSig(skInfo);
		return sig(stringToSig, SessionKeySignatureUtil.permutation1);
	}

    public static String constructStringToSig(SessionKeyInfo skInfo) {
        StringBuffer sb = new StringBuffer();
		sb.append(skInfo.getSkType());// 类型
		sb.append(skInfo.getSkLife());// 有效期
		sb.append(skInfo.getSkExpires());// 过期时间
		sb.append(skInfo.getUid());// 用户ID
		sb.append(skInfo.getAppId());// APP
		sb.append(skInfo.getSkEncryptionKey());// KEY
        return sb.toString();
    }
	
	

	public static int[] permutation2 = {11, 6, 8, 29, 1, 31, 25, 22, 12, 13, 17, 27, 
		5, 30, 2, 23, 10, 0, 19, 9, 4, 26, 3, 18,
		7, 15, 14, 24, 28, 16, 20, 21};

	/**
	 * @param sessionKey
	 * @param ssEncryptKey 加密密钥
	 * @return 根据私有的置换（permutation2）规则生成Session Secret
	 */
	public static String sigSessionSecret(String sessionKey, String ssEncryptKey){
		return sig(sessionKey + ssEncryptKey, SessionKeySignatureUtil.permutation2);
	}

	public static String sig(String strToSig, int[] permutation) {
		String md5str = MD5.digest(strToSig);
		char[] signature = new char[32];
		for (int i = 0; i < permutation.length; i++) {
			signature[i] = md5str.charAt(permutation[i]);
		}
		return new String(signature);
	}

	public static void main(String[] args) {
		String str = "fdsfjdslf";
		String md5str = MD5.digest(str);
		System.out.println(md5str);
		String sig = sig(str, permutation1);
		System.out.println(sig);
	}

}
