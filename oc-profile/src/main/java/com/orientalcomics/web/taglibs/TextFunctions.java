package com.orientalcomics.web.taglibs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONValue;

import com.orientalcomics.profile.util.text.html.HtmlFilterUtils;
import com.orientalcomics.profile.util.text.html.HtmlFilterUtils.HtmlCorrectException;

/**
 * 
 * @author DanyZhang  
 *
 */

public class TextFunctions {
    public static String escapeHtml(String html) {
        return HtmlFilterUtils.escape(html);
    }

    public static String escapeJavaScript(String js) {
        return StringEscapeUtils.escapeJavaScript(js);
    }

    public static String escapeXml(String js) {
        return StringEscapeUtils.escapeXml(js);
    }

    public static String urlEncode(String s){
        if(s == null){
            return null;
        }
        try {
            return URLEncoder.encode(s,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            return s;
        }
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
    
    public static String nl2Replace(String html) {
        return html == null ? null : html.replaceAll("<br>", "\n");
    }

    public static String json(Object value) {
        return JSONValue.toJSONString(value);
    }
    
    public static void main(String[] args) {
    	String str = "2、水电费\n1、水电费\n风格和法国\n过好几个号";
    	System.out.println(TextFunctions.nl2br(str));
    }
    
}
