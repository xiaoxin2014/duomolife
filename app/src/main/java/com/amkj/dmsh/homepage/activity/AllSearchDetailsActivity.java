package com.amkj.dmsh.homepage.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragmentActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.homepage.ListHistoryDataSave;
import com.amkj.dmsh.homepage.adapter.SearchTabPageAdapter;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zhy.autolayout.utils.AutoUtils;

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

;

public class AllSearchDetailsActivity extends BaseFragmentActivity {
    @BindView(R.id.et_search_input)
    EditText et_search_input;
    @BindView(R.id.sliding_search_bar)
    SlidingTabLayout sliding_search_bar;
    @BindView(R.id.vp_search_details_container)
    ViewPager vp_search_details_container;
    private String data;
    private SearchTabPageAdapter searchTabPageAdapter;
    private String searchText = "";
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
        data = intent.getStringExtra("data");
        String keyWord = getStrings(data);
        et_search_input.setText(keyWord);
        et_search_input.setSelection(keyWord.length());
        searchText = data;
        params = new HashMap<>();
        params.put("data", data);
//        插入历史记录
        insertHistoryData(data);
        sliding_search_bar.setTextsize(AutoSizeUtils.mm2px(mAppContext,26));
        sliding_search_bar.setIndicatorHeight(AutoUtils.getPercentHeight1px() * 2);
        searchTabPageAdapter = new SearchTabPageAdapter(getSupportFragmentManager(), params);
        vp_search_details_container.setAdapter(searchTabPageAdapter);
        sliding_search_bar.setViewPager(vp_search_details_container);
        // 搜索框的键盘搜索键点击回调
        et_search_input.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText view = (EditText) v;
                searchText = view.getText().toString().trim();
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //跳转页面，模糊搜索
                    if (!TextUtils.isEmpty(searchText)) {
                        int currentTab = sliding_search_bar.getCurrentTab();
                        EventBus.getDefault().post(new EventMessage("search" + currentTab, searchText));
                        insertHistoryData(searchText);
                    } else {
                        showToast(AllSearchDetailsActivity.this, "你输入为空，请重新输入！");
                    }
                }
                return false;
            }
        });
        sliding_search_bar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                EventBus.getDefault().post(new EventMessage("search" + position, searchText));
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private void insertHistoryData(String text) {
        if (!TextUtils.isEmpty(text)) {
            dataHistoryList.clear();
            ListHistoryDataSave listHistoryDataSave = new ListHistoryDataSave(AllSearchDetailsActivity.this, SAVE_NAME);
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
}
