package com.uyoqu.hello.docs.plugin.vo;

import java.util.List;

/**
 * Created by zhpeng2 on 2017/9/29.
 */
public class MenuGroupVo {
    private String title;
    private List<MenuVo> subs;
    private transient int sort;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MenuVo> getSubs() {
        return subs;
    }

    public void setSubs(List<MenuVo> subs) {
        this.subs = subs;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
