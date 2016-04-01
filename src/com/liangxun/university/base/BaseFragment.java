package com.liangxun.university.base;

import android.os.Bundle;
import com.liangxun.university.widget.CustomProgressDialog;

/**
 * Created by liuzwei on 2015/1/17.
 */
public class BaseFragment extends MyBaseFragment {
    public CustomProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
