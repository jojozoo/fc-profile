package com.renren.profile.web.access;

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
public class AccessTokenManager {

	private static ApplicationContext applicationContext;
	
    private static AccessTokenManager instance = new AccessTokenManager();


    
    
    @Autowired
	public void setApplicationContext(ApplicationContext ac) {
    	AccessTokenManager.applicationContext = ac;
	}

	public static AccessTokenManager getInstance() {
		if (instance == null) {
			instance =  (AccessTokenManager) BeanFactoryUtils.beanOfType(applicationContext, AccessTokenManager.class);
		}
		return instance;
	}

    private AccessTokenManager() {
    }


   /**
    * 如果没有会新创建
    * @param skType
    * @param uid
    * @param appId
    * @return
    */

    public AccessToken getAccessToken(long uid, int appId) {
    	return InfiniteSessionKeyManager.getInstance().getInfiniteSessionKey(uid, appId);
    }
    
    public String getSessionSecret(String appId, String sessionKey) {
        return getSessionSecret(Integer.valueOf(appId),sessionKey);
    }

    /**
     * @param appId
     * @param sessionKey
     * @return 得到sessionKey对应的session secret；如果没有对应的session
     *         key（例如传入无效的session key），则返回null
     */
    public String getSessionSecret(int appId, String sessionKey) {
        String sessionSecret = null;
        SessionKeyInfo skInfo = SKUtil.parseInfiniteSessionKey(sessionKey);
            if (skInfo != null) {
            	InfiniteSk infiniteSk = InfiniteSkHome.getInstance().get(skInfo.getUid(), appId);
            	if (infiniteSk != null) {
                	sessionSecret = infiniteSk.getSessionSecret();
            	}
            }
        return sessionSecret;
    }

    
    public SessionKeyValidationResult validateSessionKey(String appId, String sessionKey) {
        return validateSessionKey(Integer.valueOf(appId), sessionKey);
    }
    
    public SessionKeyValidationResult validateSessionKey(int appId, String sessionKey) {
        return validateSessionKey(appId, sessionKey, false);
    }

    /**
     * @param sessionKey
     * @param appId
     * @param needSessionSecret 表示是否需要返回一个Session Secret
     * @return 验证sessionKey是否有效，验证结果封装在一个SessionKeyValidationResults对象里，
     *         同时，验证结果中还顺带返回一个userId，
     *         并根据参数needSessionSecret的值决定是否顺带返回一个Session Secret；
     *         如果验证失败，则返回的验证结果中的userId和sessionSecret未定义；
     *         如果needSessionSecret传入false，则返回的验证结果中的sessionSecret未定义
     */
    public SessionKeyValidationResult validateSessionKey(int appId, String sessionKey, boolean needSessionSecret) {
        SessionKeyValidationResult validationResult = null;
        validationResult = InfiniteSessionKeyManager.getInstance().validateInfiniteSessionKey(appId, sessionKey,
                    needSessionSecret);
        return validationResult;
    }


}
