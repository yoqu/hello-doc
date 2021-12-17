package com.uyoqu.hello.docs.core.gen.resolver;

import com.uyoqu.hello.docs.core.annotation.ApiHeader;
import com.uyoqu.hello.docs.core.annotation.ApiHeaders;
import com.uyoqu.hello.docs.core.vo.DataVO;
import lombok.var;
import org.springframework.web.bind.annotation.RequestHeader;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class HeaderResolver implements Resolver {

  @Override
  public List<DataVO> resolve(AnnotatedElement element) {
    final List<DataVO> headerList = new ArrayList<>();
    if (element instanceof Method) {
      for (Parameter parameter : ((Method) element).getParameters()) {
        var isFound = false;
        DataVO.DataVOBuilder builder = DataVO.builder();
        if (parameter.isAnnotationPresent(RequestHeader.class)) {
          RequestHeader h = parameter.getAnnotation(RequestHeader.class);
          builder.name(h.name()).required(String.valueOf(h.required()));
          isFound = true;
        }
        if (parameter.isAnnotationPresent(ApiHeader.class)) {
          ApiHeader h = parameter.getAnnotation(ApiHeader.class);
          builder.desc(h.desc()).type(h.type());
          isFound = true;
        }
        if (isFound) {
          DataVO dataVo = builder.build();
          headerList.add(dataVo);
        }
      }
    }
    ApiHeaders apiHeaders = element.getAnnotation(ApiHeaders.class);
    if (apiHeaders != null && apiHeaders.value().length > 0) {
      for (ApiHeader header : apiHeaders.value()) {
        DataVO dataVo = DataVO.builder().desc(header.desc()).name(header.name()).type(header.type()).required(String.valueOf(header.required())).build();
        headerList.add(dataVo);
      }
    }
    return headerList;
  }
}
