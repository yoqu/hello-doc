package com.uyoqu.hello.docs.core.gen.resolver;

import com.uyoqu.hello.docs.core.annotation.ApiTimeline;
import com.uyoqu.hello.docs.core.annotation.Timeline;
import com.uyoqu.hello.docs.core.vo.DtoVO;
import com.uyoqu.hello.docs.core.vo.ServiceVO;
import com.uyoqu.hello.docs.core.vo.TimelineVO;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;


public class TimelineResolver {
  protected static final int NEW_TIMELINE_DAY = 7;
  protected static final SimpleDateFormat TIMELINE_SDF = new SimpleDateFormat("yyyy-MM-dd");

  private List<TimelineVO> resolve(AnnotatedElement element, Supplier<String> supplier) {
    final List<TimelineVO> timelineVOS = new ArrayList<>();
    if (element instanceof Method) {
      Method method = (Method) element;
      if (!method.isAnnotationPresent(ApiTimeline.class)) {
        return timelineVOS;
      }
      ApiTimeline ann = method.getAnnotation(ApiTimeline.class);
      if (ann == null || ann.value().length == 0)
        return timelineVOS;
      for (Timeline timeline : ann.value()) {
        TimelineVO vo = new TimelineVO(timeline);
        vo.setUrl(supplier.get());
        timelineVOS.add(vo);
      }
      return timelineVOS;
    } else if (element instanceof Class) {
      Class<?> clazz = (Class<?>) element;
      if (clazz.isAnnotationPresent(ApiTimeline.class)) {
        ApiTimeline ann = clazz.getAnnotation(ApiTimeline.class);
        if (ann == null || ann.value().length == 0) {
          return timelineVOS;
        }
        for (Timeline timeline : ann.value()) {
          TimelineVO vo = new TimelineVO(timeline);
          vo.setUrl(supplier.get());
          timelineVOS.add(vo);
        }
      }
    }
    return timelineVOS;
  }

  public String resolveIsNew(AnnotatedElement element) throws ParseException {
    ApiTimeline ann = element.getAnnotation(ApiTimeline.class);
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
  public List<TimelineVO> resolve(AnnotatedElement element, ServiceVO serviceVO) {
    return resolve(element, () -> resolveUrl(serviceVO));
  }

  public List<TimelineVO> resolve(AnnotatedElement element, DtoVO dtoVO) {
    return resolve(element, () -> resolveUrl(dtoVO));
  }

  public List<TimelineVO> resolveBasic(AnnotatedElement element) {
    return resolve(element, this::resolveBasic);
  }

  public String resolveBasic() {
    return "/basic-definition";
  }

  public String resolveUrl(ServiceVO serviceVO) {
    return "/service/" + serviceVO.getServiceFullName();
  }

  public String resolveUrl(DtoVO dtoVO) {
    return "/dto/" + dtoVO.getFullName();
  }
}
