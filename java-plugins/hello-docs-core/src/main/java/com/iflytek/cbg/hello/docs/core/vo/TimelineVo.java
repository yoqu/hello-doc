package com.iflytek.cbg.hello.docs.core.vo;

/**
 * Created by zhpeng2 on 2017/9/29.
 */
public class TimelineVo {
    private String time;
    private String content;
    private String url;

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
