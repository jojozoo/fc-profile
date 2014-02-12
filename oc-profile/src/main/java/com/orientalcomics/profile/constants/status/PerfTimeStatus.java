package com.orientalcomics.profile.constants.status;

import java.util.HashMap;
import java.util.Map;

import com.orientalcomics.profile.biz.base.Displayable;
import com.orientalcomics.profile.biz.base.Idable;


public enum PerfTimeStatus implements Idable,Displayable{
    READY(0, "待开始"), //
    STARTED(1, "进行中"), //
    WILL_END(2, "即将结束"), //
    END(3, "已结束"), //
    ;
    private final int    id;
    private final String display;

    PerfTimeStatus(int id, String display) {
        this.id = id;
        this.display = display;
    }

    public int getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    private static final Map<Integer, PerfTimeStatus> MAP = new HashMap<Integer, PerfTimeStatus>();

    static {
        for (PerfTimeStatus item : values()) {
            if (MAP.put(item.getId(), item) != null) {
                throw new IllegalStateException("PerfTimeStatus|id conflict|" + item.getId());
            }
        }
    }

    public static PerfTimeStatus findById(int id) {
        return MAP.get(id);
    }
}
