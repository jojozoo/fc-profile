package com.renren.profile.constants;

import java.util.HashMap;
import java.util.Map;

public enum ProfilePerfProjectWeight {
    PARTICIPART(1, "参与"), //
    CHARGE(2, "负责"), //
    CRITICAL(3, "关键"), //
    ;

    private final int                                           id;
    private final String                                        display;

    private static final Map<Integer, ProfilePerfProjectWeight> MAP = new HashMap<Integer, ProfilePerfProjectWeight>();

    static {
        for (ProfilePerfProjectWeight item : values()) {
            if (MAP.put(item.getId(), item) != null) {
                throw new IllegalStateException("ProfilePerfWeight|id conflict|" + item.getId());
            }
        }
    }

    ProfilePerfProjectWeight(int id, String display) {
        this.id = id;
        this.display = display;
    }

    public int getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public static ProfilePerfProjectWeight findById(int id) {
        return MAP.get(id);
    }
}