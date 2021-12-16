package com.uyoqu.hello.docs.core.vo;

import com.uyoqu.hello.docs.core.annotation.ApiService;

import java.util.List;

public class ServiceVO {
  private String cnName;
  private String serviceName;
  private String serviceFullName;
  private String desc;
  private String version;
  private String method;
  private String group;
  private List<ServiceDataVO> requests;
  private List<ServiceDataVO> headers;
  private List<ServiceDataVO> responses;
  private List<CodeVO> apiCodes;
  private List<TimelineVO> timelines;
  private String doc;
  private int finish;
  private Boolean needAuth;

  public Boolean getNeedAuth() {
    return needAuth;
  }

  public void setNeedAuth(Boolean needAuth) {
    this.needAuth = needAuth;
  }

  public String getServiceFullName() {
    return serviceFullName;
  }

  public void setServiceFullName(String serviceFullName) {
    this.serviceFullName = serviceFullName;
  }

  public List<CodeVO> getApiCodes() {
    return apiCodes;
  }

  public void setApiCodes(List<CodeVO> apiCodes) {
    this.apiCodes = apiCodes;
  }

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

  public int getFinish() {
    return finish;
  }

  public void setFinish(int finish) {
    this.finish = finish;
  }

  public String getDoc() {
    return doc;
  }

  public void setDoc(String doc) {
    this.doc = doc;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public String getCnName() {
    return cnName;
  }

  public void setCnName(String cnName) {
    this.cnName = cnName;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public List<ServiceDataVO> getRequests() {
    return requests;
  }

  public void setRequests(List<ServiceDataVO> requests) {
    this.requests = requests;
  }

  public List<ServiceDataVO> getHeaders() {
    return headers;
  }

  public void setHeaders(List<ServiceDataVO> headers) {
    this.headers = headers;
  }

  public List<ServiceDataVO> getResponses() {
    return responses;
  }

  public void setResponses(List<ServiceDataVO> responses) {
    this.responses = responses;
  }

  public List<TimelineVO> getTimelines() {
    return timelines;
  }

  public void setTimelines(List<TimelineVO> timelines) {
    this.timelines = timelines;
  }
}
