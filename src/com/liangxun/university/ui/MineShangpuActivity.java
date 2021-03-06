package com.liangxun.university.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.liangxun.university.R;
import com.liangxun.university.base.BaseActivity;
import com.liangxun.university.base.InternetURL;
import com.liangxun.university.data.MineStoreDATA;
import com.liangxun.university.entity.MineStore;
import com.liangxun.university.util.Constants;
import com.liangxun.university.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/17.
 * 商家  我的店铺
 */
public class MineShangpuActivity extends BaseActivity implements View.OnClickListener {
    private TextView pricesCount_one;//收入总额
    private TextView pricesCount;//今日收入总额
    private TextView order_number_one;//今日订单
    private TextView order_number_two;//总订单
    private String emp_id;
    private MineStore mineStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        emp_id = getGson().fromJson(getSp().getString(Constants.EMPID, ""), String.class);
        setContentView(R.layout.mine_shangpu_activity);
        initView();
        selectNum();//查询订单数量和今天的收入
    }

    private void initView() {
        pricesCount = (TextView) this.findViewById(R.id.pricesCount);
        pricesCount_one = (TextView) this.findViewById(R.id.pricesCount_one);
        order_number_one = (TextView) this.findViewById(R.id.order_number_one);
        order_number_two = (TextView) this.findViewById(R.id.order_number_two);
    }

    @Override
    public void onClick(View view) {

    }

    public void back(View view){
        finish();
    }
    public void orderClick(View view){
        //订单点击
        Intent intent = new Intent(MineShangpuActivity.this, MineOrdersMngActivity.class);
        startActivity(intent);
    }
    public void goodsClick(View view){
        //商品点击
        Intent intent =  new Intent(MineShangpuActivity.this, MineGoodsActivity.class);
        startActivity(intent);
    }
    public void selectNum(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.SELECT_ORDER_NUM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            MineStoreDATA data = getGson().fromJson(s,MineStoreDATA.class);
                            mineStore = data.getData();
                            initData();
                        } else {
                            Toast.makeText(MineShangpuActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MineShangpuActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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

    //填充数据
    void initData(){
        //填充数据
        pricesCount.setText(String.valueOf(mineStore.getPricesAllDay()));
        pricesCount_one.setText(String.valueOf(mineStore.getPricesAll()));
        order_number_one.setText(mineStore.getNumberDay());
        order_number_two.setText(mineStore.getNumberAll());
    }
}
