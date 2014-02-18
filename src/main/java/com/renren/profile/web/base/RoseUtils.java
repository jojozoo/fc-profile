package com.renren.profile.web.base;

import org.apache.commons.lang.math.NumberUtils;

import com.renren.profile.RenrenProfileConstants;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.InvocationUtils;

public class RoseUtils {
    public static Invocation currentInvocation() {
        return InvocationUtils.getCurrentThreadInvocation();
    }

    public static HtmlPage getHtmlMessages(Invocation inv) {
        return inv == null ? null : (HtmlPage) inv.getModel(HtmlPage.MODEL_KEY);
    }

    public static HtmlPage currentHtmlMessages(Invocation invocation) {
        if (invocation == null) {
            return new HtmlPageImpl();
        }
        HtmlPage result = (HtmlPage) invocation.getModel(HtmlPage.MODEL_KEY);
        if (result == null) {
            result = new HtmlPageImpl();
            invocation.addModel(HtmlPage.MODEL_KEY, result);
        }
        return result;
    }

    public static int getResourceOwner(Invocation inv) {
        String ownerKey = RenrenProfileConstants.HTTP_KEY_OWNER;
        String owner = inv.getParameter(ownerKey);
        return NumberUtils.toInt(owner, 0);
    }
}
