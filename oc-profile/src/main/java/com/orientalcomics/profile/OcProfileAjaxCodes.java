package com.orientalcomics.profile; 
/** 
 * @author 张浩 E-mail:zhanghao@foundercomics.com 
 * @version 创建时间：2014年2月13日 下午3:14:07 
 * 类说明  ajax 常量
 */
public interface OcProfileAjaxCodes {
	/**
     * 一般，可以携带Data数据
     */
    int NORMAL          = 0;
    /**
     * 需要登录：将Popup一个对话框，确认后跳转到登录页
     */
    int NEED_LOGIN      = 1; //
    /**
     * 没有权限：将Popup一个对话框提示用户
     */
    int NO_AUTH         = 2; //
    /**
     * 错误
     */
    int ERROR           = 3; //
    /**
     * 页面过期：将Popup一个对话框，确认后刷新页面
     */
    int EXPIRED_PAGE    = 4; //
    /**
     * 页面重定向：直接重定向页面至{page.url}
     */
    int REDIRECT        = 5; //
    /**
     * 警告用户：弹出对话框，显示内容{page.alert}
     */
    int ALERT           = 6; //
    /**
     * 当前没有绩效考核
     */
    int NO_CURRENT_PERF = 7; //
}
 