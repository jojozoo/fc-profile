package com.orientalcomics.profile.web.taglibs;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;

public class EmptyTag extends SimpleTagSupport {

    private String def = "";

    @Override
    public void doTag() throws JspException, IOException {
        StringWriter sw = new StringWriter();
        getJspBody().invoke(sw);
        String content = StringUtils.trimToNull(sw.toString());
        getJspContext().getOut().write(content == null ? def : content);
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = StringUtils.trimToEmpty(def);
    }

}
