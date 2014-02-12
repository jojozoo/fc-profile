package com.orientalcomics.profile.web.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.orientalcomics.profile.constants.ProfileAction;


/**
 * 权限的annotation，根据不同的功能点和用户权限来判断可不可以访问
 * @author DanyZhang
 *
 */

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ProfileSecurity {
    ProfileAction value();// 功能点

    @Target({ ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @interface AND {
        ProfileSecurity[] value();
    }

    @Target({ ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @interface OR {
        ProfileSecurity[] value();
    }
}
