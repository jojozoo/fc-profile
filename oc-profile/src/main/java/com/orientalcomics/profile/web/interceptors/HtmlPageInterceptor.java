package com.orientalcomics.profile.web.interceptors;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import net.paoding.rose.web.Invocation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;

import com.orientalcomics.profile.OcProfileAjaxCodes;
import com.orientalcomics.profile.core.base.HtmlPageImpl;
import com.orientalcomics.profile.core.base.NetUtils;
import com.orientalcomics.profile.core.base.RoseUtils;
import com.orientalcomics.profile.util.common.JsonUtils;
import com.orientalcomics.profile.util.common.JsonUtils.JsonBuilder;
import com.orientalcomics.profile.web.constants.AjaxType;
import com.orientalcomics.profile.web.utils.AjaxUtils;

public class HtmlPageInterceptor extends AbstractControllerInterceptorAdapter {
    @Override
    public PriorityType getPriorityType() {
        return PriorityType.HtmlPage;
    }

    @Override
    protected Object before(Invocation inv) throws Exception {
        inv.getFlash(true).get("");// 获取Flash，并清除以前的Flash值
        return super.before(inv);
    }

    @Override
    protected Object after(Invocation inv, Object instruction) throws Exception {
        // 查看是否有HtmlPage
        HtmlPageImpl page = (HtmlPageImpl) RoseUtils.getHtmlMessages(inv);
        if (page == null) {
            return null;
        }

        AjaxType ajaxType = AjaxUtils.getRequestAjaxType(inv);
        if (ajaxType == null || ajaxType == AjaxType.HTML) {// HTML的
            boolean isRedirect = page.getCode() == OcProfileAjaxCodes.REDIRECT;
            if (instruction instanceof String) {
                String string = (String) instruction;
                isRedirect = isRedirect || string.startsWith("r:");
            }

            if (isRedirect) {
                inv.addFlash("pf.code", String.valueOf(page.getCode()));
                if (StringUtils.isNotBlank(page.getAlert())) {
                    inv.addFlash("pf.alert", page.getAlert());
                }

                Map<String, List<String>> errors = page.getErrors();
                if (errors != null) {
                    addFlash(inv, "pf.errors", errors.get(""));
                }
                Map<String, List<String>> warnings = page.getWarnings();
                if (warnings != null) {
                    addFlash(inv, "pf.warnings", warnings.get(""));
                }
                Map<String, List<String>> infos = page.getInfos();
                if (infos != null) {
                    addFlash(inv, "pf.infos", infos.get(""));
                }
            } else {
                inv.addModel("p_code", page.getCode());
                Map<String, List<String>> errors = page.getErrors();
                if (errors != null) {
                    inv.addModel("p_errors", errors);
                    inv.addModel("p_global_errors", errors.get(""));
                }

                Map<String, List<String>> warnings = page.getWarnings();
                if (warnings != null) {
                    inv.addModel("warnings", warnings);
                    inv.addModel("p_global_warnings", warnings.get(""));
                }

                Map<String, List<String>> infos = page.getInfos();
                if (infos != null) {
                    inv.addModel("p_infos", infos);
                    inv.addModel("p_global_infos", infos.get(""));
                }
                if (page.getData() != null) {
                    inv.addModel("p_data", page.getData());
                }
                if (page.getAlert() != null) {
                    inv.addModel("alert", page.getAlert());//
                }
            }

            switch (page.getCode()) {
                case OcProfileAjaxCodes.NORMAL:
                case OcProfileAjaxCodes.ALERT:
                case OcProfileAjaxCodes.ERROR: {
                    return instruction;
                }
                case OcProfileAjaxCodes.EXPIRED_PAGE: {
                    return "r:";
                }
                case OcProfileAjaxCodes.NO_AUTH: {
                    return "@对不起，您没有访问该内容的权限！";
                }
                case OcProfileAjaxCodes.NEED_LOGIN: {
                    String toUrl = URLEncoder.encode(NetUtils.getUrlWithQueryString(inv.getRequest()), "UTF-8");
                    return "r:/login?to=" + toUrl; // 要求登陆
                }
                case OcProfileAjaxCodes.REDIRECT: {
                    return "r:" + StringUtils.trimToEmpty(page.getUrl());
                }
                case OcProfileAjaxCodes.NO_CURRENT_PERF: {
                    return "@Oops, 绩效考核还没有开始呢~";
                }
                default:
                    break;
            }
            return null;
        } else {
            switch (ajaxType) {
                case HTML:
                case JSON: {
                    if (instruction instanceof JSONObject) {
                        return instruction;
                    }
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
                default:
                    break;
            }
            return null;
        }
    }

    private void addFlash(Invocation inv, String name, List<String> values) {
        if (CollectionUtils.isNotEmpty(values)) {
            for (String value : values) {
                inv.addFlash(name, value);
            }
        }
    }
}