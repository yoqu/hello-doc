package com.uyoqu.hello.docs.runner.controller;

import com.uyoqu.hello.docs.core.vo.DtoVO;
import com.uyoqu.hello.docs.core.vo.MenuGroupVO;
import com.uyoqu.hello.docs.core.vo.ServiceVO;
import com.uyoqu.hello.docs.core.vo.TimelineVO;
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
@RequestMapping("/hello-docs/static/data")
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
  public Map<String, DtoVO> dto() {
    if (configEntity != null) {
      return configEntity.getDtoMap();
    }
    return null;
  }

  @GetMapping("/nav_menu.json")
  public List<MenuGroupVO> navMenu() {
    if (configEntity != null) {
      return configEntity.getMenuList();
    }
    return null;
  }

  @GetMapping("/service.json")
  public Map<String, ServiceVO> service() {
    if (configEntity != null) {
      return configEntity.getServiceMap();
    }
    return null;
  }

  @GetMapping("/timelines.json")
  public List<TimelineVO> timelines() {
    if (configEntity != null) {
      return configEntity.getTimelineList();
    }
    return null;
  }
}
