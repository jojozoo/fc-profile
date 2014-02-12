package com.orientalcomics.profile.biz.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.orientalcomics.profile.biz.dao.SecurityRelationSettingDAO;
import com.orientalcomics.profile.biz.logic.UserService;
import com.orientalcomics.profile.constants.ProfileAction;
import com.orientalcomics.profile.constants.ProfileRelation;


/**
 * 角色验证
 * 
 * @author 黄兴海(xinghai.huang)
 * @date 2012-3-28 上午11:54:06
 */
@Service
public final class RelationSecurityChecker implements ISecurityChecker {

    @Autowired
    private SecurityRelationSettingDAO securityRelationSettingDAO;

    @Autowired
    private UserService                userService;

    RelationSecurityChecker() {
    }

    public boolean hasPermission(SecurityMetaData securityMetaData) {
        ProfileAction action = securityMetaData.getAction();// 要请求的Action
        int userId = securityMetaData.getUserId(); // 当前的用户
        int ownerId = securityMetaData.getOwnerId();// 默认用户

        if (userId <= 0 || !action.isResources()) {// 不是资源，则返回false
            return false;
        }

        if (ownerId <= 0) {// 没有请求别人的资源
            ownerId = userId;// 那就是请求自己的资源
        }
        // 判断上下级关系
        Set<Integer> accessableRelationIdSet = getRelationSet(userId, ownerId);
        if (CollectionUtils.isEmpty(accessableRelationIdSet)) {
            return false;
        }
        Set<Integer> relationIdSet = securityRelationSettingDAO.queryRelationIdSetByActionId(action.getId());
        return CollectionUtils.containsAny(accessableRelationIdSet, relationIdSet);
    }

    private Set<Integer> getRelationSet(int userId, int ownerId) {
        Set<Integer> result = new HashSet<Integer>();
        $: {
            if (ownerId <= 0 || userId == ownerId) {// is self
                result.add(ProfileRelation.SELF.getId());
                break $;
            }
            // 判断直属上司
            Integer leaderId = userService.queryLeader(ownerId);// 直接领导
            if (leaderId == null || leaderId <= 0) {
                break $;
            }
            if (leaderId.equals(userId)) {
                result.add(ProfileRelation.LEADER.getId());// 直属上司
                break $;
            }
            // 判断上级（非直属上司）
            Set<Integer> leaderIdSet = new HashSet<Integer>();
            $while: while (true) {
                if (leaderIdSet.contains(leaderId)) {// 防止环
                    break $while;
                }
                if (leaderIdSet.size() > 1024) {// 防止太长
                    break $while;
                }
                leaderIdSet.add(leaderId);
                leaderId = userService.queryLeader(leaderId);
                if (leaderId == null || leaderId <= 0) {// 找不到上级了
                    break $while;
                } else {
                    if (leaderId.equals(userId)) {
                        result.add(ProfileRelation.SUPERIOR.getId());// 直属上司
                        break $;
                    }
                }
            }
            // 判断是不是同部门的（不含直属上司以及上级）
            Integer userDepartment = userService.queryDepartment(userId);
            Integer ownerDepartment = userService.queryDepartment(ownerId);
            if (userDepartment != null && userDepartment.equals(ownerDepartment)) {
                result.add(ProfileRelation.SAME_DEPARTMENT.getId());//
                break $;
            }
            // ...
        }
        return result;
    }
}
