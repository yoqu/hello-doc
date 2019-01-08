package com.uyoqu.hello.docs.core.annotation;

import java.lang.annotation.*;

/**
 * API基础定义。
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiBasicFiled {
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
}
