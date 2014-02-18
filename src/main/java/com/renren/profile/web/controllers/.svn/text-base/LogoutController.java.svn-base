package com.renren.profile.web.controllers;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.rest.Get;

import com.renren.profile.web.controllers.internal.BaseController;

public class LogoutController extends BaseController {

    @Get("")
    public String index(Invocation inv) {
        inv.getRequest().getSession().setAttribute(SESSION_KEY_LOGINED_USER, null);
        return "r:/logout/tip";
    }

    @Get("tip")
    public String tip() {
        if (isLogined()) {
            return "r:/logout";
        }
        return "logout";
    }

}
