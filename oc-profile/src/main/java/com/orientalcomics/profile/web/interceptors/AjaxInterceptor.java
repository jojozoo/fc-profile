package com.orientalcomics.profile.web.interceptors;

import net.paoding.rose.web.Invocation;

import com.orientalcomics.profile.core.base.HtmlPageImpl;
import com.orientalcomics.profile.core.base.RoseUtils;
import com.orientalcomics.profile.web.constants.AjaxType;
import com.orientalcomics.profile.web.utils.AjaxUtils;

/**
 * 验证Action是否支持用户的Ajax请求
 */
public class AjaxInterceptor extends AbstractControllerInterceptorAdapter {

    @Override
    public PriorityType getPriorityType() {
        return PriorityType.Ajax;
    }

    @Override
    protected Object before(Invocation inv) throws Exception {
        AjaxType ajaxType = AjaxUtils.getRequestAjaxType(inv);
        if(AjaxUtils.isActionSupport(inv, ajaxType)){
            return null;
        }

        HtmlPageImpl page = (HtmlPageImpl) RoseUtils.currentHtmlMessages(inv);
        page.error("不支持Ajax请求");
        return false;
    }
}