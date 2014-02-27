package com.orientalcomics.profile.constants.status; 

import java.util.HashMap;
import java.util.Map;

import com.orientalcomics.profile.biz.base.Displayable;
import com.orientalcomics.profile.biz.base.Idable;

/** 
 * @author 张浩 E-mail:zhanghao@foundercomics.com 
 * @version 创建时间：2014年2月27日 下午6:09:20 
 * 类说明 :日报的状态
 */
public enum DailyReportStatus implements Idable,Displayable{
	READY(0, "未编辑"), //
    SAVED(1, "已保存"), //
    SUBMITTED(2, "已提交"), //
    ;
    private final int    id;
    private final String display;

    DailyReportStatus(int id, String display) {
        this.id = id;
        this.display = display;
    }

    public int getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    private static final Map<Integer, DailyReportStatus> MAP = new HashMap<Integer, DailyReportStatus>();

    static {
        for (DailyReportStatus item : values()) {
            if (MAP.put(item.getId(), item) != null) {
                throw new IllegalStateException("DailyReportStatus|id conflict|" + item.getId());
            }
        }
    }

    public static DailyReportStatus findById(int id) {
        return MAP.get(id);
    }
}
 