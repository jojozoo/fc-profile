package com.renren.profile.web.taglibs;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.InvocationUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.renren.profile.RenrenProfileConstants;
import com.renren.profile.biz.model.User;
import com.renren.profile.constants.ProfileAction;
import com.renren.profile.web.access.ProfileSecurityManager;

public class SecurityTag extends SimpleTagSupport implements RenrenProfileConstants {
    private String action; // action的Id或者name或者"ROOT"
    private int    user;
    private int    owner;

    @Override
    public void doTag() throws JspException, IOException {
        ProfileAction profileAction = null;
        boolean mustBeRootUser = false;
        if (NumberUtils.isNumber(action)) {
            int actionId = NumberUtils.toInt(action, 0);
            profileAction = ProfileAction.findById(actionId);
        } else {
            if ("root".equalsIgnoreCase(action)) {
                mustBeRootUser = true;
            } else {
                profileAction = ProfileAction.findByName(action);
            }
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
            throw new JspException("SecurityTag|unkown user|action|" + action + "|user|" + user);
        }

        boolean canAccess = false;
        if (mustBeRootUser) {
            canAccess = ProfileSecurityManager.isRoot(user);
        } else {
            if (profileAction == null) {
                throw new JspException("SecurityTag|unkown action|action|" + action);
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
            canAccess = ProfileSecurityManager.hasPermission(profileAction, user, owner);
        }
        if (canAccess) {
            getJspBody().invoke(getJspContext().getOut());
        }
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = StringUtils.lowerCase(StringUtils.trimToNull(action));
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public static void main(final String[] args) {
        for (final Field f : SecurityTag.class.getDeclaredFields()) {
            if (f.getDeclaringClass() == SecurityTag.class) {
                if (f.getModifiers() != Modifier.PUBLIC) {
                    final String name = f.getName();
                    System.out.println("        <attribute>\r\n" + "            <name>" + name + "</name>\r\n" + "          <required>true</required>\r\n"
                            + "         <rtexprvalue>true</rtexprvalue>\r\n" + "        </attribute>");
                }
            }
        }
    }
}
