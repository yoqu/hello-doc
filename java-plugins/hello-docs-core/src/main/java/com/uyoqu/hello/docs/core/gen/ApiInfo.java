package com.uyoqu.hello.docs.core.gen;


import com.uyoqu.hello.docs.core.vo.DtoDataVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yoqu
 * @date 2018/4/17 - 20:46
 */
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

    private List<DtoDataVO> header;

    private List<DtoDataVO> resp;

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

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }


    public Integer getFinishCount() {
        return finishCount;
    }

    public void setFinishCount(Integer finishCount) {
        this.finishCount = finishCount;
    }

    public Integer getIncCount() {
        return incCount;
    }

    protected void setIncCount(Integer incCount) {
        this.incCount = incCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public List<DtoDataVO> getHeader() {
        return header;
    }

    public void setHeader(List<DtoDataVO> header) {
        this.header = header;
    }


    public static class Builder {
        private ApiInfo basicInfo;

        public Builder() {
            basicInfo = new ApiInfo();
        }

        public Builder name(String name) {
            basicInfo.name = name;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            basicInfo.baseUrl = baseUrl;
            return this;
        }

        public Builder doc(String doc) {
            basicInfo.doc = doc;
            return this;
        }


        public Builder copyright(String copyright) {
            basicInfo.copyright = copyright;
            return this;
        }


        public Builder enName(String enName) {
            basicInfo.enName = enName;
            return this;
        }

        public Builder buildTime(String buildTime) {
            basicInfo.buildTime = buildTime;
            return this;
        }

        public Builder header(String name, String type, String desc, String remark, boolean required, String example) {
            DtoDataVO dataVo = new DtoDataVO(name, type, desc, remark, String.valueOf(required), example);
            if (basicInfo.header == null) {
                basicInfo.header = new ArrayList<DtoDataVO>();
            }
            basicInfo.header.add(dataVo);
            return this;
        }

        public Builder tip(String tip) {
            if (basicInfo.tips == null) {
                basicInfo.tips = new ArrayList<String>();
            }
            basicInfo.tips.add(tip);
            return this;
        }

        public Builder response(String name, String type, String desc, String remark, boolean required) {
            DtoDataVO dataVo = new DtoDataVO(name, type, desc, remark, String.valueOf(required));
            if (basicInfo.resp == null) {
                basicInfo.resp = new ArrayList<DtoDataVO>();
            }
            basicInfo.resp.add(dataVo);
            return this;
        }

        public ApiInfo build() {
            return basicInfo;
        }

    }
}
