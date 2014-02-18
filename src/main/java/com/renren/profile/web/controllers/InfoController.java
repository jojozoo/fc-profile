package com.renren.profile.web.controllers;

import java.util.ArrayList;
import java.util.List;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;

import org.springframework.beans.factory.annotation.Autowired;

import com.renren.profile.biz.dao.BusinessTagDAO;
import com.renren.profile.biz.dao.UserProfileDAO;
import com.renren.profile.biz.logic.DepartmentService;
import com.renren.profile.biz.logic.UserService;
import com.renren.profile.biz.model.BusinessTag;
import com.renren.profile.biz.model.Department;
import com.renren.profile.biz.model.User;
import com.renren.profile.biz.model.UserProfile;
import com.renren.profile.util.Strings0;
import com.renren.profile.web.controllers.internal.LoginRequiredController;
import com.renren.profile.web.dto.UserExtendInfoDTO;

public class InfoController extends LoginRequiredController {
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService       userService;

    @Autowired
    private UserProfileDAO    userProfileDAO;

    @Autowired
    private BusinessTagDAO    businessTagDAO;

    @Get({ "", "my" })
    public String myInfo(Invocation inv) {

        // 得到所有下属的信息
        User user = currentUser();
        // List<BaseUserInfoDTO> myFollows = getMyFollowUser((List<HumresBean>)
        // inv.getRequest().getSession().getAttribute(SESSION_KEY_LOGINED_MYFOLLOW));
        List<User> userList = userService.queryAllMyFollow(user.getId());
        List<UserExtendInfoDTO> myFollows = getMyFollows(userList);

        // 得到部门、业务名、用户附件、上级领导的用户信息
        Department department = departmentService.queryById(user.getDepartmentId());
        List<BusinessTag> businessTags = businessTagDAO.query(user.getId());
        UserProfile userProfile = userProfileDAO.query(user.getId());
        User managerUser = userService.query(user.getManagerId());

        inv.addModel("user", user);
        inv.addModel("department", department);
        inv.addModel("businessTagNames", Strings0.convertStringByComma(businessTags));
        inv.addModel("userProfile", userProfile);
        inv.addModel("managerUser", managerUser);
        inv.addModel("myFollows", myFollows);

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

        User owner = userService.query(ownerId);
        if (owner == null)
            return "r:404";

        List<User> userList = userService.queryAllMyFollow(owner.getId());
        List<UserExtendInfoDTO> myFollows = getMyFollows(userList);
        inv.addModel("myFollows", myFollows);

        Department department = departmentService.queryById(owner.getDepartmentId());
        List<BusinessTag> businessTags = businessTagDAO.query(owner.getId());
        UserProfile userProfile = userProfileDAO.query(owner.getId());
        User managerUser = userService.query(owner.getManagerId());

        inv.addModel("user", owner);
        inv.addModel("department", department);
        inv.addModel("businessTagNames", Strings0.convertStringByComma(businessTags));
        inv.addModel("userProfile", userProfile);
        inv.addModel("managerUser", managerUser);

        return "info_other";
    }

    // /**
    // * 得到我的下属信息,bean要进行转换
    // *
    // * @param rlist
    // * @return
    // */
    // private List<BaseUserInfoDTO> getMyFollowUser(List<HumresBean> rlist) {
    //
    // List<BaseUserInfoDTO> baseUserInfos = new ArrayList<BaseUserInfoDTO>();
    //
    // for (HumresBean hums : rlist) {
    //
    // BaseUserInfoDTO baseUserInfo = new BaseUserInfoDTO();
    // baseUserInfo.setName(hums.getObjname());
    // baseUserInfo.setEmail(hums.getEmail());
    // baseUserInfo.setPic(hums.getBigpic());
    // baseUserInfo.setJobTitle(hums.getMainstationname());
    // baseUserInfo.setDeparmentName(hums.getOrgidname());
    //
    // baseUserInfos.add(baseUserInfo);
    // }
    //
    // return baseUserInfos;
    // }

    /**
     * 得到我的下属信息,bean要进行转换
     * 
     * @param rlist
     * @return
     */
    private List<UserExtendInfoDTO> getMyFollows(List<User> rlist) {

        List<UserExtendInfoDTO> baseUserInfos = new ArrayList<UserExtendInfoDTO>();

        for (User user : rlist) {

            UserExtendInfoDTO baseUserInfo = new UserExtendInfoDTO();
            baseUserInfo.setUser(user);

            Department deparment = departmentService.queryById(user.getDepartmentId());
            baseUserInfo.setDeparmentName(deparment != null ? deparment.getDepartmentName() : null);

            baseUserInfos.add(baseUserInfo);

        }

        return baseUserInfos;
    }

}
