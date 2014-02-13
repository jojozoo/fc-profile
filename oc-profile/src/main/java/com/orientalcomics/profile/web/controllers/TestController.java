package com.orientalcomics.profile.web.controllers; 

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;

import org.springframework.beans.factory.annotation.Autowired;

import com.orientalcomics.profile.biz.dao.TestDAO;
/** 
 * @author 张浩 E-mail:zhanghao@foundercomics.com 
 * @version 创建时间：2014年2月12日 下午4:49:53 
 * 类说明 :test
 */
@Path("/")
public class TestController {

	
	@Autowired
	private TestDAO testDAO;
	
	@Get("")
	public String hello(Invocation inv){
		return "index.jsp";
	}
	
	public String test(Invocation inv){
		
		return "@id:"+testDAO.getId()+"\n"+" msg:"+testDAO.getTestMsg();
	}
}
 