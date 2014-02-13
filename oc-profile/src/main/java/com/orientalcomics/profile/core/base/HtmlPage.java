package com.orientalcomics.profile.core.base;

import org.json.simple.JSONObject;

public interface HtmlPage {
    String MODEL_KEY = "_htmlPage";

    FormValidator formValidator();

    HtmlPage error(String message);

    HtmlPage error(String key, String message);

    HtmlPage warning(String message);

    HtmlPage warning(String key, String message);

    HtmlPage info(String message);

    HtmlPage info(String key, String message);

    /**
     * 警告用户
     * 
     * @param message
     * @return
     */
    HtmlPage alert(String message);

    /**
     * 页面已经成功完成
     * 
     * @return
     */
    HtmlPage success();

    /**
     * 需要重定向至url
     * 
     * @param url
     * @return
     */
    HtmlPage redirect(String url);

    /**
     * 页面已经过期，需要重新刷新
     * 
     * @return
     */
    HtmlPage expired();

    /**
     * 当页面成功后，返回给前端的数据
     * 
     * @param data
     * @return
     */
    HtmlPage data(JSONObject data);
}