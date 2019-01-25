package com.uyoqu.hello.docs.core.vo;

import lombok.Data;

/**
 * Created by zhpeng2 on 2017/9/29.
 */
@Data
public class DtoDataVo {
  private String name;
  private String type;
  private String desc;
  private String remark;
  private String required;
  private String link;
  private String example;

  public DtoDataVo() {
  }

  public DtoDataVo(String name, String type, String desc, String remark, String required) {
    this.name = name;
    this.type = type;
    this.desc = desc;
    this.remark = remark;
    this.required = required;
  }

  public DtoDataVo(String name, String type, String desc, String remark, String required, String example) {
    this.name = name;
    this.type = type;
    this.desc = desc;
    this.remark = remark;
    this.required = required;
    this.example = example;
  }
}
