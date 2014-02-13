package com.orientalcomics.profile.web.interceptors.message;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.paramresolver.ParamMetaData;
import net.paoding.rose.web.paramresolver.ParamResolver;

public class PageMessagesResolver implements ParamResolver {

	 public boolean supports(ParamMetaData metaData)
	  {
	    return PageMessages.class == metaData.getParamType();
	  }

	  public Object resolve(Invocation inv, ParamMetaData metaData) throws Exception
	  {
	    return PageMessagesUtil.getPageMessages(inv);
	  }
	}