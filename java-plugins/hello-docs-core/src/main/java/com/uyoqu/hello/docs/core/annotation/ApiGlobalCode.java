package com.uyoqu.hello.docs.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * 全局返回码
 */
@java.lang.annotation.Target({ElementType.TYPE})
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiGlobalCode {
  String codeKey() default "code";

  String msgKey() default "msg";

  String value() default "";
}
