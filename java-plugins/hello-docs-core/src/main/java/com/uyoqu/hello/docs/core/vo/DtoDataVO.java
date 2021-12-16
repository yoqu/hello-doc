package com.uyoqu.hello.docs.core.vo;

import lombok.Data;

@Data
public class DtoDataVO {
  private String name;
  private String type;
  private String desc;
  private String remark;
  private String required;
  private String link;
  private String example;
  private String reqType;

  public DtoDataVO() {
  }

  public DtoDataVO(String name, String type, String desc, String remark, String required) {
    this.name = name;
    this.type = type;
    this.desc = desc;
    this.remark = remark;
    this.required = required;
  }

  public DtoDataVO(String name, String type, String desc, String remark, String required, String reqType, String example) {
    this.name = name;
    this.type = type;
    this.desc = desc;
    this.remark = remark;
    this.reqType = reqType;
    this.example = example;
    this.required = required;
  }

  public DtoDataVO(String name, String type, String desc, String remark, String required, String example) {
    this.name = name;
    this.type = type;
    this.desc = desc;
    this.remark = remark;
    this.required = required;
    this.example = example;
  }
}
