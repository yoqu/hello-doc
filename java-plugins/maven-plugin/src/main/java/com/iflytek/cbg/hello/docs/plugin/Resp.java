package com.iflytek.cbg.hello.docs.plugin;

import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author yoqu
 * @date 2018/5/21 - 14:59
 */
public class Resp {

    @Parameter
    private String name;

    @Parameter(defaultValue = "String")
    private String type = "String";

    @Parameter(defaultValue = "")
    private String desc;

    @Parameter(defaultValue = "")
    private String remark;

    @Parameter(defaultValue = "false")
    private boolean required = false;

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

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

}
