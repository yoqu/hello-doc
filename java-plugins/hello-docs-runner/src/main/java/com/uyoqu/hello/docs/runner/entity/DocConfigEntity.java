package com.uyoqu.hello.docs.runner.entity;

import com.uyoqu.hello.docs.core.gen.AbstractScanGen;
import com.uyoqu.hello.docs.core.gen.ApiInfo;
import com.uyoqu.hello.docs.core.gen.RunningGen;
import com.uyoqu.hello.docs.core.vo.DtoVo;
import com.uyoqu.hello.docs.core.vo.MenuGroupVo;
import com.uyoqu.hello.docs.core.vo.ServiceVo;
import com.uyoqu.hello.docs.core.vo.TimelineVo;
import com.uyoqu.hello.docs.runner.config.DocConfig;
import com.uyoqu.hello.docs.runner.controller.ApiController;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: yoqu
 * @date: 2019-01-25
 * @email: yoqulin@qq.com
 **/
@Slf4j
@Data
@Configuration
public class DocConfigEntity {
    //菜单
    protected List<MenuGroupVo> menuList = new ArrayList<MenuGroupVo>();
    //基础信息
    protected Map<String, Object> basicMap = new HashMap<String, Object>();
    //实体
    protected Map<String, DtoVo> dtoMap = new HashMap<String, DtoVo>();
    //服务
    protected Map<String, ServiceVo> serviceMap = new HashMap<String, ServiceVo>();
    protected List<TimelineVo> timelineList = new ArrayList<TimelineVo>();
    //api配置
    protected ApiInfo apiInfo;

    public static DocConfigEntity build(DocConfig docConfig) {
        try {
            AbstractScanGen gen = new RunningGen();
            gen.init(docConfig.getApiInfo());
            gen.scanPakcages(docConfig.getScanPackage());
            gen.handler();
            DocConfigEntity entity = new DocConfigEntity();
            entity.setApiInfo(gen.getBasicInfo());
            entity.setBasicMap(gen.getBasicMap());
            entity.setDtoMap(gen.getDtoMap());
            entity.setMenuList(gen.getMenuList());
            entity.setServiceMap(gen.getServiceMap());
            entity.setTimelineList(gen.getTimelineList());
            return entity;
        } catch (Exception e) {
            log.error("hello doc generate fail.");
            throw new RuntimeException(e);
        }
    }

    @Bean
    public ApiController controller() {
        return new ApiController();
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
