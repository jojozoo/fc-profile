package com.orientalcomics.profile.web.taglibs;

import javax.servlet.jsp.JspException;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.InvocationUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.orientalcomics.profile.biz.logic.ProfileSecurityService;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.constants.ProfileAction;
import com.orientalcomics.profile.web.access.ProfileSecurityManager;

public class SecurityFunction {

    public static boolean isRoot(int userId) {
        return ProfileSecurityManager.isRoot(userId);
    }

    public static boolean access(Object actions) throws JspException {
        return doAccess(actions, 0, 0);
    }

    public static boolean accessForUser(Object actions, int user) throws JspException {
        return doAccess(actions, user, 0);
    }

    public static boolean accessForUserAndOwner(Object actions, int user, int owner) throws JspException {
        return doAccess(actions, user, owner);
    }

    static boolean doAccess(Object actions, int user, int owner) throws JspException {
        if (actions == null) {
            return false;
        }
        Invocation inv = InvocationUtils.getCurrentThreadInvocation();
        if (user <= 0) {
            if (inv != null) {
                User userBean = (User) inv.getModel("_user");// 见
                                                             // LoginInterceptor
                if (userBean != null) {
                    user = userBean.getId();
                }
            }
        }
        if (user <= 0) {
            throw new JspException("SecurityTag|unkown user|actions|" + actions + "|user|" + user);
        }
        if (owner <= 0) {
            if (inv != null) {
                User ownerBean = (User) inv.getModel("_owner");// 见
                                                               // LoginInterceptor
                if (ownerBean != null) {
                    owner = ownerBean.getId();
                }
            }
        }
        if (actions instanceof String) {
            String[] actionArray = StringUtils.split((String) actions, ',');
            for (String action : actionArray) {
                if (canAccess(action, user, owner)) {
                    return true;
                }
            }
            return false;
        } else if (actions instanceof Number) {
            int actionId = ((Number) actions).intValue();
            ProfileAction profileAction = ProfileAction.findById(actionId);
            if (profileAction == null) {
                throw new JspException("Security|unkown action|action|" + actionId);
            }
            return ProfileSecurityService.getInstance().hasPermission(profileAction, user, owner);
        } else if (actions instanceof ProfileAction) {
            ProfileAction profileAction = (ProfileAction) actions;
            if (profileAction == null) {
                throw new JspException("Security|unkown action|action|" + actions);
            }
            
            return ProfileSecurityService.getInstance().hasPermission(profileAction, user, owner);
        } else {
            throw new JspException("Security|hasPermission|first param must be action's id or name or action");
        }
    }

    static boolean canAccess(String action, int user, int owner) throws JspException {
        action = StringUtils.trimToNull(action);
        if (action == null) {
            return false;
        }

        if ("root".equalsIgnoreCase(action)) {
            return ProfileSecurityManager.isRoot(user);
        }

        ProfileAction profileAction = null;

        if (NumberUtils.isNumber(action)) {
            int actionId = NumberUtils.toInt(action, 0);
            profileAction = ProfileAction.findById(actionId);
        } else {
            profileAction = ProfileAction.findByName(action);
        }

        if (profileAction == null) {
            throw new JspException("Security|unkown action|action|" + action);
        }

        return ProfileSecurityManager.hasPermission(profileAction, user, owner);
    }
}
