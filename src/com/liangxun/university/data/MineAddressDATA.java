package com.liangxun.university.data;

import com.liangxun.university.entity.ShoppingAddress;

import java.util.List;

/**
 * Created by liuzwei on 2015/1/17.
 */
public class MineAddressDATA extends Data {
    private List<ShoppingAddress> data;

    public List<ShoppingAddress> getData() {
        return data;
    }

    public void setData(List<ShoppingAddress> data) {
        this.data = data;
    }
}
