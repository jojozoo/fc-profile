package com.orientalcomics.profile.constants;

import java.util.HashMap;
import java.util.Map;

import com.orientalcomics.profile.biz.base.Idable;
import com.orientalcomics.profile.biz.base.Nameable;


public enum ProfileRelation  implements Idable,Nameable{
    SELF(1, "自己"), //
    SUPERIOR(2, "上级领导"), //
    LEADER(3, "直接领导"), //
    SAME_DEPARTMENT(4, "同部门"), //
    ;
    private final int    id;
    private final String name;

    ProfileRelation(int id, String name) {
        this.id = id;
        this.name = name;
    }

    private static final Map<Integer, ProfileRelation> MAP = new HashMap<Integer, ProfileRelation>();

    static {
        for (ProfileRelation item : values()) {
            if (MAP.put(item.getId(), item) != null) {
                throw new IllegalStateException("ProfileRelation|id conflict|" + item.getId());
            }
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static ProfileRelation findById(Integer id) {
        return MAP.get(id);
    }
}