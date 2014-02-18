package com.renren.profile.web.access;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.renren.profile.biz.logic.InfiniteSkHome;
import com.renren.profile.biz.model.access.AccessToken;
import com.renren.profile.biz.model.access.InfiniteSk;
import com.renren.profile.biz.model.access.SessionKeyInfo;
import com.renren.profile.biz.model.access.SessionKeyValidationResult;
import com.renren.profile.web.util.SKUtil;


@Component
public class InfiniteSessionKeyManager {
	
    private static ApplicationContext applicationContext;
	
    private static InfiniteSessionKeyManager instance ;

    
    private static final Logger logger = Logger.getLogger(InfiniteSessionKeyManager.class);
    
    @Autowired
	public void setApplicationContext(ApplicationContext ac) {
    	InfiniteSessionKeyManager.applicationContext = ac;
	}

	public static InfiniteSessionKeyManager getInstance() {
		if (instance == null) {
			instance =  (InfiniteSessionKeyManager) BeanFactoryUtils.beanOfType(applicationContext, InfiniteSessionKeyManager.class);
		}
		return instance;
	}
	
	private InfiniteSessionKeyManager() {
	}

	public AccessToken getInfiniteSessionKey(long userId, int appId) {
		InfiniteSk inSk = InfiniteSkHome.getInstance().get(userId, appId);
		if (inSk == null) {
			inSk = generateInfiniteSessionKey(userId, appId);
		}
		AccessToken sk = new AccessToken();
		sk.setExpiresTime(-1);
		sk.setSessionKey(inSk.getSessionKey());
		sk.setSessionSecret(inSk.getSessionSecret());
		return sk;
	}

	private InfiniteSk generateInfiniteSessionKey(long userId, int appId) {
		String sessionKey = SKUtil.getUUID() + '-' + userId;
		String sessionSecret = SKUtil.getUUID();
		InfiniteSk inSk = new InfiniteSk();
		inSk.setAppId(appId);
		inSk.setUserId(userId);
		inSk.setSessionKey(sessionKey);
		inSk.setSessionSecret(sessionSecret);
		InfiniteSkHome.getInstance().generateKey(inSk);
		return inSk;
	}

	public SessionKeyValidationResult validateInfiniteSessionKey(int appId, String sessionKey, boolean needSessionSecret) {
		SessionKeyValidationResult validationResult = new SessionKeyValidationResult();
		SessionKeyInfo skInfo = SKUtil.parseInfiniteSessionKey(sessionKey);
		if (skInfo == null) {
			validationResult.setValidationCode(SessionKeyValidationResult.SK_VALIDATION_INVALID);
			return validationResult;
		}

		InfiniteSk inSk = InfiniteSkHome.getInstance().get(skInfo.getUid(), appId);
		if(logger.isDebugEnabled()){
			logger.debug("the key :"+ sessionKey +" compare to db session key is :"+inSk.getSessionKey());
		}
		int validationCode = SessionKeyValidationResult.SK_VALIDATION_INVALID;
		if (inSk != null && inSk.getSessionKey().equals(sessionKey)) {
			validationCode = SessionKeyValidationResult.SK_VALIDATION_OK;
		}
		validationResult.setValidationCode(validationCode);
		if (validationCode == SessionKeyValidationResult.SK_VALIDATION_OK) {
			validationResult.setUserId(skInfo.getUid());
			if (needSessionSecret) {
				validationResult.setSessionSecret(inSk.getSessionSecret());
			}
		}
		return validationResult;
	}
	

	public boolean expireInfiniteSessionKey(int appId, String sessionKey) {
		SessionKeyInfo skInfo = SKUtil.parseInfiniteSessionKey(sessionKey);
		if (skInfo == null) {
			return false;
		}
		InfiniteSkHome.getInstance().delete(appId, skInfo.getUid());
		return true;
	}
	
	/**
	 * 直接删除用户的持久化session_key
	 * @param appId
	 * @param userId
	 * @return
	 */
	public boolean expireInfiniteSessionKey(int appId, int userId) {
		InfiniteSkHome.getInstance().delete(appId, userId);
		return true;
	}

}
