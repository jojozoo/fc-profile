package com.renren.profile.web.controllers;

import java.util.ArrayList;
import java.util.List;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Post;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.renren.profile.biz.dao.BusinessTagDAO;
import com.renren.profile.biz.dao.UserProfileDAO;
import com.renren.profile.biz.logic.UserService;
import com.renren.profile.biz.model.BusinessTag;
import com.renren.profile.util.Strings0;
import com.renren.profile.util.time.TimeUtils;
import com.renren.profile.web.controllers.internal.LoginRequiredController;
import com.renren.profile.web.util.JsonUtils;

/**
 * 用户自己的信息管理
 * 
 * @author wen.he1
 * 
 */
public class UserSelfController extends LoginRequiredController {

    @Autowired
    private UserProfileDAO      userProfileDAO;

    @Autowired
    private UserService             userService;

    @Autowired
    private BusinessTagDAO      businessTagDAO;

    private static final String MY_INFO = "r:/../info/my";

    /**
     * 修改用户的头像
     * 
     * @return
     */
    @Post("modifyHeadPic")
    public String modifyUserHeadPic(Invocation inv, @Param("headUrl") String headUrl) {

        userService.updateUserHeadUrl(headUrl, this.currentUserId());
        if (isAjaxHtml(inv)) {
            return "@" + headUrl;
        } else if (isAjaxJson(inv)) {
            return "@" + JsonUtils.builder().put("result", headUrl).toString();
        } else {
            return MY_INFO;
        }
    }

    /**
     * 修改用户的业余爱好
     * 
     * @param hobby
     * @return
     */
    @Post("modifyHobby")
    public String modifyUserHobby(Invocation inv, @Param("hobby") String hobby) {

        userProfileDAO.updateUserHobby(hobby, this.currentUserId());
        if (isAjaxHtml(inv)) {
            return "@" + hobby;
        } else if (isAjaxJson(inv)) {
            return "@" + JsonUtils.builder().put("result", hobby).toString();
        } else {
            return MY_INFO;
        }
    }

    /**
     * 修改用户的QQ
     * 
     * @param qq
     * @return
     */
    @Post("modifyQQ")
    public String modifyUserQQ(Invocation inv, @Param("qq") String qq) {

        long strQQ = NumberUtils.toLong(qq);

        userProfileDAO.updateUserQQ(strQQ, this.currentUserId());
        if (isAjaxHtml(inv)) {
            return "@" + strQQ;
        } else if (isAjaxJson(inv)) {
            return "@" + JsonUtils.builder().put("result", qq).toString();
        } else {
            return MY_INFO;
        }
    }

    /**
     * 用户是否隐藏职称的显示；1：隐藏，0：不隐藏
     * 
     * @param is_show
     * @return
     */
    @Post("showlevel")
    public String showUserJobTitle(Invocation inv) {

        userService.updateUserShowLevel(1, this.currentUserId());
        if (isAjaxHtml(inv)) {
            return "@true";
        } else if (isAjaxJson(inv)) {
            return "@" + JsonUtils.builder().put("result", true).toString();
        } else {
            return MY_INFO;
        }
    }

    @Post("hidelevel")
    public String hideUserJobTitle(Invocation inv) {

        userService.updateUserShowLevel(0, this.currentUserId());
        if (isAjaxHtml(inv)) {
            return "@true";
        } else if (isAjaxJson(inv)) {
            return "@" + JsonUtils.builder().put("result", true).toString();
        } else {
            return MY_INFO;
        }
    }

    /**
     * 修改业务的标签
     * 
     * @return
     */
    @Post("modifyTag")
    public String modifyUserBusinessTag(Invocation inv, @Param("busName") String newTagNames) {

        List<BusinessTag> tagList = businessTagDAO.query(this.currentUserId());
        String oldTagNames = Strings0.convertStringByComma(tagList);

        List<String> delTagList = compareStringByCommaDifference(oldTagNames, newTagNames);
        List<String> addTagList = compareStringByCommaDifference(newTagNames, oldTagNames);

        for (String name : delTagList)
            businessTagDAO.delete(this.currentUserId(), name);

        for (String name : addTagList) {
            BusinessTag businessTag = new BusinessTag();
            businessTag.setChargeUser(this.currentUserId());
            businessTag.setEditTime(TimeUtils.FetchTime.now());
            businessTag.setTagName(name);
            businessTagDAO.save(businessTag);
        }
        if (isAjaxHtml(inv)) {
            return "@" + newTagNames;
        } else if (isAjaxJson(inv)) {
            return "@" + JsonUtils.builder().put("result", newTagNames).toString();
        } else {
            return MY_INFO;
        }
    }

    /**
     * 两个字符串str1和str2进行比较；注意两个字符串的格式是:"aa,bb,cc"<br>
     * 也就是把两个逗号隔开的字符串进行比较。<br>
     * <p>
     * </P>
     * 实例：<br>
     * &nbsp&nbsp str1="aa,bb,cc,dd";<br>
     * &nbsp&nbsp str2="bb,dd,ac";<br>
     * &nbsp&nbsp 那么返回的结果是：{"aa","cc"}
     * 
     * @param str1
     * @param str2
     * @return
     */
    public static List<String> compareStringByCommaDifference(String str1, String str2) {

        String[] strs1 = StringUtils.split(str1, ",");
        String[] strs2 = StringUtils.split(str2, ",");

        List<String> strList1 = new ArrayList<String>();
        List<String> strList2 = new ArrayList<String>();
        for (String str : strs1)
            strList1.add(str);
        for (String str : strs2)
            strList2.add(str);

        List<String> diffList = new ArrayList<String>();
        for (String str : strList1) {
            if (!strList2.contains(str))
                diffList.add(str);
        }

        return diffList;
    }

    public static void main(String[] args) {
        String str1 = "hewen,hhe,heh,wii,li";
        String str2 = "hewen,li,hh,rr";

        List<String> diffList = compareStringByCommaDifference(str1, str2);

        System.out.println("删除的:");
        for (String name : diffList) {
            System.out.print(name + "--");
        }

        diffList = compareStringByCommaDifference(str2, str1);
        System.out.println();
        System.out.println("添加的:");
        for (String name : diffList) {
            System.out.print(name + "--");
        }

    }

}
