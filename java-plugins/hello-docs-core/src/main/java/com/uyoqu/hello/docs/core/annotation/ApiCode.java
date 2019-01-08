package com.uyoqu.hello.docs.core.annotation;

import java.lang.annotation.*;

/**
 * 返回码定义
 */
@Inherited
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiCode {

    String code();

    String msg();
}
