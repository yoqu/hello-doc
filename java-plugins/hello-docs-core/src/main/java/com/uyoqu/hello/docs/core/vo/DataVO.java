package com.uyoqu.hello.docs.core.vo;

import com.uyoqu.hello.docs.core.annotation.ApiHeader;
import com.uyoqu.hello.docs.core.annotation.ApiParam;
import com.uyoqu.hello.docs.core.annotation.Out;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataVO {
  private String name;
  private String type;
  private String desc;
  private String remark;
  private String required;
  private String link;
  private String example;

  public DataVO(ApiParam in) {
    setDesc(in.desc());
    setLink(in.link());
    setRemark(in.remark());
    setRequired(String.valueOf(in.required()));
    setType(in.type());
    setName(in.name());
  }

  public DataVO(String name, String type, String desc, String remark, String required) {
    this.name = name;
    this.type = type;
    this.desc = desc;
    this.remark = remark;
    this.required = required;
  }

  public DataVO(String name, String type, String desc, String remark, String required, String example) {
    this.name = name;
    this.type = type;
    this.desc = desc;
    this.remark = remark;
    this.required = required;
    this.example = example;
  }

  public DataVO(Out out) {
    desc = out.desc();
    link = out.link();
    remark = out.remark();
    required = String.valueOf(out.required());
    type = out.type();
    name = out.param();
  }
  public DataVO(ApiHeader header) {
    setRequired(String.valueOf(header.required()));
    setName(header.name());
    setType(header.type());
    setDesc(header.desc());
  }
}
