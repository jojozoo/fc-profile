package com.renren.profile.web.taglibs;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;



/**
 * 截字
 * 
 * @author xinghai.huang@xiaonei.opi.com
 * @date 2010-11-12 下午05:17:15
 */
public class SubStringTag extends SimpleTagSupport {

    private String  value;
    private int     fromIndex = 0;
    private int     len;
    private String  end       = "...";
    private boolean monospace = false; // 等宽字体

    @Override
    public void doTag() throws JspException, IOException {
        String result;
        value = StringUtils.trimToNull(value);
        if (null == value) {
            result = StringUtils.EMPTY;
        } else {
            result = StringUtils.substring(value, Math.max(0, fromIndex), Math.max(0, fromIndex)+len);
            if(Math.max(0, fromIndex)+len < this.value.length()){
            	result = result+this.end;
            }
        }
        super.getJspContext().getOut().write(result);
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public int getFromIndex() {
        return fromIndex;
    }

    public void setFromIndex(final int fromIndex) {
        this.fromIndex = fromIndex;
    }

    public int getLen() {
        return len;
    }

    public void setLen(final int len) {
        this.len = len;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(final String end) {
        this.end = end;
    }

    /**
     * @param monospace
     *            设置monospace的值
     */
    public void setMonospace(boolean monospace) {
        this.monospace = monospace;
    }

    /**
     * @return monospace的值
     */
    public boolean getMonospace() {
        return monospace;
    }
}
