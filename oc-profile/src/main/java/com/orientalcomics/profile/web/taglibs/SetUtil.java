package com.orientalcomics.profile.web.taglibs; 

import java.util.Collection;

/** 
 * @author 张浩 E-mail:zhanghao@foundercomics.com 
 * @version 创建时间：2014年2月25日 下午5:24:37 
 * 类说明 给set contains  tag做的handler class
 */
public class SetUtil {
	public static boolean contains(Collection<?> coll, Object o) {
		if(coll == null || coll.size() == 0){
			return false;
		}
		if(o == null){
			return false;
		}
	    return coll.contains(o);
	  }
}
 