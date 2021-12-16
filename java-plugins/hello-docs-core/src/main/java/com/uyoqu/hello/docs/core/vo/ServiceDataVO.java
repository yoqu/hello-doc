package com.uyoqu.hello.docs.core.vo;

import com.uyoqu.hello.docs.core.annotation.ApiField;
import com.uyoqu.hello.docs.core.annotation.ApiParamIn;
import com.uyoqu.hello.docs.core.annotation.Out;
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
  private String reqType;
  private String example;

  public ServiceDataVO(ApiParamIn in) {
    desc = in.desc();
    link = in.link();
    remark = in.remark();
    required = String.valueOf(in.required());
    type = in.type();
    name = in.param();
    reqType = in.reqType();
  }

  public ServiceDataVO(Out out) {
    desc = out.desc();
    link = out.link();
    remark = out.remark();
    required = String.valueOf(out.required());
    type = out.type();
    name = out.param();
  }

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
