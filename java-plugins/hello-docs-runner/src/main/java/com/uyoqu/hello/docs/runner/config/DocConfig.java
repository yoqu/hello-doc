package com.uyoqu.hello.docs.runner.config;

import com.uyoqu.hello.docs.core.gen.ApiInfo;
import lombok.Builder;
import lombok.Data;

/**
 * @author: yoqu
 * @date: 2019-01-25
 * @email: yoqulin@qq.com
 **/
@Data
@Builder
public class DocConfig {
  private ApiInfo apiInfo;
  private String[] scanPackage;

  public DocConfig() {
  }

  public DocConfig(ApiInfo apiInfo, String[] scanPackage) {
    this.apiInfo = apiInfo;
    this.scanPackage = scanPackage;
  }
}
