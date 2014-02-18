package com.renren.profile.biz.logic;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.SetUtils;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.profile.RenrenProfileConstants;
import com.renren.profile.biz.dao.UserRoleDAO;
import com.renren.profile.biz.model.UserRole;
import com.renren.profile.constants.ProfileBuildinRole;
import com.renren.profile.util.Collections0;
import com.renren.profile.util.time.TimeUtils;

@Service
public class UserRoleService implements RenrenProfileConstants {
    @Autowired
    UserRoleDAO userRoleDAO;

    public synchronized void updateRolesOfUser(int userId, Set<Integer> roles) {
        Set<Integer> oldRoles = (Set<Integer>) ObjectUtils.defaultIfNull(userRoleDAO.queryRoleIdSetByUser(userId), SetUtils.EMPTY_SET);
        Set<Integer> newRoles = (Set<Integer>) ObjectUtils.defaultIfNull(roles, SetUtils.EMPTY_SET);
        Set<Integer> needAddedRoles = Collections0.subtract(newRoles, oldRoles);
        Set<Integer> needDeletedRoles = Collections0.subtract(oldRoles, newRoles);
        if (!needDeletedRoles.isEmpty()) {
            userRoleDAO.removeRoleIdsOfUser(userId, needDeletedRoles);
        }
        if (!needAddedRoles.isEmpty()) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setEditorId(1);
            userRole.setEditorName("系统");
            userRole.setEditTime(TimeUtils.FetchTime.now());
            for (Integer roleId : needAddedRoles) {
                userRole.setRoleId(roleId);
                userRoleDAO.save(userRole);
            }
        }
    }

    public Set<Integer> getRoleIdsOfUser(int userId) {
        Set<Integer> result = userRoleDAO.queryRoleIdSetByUser(userId);
        if (result == null) {
            result = new HashSet<Integer>();
        }
        // 加上内置的权限
        result.add(ProfileBuildinRole.LOGINED_USER.getId()); // 登录用户
        return result;
    }
}
