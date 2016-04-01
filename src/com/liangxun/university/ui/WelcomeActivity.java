package com.liangxun.university.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.liangxun.university.R;
import com.liangxun.university.base.BaseActivity;
import com.liangxun.university.base.InternetURL;
import com.liangxun.university.data.AdDATA;
import com.liangxun.university.entity.Ad;
import com.liangxun.university.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/19.
 */
public class WelcomeActivity extends BaseActivity implements View.OnClickListener,Runnable {
    private  Ad ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
//        initAd();
        // 启动一个线程
        new Thread(WelcomeActivity.this).start();
    }

    @Override
    public void onClick(View view) {}

    @Override
    public void run() {
        try {
            // 3秒后跳转到登录界面
            Thread.sleep(3000);
//            if (ad != null){
//                Intent intent = new Intent(WelcomeActivity.this, LoadingActivity.class);
//                intent.putExtra("ad",ad);
//                startActivity(intent);
//                finish();
//            } else{
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                finish();
//            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //去取广告图片
    private void initAd() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_BIGAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            AdDATA data = getGson().fromJson(s, AdDATA.class);
                            if (data.getCode() == 200) {
                                ad = data.getData();
                            }else {
                                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                                finish();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (ad != null){
                            Intent intent = new Intent(WelcomeActivity.this, LoadingActivity.class);
                            intent.putExtra("ad",ad);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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
