package com.uyoqu.hello.docs.core.annotation;

import java.lang.annotation.*;

/**
 * 时间轴信息。
 */
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Timeline {
  /**
   * @return 时间。时间轴会按时间字段排序，格式：yyyy-MM-dd。
   */
  String time();

  /**
   * @return 时间轴内容。
   */
  String content();
}
