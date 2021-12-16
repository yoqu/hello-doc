package com.uyoqu.hello.docs.core.definition;

public enum RequestTypeEnum {
  PARAM("param"), FORM_DATA("form_data"), PAYLOAD("body");
  private final String name;

  RequestTypeEnum(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
