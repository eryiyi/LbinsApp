package com.liangxun.university.data;

import com.liangxun.university.entity.GoodsFavourVO;

import java.util.List;

/**
 * Created by liuzwei on 2015/1/17.
 */
public class GoodsFavourVODATA extends Data {
    private List<GoodsFavourVO> data;

    public List<GoodsFavourVO> getData() {
        return data;
    }

    public void setData(List<GoodsFavourVO> data) {
        this.data = data;
    }
}
