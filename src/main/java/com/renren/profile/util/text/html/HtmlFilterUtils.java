package com.renren.profile.util.text.html;

import java.net.URL;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.renren.profile.util.sys.ProfileException;

public class HtmlFilterUtils {
    public static enum FilterType {
        DO_NOTHING, // 什么也不做
        REMOVE_ALL_TAGS, // 去除所有Tag（保留可见Tag下面的内容）
        ESCAPE_ALL, // 转义所有字符
        CORRECT_NORMAL // 去除非法的TAG
        ;
    }

    /**
     * 过滤HTML文本。
     * 
     * @param type
     *            过滤类型
     * @param input
     *            待过滤文本
     * @param valIfError
     *            当过滤类型为null或者过滤过程中发生错误时，返回的文本
     * @return 过滤后的文本(不会为<code>null</code>)
     */
    public static String filter(FilterType type, String input, String valIfError) {
        if (type == null) {
            return valIfError == null ? StringUtils.EMPTY : valIfError;
        }
        try {
            return filter(type, input);
        } catch (HtmlCorrectException e) {
            return valIfError == null ? StringUtils.EMPTY : valIfError;
        }
    }

    /**
     * 过滤HTML文本。
     * 
     * @param type
     *            过滤类型。不能为<code>null</code>。
     * @param input
     *            待过滤文本
     * @return 过滤之后的文本(不会为<code>null</code>)
     * @throws HtmlCorrectException
     *             HTML过滤过程中发生错误
     * @throws NullArgumentException
     *             <code>type</code>为<code>null</code>
     */
    public static String filter(FilterType type, String input) throws HtmlCorrectException, NullArgumentException {
        if (type == null) {
            throw new NullArgumentException("<type> cannot be null");
        }
        String result = null;
        try {
            switch (type) {
                case DO_NOTHING:
                    result = input;
                    break;
                case REMOVE_ALL_TAGS:
                    result = HtmlFilterUtils.onlyKeepContent(input);
                    break;
                case ESCAPE_ALL:
                    result = HtmlFilterUtils.escape(input);
                    break;
                case CORRECT_NORMAL:
                    result = HtmlFilterUtils.correct(input);
                    break;
                default:
                    break;
            }
        } catch (HtmlCorrectException e) {
            throw new HtmlCorrectException("Cannot filter content|type|" + type.name(), e);
        }
        return result == null ? StringUtils.EMPTY : result;
    }

    public static class HtmlCorrectException extends ProfileException {

        /**
         * 
         */
        private static final long serialVersionUID = 6029040065081288350L;

        public HtmlCorrectException() {
            super();
        }

        public HtmlCorrectException(String message, Throwable cause) {
            super(message, cause);
        }

        public HtmlCorrectException(String message) {
            super(message);
        }

        public HtmlCorrectException(Throwable cause) {
            super(cause);
        }

    }

    private static class HtmlCorrectorLoader {
        private static final HtmlCorrector INSTANCE;
        static {
            HtmlCorrectRule rule = null;
            try {
                URL url = HtmlCorrectRule.class.getResource("resources/html-corrector.xml");
                rule = HtmlCorrectRule.getInstance(url);
            } catch (ProfileException e) {
                e.printStackTrace();
            }
            INSTANCE = new HtmlCorrector(rule);
        }

        static HtmlCorrector getHtmlCorrector() {
            return INSTANCE;
        }
    }

    private static class HtmlCorrectorOnlyKeepContentLoader {
        private static final HtmlCorrector INSTANCE;
        static {
            HtmlCorrectRule rule = null;
            try {
                URL url = HtmlCorrectRule.class.getResource("resources/html-corrector-only-keep-content.xml");
                rule = HtmlCorrectRule.getInstance(url);
            } catch (ProfileException e) {
                e.printStackTrace();
            }
            INSTANCE = new HtmlCorrector(rule);
        }

        static HtmlCorrector getHtmlCorrectorOnlyKeepContent() {
            return INSTANCE;
        }
    }

    public static String onlyKeepContent(String html) throws HtmlCorrectException {
        if (StringUtils.isBlank(html)) {
            return html;
        }
        String result = null;
        try {
            result = HtmlCorrectorOnlyKeepContentLoader.getHtmlCorrectorOnlyKeepContent().scan(html).getCleanHTML();
        } catch (ProfileException e) {
            throw new HtmlCorrectException("Cannot correct Html|" + html, e);
        }
        if (result == null) {
            result = "";
        }
        return result;
    }

    /**
     * 取出HTML中非法的tag
     * 
     * @param html
     * @return
     * @throws HtmlCorrectException
     */
    public static String correct(String html) throws HtmlCorrectException {
        if (StringUtils.isBlank(html)) {
            return html;
        }
        String result = null;
        try {
            result = HtmlCorrectorLoader.getHtmlCorrector().scan(html).getCleanHTML();
        } catch (ProfileException e) {
            throw new HtmlCorrectException("Cannot correct Html|" + html, e);
        }
        if (result == null) {
            result = "";
        }
        return result;
    }

    public static String escape(String html) {
        if (StringUtils.isBlank(html)) {
            return html;
        }
        return StringEscapeUtils.escapeHtml(html);
    }

}
