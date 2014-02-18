package com.renren.profile.web.controllers;

import java.io.UnsupportedEncodingException;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.renren.profile.biz.logic.UserLoginService;
import com.renren.profile.biz.logic.UserTokenService;
import com.renren.profile.biz.model.User;
import com.renren.profile.biz.model.access.UserToken;
import com.renren.profile.web.controllers.internal.BaseController;
import com.renren.profile.web.interceptors.ProfileHostHolder;
import com.renren.profile.web.util.NetUtils;

public class LoginController extends BaseController {

    @Autowired
    private ProfileHostHolder profileHostHolder;

    @Autowired
    private UserLoginService  userLoginService;
    
    
    @Autowired
    private UserTokenService userTokenService;

    @Get("")
    public String index(@Param("to") String toUrl,Invocation inv) throws UnsupportedEncodingException {
        if (profileHostHolder.getUser() != null) {
            if (StringUtils.isNotEmpty(toUrl)) {
                return "r:" + toUrl;
            }
            return "r:/info/my";
        }
        inv.addModel("to",toUrl);
        return "login.jsp";
    }
    
    /**
     * 登录信息
     * 
     * @param inv
     * @return
     */
    @Get("do")
    public String doLogin(Invocation inv, @Param("to") String toUrl,@Param("name") String name,@Param("passwd") String passwd) {

        User user = userLoginService.loginUser(name, passwd);
        if(user != null){
        	inv.getRequest().getSession().setAttribute(SESSION_KEY_LOGINED_USER, user == null ? null : Integer.valueOf(user.getId()));

        	UserToken token = userTokenService.getToken(user.getId());
        	if(token == null){
        		token = userTokenService.generateToken(user.getId());
        		userTokenService.setToken(token);
        	}
        	NetUtils.setUserTokenToCookie(inv.getResponse(), token);
        	
            if (StringUtils.isNotEmpty(toUrl)) {
                return "r:" + toUrl;
            }

            return "r:/info/my";
        }else{
        	inv.addModel("msg","login failed");
        	return "r:/login";
        }
        
    }

}
