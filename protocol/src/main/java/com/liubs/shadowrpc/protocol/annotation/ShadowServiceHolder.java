package com.liubs.shadowrpc.protocol.annotation;

import java.lang.annotation.Annotation;

/**
 * @author Liubsyy
 * @date 2023/12/18 10:51 PM
 **/
public class ShadowServiceHolder <T extends Annotation> {

    private T annotation;
    private Class<?> classz;

    public ShadowServiceHolder(T annotation, Class<?> classz) {
        this.annotation = annotation;
        this.classz = classz;
    }

    public T getAnnotation() {
        return annotation;
    }

    public void setAnnotation(T annotation) {
        this.annotation = annotation;
    }

    public Class<?> getClassz() {
        return classz;
    }

    public void setClassz(Class<?> classz) {
        this.classz = classz;
    }

    @Override
    public String toString() {
        return "ShadowServiceHolder{" +
                "annotation=" + annotation +
                ", classz=" + classz +
                '}';
    }
}
