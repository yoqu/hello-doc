package com.uyoqu.hello.docs.core.annotation;

import com.uyoqu.hello.docs.core.definition.RequestType;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * API基础定义。
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiField {
  /**
   * @return 参数名称，如果为空则获取字段名称。
   */
  String param() default "";

  /**
   * @return 类型，如果为空则获取字段的Java类型。
   */
  String type() default "";

  /**
   * @return 描述
   */
  String desc() default "";

  /**
   * @return 备注
   */
  String remark() default "";

  /**
   * @return 是否需要
   */
  boolean required() default false;

  /**
   * @return 外链
   */
  String link() default "";

  /**
   * @return 例子
   */
  String example() default "";

  /**
   * @return 参数请求类型
   */
  RequestType reqType() default RequestType.PARAM;

  /**
   * 在某些group下才进行展示
   */
  Class<? extends Annotation>[] showGroup() default {};
}
