package com.orientalcomics.profile.constants.status;

import java.util.HashMap;
import java.util.Map;

import com.orientalcomics.profile.biz.base.Displayable;
import com.orientalcomics.profile.biz.base.Idable;


public enum PerfPromotionStatus implements Idable,Displayable{
    READY(0, "待开始"), //
    STARTED(1, "进行中"), //
    END(2, "已结束"), //
    ;
    private final int    id;
    private final String display;

    PerfPromotionStatus(int id, String display) {
        this.id = id;
        this.display = display;
    }

    public int getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    private static final Map<Integer, PerfPromotionStatus> MAP = new HashMap<Integer, PerfPromotionStatus>();

    static {
        for (PerfPromotionStatus item : values()) {
            if (MAP.put(item.getId(), item) != null) {
                throw new IllegalStateException("PerfPromotionStatus|id conflict|" + item.getId());
            }
        }
    }

    public static PerfPromotionStatus findById(int id) {
        return MAP.get(id);
    }
}
