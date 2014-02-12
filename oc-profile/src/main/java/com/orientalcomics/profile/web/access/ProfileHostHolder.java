package com.orientalcomics.profile.web.access;

import com.orientalcomics.profile.biz.model.User;


public interface ProfileHostHolder {
    /**
     * 返回当前访问者，如果没有登录的话返回null
     * 
     * @return
     */
    public User getUser();

    /**
     * 
     * @param user
     */
    public void setUser(User user);
}
