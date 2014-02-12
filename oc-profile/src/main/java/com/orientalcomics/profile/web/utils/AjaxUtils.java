package com.orientalcomics.profile.web.utils;

import net.paoding.rose.web.Invocation;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.orientalcomics.profile.core.base.ClassUtils;
import com.orientalcomics.profile.web.annotations.Ajaxable;
import com.orientalcomics.profile.web.constants.AjaxType;


public final class AjaxUtils {
    public static final String AJAX_KEY = "__ajax";

    /**
     * 获得Http请求中的Ajax类型
     * 
     * @param inv
     * @return
     */
    public static AjaxType getRequestAjaxType(Invocation inv) {
        String ajaxTypeString = inv.getParameter("__ajax");
        if (ajaxTypeString == null) {
            return null;
        }
        for (AjaxType ajaxType : AjaxType.values()) {
            if (ajaxType != AjaxType.UNKOWN) {
                if (StringUtils.equalsIgnoreCase(ajaxType.name(), ajaxTypeString)) {
                    return ajaxType;
                }
            }
        }
        return AjaxType.UNKOWN;
    }

    /**
     * 获得当前Action支持的Ajax类型
     * 
     * @param inv
     * @return
     */
    public static boolean isActionSupport(Invocation inv, AjaxType ajaxType) {
        Ajaxable ajaxable = ClassUtils.getAnnotationFromMethodAndClass(inv, Ajaxable.class);
        if (ajaxable == null) {// 当前方法未标注，表示不支持Ajax
            return ajaxType == null;
        }
        if (ajaxType == null && ajaxable.must()) {
            return false;
        }
        if (ajaxType == AjaxType.UNKOWN) {
            return false;
        }
        AjaxType[] ajaxTypes = ajaxable.value();
        if (ArrayUtils.contains(ajaxTypes, ajaxType)) {
            return true;
        }
        return false;
    }
}
