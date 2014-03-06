package com.orientalcomics.profile.web.controllers;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.rest.Get;

import org.springframework.beans.factory.annotation.Autowired;

import com.orientalcomics.profile.biz.logic.UserTokenService;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.web.access.ProfileHostHolder;
import com.orientalcomics.profile.web.controllers.internal.BaseController;

public class LogoutController extends BaseController  {

    @Autowired
    private ProfileHostHolder profileHostHolder;

    @Autowired
    private UserTokenService userTokenService;

    @Get("")
    public String index(Invocation inv) {
        User user = profileHostHolder.getUser();
        if (user != null) {
            userTokenService.clearToken(user.getId());
            profileHostHolder.setUser(null);
        }
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
