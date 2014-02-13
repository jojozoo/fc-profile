package com.orientalcomics.profile.web.interceptors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import net.paoding.rose.web.Invocation;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.orientalcomics.profile.OcProfileAjaxCodes;
import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.dao.SystemPageDAO;
import com.orientalcomics.profile.biz.logic.UserService;
import com.orientalcomics.profile.biz.logic.UserTokenService;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.biz.model.UserToken;
import com.orientalcomics.profile.core.base.BaseUtil;
import com.orientalcomics.profile.core.base.ClassUtils;
import com.orientalcomics.profile.core.base.HtmlPageImpl;
import com.orientalcomics.profile.core.base.NetUtils;
import com.orientalcomics.profile.core.base.RoseUtils;
import com.orientalcomics.profile.web.access.ProfileHostHolder;
import com.orientalcomics.profile.web.annotations.LoginRequired;

public class LoginInterceptor extends AbstractControllerInterceptorAdapter {

    @Autowired
    private ProfileHostHolder profileHostHolder;

    @Autowired
    private UserService       userService;

    @Autowired
    private UserTokenService  userTokenService;

//    @Autowired
//    private RewardItemService rewardItemService;
//
//    @Autowired
//	private ScoreCountDAO scoreCountDAO;
//    
//    @Autowired
//    private ScoreService scoreService;
    
    @Autowired
    private SystemPageDAO systemPageDAO;
    
//    @Autowired
//    private RewardFlowerDAO rewardFlowerDAO;

    @Override
    public PriorityType getPriorityType() {
        return PriorityType.Login;
    }

    
    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);


	public LoginInterceptor(){
		setPriority(1000);
	}

	@Override
	public Class<? extends Annotation> getRequiredAnnotationClass() {
		return LoginRequired.class;
	}

	@Override
	public List<Class<? extends Annotation>> getRequiredAnnotationClasses() {
	        Class<LoginRequired> clazz = LoginRequired.class;
	        List<Class<? extends Annotation>> list = new ArrayList<Class<? extends Annotation>>(2);
	        list.add(clazz);
	        return list;
	}

    @Override
    protected Object before(Invocation inv) throws Exception {
    	
    	logger.info("######### comming before");
        User user = null;
        
        // loginas
        Object canLoginAs = inv.getRequest().getSession().getAttribute("$profile.canloginas");
        if (canLoginAs != null) {
            int userId = NumberUtils.toInt(canLoginAs.toString());
            if (userId > 0) {
                user = userService.query(userId);// 登录成功
            }
        }
        // normal
        if (user == null) {
            UserToken requestToken = NetUtils.getUserTokenFromCookie(inv.getRequest());
            int userId = requestToken.getUserId();

            if (userId > 0 && userTokenService.isValid(requestToken)) {
                user = userService.query(userId);// 登录成功
            }
        }

        profileHostHolder.setUser(user);
        inv.addModel("_user", user);

//        // add by hao.zhang 增加勋章
//        if (user != null && user.getVirtualRewardItemId() != 0) {
//            RewardItem item = rewardItemService.query(user.getVirtualRewardItemId());
//            inv.addModel("_userVirtualRewardItem", item);
//        }
//
//        int owner = RoseUtils.getResourceOwner(inv);
//        if (owner > 0) {
//            User _owner = userService.query(owner);
//            inv.addModel("_owner", _owner);
//            inv.addModel("_owner_is_leader", userService.countSubordinates(owner) > 0);
//            
//            ScoreCount scoreCount = scoreCountDAO.queryScoreCountByUserId(_owner.getId());
//            if (scoreCount != null)
//            	inv.addModel("_owner_score_count", scoreCount.getTotalScore());
//            	
//            SystemPage scoreRankUrl = scoreService.getScoreUrl(_owner.getId());
//            if (null == scoreRankUrl) {
//            	scoreRankUrl = systemPageDAO.getSystemPageById(1);
//            }
//            inv.addModel("_owner_score_rank", scoreRankUrl);
//            inv.addModel("_owner_reward_flower", rewardFlowerDAO.getRewardFlower(_owner.getId(), 3));
//        }
        
        if (user == null && ClassUtils.isAnnotationPresentOnMethodAndClass(inv, LoginRequired.class)) {
            HtmlPageImpl page = (HtmlPageImpl) RoseUtils.currentHtmlMessages(inv);
            page.code(OcProfileAjaxCodes.NEED_LOGIN);
            
            if(logger.isDebugEnabled()){
            	logger.debug("未登录，被拦截");
            }
            return "r:" + "/login?rf=r&domain="
            + OcProfileConstants.PROFILE_MAIN_DOMAIN + "&origURL="
            + BaseUtil.getResourceFullLocation(inv.getRequest());
        }
        logger.info("######### comming before 111");
        return true;
    }
}