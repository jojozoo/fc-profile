package com.renren.profile.web.controllers.perf;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.renren.profile.biz.logic.PerfTimeService;
import com.renren.profile.biz.logic.UserPerfService;
import com.renren.profile.biz.model.PerfTime;
import com.renren.profile.biz.model.UserPerf;
import com.renren.profile.util.Collections0;
import com.renren.profile.web.base.HtmlPage;
import com.renren.profile.web.config.ProfileConfigs.PageSizeConfigView;
import com.renren.profile.web.controllers.internal.LoginRequiredController;

/**
 * 考核首页。<br/>
 * 显示历史考核结果。
 * 
 * @author 黄兴海(xinghai.huang)
 * @date 2012-3-13 下午2:56:45
 */
@Path("")
public class IndexController extends LoginRequiredController {

    @Autowired
    UserPerfService userPerfService;

    @Autowired
    PerfTimeService perfTimeService;

    @Get
    public String index(Invocation inv, HtmlPage page, @Param("curpage") int curPage) {
        int userId = currentUserId();
        $fail: {
            int total = perfTimeService.countAll();

            int pageSize = getPageSize(PageSizeConfigView.PERF_USER_RESULT);
            curPage = checkAndReturnPage(curPage, total, pageSize);
            inv.addModel("total", total);
            inv.addModel("pagesize", pageSize);
            inv.addModel("curpage", curPage);

            List<PerfTime> perfTimes = perfTimeService.query(curPage * pageSize, pageSize);
            inv.addModel("current_perftime_id", perfTimeService.getCurrentPerfId());
            inv.addModel("perf_times", perfTimes);
            if (CollectionUtils.isNotEmpty(perfTimes)) {
                Collection<Integer> perfTimeIds = Collections2.transform(perfTimes, new Function<PerfTime, Integer>() {

                    @Override
                    public Integer apply(PerfTime from) {
                        return from.getId();
                    }
                });
                List<UserPerf> userPerfs = userPerfService.getUserSelfPerfListByPerfTimes(userId, perfTimeIds);
                Map<Integer/* perf time id */, UserPerf> userPerfMap = Collections0.packageMapByField(userPerfs, new Function<UserPerf, Integer>() {

                    @Override
                    public Integer apply(UserPerf from) {
                        return from.getPerfTimeId();
                    }
                });
                inv.addModel("perf_map", userPerfMap);
            }
            return "perf_index";
        }
    }
}
