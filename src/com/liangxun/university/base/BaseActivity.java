package com.liangxun.university.base;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.easemob.chat.EMMessage;
import com.easemob.util.EasyUtils;
import com.google.gson.Gson;
import com.liangxun.university.MainActivity;
import com.liangxun.university.R;
import com.liangxun.university.huanxin.chat.utils.CommonUtils;
import com.liangxun.university.upload.MultiPartStringRequest;
import com.liangxun.university.widget.CustomProgressDialog;
import com.umeng.analytics.MobclickAgent;
import com.yixia.camera.demo.UniversityApplication;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Created by liuzwei on 2014/11/11.
 */
public class BaseActivity extends FragmentActivity{
    public CustomProgressDialog progressDialog;
    private static final int notifiId = 11;
    protected NotificationManager notificationManager;
    /**
     * 屏幕的宽度和高度
     */
    protected int mScreenWidth;
    protected int mScreenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        /**
         * 获取屏幕宽度和高度
         */
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;

        ActivityTack.getInstanse().addActivity(this);
    }

    /**
     * Toast的封装
     * @param mContext 上下文，来区别哪一个activity调用的
     * @param msg 你希望显示的值。
     */
    public static void showMsg(Context mContext,String msg) {
        Toast toast=new Toast(mContext);
        toast= Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);//设置居中
        toast.show();//显示,(缺了这句不显示)
    }

    /**
     * 获取当前Application
     *
     * @return
     */
    public UniversityApplication getMyApp() {
        return (UniversityApplication) getApplication();
    }

    //存储sharepreference
    public void save(String key, Object value) {
        getMyApp().getSp().edit()
                .putString(key, getMyApp().getGson().toJson(value))
                .commit();
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下
     * 如果不需要，注释掉即可
     *
     * @param message
     */
    protected void notifyNewMessage(EMMessage message, String empName) {
        //如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
        //以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)
        if (!EasyUtils.isAppRunningForeground(this)) {
            return;
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getApplicationInfo().icon)
                .setWhen(System.currentTimeMillis()).setAutoCancel(true);

        String ticker = CommonUtils.getMessageDigest(message, this);
        String st = getResources().getString(R.string.expression);
        if (message.getType() == EMMessage.Type.TXT)
            ticker = ticker.replaceAll("\\[.{2,3}\\]", st);
        //设置状态栏提示
        mBuilder.setTicker(empName + ": " + ticker);

        //必须设置pendingintent，否则在2.3的机器上会有bug
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notifiId, intent, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pendingIntent);

        Notification notification = mBuilder.build();
        notificationManager.notify(notifiId, notification);
        notificationManager.cancel(notifiId);
    }

    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }

    /**
     * 获取Volley请求队列
     *
     * @return
     */
    public RequestQueue getRequestQueue() {
        return getMyApp().getRequestQueue();
    }

    /**
     * 获取Gson
     *
     * @return
     */
    public Gson getGson() {
        return getMyApp().getGson();
    }

    /**
     * 获取SharedPreferences
     *
     * @return
     */
    public SharedPreferences getSp() {
        return getMyApp().getSp();
    }

    /**
     * 获取自定义线程池
     *
     * @return
     */
    public ExecutorService getLxThread() {
        return getMyApp().getLxThread();
    }



    public void addPutUploadFileRequest(final String url,
                                        final Map<String, File> files, final Map<String, String> params,
                                        final Response.Listener<String> responseListener, final Response.ErrorListener errorListener,
                                        final Object tag) {
        if (null == url || null == responseListener) {
            return;
        }

        MultiPartStringRequest multiPartRequest = new MultiPartStringRequest(
                Request.Method.POST, url, responseListener, errorListener) {

            @Override
            public Map<String, File> getFileUploads() {
                return files;
            }

            @Override
            public Map<String, String> getStringUploads() {
                return params;
            }

        };
        multiPartRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getRequestQueue().add(multiPartRequest);
    }

//    /**
//     * 0 注册   1找回密码   2重置账号
//     */
//    //初始化短信代码
//    public void initSDK(int i) {
//        typeCard = i;
//        // 初始化短信SDK
//        SMSSDK.initSDK(this, APPKEY, APPSECRET);
//        final Handler handler = new Handler(this);
//        EventHandler eventHandler = new EventHandler() {
//            public void afterEvent(int event, int result, Object data) {
//                Message msg = new Message();
//                msg.arg1 = event;
//                msg.arg2 = result;
//                msg.obj = data;
//                handler.sendMessage(msg);
//            }
//        };
//        // 注册回调监听接口
//        SMSSDK.registerEventHandler(eventHandler);
//        ready = true;
//    }
//
//    public boolean handleMessage(Message msg) {
//        if (pd != null && pd.isShowing()) {
//            pd.dismiss();
//        }
//
//        int event = msg.arg1;
//        int result = msg.arg2;
//        Object data = msg.obj;
//        if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
//            // 短信注册成功后，返回MainActivity,然后提示新好友
//            if (result == SMSSDK.RESULT_COMPLETE) {
////                Toast.makeText(this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
//
//            } else {
//                ((Throwable) data).printStackTrace();
//            }
//        } else if (event == SMSSDK.EVENT_GET_NEW_FRIENDS_COUNT){
//            if (result == SMSSDK.RESULT_COMPLETE) {
////                refreshViewCount(data);
//            } else {
//                ((Throwable) data).printStackTrace();
//            }
//        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//            //校验手机号
//            try {
////                    HashMap<String, String> mapmobile = (HashMap<String, String>) msg.obj;
//                HashMap<String, Object> res =  (HashMap<String, Object>) msg.obj;
////                    res.put("res", true);
////                    res.put("page", 2);
////                    res.put("phone", data);
//
//                mobileReg = (String) res.get("phone");
//                //验证成功
//                if (!StringUtil.isNullOrEmpty(mobileReg)) {
//                    if (typeCard == 0) {
//                        //注册
//                        Intent next = new Intent(this, RegistTwoActivity.class);
//                        next.putExtra(Constants.REGISTER_SCHOOLID, RegistSelectSchoolActivity.schoolId);
//                        next.putExtra(Constants.REGISTER_MOBILE, mobileReg);
//                        startActivity(next);
//                    }
//                    if (typeCard == 1) {
//                        //找回密码
//                        Intent next = new Intent(this, FindPwrOneActivity.class);
//                        next.putExtra(Constants.REGISTER_MOBILE, mobileReg);
//                        startActivity(next);
//                    }
//                    if (typeCard == 2) {
//                        //重置手机号
//                        Intent next = new Intent(this, ReSetMobileActivity.class);
//                        next.putExtra(Constants.REGISTER_MOBILE, mobileReg);
//                        startActivity(next);
//                    }
//                }
//            } catch (Exception e) {
//                mobileReg = "";
//            }
//        }
//        return false;
//    }


//    public boolean handleMessage(Message msg) {
//        if (pd != null && pd.isShowing()) {
//            pd.dismiss();
//        }
//        int event = msg.arg1;
//        int result = msg.arg2;
//        Object data = msg.obj;
//        if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
//
//        } else if (event == SMSSDK.EVENT_GET_NEW_FRIENDS_COUNT) {
//
//        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//            //校验手机号
//            try {
//                HashMap<String, String> mapmobile = (HashMap<String, String>) msg.obj;
//                mobileReg = mapmobile.get("phone");
//                //验证成功
//                if (!StringUtil.isNullOrEmpty(mobileReg)) {
//                    if (typeCard == 0) {
//                        //注册
//                        Intent next = new Intent(this, RegistTwoActivity.class);
//                        next.putExtra(Constants.REGISTER_SCHOOLID, RegistSelectSchoolActivity.schoolId);
//                        next.putExtra(Constants.REGISTER_MOBILE, mobileReg);
//                        startActivity(next);
//                    }
//                    if (typeCard == 1) {
//                        //找回密码
//                        Intent next = new Intent(this, FindPwrOneActivity.class);
//                        next.putExtra(Constants.REGISTER_MOBILE, mobileReg);
//                        startActivity(next);
//                    }
//                    if (typeCard == 2) {
//                        //重置手机号
//                        Intent next = new Intent(this, ReSetMobileActivity.class);
//                        next.putExtra(Constants.REGISTER_MOBILE, mobileReg);
//                        startActivity(next);
//                    }
//                }
//            } catch (Exception e) {
//                mobileReg = "";
//            }
//        } else if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
//        }
//        return false;
//    }

    protected void onDestroy() {
//        if (ready) {
            // 销毁回调监听接口
//            SMSSDK.unregisterAllEventHandler();
//        }
        ActivityTack.getInstanse().removeActivity(this);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }
}
