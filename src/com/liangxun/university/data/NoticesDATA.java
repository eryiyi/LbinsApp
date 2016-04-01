package com.liangxun.university.data;

import com.liangxun.university.entity.Notice;

import java.util.List;

/**
 * Created by liuzwei on 2015/1/17.
 */
public class NoticesDATA extends Data {
    private List<Notice> data;

    public List<Notice> getData() {
        return data;
    }

    public void setData(List<Notice> data) {
        this.data = data;
    }
}
