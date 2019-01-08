package com.uyoqu.hello.docs.core.annotation;

import java.lang.annotation.*;

/**
 * @author yoqu
 * @date 2018/4/18 - 10:52
 */
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Out {
  /**
   * @return 参数名称
   */
  String param();

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

  boolean required() default true;

  /**
   * @return 外链
   */
  String link() default "";

  /**
   * @return 例子
   */
  String example() default "";
}
