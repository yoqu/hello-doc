package com.uyoqu.hello.docs.core.annotation;

import com.uyoqu.hello.docs.core.definition.RequestType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Inherited
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiParam {

  boolean required() default true;

  /**
   * @return 参数名称
   */
  String name() default "";

  /**
   * @return 类型
   */
  String type() default "String";

  /**
   * @return 描述
   */
  String desc() default "";

  /**
   * @return 备注
   */
  String remark() default "";

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
}

