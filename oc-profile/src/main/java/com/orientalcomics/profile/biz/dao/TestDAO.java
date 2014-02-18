package com.orientalcomics.profile.biz.dao; 

import com.orientalcomics.profile.biz.model.Test;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

/** 
 * @author 张浩 E-mail:zhanghao@foundercomics.com 
 * @version 创建时间：2014年2月13日 下午5:33:44 
 * 类说明 
 */
@DAO
public interface TestDAO {
	@SQL("select msg from test limit 1")
    public String getTestMsg();
	
	@SQL("select id from test limit 1")
    public Integer getId();
	
	@SQL("insert ignore into test (id,msg) values ("+":model.id,:model.msg)" )
	public Integer add(@SQLParam("model") Test test);
}
 