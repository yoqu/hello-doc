package com.uyoqu.hello.docs.plugin.vo;

import com.uyoqu.hello.docs.core.annotation.ApiBasicFiled;
import com.uyoqu.hello.docs.core.annotation.In;
import com.uyoqu.hello.docs.core.annotation.Out;

/**
 * Created by zhpeng2 on 2017/9/29.
 */
public class ServiceDataVo {
  private String name;
  private String type;
  private String desc;
  private String required;
  private String remark;
  private String link;

  public ServiceDataVo() {
  }

  public ServiceDataVo(In in) {
    desc = in.desc();
    link = in.link();
    remark = in.remark();
    required = String.valueOf(in.required());
    type = in.type();
    name = in.param();
  }

  public ServiceDataVo(Out out) {
    desc = out.desc();
    link = out.link();
    remark = out.remark();
    required = String.valueOf(out.required());
    type = out.type();
    name = out.param();
  }

  public ServiceDataVo(ApiBasicFiled basicFiled) {
    if (basicFiled == null) {
      return;
    }
    setRequired(String.valueOf(basicFiled.required()));
    setName(basicFiled.param());
    setType(basicFiled.type());
    setLink(basicFiled.link());
    setDesc(basicFiled.desc());
    setRemark(basicFiled.remark());
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getRequired() {
    return required;
  }

  public void setRequired(String required) {
    this.required = required;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}
