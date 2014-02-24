package com.orientalcomics.profile.web.params;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.ParamValidator;
import net.paoding.rose.web.paramresolver.ParamMetaData;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.orientalcomics.profile.util.text.html.HtmlFilterUtils;
import com.orientalcomics.profile.util.text.html.HtmlFilterUtils.FilterType;
import com.orientalcomics.profile.web.annotations.ProfileHtmlCorrect;
import com.orientalcomics.profile.web.annotations.ProfileHtmlEscape;
import com.orientalcomics.profile.web.annotations.ProfileHtmlPure;


@Service
public class HtmlFilterValidator implements ParamValidator {
    @Override
    public boolean supports(ParamMetaData metaData) {
        if (metaData.getParamType() == String.class || metaData.getParamType() == String[].class) {
            return metaData.isAnnotationPresent(ProfileHtmlCorrect.class) //
                    || metaData.isAnnotationPresent(ProfileHtmlPure.class)//
                    || metaData.isAnnotationPresent(ProfileHtmlEscape.class);
        }
        return false;
    }

    @Override
    public Object validate(ParamMetaData metaData, Invocation inv, Object target, Errors errors) {
        if (target == null)
            return true;
        if (metaData.isAnnotationPresent(ProfileHtmlCorrect.class)) {// 富文本过滤
            target = handleStringType(target, FilterType.CORRECT_NORMAL);
        } else if (metaData.isAnnotationPresent(ProfileHtmlPure.class)) {// 纯文本过滤
            target = handleStringType(target, FilterType.REMOVE_ALL_TAGS);
        } else if (metaData.isAnnotationPresent(ProfileHtmlEscape.class)) {// 转义
            target = handleStringType(target, FilterType.ESCAPE_ALL);
        } else {
            return true;
        }
        inv.changeMethodParameter(metaData.getIndex(), target);
        return true;
    }

    /**
     * 处理字符串类型 暂时只支持String/String[]类型
     * 
     * @param target
     * @param type
     * @return
     */
    private Object handleStringType(Object target, FilterType type) {
        if (target instanceof String) // 暂时只支持String/String[]类型
            target = coreFilter((String) target, type);
        else if (target instanceof String[]) {// String[]类型
            String[] result = (String[]) target;
            for (int i = 0; i < result.length; i++) {
                result[i] = coreFilter(result[i], type);
            }
            target = result;
        }
        return target;
    }

    private String coreFilter(String target, FilterType type) {
        return HtmlFilterUtils.filter(type, target, target);
    }

}
