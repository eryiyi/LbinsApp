package com.liangxun.university.ui;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.liangxun.university.R;
import com.liangxun.university.adapter.MineSchoolsAdapter;
import com.liangxun.university.base.BaseActivity;
import com.liangxun.university.base.InternetURL;
import com.liangxun.university.data.MineShangjiasDATA;
import com.liangxun.university.entity.ContractSchool;
import com.liangxun.university.util.Constants;
import com.liangxun.university.util.StringUtil;
import com.liangxun.university.widget.CustomProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/30.
 * 代理商开通的学校
 */
public class MineSchoolsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mine_school_menu;
    private ListView mine_schools_lstv;
    private List<ContractSchool> lists = new ArrayList<ContractSchool>();
    private MineSchoolsAdapter adapter;
    private String emp_id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_schools);
        initView();
        emp_id = getGson().fromJson(getSp().getString(Constants.EMPID, ""), String.class);
        progressDialog = new CustomProgressDialog(MineSchoolsActivity.this , "正在加载", R.anim.frame_paopao);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();
    }

    private void initView() {
        mine_school_menu = (ImageView) this.findViewById(R.id.mine_school_menu);
        mine_school_menu.setOnClickListener(this);
        mine_schools_lstv = (ListView) this.findViewById(R.id.mine_schools_lstv);
        adapter = new MineSchoolsAdapter(lists, this);
        mine_schools_lstv.setAdapter(adapter);
        mine_schools_lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_school_menu:
                finish();
                break;
        }
    }

    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_SCHOOLS_BY_JXS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            MineShangjiasDATA data = getGson().fromJson(s, MineShangjiasDATA.class);
                            if (data.getCode() == 200) {
                                lists.clear();
                                lists.addAll(data.getData());
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MineSchoolsActivity.this, R.string.report_error_two, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MineSchoolsActivity.this, R.string.report_error_two, Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(MineSchoolsActivity.this, R.string.report_error_two, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", emp_id);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }

}
