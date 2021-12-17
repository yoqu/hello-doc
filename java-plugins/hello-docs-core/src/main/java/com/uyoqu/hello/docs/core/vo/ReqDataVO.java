package com.uyoqu.hello.docs.core.vo;

import com.uyoqu.hello.docs.core.annotation.ApiParam;
import com.uyoqu.hello.docs.core.definition.RequestType;

public class ReqDataVO extends DataVO {
  private RequestType reqType;

  public ReqDataVO() {
  }

  public ReqDataVO(ApiParam in) {
    super(in);
    this.reqType = in.reqType();
  }

  public ReqDataVO(String name, String type, String desc, String remark, String required, RequestType reqType, String example) {
    super(name, type, desc, remark, required, example);
    this.reqType = reqType;
  }

  public RequestType getReqType() {
    return reqType;
  }

  public void setReqType(RequestType reqType) {
    this.reqType = reqType;
  }

}
