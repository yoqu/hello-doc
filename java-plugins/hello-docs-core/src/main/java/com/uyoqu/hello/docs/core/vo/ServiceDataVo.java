package com.uyoqu.hello.docs.core.vo;

import com.uyoqu.hello.docs.core.annotation.ApiField;
import com.uyoqu.hello.docs.core.annotation.In;
import com.uyoqu.hello.docs.core.annotation.Out;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by zhpeng2 on 2017/9/29.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDataVo {
    private String name;
    private String type;
    private String desc;
    private String required;
    private String remark;
    private String link;
    private String example;

    public ServiceDataVo(In in) {
        desc = in.desc();
        link = in.link();
        remark = in.remark();
        required = String.valueOf(in.required());
        type = in.type();
        name = in.param();
    }

    public ServiceDataVo(Out out) {
        desc = out.desc();
        link = out.link();
        remark = out.remark();
        required = String.valueOf(out.required());
        type = out.type();
        name = out.param();
    }

    public ServiceDataVo(ApiField basicFiled) {
        if (basicFiled == null) {
            return;
        }
        setRequired(String.valueOf(basicFiled.required()));
        setName(basicFiled.param());
        setType(basicFiled.type());
        setLink(basicFiled.link());
        setDesc(basicFiled.desc());
        setRemark(basicFiled.remark());
    }
}
