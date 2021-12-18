package com.uyoqu.hello.docs.core.gen;


import com.uyoqu.hello.docs.core.definition.RequestType;
import com.uyoqu.hello.docs.core.vo.DataVO;
import com.uyoqu.hello.docs.core.vo.ReqDataVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yoqu
 * @date 2018/4/17 - 20:46
 */
@Data
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

  private List<ReqDataVO> requests;

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

    public Builder request(String name, String type, String desc, String remark, boolean required, RequestType requestType, String example) {
      ReqDataVO dataVo = new ReqDataVO(name, type, desc, remark, String.valueOf(required), requestType, example);
      if (apiInfo.requests == null) {
        apiInfo.requests = new ArrayList<>();
      }
      apiInfo.requests.add(dataVo);
      return this;
    }

    public ApiInfo build() {
      return apiInfo;
    }
  }
}
