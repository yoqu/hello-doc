package com.uyoqu.hello.docs.core.vo;

import lombok.Data;

import java.util.List;

@Data
public class DtoVO {
    private String cnName;
    private String enName;
    private String desc;
    private List<DataVO> fields;
    private List<TimelineVO> timelines;
    private String doc;
    private String fullName;
}
