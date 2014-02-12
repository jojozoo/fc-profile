package com.orientalcomics.profile.web.controllers;

import java.util.List;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.dao.UserProfileDAO;
import com.orientalcomics.profile.biz.logic.BusinessTagService;
import com.orientalcomics.profile.biz.logic.DepartmentService;
import com.orientalcomics.profile.biz.logic.UserService;
import com.orientalcomics.profile.biz.model.BusinessTag;
import com.orientalcomics.profile.biz.model.Department;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.biz.model.UserProfile;
import com.orientalcomics.profile.constants.ProfileAction;
import com.orientalcomics.profile.core.base.CookieManager;
import com.orientalcomics.profile.util.common.Strings0;
import com.orientalcomics.profile.web.annotations.ProfileSecurity;
import com.orientalcomics.profile.web.controllers.internal.LoginRequiredController;
import com.orientalcomics.profile.web.dto.UserExtendInfoDTO;

@ProfileSecurity(ProfileAction.VIEW_INFO)
public class InfoController extends LoginRequiredController {
    @Autowired
    private DepartmentService  departmentService;

    @Autowired
    private UserService        userService;

    @Autowired
    private UserProfileDAO     userProfileDAO;

    @Autowired
    private BusinessTagService businessTagService;

    @Get({ "", "my" })
    public String myInfo(Invocation inv) {

        // 得到所有下属的信息
        User user = currentUser();

        List<User> userList = userService.queryAllMyFollow(user.getId());
        List<UserExtendInfoDTO> myFollows = userService.getMyFollowsAndContainsStatus(userList);

        // 得到部门、业务名、用户附件、上级领导的用户信息
        Department department = departmentService.queryById(user.getDepartmentId());
        List<BusinessTag> businessTags = businessTagService.query(user.getId());
        UserProfile userProfile = userProfileDAO.query(user.getId());
        User managerUser = userService.query(user.getManagerId());

        inv.addModel("user", user);
        inv.addModel("department", department);
        inv.addModel("businessTagNames", Strings0.convertStringByComma(businessTags));
        inv.addModel("userProfile", userProfile);
        inv.addModel("managerUser", managerUser);
        inv.addModel("myFollows", myFollows);
        String userId = CookieManager.getInstance().getCookie(inv.getRequest(),OcProfileConstants.COOKIE_KEY_USER);
        if (StringUtils.isNotEmpty(userId) && StringUtils.equalsIgnoreCase(userId, String.valueOf(user.getId()))) {
        	inv.addModel("is_notice", 1);
        }

        return "info_my";
    }

    /**
     * 浏览其他人的信息
     * 
     * @param inv
     * @param userId
     * @return
     */
    @Get("{owner:\\d+}")
    public String othersInfo(Invocation inv, @Param("owner") int ownerId) {
        if (ownerId == currentUserId()) {
            return "r:/info/my";
        }
        User owner = userService.query(ownerId);
        if (owner == null)
            return "r:404";

        List<User> userList = userService.queryAllMyFollow(owner.getId());
        List<UserExtendInfoDTO> myFollows = userService.getMyFollows(userList);
        inv.addModel("myFollows", myFollows);

        Department department = departmentService.queryById(owner.getDepartmentId());
        List<BusinessTag> businessTags = businessTagService.query(owner.getId());
        UserProfile userProfile = userProfileDAO.query(owner.getId());
        User managerUser = userService.query(owner.getManagerId());

        inv.addModel("user", owner);
        inv.addModel("department", department);
        inv.addModel("businessTagNames", Strings0.convertStringByComma(businessTags));
        inv.addModel("userProfile", userProfile);
        inv.addModel("managerUser", managerUser);

        return "info_other";
    }

}
