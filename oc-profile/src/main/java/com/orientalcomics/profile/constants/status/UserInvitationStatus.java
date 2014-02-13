package com.orientalcomics.profile.constants.status;

import java.util.HashMap;
import java.util.Map;

import com.orientalcomics.profile.biz.base.Displayable;
import com.orientalcomics.profile.biz.base.Idable;

/**
 * @author 张浩 E-mail:hao.zhang@renren-inc.com
 * @date 2012-3-21 上午11:16:33
 * 
 *       用户的邀请状态
 */

public enum UserInvitationStatus implements Idable,Displayable{
    READY(0, "未知"), //
    NO_INVITATIONS(4, "还未邀请"), //
    ON_INVITATIONS(5, "邀请人正在进行互评"), //
    COMPLETE_INVITATIONS(6, "邀请人已完成互评"), //

    // --------->>内部判断这些
    /**
     * 内部判断
     */
    NOT_ACCEPT(1, "未接受，可以撤销"), //
    /**
     * 内部判断
     */
    PEER_NOT_SUBMIT(2, "邀请人还未提交"), //
    /**
     * 内部判断
     */
    PEER_SUBMIT(3, "邀请人已经提交申请"), //
    // <<-------------------

    ;
    private final int    id;
    private final String display;

    UserInvitationStatus(int id, String display) {
        this.id = id;
        this.display = display;
    }

    public int getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    private static final Map<Integer, UserInvitationStatus> MAP = new HashMap<Integer, UserInvitationStatus>();

    static {
        for (UserInvitationStatus item : values()) {
            if (MAP.put(item.getId(), item) != null) {
                throw new IllegalStateException("UserInvitationStatus|id conflict|" + item.getId());
            }
        }
    }

    public static UserInvitationStatus findById(int id) {
        return MAP.get(id);
    }
}
