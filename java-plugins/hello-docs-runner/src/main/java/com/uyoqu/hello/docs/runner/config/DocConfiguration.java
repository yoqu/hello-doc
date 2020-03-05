package com.uyoqu.hello.docs.runner.config;

import com.uyoqu.hello.docs.runner.entity.DocConfigEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.concurrent.TimeUnit;

@Configuration
@ComponentScan(basePackages = {"com.uyoqu.hello.docs.runner.controller"})
public class DocConfiguration {

    @Bean
    public DocConfigEntity configEntity(DocConfig docConfig) {
        return DocConfigEntity.build(docConfig);
    }

    @Configuration
    static class WebConfig extends WebMvcConfigurerAdapter {

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            super.addResourceHandlers(registry);
            registry
                    .addResourceHandler("/hello-docs/**")
                    .addResourceLocations("classpath:/META-INF/resources/hello-docs/")
                    .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());
        }
    }
}
