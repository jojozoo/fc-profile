package com.orientalcomics.profile.web.interceptors;

import java.util.List;
import java.util.Map;

import net.paoding.rose.web.Invocation;

import org.springframework.beans.factory.annotation.Autowired;

import com.orientalcomics.profile.biz.dao.RewardFlowerDAO;
import com.orientalcomics.profile.biz.dao.SystemPageDAO;
import com.orientalcomics.profile.biz.logic.DepartmentService;
import com.orientalcomics.profile.biz.logic.KpiService;
import com.orientalcomics.profile.biz.logic.PerfTimeService;
import com.orientalcomics.profile.biz.logic.ProfileConfigHelper;
import com.orientalcomics.profile.biz.logic.ProfileConfigService;
import com.orientalcomics.profile.biz.logic.ProfileConfigs.StringConfigView;
import com.orientalcomics.profile.biz.logic.StatusService;
import com.orientalcomics.profile.biz.logic.UserService;
import com.orientalcomics.profile.biz.model.Department;
import com.orientalcomics.profile.biz.model.PerfTime;
import com.orientalcomics.profile.biz.model.SystemPage;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.biz.model.UserKpi;
import com.orientalcomics.profile.util.common.Collections0;
import com.orientalcomics.profile.web.access.ProfileHostHolder;
import com.orientalcomics.profile.web.utils.AjaxUtils;

public class EnvInterceptor extends AbstractControllerInterceptorAdapter {

    @Autowired
    private ProfileHostHolder profileHostHolder;

    @Autowired
    private StatusService     statusService;
    @Autowired
    private UserService       userService;

    @Autowired
    private DepartmentService departmentService;
    
    @Autowired 
    private PerfTimeService perfTimeService;
    
//    @Autowired
//    private UserPerfService userPerfService;
//    
    @Autowired
    private KpiService      kpiService;
    
    @Autowired
    private ProfileConfigService profileConfigService;
    
    @Autowired
    private RewardFlowerDAO rewardFlowerDAO;
    
    @Autowired
    private SystemPageDAO systemPageDAO;

    @Override
    public PriorityType getPriorityType() {
        return PriorityType.Env;
    }

    @Override
    protected Object after(Invocation inv, Object instruction) throws Exception {
        if (AjaxUtils.getRequestAjaxType(inv) != null) {
            return null;
        }
        
//        Map<Integer,Department> mapDept = departmentService.getFirstDepartment();
//        Department 				dept    = mapDept.get(Integer.valueOf(31));
//        
//        
//        inv.addModel("_department_big_tree", dept);
//        inv.addModel("_nav_tree", profileConfigService.getNavData());
//        inv.addModel("_department_tree", mapDept.values());
        
        User user = profileHostHolder.getUser();
        int userId = user == null ? 0 : user.getId();
//        inv.addModel("_perf_status", statusService.getCurrentPerfStatus());
        if (userId > 0) {
            inv.addModel("_is_leader", userService.countSubordinates(userId) > 0);
            
//            List<UserKpi> list = kpiService.getUserKpiInfoContainStatus(userId,1);
//            if (Collections0.isNotEmpty(list)) {
//            	float sum = 0.0f;
//            	for (UserKpi userKpi : list) {
//            		sum += userKpi.getWeight();
//            	}
//            	if ( Float.valueOf("100").compareTo(Float.valueOf(sum)) == 0) {
//            		inv.addModel("_kpi_status", 1);
//            	}
//                
//            }
            
//            inv.addModel("_invitation_status", statusService.getUserInvitationStatus(userId));
//            inv.addModel("_self_perf_status", statusService.getUserSelfPerfStatus(userId));
//            inv.addModel("_peer_perf_status", statusService.getUserPeerPerfStatus(userId));
//            //加入等级和积分,若没有，则显示systempage里主键为一的
//            	
//            inv.addModel("_reward_flower", rewardFlowerDAO.getRewardFlower(userId, 3));
            
            
//            PerfTime perfTime = perfTimeService.getCurrent();
//            if (perfTime != null) {
//	            List<UserAccessInfoDTO> myFollowList = userPerfService.getMyReportUserInfo(userId, perfTime.getId());
//	            // 说明评价下属的是否已经完成的状态
//	            inv.addModel("displayStatus", false);
//	            if(myFollowList != null){
//		            for(UserAccessInfoDTO userAccess : myFollowList)
//		            	if(userAccess.getStatus() != ReportStatus.VIEW.getName()){
//		            	     inv.addModel("displayStatus", true);
//		            	     break;
//		            	}
//	            }
//            }
        }
        
        return null;
    }
    
}