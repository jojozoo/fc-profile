package com.orientalcomics.profile.web.controllers; 

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.rest.Get;

import org.springframework.beans.factory.annotation.Autowired;

import com.orientalcomics.profile.biz.dao.TestDAO;
import com.orientalcomics.profile.biz.model.Test;
/** 
 * @author 张浩 E-mail:zhanghao@foundercomics.com 
 * @version 创建时间：2014年2月12日 下午4:49:53 
 * 类说明 :test
 */
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
	
	public String add(Invocation inv){
		Test test = new Test();
		test.setId(111);
		test.setMsg("dynamic add");
		testDAO.add(test);
		
		return "@insert complete";
	}
}
 