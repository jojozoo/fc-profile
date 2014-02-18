package com.renren.profile.biz.logic;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.profile.RenrenProfileConstants;
import com.renren.profile.biz.dao.UserTokenDAO;
import com.renren.profile.biz.model.access.UserToken;
import com.renren.profile.util.DateTimeUtil;


@Service
public class UserTokenService implements RenrenProfileConstants {

    @Autowired
    UserTokenDAO userTokenDAO;
    
    
    private SecureRandom random = new SecureRandom();

    public boolean isValid(UserToken requestToken) {
        if (requestToken == null || requestToken.getUserId() <= 0) {
            return false;
        }
        int userId = requestToken.getUserId();

        if (userId > 0) {
            UserToken userToken = getToken(userId);
            if (userToken != null) {
                if (StringUtils.equals(requestToken.getToken(), userToken.getToken())) { // 票相同
                    // remove this
                    if (userToken.getExpiredTime() != null && !userToken.getExpiredTime().before(new Date())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public UserToken getToken(int userId) {
    	UserToken userToken = null;
        userToken = userTokenDAO.query(userId);
        return userToken;
    }

    public void clearToken(int userId) {
        userTokenDAO.delete(userId);
    }

    public void setToken(UserToken userToken) {
        userTokenDAO.update(userToken);
    }
    
    /**
     * 生成token
     * @return
     */
    public UserToken generateToken(int userId){
 
    	String token = new BigInteger(130, random).toString(32);
    	UserToken userToken = new UserToken();
    	userToken.setToken(token);
    	userToken.setUserId(userId);
    	userToken.setExpiredTime(DateTimeUtil.getDateBeforeOrAfter(new Date(), 180));
    	userToken.setUpdateTime(new Date());
    			
    	return userToken;
    }
}
