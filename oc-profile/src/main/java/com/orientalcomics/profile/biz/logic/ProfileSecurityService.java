package com.orientalcomics.profile.biz.logic;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.security.ISecurityChecker;
import com.orientalcomics.profile.biz.security.RelationSecurityChecker;
import com.orientalcomics.profile.biz.security.RoleSecurityChecker;
import com.orientalcomics.profile.biz.security.SecurityMetaData;
import com.orientalcomics.profile.constants.ProfileAction;

@Service
public class ProfileSecurityService implements OcProfileConstants, ApplicationContextAware {

    public static volatile ProfileSecurityService instance;

    public static ProfileSecurityService getInstance() {
        return instance;
    }

    @Autowired
    RoleSecurityChecker                 roleSecurityChecker;

    @Autowired
    RelationSecurityChecker             relationSecurityChecker;
    
    @Autowired
    UserWhiteListService                userWhiteListService;

    private volatile ISecurityChecker[] buildinCheckers = new ISecurityChecker[0];

    private ProfileSecurityService() {

    }

    @PostConstruct
    void init() {
        buildinCheckers = new ISecurityChecker[] { roleSecurityChecker, relationSecurityChecker };
    }

    public boolean hasPermission(ProfileAction action, int userId, int ownerId) {
        SecurityMetaData securityMetaData = new SecurityMetaData();
        securityMetaData.setAction(action);
        securityMetaData.setUserId(userId);
        securityMetaData.setOwnerId(ownerId);
        
        if (action.toString().equals("VIEW_WEEKLY_REPORT") && userWhiteListService.getWhiteListUserViewWeeklyReport(userId, ownerId)) {
        	return true;
        }
        	
        for (ISecurityChecker checker : buildinCheckers) {
            if (checker.hasPermission(securityMetaData)) {
                return true;
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
    
    public static void main(String[] args) {
    	System.out.println(ProfileAction.VIEW_WEEKLY_REPORT.toString());
    }

}
