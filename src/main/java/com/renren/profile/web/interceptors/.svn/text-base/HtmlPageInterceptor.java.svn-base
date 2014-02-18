package com.renren.profile.web.interceptors;

import net.paoding.rose.web.Invocation;

import org.json.simple.JSONObject;

import com.renren.profile.web.annotations.AjaxJson;
import com.renren.profile.web.base.HtmlPageImpl;
import com.renren.profile.web.base.RoseUtils;
import com.renren.profile.web.util.ClassUtils;
import com.renren.profile.web.util.JsonUtils;
import com.renren.profile.web.util.JsonUtils.JsonBuilder;

public class HtmlPageInterceptor extends AbstractControllerInterceptorAdapter {
    @Override
    public int getPriority() {
        return 100100;// 必须大于loginSecurityInteceptor
    }

    
    @Override
    protected Object after(Invocation inv, Object instruction) throws Exception {
        // 如果是Ajax请求，则将HtmlPage中的消息封装成JSON
        if (ClassUtils.isAnnotationPresentOnMethodAndClass(inv, AjaxJson.class)) {
            if (instruction instanceof JSONObject) {
                return instruction;
            }
            // 查看是否有HtmlPage
            HtmlPageImpl page = (HtmlPageImpl) RoseUtils.getHtmlMessages(inv);
            if (page != null) {
                // 注入到Ajax中
                JsonBuilder jb = JsonUtils.builder();
                jb.put("code", page.getCode())//
                        .putIntIfNotEmpty("errors", page.getErrors())//
                        .putIntIfNotEmpty("warnings", page.getWarnings())//
                        .putIntIfNotEmpty("infos", page.getInfos())//
                        .putObjIfNotNull("data", page.getData())//
                        .putObjIfNotNull("url", page.getUrl())//
                        .putObjIfNotNull("alert", page.getAlert())//
                        .putObjIfNotNull("extras", instruction)//
                ;
                return jb.build();
            }
        }

        return super.after(inv, instruction);
    }
}