package com.amkj.dmsh.homepage.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.HotSearchTagEntity;
import com.amkj.dmsh.homepage.ListHistoryDataSave;
import com.amkj.dmsh.homepage.adapter.HotSearchAdapter;
import com.amkj.dmsh.shopdetails.activity.IndentSearchDetailsActivity;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_DATA;


/**
 * Created by atd48 on 2016/6/24.
 */
public class IndentSearchActivity extends BaseActivity {
    @BindView(R.id.et_search_input)
    public EditText et_search_input;
    //    最近搜索
    @BindView(R.id.ll_search_history)
    public LinearLayout ll_search_history;
    @BindView(R.id.rv_history_search)
    RecyclerView rvHistorySearch;
    private String SAVE_NAME = "SearchHistory";
    private String searchType = "indentSearch";
    protected HotSearchAdapter historySearchAdapter;
    protected List<HotSearchTagEntity.HotSearchTagBean> historySearchList = new ArrayList<>();
    private LinkedList<String> dataHistoryList = new LinkedList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_search;
    }

    @Override
    protected void initViews() {
        et_search_input.setHint("搜索订单");
        historySearchAdapter = new HotSearchAdapter(historySearchList);
        rvHistorySearch.setNestedScrollingEnabled(false);
        FlexboxLayoutManager manager = new FlexboxLayoutManager(getActivity());
        manager.setFlexWrap(FlexWrap.WRAP);
        rvHistorySearch.setLayoutManager(manager);
        rvHistorySearch.setAdapter(historySearchAdapter);
        historySearchAdapter.setOnItemClickListener((adapter, view, position) -> {
            HotSearchTagEntity.HotSearchTagBean hotSearchTagBean = (HotSearchTagEntity.HotSearchTagBean) view.getTag();
            if (hotSearchTagBean != null) {
                if (!TextUtils.isEmpty(hotSearchTagBean.getAndroid_link())) {
                    setSkipPath(getActivity(), hotSearchTagBean.getAndroid_link(), false);
                } else if (!TextUtils.isEmpty(hotSearchTagBean.getTag_name())) {
                    getTagResult(hotSearchTagBean.getTag_name());
                }
            }
        });
        // 搜索框的键盘搜索键点击回调
        et_search_input.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText view = (EditText) v;
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                    // 先隐藏键盘
                    KeyboardUtils.hideSoftInput(IndentSearchActivity.this);
                    //跳转页面，模糊搜索
                    skipDetailsPage(view);
                }
                return false;
            }
        });
        getHistoryData();
        KeyboardUtils.registerSoftInputChangedListener(this, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if (height == 0) {
                    ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).requestFocus();
                }
            }
        });
    }

    private void getTagResult(String cate) {
        if (!TextUtils.isEmpty(cate)) {
            Intent intent = new Intent();
            intent.setClass(IndentSearchActivity.this, IndentSearchDetailsActivity.class);
            intent.putExtra(SEARCH_DATA, cate);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
            // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在提前
//        插入数据
            insertHistoryData(cate);
        } else {
            showToast(this, "请输入关键词");
        }
    }

    private void insertHistoryData(String text) {
        dataHistoryList.clear();
        ListHistoryDataSave listHistoryDataSave = new ListHistoryDataSave(IndentSearchActivity.this, SAVE_NAME);
        dataHistoryList.addAll(listHistoryDataSave.<String>getDataList(searchType));
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
        listHistoryDataSave.setDataList(searchType, dataHistoryList);
        getHistoryData();
    }

    //获取历史搜索
    private void getHistoryData() {
        historySearchList.clear();
        try {
            ListHistoryDataSave listHistoryDataSave = new ListHistoryDataSave(getActivity(), SAVE_NAME);
            List<String> dataList = listHistoryDataSave.getDataList(searchType);
            if (dataList != null && dataList.size() > 0) {
                for (String keyword : dataList) {
                    historySearchList.add(new HotSearchTagEntity.HotSearchTagBean(keyword));
                }
            }
            historySearchAdapter.notifyDataSetChanged();
            ll_search_history.setVisibility(historySearchList.size() > 0 ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void skipDetailsPage(EditText view) {
        if (view.getText().toString().trim().length() > 0) {
            String cate = view.getText().toString().trim();
            getTagResult(cate);
        } else {
            showToast(IndentSearchActivity.this, "你输入为空，请重新输入！");
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("refresh")) {
            getTagResult((String) message.result);
        }
    }

    @OnClick(R.id.tv_home_search_cancel)
    void goBack(View view) {
        finish();
    }

    //  清除历史记录
    @OnClick(R.id.tv_search_history_hint)
    void deleteHistoryData(View view) {
        ListHistoryDataSave listHistoryDataSave = new ListHistoryDataSave(IndentSearchActivity.this, SAVE_NAME);
        listHistoryDataSave.delDataList(searchType);
        getHistoryData();
    }

    @Override
    protected void loadData() {
    }

    @OnClick(R.id.tv_home_search_cancel)
    void cancel(View view) {
        finish();
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
