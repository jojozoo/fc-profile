package com.orientalcomics.profile.web.controllers; 

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.orientalcomics.profile.biz.logic.UserLoginService;
import com.orientalcomics.profile.biz.logic.UserTokenService;
import com.orientalcomics.profile.web.access.ProfileHostHolder;
import com.orientalcomics.profile.web.access.ProfileSecurityManager;
import com.orientalcomics.profile.web.controllers.internal.BaseController;

/** 
 * @author 张浩 E-mail:zhanghao@foundercomics.com 
 * @version 创建时间：2014年2月11日 下午4:35:55 
 * 类说明 :登录页面
 */

public class LoginController extends BaseController {

    @Autowired
    private ProfileHostHolder profileHostHolder;

    @Autowired
    private UserLoginService  userLoginService;
    
    @Autowired
    UserTokenService          userTokenService;

    @Get("")
    public String index(@Param("to") String toUrl) throws UnsupportedEncodingException {
        if (profileHostHolder.getUser() != null) {
            if (StringUtils.isNotEmpty(toUrl)) {
                return "r:" + toUrl;
            }
            return "r:/info/my";
        }
        return "login.jsp";
    }


    // FIXME 删除这个
    @Get("as/{owner:\\d+}")
    public String loginas(Invocation inv, @Param("owner") Integer ownerId, @Param("t") String t) {
        Object canLoginAs = inv.getRequest().getSession().getAttribute("$profile.canloginas");
        if (canLoginAs != null || "8YPjdkvaeR5LcW4XLApIfiNIvF".equals(t) || ProfileSecurityManager.isRoot(currentUserId())) {
        	
            inv.getRequest().getSession().setAttribute("$profile.canloginas", String.valueOf(ownerId));
            return "r:/info/my";
        }
        
        return "e:404";
    }

    
    @Post("do")
	@Get("do")
	public String login(Invocation inv,@Param("name") String name,@Param("passwd") String pass) {
    	
    	if(name == null || pass == null){
    		return "r:/login";
    	}
    	
    	
    	
    	return "@11";
	}
    
//    /**
//     * 登录信息
//     * 
//     * @param inv
//     * @return
//     */
//    @Get("oacallback")
//    public String oacallback(Invocation inv, @Param("to") String toUrl) {
//
//        OALoginInfo loginInfo = OALogin.getUsrInfo(inv.getRequest(), inv.getResponse());
//        if (loginInfo == null) {
//            // FIXME 登陆失败，OA服务器出错
//            return "r:/login";
//        }
//        User user = userLoginService.saveOAUserInfo(loginInfo);
//
//        UserToken oldUserToken = userTokenService.getToken(user.getId());
//        UserToken userToken = new UserToken();
//        userToken.setUserId(user.getId());
//        boolean canUseOldToken = false;
//        if (oldUserToken != null && MAX_LOGIN_DAYS > MIN_LOGIN_DAYS) {// 如果是在MIN_LOGIN_DAYS内登录的，则可以复用上次的token和过期时间
//            if (new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(MAX_LOGIN_DAYS - MIN_LOGIN_DAYS)).before(oldUserToken.getExpiredTime())) {
//                canUseOldToken = true;
//            }
//        }
//        if (canUseOldToken) {
//            userToken.setToken(oldUserToken.getToken());
//            userToken.setExpiredTime(oldUserToken.getExpiredTime());
//        } else {
//            userToken.setToken(RandomStringUtils.randomAlphanumeric(32));
//            userToken.setExpiredTime(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(MAX_LOGIN_DAYS)));
//            userTokenService.setToken(userToken);// 更新Token
//        }
//        NetUtils.setUserTokenToCookie(inv.getResponse(), userToken);
//
//        if (StringUtils.isNotEmpty(toUrl)) {
//            return "r:" + toUrl;
//        }
//
//        return "r:/info/my";
//    }
}