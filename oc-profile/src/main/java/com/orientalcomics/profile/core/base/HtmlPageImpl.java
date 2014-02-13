package com.orientalcomics.profile.core.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;

import com.orientalcomics.profile.OcProfileAjaxCodes;


public class HtmlPageImpl implements HtmlPage, OcProfileAjaxCodes {
    private Map<String, List<String>> errors        = new HashMap<String, List<String>>();
    private Map<String, List<String>> warnings      = new HashMap<String, List<String>>();
    private Map<String, List<String>> infos         = new HashMap<String, List<String>>();
    private int                       code          = NORMAL;
    private String                    alert         = null;
    private JSONObject                data          = null;
    private String                    url           = null;
    private FormValidator             formValidator = new FormValidator(this);

    HtmlPageImpl() {

    }

    @Override
    public FormValidator formValidator() {
        return formValidator;
    }

    @Override
    public HtmlPage error(String message) {
        error(null, message);
        return this;
    }

    @Override
    public HtmlPage error(String key, String message) {
        message(errors, key, message);
        code = ERROR;
        return this;
    }

    @Override
    public HtmlPage warning(String message) {
        warning(null, message);
        return this;
    }

    @Override
    public HtmlPage warning(String key, String message) {
        message(warnings, key, message);
        return this;
    }

    @Override
    public HtmlPage info(String message) {
        info(null, message);
        return this;
    }

    @Override
    public HtmlPage info(String key, String message) {
        message(infos, key, message);
        return this;
    }

    @Override
    public HtmlPage alert(String message) {
        code(ALERT);
        this.alert = message;
        return this;
    }

    @Override
    public HtmlPage success() {
        code(NORMAL);
        return this;
    }

    @Override
    public HtmlPage expired() {
        code(EXPIRED_PAGE);
        return this;
    }

    public HtmlPage code(int code) {
        this.code = code;
        return this;
    }

    @Override
    public HtmlPage data(JSONObject data) {
        this.data = data;
        return this;
    }

    public int getCode() {
        return code;
    }

    @Override
    public HtmlPage redirect(String url) {
        this.code = REDIRECT;
        this.url = url;
        return this;
    }

    public JSONObject getData() {
        return data;
    }

    public String getUrl() {
        return url;
    }

    public String getAlert() {
        return alert;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public Map<String, List<String>> getWarnings() {
        return warnings;
    }

    public Map<String, List<String>> getInfos() {
        return infos;
    }

    private void message(Map<String, List<String>> map, String key, String message) {
        String mapKey = StringUtils.trimToEmpty(key);
        List<String> list = map.get(mapKey);
        if (list == null) {
            list = new ArrayList<String>();
            map.put(mapKey, list);
        }
        list.add(message);
    }
    
}
