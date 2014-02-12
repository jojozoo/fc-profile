package com.orientalcomics.profile.biz.security;


public interface ISecurityChecker {
    boolean hasPermission(SecurityMetaData securityMetaData);
}
