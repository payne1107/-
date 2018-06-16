package android.sgz.com.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.sgz.com.R;
import android.sgz.com.activity.ContactsActivity;
import android.sgz.com.activity.MineSalaryActivity;
import android.sgz.com.activity.SearchActivity;
import android.sgz.com.activity.WorkDayNumActivity;
import android.sgz.com.activity.WorkOrderActivity;
import android.sgz.com.adapter.FirstFragmentAdapter;
import android.sgz.com.application.MyApplication;
import android.sgz.com.base.BaseFragment;
import android.sgz.com.bean.TopInfoBean;
import android.sgz.com.utils.ConfigUtil;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.zaaach.citypicker.CityPickerActivity;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 92457 on 2018/4/16.
 */

public class Fragment1 extends BaseFragment implements View.OnClickListener {

    private static final int REQUEST_CODE_PICK_CITY = 1;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private EditText etSearch;
    private AutoRelativeLayout tvTitle;
    private AutoLinearLayout layoutWorkDay;
    private AutoLinearLayout layoutFriends;
    private AutoLinearLayout layoutworkOrder;
    private AutoLinearLayout layoutSalary;
    private String city;
    private TextView tvCity;
    private TextView tvWorkDays;
    private TextView tvWorkFriends;
    private TextView tvWorkOrder;
    private TextView tvSalary;


    @Override
    public View onCustomCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment1, null);
        }
        //缓存的rootView需要判断是否已经被加过parent,如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvTitle = (AutoRelativeLayout) mRootView.findViewById(R.id.rl_title);
        etSearch = (EditText) mRootView.findViewById(R.id.et_search);
        tvCity = (TextView) mRootView.findViewById(R.id.activity_city);
        layoutWorkDay = (AutoLinearLayout) mRootView.findViewById(R.id.layout_work_day);
        layoutFriends = (AutoLinearLayout) mRootView.findViewById(R.id.layout_friends);
        layoutworkOrder = (AutoLinearLayout) mRootView.findViewById(R.id.layout_word_order);
        layoutSalary = (AutoLinearLayout) mRootView.findViewById(R.id.layout_salary);
        tvWorkDays = mRootView.findViewById(R.id.tv_work_days);
        tvWorkFriends = mRootView.findViewById(R.id.tv_work_friends);
        tvWorkOrder = mRootView.findViewById(R.id.tv_work_order);
        tvSalary = mRootView.findViewById(R.id.tv_salary);

        viewPager = (ViewPager) mRootView.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) mRootView.findViewById(R.id.tabLayout);
        FirstFragmentAdapter adapter = new FirstFragmentAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        setListener();
        initData();
    }

    /***
     * 初始化数据
     */
    private void initData() {
        initLocation();
        queryTopInfo();
    }

    /****
     * 设置监听器
     */
    private void setListener() {
        tvTitle.setOnClickListener(this);
        layoutWorkDay.setOnClickListener(this);
        layoutFriends.setOnClickListener(this);
        layoutworkOrder.setOnClickListener(this);
        layoutSalary.setOnClickListener(this);
        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    startActivity(new Intent(getActivity(), SearchActivity.class));
                    etSearch.clearFocus();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_work_day:
                startActivity(new Intent(getActivity(), WorkDayNumActivity.class));
                break;
            case R.id.layout_friends:
                startActivity(new Intent(getActivity(), ContactsActivity.class));
                break;
            case R.id.layout_word_order:
                //工单
                startActivity(new Intent(getActivity(), WorkOrderActivity.class));
                break;
            case R.id.layout_salary:
                startActivity(new Intent(getActivity(), MineSalaryActivity.class));
                break;
            case R.id.rl_title:
                startLocationActivity();
                break;
        }
    }

    /****
     * 跳转到选择城市页面
     */
    private void startLocationActivity() {
        startActivityForResult(new Intent(getActivity(),CityPickerActivity.class),REQUEST_CODE_PICK_CITY);
    }


    private void initLocation() {
        AMapLocationClient mLocationClient = new AMapLocationClient(getActivity());
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocation(true);
        mLocationClient.setLocationOption(option);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //登陆之后记录经纬度
                        MyApplication.currentLat = aMapLocation.getLatitude();
                        MyApplication.currentLon = aMapLocation.getLongitude();
                        MyApplication.currentArea = aMapLocation.getAddress();
                        city = aMapLocation.getCity();
                        tvCity.setText(city);
                        Log.d("Dong", "定位成功-----》" + city);
                    } else {
                        //定位失败
                        city = "合肥市";
                        tvCity.setText(city);
                    }
                }
            }
        });
        mLocationClient.startLocation();
    }

    /****
     * 获取工友 工资等信息
     */
    private void queryTopInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("random", "123");
        httpPostRequest(ConfigUtil.QUERY_INDEX_DATA_URL, params, ConfigUtil.QUERY_INDEX_DATA_URL_ACTION);
    }

    @Override
    public void httpOnResponse(String json, int action) {
        super.httpOnResponse(json, action);
        switch (action) {
            case ConfigUtil.QUERY_INDEX_DATA_URL_ACTION:
                handlerQueryTopInfo(json);
            break;
        }
    }

    /****
     * 顶部数据处理
     * @param json
     */
    private void handlerQueryTopInfo(String json) {
        Log.d("Dong", "顶部数据处理----->" + json);
        TopInfoBean topInfoBean = JSON.parseObject(json, TopInfoBean.class);
        if (topInfoBean != null) {
            TopInfoBean.DataBean data = topInfoBean.getData();
            if (data != null) {
                int workDays = data.getWorkdays();
                int workFriends = data.getWorkfriends();
                int projectCount = data.getProjectcount();
                double salaryTotal = data.getIncome();//工资
                tvSalary.setText(salaryTotal+"元");
                tvWorkDays.setText(workDays+"天");
                tvWorkFriends.setText(workFriends + "人");
                tvWorkOrder.setText(projectCount+"个");
            }
        }
    }
}

