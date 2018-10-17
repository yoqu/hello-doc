package com.uyoqu.hello.docs.core.annotation;

import java.lang.annotation.*;

/**
 * API基础定义。
 * Created by zhpeng2 on 2017/9/29.
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiBasicFiled {
    /**
     * 参数名称，如果为空则获取字段名称。
     */
    String param() default "";

    /**
     * 类型，如果为空则获取字段的Java类型。
     */
    String type() default "";

    /**
     * 描述
     */
    String desc() default "";

    /**
     * 备注
     */
    String remark() default "";

    /**
     * 是否需要
     *
     * @return
     */
    boolean required() default false;

    /**
     * 外链
     *
     * @return
     */
    String link() default "";

    /**
     * 例子
     * @return
     */
    String example() default "";
}
