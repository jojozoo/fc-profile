package com.orientalcomics.profile.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 张浩 E-mail:hao.zhang@renren-inc.com
 * @date 2012-4-16 下午02:21:57
 *
 * 小红花奖励类型
 */

public enum RewardItemType {
	VIRTUAL(1, "虚拟"), //
    REAL(2, "现实") //
    ;
    private final int    id;
    private final String name;

    RewardItemType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    private static final Map<Integer, RewardItemType> MAP = new HashMap<Integer, RewardItemType>();

    static {
        for (RewardItemType item : values()) {
            if (MAP.put(item.getId(), item) != null) {
                throw new IllegalStateException("RewardItemType|id conflict|" + item.getId());
            }
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static RewardItemType findById(Integer id) {
        return MAP.get(id);
    }
}
