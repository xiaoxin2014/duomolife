package com.amkj.dmsh.shopdetails.activity;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.adapter.IndentPagerAdapter;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;
import com.amkj.dmsh.views.flycoTablayout.listener.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_DATA;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_WAITAPPRAISE_ICON;

/**
 * Created by xiaoxin on 2020/3/20
 * Version:v4.5.0
 * ClassDescription :订单列表+订单搜索结果页面合并
 */
public class DoMoIndentAllActivity extends BaseActivity {
    @BindView(R.id.communal_stl_tab)
    SlidingTabLayout communal_stl_tab;
    @BindView(R.id.vp_indent_container)
    ViewPager vp_indent_container;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    private String type = "";
    private int waitEvaluateNum;
    private IndentPagerAdapter indentPagerAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_duomo_indent_all;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        Intent intent = getIntent();
        type = intent.getStringExtra("tab");
        if (TextUtils.isEmpty(type)) {
            type = "all";
        }
        setIndentTabData();
    }

    private void setIndentTabData() {
        if (userId < 1) {
            return;
        }

        communal_stl_tab.setTextsize(AutoSizeUtils.mm2px(mAppContext, 28));
        communal_stl_tab.setTextUnselectColor(getResources().getColor(R.color.text_login_gray_s));
        indentPagerAdapter = new IndentPagerAdapter(getSupportFragmentManager());
        vp_indent_container.setAdapter(indentPagerAdapter);
        vp_indent_container.setOffscreenPageLimit(4);
        communal_stl_tab.setViewPager(vp_indent_container);
        communal_stl_tab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vp_indent_container.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        if (!TextUtils.isEmpty(type)) {
            int position;
            switch (type) {
                case "waitPay":
                    position = 1;
                    break;
                case "waitSend":
                    position = 2;
                    break;
                case "delivered":
                    position = 3;
                    break;
                case "appraise":
                    position = 4;
                    break;
                default:
                    position = 0;
                    break;
            }
            communal_stl_tab.setCurrentTab(position);
        } else {
            communal_stl_tab.setCurrentTab(0);
        }

        //待评价商品数量
        waitEvaluateNum = getIntent().getIntExtra("waitEvaluateNum", 0);
        if (waitEvaluateNum > 0) {
            communal_stl_tab.showMsg(4, "有奖励");
        }

        // 搜索框的键盘搜索键点击回调
        mEtSearch.setFocusableInTouchMode(true);
        mEtSearch.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText view = (EditText) v;
                String keyWord = view.getText().toString().trim();
                if (keyCode == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                    // 先隐藏键盘
                    KeyboardUtils.hideSoftInput(getActivity());
                    //待评价商品不支持搜索
                    if (communal_stl_tab.getCurrentTab() == 4) {
                        communal_stl_tab.setCurrentTab(0);
                    }
                    //跳转页面，模糊搜索
                    EventBus.getDefault().post(new EventMessage(SEARCH_DATA, keyWord));
                }
                return false;
            }
        });
        KeyboardUtils.registerSoftInputChangedListener(this, height -> {
            if (height == 0) {
                ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).requestFocus();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            setIndentTabData();
        }
    }

    @Override
    protected void loadData() {
    }

    @OnClick(R.id.tv_indent_back)
    void goBack(View view) {
        finish();
    }

    @OnClick(R.id.iv_indent_service)
    void skipService() {
        QyServiceUtils.getQyInstance()
                .openQyServiceChat(DoMoIndentAllActivity.this
                        , "订单列表", "");
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        //更新待评价角标
        if (UPDATE_WAITAPPRAISE_ICON.equals(message.type)) {
            if (((int) message.result) > 0) {
                communal_stl_tab.showMsg(4, "有奖励");
            } else {
                communal_stl_tab.hideMsg(4);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v instanceof EditText) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.unregisterSoftInputChangedListener(this);
    }

    public String getKewords() {
        return mEtSearch.getText().toString().trim();
    }
}
