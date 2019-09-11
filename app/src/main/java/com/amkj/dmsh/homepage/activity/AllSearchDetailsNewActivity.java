package com.amkj.dmsh.homepage.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
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
import com.amkj.dmsh.homepage.ListHistoryDataSave;
import com.amkj.dmsh.homepage.adapter.SearchTabPageAdapter;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.homepage.activity.HomePageSearchActivity.SEARCH_DATA;

;

public class AllSearchDetailsNewActivity extends BaseActivity {
    @BindView(R.id.et_search_input)
    EditText et_search_input;
    @BindView(R.id.sliding_search_bar)
    SlidingTabLayout sliding_search_bar;
    @BindView(R.id.vp_search_details_container)
    ViewPager vp_search_details_container;
    private String searchDate;
    private LinkedList<String> dataHistoryList = new LinkedList<>();
    private String SAVE_TYPE = "allSearch";
    private String SAVE_NAME = "SearchHistory";
    private Map<String, String> params;

    @Override
    protected int getContentView() {
        return R.layout.activity_search_details;
    }

    @Override
    protected void initViews() {
        et_search_input.setFocusableInTouchMode(true);
        Intent intent = getIntent();
        searchDate = intent.getStringExtra(SEARCH_DATA);
        String keyWord = getStrings(searchDate);
        et_search_input.setText(keyWord);
        et_search_input.setSelection(keyWord.length());
        params = new HashMap<>();
        params.put(SEARCH_DATA, searchDate);
//        插入历史记录
        insertHistoryData(searchDate);
        sliding_search_bar.setTextsize(AutoSizeUtils.mm2px(mAppContext, 26));
        sliding_search_bar.setIndicatorHeight(AutoSizeUtils.mm2px(mAppContext, 2));
        SearchTabPageAdapter searchTabPageAdapter = new SearchTabPageAdapter(getSupportFragmentManager(), params);
        vp_search_details_container.setAdapter(searchTabPageAdapter);
        sliding_search_bar.setViewPager(vp_search_details_container);
        // 搜索框的键盘搜索键点击回调
        et_search_input.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText view = (EditText) v;
                searchDate = view.getText().toString().trim();
                if (keyCode == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                    // 先隐藏键盘
                    KeyboardUtils.hideSoftInput(AllSearchDetailsNewActivity.this);
                    //跳转页面，模糊搜索
                    if (!TextUtils.isEmpty(searchDate)) {
                        params.put(SEARCH_DATA, searchDate);
                        EventBus.getDefault().post(new EventMessage(SEARCH_DATA, searchDate));
                        insertHistoryData(searchDate);
                    } else {
                        showToast(AllSearchDetailsNewActivity.this, "你输入为空，请重新输入！");
                    }
                }
                return false;
            }
        });
        KeyboardUtils.registerSoftInputChangedListener(this, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if (height == 0) {
                    ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).requestFocus();
                }
            }
        });
    }

    private void insertHistoryData(String text) {
        if (!TextUtils.isEmpty(text)) {
            dataHistoryList.clear();
            ListHistoryDataSave listHistoryDataSave = new ListHistoryDataSave(AllSearchDetailsNewActivity.this, SAVE_NAME);
            dataHistoryList.addAll(listHistoryDataSave.<String>getDataList(SAVE_TYPE));
            if (dataHistoryList.size() > 0) {
                Iterator iterator = dataHistoryList.listIterator();
                boolean hasNext = iterator.hasNext();
                while (hasNext) {
                    String str = (String) iterator.next();
                    if (str.equals(text)) {
                        dataHistoryList.remove(text);
                        dataHistoryList.addFirst(text);
                        break;
                    }
                    hasNext = iterator.hasNext();
                    if (!hasNext) {
                        dataHistoryList.addFirst(text);
                    }
                }
            } else {
                dataHistoryList.addFirst(text);
            }
            listHistoryDataSave.setDataList(SAVE_TYPE, dataHistoryList);
        } else {
            showToast(this, "请输入搜索内容");
        }
    }

    @Override
    protected void loadData() {
    }

    @OnClick(R.id.tv_home_search_cancel)
    void cancel(View view) {
        finish();
    }

    /**
     * 点击编辑器外区域隐藏键盘 避免点击搜索完没有隐藏键盘
     */
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
        return super.dispatchTouchEvent(ev);
    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
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
}
