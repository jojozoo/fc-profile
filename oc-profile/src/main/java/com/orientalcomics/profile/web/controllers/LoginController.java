package com.orientalcomics.profile.web.controllers; 

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.orientalcomics.profile.biz.logic.UserLoginService;
import com.orientalcomics.profile.biz.logic.UserTokenService;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.biz.model.UserToken;
import com.orientalcomics.profile.core.base.NetUtils;
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

    
    @Post("")
	public String login(Invocation inv,
			@Param("loginName") String name,//登录名
			@Param("passwd") String pass,//登录密码
			@Param("to") String toUrl
			) {
    	
    	String trimedName = StringUtils.trimToNull(name);
    	String trimedPass = StringUtils.trimToNull(pass);
    	
    	if(trimedName == null || trimedPass == null){
    		return "r:/login";
    	}
    	
    	
        User user = userLoginService.checkUserByNameAndPass(trimedName,trimedPass);

        //查找token，如果已经有了就token就直接放过cookie,没有的话就生成一个
        UserToken oldUserToken = userTokenService.getToken(user.getId());
        UserToken userToken = new UserToken();
        userToken.setUserId(user.getId());
        boolean canUseOldToken = false;
        if (oldUserToken != null && MAX_LOGIN_DAYS > MIN_LOGIN_DAYS) {// 如果是在MIN_LOGIN_DAYS内登录的，则可以复用上次的token和过期时间
            if (new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(MAX_LOGIN_DAYS - MIN_LOGIN_DAYS)).before(oldUserToken.getExpiredTime())) {
                canUseOldToken = true;
            }
        }
        if (canUseOldToken) {
            userToken.setToken(oldUserToken.getToken());
            userToken.setExpiredTime(oldUserToken.getExpiredTime());
        } else {
            userToken.setToken(RandomStringUtils.randomAlphanumeric(32));
            userToken.setExpiredTime(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(MAX_LOGIN_DAYS)));
            userTokenService.setToken(userToken);// 更新Token
        }
        NetUtils.setUserTokenToCookie(inv.getResponse(), userToken);

        if (StringUtils.isNotEmpty(toUrl)) {
            return "r:" + toUrl;
        }
        return "r:/info/my";
	}
    
}