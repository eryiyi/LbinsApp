package com.liangxun.university.data;

import com.liangxun.university.entity.PKTheme;

import java.util.List;

/**
 * Created by liuzwei on 2015/1/17.
 */
public class PkThemesDATA extends Data {
    private List<PKTheme> data;

    public List<PKTheme> getData() {
        return data;
    }

    public void setData(List<PKTheme> data) {
        this.data = data;
    }
}
