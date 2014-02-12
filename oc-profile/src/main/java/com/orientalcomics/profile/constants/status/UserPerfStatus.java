package com.orientalcomics.profile.constants.status;

import java.util.HashMap;
import java.util.Map;

import com.orientalcomics.profile.biz.base.Displayable;
import com.orientalcomics.profile.biz.base.Idable;


/**
 * @author 张浩 E-mail:hao.zhang@renren-inc.com
 * @date 2012-3-20 下午06:50:01
 * 
 *       用户绩效状态
 */

public enum UserPerfStatus  implements Idable,Displayable{
    READY(0, "未开始自评"), //
    SAVED(1, "自评已保存"), //
    SUBMITTED(2, "自评已提交"), //
    ;
    private final int    id;
    private final String display;

    UserPerfStatus(int id, String display) {
        this.id = id;
        this.display = display;
    }

    public int getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    private static final Map<Integer, UserPerfStatus> MAP = new HashMap<Integer, UserPerfStatus>();

    static {
        for (UserPerfStatus item : values()) {
            if (MAP.put(item.getId(), item) != null) {
                throw new IllegalStateException("UserPerfStatus|id conflict|" + item.getId());
            }
        }
    }

    public static UserPerfStatus findById(int id) {
        return MAP.get(id);
    }
}
