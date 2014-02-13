package com.orientalcomics.profile.web.interceptors;

import net.paoding.rose.web.ControllerInterceptorAdapter;

public abstract class AbstractControllerInterceptorAdapter extends ControllerInterceptorAdapter {
    // 定义级别，越往前，越先执行
    protected static enum PriorityType {
        BROWSER, // 唉，浏览器探测
        HtmlPage, // 保证能够拿到Page
        Ajax, // 保证Ajax请求是有效的
        Login, // 保证用户登陆了
        Security, // 保证用户有权限
        RequirePerf, // 保证当前绩效的权限
        Env, // 【附加】只是加入了一些状态字段
        ;
    };

    @Override
    public final int getPriority() {
        PriorityType priorityType = getPriorityType();
        int order = priorityType == null ? PriorityType.values().length : priorityType.ordinal();
        return 1000000 - order;
    }

    protected abstract PriorityType getPriorityType();

}
