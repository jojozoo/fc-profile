package com.orientalcomics.profile.web.listeners;

import java.util.Arrays;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.StringUtils;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.constants.ProfileAction;
import com.orientalcomics.profile.constants.ProfileActionCategory;
import com.orientalcomics.profile.constants.ProfilePerfProjectWeight;
import com.orientalcomics.profile.constants.ProfileRelation;
import com.orientalcomics.profile.constants.status.WeeklyReportStatus;

public class InitConstantsListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        // 字面常量
        sc.setAttribute("_empty_headpic_url", OcProfileConstants.EMPTY_MAIN_URL);
        // 枚举常量
        // _<name>_arr, _<name>_array:数组,如ProfileAction.values()
        // _<name>_list:列表，如Arrays.asList(ProfileAction.values())
        // _<name>$<field>：具体的枚举值，如_action$VIEW_USER_KPI或_action$view_user_kpi表示ProfileAction.VIEW_USER_KPI
        initEnum(sc,"action_category",ProfileActionCategory.values());
        initEnum(sc,"action",ProfileAction.values());
        initEnum(sc,"relation",ProfileRelation.values());
        initEnum(sc,"project_weight",ProfilePerfProjectWeight.values());
        
        // status
        initEnum(sc,"weeklyreport_status",WeeklyReportStatus.values());
    }

    private <E extends Enum<E>> void initEnum(ServletContext sc, String name, Enum<E>[] inputs) {
        if (name == null) {
            if(inputs.length == 0){
                return;
            }
            name = convertCamelToUnderscoreName(inputs[0].getClass().getSimpleName());
        }
        String prefix = "_" + name;
        sc.setAttribute(prefix + "_arr", inputs);
        sc.setAttribute(prefix + "_array", inputs);
        sc.setAttribute(prefix + "_list", Arrays.asList(inputs));
        for (Enum<E> input : inputs) {
            sc.setAttribute(prefix + "$"+input.name(), input);
            sc.setAttribute(prefix + "$"+convertCamelToUnderscoreName(input.name()), input);
        }
    }

    private String convertCamelToUnderscoreName(String name) {
        if (name == null) {
            return null;
        }
        String[] items = StringUtils.splitByCharacterTypeCamelCase(name);
        return StringUtils.lowerCase(StringUtils.join(items, "_"));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
