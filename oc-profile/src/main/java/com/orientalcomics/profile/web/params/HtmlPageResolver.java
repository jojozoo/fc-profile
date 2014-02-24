package com.orientalcomics.profile.web.params;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.paramresolver.ParamMetaData;
import net.paoding.rose.web.paramresolver.ParamResolver;

import org.springframework.stereotype.Service;

import com.orientalcomics.profile.core.base.HtmlPage;
import com.orientalcomics.profile.core.base.RoseUtils;

@Service
public class HtmlPageResolver implements ParamResolver {

    @Override
    public boolean supports(ParamMetaData metaData) {
        return HtmlPage.class.isAssignableFrom(metaData.getParamType());
    }

    @Override
    public Object resolve(Invocation inv, ParamMetaData metaData) throws Exception {
        return RoseUtils.currentHtmlMessages(inv);
    }

}
