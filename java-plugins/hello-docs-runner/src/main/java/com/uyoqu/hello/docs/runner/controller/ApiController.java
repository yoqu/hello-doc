package com.uyoqu.hello.docs.runner.controller;

import com.uyoqu.hello.docs.core.vo.DtoVo;
import com.uyoqu.hello.docs.core.vo.MenuGroupVo;
import com.uyoqu.hello.docs.core.vo.ServiceVo;
import com.uyoqu.hello.docs.core.vo.TimelineVo;
import com.uyoqu.hello.docs.runner.entity.DocConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author: yoqu
 * @date: 2019-01-15
 * @email: yoqulin@qq.com
 **/
@RestController
@RequestMapping("/hello-doc")
public class ApiController {

  @Autowired(required = false)
  DocConfigEntity configEntity;

  @GetMapping("/basic_definition.json")
  public Map<String, Object> index() {
    if (configEntity != null) {
      return configEntity.getBasicMap();
    }
    return null;
  }

  @GetMapping("/dto.json")
  public Map<String, DtoVo> dto() {
    if (configEntity != null) {
      return configEntity.getDtoMap();
    }
    return null;
  }

  @GetMapping("/nav_menu.json")
  public List<MenuGroupVo> navMenu() {
    if (configEntity != null) {
      return configEntity.getMenuList();
    }
    return null;
  }

  @GetMapping("/service.json")
  public Map<String, ServiceVo> service() {
    if (configEntity != null) {
      return configEntity.getServiceMap();
    }
    return null;
  }

  @GetMapping("/timelines.json")
  public List<TimelineVo> timelines() {
    if (configEntity != null) {
      return configEntity.getTimelineList();
    }
    return null;
  }
}
