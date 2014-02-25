package com.orientalcomics.profile.web.taglibs;

import java.io.IOException;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.time.FastDateFormat;

import com.orientalcomics.profile.util.time.TimeFormatUtils.FormatType;
import com.orientalcomics.profile.util.time.TimeUtils;

public class FormatTimeTag extends TagSupport {
    private static final long   serialVersionUID = -8390711656032228380L;

    private static final String DATE             = "date";
    private static final String TIME             = "time";
    private static final String DATETIME         = "datetime";
    private static final String HUAMAN_DATE      = "humandate";
    private static final String HUAMAN_DATETIME  = "humandatetime";

    // 往年 -- 显示“年月”，往日 -- 显示“月日”，今日 -- 显示“时分”
    private static final String AUTO             = "auto";
    protected Date              value;
    protected String            type;
    protected String            pattern;
    protected String            empty;
    protected boolean           quiet;
    protected boolean           title;                                   // 当HTML输出时，是否显示title

    private String              var;
    private int                 scope;

    public FormatTimeTag() {
        init();
    }

    private void init() {
        this.type = null;
        this.pattern = null;
        this.var = null;
        this.value = null;
        this.scope = PageContext.PAGE_SCOPE;
        this.empty = null;
        this.quiet = true;
        this.title = true;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public void setScope(String scope) {
        this.scope = getScope(scope);
    }

    public void setValue(Date value) {
        this.value = value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setEmpty(String empty) {
        this.empty = empty;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    private int getScope(String scope) {
        int ret = PageContext.PAGE_SCOPE; // default

        if ("request".equalsIgnoreCase(scope))
            ret = PageContext.REQUEST_SCOPE;
        else if ("session".equalsIgnoreCase(scope))
            ret = PageContext.SESSION_SCOPE;
        else if ("application".equalsIgnoreCase(scope))
            ret = PageContext.APPLICATION_SCOPE;

        return ret;
    }

    public int doEndTag() throws JspException {
        String formatted = null;

        if (this.value != null) {
            if (this.pattern != null) {
                try {
                    formatted = FastDateFormat.getInstance(this.pattern).format(this.value);
                } catch (Exception e) {
                    if (!quiet) {
                        throw new JspTagException(e.toString(), e);
                    }
                }
            } else {
                FormatType formatType = getTimeFormatUsingType();
                formatted = TimeUtils.Format.format(formatType, this.value);
            }

            if (formatted == null) {
                formatted = this.value.toString();
            }
        } else {
            formatted = this.empty;
        }

        if (this.var != null) {
            if (formatted == null) {
                this.pageContext.removeAttribute(var, scope);
            } else {
                this.pageContext.setAttribute(this.var, formatted, this.scope);
            }
        } else {
            if (formatted != null) {
                if (this.title && this.value != null) {
                    String fullTime = format(TimeUtils.Format.DATE_TIME, this.value);
                    if (fullTime != null) {
                        formatted =
                                "<span class='-admin-tag-auto-time' title='" + fullTime + "'>" + formatted + "</span>";
                    }
                }
                try {
                    this.pageContext.getOut().print(formatted);
                } catch (IOException ioe) {
                    if (!quiet) {
                        throw new JspTagException(ioe.toString(), ioe);
                    }
                }
            }
        }

        return EVAL_PAGE;
    }

    private String format(FastDateFormat dateFormat, Date time) throws JspTagException {
        try {
            return dateFormat.format(time);
        } catch (Exception e) {
            if (!quiet) {
                throw new JspTagException(e.toString(), e);
            }
        }
        return null;
    }

    private FormatType getTimeFormatUsingType() {
        if (DATE.equalsIgnoreCase(this.type)) {
            return FormatType.DATE;
        }
        if (TIME.equalsIgnoreCase(this.type)) {
            return FormatType.TIME;
        }
        if (DATETIME.equalsIgnoreCase(this.type)) {
            return FormatType.DATE_TIME;
        }
        if (HUAMAN_DATE.equalsIgnoreCase(this.type)) {
            return FormatType.DATE_HUMAN;
        }
        if (HUAMAN_DATETIME.equalsIgnoreCase(this.type)) {
            return FormatType.DATETIME_HUMAN;
        }
        if (AUTO.equalsIgnoreCase(this.type)) {
            return FormatType.AUTO;
        }
        return FormatType.AUTO;
    }

    public void release() {
        init();
    }
}
