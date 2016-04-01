package com.liangxun.university.ui;

import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import cn.smssdk.utils.SMSLog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.liangxun.university.R;
import com.liangxun.university.base.ActivityTack;
import com.liangxun.university.base.BaseActivity;
import com.liangxun.university.base.InternetURL;
import com.liangxun.university.data.ProvinceDATA;
import com.liangxun.university.data.SuccessData;
import com.liangxun.university.data.UnivertyDATA;
import com.liangxun.university.entity.Province;
import com.liangxun.university.entity.Univerty;
import com.liangxun.university.receiverMob.SMSBroadcastReceiver;
import com.liangxun.university.util.Constants;
import com.liangxun.university.util.StringUtil;
import com.liangxun.university.widget.popview.CustomerSpinner;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.liangxun.university.R.layout.simple_spinner_item;

/**
 * author: ${zhanghailong}
 * Date: 2015/2/24
 * Time: 11:25
 * 类的功能、说明写在此处.
 */
public class RegistSelectSchoolActivity extends BaseActivity implements View.OnClickListener {
    private TextView regist_sure;
    private CustomerSpinner provinceSpinner;
    private CustomerSpinner universitySpinner;
    private ArrayList<Province> provinces = new ArrayList<Province>();
    private ArrayList<Univerty> universitys = new ArrayList<Univerty>();
    private ArrayList<String> uniersitys_names = new ArrayList<String>();
    private ArrayList<String> provinces_names = new ArrayList<String>();
    private ArrayAdapter<String> provinceAdapter;
    private ArrayAdapter<String> universityAdapter;

    public static String schoolId = "";
    private ImageView registselectschool_back;

    Resources res;
    private EditText mm_emp_mobile;
    private EditText regist_nickname;
    private EditText code;
    private EditText password;
    private EditText surepass;
    private Button btn_code;

    public String phString;//手机号码

    //短信读取
    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_select_school_xml);
        res = getResources();
        //mob短信无GUI
        SMSSDK.initSDK(this, Constants.APPKEY, Constants.APPSECRET, true);
        EventHandler eh=new EventHandler(){

            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);
        initView();
        getProvince();
        //生成广播处理
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();
        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(ACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);
        //注册广播
        this.registerReceiver(mSMSBroadcastReceiver, intentFilter);
        mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void onReceived(String message) {
                //花木通的验证码：8469【掌淘科技】
//                if(!StringUtil.isNullOrEmpty(message) && message.startsWith("花木通")){
                    String codestr = StringUtil.valuteNumber(message);
                    if(!StringUtil.isNullOrEmpty(codestr)){
                        code.setText(codestr);
                    }
//                }
            }
        });

    }

    Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                System.out.println("--------result"+event);
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
//                    Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
                    regist();

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //已经验证
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                }

            } else {
//				((Throwable) data).printStackTrace();
                Toast.makeText(RegistSelectSchoolActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
//					Toast.makeText(MainActivity.this, "123", Toast.LENGTH_SHORT).show();
                int status = 0;
                try {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;

                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    status = object.optInt("status");
                    if (!TextUtils.isEmpty(des)) {
                        Toast.makeText(RegistSelectSchoolActivity.this, des, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    SMSLog.getInstance().w(e);
                }
            }
        };
    };

    public void onDestroy() {
        super.onPause();
        SMSSDK.unregisterAllEventHandler();
        //注销短信监听广播
        this.unregisterReceiver(mSMSBroadcastReceiver);
    };

    /**
     * 2 环信保存失败   1是保存失败  3是手机号存在
     */
    private void regist() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.REGIST_END_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            SuccessData data = getGson().fromJson(s, SuccessData.class);
                            if (data.getCode() == 200) {
                                save(Constants.EMPMOBILE, mm_emp_mobile.getText().toString());
                                save(Constants.EMPPASS, password.getText().toString());
                                save(Constants.EMPNAME, regist_nickname.getText().toString());
                                save(Constants.SCHOOLID, schoolId);
                                ActivityTack.getInstanse().popUntilActivity(LoginActivity.class);
                            } else if (data.getCode() == 2) {
                                Toast.makeText(RegistSelectSchoolActivity.this, R.string.register_error_thirtyone, Toast.LENGTH_SHORT).show();
                            } else if (data.getCode() == 3) {
                                Toast.makeText(RegistSelectSchoolActivity.this, R.string.register_error_thirtytwo, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegistSelectSchoolActivity.this, R.string.register_error_thirty, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegistSelectSchoolActivity.this, R.string.register_error_fourty, Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        regist_sure.setClickable(true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(RegistSelectSchoolActivity.this, R.string.register_error_fourty, Toast.LENGTH_SHORT).show();
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        regist_sure.setClickable(true);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empMobile", mm_emp_mobile.getText().toString());
                params.put("empPass", password.getText().toString());
                params.put("empName", regist_nickname.getText().toString());
                params.put("schoolId", schoolId);
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

    private void initView() {
        regist_nickname = (EditText) this.findViewById(R.id.regist_nickname);
        mm_emp_mobile = (EditText) this.findViewById(R.id.mm_emp_mobile);
        code = (EditText) this.findViewById(R.id.code);
        password = (EditText) this.findViewById(R.id.password);
        surepass = (EditText) this.findViewById(R.id.surepass);
        btn_code = (Button) this.findViewById(R.id.btn_code);

        btn_code.setOnClickListener(this);

        registselectschool_back = (ImageView) this.findViewById(R.id.registselectschool_back);
        registselectschool_back.setOnClickListener(this);
        regist_sure = (TextView) this.findViewById(R.id.regist_sure);
        regist_sure.setOnClickListener(this);

        provinceAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, provinces_names);
        provinceSpinner = (CustomerSpinner) findViewById(R.id.provice_select_school);
        provinceSpinner.setList(provinces_names);
        provinceSpinner.setAdapter(provinceAdapter);

        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    uniersitys_names.clear();
                    Province building = provinces.get(position - 1);
                    String province_name = building.getPname();
                    try {
                        //获取大学列表
                        getUniversity(building.getProvinceId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    universitySpinner.setEnabled(false);//如果没有选择建筑物，让选择楼层不可点击
                    uniersitys_names.clear();
                    uniersitys_names.add("请选择大学");
                    universityAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        universityAdapter = new ArrayAdapter<String>(this, simple_spinner_item, uniersitys_names);
        universitySpinner = (CustomerSpinner) findViewById(R.id.university_select_school);
        universitySpinner.setList(uniersitys_names);
        universitySpinner.setAdapter(universityAdapter);
        universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    Univerty univerty = universitys.get(position - 1);
                    schoolId = univerty.getCoid();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registselectschool_back:
                finish();
                break;
            case R.id.regist_sure:
                if(StringUtil.isNullOrEmpty(mm_emp_mobile.getText().toString())){
                    showMsg(RegistSelectSchoolActivity.this, "请输入手机号码");
                    return;
                }
                if(StringUtil.isNullOrEmpty(code.getText().toString())){
                    showMsg(RegistSelectSchoolActivity.this, "请输入验证码");
                    return;
                }

                if(StringUtil.isNullOrEmpty(password.getText().toString())){
                    showMsg(RegistSelectSchoolActivity.this, "请输入密码");
                    return;
                }
                if(password.getText().toString().length()>18 || password.getText().toString().length()<6){
                    showMsg(RegistSelectSchoolActivity.this, "密码长度在6到18位之间");
                    return;
                }
                if(StringUtil.isNullOrEmpty(surepass.getText().toString())){
                    showMsg(RegistSelectSchoolActivity.this, "请输入确认密码");
                    return;
                }
                if(!password.getText().toString().equals(surepass.getText().toString())){
                    showMsg(RegistSelectSchoolActivity.this, "两次输入密码不一致");
                    return;
                }
                if (StringUtil.isNullOrEmpty(schoolId)) {
                    showMsg(RegistSelectSchoolActivity.this, res.getString(R.string.register_error_school));
                    return;
                }
                if (StringUtil.isNullOrEmpty(regist_nickname.getText().toString())) {
                    showMsg(RegistSelectSchoolActivity.this, "请输入昵称");
                    return;
                }
                SMSSDK.submitVerificationCode("86", phString, code.getText().toString());
//                RegisterPage registerPage = new RegisterPage();
//                registerPage.setRegisterCallback(new EventHandler() {
//                    public void afterEvent(int event, int result, Object data) {
//                        // 解析注册结果
//                        if (result == SMSSDK.RESULT_COMPLETE) {
//                            @SuppressWarnings("unchecked")
//                            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
//                            String country = (String) phoneMap.get("country");
//                            String phone = (String) phoneMap.get("phone");
//                            registerUser(country, phone);
//                        }
//                    }
//                });
//                registerPage.show(this);
                break;
            case R.id.btn_code:
                //验证码
                if(!TextUtils.isEmpty(mm_emp_mobile.getText().toString()) && mm_emp_mobile.getText().toString().length() == 11){
                    SMSSDK.getVerificationCode("86",mm_emp_mobile.getText().toString());//发送请求验证码，手机10s之内会获得短信验证码
                    phString=mm_emp_mobile.getText().toString();
                    btn_code.setClickable(false);//不可点击
                    MyTimer myTimer = new MyTimer(60000,1000);
                    myTimer.start();
                }else {
                    showMsg(RegistSelectSchoolActivity.this, "请输入手机号码");
                    return;
                }
                break;
        }
    }

    class MyTimer extends CountDownTimer {
        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            btn_code.setText(res.getString(R.string.daojishi_three));
            btn_code.setClickable(true);//可点击
        }
        @Override
        public void onTick(long millisUntilFinished) {
            btn_code.setText(res.getString(R.string.daojishi_one) + millisUntilFinished / 1000 + res.getString(R.string.daojishi_two));
        }
    }

    //获取省份列表
    private void getProvince() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_PROVINCE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            ProvinceDATA data = getGson().fromJson(s, ProvinceDATA.class);
                            if (data.getCode() == 200) {
                                provinces.clear();
                                provinces.addAll(data.getData());
                                provinces_names.clear();
                                provinces_names.add("请选择省份");
                                for (Province pro : provinces) {
                                    provinces_names.add(pro.getPname());
                                }
                                provinceAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(RegistSelectSchoolActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegistSelectSchoolActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(RegistSelectSchoolActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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

    private void getUniversity(final String province_uuid) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_COLLEAGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            UnivertyDATA data = getGson().fromJson(s, UnivertyDATA.class);
                            if (data.getCode() == 200) {
                                universitys.clear();
                                universitys.addAll(data.getData());
                                uniersitys_names.clear();
                                uniersitys_names.add("请选择大学");
                                for (Univerty univerty : universitys) {
                                    uniersitys_names.add(univerty.getName());
                                }
                                universitySpinner.setSelection(0);
                                universitySpinner.setEnabled(true);
                                universityAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(RegistSelectSchoolActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegistSelectSchoolActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(RegistSelectSchoolActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("provinceId", province_uuid);
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

    @Override
    public void onResume() {
        super.onResume();
        schoolId = "";
    }
}
