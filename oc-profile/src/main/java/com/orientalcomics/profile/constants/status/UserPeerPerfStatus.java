package com.orientalcomics.profile.constants.status;

import java.util.HashMap;
import java.util.Map;

import com.orientalcomics.profile.biz.base.Displayable;
import com.orientalcomics.profile.biz.base.Idable;


/**
 * @author 张浩 E-mail:hao.zhang@renren-inc.com
 * @date 2012-3-21 上午11:47:07
 * 
 *       用户对别人互评的状态
 */

public enum UserPeerPerfStatus implements Idable,Displayable{
    READY(1, "未知"), //
    NO_INVITATIONS(4, "我没有互评任务"),
    ON_INVITATIONS(5, "正在对peer进行互评"),
    COMPLETE_INVITATIONS(6, "我的互评任务已完成"),

    // --------->>内部判断这些
    /**
     * 内部判断
     */
    SAVED(2, "互评已保存"),
    /**
     * 内部判断
     */
    SUBMITTED(3, "互评已提交"),
    // <<-------------------

    ;
    private final int    id;
    private final String display;

    UserPeerPerfStatus(int id, String display) {
        this.id = id;
        this.display = display;
    }

    public int getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    private static final Map<Integer, UserPeerPerfStatus> MAP = new HashMap<Integer, UserPeerPerfStatus>();

    static {
        for (UserPeerPerfStatus item : values()) {
            if (MAP.put(item.getId(), item) != null) {
                throw new IllegalStateException("UserPeerPerfStatus|id conflict|" + item.getId());
            }
        }
    }

    public static UserPeerPerfStatus findById(int id) {
        return MAP.get(id);
    }
}
