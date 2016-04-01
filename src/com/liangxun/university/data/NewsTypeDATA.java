package com.liangxun.university.data;

import com.liangxun.university.entity.NewsClassify;

import java.util.List;

/**
 * Created by liuzwei on 2015/1/17.
 */
public class NewsTypeDATA extends Data {
    private List<NewsClassify> data;

    public List<NewsClassify> getData() {
        return data;
    }

    public void setData(List<NewsClassify> data) {
        this.data = data;
    }
}
