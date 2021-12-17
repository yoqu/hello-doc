package com.uyoqu.hello.docs.core.gen.resolver;

import com.uyoqu.hello.docs.core.annotation.ApiField;
import com.uyoqu.hello.docs.core.annotation.ApiOutDTO;
import com.uyoqu.hello.docs.core.vo.DataVO;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DataFieldsResolver extends DataResolver implements ReflectionUtils.FieldCallback {
  private final List<DataVO> dataVos = new ArrayList<>();
  Class<? extends Annotation>[] groups;

  public DataFieldsResolver(Class<? extends Annotation>[] groups) {
    this.groups = groups;
  }

  public static List<DataVO> find(ApiOutDTO outDTO, Class<?> maybyClazz) {
    DataFieldsResolver callback = new DataFieldsResolver(outDTO.groups());
    ReflectionUtils.doWithFields(outDTO.clazz() != null ? outDTO.clazz() : maybyClazz, callback);
    return callback.getDataVos();
  }


  public static List<DataVO> find(Class<?> clazz) {
    DataFieldsResolver callback = new DataFieldsResolver(null);
    ReflectionUtils.doWithFields(clazz, callback);
    return callback.getDataVos();
  }

  private void addDataVo(DataVO dataVo) {
    if (dataVo == null) {
      return;
    }
    dataVos.add(dataVo);
  }

  protected DataVO resolve(Field field) {
    ApiField basicField = field.getAnnotation(ApiField.class);
    if (basicField == null) {
      return null;
    }
    DataVO dataVO = new DataVO();
    convert(dataVO, field);
    return dataVO;
  }

  public void doWith(Field field) throws IllegalArgumentException {
    if (groups != null && groups.length > 0) {
      for (Class<? extends Annotation> group : groups) {
        ApiField apiField = field.getAnnotation(ApiField.class);
        if (apiField == null) {
          return;
        }
        for (Class<? extends Annotation> showGroup : apiField.showGroup()) {
          if (showGroup.isAssignableFrom(group)) {
            addDataVo(resolve(field));
            return;
          }
        }
      }
    } else {
      addDataVo(resolve(field));
    }
  }

  public List<DataVO> getDataVos() {
    return dataVos;
  }
}
