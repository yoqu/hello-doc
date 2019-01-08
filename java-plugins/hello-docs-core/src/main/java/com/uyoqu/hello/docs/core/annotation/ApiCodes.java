package com.uyoqu.hello.docs.core.annotation;

import java.lang.annotation.*;

/**
 * 用于方法层的返回码
 */
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiCodes {
    /**
     * @return 返回码定义
     */
    ApiCode[] value();
}
