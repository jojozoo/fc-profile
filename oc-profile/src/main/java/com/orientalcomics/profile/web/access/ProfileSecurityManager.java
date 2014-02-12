package com.orientalcomics.profile.web.access;

import com.orientalcomics.profile.biz.logic.ProfileSecurityService;
import com.orientalcomics.profile.constants.ProfileAction;


public class ProfileSecurityManager {

    public static boolean isRoot(int userId) {
        return ProfileSecurityService.getInstance().isRoot(userId);
    }

    public static boolean hasPermission(ProfileAction action, int userId, int owner) {
        return ProfileSecurityService.getInstance().hasPermission(action, userId, owner);
    }
}
