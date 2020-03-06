package com.uyoqu.hello.docs.core.vo;

import com.uyoqu.hello.docs.core.annotation.ApiService;

import java.util.List;

/**
 * Created by zhpeng2 on 2017/9/29.
 */
public class ServiceVo {
  private String cnName;
  private String serviceName;
  private String serviceFullName;
  private String desc;
  private String version;
  private String method;
  private String group;
  private List<ServiceDataVo> requests;
  private List<ServiceDataVo> headers;
  private List<ServiceDataVo> responses;
  private List<CodeVo> apiCodes;
  private List<TimelineVo> timelines;
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

  public List<CodeVo> getApiCodes() {
    return apiCodes;
  }

  public void setApiCodes(List<CodeVo> apiCodes) {
    this.apiCodes = apiCodes;
  }

  public ServiceVo() {
  }

  public ServiceVo(ApiService apiService) {
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

  public List<ServiceDataVo> getRequests() {
    return requests;
  }

  public void setRequests(List<ServiceDataVo> requests) {
    this.requests = requests;
  }

  public List<ServiceDataVo> getHeaders() {
    return headers;
  }

  public void setHeaders(List<ServiceDataVo> headers) {
    this.headers = headers;
  }

  public List<ServiceDataVo> getResponses() {
    return responses;
  }

  public void setResponses(List<ServiceDataVo> responses) {
    this.responses = responses;
  }

  public List<TimelineVo> getTimelines() {
    return timelines;
  }

  public void setTimelines(List<TimelineVo> timelines) {
    this.timelines = timelines;
  }
}
