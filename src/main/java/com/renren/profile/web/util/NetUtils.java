package com.renren.profile.web.util;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.renren.profile.RenrenProfileConstants;
import com.renren.profile.biz.model.access.UserToken;



public class NetUtils implements RenrenProfileConstants {
	
//	private static  Logger logger = Logger.getLogger(NetUtils.class );
	
    public static final String getUrlWithQueryString(HttpServletRequest request) {
        String url = request.getRequestURI();// path,不含schema、host、port及query
        String queryString = request.getQueryString();
        if (StringUtils.isEmpty(url)) {
            url = "/";
        }
        if (StringUtils.isNotEmpty(queryString)) {
            url += "?" + queryString;
        }
        return url;
    }

    public static final UserToken getUserTokenFromCookie(HttpServletRequest request) {
        UserToken userToken = new UserToken();
        userToken.setUserId(NumberUtils.toInt(CookieManager.getInstance().getCookie(request, COOKIE_KEY_USER), 0));
        userToken.setToken(CookieManager.getInstance().getCookie(request, COOKIE_KEY_TOKEN));
        return userToken;
    }

    public static final String getTokenFromCookie(HttpServletRequest request) {
        return CookieManager.getInstance().getCookie(request, COOKIE_KEY_TOKEN);
    }

    public static final void clearTokenFromCookie(HttpServletResponse response) {
        CookieManager.getInstance().clearCookie(response, COOKIE_KEY_USER, -1, "/");
        CookieManager.getInstance().clearCookie(response, COOKIE_KEY_TOKEN, -1, "/", PROFILE_MAIN_DOMAIN);
    }
    

    public static final void setUserTokenToCookie(HttpServletResponse response, UserToken token) {
        if (token == null) {
            clearTokenFromCookie(response);
            return;
        }
        Date expiredTime = token.getExpiredTime();
        long seconds = -1;
        if (expiredTime != null) {
            seconds = expiredTime.getTime() - System.currentTimeMillis() / 1000;
            if (seconds > Integer.MAX_VALUE) {
                seconds = -1;
            }
            seconds -= 60;
        }
        CookieManager.getInstance().saveCookie(response, COOKIE_KEY_TOKEN,
				token.getToken(), (int) seconds, "/", RenrenProfileConstants.DOMAIN_URL);
		CookieManager.getInstance().saveCookie(response, COOKIE_KEY_USER, String.valueOf(token.getUserId()), (int) seconds, "/", RenrenProfileConstants.DOMAIN_URL);
    }

}
