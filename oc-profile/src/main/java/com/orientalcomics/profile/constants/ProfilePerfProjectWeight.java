package com.orientalcomics.profile.constants;

import java.util.HashMap;
import java.util.Map;

import com.orientalcomics.profile.biz.base.Displayable;
import com.orientalcomics.profile.biz.base.Idable;

public enum ProfilePerfProjectWeight implements Idable, Displayable {
    PARTICIPART(1, "参与"), //
    CHARGE(2, "关键"), //
    CRITICAL(3, "负责"), //
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