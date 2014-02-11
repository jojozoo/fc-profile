package com.orientalcomics.web.controllers; 

import java.io.UnsupportedEncodingException;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;

/** 
 * @author 张浩 E-mail:zhanghao@foundercomics.com 
 * @version 创建时间：2014年2月11日 下午4:35:55 
 * 类说明 :登录页面
 */

public class LoginController {
	
	@Get("")
    public String index(@Param("to") String toUrl,Invocation inv) throws UnsupportedEncodingException {
		
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

    	return "@aaa";
        
    }
}
 