package com.liangxun.university.data;

import com.liangxun.university.entity.ManagerEmp;

import java.util.List;

/**
 * Created by liuzwei on 2015/1/17.
 */
public class ManagerEmpsDATA extends Data {
    private List<ManagerEmp> data;

    public List<ManagerEmp> getData() {
        return data;
    }

    public void setData(List<ManagerEmp> data) {
        this.data = data;
    }
}
