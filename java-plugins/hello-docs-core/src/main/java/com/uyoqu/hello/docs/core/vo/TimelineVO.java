package com.uyoqu.hello.docs.core.vo;

import com.uyoqu.hello.docs.core.annotation.Timeline;

public class TimelineVO {
  private String time;
  private String content;
  private String url;

  public TimelineVO() {
  }

  public TimelineVO(Timeline timeline) {
    setTime(timeline.time());
    setContent(timeline.content());
  }

  private transient Class<?> clazz;

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Class<?> getClazz() {
    return clazz;
  }

  public void setClazz(Class<?> clazz) {
    this.clazz = clazz;
  }
}
