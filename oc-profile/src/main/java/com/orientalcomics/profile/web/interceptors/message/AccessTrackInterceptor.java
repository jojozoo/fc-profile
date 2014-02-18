package com.orientalcomics.profile.web.interceptors.message; 

import java.lang.annotation.Annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orientalcomics.profile.web.annotations.PriCheckRequired;

import net.paoding.rose.web.ControllerInterceptorAdapter;
import net.paoding.rose.web.Invocation;

/** 
 * @author 张浩 E-mail:zhanghao@foundercomics.com 
 * @version 创建时间：2014年2月18日 上午11:32:30 
 * 类说明 
 */
public class AccessTrackInterceptor extends ControllerInterceptorAdapter {
	
	private static  Logger logger = LoggerFactory.getLogger(AccessTrackInterceptor.class);
	
	public AccessTrackInterceptor() {
		setPriority(2000);
	}
	
	public Class <? extends Annotation> getRequiredAnnotationClass(){
		return PriCheckRequired.class;
	}
	
	
	@Override
	public Object before(Invocation inv)throws Exception{
		
		if(logger.isDebugEnabled()){
			logger.debug("#######before");
		}
		logger.debug("#######before");
		return "/admin/shadow";
	}
	
	@Override
    public void afterCompletion(final Invocation inv, Throwable ex) throws Exception {
    // TODO ....
		
		System.out.println("#######completion");
		if(logger.isDebugEnabled()){
			logger.debug("#######completion");
		}
    }
}
 