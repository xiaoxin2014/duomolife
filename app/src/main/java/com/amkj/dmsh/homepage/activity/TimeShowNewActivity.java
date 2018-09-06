package com.amkj.dmsh.homepage.activity;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.homepage.adapter.TimeShowPagerAdapter;
import com.amkj.dmsh.homepage.bean.TabDoubleEntity;
import com.amkj.dmsh.homepage.bean.TimeShowEntity;
import com.amkj.dmsh.homepage.bean.TimeShowEntity.TimeShowBean;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.flyco.tablayout.SlidingTabLayoutDouble;
import com.flyco.tablayout.listener.CustomTabDoubleEntity;
import com.google.gson.Gson;
import com.zhy.autolayout.utils.AutoUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.TIME_REFRESH;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/4/20
 * class description:新版限时特惠
 */
public class TimeShowNewActivity extends BaseActivity {
    @BindView(R.id.vp_show_time)
    ViewPager vp_show_time;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_title;
    @BindView(R.id.std_time_shaft)
    SlidingTabLayoutDouble std_time_shaft;

    private List<TimeShowBean> timeShowBeanList = new ArrayList<>();
    private List<CustomTabDoubleEntity> customTabDoubleList = new ArrayList<>();
    private float screenWidth;

    @Override
    protected int getContentView() {
        return R.layout.activity_time_show_shaft_new;
    }
    @Override
    protected void initViews() {
        tv_header_shared.setVisibility(View.GONE);
        tv_header_title.setText("限时特惠");
    }

    @Override
    protected void loadData() {
        getTimeShaft();
    }

    /**
     * 访问网络 获取时间轴
     */
    private void getTimeShaft() {
        String url = Url.BASE_URL + Url.TIME_SHOW_SHAFT;
        XUtil.Get(url, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                String code = "";
                String msg = "";
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = (String) jsonObject.get("code");
                    msg = (String) jsonObject.get("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code.equals("01")) {
                    Gson gson = new Gson();
                    TimeShowEntity timeShowEntity = gson.fromJson(result, TimeShowEntity.class);
                    if (timeShowEntity != null) {
                        setTimeShaft(timeShowEntity);
                    }
                } else if (!code.equals("02")) {
                    showToast(TimeShowNewActivity.this, msg);
                }
            }
        });
    }

    private void setTimeShaft(TimeShowEntity timeShowEntity) {
        if(timeShowBeanList.size()>0){
            boolean isRefresh = false;
            if(timeShowBeanList.size() == timeShowEntity.getTimeShowBeanList().size()){
                for (int i = 0; i < timeShowEntity.getTimeShowBeanList().size(); i++) {
                    TimeShowBean timeShowBean = timeShowEntity.getTimeShowBeanList().get(i);
                    TimeShowBean timeShowOldBean = timeShowBeanList.get(i);
                    if(!timeShowBean.getDate().equals(timeShowOldBean.getDate())
                            ||timeShowBean.getType() != timeShowOldBean.getType()){
                        isRefresh = true;
                        break;
                    }
                }
            }
            if(isRefresh){
                setTimeShaftIndex(timeShowEntity);
            }
        }else{
            setTimeShaftIndex(timeShowEntity);
        }
    }

    private void setTimeShaftIndex(TimeShowEntity timeShowEntity) {
        timeShowBeanList.clear();
        customTabDoubleList.clear();
        int index = 0;
        TabDoubleEntity tabDoubleEntity;
        for (int i = 0; i < timeShowEntity.getTimeShowBeanList().size(); i++) {
            TimeShowBean timeShowBean = timeShowEntity.getTimeShowBeanList().get(i);
            if (timeShowBean.getType() == 1) {
                index = i;
            }
            timeShowBeanList.add(timeShowBean);
            tabDoubleEntity = new TabDoubleEntity();
            tabDoubleEntity.setTabTitle(timeShowBean.getName());
            tabDoubleEntity.setTabSubTitle(timeShowBean.getType() == 2 ? "即将开始" : "抢购中");
            customTabDoubleList.add(tabDoubleEntity);
        }
        TimeShowPagerAdapter timeShowPagerAdapter = new TimeShowPagerAdapter(getSupportFragmentManager(), timeShowBeanList);
        vp_show_time.setAdapter(timeShowPagerAdapter);
        if (screenWidth == 0) {
            BaseApplication app = (BaseApplication) getApplication();
            screenWidth = app.getScreenWidth() / 5f;
        }
        std_time_shaft.setTabWidth(screenWidth);
        std_time_shaft.setTextsize(AutoUtils.getPercentHeightSize(30));
        std_time_shaft.setSubTextsize(AutoUtils.getPercentHeightSize(22));
        std_time_shaft.setViewPager(vp_show_time, customTabDoubleList);
        vp_show_time.setCurrentItem(index);
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if(message.type.equals(TIME_REFRESH)){
            loadData();
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnClick(R.id.ll_spr_sale)
    public void refreshTimeShaft(){
        if(timeShowBeanList.size()<1){
            if(loadHud!=null){
                loadHud.show();
            }
            getTimeShaft();
        }
    }

}
