package com.orientalcomics.profile.web.taglibs;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;

import com.orientalcomics.profile.OcProfileConstants;

public class SecurityTag extends SimpleTagSupport implements OcProfileConstants {
    private String actions; // action的Id或者name或者"ROOT"
    private int    user;
    private int    owner;

    @Override
    public void doTag() throws JspException, IOException {
        if (this.actions == null) {
            return;
        }

        if (SecurityFunction.doAccess(actions, user, owner)) {
            getJspBody().invoke(getJspContext().getOut());
        }
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String action) {
        this.actions = StringUtils.lowerCase(StringUtils.trimToNull(action));
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
}
