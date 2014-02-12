package com.orientalcomics.profile.util.text.html;

import java.io.StringWriter;
import java.net.URL;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

import com.orientalcomics.profile.exception.ProfileException;


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
        StringWriter writer = new StringWriter((int) (html.length() * 1.5));
        escapeHtml(writer, html);
        return writer.toString();
    }

    private static void escapeHtml(StringWriter writer, String str) {
        int len = str.length();
        char ch;
        try {
            for (int i = 0; i < len; i++) {
                ch = str.charAt(i);
                switch (ch) {
                // 过滤一些恶意代码,这些字符会让utf-8的页面混乱，倒排
                    case '\'':
                        writer.append("&#39;");
                        break;
                    case '\"':
                        writer.append("&quot;");
                        break;
                    case '\\':
                        writer.append("\\&shy;");
                        break;
                    case '>':
                        writer.append("&gt;");
                        break;
                    case '<':
                        writer.append("&lt;");
                        break;
                    case '&': {// 不转义&#<number>;以及&<alpha>;
                        int semicolonPos = i + 1;
                        int end = Math.min(len, i + 10);
                        boolean findSemicolon = false;
                        while (semicolonPos < end) {
                            char _c = str.charAt(semicolonPos);
                            if (_c == '&') {
                                findSemicolon = false;
                                break;
                            }
                            if (_c == ';') {
                                findSemicolon = true;
                                break;
                            }
                            semicolonPos++;
                        }
                        if (!findSemicolon) {
                            writer.append("&amp;");
                        } else {// 找到了分号，需要对特殊的UTF控制符进行替换
                            String entityContent = str.substring(i + 1, semicolonPos);
                            int entityValue = -1;
                            int entityContentLen = entityContent.length();
                            if (entityContentLen > 0) {
                                if (entityContent.charAt(0) == '#') {
                                    if (entityContentLen > 1) {
                                        char isHexChar = entityContent.charAt(1);
                                        try {
                                            switch (isHexChar) {
                                                case 'X':
                                                case 'x': {
                                                    entityValue = Integer.parseInt(entityContent.substring(2), 16);
                                                    break;
                                                }
                                                default: {
                                                    entityValue = Integer.parseInt(entityContent.substring(1), 10);
                                                }
                                            }
                                            if (entityValue > 0xFFFF) {
                                                entityValue = -1;
                                            }
                                        } catch (NumberFormatException e) {
                                            entityValue = -1;
                                        }
                                    }
                                } else {
                                    entityValue = Entities.HTML40.entityValue(entityContent);
                                }
                            }
                            if (entityValue <= 0) {// 不是有效的转义
                                writer.append("&amp;");
                            } else {
                                if (entityValue - 1 < UCCFMap.length) {
                                    writer.append("&#").append(String.valueOf(UCCFMap[entityValue - 1])).append(";");
                                } else {
                                    writer.append(str.substring(i, semicolonPos + 1));
                                }
                                i = semicolonPos;// 跳转到下一个
                            }
                        }
                    }
                        break;
                    // 以下两个是为了转换/*xxxx*/这种注释的，这种注释会让代码乱掉
                    case '/':
                        if ((i + 1) < str.length() && str.charAt(i + 1) == '*') {
                            writer.append("&#47;&#42;");
                            ++i;
                            break;
                        }
                        writer.append(ch);
                        break;
                    case '*':
                        if ((i + 1) < str.length() && str.charAt(i + 1) == '/') {
                            writer.append("&#42;&#47;");
                            ++i;
                            break;
                        }
                        writer.append(ch);
                        break;
                    default:
                        // 防止几个从左到右的字符影响utf-8页面
                        if (Character.getDirectionality(ch) == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC
                                || Character.getDirectionality(ch) == Character.DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING
                                || Character.getDirectionality(ch) == Character.DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE) {
                            // do nothing
                        } else {
                            writer.append(ch);
                        }
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int[] UCCFMap = new int[] { 9486, 9490, 9492, 9498, 9474, 9472, 8226, 9688, 9, 10, 11, 12, 13, 9836, 9788, 8224, 9668, 8616, 8252, 182,
                                 9524, 9516, 9508, 8593, 9500, 8594, 8592, 28, 29, 30 };

}
