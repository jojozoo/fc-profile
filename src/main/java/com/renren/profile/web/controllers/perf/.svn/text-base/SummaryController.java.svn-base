package com.renren.profile.web.controllers.perf;

import java.util.List;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.renren.profile.biz.logic.PerfTimeService;
import com.renren.profile.biz.logic.UserPerfService;
import com.renren.profile.biz.model.PeerPerf;
import com.renren.profile.biz.model.PerfTime;
import com.renren.profile.biz.model.UserPerf;
import com.renren.profile.biz.model.UserPerfProject;
import com.renren.profile.web.controllers.internal.LoginRequiredController;

/**
 * 汇总
 * 
 * @author 黄兴海(xinghai.huang)
 * @date 2012-3-13 下午5:42:58
 */
public class SummaryController extends LoginRequiredController {
    @Autowired
    PerfTimeService perfTimeService;

    @Autowired
    UserPerfService userPerfService;

    @Get("my/current")
    public String get_my_current(Invocation inv) {
        int userId = currentUserId();
        // 获得绩效时间
        PerfTime perfTime = perfTimeService.getCurrentPerf();
        if (perfTime == null) {
            return "e:404";
        }
        inv.addModel("perf_time", perfTime);
        inv.addModel("is_current_perf", true);
        inv.addModel("is_self_resource", true);
        
        String result = render(inv, userId, perfTime);
        if (result != null) {
            return result;
        }
        return "perf_summary";
    }

    @Get("my/${perf:\\d+}")
    public Object get_my_history(Invocation inv, @Param("perf") int perfTimeId) {
        if (perfTimeService.isCurrentPerf(perfTimeId)) {
            return "r:/perf/summary/my/current";
        }
        int userId = currentUserId();
        PerfTime perfTime = perfTimeService.getPerfTime(perfTimeId);
        if (perfTime == null) {
            return "e:404";
        }
        inv.addModel("perf_time", perfTime);
        inv.addModel("is_current_perf", false);
        inv.addModel("is_self_resource", true);

        String result = render(inv, userId, perfTime);
        if (result != null) {
            return result;
        }
        return "perf_summary";
    }

    @Get("{owner:\\d+}/current")
    public String get_other_current(Invocation inv, @Param("owner") int ownerId) {
        PerfTime perfTime = perfTimeService.getCurrentPerf();
        if (perfTime == null) {
            return "e:404";
        }
        inv.addModel("perf_time", perfTime);
        inv.addModel("is_current_perf", true);
        inv.addModel("is_self_resource", false);

        String result = render(inv, ownerId, perfTime);
        if (result != null) {
            return result;
        }
        return "perf_summary";
    }

    @Get("{owner:\\\\d+}/${perf:\\d+}")
    public String get_other_history(Invocation inv, @Param("owner") int ownerId, @Param("perf") int perfTimeId) {
        if (perfTimeService.isCurrentPerf(perfTimeId)) {
            return "r:/perf/summary/" + ownerId + "/current";
        }
        PerfTime perfTime = perfTimeService.getPerfTime(perfTimeId);
        if (perfTime == null) {
            return "e:404";
        }
        inv.addModel("perf_time", perfTime);
        inv.addModel("is_current_perf", false);
        inv.addModel("is_self_resource", false);

        String result = render(inv, ownerId, perfTime);
        if (result != null) {
            return result;
        }
        return "perf_summary";
    }

    private String render(Invocation inv, int userId, PerfTime perfTime) {
        // 获得用户的自评
        UserPerf userPerf = userPerfService.getUserSelfPerf(userId, perfTime.getId());
        if (userPerf == null) {
            return "e:404";
        }
        userPerfService.wrapPerfProjects(userPerf);
        // 项目的Wrap
        List<UserPerfProject> userPerfProjects = userPerf.projects();
        if (CollectionUtils.isNotEmpty(userPerfProjects)) {
            for (UserPerfProject userPeerPerf : userPerfProjects) {
                userPerfService.wrapPeerPerfProjects(userPeerPerf);
            }
        }

        inv.addModel("user_perf", userPerf);

        // 获得用户的互评
        List<PeerPerf> peerPerfs = userPerfService.getPeerPerfListByUserSelfPerfId(userPerf.getId());
        inv.addModel("peer_perfs", peerPerfs);

        // 获得汇报人的评价
        PeerPerf managerPerf = userPerfService.getManagerPerfByUserSelfPerfId(userPerf.getId());
        inv.addModel("manager_perf", managerPerf);
        return null;
    }
}
