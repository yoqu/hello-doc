package com.uyoqu.hello.docs.core.gen.resolver;

import com.uyoqu.hello.docs.core.annotation.ApiDTO;
import com.uyoqu.hello.docs.core.annotation.ApiField;
import com.uyoqu.hello.docs.core.vo.DataVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public class DataResolver {

  protected void convert(DataVO dataVO, Field f) {
    ApiField apiField = f.getAnnotation(ApiField.class);
    dataVO.setRequired(String.valueOf(apiField.required()));
    if (StringUtils.isBlank(apiField.param())) {
      dataVO.setName(f.getName());
    } else {
      dataVO.setName(apiField.param());
    }
    if (StringUtils.isNotBlank(apiField.type())) {
      dataVO.setType(apiField.type());
    }
    resolveTypeAndLink(f, dataVO);
    dataVO.setDesc(apiField.desc());
    dataVO.setRemark(apiField.remark());
  }


  private void resolveTypeAndLink(Field field, DataVO dataVO) {
    Class<?> fieldClass = field.getType();
    String typeName = fieldClass.getSimpleName();
    if (Collection.class.isAssignableFrom(fieldClass)) {
      ResolvableType type = ResolvableType.forField(field);
      if (type.getGenerics()[0].resolve() != null) {
        if (type.getGenerics()[0].resolve().isAnnotationPresent(ApiDTO.class)) {
          dataVO.setLink(type.getGenerics()[0].resolve().getName());
          typeName = type.getGenerics()[0].resolve().getSimpleName() + "[]";
        }
      }
    } else if (Map.class.isAssignableFrom(fieldClass)) {
      ResolvableType type = ResolvableType.forField(field);
      StringBuilder typeStr = new StringBuilder("Map<");
      if (type.getGeneric(0).resolve() != null) {
        typeStr.append(type.getGeneric(0).resolve().getSimpleName()).append(",");
      } else {
        typeStr.append("?,");
      }
      if (type.getGeneric(1).resolve() != null) {
        if (type.getGenerics()[1].resolve().isAnnotationPresent(ApiDTO.class)) {
          dataVO.setLink(type.getGenerics()[1].resolve().getName());
        }
        typeStr.append(type.getGenerics()[1].resolve().getSimpleName()).append(">");
      } else {
        typeStr.append("?>");
      }
      typeName = typeStr.toString();
    } else if (fieldClass.isAnnotationPresent(ApiDTO.class)) {
      dataVO.setLink(fieldClass.getName());
    }
    if (StringUtils.isEmpty(dataVO.getType())) {
      dataVO.setType(typeName);
    }
  }
}
