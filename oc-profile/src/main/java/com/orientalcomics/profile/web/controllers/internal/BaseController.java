package com.orientalcomics.profile.web.controllers.internal;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.InvocationUtils;
import net.paoding.rose.web.annotation.Ignored;

import org.springframework.beans.factory.annotation.Autowired;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.logic.ProfileConfigHelper;
import com.orientalcomics.profile.biz.logic.ProfileConfigs.PageSizeConfigView;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.core.annotations.IgnorePassportValidation;
import com.orientalcomics.profile.core.annotations.IgnoreUserStatusImportedValidation;
import com.orientalcomics.profile.core.annotations.IgnoreUserStatusValidation;
import com.orientalcomics.profile.util.logging.ILogger;
import com.orientalcomics.profile.util.logging.ProfileLogger;
import com.orientalcomics.profile.web.access.ProfileHostHolder;
import com.orientalcomics.profile.web.constants.AjaxType;

@Ignored
@IgnorePassportValidation
@IgnoreUserStatusImportedValidation
@IgnoreUserStatusValidation
public abstract class BaseController implements OcProfileConstants {
    protected ILogger             LOG = ProfileLogger.getLogger(this.getClass());
    @Autowired
    protected ProfileConfigHelper configHelper;

    @Autowired
    ProfileHostHolder             profileHostHolder;

    protected boolean isLogined() {
        return profileHostHolder.getUser() != null;
    }

    protected User currentUser() {
        return profileHostHolder.getUser();
    }

    protected int currentUserId() {
        User user = currentUser();
        return user == null ? 0 : user.getId();
    }

    protected int getPageSize(PageSizeConfigView configView) {
        Invocation inv = InvocationUtils.getCurrentThreadInvocation();
        int result;
        if (configView == null) {
            configView = PageSizeConfigView.DEFAULT;
        }
        result = configHelper.getValue(configView);

        if (result <= 0) {
            if (configView.defaultValue() != null
                    && configView.defaultValue() > 0) {
                result = configView.defaultValue();
            } else {
                result = 20;
            }
        }
        if (configView.maxValue() != null && configView.maxValue() > 0) {
            if (result > configView.maxValue()) {
                result = configView.maxValue();
            }
        }
        inv.addModel("_pagesize_", result);
        return result;
    }

    protected int getDefaultPageSize() {
        return getPageSize(null);
    }

    /**
     * 根据total和pagesize，验证curpage是否超出范围，如果超出，返回最后一页
     */
    protected int checkAndReturnPage(final int curPage, final int total,
            final int pageSize) {
        if (pageSize <= 0) {
            return curPage;
        }
        if (curPage * pageSize >= total) { // 超出了，定位到最后一页
            return total <= 0 ? 0 : ((total - 1) / pageSize);
        }
        return curPage;
    }

    protected boolean isAjaxJson(Invocation inv) {
        return AjaxType.JSON == AjaxUtils.getRequestAjaxType(inv);
    }
}
