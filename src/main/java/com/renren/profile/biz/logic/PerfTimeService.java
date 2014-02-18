package com.renren.profile.biz.logic;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.renren.profile.biz.base.PerfTimeAssignable;
import com.renren.profile.biz.dao.PerfTimeDAO;
import com.renren.profile.biz.model.PerfTime;

@Service
public class PerfTimeService {
    @Autowired
    PerfTimeDAO perfTimeDAO;

    public void wrapPerfTimes(Collection<? extends PerfTimeAssignable> inputs) {
        if (CollectionUtils.isEmpty(inputs)) {
            return;
        }
        for (PerfTimeAssignable input : inputs) {
            input.perfTime(perfTimeDAO.query(input.perfTimeId()));
        }
    }

    public PerfTime getCurrentPerf() {
        return perfTimeDAO.queryNewestPerfItem();
    }

    public int getCurrentPerfId() {
        PerfTime perfTime = getCurrentPerf();
        return perfTime == null ? 0 : perfTime.getId();
    }

    public boolean isCurrentPerf(int perfTimeId) {
        return perfTimeDAO.queryNewestPerfItem().getId() == perfTimeId;
    }

    public PerfTime getPerfTime(int perfTimeId) {
        return perfTimeDAO.query(perfTimeId);
    }

    public int countAll() {
        return perfTimeDAO.countAll();
    }

    public List<PerfTime> query(int offset, int count) {
        return perfTimeDAO.queryAll(offset, count);
    }
}
