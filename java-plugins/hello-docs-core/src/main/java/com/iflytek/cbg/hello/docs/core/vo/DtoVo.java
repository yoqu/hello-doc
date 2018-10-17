package com.iflytek.cbg.hello.docs.core.vo;

import java.util.List;

/**
 * Created by zhpeng2 on 2017/9/29.
 */
public class DtoVo {
    private String cnName;
    private String enName;
    private String desc;
    private List<DtoDataVo> data;
    private List<TimelineVo> timelines;
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

    public List<DtoDataVo> getData() {
        return data;
    }

    public void setData(List<DtoDataVo> data) {
        this.data = data;
    }

    public List<TimelineVo> getTimelines() {
        return timelines;
    }

    public void setTimelines(List<TimelineVo> timelines) {
        this.timelines = timelines;
    }
}
