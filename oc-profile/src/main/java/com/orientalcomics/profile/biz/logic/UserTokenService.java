package com.orientalcomics.profile.biz.logic;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.dao.UserTokenDAO;
import com.orientalcomics.profile.biz.model.UserToken;


@Service
public class UserTokenService implements OcProfileConstants {

    @Autowired
    UserTokenDAO userTokenDAO;

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
        return userTokenDAO.query(userId);
    }

    public void clearToken(int userId) {
        userTokenDAO.delete(userId);
    }

    public void setToken(UserToken userToken) {
        userTokenDAO.update(userToken);
    }
}
