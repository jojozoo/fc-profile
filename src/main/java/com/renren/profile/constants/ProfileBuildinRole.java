package com.renren.profile.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum ProfileBuildinRole {
    ANONYMOUS_USER(1, "匿名用户", true), //
    LOGINED_USER(2, "登录用户"), //
    ;
    private final int     id;
    private final String  name;
    private final boolean hidden;

    ProfileBuildinRole(int id, String name) {
        this(id, name, false);
    }

    ProfileBuildinRole(int id, String name, boolean hidden) {
        this.id = id;
        this.name = name;
        this.hidden = hidden;
    }

    private static final Map<Integer, ProfileBuildinRole> MAP = new HashMap<Integer, ProfileBuildinRole>();

    static {
        for (ProfileBuildinRole item : values()) {
            if (MAP.put(item.getId(), item) != null) {
                throw new IllegalStateException("ProfileBuildinRole|id conflict|" + item.getId());
            }
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isHidden() {
        return hidden;
    }

    public static Set<Integer> idSet() {
        return MAP.keySet();
    }
}
