package com.uyoqu.hello.docs.core.gen;


import com.uyoqu.hello.docs.core.vo.DtoDataVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yoqu
 * @date 2018/4/17 - 20:46
 */
public class ApiInfo {

    private String name;

    private String enName;

    private String copyright;

    private List<DtoDataVo> header;

    private List<DtoDataVo> basicResp;

    private String buildTime;

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    /**
     * 接口总数
     */
    private Integer incCount = 0;

    /**
     * 完成接口总数.
     */
    private Integer finishCount = 0;

    private List<String> basicTip;

    public List<String> getBasicTip() {
        return basicTip;
    }

    public void setBasicTip(List<String> basicTip) {
        this.basicTip = basicTip;
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

    public List<DtoDataVo> getHeader() {
        return header;
    }

    public void setHeader(List<DtoDataVo> header) {
        this.header = header;
    }

    public List<DtoDataVo> getBasicResp() {
        return basicResp;
    }

    public void setBasicResp(List<DtoDataVo> basicResp) {
        this.basicResp = basicResp;
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

        public Builder header(String name, String type, String desc, String remark, boolean required) {
            DtoDataVo dataVo = new DtoDataVo(name, type, desc, remark, String.valueOf(required));
            if (basicInfo.header == null) {
                basicInfo.header = new ArrayList<DtoDataVo>();
            }
            basicInfo.header.add(dataVo);
            return this;
        }

        public Builder tip(String tip) {
            if (basicInfo.basicTip == null) {
                basicInfo.basicTip = new ArrayList<String>();
            }
            basicInfo.basicTip.add(tip);
            return this;
        }

        public Builder response(String name, String type, String desc, String remark, boolean required) {
            DtoDataVo dataVo = new DtoDataVo(name, type, desc, remark, String.valueOf(required));
            if (basicInfo.basicResp == null) {
                basicInfo.basicResp = new ArrayList<DtoDataVo>();
            }
            basicInfo.basicResp.add(dataVo);
            return this;
        }


        public ApiInfo build() {
            return basicInfo;
        }

    }
}
