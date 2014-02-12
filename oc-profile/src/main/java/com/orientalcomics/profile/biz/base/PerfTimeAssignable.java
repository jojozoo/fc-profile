package com.orientalcomics.profile.biz.base;

import com.orientalcomics.profile.biz.model.PerfTime;

public interface PerfTimeAssignable {
    int perfTimeId();

    void perfTime(PerfTime perfTime);
}
