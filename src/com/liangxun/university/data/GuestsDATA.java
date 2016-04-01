package com.liangxun.university.data;

import com.liangxun.university.entity.Guest;

import java.util.List;

/**
 * Created by liuzwei on 2015/1/17.
 */
public class GuestsDATA extends Data {
    private List<Guest> data;

    public List<Guest> getData() {
        return data;
    }

    public void setData(List<Guest> data) {
        this.data = data;
    }
}
