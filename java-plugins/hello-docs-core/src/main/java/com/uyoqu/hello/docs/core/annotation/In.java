package com.uyoqu.hello.docs.core.annotation;

import java.lang.annotation.*;


@Inherited
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface In {

  boolean required() default true;

  /**
   * @return 参数名称
   */
  String param() default "";

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
}

