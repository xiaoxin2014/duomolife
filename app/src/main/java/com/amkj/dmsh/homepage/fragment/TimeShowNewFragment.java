package com.amkj.dmsh.homepage.fragment;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.homepage.adapter.TimeShowPagerAdapter;
import com.amkj.dmsh.homepage.bean.TimeShowShaftEntity;
import com.amkj.dmsh.homepage.bean.TimeShowShaftEntity.TimeShowShaftBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TIME_REFRESH;
import static com.amkj.dmsh.constant.Url.TIME_SHOW_SHAFT;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/4/20
 * class description:新版限时特惠
 */
public class TimeShowNewFragment extends BaseFragment {
    @BindView(R.id.tv_life_back)
    TextView tv_life_back;
    @BindView(R.id.tv_header_title)
    TextView tv_header_title;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    @BindView(R.id.ll_spr_sale)
    LinearLayout ll_spr_sale;
    @BindView(R.id.vp_show_time)
    ViewPager vp_show_time;
    @BindView(R.id.rp_time_spring)
    RadioGroup rp_time_spring;

    private List<TimeShowShaftBean> timeShowBeanList = new ArrayList<>();
    private TimeShowShaftEntity mTimeShowEntity;

    @Override
    protected int getContentView() {
        return R.layout.fragment_time_show_shaft_new;
    }

    @Override
    protected void initViews() {
        tv_life_back.setVisibility(View.GONE);
        tv_header_shared.setVisibility(View.GONE);
        tv_header_title.setText("淘好货");
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
        vp_show_time.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position < rp_time_spring.getChildCount()) {
                    RadioButton radioButton = (RadioButton) rp_time_spring.getChildAt(position);
                    radioButton.setChecked(true);
                }
            }
        });
    }

    @Override
    protected void loadData() {
        getTimeShaft();
    }

    private void getTimeShaft() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), TIME_SHOW_SHAFT, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                mTimeShowEntity = gson.fromJson(result, TimeShowShaftEntity.class);
                List<TimeShowShaftBean> timeShowShaftList = mTimeShowEntity.getTimeShowShaftList();
                if (mTimeShowEntity != null) {
                    if (timeShowShaftList != null && timeShowShaftList.size() > 0) {
                        setTimeShaftIndex(mTimeShowEntity);
                    } else if (ERROR_CODE.equals(mTimeShowEntity.getCode())) {
                        ConstantMethod.showToast(mTimeShowEntity.getCode());
                    }
                }

                NetLoadUtils.getNetInstance().showLoadSir(loadService,timeShowBeanList,mTimeShowEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService,timeShowBeanList,mTimeShowEntity);
            }
        });
    }


    private void setTimeShaftIndex(TimeShowShaftEntity timeShowEntity) {
        timeShowBeanList.clear();
        timeShowBeanList.addAll(timeShowEntity.getTimeShowShaftList());
        int currentPosition = 0;
        for (int i = 0; i < rp_time_spring.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) rp_time_spring.getChildAt(i);
            if (radioButton.isChecked()) {
                currentPosition = i;
                break;
            }
        }
        if (timeShowBeanList.size() > 0) {
            TimeShowPagerAdapter timeShowPagerAdapter = new TimeShowPagerAdapter(getChildFragmentManager(), timeShowBeanList, timeShowEntity.getCurrentTime());
            vp_show_time.setAdapter(timeShowPagerAdapter);
            vp_show_time.setCurrentItem(currentPosition);
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(TIME_REFRESH) && "timeShaft".equals(message.result)) {
            loadData();
        }
    }

    @Override
    public boolean immersionBarEnabled() {
        return true;
    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this).statusBarColor(R.color.colorPrimary).keyboardEnable(true)
                .statusBarDarkFont(true).fitsSystemWindows(true).navigationBarEnable(false).init();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected boolean isLazy() {
        return false;
    }
}
