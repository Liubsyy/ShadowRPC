package com.liubs.shadowrpc.base.util;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author Liubsyy
 * @date 2024/1/15
 */
public class PackageScanUtil {

    public static Set<Class<?>> scanClasses(String packageName, Class<? extends Annotation> annotation) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getTypesAnnotatedWith(annotation);
    }
}
