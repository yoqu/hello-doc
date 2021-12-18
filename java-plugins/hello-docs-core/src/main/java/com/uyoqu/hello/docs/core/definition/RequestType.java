package com.uyoqu.hello.docs.core.definition;

import java.util.Objects;

public enum RequestType {
  DEFAULT("default"),PARAM("param"), FORM_DATA("form_data"), PAYLOAD("body");
  private final String name;

  RequestType(String name) {
    this.name = name;
  }

  public static RequestType parse(String title) {
    if (title == null || title == "") {
      return RequestType.PARAM;
    }
    for (RequestType value : RequestType.values()) {
      if (Objects.equals(value.getName(), title.toLowerCase())) {
        return value;
      }
    }
    throw new IllegalArgumentException("请求方式异常");
  }

  public String getName() {
    return name;
  }
}
