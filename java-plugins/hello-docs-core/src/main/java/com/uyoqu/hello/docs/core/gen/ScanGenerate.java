package com.uyoqu.hello.docs.core.gen;

import com.uyoqu.hello.docs.core.annotation.ApiCode;
import com.uyoqu.hello.docs.core.annotation.ApiCodes;
import com.uyoqu.hello.docs.core.annotation.ApiDTO;
import com.uyoqu.hello.docs.core.annotation.ApiGlobalCode;
import com.uyoqu.hello.docs.core.annotation.ApiService;
import com.uyoqu.hello.docs.core.gen.resolver.ApiInResolver;
import com.uyoqu.hello.docs.core.gen.resolver.ApiOutResolver;
import com.uyoqu.hello.docs.core.gen.resolver.DataFieldsResolver;
import com.uyoqu.hello.docs.core.gen.resolver.HeaderResolver;
import com.uyoqu.hello.docs.core.gen.resolver.Resolver;
import com.uyoqu.hello.docs.core.gen.resolver.TimelineResolver;
import com.uyoqu.hello.docs.core.vo.CodeVO;
import com.uyoqu.hello.docs.core.vo.DtoVO;
import com.uyoqu.hello.docs.core.vo.MenuGroupVO;
import com.uyoqu.hello.docs.core.vo.MenuVO;
import com.uyoqu.hello.docs.core.vo.ReqDataVO;
import com.uyoqu.hello.docs.core.vo.ServiceVO;
import com.uyoqu.hello.docs.core.vo.TimelineVO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: yoqu
 * @date: 2018/10/18
 * @email: yoqulin@qq.com
 **/
public class ScanGenerate implements Generate {


  public static final Logger log = LoggerFactory.getLogger(ScanGenerate.class);

  private final Resolver headerResolver = new HeaderResolver();
  private final Resolver apiOutResolver = new ApiOutResolver();
  private final ApiInResolver apiInResolver = new ApiInResolver();
  private final TimelineResolver timelineResolver = new TimelineResolver();
  // 待处理的类
  protected Set<Class<?>> basicSet = new HashSet<>();
  protected Set<Class<?>> dtoSet = new HashSet<>();
  protected Set<Class<?>> serviceSet = new HashSet<>();
  // 结果集
  protected List<TimelineVO> timelineList = new ArrayList<>();
  protected Map<String, MenuGroupVO> menuTempMap = new HashMap<>(); // Key: 分组名称
  protected List<MenuGroupVO> menuList = new ArrayList<>();
  protected Map<String, Object> basicMap = new HashMap<>(); // Key: js file
  protected Map<String, DtoVO> dtoMap = new HashMap<>(); // // Key: js file
  protected Map<String, ServiceVO> serviceMap = new HashMap<>(); // // Key: js file
  //基本信息map
  protected ApiInfo basicInfo = new ApiInfo();

  public List<TimelineVO> getTimelineList() {
    return timelineList;
  }

  public List<MenuGroupVO> getMenuList() {
    return menuList;
  }

  public Map<String, Object> getBasicMap() {
    return basicMap;
  }

  public Map<String, DtoVO> getDtoMap() {
    return dtoMap;
  }

  public Map<String, ServiceVO> getServiceMap() {
    return serviceMap;
  }

  public ApiInfo getBasicInfo() {
    return basicInfo;
  }

  public void scanPackages(String... packageName) {
    Assert.notEmpty(packageName, "package");
    try {
      // 扫描基础定义
      ScanWrapper scan = new ScanWrapper(packageName, ApiGlobalCode.class);
      Set<Class<?>> set = scan.getClassSet();
      if (set != null) {
        basicSet.addAll(set);
      }
      // 扫描dto
      scan = new ScanWrapper(packageName, ApiDTO.class);
      set = scan.getClassSet();
      if (set != null) {
        dtoSet.addAll(set);
      }
      // 描述service
      scan = new ScanWrapper(packageName, ApiService.class, Controller.class, RestController.class);
      set = scan.getClassSet();
      if (set != null) {
        serviceSet.addAll(set);
      }
    } catch (Throwable e) {
      log.error("扫描包失败：" + e.getMessage(), e);
    }
  }

  public void init(ApiInfo basicInfo) {
    this.basicInfo = basicInfo;
    initMenu();
  }

  private void initMenu() {
    String groupName = "接口服务";
    MenuGroupVO groupVo = menuTempMap.get(groupName);
    if (groupVo == null) {
      groupVo = new MenuGroupVO();
      groupVo.setSort(1);
      groupVo.setTitle(groupName);
      groupVo.setSubs(new ArrayList<>());
      menuTempMap.put(groupName, groupVo);
    }
    groupName = "数据结构";
    MenuGroupVO groupVo2 = menuTempMap.get(groupName);
    if (groupVo2 == null) {
      groupVo2 = new MenuGroupVO();
      groupVo2.setSort(0);
      groupVo2.setTitle(groupName);
      groupVo2.setSubs(new ArrayList<>());
      menuTempMap.put(groupName, groupVo2);
    }
  }


  protected void check() {
    Assert.notNull(basicInfo, "apiInfo");
    Assert.notNull(basicInfo.getEnName(), "apiInfo enName");
  }


  private void resolve() throws GenException {
    for (Class<?> clazz : basicSet) {
      // 分析基础定义
      resolveBasic(clazz);
    }
    for (Class<?> clazz : dtoSet) {
      // 分析数据结构
      resolveDto(clazz);
    }
    for (Class<?> clazz : serviceSet) {
      // 分析接口服务
      resolveService(clazz);
    }
    if (basicInfo != null) {//基础信息
      basicMap.put("basic", basicInfo);
    }
  }

  private void resolveServiceMenu(AnnotatedElement element, ServiceVO serviceVo) {
    String groupName = "接口服务";
    MenuGroupVO groupVo = menuTempMap.get(groupName);
    MenuVO vo = new MenuVO();
    vo.setTitle(serviceVo.getServiceFullName());
    vo.setUrl("/service/" + serviceVo.getServiceFullName());
    vo.setName(serviceVo.getCnName());
    vo.setGroup(serviceVo.getGroup());
    vo.setFinish(serviceVo.getFinish());
    try {
      vo.setIsnew(timelineResolver.resolveIsNew(element));
    } catch (ParseException exception) {
      log.error("接口注解时间解析异常，异常位置：{}", element.toString());
    }
    groupVo.getSubs().add(vo);
  }

  public void handler() throws GenException {
    //检查输入的参数是否都有了
    check();
    // 分析
    resolve();
    // 菜单排序
    sortMenu();
  }

  private void sortMenu() {
    // 菜单排序
    for (Map.Entry<String, MenuGroupVO> vo : menuTempMap.entrySet()) {
      vo.getValue().getSubs().sort((o1, o2) -> {
        if (o1 == null && o2 == null)
          return 0;
        else if (o1 == null)
          return 1;
        else if (o2 == null)
          return -1;
        return ObjectUtils.compare(o1.getTitle(), o2.getTitle());
      });
      // 复制到列表
      menuList.add(vo.getValue());
      // 列表排序
      menuList.sort((o1, o2) -> {
        if (o1 == null && o2 == null)
          return 0;
        else if (o1 == null)
          return 1;
        else if (o2 == null)
          return -1;
        return ObjectUtils.compare(o1.getSort(), o2.getSort());
      });
    }
  }


  private void resolveBasic(Class<?> clazz) throws GenException {
    try {
      if (!clazz.isAnnotationPresent(ApiGlobalCode.class))
        return;
      ApiGlobalCode ann = clazz.getAnnotation(ApiGlobalCode.class);
      List<CodeVO> voList = new ArrayList<>();
      basicMap.put("code", voList);
      timelineList.addAll(timelineResolver.resolveBasic(clazz));
      if (clazz.isEnum()) {//枚举类型单独处理
        for (Object enumData : clazz.getEnumConstants()) {
          Method getCode = clazz.getMethod("get" + StringUtils.capitalize(ann.codeKey()));
          Method getValue = clazz.getMethod("get" + StringUtils.capitalize(ann.msgKey()));
          CodeVO vo = new CodeVO();
          vo.setCode(String.valueOf(getCode.invoke(enumData)));
          vo.setDesc(String.valueOf(getValue.invoke(enumData)));
          voList.add(vo);
        }
      } else {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
          CodeVO vo = new CodeVO();
          Object obj = field.get(field.getName());
          String code = (String) field.getType().getMethod("get" + StringUtils.capitalize(ann.codeKey())).invoke(obj);
          vo.setCode(code);
          String msg = (String) field.getType().getMethod("get" + StringUtils.capitalize(ann.msgKey())).invoke(obj);
          vo.setDesc(msg);
          voList.add(vo);
        }
      }
    } catch (Throwable e) {
      throw new GenException("分析基础定义异常，", clazz, e);
    }
  }

  private void resolveDto(Class<?> clazz) throws GenException {
    try {
      if (!clazz.isAnnotationPresent(ApiDTO.class))
        return;
      ApiDTO ann = clazz.getAnnotation(ApiDTO.class);
      DtoVO vo = new DtoVO();
      vo.setFullName(clazz.getName());
      vo.setCnName(ann.cnName());
      vo.setEnName(StringUtils.isNotBlank(ann.enName()) ? ann.enName() : clazz.getSimpleName());
      vo.setDesc(ann.desc());
      vo.setDoc(ann.doc());
      // 字段
      vo.setFields(DataFieldsResolver.find(clazz));
      // 时间轴
      List<TimelineVO> timelines = timelineResolver.resolve(clazz, vo);
      timelineList.addAll(timelines);
      vo.setTimelines(timelines);
      dtoMap.put(vo.getFullName(), vo);
      //处理菜单
      String groupName = "数据结构";
      MenuGroupVO groupVo = menuTempMap.get(groupName);
      MenuVO menuVO = new MenuVO();
      menuVO.setTitle(vo.getFullName());
      menuVO.setName(ann.cnName());
      menuVO.setUrl(timelineResolver.resolveUrl(vo));
      menuVO.setGroup(ann.group());
      menuVO.setIsnew(timelineResolver.resolveIsNew(clazz));
      groupVo.getSubs().add(menuVO);
    } catch (Throwable e) {
      throw new GenException("分析数据结构异常，", clazz, e);
    }
  }


  private List<CodeVO> resolveCodes(AnnotatedElement element) {
    if (element.isAnnotationPresent(ApiCodes.class)) {
      List<CodeVO> codeVOS = new ArrayList<>();
      ApiCodes def = element.getAnnotation(ApiCodes.class);
      for (ApiCode code : def.value()) {
        CodeVO codeVo = new CodeVO();
        codeVo.setCode(code.code());
        codeVo.setDesc(code.msg());
        codeVOS.add(codeVo);
      }
      return codeVOS;
    }
    return null;
  }

  private void resolveService(final Class<?> clazz) throws GenException {
    try {
      if (clazz.isAnnotationPresent(ApiService.class)) {
        ApiService ann = clazz.getAnnotation(ApiService.class);
        ServiceVO vo = new ServiceVO(ann);
        vo.setCnName(ann.cnName());
        if (StringUtils.isBlank(ann.serviceName())) {
          vo.setServiceName(clazz.getName());
        } else {
          vo.setServiceName(ann.serviceName().replaceAll("/", "."));
        }
        vo.setServiceFullName(clazz.getName());
        //接口计数.
        basicInfo.setIncCount(basicInfo.getIncCount() + 1);
        if (ann.finish() == 100) {
          basicInfo.setFinishCount(basicInfo.getFinishCount() + 1);
        }
        // 入参
        vo.setRequests((List<ReqDataVO>) apiInResolver.resolve2(clazz, vo));
        // 出参
        vo.setResponses(apiOutResolver.resolve(clazz));
        //返回码
        vo.setApiCodes(resolveCodes(clazz));
        vo.setRequestHeaders(headerResolver.resolve(clazz));
        vo.setNeedAuth(ann.needAuth());
        // 时间轴
        List<TimelineVO> timelines = timelineResolver.resolve(clazz, vo);
        vo.setTimelines(timelines);
        timelineList.addAll(timelines);
        serviceMap.put(vo.getServiceFullName(), vo);
        resolveServiceMenu(clazz, vo);
      }
      String baseUrl = "";
      final RequestMapping mapping = clazz.getAnnotation(RequestMapping.class);
      if (mapping != null) {
        baseUrl = mapping.value()[0];
        if (!baseUrl.startsWith("/")) {
          baseUrl = "/" + baseUrl;
        }
      }
      final String finalBaseUrl = baseUrl;
      ReflectionUtils.doWithMethods(clazz, method -> {
        ApiService apiService = method.getAnnotation(ApiService.class);
        if (apiService == null) {
          return;
        }
        ServiceVO vo = new ServiceVO(apiService);
        vo.setServiceFullName(clazz.getName() + "." + method.getName());
        vo.setNeedAuth(apiService.needAuth());
        String url = "";
        boolean isRest = clazz.isAnnotationPresent(RestController.class);//如果是rest请求进行
        if (method.getAnnotatedReturnType().isAnnotationPresent(ResponseBody.class)) {
          isRest = true;
        }
        if (!isRest) {
          log.warn("{}.{} is not rest api. generate skip.", clazz.getName(), method.getName());
          return;
        }
        if (method.isAnnotationPresent(RequestMapping.class)) {
          RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
          url = requestMapping.value()[0];
          String requestMethodNames = StringUtils.join(requestMapping.method(), ",");
          vo.setMethod(requestMethodNames);
        } else if (method.isAnnotationPresent(GetMapping.class)) {
          url = method.getAnnotation(GetMapping.class).value()[0];
          vo.setMethod("GET");
        } else if (method.isAnnotationPresent(PostMapping.class)) {
          url = method.getAnnotation(PostMapping.class).value()[0];
          vo.setMethod("POST");
        } else if (method.isAnnotationPresent(PutMapping.class)) {
          url = method.getAnnotation(PutMapping.class).value()[0];
          vo.setMethod("PUT");
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
          url = method.getAnnotation(DeleteMapping.class).value()[0];
          vo.setMethod("DELETE");
        } else {
          if (StringUtils.isBlank(apiService.serviceName())) {
            vo.setServiceName(clazz.getName() + "." + method.getName());
          } else {
            vo.setServiceName(apiService.serviceName().replaceAll("/", "."));
          }
        }
        if (vo.getServiceName() == null) {
          //fix 首尾连接都无斜杠的情况
          if (finalBaseUrl.charAt(finalBaseUrl.length() - 1) != '/' && StringUtils.isNotEmpty(url) && url.charAt(0) != '/') {
            url = "/" + url;
          }
          vo.setServiceName((finalBaseUrl + url).replaceAll("//", "/"));
        }
        //接口计数.
        basicInfo.setIncCount(basicInfo.getIncCount() + 1);
        if (apiService.finish() == 100) {
          basicInfo.setFinishCount(basicInfo.getFinishCount() + 1);
        }
        //入参
        vo.setRequests((List<ReqDataVO>) apiInResolver.resolve2(method, vo));
        // 出参
        vo.setResponses(apiOutResolver.resolve(method));
        //请求header
        vo.setRequestHeaders(headerResolver.resolve(method));
        //返回码
        vo.setApiCodes(resolveCodes(method));
        // 时间轴
        List<TimelineVO> timelines = timelineResolver.resolve(method, vo);
        timelineList.addAll(timelines);
        vo.setTimelines(timelines);
        serviceMap.put(vo.getServiceFullName(), vo);
        resolveServiceMenu(method, vo);
      });
    } catch (Throwable e) {
      throw new GenException("分析接口服务异常，", clazz, e);
    }
  }

}
