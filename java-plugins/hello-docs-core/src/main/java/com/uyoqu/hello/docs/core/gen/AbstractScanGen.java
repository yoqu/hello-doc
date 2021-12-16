package com.uyoqu.hello.docs.core.gen;

import com.uyoqu.hello.docs.core.annotation.ApiCode;
import com.uyoqu.hello.docs.core.annotation.ApiCodes;
import com.uyoqu.hello.docs.core.annotation.ApiDTO;
import com.uyoqu.hello.docs.core.annotation.ApiField;
import com.uyoqu.hello.docs.core.annotation.ApiGlobalCode;
import com.uyoqu.hello.docs.core.annotation.ApiHeader;
import com.uyoqu.hello.docs.core.annotation.ApiHeaders;
import com.uyoqu.hello.docs.core.annotation.ApiIn;
import com.uyoqu.hello.docs.core.annotation.ApiInDTO;
import com.uyoqu.hello.docs.core.annotation.ApiOut;
import com.uyoqu.hello.docs.core.annotation.ApiOutDTO;
import com.uyoqu.hello.docs.core.annotation.ApiService;
import com.uyoqu.hello.docs.core.annotation.ApiTimeline;
import com.uyoqu.hello.docs.core.annotation.ApiParamIn;
import com.uyoqu.hello.docs.core.annotation.Out;
import com.uyoqu.hello.docs.core.annotation.Timeline;
import com.uyoqu.hello.docs.core.exception.ParameterErrorException;
import com.uyoqu.hello.docs.core.vo.CodeVO;
import com.uyoqu.hello.docs.core.vo.DtoDataVO;
import com.uyoqu.hello.docs.core.vo.DtoVO;
import com.uyoqu.hello.docs.core.vo.MenuGroupVO;
import com.uyoqu.hello.docs.core.vo.MenuVO;
import com.uyoqu.hello.docs.core.vo.ServiceDataVO;
import com.uyoqu.hello.docs.core.vo.ServiceVO;
import com.uyoqu.hello.docs.core.vo.TimelineVO;
import lombok.var;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
public abstract class AbstractScanGen implements Gen {


  public static final Logger log = LoggerFactory.getLogger(AbstractScanGen.class);
  protected static final SimpleDateFormat TIMELINE_SDF = new SimpleDateFormat("yyyy-MM-dd");

  protected static final int NEW_TIMELINE_DAY = 7;

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
      // 分析时间轴
      resolveTimeline(clazz);
      // 分析基础定义
      resolveBasic(clazz);
    }
    for (Class<?> clazz : dtoSet) {
      // 分析时间轴
      resolveTimeline(clazz);
      // 分析实体菜单
      resolveDtoMenu(clazz);
      // 分析数据结构
      resolveDto(clazz);
    }
    for (Class<?> clazz : serviceSet) {
      // 分析时间轴
      resolveTimeline(clazz);
      // 分析接口服务
      resolveService(clazz);
    }
    if (basicInfo != null) {//基础信息
      basicMap.put("basic", basicInfo);
    }
  }

  private void resolveServiceMenu(ServiceVO serviceVo) {
    String groupName = "接口服务";
    MenuGroupVO groupVo = menuTempMap.get(groupName);
    MenuVO vo = new MenuVO();
    vo.setTitle(serviceVo.getServiceName());
    vo.setUrl("/service/" + serviceVo.getServiceFullName());
    vo.setName(serviceVo.getCnName());
    vo.setGroup(serviceVo.getGroup());
    vo.setFinish(serviceVo.getFinish());
    groupVo.getSubs().add(vo);
  }

  public void handler() throws GenException {
    //检查输入的参数是否都有了
    check();
    // 分析
    resolve();
    // 排序
    sort();
    //写入文件
    write();
  }

  private void sort() {
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

  private void resolveTimeline(Class<?> clazz) throws GenException {
    resolveTimeline(clazz, timelineList);
  }


  private List<TimelineVO> resolveMethodTimeline(Method method) {
    final List<TimelineVO> timelineVOS = new ArrayList<>();
    if (!method.isAnnotationPresent(ApiTimeline.class)) {
      return timelineVOS;
    }
    ApiTimeline ann = method.getAnnotation(ApiTimeline.class);
    if (ann == null || ann.value().length == 0)
      return timelineVOS;
    for (Timeline timeline : ann.value()) {
      TimelineVO vo = new TimelineVO(timeline);
      vo.setUrl(resolveUrl(method));
      timelineVOS.add(vo);
    }
    return timelineVOS;
  }


  private void resolveTimeline(Class<?> clazz, final List<TimelineVO> list) throws GenException {
    try {
      if (clazz.isAnnotationPresent(ApiTimeline.class)) {
        ApiTimeline ann = clazz.getAnnotation(ApiTimeline.class);
        if (ann == null || ann.value().length == 0)
          return;
        for (Timeline timeline : ann.value()) {
          TimelineVO vo = new TimelineVO(timeline);
          vo.setUrl(resolveUrl(clazz));
          list.add(vo);
        }
      } else if (clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(RestController.class)) {
        final List<TimelineVO> timelineVOS = new ArrayList<>();
        ReflectionUtils.doWithMethods(clazz, method -> timelineVOS.addAll(resolveMethodTimeline(method)));
        list.addAll(timelineVOS);
      }
    } catch (Throwable e) {
      throw new GenException("分析时间轴异常，", clazz, e);
    } finally {
      // 时间轴排序
      list.sort((o1, o2) -> {
        if (o1 == null && o2 == null)
          return 0;
        else if (o1 == null)
          return 1;
        else if (o2 == null)
          return -1;
        return ObjectUtils.compare(o2.getTime(), o1.getTime());
      });
    }
  }

  private void resolveDtoMenu(Class<?> clazz) throws GenException {
    try {
      if (clazz.isAnnotationPresent(ApiDTO.class)) {
        ApiDTO ann = clazz.getAnnotation(ApiDTO.class);
        String groupName = "数据结构";
        MenuGroupVO groupVo = menuTempMap.get(groupName);
        MenuVO vo = new MenuVO();
        String enName = StringUtils.isBlank(ann.enName()) ? clazz.getSimpleName() : ann.enName();
        vo.setTitle(enName);
        vo.setName(ann.cnName());
        vo.setUrl(resolveUrl(clazz));
        vo.setGroup(ann.group());
        vo.setIsnew(resolveIsNew(clazz));
        groupVo.getSubs().add(vo);
      }
    } catch (Throwable e) {
      throw new GenException("分析菜单异常，", clazz, e);
    }
  }

  private void resolveBasic(Class<?> clazz) throws GenException {
    try {
      if (!clazz.isAnnotationPresent(ApiGlobalCode.class))
        return;

      ApiGlobalCode ann = clazz.getAnnotation(ApiGlobalCode.class);
      List<CodeVO> voList = new ArrayList<>();
      basicMap.put("code", voList);

      Field[] fields = clazz.getDeclaredFields();
      if (fields.length == 0)
        return;
      for (Field field : fields) {
        CodeVO vo = new CodeVO();
        Object obj = field.get(field.getName());
        String code = (String) field.getType().getMethod("get" + StringUtils.capitalize(ann.codeKey())).invoke(obj);
        vo.setCode(code);
        String msg = (String) field.getType().getMethod("get" + StringUtils.capitalize(ann.msgKey())).invoke(obj);
        vo.setDesc(msg);
        voList.add(vo);
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
      vo.setCnName(ann.cnName());
      vo.setEnName(StringUtils.isNotBlank(ann.enName()) ? ann.enName() : clazz.getSimpleName());
      vo.setDesc(ann.desc());
      vo.setDoc(ann.doc());
      // 字段
      final List<DtoDataVO> dataList = new ArrayList<>();
      vo.setData(dataList);
      ReflectionUtils.doWithFields(clazz, field -> {
        if (!field.isAnnotationPresent(ApiField.class))
          return;
        ApiField def = field.getAnnotation(ApiField.class);
        if (def == null) {
          return;
        }
        DtoDataVO dataVo = new DtoDataVO();
        if (StringUtils.isNotBlank(def.param())) {
          dataVo.setName(def.param());
        } else {
          dataVo.setName(field.getName());
        }
        if (StringUtils.isNotBlank(def.type())) {
          dataVo.setType(def.type());
        } else {
          dataVo.setType(field.getType().getSimpleName());
        }
        dataVo.setDesc(def.desc());
        dataVo.setRemark(def.remark());
        dataVo.setRequired(String.valueOf(def.required()));
        dataVo.setLink(def.link());
        dataList.add(dataVo);
      });
      // 时间轴
      List<TimelineVO> timelines = new ArrayList<>();
      resolveTimeline(clazz, timelines);
      vo.setTimelines(timelines);
      dtoMap.put(StringUtils.isNotBlank(ann.enName()) ? ann.enName() : clazz.getSimpleName(), vo);
    } catch (Throwable e) {
      throw new GenException("分析数据结构异常，", clazz, e);
    }
  }

  private List<ServiceDataVO> resolveMethodHeaderAnnotation(Method method) {
    final List<ServiceDataVO> headerList = new ArrayList<>();
    Annotation[][] anns = method.getParameterAnnotations();
    Class<?>[] methodCLass = method.getParameterTypes();
    for (int i = 0; i < methodCLass.length; i++) {
      var isFound = false;
      ServiceDataVO.ServiceDataVOBuilder builder = ServiceDataVO.builder();
      for (Annotation a : anns[i]) {
        if (a.annotationType() == RequestHeader.class) {
          RequestHeader h = ((RequestHeader) a);
          builder.name(h.name()).required(String.valueOf(h.required()));
          isFound = true;
        }
        if (a.annotationType() == ApiHeader.class) {
          ApiHeader h = (ApiHeader) a;
          builder.desc(h.desc()).type(h.type());
          isFound = true;
        }
      }
      if (isFound) {
        ServiceDataVO dataVo = builder.build();
        headerList.add(dataVo);
      }
    }
    ApiHeaders apiHeaders = method.getAnnotation(ApiHeaders.class);
    if (apiHeaders != null && apiHeaders.value().length > 0) {
      for (ApiHeader header : apiHeaders.value()) {
        ServiceDataVO dataVo = ServiceDataVO.builder().desc(header.desc()).name(header.name()).type(header.type()).required(String.valueOf(header.required())).build();
        headerList.add(dataVo);
      }
    }
    return headerList;
  }

  /**
   * 处理方法上的注解
   */
  private List<ServiceDataVO> resolveMethodAnnoIn(AnnotatedElement element, boolean isRest) {
    // 入参
    final List<ServiceDataVO> requestList = new ArrayList<>();
    var isAdded = false;
    if (element.isAnnotationPresent(ApiIn.class)) {
      ApiIn def = element.getAnnotation(ApiIn.class);
      for (ApiParamIn in : def.value()) {
        ServiceDataVO dataVo = new ServiceDataVO(in);
        requestList.add(dataVo);
      }
      isAdded = true;
    }
    //如果没有使用APiIn注解使用了APIInDto的注解
    if (!isAdded && element.isAnnotationPresent(ApiInDTO.class)) {
      ApiInDTO def = element.getAnnotation(ApiInDTO.class);
      readServiceDto(requestList, def.clazz(), def.groups());
      isAdded = true;
    }
    //处理method类型的数据
    if (!isAdded && element instanceof Method) {
      for (Parameter parameter : ((Method) element).getParameters()) {
        if (parameter.isAnnotationPresent(ApiInDTO.class)) {
          readServiceDto(requestList, parameter, parameter.getAnnotation(ApiInDTO.class).groups());
          break;
        }
        if (isRest && parameter.getType().isAnnotationPresent(ApiDTO.class)) {
          readServiceDto(requestList, parameter, null);
          break;
        }
      }
    }
    return requestList;
  }

  /**
   * 通过注解写入data数据
   */
  private void readServiceDto(List<ServiceDataVO> dataVos, Class<?> clazz2, Class<? extends Annotation>[] groups) {
    if (clazz2 == null) {
      throw new ParameterErrorException("parameter type error.");
    }
    DtoFieldCallback dtoCallBack = new DtoFieldCallback(groups);
    ReflectionUtils.doWithFields(clazz2, dtoCallBack);
    dataVos.addAll(dtoCallBack.getDataVos());
  }

  /**
   * 通过注解写入data数据
   */
  private void readServiceDto(List<ServiceDataVO> dataVos, Parameter parameter, Class<? extends Annotation>[] groups) {
    Class<?> clazz2 = parameter.getType();
    if (clazz2 == null) {
      throw new ParameterErrorException("parameter type error.");
    }
    if (String.class.isAssignableFrom(clazz2) || clazz2.isPrimitive()) {//处理方法的基础类型
      dataVos.add(new ServiceDataVO(parameter.getName(), clazz2.getSimpleName(), "", "true", "", "", null, null));
      return;
    }
    readServiceDto(dataVos, clazz2, groups);
  }

  private List<ServiceDataVO> resolveServiceOut(AnnotatedElement element) {
    final List<ServiceDataVO> responseList = new ArrayList<>();
    var isAdded = false;
    if (element.isAnnotationPresent(ApiOut.class)) {
      ApiOut def = element.getAnnotation(ApiOut.class);
      for (Out out : def.value()) {
        ServiceDataVO dataVo = new ServiceDataVO(out);
        responseList.add(dataVo);
      }
      isAdded = true;
    }
    if (!isAdded && element.isAnnotationPresent(ApiOutDTO.class)) {
      ApiOutDTO def = element.getAnnotation(ApiOutDTO.class);
      Class<?> clazz = def.clazz();
      if (clazz == Object.class && element instanceof Method) {//处理空场景
        clazz = ((Method) element).getReturnType();
      }
      readServiceDto(responseList, clazz, def.groups());
    }
    return responseList;
  }

  private List<ServiceDataVO> resolveServiceHeaders(AnnotatedElement element) {
    final List<ServiceDataVO> headerVos = new ArrayList<>();
    if (element.isAnnotationPresent(ApiHeaders.class)) {
      ApiHeaders def = element.getAnnotation(ApiHeaders.class);
      for (ApiHeader header : def.value()) {
        ServiceDataVO dataVo = new ServiceDataVO();
        dataVo.setRequired(String.valueOf(header.required()));
        dataVo.setName(header.name());
        dataVo.setType(header.type());
        dataVo.setDesc(header.desc());
        headerVos.add(dataVo);
      }
    }
    return headerVos;
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
        vo.setRequests(resolveMethodAnnoIn(clazz, false));
        // 出参
        vo.setResponses(resolveServiceOut(clazz));
        //返回码
        vo.setApiCodes(resolveCodes(clazz));
        vo.setHeaders(resolveServiceHeaders(clazz));
        vo.setNeedAuth(ann.needAuth());
        // 时间轴
        List<TimelineVO> timelines = new ArrayList<>();
        resolveTimeline(clazz, timelines);
        vo.setTimelines(timelines);
        serviceMap.put(vo.getServiceFullName(), vo);
        resolveServiceMenu(vo);
      }
      String baseUrl = "";
      final RequestMapping mapping = clazz.getAnnotation(RequestMapping.class);
      if (mapping != null) {
        baseUrl = mapping.value()[0];
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
          vo.setMethod("PUT");
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
        List<ServiceDataVO> ins = resolveMethodAnnoIn(method, isRest);
        if (!CollectionUtils.isEmpty(ins)) {
          vo.setRequests(ins);
        } else {
          List<ServiceDataVO> in = resolveMethodParameterAnnoIn(method);
          vo.setRequests(in);
        }
        // 出参
        if (isRest && method.getReturnType().isAnnotationPresent(ApiDTO.class)) {
          DtoFieldCallback dtoCallBack = new DtoFieldCallback(null);
          ReflectionUtils.doWithFields(method.getReturnType(), dtoCallBack);
          vo.setResponses(dtoCallBack.getDataVos());
        } else {
          vo.setResponses(resolveServiceOut(method));
        }
        if (vo.getHeaders() == null || vo.getHeaders().isEmpty()) {
          List<ServiceDataVO> headers = resolveMethodHeaderAnnotation(method);
          vo.setHeaders(headers);
        }
        //返回码
        vo.setApiCodes(resolveCodes(method));
        // 时间轴
        List<TimelineVO> timelines = resolveMethodTimeline(method);
        vo.setTimelines(timelines);
        serviceMap.put(vo.getServiceFullName(), vo);
        resolveServiceMenu(vo);
      });
    } catch (Throwable e) {
      throw new GenException("分析接口服务异常，", clazz, e);
    }
  }

  /**
   * 处理方法参数注解
   */
  private List<ServiceDataVO> resolveMethodParameterAnnoIn(Method method) {
    List<ServiceDataVO> in = new ArrayList<>();
    int i = 0;
    LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
    String[] names = u.getParameterNames(method);
    for (Class<?> request : method.getParameterTypes()) {
      Annotation[][] parameterAnnotations = method.getParameterAnnotations();
      boolean isFoundAnnotation = false;
      for (Annotation[] annotations : parameterAnnotations) {
        for (Annotation annotation : annotations) {
          if (annotation instanceof ApiParamIn) {
            ApiParamIn inAnnotation = (ApiParamIn) annotation;
            ServiceDataVO serviceDataVo = new ServiceDataVO(inAnnotation);
            serviceDataVo.setType(request.getSimpleName());
            if (serviceDataVo.getLink().length() == 0 && request.isAnnotationPresent(ApiDTO.class)) {
              serviceDataVo.setLink(request.getSimpleName());
            }
            if (serviceDataVo.getName().length() == 0) {
              serviceDataVo.setName(names[i]);
            }
            in.add(serviceDataVo);
            isFoundAnnotation = true;
            break;
          }
        }
      }
      if (!isFoundAnnotation && request.isAnnotationPresent(ApiDTO.class)) {
        DtoFieldCallback dtoCallBack = new DtoFieldCallback(null);
        log.warn("request:{}", request);
        ReflectionUtils.doWithFields(request, dtoCallBack);
        in.addAll(dtoCallBack.getDataVos());
      }
      i++;
    }
    return in;
  }

  /**
   * 功过字段注解生成serviceDataVo实体
   */
  private ServiceDataVO getDataVoByDtoField(Field f) {
    ApiField basicField = f.getAnnotation(ApiField.class);
    if (basicField == null) {
      return null;
    }
    ServiceDataVO dataVo = new ServiceDataVO();
    dataVo.setRequired(String.valueOf(basicField.required()));
    if (StringUtils.isBlank(basicField.param())) {
      dataVo.setName(f.getName());
    } else {
      dataVo.setName(basicField.param());
    }

    if (StringUtils.isBlank(basicField.type())) {
      dataVo.setType(f.getType().getSimpleName());
    } else {
      dataVo.setType(basicField.type());
    }
    dataVo.setLink(basicField.link());
    dataVo.setDesc(basicField.desc());
    dataVo.setRemark(basicField.remark());
    return dataVo;
  }

  private String resolveUrl(Class<?> clazz) {
    if (clazz.isAnnotationPresent(ApiGlobalCode.class)) {
      return "/basic-definition";
    } else if (clazz.isAnnotationPresent(ApiDTO.class)) {
      ApiDTO ann = clazz.getAnnotation(ApiDTO.class);
      return "/dto/" + (StringUtils.isNotBlank(ann.enName()) ? ann.enName() : clazz.getSimpleName());
    } else if (clazz.isAnnotationPresent(ApiService.class)) {
      ApiService ann = clazz.getAnnotation(ApiService.class);
      return "/service/" + (StringUtils.isBlank(ann.serviceName()) ? clazz.getName() : ann.serviceName().replaceAll("/", ".")) + "_" + ann.version();
    } else {
      return "";
    }
  }

  private String resolveUrl(Method method) {
    if (method.isAnnotationPresent(ApiGlobalCode.class)) {
      return "/basic-definition";
    } else if (method.isAnnotationPresent(ApiDTO.class)) {
      ApiDTO ann = method.getAnnotation(ApiDTO.class);
      return "/dto/" + (StringUtils.isNotBlank(ann.enName()) ? ann.enName() : method.getName());
    } else if (method.isAnnotationPresent(ApiService.class)) {
      ApiService ann = method.getAnnotation(ApiService.class);
      return "/service/" + (StringUtils.isBlank(ann.serviceName()) ? method.getName() : ann.serviceName().replaceAll("/", ".")) + "_" + ann.version();
    } else {
      return "";
    }
  }

  private String resolveIsNew(Class<?> clazz) throws GenException {
    try {
      if (!clazz.isAnnotationPresent(ApiTimeline.class))
        return "";
      ApiTimeline ann = clazz.getAnnotation(ApiTimeline.class);
      return resolveIsNew(ann);
    } catch (Throwable e) {
      throw new GenException("分析时间轴是否更新时异常，", clazz, e);
    }
  }

  private String resolveIsNew(ApiTimeline ann) throws ParseException {
    if (ann == null || ann.value().length == 0)
      return "";
    for (Timeline timeline : ann.value()) {
      Date date = TIMELINE_SDF.parse(timeline.time());
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      cal.add(Calendar.DATE, NEW_TIMELINE_DAY);
      Date now = new Date();
      if (cal.getTime().getTime() >= now.getTime()) {
        return "1";
      }
    }
    return "";
  }

  public abstract void write();

  /**
   * 处理实体字段
   */
  private class DtoFieldCallback implements ReflectionUtils.FieldCallback {
    private final List<ServiceDataVO> dataVos = new ArrayList<>();
    Class<? extends Annotation>[] groups;

    private DtoFieldCallback(Class<? extends Annotation>[] groups) {
      this.groups = groups;
    }

    private void addDataVo(ServiceDataVO dataVo) {
      if (dataVo == null) {
        return;
      }
      dataVos.add(dataVo);
    }

    public void doWith(Field field) throws IllegalArgumentException {
      if (groups != null && groups.length > 0) {
        for (Class<? extends Annotation> group : groups) {
          ApiField apiField = field.getAnnotation(ApiField.class);
          if (apiField == null) {
            return;
          }
          for (Class<? extends Annotation> showGroup : apiField.showGroup()) {
            if (showGroup.isAssignableFrom(group)) {
              addDataVo(getDataVoByDtoField(field));
              return;
            }
          }
        }
      } else {
        addDataVo(getDataVoByDtoField(field));
      }
    }

    private List<ServiceDataVO> getDataVos() {
      return dataVos;
    }
  }
}
