package com.uyoqu.hello.docs.runner.config;

import com.uyoqu.hello.docs.runner.entity.DocConfigEntity;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author: yoqu
 * @date: 2019-01-25
 * @email: yoqulin@qq.com
 **/
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DocConfigEntity.class)
public @interface EnableHelloDocConfig {
}
