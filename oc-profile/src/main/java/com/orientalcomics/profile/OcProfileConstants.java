package com.orientalcomics.profile; 

import java.util.Date;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.orientalcomics.profile.util.time.TimeUtils;


/** 
 * @author 张浩 E-mail:zhanghao@foundercomics.com 
 * @version 创建时间：2014年2月12日 下午4:59:01 
 * 类说明 :定义的全局常量
 */
public interface OcProfileConstants {
	 // 字面常量
    boolean                    isDevEnv                  = StringUtils.equalsIgnoreCase("dev", System.getProperty("env"));
    boolean                    exeAutoTask               = StringUtils.isNotBlank(System.getProperty("autotask"));
    String                     DB_CATALOG                = "";
    String                     EXCEL_DATA_DIR            = "/data/profile-data/excel/profile.xls";
    String                     EXCEL_DATA_DIR_USER       = "/data/profile-data/excel/all-user.xls";
    String                     EMPTY_TINY_URL            = "/data/photos/empty_50.png";
    String                     DIR              =          "/data/profile-data/excel/";
    String                     EMPTY_MAIN_URL            = "/data/photos/empty_200.png";
    String                     REWARD_FLOWER_URL         = "/data/photos/flower.jpg";

    // webhost在resin.xml中配置
    String                     PROFILE_CONF_HOST         = (String) ObjectUtils.defaultIfNull(StringUtils.trimToNull(System.getProperty("webhost")), "who");
    String                     PROFILE_MAIN_DOMAIN       = "weekly.ccdany.com";

    String                     PROFILE_MAIN_URL          = "http://" + PROFILE_MAIN_DOMAIN;


    String                     COOKIE_KEY_USER           = "u";
    String                     COOKIE_KEY_TOKEN          = "t";

    String                     HTTP_KEY_OWNER            = "owner";

    int                        PERF_TIME_START           = 1;

    int                        PERF_TIME_END             = 1;

//    Version                    LUCENE_VERSION            = Version.LUCENE_35;

    String                     EMAIL_HOST                = "smtp.gmail.com";

    String                     SEND_EMAIL_HOST           = "no-reply@foundercomics.com";

    String                     SEND_EMAIL_HOST_NAME      = "who系统技术运维部";

    String                     MD5_PREFIX                = "accdeaaad-_daf&^%#@2sdfaa";

    int                        MIN_LOGIN_DAYS            = 2;                                                                                               // 用户在MIN_LOGIN_DAYS内登录时，不更新UserToken
    int                        MAX_LOGIN_DAYS            = 3;                                                                                               // 用户的Cookie保留这么长时间
    String                     EMAIL_MAIN_PATTERN        = "^[a-zA-z_.0-9]+$";
    String                     EMAIL_DOMAIN              = "foundercomcis.com";

    // 上线时间
    Date                       ONLINE_TIME               = TimeUtils.FetchTime.create(2012, 4, 23);                                                          // 上线时间
    // 图片

    public static final int    PHOTO_SIZE_TINY           = 50;
    public static final int    PHOTO_SIZE_MAIN           = 200;
    public static final int[]  PHOTO_SIZES               = { PHOTO_SIZE_TINY, PHOTO_SIZE_MAIN };
    public static final String PHOTO_EXT                 = "png";
    public static final String PHOTO_DATA_DIR            = "/data/profile-data";
    public static final String PHOTO_BASE_URL            = "/data";
    public static final int    PHOTO_CONTAINER_SIZE      = 300;

    // 页面title
    public static final String SYS_NAME                  = "技术评级系统";
    
    // ---------------------------------公共下载excel最大显示的数量--------------------//
    public static final int    MAXNUM_FOR_PER_SHEET      = 5000;
}
 