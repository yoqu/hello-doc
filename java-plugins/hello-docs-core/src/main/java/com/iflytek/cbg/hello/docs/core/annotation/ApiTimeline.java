package com.iflytek.cbg.hello.docs.core.annotation;

import java.lang.annotation.*;

/**
 * API时间轴。
 * Created by zhpeng2 on 2017/9/29.
 */
@Inherited
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiTimeline {
    /**
     * 时间轴列表。
     */
    Timeline[] value();
}
