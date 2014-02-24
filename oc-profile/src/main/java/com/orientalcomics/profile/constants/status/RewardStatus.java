package com.orientalcomics.profile.constants.status;

public enum RewardStatus {
	
    SEND_REWARD(1, "赠送小红花"), //
    LEADER_DISAGREE(2, "对方主管不同意赠送小红花"), //
    LEADER_AGREE(3, "对方主管同意赠送小红花"), //
    CONVERT_THINGS(4, "小红花已经兑换成别的东西"), //
    ;
    private final int id;
    private final String display;

    RewardStatus(int id, String display) {
        this.id = id;
        this.display = display;
    }

    public int getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }
}
