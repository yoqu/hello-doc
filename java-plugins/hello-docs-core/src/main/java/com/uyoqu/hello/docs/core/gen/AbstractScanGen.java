package com.uyoqu.hello.docs.core.gen;

import com.uyoqu.hello.docs.core.annotation.*;
import com.uyoqu.hello.docs.core.exception.ParameterErrorException;
import com.uyoqu.hello.docs.core.vo.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: yoqu
 * @date: 2018/10/18
 * @email: yoqulin@qq.com
 **/
public abstract class AbstractScanGen implements TopGen {


  public static final Logger log = LoggerFactory.getLogger(AbstractScanGen.class);
  protected static final SimpleDateFormat TIMELINE_SDF = new SimpleDateFormat("yyyy-MM-dd");

  protected static final int NEW_TIMELINE_DAY = 7;

  // 待处理的类
  protected Set<Class<?>> basicSet = new HashSet<Class<?>>();
  protected Set<Class<?>> dtoSet = new HashSet<Class<?>>();
  protected Set<Class<?>> serviceSet = new HashSet<Class<?>>();

  // 结果集
  protected List<TimelineVo> timelineList = new ArrayList<TimelineVo>();
  protected Map<String, MenuGroupVo> menuTempMap = new HashMap<String, MenuGroupVo>(); // Key: 分组名称
  protected List<MenuGroupVo> menuList = new ArrayList<MenuGroupVo>();
  protected Map<String, Object> basicMap = new HashMap<String, Object>(); // Key: js file
  protected Map<String, DtoVo> dtoMap = new HashMap<String, DtoVo>(); // // Key: js file
  protected Map<String, ServiceVo> serviceMap = new HashMap<String, ServiceVo>(); // // Key: js file
  //基本信息map
  protected ApiInfo basicInfo = new ApiInfo();

  public List<TimelineVo> getTimelineList() {
    return timelineList;
  }

  public List<MenuGroupVo> getMenuList() {
    return menuList;
  }

  public Map<String, Object> getBasicMap() {
    return basicMap;
  }

  public Map<String, DtoVo> getDtoMap() {
    return dtoMap;
  }

  public Map<String, ServiceVo> getServiceMap() {
    return serviceMap;
  }

  public ApiInfo getBasicInfo() {
    return basicInfo;
  }

  public void scanPakcages(String... packageName) {
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
      scan = new ScanWrapper(packageName, ApiServiceDocs.class, Controller.class, RestController.class);
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
    MenuGroupVo groupVo = menuTempMap.get(groupName);
    if (groupVo == null) {
      groupVo = new MenuGroupVo();
      groupVo.setSort(1);
      groupVo.setTitle(groupName);
      groupVo.setSubs(new ArrayList<MenuVo>());
      menuTempMap.put(groupName, groupVo);
    }
    groupName = "数据结构";
    MenuGroupVo groupVo2 = menuTempMap.get(groupName);
    if (groupVo2 == null) {
      groupVo2 = new MenuGroupVo();
      groupVo2.setSort(0);
      groupVo2.setTitle(groupName);
      groupVo2.setSubs(new ArrayList<MenuVo>());
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

  private void resolveServiceMenu(ServiceVo serviceVo) {
    String groupName = "接口服务";
    MenuGroupVo groupVo = menuTempMap.get(groupName);
    MenuVo vo = new MenuVo();
    vo.setTitle(serviceVo.getServiceName());
    vo.setUrl("/service/" + serviceVo.getServiceFullName());
    vo.setName(serviceVo.getCnName());
    vo.setGroup(serviceVo.getGroup());
    vo.setFinish(serviceVo.getFinish());
    groupVo.getSubs().add(vo);
  }

  public void handler() throws GenException, IOException {
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
    for (Map.Entry<String, MenuGroupVo> vo : menuTempMap.entrySet()) {
      Collections.sort(vo.getValue().getSubs(), new Comparator<MenuVo>() {
        public int compare(MenuVo o1, MenuVo o2) {
          if (o1 == null && o2 == null)
            return 0;
          else if (o1 == null)
            return 1;
          else if (o2 == null)
            return -1;
          return ObjectUtils.compare(o1.getTitle(), o2.getTitle());
        }
      });
      // 复制到列表
      menuList.add(vo.getValue());
      // 列表排序
      Collections.sort(menuList, new Comparator<MenuGroupVo>() {
        public int compare(MenuGroupVo o1, MenuGroupVo o2) {
          if (o1 == null && o2 == null)
            return 0;
          else if (o1 == null)
            return 1;
          else if (o2 == null)
            return -1;
          return ObjectUtils.compare(o1.getSort(), o2.getSort());
        }
      });
    }
  }

  private void resolveTimeline(Class<?> clazz) throws GenException {
    resolveTimeline(clazz, timelineList);
  }


  private List<TimelineVo> resolveMethodTimeline(Method method) {
    final List<TimelineVo> timelineVos = new ArrayList<TimelineVo>();
    if (!method.isAnnotationPresent(ApiTimeline.class)) {
      return timelineVos;
    }
    ApiTimeline ann = method.getAnnotation(ApiTimeline.class);
    if (ann == null || ann.value() == null || ann.value().length == 0)
      return timelineVos;
    for (Timeline timeline : ann.value()) {
      TimelineVo vo = new TimelineVo(timeline);
      vo.setUrl(resolveUrl(method));
      timelineVos.add(vo);
    }
    return timelineVos;
  }


  private void resolveTimeline(Class<?> clazz, final List<TimelineVo> list) throws GenException {
    try {
      if (clazz.isAnnotationPresent(ApiTimeline.class)) {
        ApiTimeline ann = clazz.getAnnotation(ApiTimeline.class);
        if (ann == null || ann.value() == null || ann.value().length == 0)
          return;
        for (Timeline timeline : ann.value()) {
          TimelineVo vo = new TimelineVo(timeline);
          vo.setUrl(resolveUrl(clazz));
          list.add(vo);
        }
      } else if (clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(RestController.class)) {
        final List<TimelineVo> timelineVos = new ArrayList<TimelineVo>();
        ReflectionUtils.doWithMethods(clazz, new ReflectionUtils.MethodCallback() {
          public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
            timelineVos.addAll(resolveMethodTimeline(method));
          }
        });
        list.addAll(timelineVos);
      }
    } catch (Throwable e) {
      throw new GenException("分析时间轴异常，", clazz, e);
    } finally {
      // 时间轴排序
      Collections.sort(list, new Comparator<TimelineVo>() {
        public int compare(TimelineVo o1, TimelineVo o2) {
          if (o1 == null && o2 == null)
            return 0;
          else if (o1 == null)
            return 1;
          else if (o2 == null)
            return -1;
          return ObjectUtils.compare(o2.getTime(), o1.getTime());
        }
      });
    }
  }

  private void resolveDtoMenu(Class<?> clazz) throws GenException {
    try {
      if (clazz.isAnnotationPresent(ApiDTO.class)) {
        ApiDTO ann = clazz.getAnnotation(ApiDTO.class);
        String groupName = "数据结构";
        MenuGroupVo groupVo = menuTempMap.get(groupName);
        MenuVo vo = new MenuVo();
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
      List<CodeVo> voList = new ArrayList<CodeVo>();
      basicMap.put("code", voList);

      Field[] fields = clazz.getDeclaredFields();
      if (fields == null || fields.length == 0)
        return;

      for (Field field : fields) {
        CodeVo vo = new CodeVo();
        Object obj = field.get(field.getName());
        String code = (String) field.getType().getMethod("get" + StringUtils.capitalize(ann.codeKey())).invoke(obj);
        vo.setCode(code);
        String msg = (String) field.getType().getMethod("get" + StringUtils.capitalize(ann.msgKey())).invoke(obj);
        vo.setDes(msg);
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
      DtoVo vo = new DtoVo();
      vo.setCnName(ann.cnName());
      vo.setEnName(StringUtils.isNotBlank(ann.enName()) ? ann.enName() : clazz.getSimpleName());
      vo.setDesc(ann.desc());
      vo.setDoc(ann.doc());
      // 字段
      final List<DtoDataVo> dataList = new ArrayList<DtoDataVo>();
      vo.setData(dataList);
      ReflectionUtils.doWithFields(clazz, new ReflectionUtils.FieldCallback() {
        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
          if (!field.isAnnotationPresent(ApiBasicFiled.class))
            return;
          ApiBasicFiled def = field.getAnnotation(ApiBasicFiled.class);
          if (def == null) {
            return;
          }
          DtoDataVo dataVo = new DtoDataVo();
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
        }
      });
      // 时间轴
      List<TimelineVo> timelines = new ArrayList<TimelineVo>();
      resolveTimeline(clazz, timelines);
      vo.setTimelines(timelines);
      dtoMap.put(StringUtils.isNotBlank(ann.enName()) ? ann.enName() : clazz.getSimpleName(), vo);
    } catch (Throwable e) {
      throw new GenException("分析数据结构异常，", clazz, e);
    }
  }

  /**
   * 处理方法上的注解
   *
   * @param element
   * @return
   */
  private List<ServiceDataVo> resolveMethodAnnoIn(AnnotatedElement element) {
    // 入参
    final List<ServiceDataVo> requestList = new ArrayList<ServiceDataVo>();
    if (element.isAnnotationPresent(ApiIn.class)) {
      ApiIn def = element.getAnnotation(ApiIn.class);
      for (In in : def.value()) {
        ServiceDataVo dataVo = new ServiceDataVo();
        dataVo.setRequired(String.valueOf(in.remark()));
        dataVo.setName(in.param());
        dataVo.setType(in.type());
        dataVo.setDesc(in.desc());
        dataVo.setRemark(in.remark());
        dataVo.setLink(in.link());
        requestList.add(dataVo);
      }
    }
    //如果没有使用APiIn注解使用了APIInDto的注解
    if (!element.isAnnotationPresent(ApiIn.class) && element.isAnnotationPresent(ApiInDTO.class)) {
      ApiInDTO def = element.getAnnotation(ApiInDTO.class);
      if (Object.class.equals(def.clazz())) {
        throw new ParameterErrorException("[ApiInDTO] annotation variable" + element + " clazz property not use default value [Object.class]");
      }
      DtoFieldCallback dtoCallBack = new DtoFieldCallback();
      ReflectionUtils.doWithFields(def.clazz(), dtoCallBack);
      requestList.addAll(dtoCallBack.getDataVos());
    }
    return requestList;
  }

  private List<ServiceDataVo> resolveServiceOut(AnnotatedElement element) {
    final List<ServiceDataVo> responseList = new ArrayList<ServiceDataVo>();
    if (element.isAnnotationPresent(ApiOut.class)) {
      ApiOut def = element.getAnnotation(ApiOut.class);
      for (Out out : def.value()) {
        ServiceDataVo dataVo = new ServiceDataVo();
        dataVo.setRequired(String.valueOf(out.required()));
        dataVo.setName(out.param());
        dataVo.setType(out.type());
        dataVo.setDesc(out.desc());
        dataVo.setRemark(out.remark());
        dataVo.setLink(out.link());
        responseList.add(dataVo);
      }
    }
    //如果没有使用ApiOut注解,使用ApiOutDto注解
    if (!element.isAnnotationPresent(ApiOut.class) && element.isAnnotationPresent(ApiOutDTO.class)) {
      ApiOutDTO def = element.getAnnotation(ApiOutDTO.class);
      if (Object.class.equals(def.clazz())) {
        throw new ParameterErrorException("[ApiOutDTO] annotation variable" + element + " clazz property not use default value [Object.class]");
      }
      DtoFieldCallback dtoCallBack = new DtoFieldCallback();
      ReflectionUtils.doWithFields(def.clazz(), dtoCallBack);
      responseList.addAll(dtoCallBack.getDataVos());
    }
    return responseList;
  }

  private List<CodeVo> resolveCodes(AnnotatedElement element) {
    if (element.isAnnotationPresent(ApiCodes.class)) {
      List<CodeVo> codeVos = new ArrayList<CodeVo>();
      ApiCodes def = element.getAnnotation(ApiCodes.class);
      for (ApiCode code : def.value()) {
        CodeVo codeVo = new CodeVo();
        codeVo.setCode(code.code());
        codeVo.setDes(code.msg());
        codeVos.add(codeVo);
      }
      return codeVos;
    }
    return null;
  }

  private void resolveService(final Class<?> clazz) throws GenException {
    try {
      if (clazz.isAnnotationPresent(ApiServiceDocs.class)) {
        ApiServiceDocs ann = clazz.getAnnotation(ApiServiceDocs.class);
        ServiceVo vo = new ServiceVo(ann);
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
        vo.setRequests(resolveMethodAnnoIn(clazz));
        // 出参
        vo.setResponses(resolveServiceOut(clazz));
        //返回码
        vo.setApiCodes(resolveCodes(clazz));
        // 时间轴
        List<TimelineVo> timelines = new ArrayList<TimelineVo>();
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
      ReflectionUtils.doWithMethods(clazz, new ReflectionUtils.MethodCallback() {
        public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
          ApiServiceDocs apiServiceDocs = method.getAnnotation(ApiServiceDocs.class);
          if (apiServiceDocs == null) {
            return;
          }
          ServiceVo vo = new ServiceVo(apiServiceDocs);
          vo.setServiceFullName(clazz.getName() + "." + method.getName());
          String url = "";
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
            if (StringUtils.isBlank(apiServiceDocs.serviceName())) {
              vo.setServiceName(clazz.getName() + "." + method.getName());
            } else {
              vo.setServiceName(apiServiceDocs.serviceName().replaceAll("/", "."));
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
          if (apiServiceDocs.finish() == 100) {
            basicInfo.setFinishCount(basicInfo.getFinishCount() + 1);
          }
          //入参
          List<ServiceDataVo> ins = resolveMethodAnnoIn(method);
          if (!CollectionUtils.isEmpty(ins)) {
            vo.setRequests(ins);
          } else {
            List<ServiceDataVo> in = resolveMethodParameterAnnoIn(method);
            vo.setRequests(in);
          }
          // 出参
          if (method.getReturnType().isAnnotationPresent(ApiDTO.class)) {
            DtoFieldCallback dtoCallBack = new DtoFieldCallback();
            ReflectionUtils.doWithFields(method.getReturnType(), dtoCallBack);
            vo.setResponses(dtoCallBack.getDataVos());
          } else {
            vo.setResponses(resolveServiceOut(method));
          }
          //返回码
          vo.setApiCodes(resolveCodes(method));
          // 时间轴
          List<TimelineVo> timelines = resolveMethodTimeline(method);
          vo.setTimelines(timelines);
          serviceMap.put(vo.getServiceFullName(), vo);
          resolveServiceMenu(vo);
        }
      });
    } catch (Throwable e) {
      throw new GenException("分析接口服务异常，", clazz, e);
    }
  }

  /**
   * 处理方法参数注解
   *
   * @param method
   * @return
   */
  private List<ServiceDataVo> resolveMethodParameterAnnoIn(Method method) {
    List<ServiceDataVo> in = new ArrayList<ServiceDataVo>();
    int i = 0;
    LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
    String[] names = u.getParameterNames(method);
    for (Class<?> request : method.getParameterTypes()) {
      Annotation[][] parameterAnnotations = method.getParameterAnnotations();
      boolean isFoundAnnotation = false;
      for (Annotation[] annotations : parameterAnnotations) {
        for (Annotation annotation : annotations) {
          if (annotation instanceof In) {
            In inAnnotation = (In) annotation;
            ServiceDataVo serviceDataVo = new ServiceDataVo(inAnnotation);
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
        DtoFieldCallback dtoCallBack = new DtoFieldCallback();
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
   *
   * @param f
   * @return
   */
  private ServiceDataVo getDataVoByDtoField(Field f) {
    ApiBasicFiled basicField = f.getAnnotation(ApiBasicFiled.class);
    if (basicField == null) {
      return null;
    }
    ServiceDataVo dataVo = new ServiceDataVo();
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
    } else if (clazz.isAnnotationPresent(ApiServiceDocs.class)) {
      ApiServiceDocs ann = clazz.getAnnotation(ApiServiceDocs.class);
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
    } else if (method.isAnnotationPresent(ApiServiceDocs.class)) {
      ApiServiceDocs ann = method.getAnnotation(ApiServiceDocs.class);
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
    if (ann == null || ann.value() == null || ann.value().length == 0)
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

    private List<ServiceDataVo> dataVos = new ArrayList<ServiceDataVo>();

    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
      ServiceDataVo dataVo = getDataVoByDtoField(field);
      if (dataVo == null) {
        return;
      }
      dataVos.add(dataVo);
    }

    public List<ServiceDataVo> getDataVos() {
      return dataVos;
    }
  }
}
