package com.renren.profile.web.controllers;

import java.util.List;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.renren.profile.biz.dao.UserDAO;
import com.renren.profile.biz.logic.UserService;
import com.renren.profile.biz.model.User;
import com.renren.profile.web.controllers.internal.LoginRequiredController;

/**
 * 搜索页面
 * 
 * @author 黄兴海(xinghai.huang)
 * @date 2012-3-5 上午11:28:53
 */
public class SearchController extends LoginRequiredController {
    @Autowired
    UserService userService;

    private String escapeSql(String keyword) {
        return StringUtils.remove(keyword, "%_; '\"");
    }

    @Get
    @Post
    public String search(Invocation inv, @Param("keyword") String keyword) {
        inv.addModel("_search_keyword", keyword);

        String escapedKeyword = StringUtils.trimToNull(escapeSql(keyword));
        if (escapedKeyword == null) {// 未输入关键词
            return "r:/index";
        }

        List<User> users = userService.queryByCondition(//
                UserDAO.NAME + " like '%" + escapedKeyword + "%' " //
                        + " OR " + UserDAO.EMAIL + " like '%" + escapedKeyword + "%' " //
                        + " OR " + UserDAO.NUMBER + " = '" + escapedKeyword + "' "//
                , //
                0, 10000);
        inv.addModel("users", users);
        return "search_result";
    }
}
