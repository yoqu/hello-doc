package com.iflytek.cbg.hello.docs.core.annotation;

/**
 * Created by lanjian
 */

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@java.lang.annotation.Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiOutDTO {
    Class clazz();

    /**
     * 返回值是否
     *
     * @return
     */
    boolean generics() default false;

    /**
     * 泛型类型.
     *
     * @return
     */
    Class genericType() default Object.class;
}
