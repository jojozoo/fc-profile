package com.renren.profile.util.sys.status;
/**
 * 
 * 项目名称：renren-profile  
 * 类名称： InvitationStatus  
 * 类描述： 互相邀请的状态信息  
 * 创建人： wen.he1  
 * 创建时间：2012-3-16 上午11:47:07  
 * 
 * @version
 */
public enum InvitationStatus {

    ON_ACCEPT(1), // 未接受，可以撤销
    ON_SUBMIT(2), //   邀请人还未提交
    SUBMITED(3),  //  邀请人已经提交申请
    ;
    private final int name;

    InvitationStatus(int name) {
        this.name = name;
    }

    public int getName() {
        return name;
    }
}
