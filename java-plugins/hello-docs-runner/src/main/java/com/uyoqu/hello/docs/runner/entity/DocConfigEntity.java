package com.uyoqu.hello.docs.runner.entity;

import com.sun.deploy.ui.AppInfo;
import com.uyoqu.hello.docs.core.vo.DtoVo;
import com.uyoqu.hello.docs.core.vo.MenuGroupVo;
import com.uyoqu.hello.docs.core.vo.ServiceVo;
import com.uyoqu.hello.docs.core.vo.TimelineVo;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: yoqu
 * @date: 2019-01-25
 * @email: yoqulin@qq.com
 **/
@Builder
@Data
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

  //app配置
  private AppInfo appInfo;
}
