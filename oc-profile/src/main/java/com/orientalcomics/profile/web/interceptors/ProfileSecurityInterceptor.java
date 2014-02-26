package com.orientalcomics.profile.web.interceptors;

import java.lang.annotation.Annotation;
import java.util.List;

import net.paoding.rose.web.Invocation;

import org.springframework.beans.factory.annotation.Autowired;

import com.orientalcomics.profile.OcProfileAjaxCodes;
import com.orientalcomics.profile.biz.logic.ProfileSecurityService;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.constants.ProfileAction;
import com.orientalcomics.profile.core.base.ClassUtils;
import com.orientalcomics.profile.core.base.HtmlPageImpl;
import com.orientalcomics.profile.core.base.RoseUtils;
import com.orientalcomics.profile.web.access.ProfileHostHolder;
import com.orientalcomics.profile.web.annotations.AjaxJson;
import com.orientalcomics.profile.web.annotations.ProfileRootSecurity;
import com.orientalcomics.profile.web.annotations.ProfileSecurity;

public class ProfileSecurityInterceptor extends AbstractControllerInterceptorAdapter {

    @Autowired
    private ProfileHostHolder      profileHostHolder;
    
    @Autowired
    private ProfileSecurityService profileSecurityService;

    @Override
    public int getPriority() {
        return 100001;
    }


	@Override
	protected PriorityType getPriorityType() {
		return PriorityType.Security;
	}
	
    @Override
    protected List<Class<? extends Annotation>> getRequiredAnnotationClasses() {
        return createList(2).add(ProfileSecurity.class).add(ProfileRootSecurity.class).getList();
    }

    @Override
    protected Object before(Invocation inv) throws Exception {
        User user = profileHostHolder.getUser();
        $: {
            if (user == null) { // 当前用户没有登录，目前禁止匿名用户访问
                break $;
            }

            ProfileRootSecurity profileRootSecurity = ClassUtils.getAnnotationFromMethodAndClass(inv, ProfileRootSecurity.class);
            if (profileRootSecurity != null) {// 必须是Root
                if (!profileSecurityService.isRoot(user.getId())) {
                    break $;
                }
                return true;// 是Root
            }
            ProfileSecurity profileSecurity = ClassUtils.getAnnotationFromMethodAndClass(inv, ProfileSecurity.class);
            if (profileSecurity == null) {// 没有权限控制
                return true;
            }

            ProfileAction action = profileSecurity.value();
            if (!profileSecurityService.hasPermission(action, user.getId(), RoseUtils.getResourceOwner(inv))) { // 如果没有权限
                break $;
            }
            return true;
        }
        // 没有权限
        if (ClassUtils.isAnnotationPresentOnMethodAndClass(inv, AjaxJson.class)) {
            HtmlPageImpl page = (HtmlPageImpl) RoseUtils.currentHtmlMessages(inv);
            page.code(OcProfileAjaxCodes.NO_AUTH);
            return false;
        } else {
            return "@Oops，你没有权限哦~~~";
        }
    }

}
