package com.uyoqu.hello.docs.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@java.lang.annotation.Target({ElementType.TYPE, ElementType.METHOD})
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiServiceDocs {

  /**
   * @return 中文名
   */
  String cnName();

  /**
   * @return 服务名
   */
  String serviceName() default "";

  /**
   * @return 版本号
   */
  String version();

  /**
   * @return 请求方法[GET/POST/PUT/DELETE]
   */
  String[] methods() default "POST";

  /**
   * @return 简介
   */
  String desc() default "";

  /**
   * @return 菜单一级分组
   */
  String group() default "";

  /**
   * @return md文档相对路径
   */
  String doc() default "";

  /**
   * @return 完成开发进度百分比, 默认为0, 最高100
   */
  int finish() default 0;
}
