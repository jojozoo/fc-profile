package com.renren.profile.constants.status;

import java.util.HashMap;
import java.util.Map;

/**
 * 周报的状态
 * 
 * @author 黄兴海(xinghai.huang)
 * @date 2012-3-16 下午12:56:22
 */
public enum WeeklyReportStatus {
    READY(0, "未编辑"), //
    SAVED(1, "已保存"), //
    SUBMITTED(2, "已提交"), //
    ;
    private final int    id;
    private final String display;

    WeeklyReportStatus(int id, String display) {
        this.id = id;
        this.display = display;
    }

    public int getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    private static final Map<Integer, WeeklyReportStatus> MAP = new HashMap<Integer, WeeklyReportStatus>();

    static {
        for (WeeklyReportStatus item : values()) {
            if (MAP.put(item.getId(), item) != null) {
                throw new IllegalStateException("WeeklyReportStatus|id conflict|" + item.getId());
            }
        }
    }

    public static WeeklyReportStatus findById(int id) {
        return MAP.get(id);
    }
}
