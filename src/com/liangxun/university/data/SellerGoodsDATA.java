package com.liangxun.university.data;

import com.liangxun.university.entity.SellerGoods;

import java.util.List;

/**
 * Created by liuzwei on 2015/1/17.
 */
public class SellerGoodsDATA extends Data {
    private List<SellerGoods> data;

    public List<SellerGoods> getData() {
        return data;
    }

    public void setData(List<SellerGoods> data) {
        this.data = data;
    }
}
