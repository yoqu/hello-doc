package com.uyoqu.hello.docs.core.gen.resolver;

import com.uyoqu.hello.docs.core.annotation.ApiIn;
import com.uyoqu.hello.docs.core.annotation.ApiInDTO;
import com.uyoqu.hello.docs.core.annotation.ApiParam;
import com.uyoqu.hello.docs.core.definition.RequestType;
import com.uyoqu.hello.docs.core.exception.ParameterErrorException;
import com.uyoqu.hello.docs.core.vo.DataVO;
import com.uyoqu.hello.docs.core.vo.ReqDataVO;
import com.uyoqu.hello.docs.core.vo.ServiceVO;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApiInResolver implements Resolver {

  public List<? extends DataVO> resolve2(AnnotatedElement element, ServiceVO vo) {
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
        if (parameter.isAnnotationPresent(RequestHeader.class)
          || parameter.isAnnotationPresent(CookieValue.class)) {//跳过其他参数
          continue;
        }
        if (parameter.isAnnotationPresent(ApiInDTO.class)) {
          if (parameter.getAnnotation(ApiInDTO.class).clazz() != Object.class) {
            reqDataVOS.addAll(ReqDataFieldResolver.find(parameter.getAnnotation(ApiInDTO.class)));
            continue;
          }
        }
        Class<?> clazz2 = parameter.getType();
        if (clazz2 == null) {
          throw new ParameterErrorException("parameter type error.");
        }
        if (parameter.isAnnotationPresent(ApiParam.class)) {
          ApiParam in = parameter.getAnnotation(ApiParam.class);
          ReqDataVO reqDataVO = new ReqDataVO(in);
          if (StringUtils.isBlank(reqDataVO.getName())) {
            reqDataVO.setName(parameter.getName());
          }
          if (StringUtils.isBlank(reqDataVO.getType())) {
            reqDataVO.setType(clazz2.getSimpleName());
          }
          reqDataVOS.add(reqDataVO);
          continue;
        }
        if (String.class.isAssignableFrom(clazz2) || clazz2.isPrimitive()) {//处理方法的基础类型
          ReqDataVO reqDataVO = new ReqDataVO(parameter.getName(), clazz2.getSimpleName(), "", "", "true", RequestType.PARAM, null);
          if (parameter.isAnnotationPresent(RequestParam.class)) {
            RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
            reqDataVO.setName(StringUtils.isNotEmpty(requestParam.name()) ? requestParam.name() : requestParam.value());
            reqDataVO.setRequired(String.valueOf(requestParam.required()));
            if (!Objects.equals(requestParam.defaultValue(), ValueConstants.DEFAULT_NONE)) {
              reqDataVO.setExample(requestParam.defaultValue());
            }
          }
          reqDataVOS.add(reqDataVO);
        } else {
          List<ReqDataVO> bodyDataList = ReqDataFieldResolver.findReqs(clazz2);
          for (ReqDataVO reqDataVO : bodyDataList) {
            if (vo.getMethod().contains("POST") ||
              vo.getMethod().contains("DELETE") ||
              vo.getMethod().contains("PUT")) {
              if (parameter.isAnnotationPresent(RequestBody.class)) {
                reqDataVO.setReqType(RequestType.PAYLOAD);
              } else {
                reqDataVO.setReqType(RequestType.FORM_DATA);
              }
            } else {
              reqDataVO.setReqType(RequestType.PARAM);
            }
          }
          reqDataVOS.addAll(bodyDataList);
        }
      }
    }
    return reqDataVOS;
  }

  @Override
  public List<DataVO> resolve(AnnotatedElement element) {
    throw new UnsupportedOperationException();
  }
}
