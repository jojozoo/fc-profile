package com.renren.profile;

public interface RenrenProfileAjaxCodes {
    /**
     * 一般，可以携带Data数据
     */
    int NORMAL       = 0;
    /**
     * 需要登录：将Popup一个对话框，确认后跳转到登录页
     */
    int NEED_LOGIN   = 1; //
    /**
     * 没有权限：将Popup一个对话框提示用户
     */
    int NO_AUTH      = 2; //
    /**
     * 错误
     */
    int ERROR        = 3; //
    /**
     * 页面过期：将Popup一个对话框，确认后刷新页面
     */
    int EXPIRED_PAGE = 4; //
    /**
     * 页面重定向：直接重定向页面至URL
     */
    int REDIRECT     = 5; //
    /**
     * 警告用户：弹出对话框
     */
    int ALERT        = 6; //
}
