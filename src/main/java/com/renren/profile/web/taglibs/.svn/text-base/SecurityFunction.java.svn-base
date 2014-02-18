package com.renren.profile.web.taglibs;

import javax.servlet.jsp.JspException;

import com.renren.profile.biz.logic.ProfileSecurityService;
import com.renren.profile.constants.ProfileAction;
import com.renren.profile.web.access.ProfileSecurityManager;

public class SecurityFunction {

    public static boolean isRoot(int userId) {
        return ProfileSecurityManager.isRoot(userId);
    }

    public static boolean hasPermission(int action, int user, int owner) throws JspException {
        ProfileAction profileAction = ProfileAction.findById(action);
        if (profileAction == null) {
            throw new JspException("SecurityTag|unkown action|action|" + action);
        }
        return ProfileSecurityService.getInstance().hasPermission(profileAction, user, owner);
    }
}
