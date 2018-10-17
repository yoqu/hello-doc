package com.uyoqu.hello.docs.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lanjian
 */
@java.lang.annotation.Target({ElementType.TYPE,ElementType.METHOD})
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiServiceDocs {

    String cnName();

    String serviceName() default "";

    String version();

    String[] methods() default "POST";

    String desc() default "";

    String group() default "";

    String doc() default "";

    /**
     * 完成开发进度百分比,默认为0,最高100
     *
     * @return
     */
    int finish() default 0;
}
