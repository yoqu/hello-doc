package com.uyoqu.hello.docs.core.config;

import lombok.Data;

/**
 * @author: yoqu
 * @date: 2019-01-25
 * @email: yoqulin@qq.com
 **/
@Data
public class Header {
  private String name;

  private String type = "String";

  private String desc;

  private String remark;

  private boolean required = false;

  private String example;

}
