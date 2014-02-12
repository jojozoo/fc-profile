package com.orientalcomics.profile.core.base;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import net.paoding.rose.web.Invocation;

public class ClassUtils {
    public static boolean isAnnotationPresentOnMethod(Invocation inv, Class<? extends Annotation> annotationClass) {
        Method actionMethod = inv.getMethod();
        return actionMethod.isAnnotationPresent(annotationClass);
    }

    public static boolean isAnnotationPresentOnMethodAndClass(Invocation inv, Class<? extends Annotation> annotationClass) {
        Method actionMethod = inv.getMethod();
        Class<?> controllerClazz = inv.getControllerClass();
        if (actionMethod.isAnnotationPresent(annotationClass))
            return true;
        if (controllerClazz.isAnnotationPresent(annotationClass)) {
            return true;
        }
        return false;
    }

    public static <T extends Annotation> T getAnnotationFromMethod(Invocation inv, Class<T> annotationClass) {
        Method actionMethod = inv.getMethod();
        return actionMethod.getAnnotation(annotationClass);
    }

    public static <T extends Annotation> T getAnnotationFromMethodAndClass(Invocation inv, Class<T> annotationClass) {
        T result = null;
        Method actionMethod = inv.getMethod();
        Class<?> controllerClazz = inv.getControllerClass();
        if ((result = actionMethod.getAnnotation(annotationClass)) != null)
            return result;
        if ((result = controllerClazz.getAnnotation(annotationClass)) != null) {
            return result;
        }
        return result;
    }
}
