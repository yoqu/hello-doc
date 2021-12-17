package com.uyoqu.hello.docs.core.gen.resolver;

import com.uyoqu.hello.docs.core.annotation.ApiField;
import com.uyoqu.hello.docs.core.vo.DataVO;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

public class DataResolver {

  protected void convert(DataVO dataVO, Field f) {
    ApiField apiField = f.getAnnotation(ApiField.class);
    dataVO.setRequired(String.valueOf(apiField.required()));
    if (StringUtils.isBlank(apiField.param())) {
      dataVO.setName(f.getName());
    } else {
      dataVO.setName(apiField.param());
    }

    if (StringUtils.isBlank(apiField.type())) {
      dataVO.setType(f.getType().getSimpleName());
    } else {
      dataVO.setType(apiField.type());
    }
    dataVO.setLink(apiField.link());
    dataVO.setDesc(apiField.desc());
    dataVO.setRemark(apiField.remark());
  }
}
