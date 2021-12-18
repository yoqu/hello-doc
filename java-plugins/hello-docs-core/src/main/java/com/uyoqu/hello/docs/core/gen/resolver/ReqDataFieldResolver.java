package com.uyoqu.hello.docs.core.gen.resolver;

import com.uyoqu.hello.docs.core.annotation.ApiField;
import com.uyoqu.hello.docs.core.annotation.ApiInDTO;
import com.uyoqu.hello.docs.core.vo.DataVO;
import com.uyoqu.hello.docs.core.vo.ReqDataVO;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReqDataFieldResolver extends DataFieldsResolver {

  private List<ReqDataVO> reqDataVOS = new ArrayList<>();

  public ReqDataFieldResolver(Class<? extends Annotation>[] groups) {
    super(groups);
  }

  public static List<ReqDataVO> find(ApiInDTO def) {
    ReqDataFieldResolver callback = new ReqDataFieldResolver(def.groups());
    ReflectionUtils.doWithFields(def.clazz(), callback);
    return callback.getReqDataVOS();
  }

  public static List<ReqDataVO> findReqs(Class<?> clazz) {
    ReqDataFieldResolver callback = new ReqDataFieldResolver(null);
    ReflectionUtils.doWithFields(clazz, callback);
    return callback.getReqDataVOS();
  }

  @Override
  protected DataVO resolve(Field field) {
    ApiField basicField = field.getAnnotation(ApiField.class);
    if (basicField == null) {
      return null;
    }
    ReqDataVO dataVO = new ReqDataVO();
    convert(dataVO, field);
    dataVO.setReqType(basicField.reqType());
    reqDataVOS.add(dataVO);
    return dataVO;
  }

  public List<ReqDataVO> getReqDataVOS() {
    return reqDataVOS;
  }
}
