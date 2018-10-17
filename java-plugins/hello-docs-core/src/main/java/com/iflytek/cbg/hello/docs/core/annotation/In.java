package com.iflytek.cbg.hello.docs.core.annotation;

import java.lang.annotation.*;

/**
 * @author yoqu
 * @date 2018/4/18 - 10:51
 */

@Inherited
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface In {

    boolean required() default true;

    //参数名称
    String param();

    //类型
    String type() default "String";

    //描述
    String desc() default "";

    //备注
    String remark() default "";
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

