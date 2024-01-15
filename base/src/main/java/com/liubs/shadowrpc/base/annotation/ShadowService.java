package com.liubs.shadowrpc.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务类注解
 * @author Liubsyy
 * @date 2023/12/18 10:40 PM
 **/

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ShadowService {

    /**
     * 服务名字
     */
    String serviceName();

    /**
     * 是否同步调用
     */
    boolean sync() default true;

}