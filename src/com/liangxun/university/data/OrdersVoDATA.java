package com.liangxun.university.data;

import com.liangxun.university.entity.OrderVo;

import java.util.List;

/**
 * Created by liuzwei on 2015/1/17.
 */
public class OrdersVoDATA extends Data {
    private List<OrderVo> data;

    public List<OrderVo> getData() {
        return data;
    }

    public void setData(List<OrderVo> data) {
        this.data = data;
    }
}
