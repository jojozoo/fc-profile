package com.orientalcomics.profile.biz.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.orientalcomics.profile.biz.dao.SecurityRoleSettingDAO;
import com.orientalcomics.profile.biz.logic.UserRoleService;
import com.orientalcomics.profile.biz.logic.UserService;
import com.orientalcomics.profile.constants.ProfileAction;
import com.orientalcomics.profile.constants.ProfileBuildinRole;


/**
 * 角色验证
 * 
 * @author 黄兴海(xinghai.huang)
 * @date 2012-3-28 上午11:54:06
 */
@Service
public final class RoleSecurityChecker implements ISecurityChecker {

    @Autowired
    private UserRoleService        userRoleService;

    @Autowired
    private SecurityRoleSettingDAO securityRoleSettingDAO;

    @Autowired
    private UserService            userService;

    RoleSecurityChecker() {
    }

    public boolean hasPermission(SecurityMetaData securityMetaData) {
        ProfileAction action = securityMetaData.getAction();// 要请求的Action
        int userId = securityMetaData.getUserId(); // 当前的用户
        int ownerId = securityMetaData.getOwnerId();// 默认用户

        Set<Integer> userRoleIdSet;
        if (userId <= 0) {
            userRoleIdSet = new HashSet<Integer>();
            userRoleIdSet.add(ProfileBuildinRole.ANONYMOUS_USER.getId());
        } else {
            userRoleIdSet = userRoleService.getRoleIdsOfUser(userId);
            userRoleIdSet.add(ProfileBuildinRole.LOGINED_USER.getId());
        }

        // 获得某功能可以被访问的角色
        Set<Integer> accessibleRoleIdSet = securityRoleSettingDAO.queryRoleIdSetByActionId(action.getId());
        // 如果有交集，说明有权限
        return CollectionUtils.containsAny(userRoleIdSet, accessibleRoleIdSet);
    }
}
