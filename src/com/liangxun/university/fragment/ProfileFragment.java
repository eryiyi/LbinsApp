package com.liangxun.university.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.liangxun.university.R;
import com.yixia.camera.demo.UniversityApplication;
import com.liangxun.university.adapter.AnimateFirstDisplayListener;
import com.liangxun.university.adapter.ItemMineGridviewAdapter;
import com.liangxun.university.base.BaseFragment;
import com.liangxun.university.base.InternetURL;
import com.liangxun.university.data.EmpDATA;
import com.liangxun.university.entity.Emp;
import com.liangxun.university.huanxin.chat.activity.ContactlistActivity;
import com.liangxun.university.ui.*;
import com.liangxun.university.util.Constants;
import com.liangxun.university.util.StringUtil;
import com.liangxun.university.widget.PictureGridview;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的主页
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener {
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private TextView profile_name;//标题
    private ImageView profile_cover;//头像
    private TextView profile_nickname;//昵称
    private TextView profile_sign;//签名
    private TextView level_title;//等级
    private ImageView profile_sex;//性别图片
    private ImageView profile_type;//类别  商家还是代理

    private String emp_id = "";//当前登陆者UUID
    private Emp emp;//被访问用户的资料

    private String empType = "";

    //功能项
    private PictureGridview mine_gridview;
    private ItemMineGridviewAdapter adapter;
    private List<Integer> pics = new ArrayList<>();
    //头部区域
    private RelativeLayout liner_one;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
        emp_id = getGson().fromJson(getSp().getString(Constants.EMPID, ""), String.class);
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, null);
        initView(view);
        return view;
    }

    public void initView(View view) {
        liner_one = (RelativeLayout) view.findViewById(R.id.liner_one);
        liner_one.setOnClickListener(this);
        profile_name = (TextView) view.findViewById(R.id.profile_name);
        profile_cover = (ImageView) view.findViewById(R.id.profile_cover);
        profile_nickname = (TextView) view.findViewById(R.id.profile_nickname);
        profile_sign = (TextView) view.findViewById(R.id.profile_sign);
        level_title = (TextView) view.findViewById(R.id.level_title);
        profile_sex = (ImageView) view.findViewById(R.id.profile_sex);
        profile_type = (ImageView) view.findViewById(R.id.profile_type);

        mine_gridview = (PictureGridview) view.findViewById(R.id.mine_gridview);
        mine_gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new ItemMineGridviewAdapter(pics, getActivity());
        mine_gridview.setAdapter(adapter);
        mine_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if("0".equals(empType)){
                    //普通会员
                    switch (i){
                        case 0:
                            Intent recordView = new Intent(getActivity(), MineRecordActivity.class);
                            startActivity(recordView);
                            break;
                        case 1:
                            Intent relateView = new Intent(getActivity(), AndMeAcitvity.class);
                            startActivity(relateView);
                            break;
                        case 2:
                            Intent friendView = new Intent(getActivity(), ContactlistActivity.class);
                            startActivity(friendView);
                            break;
                        case 3:
                            //收藏
                            Intent favourView = new Intent(getActivity(), MineFavoursActivity.class);
                            startActivity(favourView);
                            break;
                        case 4:
                            //订单
                            Intent orderView = new Intent(getActivity(), MineOrdersActivity.class);
                            startActivity(orderView);
                            break;
                        case 5:
                            //购物车
                            Intent cartView = new Intent(getActivity(), MineCartActivity.class);
                            startActivity(cartView);
                            break;
//                        case 6:
//                            //帮助与反馈
//                            break;
                        case 6:
                            //我的收货地址
                            Intent addressView = new Intent(getActivity(), MineAddressActivity.class);
                            startActivity(addressView);
                            break;
                        case 7:
                            //设置
                            Intent setView = new Intent(getActivity(), SetActivity.class);
                            startActivity(setView);
                            break;
                    }
                }
                if("1".equals(empType)){
                    //是管理员
                    switch (i){
                        case 0:
                            Intent reportView = new Intent(getActivity(), ReportActivity.class);
                            startActivity(reportView);
                            break;
                        case 1:
                            Intent pingbiView = new Intent(getActivity(), JinbiActivity.class);
                            startActivity(pingbiView);
                            break;
                        case 2:
                            Intent recordView = new Intent(getActivity(), MineRecordActivity.class);
                            startActivity(recordView);
                            break;
                        case 3:
                            Intent relateView = new Intent(getActivity(), AndMeAcitvity.class);
                            startActivity(relateView);
                            break;
                        case 4:
                            Intent friendView = new Intent(getActivity(), ContactlistActivity.class);
                            startActivity(friendView);
                            break;
                        case 5:
                            //收藏
                            Intent favourView = new Intent(getActivity(), MineFavoursActivity.class);
                            startActivity(favourView);
                            break;
                        case 6:
                            //订单
                            Intent orderView = new Intent(getActivity(), MineOrdersActivity.class);
                            startActivity(orderView);
                            break;
                        case 7:
                            //购物车
                            Intent cartView = new Intent(getActivity(), MineCartActivity.class);
                            startActivity(cartView);
                            break;
//                        case 8:
//                            //帮助与反馈
//                            break;
                        case 8:
                            //我的收货地址
                            Intent addressView = new Intent(getActivity(), MineAddressActivity.class);
                            startActivity(addressView);
                            break;
                        case 9:
                            //设置
                            Intent setView = new Intent(getActivity(), SetActivity.class);
                            startActivity(setView);
                            break;
                    }
                }
                if("2".equals(empType)){
                    //是商家
                    switch (i){
                        case 0:
                            //我的店铺
                            Intent dianpu = new Intent(getActivity(), MineShangpuActivity.class);
                            startActivity(dianpu);
                            break;
                        case 1:
                            Intent mineschoolView = new Intent(getActivity(), MineSchoolsSjActivity.class);
                            startActivity(mineschoolView);
                            break;
                        case 2:
                            Intent recordView = new Intent(getActivity(), MineRecordActivity.class);
                            startActivity(recordView);
                            break;
                        case 3:
                            Intent relateView = new Intent(getActivity(), AndMeAcitvity.class);
                            startActivity(relateView);
                            break;
                        case 4:
                            Intent friendView = new Intent(getActivity(), ContactlistActivity.class);
                            startActivity(friendView);
                            break;
                        case 5:
                            //收藏
                            Intent favourView = new Intent(getActivity(), MineFavoursActivity.class);
                            startActivity(favourView);
                            break;
                        case 6:
                            //订单
                            Intent orderView = new Intent(getActivity(), MineOrdersActivity.class);
                            startActivity(orderView);
                            break;
                        case 7:
                            //购物车
                            Intent cartView = new Intent(getActivity(), MineCartActivity.class);
                            startActivity(cartView);
                            break;
//                        case 8:
//                            //帮助与反馈
//                            break;
                        case 8:
                            //我的收货地址
                            Intent addressView = new Intent(getActivity(), MineAddressActivity.class);
                            startActivity(addressView);
                            break;
                        case 9:
                            //设置
                            Intent setView = new Intent(getActivity(), SetActivity.class);
                            startActivity(setView);
                            break;
                    }
                }
                if("3".equals(empType)){
                    //是代理商
                    switch (i){
                        case 0:
                            Intent mineschool = new Intent(getActivity(), MineSchoolsActivity.class);
                            startActivity(mineschool);
                            break;
                        case 1:
                            Intent shangjia = new Intent(getActivity(), MineShangjiaActivity.class);
                            startActivity(shangjia);
                            break;
                        case 2:
                            Intent tg = new Intent(getActivity(), MineTuiguangActivity.class);
                            startActivity(tg);
                            break;
                        case 3:
                            //pk奖品
                            Intent prizesView = new Intent(getActivity(), PkPrizesActivity.class);
                            startActivity(prizesView);
                            break;
                        case 4:
                            Intent recordView = new Intent(getActivity(), MineRecordActivity.class);
                            startActivity(recordView);
                            break;
                        case 5:
                            Intent relateView = new Intent(getActivity(), AndMeAcitvity.class);
                            startActivity(relateView);
                            break;
                        case 6:
                            Intent friendView = new Intent(getActivity(), ContactlistActivity.class);
                            startActivity(friendView);
                            break;
                        case 7:
                            //收藏
                            Intent favourView = new Intent(getActivity(), MineFavoursActivity.class);
                            startActivity(favourView);
                            break;
                        case 8:
                            //订单
                            Intent orderView = new Intent(getActivity(), MineOrdersActivity.class);
                            startActivity(orderView);
                            break;
                        case 9:
                            //购物车
                            Intent cartView = new Intent(getActivity(), MineCartActivity.class);
                            startActivity(cartView);
                            break;
//                        case 10:
//                            //帮助与反馈
//                            break;
                        case 10:
                            //我的收货地址
                            Intent addressView = new Intent(getActivity(), MineAddressActivity.class);
                            startActivity(addressView);
                            break;
                        case 11:
                            //设置
                            Intent setView = new Intent(getActivity(), SetActivity.class);
                            startActivity(setView);
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.liner_one:
                Intent updateprofile = new Intent(getActivity(), UpdateProfilePersonalActivity.class);
                startActivity(updateprofile);
                break;

        }
    }

    //根据用户UUID获取用户信息
    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_EMP_DETAIL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            EmpDATA data = getGson().fromJson(s, EmpDATA.class);
                            if (data.getCode() == 200) {
                                emp = data.getData();
                                initData();
                            } else {
                                Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
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

    public void initData() {
        empType = emp.getEmpTypeId();
        pics.clear();
        if (empType.equals("1")) {//是管理员
            //显示举报和屏蔽
            pics.add(R.drawable.mine_report_n);
            pics.add(R.drawable.mine_pingbi_n);
        }
        if (empType.equals("2")) {//是商家
            pics.add(R.drawable.mine_dianpu_n);
            pics.add(R.drawable.mine_school_n);
        }
        if (empType.equals("3")) {//是代理商
            //显示我的商家、学校、推广
            pics.add(R.drawable.mine_school_n);
            pics.add(R.drawable.mine_shangjia_n);
            pics.add(R.drawable.mine_tg_n);
            pics.add(R.drawable.mine_pk_n);
        }

        pics.add(R.drawable.mine_record_n);
        pics.add(R.drawable.mine_relate_n);
        pics.add(R.drawable.mine_friends_n);

        pics.add(R.drawable.mine_favour_n);
        pics.add(R.drawable.mine_order_n);
        pics.add(R.drawable.mine_cart_n);


//        pics.add(R.drawable.mine_help_n);
        pics.add(R.drawable.mine_address_n);
        pics.add(R.drawable.mine_set_n);

        adapter.notifyDataSetChanged();

        imageLoader.displayImage(emp.getEmpCover(), profile_cover, UniversityApplication.txOptions, animateFirstListener);
        profile_nickname.setText(emp.getEmpName());
        if ("0".equals(emp.getEmpSex())) {
            profile_sex.setImageResource(R.drawable.icon_sex_male);
        }
        if ("1".equals(emp.getEmpSex())) {
            profile_sex.setImageResource(R.drawable.icon_sex_female);
        }

        level_title.setText(emp.getJfcount());
        level_title.setText(emp.getLevelName());
        if (!StringUtil.isNullOrEmpty(emp.getEmpSign())) {
            profile_sign.setText(emp.getEmpSign());
        } else {
            profile_sign.setText(R.string.sign);
        }

        if (empType.equals("0")) {
            profile_type.setImageResource(R.drawable.icon_type_min);
        }
        if (empType.equals("1")) {
            profile_type.setImageResource(R.drawable.icon_type_guan);
        }
        if (empType.equals("2")) {
            profile_type.setImageResource(R.drawable.icon_type_shang);
        }
        if (empType.equals("3")) {
            profile_type.setImageResource(R.drawable.icon_type_official);
        }
    }

    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.SEND_PIC_TX_SUCCESS)) {
                getData();
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.SEND_PIC_TX_SUCCESS);//设置头像的广播事件
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

}