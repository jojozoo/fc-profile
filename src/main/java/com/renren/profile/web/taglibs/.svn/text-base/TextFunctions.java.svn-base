package com.renren.profile.web.taglibs;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONValue;

import com.renren.profile.util.text.html.HtmlFilterUtils;
import com.renren.profile.util.text.html.HtmlFilterUtils.HtmlCorrectException;

public class TextFunctions {
    public static String escapeHtml(String html) {
        return StringEscapeUtils.escapeHtml(html);
    }

    public static String escapeJavaScript(String js) {
        return StringEscapeUtils.escapeJavaScript(js);
    }

    public static String escapeXml(String js) {
        return StringEscapeUtils.escapeXml(js);
    }

    public static String correctHtml(String html) {
        try {
            return HtmlFilterUtils.correct(html);
        } catch (HtmlCorrectException e) {
            return escapeHtml(html);
        }
    }

    public static String nl2br(String html) {
        return html == null ? null : html.replaceAll("\r?\n", "<br/>");
    }

    public static String json(Object value) {
        return JSONValue.toJSONString(value);
    }

}
