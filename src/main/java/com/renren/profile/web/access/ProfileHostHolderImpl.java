package com.renren.profile.web.access;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.InvocationLocal;

import org.springframework.beans.factory.annotation.Autowired;

import com.renren.profile.biz.model.User;

public class ProfileHostHolderImpl implements ProfileHostHolder {

    private static final String KEY_CUR_USER = "__current_user__";

    @Autowired
    InvocationLocal             inv;

    
    public ProfileHostHolderImpl(){
    	System.out.println("======== inited");
    }
    
    @Override
    public User getUser() {
        Invocation inv = this.inv.getCurrent(false);
        if (inv != null) {
            return (User) inv.getRequest().getAttribute(KEY_CUR_USER);
        } else {
            return null;
        }
    }

    @Override
    public void setUser(User user) {
        inv.getRequest().setAttribute(KEY_CUR_USER, user);
    }
}
