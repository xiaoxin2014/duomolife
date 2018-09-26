package com.amkj.dmsh.homepage.activity;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.homepage.adapter.TimeShowPagerAdapter;
import com.amkj.dmsh.homepage.bean.TimeShowShaftEntity;
import com.amkj.dmsh.homepage.bean.TimeShowShaftEntity.TimeShowShaftBean;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.google.gson.Gson;

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
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_title;
    @BindView(R.id.ll_spr_sale)
    LinearLayout ll_spr_sale;
    @BindView(R.id.vp_show_time)
    ViewPager vp_show_time;
    @BindView(R.id.rp_time_spring)
    RadioGroup rp_time_spring;

    private List<TimeShowShaftBean> timeShowBeanList = new ArrayList();

    @Override
    protected int getContentView() {
        return R.layout.activity_time_show_shaft_new;
    }
    @Override
    protected void initViews() {
        tv_header_shared.setVisibility(View.GONE);
        tv_header_title.setText("限时特惠");
        rp_time_spring.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_time_spring_open_group) {
                    vp_show_time.setCurrentItem(0);
                } else {
                    vp_show_time.setCurrentItem(1);
                }
            }
        });
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
                    TimeShowShaftEntity timeShowEntity = gson.fromJson(result, TimeShowShaftEntity.class);
                    if (timeShowEntity != null) {
                        setTimeShaft(timeShowEntity);
                    }
                } else if (!code.equals("02")) {
                    showToast(TimeShowNewActivity.this, msg);
                }
            }
        });
    }

    private void setTimeShaft(TimeShowShaftEntity timeShowEntity) {
        if (timeShowBeanList.size() > 0) {
            boolean isRefresh = false;
            if (timeShowBeanList.size() == timeShowEntity.getTimeShowShaftList().size()) {
                for (int i = 0; i < timeShowEntity.getTimeShowShaftList().size(); i++) {
                    TimeShowShaftBean timeShowBean = timeShowEntity.getTimeShowShaftList().get(i);
                    TimeShowShaftBean timeShowOldBean = timeShowBeanList.get(i);
                    if (!timeShowBean.getDate().equals(timeShowOldBean.getDate())
                            || timeShowBean.getType() != timeShowOldBean.getType()) {
                        isRefresh = true;
                        break;
                    }
                }
            }
            if (isRefresh) {
                setTimeShaftIndex(timeShowEntity);
            }
        } else {
            setTimeShaftIndex(timeShowEntity);
        }
    }

    private void setTimeShaftIndex(TimeShowShaftEntity timeShowEntity) {
        timeShowBeanList.clear();
        timeShowBeanList.addAll(timeShowEntity.getTimeShowShaftList());
        int currentPosition = 0;
        for (int i = 0; i < rp_time_spring.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) rp_time_spring.getChildAt(i);
            if(radioButton.isChecked()){
                currentPosition = i;
                break;
            }
        }
        if (timeShowBeanList.size() > 0) {
            TimeShowPagerAdapter timeShowPagerAdapter = new TimeShowPagerAdapter(getSupportFragmentManager(), timeShowBeanList,timeShowEntity.getSystemTime());
            vp_show_time.setAdapter(timeShowPagerAdapter);
            vp_show_time.setCurrentItem(currentPosition);
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if(message.type.equals(TIME_REFRESH)&&"timeShaft".equals(message.result)){
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
