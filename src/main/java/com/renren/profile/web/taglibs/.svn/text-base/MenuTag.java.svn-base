package com.renren.profile.web.taglibs;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

public class MenuTag extends SimpleTagSupport {
    private String  prefix;
    private boolean sub = false; // 子菜单
    private String  htmlClass;  // class

    @Override
    public void doTag() throws JspException, IOException {
        String navMenu = StringUtils.lowerCase(StringUtils.trimToNull(ObjectUtils.toString(getJspContext().getAttribute("nav_menu"))));
        if (prefix != null && navMenu != null) {
            if (navMenu.startsWith(prefix)) {
                if (htmlClass != null) {
                    htmlClass = "selected " + htmlClass;
                } else {
                    htmlClass = "selected";
                }
            }
        }

        JspWriter out = getJspContext().getOut();
        out.write(sub ? "<dd" : "<dt");
        if (htmlClass != null) {
            out.write(" class=\"" + htmlClass + "\"");
        }
        out.write(">");
        getJspBody().invoke(out);
        out.write(sub ? "</dd>" : "</dt>");
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = StringUtils.lowerCase(StringUtils.trimToNull(prefix));
    }

    public boolean isSub() {
        return sub;
    }

    public void setSub(boolean sub) {
        this.sub = sub;
    }

    public String getHtmlClass() {
        return htmlClass;
    }

    public void setHtmlClass(String htmlClass) {
        this.htmlClass = StringUtils.trimToNull(htmlClass);
    }

    public static void main(final String[] args) {
        for (final Field f : MenuTag.class.getDeclaredFields()) {
            if (f.getDeclaringClass() == MenuTag.class) {
                if (f.getModifiers() != Modifier.PUBLIC) {
                    final String name = f.getName();
                    System.out.println("      <attribute>\r\n" +
                            "            <name>" + name + "</name>\r\n" + "          <required>true</required>\r\n" +
                            "            <rtexprvalue>true</rtexprvalue>\r\n" + "        </attribute>");
                }
            }
        }
    }

}
