package com.amkj.dmsh.homepage.fragment;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.homepage.adapter.TimeShowPagerAdapter;
import com.amkj.dmsh.homepage.bean.TimeShowEntity;
import com.amkj.dmsh.homepage.bean.TimeShowEntity.TimeShowBean;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.views.SystemBarHelper;
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

    private List<TimeShowBean> timeShowBeanList = new ArrayList<>();
    private float screenWidth;

    @Override
    protected int getContentView() {
        return R.layout.fragment_time_show_shaft_new;
    }

    @Override
    protected void initViews() {
        setStatusColor();
        tv_life_back.setVisibility(View.GONE);
        tv_header_shared.setVisibility(View.GONE);
        tv_header_title.setText("限时特惠");
        if (screenWidth == 0) {
            BaseApplication app = (BaseApplication) getActivity().getApplication();
            screenWidth = app.getScreenWidth() / 5f;
        }
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

    private void setStatusColor() {
        SystemBarHelper.setPadding(getActivity(), ll_spr_sale);
        SystemBarHelper.immersiveStatusBar(getActivity());
    }

    @Override
    protected void loadData() {
        getTimeShaft();
    }

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
                    showToast(getActivity(), msg);
                }
                setTimeLoadError();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                setTimeLoadError();
            }
        });
    }

    private void setTimeLoadError() {
        if (timeShowBeanList.size() < 1) {
            showToast(getActivity(), R.string.unConnectedNetwork);
        }
    }

    private void setTimeShaft(TimeShowEntity timeShowEntity) {
        if (timeShowBeanList.size() > 0) {
            boolean isRefresh = false;
            if (timeShowBeanList.size() == timeShowEntity.getTimeShowBeanList().size()) {
                for (int i = 0; i < timeShowEntity.getTimeShowBeanList().size(); i++) {
                    TimeShowBean timeShowBean = timeShowEntity.getTimeShowBeanList().get(i);
                    TimeShowBean timeShowOldBean = timeShowBeanList.get(i);
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

    private void setTimeShaftIndex(TimeShowEntity timeShowEntity) {
        timeShowBeanList.clear();
        timeShowBeanList.addAll(timeShowEntity.getTimeShowBeanList());
        if (timeShowBeanList.size() > 0) {
            TimeShowPagerAdapter timeShowPagerAdapter = new TimeShowPagerAdapter(getChildFragmentManager(), timeShowBeanList);
            vp_show_time.setAdapter(timeShowPagerAdapter);
            vp_show_time.setCurrentItem(0);
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(TIME_REFRESH)) {
            loadData();
        }
    }

    @OnClick(R.id.ll_spr_sale)
    public void refreshTimeShaft() {
        if (timeShowBeanList.size() < 1) {
            if (loadHud != null) {
                loadHud.show();
            }
            getTimeShaft();
        }
    }
}
