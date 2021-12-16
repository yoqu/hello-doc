package com.uyoqu.hello.docs.core.vo;

import java.util.List;
public class MenuGroupVO {
    private String title;
    private List<MenuVO> subs;
    private transient int sort;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MenuVO> getSubs() {
        return subs;
    }

    public void setSubs(List<MenuVO> subs) {
        this.subs = subs;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
