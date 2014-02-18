package com.renren.profile.web.interceptors;

import java.net.URLEncoder;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Interceptor;

import org.springframework.beans.factory.annotation.Autowired;

import com.renren.profile.RenrenProfileAjaxCodes;
import com.renren.profile.biz.logic.UserService;
import com.renren.profile.biz.logic.UserTokenService;
import com.renren.profile.biz.model.User;
import com.renren.profile.biz.model.access.UserToken;
import com.renren.profile.web.access.ProfileHostHolderImpl;
import com.renren.profile.web.annotations.AjaxJson;
import com.renren.profile.web.annotations.LoginRequired;
import com.renren.profile.web.base.HtmlPageImpl;
import com.renren.profile.web.base.RoseUtils;
import com.renren.profile.web.util.ClassUtils;
import com.renren.profile.web.util.NetUtils;

@Interceptor(oncePerRequest=true)
public class LoginInterceptor extends AbstractControllerInterceptorAdapter {

    @Autowired
    private ProfileHostHolderImpl profileHostHolder;

    @Autowired
    private UserService           userService;
    
    @Autowired
    private UserTokenService      userTokenService;

    @Override
    public int getPriority() {
        return 100009;
    }

    @Override
    protected Object before(Invocation inv) throws Exception {
    	
    	User user = profileHostHolder.getUser();
    	if(user == null){
    		UserToken requestToken = NetUtils.getUserTokenFromCookie(inv.getRequest());
            int userId = requestToken.getUserId();
            if (userId > 0 && userTokenService.isValid(requestToken)) {
                user = userService.query(userId);// 登录成功
            }

            if(user != null) {
            	profileHostHolder.setUser(user);
            }
                  
            if (user == null && ClassUtils.isAnnotationPresentOnMethodAndClass(inv, LoginRequired.class)) {
                if (ClassUtils.isAnnotationPresentOnMethodAndClass(inv, AjaxJson.class)) {
                    HtmlPageImpl page = (HtmlPageImpl) RoseUtils.currentHtmlMessages(inv);
                    page.code(RenrenProfileAjaxCodes.NEED_LOGIN);
                    return false;
                } else {
                    String toUrl = URLEncoder.encode(inv.getRequestPath().getUri(), "UTF-8");
                    return "r:/login?to=" + toUrl; // 要求登陆
                }
            }
    	}
		
        return true;
    }
}