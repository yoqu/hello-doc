package com.iflytek.cbg.hello.docs.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lanjian
 */
@java.lang.annotation.Target({ElementType.TYPE})
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiDTO {

    String cnName();

    String enName();

    String desc() default "";

    String group() default "";

    String doc() default "";
}
