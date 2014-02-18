package com.renren.profile.web.controllers;

import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;

import com.renren.profile.web.controllers.internal.BaseController;

@Path({ "", "index" })
public class IndexController extends BaseController {
    @Get("")
    public String index() {
        return "r:/info/my";
    }
}
