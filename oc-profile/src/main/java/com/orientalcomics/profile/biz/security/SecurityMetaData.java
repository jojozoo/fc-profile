package com.orientalcomics.profile.biz.security;

import com.orientalcomics.profile.constants.ProfileAction;


public class SecurityMetaData {
    private ProfileAction action;
    private int           userId;
    private int           ownerId;

    public ProfileAction getAction() {
        return action;
    }

    public void setAction(ProfileAction action) {
        this.action = action;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

}
