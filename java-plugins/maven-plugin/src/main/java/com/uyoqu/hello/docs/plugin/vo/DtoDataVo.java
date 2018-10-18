package com.uyoqu.hello.docs.plugin.vo;

/**
 * Created by zhpeng2 on 2017/9/29.
 */
public class DtoDataVo {
    private String name;
    private String type;
    private String desc;
    private String remark;
    private String required;
    private String link;

    public DtoDataVo() {
    }

    public DtoDataVo(String name, String type, String desc, String remark, String required) {
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.remark = remark;
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
