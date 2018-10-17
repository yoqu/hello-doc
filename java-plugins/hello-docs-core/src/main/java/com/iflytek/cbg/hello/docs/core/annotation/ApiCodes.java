package com.iflytek.cbg.hello.docs.core.annotation;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiCodes {
    /**
     * 返回码定义
     * @return
     */
    ApiCode[] value();
}
