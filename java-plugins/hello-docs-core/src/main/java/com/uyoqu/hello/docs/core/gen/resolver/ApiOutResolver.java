package com.uyoqu.hello.docs.core.gen.resolver;

import com.uyoqu.hello.docs.core.annotation.ApiOut;
import com.uyoqu.hello.docs.core.annotation.ApiOutDTO;
import com.uyoqu.hello.docs.core.annotation.ApiParam;
import com.uyoqu.hello.docs.core.vo.DataVO;
import lombok.var;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ApiOutResolver implements Resolver{

  @Override
  public List<DataVO> resolve(AnnotatedElement element) {
    final List<DataVO> dataVOS = new ArrayList<>();
    var isAdded = false;
    if (element.isAnnotationPresent(ApiOut.class)) {
      ApiOut def = element.getAnnotation(ApiOut.class);
      for (ApiParam out : def.value()) {
        DataVO dataVo = new DataVO(out);
        dataVOS.add(dataVo);
      }
      isAdded = true;
    }
    if (!isAdded && element.isAnnotationPresent(ApiOutDTO.class)) {
      ApiOutDTO def = element.getAnnotation(ApiOutDTO.class);
      Class<?> clazz = def.clazz();
      if (clazz == Object.class && element instanceof Method) {//处理空场景
        clazz = ((Method) element).getReturnType();
      }
      dataVOS.addAll(DataFieldsResolver.find(def, clazz));
      isAdded = true;
    }
    if (!isAdded && element instanceof Method) {
      Class<?> clazz = ((Method) element).getReturnType();
      dataVOS.addAll(DataFieldsResolver.find(clazz));
    }
    return dataVOS;
  }
}
