package com.renren.profile.web.taglibs;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.renren.profile.util.text.html.HtmlFilterUtils;
import com.renren.profile.util.text.html.HtmlFilterUtils.FilterType;
import com.renren.profile.util.text.html.HtmlFilterUtils.HtmlCorrectException;

/**
 * HTML归整化
 * 
 * @author "黄兴海  xinghai.huang@xiaonei.opi.com"
 * @date 2010-11-25 下午04:37:36
 */
public class HtmlTextTag extends SimpleTagSupport {
    private static final Logger LOGGER      = Logger.getLogger(HtmlTextTag.class);
    private boolean             newline     = true;                               // 自动将换行符转为br

    private String              filter      = FilterText;                         // 是否过滤
    private static final String FilterAll   = "all";                              // 过滤掉所有的Tag，只保留文本
    private static final String FilterNone  = "none";                             // 不过滤（默认）
    private static final String FilterHtml  = "html";                             // 普通html过滤方法
    private static final String FilterText  = "text";                             // 转义所有字符
    private static final String FilterStyle = "style";                            // 去掉style样式


    @Override
    public void doTag() throws JspException, IOException {
        final JspFragment jsf = this.getJspBody();
        if (jsf == null) {
            return;
        }
        final JspContext context = this.getJspContext();
        final StringWriter writer = new StringWriter();
        jsf.invoke(writer);
        final String content = writer.toString();
        if (StringUtils.isBlank(content)) {
            return;
        }
        String result = content;
        if (this.newline) {
            result = result.replaceAll("\r?\n", "<br/>");
        }
        FilterType filterType = null;

        // filter
        if (FilterNone.equalsIgnoreCase(this.filter)) { //
            filterType = FilterType.DO_NOTHING;
        } else if (FilterAll.equalsIgnoreCase(this.filter)) { //
            filterType = FilterType.REMOVE_ALL_TAGS;
        } else if (FilterHtml.equalsIgnoreCase(this.filter)) { //
            filterType = FilterType.CORRECT_NORMAL;
        } else if (FilterText.equalsIgnoreCase(this.filter)) {
            filterType = FilterType.ESCAPE_ALL;
        } else {
            throw new JspException("fileType为NULL。请确认传入的type参数是(all,none,blog,html,text)之一。");
        }
        try {
            result = HtmlFilterUtils.filter(filterType, result);
        } catch (HtmlCorrectException e) {
            LOGGER.error("htmlTextTag|filter", e);
            try {
                result = HtmlFilterUtils.onlyKeepContent(result);
            } catch (HtmlCorrectException e1) {
                LOGGER.error("htmlTextTag|filter|exceptionCatch", e);
                result = HtmlFilterUtils.escape(result);
            }
        }
        context.getOut().write(result);
    }

    public boolean isNewline() {
        return this.newline;
    }

    public void setNewline(final boolean newline) {
        this.newline = newline;
    }

    public String getFilter() {
        return this.filter;
    }

    public void setFilter(final String filter) {
        this.filter = filter;
    }

    /*
     * public static void main(final String[] args) { for (final Field f :
     * HtmlTextTag.class.getDeclaredFields()) { if (f.getDeclaringClass() ==
     * HtmlTextTag.class) { if (f.getModifiers() != Modifier.PUBLIC) { final
     * String name = f.getName(); System.out.println("		<attribute>\r\n" +
     * "			<name>" + name + "</name>\r\n" + "			<required>true</required>\r\n" +
     * "			<rtexprvalue>true</rtexprvalue>\r\n" + "		</attribute>"); } } } }
     */
}
