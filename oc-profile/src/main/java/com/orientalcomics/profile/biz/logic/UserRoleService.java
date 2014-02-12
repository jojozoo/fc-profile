package com.orientalcomics.profile.biz.logic;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.SetUtils;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.dao.UserRoleDAO;
import com.orientalcomics.profile.biz.model.UserRole;
import com.orientalcomics.profile.util.common.Collections0;
import com.orientalcomics.profile.util.time.TimeUtils;


@Service
public class UserRoleService implements OcProfileConstants {
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
        return result;
    }

}
