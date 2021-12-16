package com.uyoqu.hello.docs.core.vo;

import java.util.List;

public class DtoVO {
    private String cnName;
    private String enName;
    private String desc;
    private List<DtoDataVO> data;
    private List<TimelineVO> timelines;
    private String doc;

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<DtoDataVO> getData() {
        return data;
    }

    public void setData(List<DtoDataVO> data) {
        this.data = data;
    }

    public List<TimelineVO> getTimelines() {
        return timelines;
    }

    public void setTimelines(List<TimelineVO> timelines) {
        this.timelines = timelines;
    }
}
