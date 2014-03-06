package com.orientalcomics.profile.constants.email.type;

public enum SendEmailType {
	
	    PERF_START(1, "绩效考核开始"), 							// 绩效考核开始
	    
	    PERF_DEADLINE(2, "绩效考核截止"), 						// 绩效考核截止
	    
	    WEEKLY_REPORT_SUBMMITED(3, "周报提交"), 				// 周报提交
	    
	    WEEKLY_REPORT_PAY(4, "周报补交"),    				// 周报补交
	    
	    WEEKLY_REPORT_TIP(5, "周报提醒"),      			    // 周报提醒
	    
	    INVITE_FRIEND_ACCESS(6, "邀请好友互评"),                // 邀请好友互评 
	    
	    DELETE_FRIEND_ACCESS_SELF(7, "撤销好友互评(对邀请人)"),    // 撤销好友互评(对邀请人)
	    
	    DELETE_FRIEND_ACCESS_INVITE(8, "撤销好友互评(对被邀请人)"), // 撤销好友互评(对被邀请人)
	    
	    LEADER_CONFIRM_FLOWER(9, "赠送红花申请（发给上级）"),       // 赠送红花申请（发给上级）
	    
	    LEADER_AGREE_FLOWER_FOR_OBTAIN(10, "赠送红花成功提示（发给发红花人）"),    //赠送红花成功提示（发给发红花人）
	    
	    LEADER_CONFIRM_FLOWER_FOR_SENDER(11, "赠送红花成功提示（发给发红花人）"),    // 赠送红花成功提示（发给发红花人）
	    
	    REWARD_CONFIRM_FLOWER(12, "兑奖确认提醒"),                     // 兑奖确认提醒
	    
	    OBTAIN_MONEY_REWARD(13, "领取实物兑奖提醒"), 						// 领取实物兑奖提醒
	    
	    OBTATIN_VIRTUAL_REWARD(14, "领取虚拟奖章"),						 // 领取虚拟奖章
	    
	    LEADER_ACCESS_WEEKLY_REPORT(15, "提交周报点评"),						 // 提交周报点评
	    
	    TIP_LEADER_ACCESS_WEEKLY_REPORT(16, "提醒给下属周报点评"),						 // 提醒给下属周报点评
	    
	    TIP_NO_SUMMBIT_WEEKLY_REPORT(17, "周五1点发未提交周报的用户"),						 //周五1点发未提交周报的用户
	    
	    TIP_LEADER_NO_SUMMBIT_WEEKLY_REPORT(18, "下属没有提交周报提醒主管"),						 //下属没有提交周报发给主管
	    
	    LEADER_CONFIRM_SCORE(19, "积分申请（发给上级）"),
	    
	    
	    LEADER_COMFIRM_FOR_SENDER(20, "积分申请同意或拒绝提醒，发给申请人"),
	    
	    DAILY_REPORT_PLAN(21,"发送日报早报"),

	    DAILY_REPORT_DONE(22,"发送日报晚报"),
	    
	    USER_CREATE(23,"用户创建")
	    ;
	    
	    private final int    id;
	    private final String name;

	    public String getName() {
			return name;
		}

		SendEmailType(int id, String name) {
	        this.id = id;
	        this.name = name;
	    }

	    public int getId() {
	        return id;
	    }
}
