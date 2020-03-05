package com.uyoqu.hello.docs.core.annotation;

import java.lang.annotation.*;

/**
 * API时间轴。
 */
@Inherited
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiTimeline {
    /**
     * @return 时间轴列表。
     */
    Timeline[] value() ;
}
