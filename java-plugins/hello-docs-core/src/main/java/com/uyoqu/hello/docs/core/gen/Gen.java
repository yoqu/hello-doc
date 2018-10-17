package com.uyoqu.hello.docs.core.gen;


import com.alibaba.fastjson.JSON;
import com.uyoqu.hello.docs.core.annotation.*;
import com.uyoqu.hello.docs.core.vo.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

public class Gen {
  public static final Logger log = LoggerFactory.getLogger(Gen.class);
  private static final SimpleDateFormat TIMELINE_SDF = new SimpleDateFormat("yyyy-MM-dd");
  private static final int NEW_TIMELINE_DAY = 7;
  private static final String DOC_ROOT = "./docs";

  // 待处理的类
  private Set<Class<?>> basicSet = new HashSet<Class<?>>();
  private Set<Class<?>> dtoSet = new HashSet<Class<?>>();
  private Set<Class<?>> serviceSet = new HashSet<Class<?>>();

  // 结果集
  private List<TimelineVo> timelineList = new ArrayList<TimelineVo>();
  private Map<String, MenuGroupVo> menuTempMap = new HashMap<String, MenuGroupVo>(); // Key: 分组名称
  private List<MenuGroupVo> menuList = new ArrayList<MenuGroupVo>();
  private Map<String, Object> basicMap = new HashMap<String, Object>(); // Key: js file
  private Map<String, DtoVo> dtoMap = new HashMap<String, DtoVo>(); // // Key: js file
  private Map<String, ServiceVo> serviceMap = new HashMap<String, ServiceVo>(); // // Key: js file

  //基本嘻嘻map
  private ApiInfo basicInfo = new ApiInfo();

  private String docRoot;

  public String getDocRoot() {
    return StringUtils.isNotBlank(docRoot) ? docRoot : DOC_ROOT;
  }

  public void setDocRoot(String docRoot) {
    this.docRoot = docRoot;
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
      scan = new ScanWrapper(packageName, ApiServiceDocs.class);
      set = scan.getClassSet();
      if (set != null) {
        System.out.println(">>>>>>>>>>>>查询到的Service：<<<<<<<<<<<<");
        for (Class<?> c : set) {
          System.out.println(c.getSimpleName());
        }
        System.out.println(">>>>>>>>>>finish<<<<<<<<<<<<");
        serviceSet.addAll(set);
      }
    } catch (Throwable e) {
      log.error("扫描包失败：" + e.getMessage(), e);
    }
  }


  public void initInfo(ApiInfo basicInfo) {
    this.basicInfo = basicInfo;
  }


  public boolean register(Class clazz, GenType type) {
    if (clazz == null)
      return false;

    switch (type) {
      case Basic:
        return basicSet.add(clazz);
      case Dto:
        return dtoSet.add(clazz);
      case Service:
        return serviceSet.add(clazz);
      default:
        return false;
    }
  }

  public void handler() throws GenException, IOException {
    log.info("输出目录：" + new File(getDocRoot()).getAbsolutePath());
    //检查输入的参数是否都有了
    check();
    // 分析
    resolve();
    // 排序
    sort();
    // 拷贝模板
    copyTemplate();
    // 写入
    write();
    log.info("文件生成成功！");
  }

  private void check() {
    Assert.notNull(basicInfo, "apiInfo");
    Assert.notNull(basicInfo.getEnName(), "apiInfo enName");
    Assert.notNull(docRoot, "docRot目录");
  }

  private void resolve() throws GenException {
    for (Class<?> clazz : basicSet) {
      // 分析时间轴。
      resolveTimeline(clazz);
      // 分析基础定义。

      resolveBasic(clazz);
    }
    for (Class<?> clazz : dtoSet) {
      // 分析时间轴。
      resolveTimeline(clazz);
      // 分析菜单。
      resolveMenu(clazz);
      // 分析数据结构。
      resolveDto(clazz);
    }
    for (Class<?> clazz : serviceSet) {
      // 分析时间轴。
      resolveTimeline(clazz);
      // 分析菜单。
      resolveMenu(clazz);
      // 分析接口服务。
      resolveService(clazz);
    }
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

  private void copyTemplate() throws IOException {
//        FileUtils.deleteDirectory(new File(getDocRoot()));
    FileUtils.forceMkdir(new File(getDocRoot()));
//        FileUtils.copyDirectory(new File(URLDecoder.decode(Gen.class.getResource("/").getFile()) + basicInfo.getEnName()), new File(getDocRoot()));
    FileUtils.deleteDirectory(new File(getDocRoot() + "/static/data"));
    FileUtils.forceMkdir(new File(getDocRoot() + "/static/data"));
  }

  private void write() throws IOException {
    // 写菜单
    writeFile(getDocRoot() + "/static/data", "nav_menu.json", JSON.toJSONString(menuList,true));
    // 写时间轴
    writeFile(getDocRoot() + "/static/data", "timelines.json", JSON.toJSONString(timelineList,true));
    if (basicInfo != null) {//基础信息
      basicMap.put("basic", basicInfo);
    }
    // 写基础定义
    writeFile(getDocRoot() + "/static/data/", "basic_definition.json", JSON.toJSONString(basicMap,true));
    // 写数据结构
    writeFile(getDocRoot() + "/static/data", "dto.json", JSON.toJSONString(dtoMap,true));
    // 写接口服务
    writeFile(getDocRoot() + "/static/data", "service.json", JSON.toJSONString(serviceMap,true));
  }

  private void writeFile(String path, String filename, String content) throws IOException {
    FileUtils.forceMkdir(new File(path));
    File file = new File(path + "/" + filename);
    file.delete();

    FileWriter writer = null;
    try {
      writer = new FileWriter(file, false);
      writer.write(content);
      writer.flush();
    } catch (IOException e) {
      try {
        if (writer != null)
          writer.close();
      } catch (Throwable ignored) {

      }
      throw e;
    }
  }

  private void resolveTimeline(Class<?> clazz) throws GenException {
    resolveTimeline(clazz, timelineList);
  }

  private void resolveTimeline(Class<?> clazz, List<TimelineVo> list) throws GenException {
    try {
      if (!clazz.isAnnotationPresent(ApiTimeline.class))
        return;

      ApiTimeline ann = clazz.getAnnotation(ApiTimeline.class);
      if (ann == null || ann.value() == null || ann.value().length == 0)
        return;

      for (Timeline timeline : ann.value()) {
        TimelineVo vo = new TimelineVo();
        vo.setTime(timeline.time());
        vo.setContent(timeline.content());
        vo.setUrl(resolveUrl(clazz));
        list.add(vo);
      }
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
    } catch (Throwable e) {
      throw new GenException("分析时间轴异常，", clazz, e);
    }
  }

  private void resolveMenu(Class<?> clazz) throws GenException {
    try {
      if (clazz.isAnnotationPresent(ApiDTO.class)) {
        ApiDTO ann = clazz.getAnnotation(ApiDTO.class);
        String groupName = "数据结构";
        MenuGroupVo groupVo = menuTempMap.get(groupName);
        if (groupVo == null) {
          groupVo = new MenuGroupVo();
          groupVo.setSort(0);
          groupVo.setTitle(groupName);
          groupVo.setSubs(new ArrayList<MenuVo>());
          menuTempMap.put(groupName, groupVo);
        }
        MenuVo vo = new MenuVo();
        vo.setTitle(ann.enName());
        vo.setName(ann.cnName());
        vo.setUrl(resolveUrl(clazz));
        vo.setGroup(ann.group());
        vo.setIsnew(resolveIsnew(clazz));
        groupVo.getSubs().add(vo);
      }
      if (clazz.isAnnotationPresent(ApiServiceDocs.class)) {
        ApiServiceDocs ann = clazz.getAnnotation(ApiServiceDocs.class);
        String groupName = "接口服务";
        MenuGroupVo groupVo = menuTempMap.get(groupName);
        if (groupVo == null) {
          groupVo = new MenuGroupVo();
          groupVo.setSort(1);
          groupVo.setTitle(groupName);
          groupVo.setSubs(new ArrayList<MenuVo>());
          menuTempMap.put(groupName, groupVo);
        }
        MenuVo vo = new MenuVo();
        if (StringUtils.isBlank(ann.serviceName())) {
          vo.setTitle(clazz.getName());
        } else {
          vo.setTitle(ann.serviceName());
        }
        vo.setName(ann.cnName());
        vo.setUrl(resolveUrl(clazz));
        vo.setIsnew(resolveIsnew(clazz));
        vo.setGroup(ann.group());
        vo.setFinish(ann.finish());
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

  private void resolveService(Class<?> clazz) throws GenException {
    try {
      if (!clazz.isAnnotationPresent(ApiServiceDocs.class))
        return;

      ApiServiceDocs ann = clazz.getAnnotation(ApiServiceDocs.class);
      ServiceVo vo = new ServiceVo();
      vo.setCnName(ann.cnName());
      if (StringUtils.isBlank(ann.serviceName())) {
        vo.setServiceName(clazz.getName());
      } else {
        vo.setServiceName(ann.serviceName().replaceAll("/","."));

      }
      vo.setVersion(ann.version());
      vo.setMethod(StringUtils.join(ann.methods()));
      vo.setDesc(ann.desc());
      vo.setGroup(ann.group());
      vo.setDoc(ann.doc());
      vo.setFinish(ann.finish());
      //接口计数.
      basicInfo.setIncCount(basicInfo.getIncCount() + 1);
      if (ann.finish() == 100) {
        basicInfo.setFinishCount(basicInfo.getFinishCount() + 1);
      }
      // 入参
      final List<ServiceDataVo> requestList = new ArrayList<ServiceDataVo>();
      vo.setRequests(requestList);
      if (clazz.isAnnotationPresent(ApiIn.class)) {
        ApiIn def = clazz.getAnnotation(ApiIn.class);
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
      if (!clazz.isAnnotationPresent(ApiIn.class) && clazz.isAnnotationPresent(ApiInDTO.class)) {
        ApiInDTO def = clazz.getAnnotation(ApiInDTO.class);
        ReflectionUtils.doWithFields(def.clazz(), new ReflectionUtils.FieldCallback() {
          public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
            ServiceDataVo dataVo = getDataVoByDtoField(field);
            if (dataVo == null) {
              return;
            }
            requestList.add(dataVo);
          }
        });
      }

      // 出参
      final List<ServiceDataVo> responseList = new ArrayList<ServiceDataVo>();
      vo.setResponses(responseList);
      if (clazz.isAnnotationPresent(ApiOut.class)) {
        ApiOut def = clazz.getAnnotation(ApiOut.class);
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
      if (!clazz.isAnnotationPresent(ApiOut.class) && clazz.isAnnotationPresent(ApiOutDTO.class)) {
        ApiOutDTO def = clazz.getAnnotation(ApiOutDTO.class);
        ReflectionUtils.doWithFields(def.clazz(), new ReflectionUtils.FieldCallback() {
          public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
            ServiceDataVo dataVo = getDataVoByDtoField(field);
            if (dataVo == null) {
              return;
            }
            responseList.add(dataVo);
          }
        });
      }
      //返回码
      if (clazz.isAnnotationPresent(ApiCodes.class)) {
        List<CodeVo> codeVos = new ArrayList<CodeVo>();
        ApiCodes def = clazz.getAnnotation(ApiCodes.class);
        for (ApiCode code : def.value()) {
          CodeVo codeVo = new CodeVo();
          codeVo.setCode(code.code());
          codeVo.setDes(code.msg());
          codeVos.add(codeVo);
        }
        vo.setApiCodes(codeVos);
      }
      // 时间轴
      List<TimelineVo> timelines = new ArrayList<TimelineVo>();
      resolveTimeline(clazz, timelines);
      vo.setTimelines(timelines);
      serviceMap.put(vo.getServiceName() + "_" + vo.getVersion(), vo);
    } catch (Throwable e) {
      throw new GenException("分析接口服务异常，", clazz, e);
    }
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
      return "/service/" + (StringUtils.isBlank(ann.serviceName()) ? clazz.getName() : ann.serviceName().replaceAll("/",".")) + "_" + ann.version();
    } else {
      return "";
    }
  }

  private String resolveIsnew(Class<?> clazz) throws GenException {
    try {
      if (!clazz.isAnnotationPresent(ApiTimeline.class))
        return "";

      ApiTimeline ann = clazz.getAnnotation(ApiTimeline.class);
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
    } catch (Throwable e) {
      throw new GenException("分析时间轴是否更新时异常，", clazz, e);
    }
  }

  private boolean checkLength(Integer... list) {
    if (list == null || list.length == 0) return true;

    boolean eq = true;
    Integer cur = list[0];
    for (int i = 1; i < list.length; i++) {
      eq = (list[i].equals(cur));
      if (!eq) break;
      cur = list[i];
    }
    return eq;
  }

}
