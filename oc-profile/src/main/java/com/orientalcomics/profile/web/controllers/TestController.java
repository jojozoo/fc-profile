package com.orientalcomics.profile.web.controllers; 

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Path;
/** 
 * @author 张浩 E-mail:zhanghao@foundercomics.com 
 * @version 创建时间：2014年2月12日 下午4:49:53 
 * 类说明 :test
 */
@Path("/")
public class TestController {

	public String hello(Invocation inv){
		return "@hello";
	}
}
 