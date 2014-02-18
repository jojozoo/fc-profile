package com.renren.profile.biz.base;

import com.renren.profile.biz.model.PerfTime;

public interface PerfTimeAssignable {
    int perfTimeId();

    void perfTime(PerfTime perfTime);
}
