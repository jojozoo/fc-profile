package com.renren.profile;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


public interface RenrenProfileConstants {
    // 字面常量
    String DB_CATALOG                   = "";
    String EMPTY_HEAD_PIC_URL           = "/static/images/man_headpic.gif";
    String OA_URL                       = "https://accounts.google.com/o/oauth2/auth";

    String OA_REDIRECT_URL_USER_INFO    = "http://localhost:8080/oauth2callback";

    String SESSION_KEY_LOGINED_USER     = "$$profile.logined_user";

    String SESSION_KEY_LOGINED_MYFOLLOW = "$$profile.my_follow";

    String HTTP_KEY_OWNER               = "owner";

    int    PERF_TIME_START              = 1;

    int    PERF_TIME_END                = 1;
    
    
    String DOMAIN                       = "profile";
    
    
    String                     COOKIE_KEY_USER           = "u";
    String                     COOKIE_KEY_TOKEN          = "t";
    // webhost在server_instance.xml中配置
    String                     PROFILE_CONF_HOST         = (String) ObjectUtils.defaultIfNull(StringUtils.trimToNull(System.getProperty("webhost")), "who");
    String                     PROFILE_MAIN_DOMAIN       = "731275.ichengyun.net";
    int                        MAX_LOGIN_DAYS            = 365;
	String                     DOMAIN_URL                = "localhost";
}
