package com.uyoqu.hello.docs.runner.entity;

import com.uyoqu.hello.docs.core.gen.ScanGenerate;
import com.uyoqu.hello.docs.core.gen.ApiInfo;
import com.uyoqu.hello.docs.core.gen.RunningGenerate;
import com.uyoqu.hello.docs.core.vo.DtoVO;
import com.uyoqu.hello.docs.core.vo.MenuGroupVO;
import com.uyoqu.hello.docs.core.vo.ServiceVO;
import com.uyoqu.hello.docs.core.vo.TimelineVO;
import com.uyoqu.hello.docs.runner.config.DocConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: yoqu
 * @date: 2019-01-25
 * @email: yoqulin@qq.com
 **/
@Slf4j
@Data
public class DocConfigEntity {
    //菜单
    protected List<MenuGroupVO> menuList = new ArrayList<MenuGroupVO>();
    //基础信息
    protected Map<String, Object> basicMap = new HashMap<String, Object>();
    //实体
    protected Map<String, DtoVO> dtoMap = new HashMap<String, DtoVO>();
    //服务
    protected Map<String, ServiceVO> serviceMap = new HashMap<String, ServiceVO>();
    protected List<TimelineVO> timelineList = new ArrayList<TimelineVO>();
    //api配置
    protected ApiInfo apiInfo;

    public static DocConfigEntity build(DocConfig docConfig) {
        try {
            ScanGenerate gen = new RunningGenerate();
            gen.init(docConfig.getApiInfo());
            gen.scanPackages(docConfig.getScanPackage());
            gen.handler();
            log.info("hello doc generate success. visit uri:{}", "hello-docs/");
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
}
