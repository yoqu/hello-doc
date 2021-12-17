package com.uyoqu.hello.docs.core.vo;

import com.uyoqu.hello.docs.core.annotation.ApiService;
import lombok.Data;

import java.util.List;

@Data
public class ServiceVO {
  private String cnName;
  private String serviceName;
  private String serviceFullName;
  private String desc;
  private String version;
  private String method;
  private String group;
  private List<ReqDataVO> requests;
  private List<DataVO> requestHeaders;
  private List<DataVO> responses;
  private List<CodeVO> apiCodes;
  private List<TimelineVO> timelines;
  private String doc;
  private int finish;
  private Boolean needAuth;

  public ServiceVO() {
  }

  public ServiceVO(ApiService apiService) {
    this.cnName = apiService.cnName();
    this.desc = apiService.desc();
    this.version = apiService.version();
    this.group = apiService.group();
    this.doc = apiService.doc();
    this.finish = apiService.finish();
  }
}
