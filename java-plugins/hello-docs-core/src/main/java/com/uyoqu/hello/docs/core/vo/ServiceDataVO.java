package com.uyoqu.hello.docs.core.vo;

import com.uyoqu.hello.docs.core.annotation.ApiField;
import com.uyoqu.hello.docs.core.annotation.Out;
import com.uyoqu.hello.docs.core.definition.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDataVO {
  private String name;
  private String type;
  private String desc;
  private String required;
  private String remark;
  private String link;
  private RequestType reqType;
  private String example;



  public ServiceDataVO(ApiField basicFiled) {
    if (basicFiled == null) {
      return;
    }
    setRequired(String.valueOf(basicFiled.required()));
    setName(basicFiled.param());
    setType(basicFiled.type());
    setLink(basicFiled.link());
    setDesc(basicFiled.desc());
    setRemark(basicFiled.remark());
    setReqType(basicFiled.reqType());
  }
}
