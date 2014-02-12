package com.orientalcomics.profile.constants;

import com.orientalcomics.profile.biz.base.Nameable;

public enum ProfileActionCategory implements Nameable {
    USER_MANAGE("员工管理"), //
    PERF_MANAGE("绩效管理"), //
    PROMOTION_MANAGE("升职管理"), //
    INFO("个人信息"), //
    RESUME("简历"), //
    KPI("KPI"), //
    WEEKLY_REPORT("周报"), //
    PERF("绩效"), //
    ;
    private final String name;

    ProfileActionCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}