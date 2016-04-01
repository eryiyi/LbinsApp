package com.liangxun.university.data;

import com.liangxun.university.entity.PartTime;

import java.util.List;

/**
 * Created by liuzwei on 2015/1/17.
 */
public class PartTimeDATA extends Data {
    private List<PartTime> data;

    public List<PartTime> getData() {
        return data;
    }

    public void setData(List<PartTime> data) {
        this.data = data;
    }
}
