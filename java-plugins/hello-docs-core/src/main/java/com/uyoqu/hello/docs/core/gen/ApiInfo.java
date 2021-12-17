package com.uyoqu.hello.docs.core.gen;


import com.uyoqu.hello.docs.core.vo.DataVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yoqu
 * @date 2018/4/17 - 20:46
 */
public class ApiInfo {
  /**
   * 首页文档地址
   */
  private String doc;

  /**
   * 文档前缀网址默认取当前的网址
   */
  private String baseUrl;

  private String name;

  private String enName;

  private String copyright;

  private List<DataVO> headers;

  private List<DataVO> requests;

  private List<DataVO> responses;

  private String buildTime;
  /**
   * 接口总数
   */
  private Integer incCount = 0;
  /**
   * 完成接口总数.
   */
  private Integer finishCount = 0;
  private List<String> tips;

  public String getBuildTime() {
    return buildTime;
  }

  public void setBuildTime(String buildTime) {
    this.buildTime = buildTime;
  }


  public Integer getFinishCount() {
    return finishCount;
  }

  public void setFinishCount(Integer finishCount) {
    this.finishCount = finishCount;
  }

  public Integer getIncCount() {
    return incCount;
  }

  protected void setIncCount(Integer incCount) {
    this.incCount = incCount;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public String getCopyright() {
    return copyright;
  }

  public void setCopyright(String copyright) {
    this.copyright = copyright;
  }

  public String getDoc() {
    return doc;
  }

  public void setDoc(String doc) {
    this.doc = doc;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public List<DataVO> getHeaders() {
    return headers;
  }

  public void setHeaders(List<DataVO> headers) {
    this.headers = headers;
  }

  public List<DataVO> getResponses() {
    return responses;
  }

  public void setResponses(List<DataVO> responses) {
    this.responses = responses;
  }

  public List<String> getTips() {
    return tips;
  }

  public void setTips(List<String> tips) {
    this.tips = tips;
  }

  public List<DataVO> getRequests() {
    return requests;
  }

  public void setRequests(List<DataVO> requests) {
    this.requests = requests;
  }

  public static class Builder {
    private final ApiInfo apiInfo;

    public Builder() {
      apiInfo = new ApiInfo();
    }

    public Builder name(String name) {
      apiInfo.name = name;
      return this;
    }

    public Builder baseUrl(String baseUrl) {
      apiInfo.baseUrl = baseUrl;
      return this;
    }

    public Builder doc(String doc) {
      apiInfo.doc = doc;
      return this;
    }


    public Builder copyright(String copyright) {
      apiInfo.copyright = copyright;
      return this;
    }


    public Builder enName(String enName) {
      apiInfo.enName = enName;
      return this;
    }

    public Builder buildTime(String buildTime) {
      apiInfo.buildTime = buildTime;
      return this;
    }

    public Builder header(String name, String type, String desc, String remark, boolean required, String example) {
      DataVO dataVo = new DataVO(name, type, desc, remark, String.valueOf(required), example);
      if (apiInfo.headers == null) {
        apiInfo.headers = new ArrayList<>();
      }
      apiInfo.headers.add(dataVo);
      return this;
    }

    public Builder tip(String tip) {
      if (apiInfo.tips == null) {
        apiInfo.tips = new ArrayList<>();
      }
      apiInfo.tips.add(tip);
      return this;
    }

    public Builder response(String name, String type, String desc, String remark, boolean required) {
      DataVO dataVo = new DataVO(name, type, desc, remark, String.valueOf(required));
      if (apiInfo.responses == null) {
        apiInfo.responses = new ArrayList<>();
      }
      apiInfo.responses.add(dataVo);
      return this;
    }

    public ApiInfo build() {
      return apiInfo;
    }
  }
}
