package com.renren.profile.web.util;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.InvocationUtils;

import com.renren.profile.util.logging.ILogger;
import com.renren.profile.util.logging.ProfileLogger;

public final class ControllerLogger extends ProfileLogger implements ILogger {
    public static <T> ILogger getLogger(Class<T> clazz) {
        return new ControllerLogger(clazz);
    }

    private final String controllerName;

    private <T> ControllerLogger(Class<T> clazz) {
        super(clazz);
        this.controllerName = clazz.getSimpleName();
    }

    @Override
    protected void doPrefixMessage(StringBuilder result) {
        Invocation inv = InvocationUtils.getCurrentThreadInvocation();
        String methodName = "_UnkownMethod_";
        String uri = "_UnkownUri_";
        if (inv != null) {
            methodName = inv.getMethod().getName();
            uri = inv.getRequestPath().getUri();
        }
        result.append("controller").append("|").append(controllerName).append("|").append(methodName).append("|").append(uri);
    }

}
