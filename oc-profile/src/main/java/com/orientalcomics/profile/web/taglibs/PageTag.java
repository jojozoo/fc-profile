package com.orientalcomics.profile.web.taglibs;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.InvocationUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.orientalcomics.profile.core.utility.Escape;


public class PageTag extends SimpleTagSupport {

    static final String      HIDE_WHEN_NONE    = "none";               // 任何情况都不隐藏
    static final String      HIDE_WHEN_EMPTY   = "empty";              // 当total为0时，自动隐藏
    static final String      HIDE_WHEN_SINGLE  = "single";             // 当只有一页时，自动隐藏
    static final String      HIDE_WHEN_DEFAULT = HIDE_WHEN_NONE;       // 默认

    String                   hidewhen          = HIDE_WHEN_NONE;

    boolean                  container         = true;                 // 是否容器

    String                   ctag              = "div";                // 容器tag，默认为：当wrapTag为li时，ctag为ul；否则为div

    String                   cclass            = "pages";              // 容器Class

    // 页面属性
    boolean                  wrap              = false;                // 是否wrap

    String                   wtag              = "li";                 // Wrap的元素名称

    String                   wclass            = null;                 // 默认的Class

    String                   wcurclass         = "current";            // 选中当前的Class

    String                   wprevclass        = "prev";               // 上一页

    String                   wnextclass        = "next";               // 下一页

    String                   wclick            = null;                 // Click事件

    String                   prevclass         = "prev chn";           // 上一页

    String                   nextclass         = "next chn";           // 下一页

    String                   href              = "javascript:void(0);"; // a
                                                                        // 标签的HREF

    String                   click             = null;                 // a
                                                                        // 标签的Click事件

    String                   aClass            = null;

    String                   curclass          = "current";            // 选中的当前的Class

    boolean                  curHidden         = false;                // 对于选中的，不使用a标签

    int                      len               = 10;                   // 一共显示多少个页面数目

    // 页面属性
    int                      page              = 0;                    // 当前页面的值，从0开始

    int                      poffset           = 0;                    // 页面的偏移量

    boolean                  offsetmode        = false;                // Offset模式

    int                      offset            = 0;                    // 偏移量

    int                      total             = 0;                    // 总数，必须指定

    private static final int DEFAULT_SIZE      = 20;
    int                      size              = 0;                    // 每页的显示的数目，必须大于0

    public int lt0Then0(final int val) {
        return this.lt0Then(val, 0);
    }

    public int lt0Then(final int val, final int def) {
        if (val < 0) {
            return def;
        }
        return val;
    }

    public int ltThen(final int val, final int base, final int def) {
        if (val < base) {
            return def;
        }
        return val;
    }

    public int gtThen(final int val, final int base, final int def) {
        if (val > base) {
            return def;
        }
        return val;
    }

    public int le0Then(final int val, final int def) {
        if (val <= 0) {
            return def;
        }
        return val;
    }

    public String emptyThen(final String val, final String def) {
        String res = val;
        if (StringUtils.isBlank(val)) {
            res = def;
        }
        return StringUtils.trimToNull(res);
    }

    private String format(final String str, final int p) {
        return Escape.stringToHTMLString(str == null ? str : str.replace("{page}", String.valueOf(p + this.poffset)));
    }

    /**
     * 渲染
     * 
     * @param p
     *            渲染的页码
     * @param name
     *            显示的文本
     */
    private void render(final int p, String wExtClass, String extClass, final String name, final StringBuilder sb) {
        // 渲染Wrap
        wExtClass = this.emptyThen(wExtClass, null);
        extClass = this.emptyThen(extClass, null);

        if (this.wrap) {
            sb.append("<").append(this.wtag);// <li
            if (this.page == p) { // cur
                if (this.wcurclass != null) {
                    sb.append(" ").append("class='");
                    if (wExtClass != null) {
                        sb.append(wExtClass).append(" ");
                    }
                    sb.append(this.wcurclass).append("'");// class='cur'
                }
            } else {
                if (this.wclass != null) {
                    sb.append(" ").append("class='");
                    if (wExtClass != null) {
                        sb.append(wExtClass).append(" ");
                    }
                    sb.append(this.wclass).append("'");// class='cur'
                }
            }

            if (this.wclick != null) {
                sb.append(" ").append("onclick='").append(this.format(this.wclick, p)).append("'");// onclick='...'
            }

            sb.append(">");
        }

        final boolean a = p >= 0 && (this.page != p || !this.curHidden);//

        // a tag
        if (a) {
            sb.append("<a title='").append(name).append("'");

            // class
            if (this.page == p) { // cur
                if (this.curclass != null) {
                    sb.append(" ").append("class='");
                    if (extClass != null) {
                        sb.append(extClass).append(" ");
                    }
                    sb.append(this.curclass).append("'");// class='cur'
                }
            } else {
                if (this.aClass != null) {
                    sb.append(" ").append("class='");
                    if (extClass != null) {
                        sb.append(extClass).append(" ");
                    }
                    sb.append(this.aClass).append("'");// class='cur'
                }
            }

            // href
            if (this.href != null) {
                sb.append(" ").append("href='").append(this.format(this.href, p)).append("'");// href='...'
            }

            // click
            if (this.click != null) {
                sb.append(" ").append("onclick='").append(this.format(this.click, p)).append("'");// onclick='...'
            }
            sb.append(">");
        } else {
            sb.append("<span>");
        }
        sb.append(name);

        if (a) {
            sb.append("</a>");
        } else {
            sb.append("</span>");
        }

        if (this.wrap) {
            sb.append("</").append(this.wtag).append(">");
        }
    }

    private String renderPages() {
        final StringBuilder sb = new StringBuilder();

        // 样式
        // 显示： 上一页 1 ... 5 6 7 8 9 ... 1000 下一页
        // page: 0 ... 4 5 6 7 8 ... 999
        // 获得页面大小
        $size: if (this.size <= 0) {
            this.size = NumberUtils.toInt(String.valueOf(getJspContext().findAttribute("_pagesize_")), 0);
            if (this.size > 0) {
                break $size;
            }
            Invocation inv = InvocationUtils.getCurrentThreadInvocation();
            if (inv != null) {
                this.size = NumberUtils.toInt(String.valueOf(inv.getModel("_pagesize_")), 0);
                if (this.size > 0) {
                    break $size;
                }
                this.size = NumberUtils.toInt(String.valueOf(inv.getRequest().getAttribute("_pagesize_")), 0);
                if (this.size > 0) {
                    break $size;
                }
            }
            this.size = DEFAULT_SIZE;
        }
        // 计算页码数目
        final int pageCnt = this.total == 0 ? 1 : (this.total - 1) / this.size + 1; // 如果没有任何内容，同样认为在第一页
        final int maxPage = pageCnt - 1;
        if (this.page > maxPage) { // page从0开始，最大为pageCnt - 1
            this.page = maxPage;
        }

        if (HIDE_WHEN_NONE.equals(hidewhen)) {
            // do nothing
        } else if (HIDE_WHEN_EMPTY.equals(hidewhen)) {
            if (total == 0) {
                return StringUtils.EMPTY;
            }
        } else if (HIDE_WHEN_SINGLE.equals(hidewhen)) {
            if (pageCnt == 1) {
                return StringUtils.EMPTY;
            }
        }

        // 计算中间块(长度为len）
        final int leftLen = (this.len - 1) / 2;
        final int rightLen = this.len - 1 - leftLen;
        final int spage = this.lt0Then(this.page - leftLen, 0);
        final int epage = this.gtThen(this.page + rightLen, maxPage, maxPage);

        // 开始搞喽
        if (this.container) {
            sb.append("<").append(this.ctag);
            // class
            if (this.cclass != null) { // cur
                sb.append(" ").append("class='").append(this.cclass).append("'");// class='page-panel';
            }
            sb.append(">");
        }
        // 上一页
        if (this.page > 0) { // 大于0就有上一页
            this.render(this.page - 1, this.wprevclass, this.prevclass, "上一页", sb);
        }
        // 快速跳转第一页
        if (spage - 0 > 0) {
            this.render(0, null, null, "1", sb);
        }

        if (spage - 0 > 1) {
            this.render(-1, null, null, "...", sb);
        }

        for (int p = spage; p <= epage; ++p) {
            this.render(p, null, null, String.valueOf(p + 1), sb);
        }

        if (maxPage - epage > 1) {
            this.render(-1, null, null, "...", sb);
        }

        if (maxPage - epage > 0) {
            this.render(maxPage, null, null, String.valueOf(pageCnt), sb);
        }

        if (this.page + 1 < pageCnt) {
            this.render(this.page + 1, this.wnextclass, this.nextclass, "下一页", sb);
        }

        if (this.container) {
            sb.append("</").append(this.ctag).append(">");
        }

        return sb.toString();
    }

    private String renderOffsets() {
        return null;
    }

    @Override
    public void doTag() throws JspException, IOException {
        // 核实样式
        if (this.wtag == null) {
            this.wrap = false;
        }

        if (this.container) {
            if (this.wrap && "li".equalsIgnoreCase(this.wtag)) {
                this.ctag = "ul";
            } else {
                this.ctag = "div";
            }
        }

        final String result = this.renderPages();

        if (result != null) {
            super.getJspContext().getOut().write(result);
        }
    }

    public boolean isContainer() {
        return this.container;
    }

    public void setContainer(final boolean container) {
        this.container = container;
    }

    public String getCtag() {
        return this.ctag;
    }

    public void setCtag(final String ctag) {
        this.ctag = this.emptyThen(ctag, null);
    }

    public boolean isWrap() {
        return this.wrap;
    }

    public void setWrap(final boolean wrap) {
        this.wrap = wrap;
    }

    public String getWtag() {
        return this.wtag;
    }

    public void setWtag(final String wtag) {
        this.wtag = this.emptyThen(wtag, "li");
    }

    public String getWclass() {
        return this.wclass;
    }

    public void setWclass(final String wclass) {
        this.wclass = this.emptyThen(wclass, null);
    }

    public String getWcurclass() {
        return this.wcurclass;
    }

    public void setWcurclass(final String wcurclass) {
        this.wcurclass = this.emptyThen(wcurclass, "cur");
    }

    public String getWclick() {
        return this.wclick;
    }

    public void setWclick(final String wclick) {
        this.wclick = this.emptyThen(wclick, null);
    }

    public String getHref() {
        return this.href;
    }

    public void setHref(final String href) {
        this.href = this.emptyThen(href, null);
    }

    public String getClick() {
        return this.click;
    }

    public void setClick(final String click) {
        this.click = this.emptyThen(click, null);
    }

    public String getCurclass() {
        return this.curclass;
    }

    public void setCurclass(final String curclass) {
        this.curclass = this.emptyThen(curclass, null);
    }

    public void setClass(final String aclass) {
        this.aClass = this.emptyThen(this.aClass, null);
    }

    public String getNextclass() {
        return this.nextclass;
    }

    public void setNextclass(final String nextclass) {
        this.nextclass = this.emptyThen(nextclass, null);
    }

    public String getPrevclass() {
        return this.prevclass;
    }

    public void setPrevclass(final String prevclass) {
        this.prevclass = this.emptyThen(prevclass, null);
    }

    public String getWnextclass() {
        return this.wnextclass;
    }

    public void setWnextclass(final String wnextclass) {
        this.wnextclass = this.emptyThen(wnextclass, null);
    }

    public String getWprevclass() {
        return this.wprevclass;
    }

    public void setWprevclass(final String wprevclass) {
        this.wprevclass = this.emptyThen(wprevclass, null);
    }

    public boolean isCurHidden() {
        return this.curHidden;
    }

    public void setCurHidden(final boolean curHidden) {
        this.curHidden = curHidden;
    }

    public int getLen() {
        return this.len;
    }

    public void setLen(final int len) {
        this.len = this.le0Then(len, 10);
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(final int page) {
        this.page = this.lt0Then0(page);
    }

    public int getPoffset() {
        return this.poffset;
    }

    public void setPoffset(final int poffset) {
        this.poffset = this.lt0Then0(poffset);
    }

    // public boolean isOffsetmode() {
    // return this.offsetmode;
    // }
    //
    // public void setOffsetmode(final boolean offsetmode) {
    // this.offsetmode = offsetmode;
    // }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(final int offset) {
        this.offset = this.lt0Then0(offset);
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(final int total) {
        this.total = this.lt0Then0(total);
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(final int size) {
        this.size = this.le0Then(size, 20);
    }

    public String getHidewhen() {
        return hidewhen;
    }

    public void setHidewhen(String hidewhen) {
        hidewhen = StringUtils.lowerCase(hidewhen);

        if (!Arrays.asList(HIDE_WHEN_NONE, HIDE_WHEN_EMPTY, HIDE_WHEN_SINGLE).contains(hidewhen)) {
            hidewhen = HIDE_WHEN_DEFAULT;
        }
        this.hidewhen = hidewhen;
    }

    public static void main(final String[] args) {
        for (final Field f : PageTag.class.getDeclaredFields()) {
            if (f.getDeclaringClass() == PageTag.class) {
                if (f.getModifiers() != Modifier.PUBLIC) {
                    final String name = f.getName();
                    System.out.println("		<attribute>\r\n" + "			<name>" + name + "</name>\r\n" + "			<required>true</required>\r\n"
                            + "			<rtexprvalue>true</rtexprvalue>\r\n" + "		</attribute>");
                }
            }
        }
    }
}
