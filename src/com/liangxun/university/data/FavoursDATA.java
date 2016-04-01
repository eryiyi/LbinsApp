package com.liangxun.university.data;

import com.liangxun.university.entity.Favour;

import java.util.List;

/**
 * Created by liuzwei on 2015/1/17.
 */
public class FavoursDATA extends Data {
    private List<Favour> data;

    public List<Favour> getData() {
        return data;
    }

    public void setData(List<Favour> data) {
        this.data = data;
    }
}
