package com.orientalcomics.profile.web.controllers.download; 

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

/** 
 * @author 张浩 E-mail:zhanghao@foundercomics.com 
 * @version 创建时间：2014年3月19日 下午3:38:19 
 * 类说明 
 */
@Path("")
public class DownloadController {
	
	@Get("")
	@Post("")
	public String index(Invocation inv){
		
		return "download.jsp";
	}
}
 