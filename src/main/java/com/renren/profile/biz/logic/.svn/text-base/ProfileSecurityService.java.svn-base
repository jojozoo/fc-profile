package com.renren.profile.biz.logic;

import java.util.HashSet;
import java.util.Set;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.InvocationUtils;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.renren.profile.RenrenProfileConstants;
import com.renren.profile.biz.dao.SecurityRelationSettingDAO;
import com.renren.profile.biz.dao.SecurityRoleSettingDAO;
import com.renren.profile.constants.ProfileAction;
import com.renren.profile.constants.ProfileRelation;
import com.renren.profile.web.config.ProfileConfigHelper;
import com.renren.profile.web.config.ProfileConfigs;

@Service
public class ProfileSecurityService implements RenrenProfileConstants, ApplicationContextAware {

    public static volatile ProfileSecurityService instance;
    @Autowired
    private UserRoleService                       userRoleService;
    @Autowired
    private SecurityRoleSettingDAO                securityRoleSettingDAO;
    @Autowired
    private SecurityRelationSettingDAO            securityRelationSettingDAO;
    @Autowired
    private UserService                               userService;

    public static ProfileSecurityService getInstance() {
        return instance;
    }

    // XXX 本方法需要进一步优化
    public boolean hasPermission(ProfileAction action, int userId, int owner) {
        // 验证角色权限
        Set<Integer> userRoleIdSet = userRoleService.getRoleIdsOfUser(userId);
        // 获得某功能可以被访问的角色
        Set<Integer> accessibleRoleIdSet = securityRoleSettingDAO.queryRoleIdSetByActionId(action.getId());
        // 如果有交集，说明有权限
        if (CollectionUtils.containsAny(userRoleIdSet, accessibleRoleIdSet)) {
            return true;
        }

        // 验证用户资源权限
        if (action.isUserResource()) {
            if (owner <= 0) { // 没有owner信息
                Invocation inv = InvocationUtils.getCurrentThreadInvocation();
                throw new IllegalStateException("ProfileSecurityInterceptor|hasPermission|" + inv.getControllerClass().getName() + "|"
                        + inv.getMethod().getName() + "|" + action.name() + " need <owner> param");
            }
            if (userId == owner) { // 自己的
                return true;
            }

            // 判断上下级关系
            Set<Integer> relationIdSet = securityRelationSettingDAO.queryRelationIdSetByActionId(action.getId());
            if (CollectionUtils.isEmpty(relationIdSet)) {
                return false;
            }
            for (Integer relationId : relationIdSet) {
                ProfileRelation profileRelation = ProfileRelation.findById(relationId);
                if (profileRelation != null) {
                    switch (profileRelation) {// XXX 需要优化成“继承模式”
                        case SUPERIOR: // 上级领导，不包括直接领导
                        {
                            Set<Integer> leaderIdSet = new HashSet<Integer>();
                            Integer leaderId = userService.queryLeader(owner);// 直接领导
                            if (leaderId != null && leaderId > 0) {
                                $while: while (true) {
                                    if (leaderIdSet.contains(leaderId)) {// 防止环
                                        break $while;
                                    }
                                    if (leaderIdSet.size() > 1024) {// 防止太长
                                        break $while;
                                    }
                                    leaderIdSet.add(leaderId);
                                    leaderId = userService.queryLeader(leaderId);
                                    if (leaderId == null || leaderId <= 0) {
                                        break $while;
                                    } else {
                                        if (leaderId.equals(userId)) {// 有权限
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                            break;
                        case LEADER: // 直接领导
                        {
                            Integer leaderId = userService.queryLeader(owner);// 直接领导
                            if (ObjectUtils.equals(leaderId, userId)) {
                                return true;
                            }
                        }
                            break;
                        case SAME_DEPARTMENT: // 同部门
                        {
                            Integer userDepartment = userService.queryDepartment(userId);
                            Integer ownerDepartment = userService.queryDepartment(owner);
                            if (ObjectUtils.equals(userDepartment, ownerDepartment)) {
                                return true;
                            }
                        }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return false;
    }

    public boolean isRoot(int userId) {
        Set<Integer> rootUserIds = ProfileConfigHelper.ins().getValue(ProfileConfigs.IntegerSetConfigView.ROOT_USERS);
        return rootUserIds.contains(userId);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        instance = this;
    }

}
