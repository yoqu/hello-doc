package com.uyoqu.hello.docs.core.gen.resolver;

import com.uyoqu.hello.docs.core.annotation.ApiDTO;
import com.uyoqu.hello.docs.core.annotation.ApiIn;
import com.uyoqu.hello.docs.core.annotation.ApiInDTO;
import com.uyoqu.hello.docs.core.annotation.ApiParam;
import com.uyoqu.hello.docs.core.definition.RequestType;
import com.uyoqu.hello.docs.core.exception.ParameterErrorException;
import com.uyoqu.hello.docs.core.vo.DataVO;
import com.uyoqu.hello.docs.core.vo.ReqDataVO;
import lombok.var;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class ApiInResolver implements Resolver {

  public List<? extends DataVO> resolve2(AnnotatedElement element) {
    final List<ReqDataVO> reqDataVOS = new ArrayList<>();
    var isAdded = false;
    if (element.isAnnotationPresent(ApiIn.class)) {
      ApiIn def = element.getAnnotation(ApiIn.class);
      for (ApiParam in : def.value()) {
        ReqDataVO dataVo = new ReqDataVO(in);
        reqDataVOS.add(dataVo);
      }
      isAdded = true;
    }
    //如果没有使用APiIn注解使用了APIInDto的注解
    if (!isAdded && element.isAnnotationPresent(ApiInDTO.class)) {
      ApiInDTO def = element.getAnnotation(ApiInDTO.class);
      reqDataVOS.addAll(ReqDataFieldResolver.find(def));
      isAdded = true;
    }
    //处理method类型的数据
    if (!isAdded && element instanceof Method) {
      for (Parameter parameter : ((Method) element).getParameters()) {
        if (parameter.isAnnotationPresent(ApiInDTO.class)) {
          reqDataVOS.addAll(ReqDataFieldResolver.find(parameter.getAnnotation(ApiInDTO.class)));
          break;
        }
        if (parameter.getType().isAnnotationPresent(ApiDTO.class)) {
          Class<?> clazz2 = parameter.getType();
          if (clazz2 == null) {
            throw new ParameterErrorException("parameter type error.");
          }
          if (String.class.isAssignableFrom(clazz2) || clazz2.isPrimitive()) {//处理方法的基础类型
            ReqDataVO reqDataVO = new ReqDataVO(parameter.getName(), clazz2.getSimpleName(), "", "", "true", RequestType.PARAM, null);
            reqDataVOS.add(reqDataVO);
          } else {
            reqDataVOS.addAll(ReqDataFieldResolver.find(clazz2.getAnnotation(ApiInDTO.class)));
          }
          break;
        }
        if (parameter.isAnnotationPresent(ApiParam.class)) {
          ApiParam in = parameter.getAnnotation(ApiParam.class);
          reqDataVOS.add(new ReqDataVO(in));
        }
      }
    }
    return reqDataVOS;
  }

  @Override
  public List<DataVO> resolve(AnnotatedElement element) {
    return (List<DataVO>) resolve2(element);
  }
}
